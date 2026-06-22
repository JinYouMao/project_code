// 管理后台JavaScript脚本

// 检查登录状态
function checkAdminLogin() {
    fetch('/admin/checkLogin')
        .then(response => response.json())
        .then(data => {
            if (!data.success || !data.data.loggedIn) {
                location.href = '/admin/login';
            }
        })
        .catch(error => {
            location.href = '/admin/login';
        });
}

// 退出登录
function adminLogout() {
    if (confirm('确定要退出登录吗？')) {
        location.href = '/admin/logout';
    }
}

// 显示提示信息
function showTip(message, type) {
    const tip = document.createElement('div');
    tip.className = 'global-tip tip-' + (type || 'info');
    tip.textContent = message;
    document.body.appendChild(tip);

    setTimeout(() => {
        tip.classList.add('show');
    }, 10);

    setTimeout(() => {
        tip.classList.remove('show');
        setTimeout(() => {
            tip.remove();
        }, 300);
    }, 3000);
}

// 格式化日期时间
function formatDateTime(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// HTML转义
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 防抖函数
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// 确认对话框
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// 加载动画
function showLoading(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = '<div class="loading">加载中...</div>';
    }
}

// 空状态
function showEmptyState(elementId, message) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">📭</div>
                <div class="empty-state-text">${message || '暂无数据'}</div>
            </div>
        `;
    }
}

// 页面加载完成后自动检查登录
document.addEventListener('DOMContentLoaded', function() {
    // 如果不是登录页面，则检查登录状态
    if (!window.location.pathname.includes('/admin/login')) {
        checkAdminLogin();
    }
});
