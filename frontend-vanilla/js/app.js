// Hiển thị Toast Notification
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `glass-card toast ${type}`;
  toast.innerText = message;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = 'slideInRight 0.3s ease reverse forwards';
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
};

function ensureNavbarMount() {
  let navbar = document.getElementById('app-navbar');
  if (!navbar) {
    navbar = document.createElement('nav');
    navbar.id = 'app-navbar';
    navbar.className = 'navbar';
    document.body.prepend(navbar);
  }
  return navbar;
}

function resolveDisplayName(user) {
  if (!user) return '';
  if (user.fullName && user.fullName.trim()) {
    return user.fullName.trim().split(' ').pop();
  }
  if (user.email && user.email.includes('@')) {
    return user.email.split('@')[0];
  }
  return 'User';
}

// Khởi tạo layout chung (Navbar)
async function renderNavbar() {
  const navbar = ensureNavbarMount();

  const token = localStorage.getItem('accessToken');
  let user = null;
  let cartItemCount = 0;

  if (token) {
    user = JSON.parse(localStorage.getItem('user'));
    // Thử fetch profile nếu chưa có
    if (!user) {
      try {
        const res = await api.user.getMe();
        user = res.data;
        localStorage.setItem('user', JSON.stringify(user));
      } catch (e) {
        console.error('Failed to load user', e);
        api.setToken(null);
        user = null;
      }
    }
    
    // Đếm số lượng giỏ hàng
    try {
      const cartRes = await api.cart.getCart();
      if (cartRes.data && cartRes.data.cartItems) {
        cartItemCount = cartRes.data.cartItems.reduce((acc, item) => acc + item.quantity, 0);
      }
    } catch (e) {
      console.error('Failed to load cart', e);
    }
  }

  const displayName = resolveDisplayName(user);

  navbar.innerHTML = `
    <div class="container">
      <a href="index.html" class="nav-brand">⚡ ShopVN</a>
      <div class="nav-links">
        <a href="index.html" class="nav-link">Trang chủ</a>
        <a href="products.html" class="nav-link">Sản phẩm</a>
        ${user ? `<a href="orders.html" class="nav-link">Đơn hàng</a>` : ''}
        ${user && user.role === 'ADMIN' ? `<a href="admin.html" class="nav-link">Quản trị</a>` : ''}
      </div>
      <div class="nav-actions">
        ${user ? `
          <a href="cart.html" class="btn btn-ghost" title="Giỏ hàng">
            🛒 Giỏ hàng ${cartItemCount > 0 ? `(${cartItemCount})` : ''}
          </a>
          <span style="color: var(--color-text-secondary)">Chào, <strong>${displayName}</strong></span>
          <button class="btn btn-danger btn-sm" onclick="logout()">Đăng xuất</button>
        ` : `
          <a href="login.html" class="btn btn-ghost">Đăng nhập</a>
          <a href="login.html?tab=register" class="btn btn-primary btn-sm">Đăng ký</a>
        `}
      </div>
    </div>
  `;
}

function logout() {
  api.setToken(null);
  window.location.href = 'index.html';
}
window.logout = logout;
window.renderNavbar = renderNavbar;

// Chạy khởi tạo
document.addEventListener('DOMContentLoaded', () => {
  renderNavbar();
});
