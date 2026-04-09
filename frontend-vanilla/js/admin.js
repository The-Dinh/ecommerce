document.addEventListener('DOMContentLoaded', async () => {
  try {
    const meRes = await api.user.getMe();
    const user = meRes?.data || meRes;
    if (!user || user.role !== 'ADMIN') {
      alert('Bạn không có quyền truy cập trang này!');
      window.location.href = 'index.html';
      return;
    }

    const userInfoEl = document.getElementById('admin-user-name');
    if (userInfoEl) {
      userInfoEl.textContent = `Xin chào, ${user.username || user.email}`;
    }
  } catch (error) {
    if (error.message.includes('401') || error.message.includes('403')) {
      alert('Vui lòng đăng nhập bằng tài khoản Admin!');
      window.location.href = 'login.html';
    } else {
      console.error(error);
    }
    return;
  }

  const sidebarContainer = document.getElementById('admin-sidebar-container');
  if (sidebarContainer) {
    const currentPage = window.location.pathname.split('/').pop();

    sidebarContainer.innerHTML = `
      <div class="admin-sidebar-header">
        <span>Admin Panel</span>
      </div>
      <div class="admin-nav">
        <a href="admin.html" class="admin-nav-item ${currentPage === 'admin.html' || currentPage === '' ? 'active' : ''}">
          <span>Dashboard</span>
        </a>
        <a href="admin-categories.html" class="admin-nav-item ${currentPage === 'admin-categories.html' ? 'active' : ''}">
          <span>Danh mục</span>
        </a>
        <a href="admin-products.html" class="admin-nav-item ${currentPage === 'admin-products.html' ? 'active' : ''}">
          <span>Sản phẩm</span>
        </a>
        <a href="admin-orders.html" class="admin-nav-item ${currentPage === 'admin-orders.html' ? 'active' : ''}">
          <span>Đơn hàng</span>
        </a>
        <a href="admin-users.html" class="admin-nav-item ${currentPage === 'admin-users.html' ? 'active' : ''}">
          <span>Người dùng</span>
        </a>
      </div>
      <div class="admin-sidebar-footer">
        <button class="admin-logout-btn" id="admin-logout">Đăng xuất</button>
      </div>
    `;

    document.getElementById('admin-logout').addEventListener('click', () => {
      api.setToken(null);
      window.location.href = 'login.html';
    });
  }

  window.showModal = function (modalId) {
    document.getElementById(modalId).classList.add('active');
  };

  window.hideModal = function (modalId) {
    document.getElementById(modalId).classList.remove('active');
  };

  document.querySelectorAll('.admin-modal-close').forEach((btn) => {
    btn.addEventListener('click', function () {
      const modal = this.closest('.admin-modal');
      if (modal) modal.classList.remove('active');
    });
  });
});

window.unwrapApiData = function (res) {
  if (res && typeof res === 'object' && Object.prototype.hasOwnProperty.call(res, 'data')) {
    return res.data;
  }
  return res;
};
