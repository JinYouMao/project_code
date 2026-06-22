// 用户端JavaScript脚本

// 全局变量
let currentPage = 1;
let pageSize = 10;
let totalPages = 1;

// 加载留言列表
function loadMessages() {
    const messageList = document.getElementById('messageList');
    messageList.innerHTML = '<div class="loading">加载中...</div>';

    fetch('/user/list?pageNum=' + currentPage + '&pageSize=' + pageSize)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayMessages(data.data);
                updatePagination(data.total);
            } else {
                messageList.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div class="empty-state-text">加载失败，请刷新重试</div></div>';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageList.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div class="empty-state-text">网络错误，请刷新重试</div></div>';
        });
}

// 显示留言列表
function displayMessages(messages) {
    const messageList = document.getElementById('messageList');

    if (!messages || messages.length === 0) {
        messageList.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📝</div><div class="empty-state-text">暂无留言，快来发表第一条留言吧！</div></div>';
        return;
    }

    let html = '';
    messages.forEach(msg => {
        // 状态标签
        const statusClass = msg.status === 1 ? 'status-replied' : 'status-pending';
        const statusText = msg.status === 1 ? '已回复' : '待回复';
        
        // 类型标签
        const typeClass = getTypeClass(msg.type);
        
        // 风险标记
        const riskyClass = msg.isRisk === 1 ? 'risky' : '';
        
        // 风险标签
        const riskTag = msg.isRisk === 1 ? '<span class="tag tag-danger">⚠️ 违规</span>' : '';
        
        // 内容摘要
        const contentPreview = truncateContent(msg.content, 100);

        html += `
            <div class="message-card ${riskyClass}" onclick="goToDetail(${msg.id})">
                <div class="message-header">
                    <div class="message-user">
                        <span>👤</span>
                        <span>${escapeHtml(msg.username)}</span>
                    </div>
                    <div class="message-tags">
                        <span class="tag ${statusClass}">${statusText}</span>
                        <span class="tag ${typeClass}">${msg.type}</span>
                        ${riskTag}
                    </div>
                </div>
                <div class="message-content">
                    ${escapeHtml(contentPreview)}
                </div>
                <div class="message-footer">
                    <div class="message-stats">
                        <span>🕐 ${msg.createTime}</span>
                    </div>
                    <span class="view-detail">查看详情 →</span>
                </div>
            </div>
        `;
    });

    messageList.innerHTML = html;
}

// 获取类型样式类
function getTypeClass(type) {
    const classes = {
        '咨询': 'tag-info',
        '建议': 'tag-success',
        '投诉': 'tag-warning',
        '其他': 'tag-secondary',
        '未知': 'tag-secondary',
        '感谢': 'tag-success'
    };
    return classes[type] || 'tag-secondary';
}

// HTML转义
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 内容截断
function truncateContent(content, maxLength) {
    if (!content || content.length <= maxLength) {
        return content;
    }
    return content.substring(0, maxLength) + '...';
}

// 跳转详情页
function goToDetail(id) {
    window.location.href = '/user/detail?id=' + id;
}

// 更新分页
function updatePagination(total) {
    totalPages = Math.ceil(total / pageSize);
    if (totalPages === 0) totalPages = 1;

    const pageInfo = document.getElementById('pageInfo');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    pageInfo.textContent = '第 ' + currentPage + ' 页 / 共 ' + totalPages + ' 页';
    
    prevBtn.disabled = currentPage <= 1;
    nextBtn.disabled = currentPage >= totalPages;
}

// 上一页
function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        loadMessages();
    }
}

// 下一页
function nextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        loadMessages();
    }
}

// 跳转到指定页
function goToPage(page) {
    if (page >= 1 && page <= totalPages) {
        currentPage = page;
        loadMessages();
    }
}
