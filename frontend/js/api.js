const API_HOST = window.location.hostname
  ? `${window.location.protocol}//${window.location.hostname}:8080`
  : 'http://localhost:8080';
const FALLBACK_API_HOST = 'http://localhost:8080';
const BASE_URL = `${API_HOST}/api`;
const FALLBACK_BASE_URL = `${FALLBACK_API_HOST}/api`;

const api = {
  getToken() {
    return localStorage.getItem('accessToken');
  },

  setToken(token) {
    if (token) {
      localStorage.setItem('accessToken', token);
    } else {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
    }
  },

  getApiOrigin() {
    return API_HOST;
  },

  async parseResponse(response) {
    const text = await response.text();
    if (!text) return null;
    try {
      return JSON.parse(text);
    } catch (_) {
      return { rawText: text };
    }
  },

  async fetchWithFallback(endpoint, config) {
    try {
      return await fetch(`${BASE_URL}${endpoint}`, config);
    } catch (error) {
      const shouldFallback = API_HOST !== FALLBACK_API_HOST
        && error instanceof TypeError
        && String(error.message || '').toLowerCase().includes('fetch');

      if (!shouldFallback) {
        throw error;
      }
      return fetch(`${FALLBACK_BASE_URL}${endpoint}`, config);
    }
  },

  async request(endpoint, method = 'GET', body = null, options = {}) {
    const { redirectOn401 = true } = options;
    const headers = {
      'Content-Type': 'application/json',
    };

    const token = this.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
      method,
      headers,
    };

    if (body) {
      config.body = JSON.stringify(body);
    }

    const response = await this.fetchWithFallback(endpoint, config);
    const data = await this.parseResponse(response);

    if (!response.ok) {
      if (response.status === 401 && redirectOn401) {
        this.setToken(null);
        if (!window.location.pathname.includes('login.html')) {
          window.location.href = 'login.html';
        }
      }
      throw new Error(data?.message || data?.rawText || `Loi HTTP: ${response.status}`);
    }

    return data;
  },

  // tải ảnh lên
  async requestFormData(endpoint, method = 'POST', formData, options = {}) {
    const { redirectOn401 = true } = options;
    const headers = {};
    const token = this.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
      method,
      headers,
      body: formData,
    };

    const response = await this.fetchWithFallback(endpoint, config);
    const data = await this.parseResponse(response);

    if (!response.ok) {
      if (response.status === 401 && redirectOn401) {
        this.setToken(null);
        if (!window.location.pathname.includes('login.html')) {
          window.location.href = 'login.html';
        }
      }
      throw new Error(data?.message || data?.rawText || `Loi HTTP: ${response.status}`);
    }

    return data;
  },

  auth: {
    login: (credentials) => api.request('/auth/login', 'POST', credentials),
    register: (data) => api.request('/auth/register', 'POST', data),
  },

  product: {
    getAll: (page = 0, size = 12, keyword = '') => {
      const qs = keyword ? `&keyword=${encodeURIComponent(keyword)}` : '';
      return api.request(`/products?page=${page}&size=${size}${qs}`);
    },
    getById: (id) => api.request(`/products/${id}`),
    getByCategory: (categoryId, page = 0, size = 12) => {
      return api.request(`/products/category/${categoryId}?page=${page}&size=${size}`);
    },
    create: (data) => api.request('/products', 'POST', data),
    update: (id, data) => api.request(`/products/${id}`, 'PUT', data),
    delete: (id) => api.request(`/products/${id}`, 'DELETE'),
    uploadImage: (file) => {
      const formData = new FormData();
      formData.append('file', file);
      return api.requestFormData('/products/upload-image', 'POST', formData, { redirectOn401: false });
    },
  },

  category: {
    getAll: () => api.request('/categories'),
    getById: (id) => api.request(`/categories/${id}`),
    create: (data) => api.request('/categories', 'POST', data),
    update: (id, data) => api.request(`/categories/${id}`, 'PUT', data),
    delete: (id) => api.request(`/categories/${id}`, 'DELETE'),
  },

  user: {
    getMe: () => api.request('/users/me'),
    updateMe: (data) => api.request('/users/me', 'PUT', data),
    getAll: () => api.request('/users'),
    getById: (id) => api.request(`/users/${id}`),
    update: (id, data) => api.request(`/users/${id}`, 'PUT', data),
    changeRole: (id, role) => api.request(`/users/${id}/role`, 'PATCH', { role }),
    delete: (id) => api.request(`/users/${id}`, 'DELETE'),
  },

  cart: {
    getCart: () => api.request('/cart/me'),
    addItem: (productId, quantity) => api.request('/cart/me/items', 'POST', { productId, quantity }),
    updateItem: (cartItemId, productId, quantity) => api.request(`/cart/me/items/${cartItemId}`, 'PUT', { productId, quantity }),
    removeItem: (cartItemId) => api.request(`/cart/me/items/${cartItemId}`, 'DELETE'),
    clear: () => api.request('/cart/me/clear', 'DELETE'),
  },

  order: {
    createOrder: (data) => api.request('/orders/me', 'POST', data),
    getMyOrders: () => api.request('/orders/me'),
    getOrderById: (id) => api.request(`/orders/me/${id}`),
    cancelMyOrder: (id) => api.request(`/orders/me/${id}/cancel`, 'PATCH'),
    getAll: () => api.request('/orders'),
    getAdminOrderById: (id) => api.request(`/orders/${id}`),
    updateStatus: (id, status) => api.request(`/orders/${id}/status?status=${status}`, 'PUT'),
  },
};

window.api = api;
