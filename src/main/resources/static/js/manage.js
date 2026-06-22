document.addEventListener('DOMContentLoaded', function () {
    const replyForm = document.getElementById('replyForm');
    if (replyForm) {
        replyForm.addEventListener('submit', function (e) {
            e.preventDefault();
            addReply();
        });
    }
});

function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.querySelectorAll('.row-checkbox');
    checkboxes.forEach(function (checkbox) {
        checkbox.checked = selectAll.checked;
    });
}

async function addReply() {
    const messageId = document.getElementById('replyMessageId')?.value;
    const replyContent = document.getElementById('replyContent')?.value;

    if (!replyContent || replyContent.trim() === '') {
        alert('请输入回复内容');
        return;
    }

    const formData = new FormData();
    formData.append('messageId', messageId);
    formData.append('replyContent', replyContent);

    try {
        const response = await fetch('/manage/reply/add', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('回复成功');
            // 强制刷新，带时间戳确保不缓存
            window.location.href = window.location.pathname + '?t=' + Date.now();
        } else {
            alert(result.message || '回复失败');
        }
    } catch (error) {
        console.error(error);
        alert('回复失败，请重试');
    }
}

async function deleteReply(id, messageId) {
    if (!confirm('确定要删除此回复吗？')) {
        return;
    }

    const formData = new FormData();
    formData.append('id', id);
    formData.append('messageId', messageId);

    try {
        const response = await fetch('/manage/reply/delete', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('删除成功');
            // 强制刷新，带时间戳确保不缓存
            window.location.href = window.location.pathname + '?t=' + Date.now();
        } else {
            alert(result.message || '删除失败');
        }
    } catch (error) {
        console.error(error);
        alert('删除失败，请重试');
    }
}

async function deleteMessage(id) {
    if (!confirm('确定要删除此留言吗？删除后将无法恢复！')) {
        return;
    }

    const formData = new FormData();
    formData.append('id', id);

    try {
        const response = await fetch('/manage/message/delete', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('删除成功');
            window.location.reload();
        } else {
            alert(result.message || '删除失败');
        }
    } catch (error) {
        console.error(error);
        alert('删除失败，请重试');
    }
}

async function deleteBatchMessages() {
    const checkboxes = document.querySelectorAll('.row-checkbox:checked');
    if (checkboxes.length === 0) {
        alert('请先选择要删除的留言');
        return;
    }

    if (!confirm('确定要删除选中的 ' + checkboxes.length + ' 条留言吗？删除后将无法恢复！')) {
        return;
    }

    const ids = [];
    checkboxes.forEach(function (checkbox) {
        ids.push(checkbox.value);
    });

    const formData = new FormData();
    formData.append('idsStr', ids.join(','));

    try {
        const response = await fetch('/manage/message/deleteBatch', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('批量删除成功');
            window.location.reload();
        } else {
            alert(result.message || '删除失败');
        }
    } catch (error) {
        console.error(error);
        alert('删除失败，请重试');
    }
}

async function updateMessageStatus(id, status) {
    const formData = new FormData();
    formData.append('id', id);
    formData.append('status', status);

    try {
        const response = await fetch('/manage/message/updateStatus', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('状态更新成功');
            window.location.reload();
        } else {
            alert(result.message || '更新失败');
        }
    } catch (error) {
        console.error(error);
        alert('更新失败，请重试');
    }
}

async function aiClassify(id) {
    const btn = document.getElementById('aiClassifyBtn_' + id);
    if (btn) {
        btn.disabled = true;
        btn.innerText = '分类中...';
    }

    const formData = new FormData();
    formData.append('id', id);

    try {
        const response = await fetch('/manage/ai/classify', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('分类完成');
            window.location.reload();
        } else {
            alert(result.message || '分类失败');
        }
    } catch (error) {
        console.error(error);
        alert('分类失败，请重试');
    } finally {
        if (btn) {
            btn.disabled = false;
            btn.innerText = 'AI分类';
        }
    }
}

async function aiCheckRisk(id) {
    const btn = document.getElementById('aiCheckRiskBtn_' + id);
    if (btn) {
        btn.disabled = true;
        btn.innerText = '检查中...';
    }

    const formData = new FormData();
    formData.append('id', id);

    try {
        const response = await fetch('/manage/ai/checkRisk', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            if (result.isRisk) {
                alert('检测完成！该留言存在违规内容');
            } else {
                alert('检测完成！该留言未发现违规内容');
            }
            window.location.reload();
        } else {
            alert(result.message || '检查失败');
        }
    } catch (error) {
        console.error(error);
        alert('检查失败，请重试');
    } finally {
        if (btn) {
            btn.disabled = false;
            btn.innerText = '违规检查';
        }
    }
}

async function batchCheckRisk() {
    if (!confirm('确定要对所有留言进行违规检测吗？这可能需要一些时间。')) {
        return;
    }

    try {
        const response = await fetch('/manage/ai/batchCheckRisk', {
            method: 'POST'
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('批量检测完成');
            window.location.reload();
        } else {
            alert(result.message || '检测失败');
        }
    } catch (error) {
        console.error(error);
        alert('检测失败，请重试');
    }
}

async function aiGenerateReply(id) {
    const btn = document.getElementById('aiGenerateReply_' + id);
    if (btn) {
        btn.disabled = true;
        btn.innerText = '生成中...';
    }

    const formData = new FormData();
    formData.append('id', id);

    try {
        const response = await fetch('/manage/ai/generateReply', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            document.getElementById('replyContent').value = result.reply;
            alert('回复生成成功');
        } else {
            alert(result.message || '生成失败');
        }
    } catch (error) {
        console.error(error);
        alert('生成失败，请重试');
    } finally {
        if (btn) {
            btn.disabled = false;
            btn.innerText = 'AI生成回复';
        }
    }
}

async function editReply(id) {
    const newContent = prompt('请输入新的回复内容');
    if (newContent == null || newContent.trim() === '') {
        return;
    }

    const formData = new FormData();
    formData.append('id', id);
    formData.append('replyContent', newContent);

    try {
        const response = await fetch('/manage/reply/update', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('编辑成功');
            // 强制刷新，带时间戳确保不缓存
            window.location.href = window.location.pathname + '?t=' + Date.now();
        } else {
            alert(result.message || '编辑失败');
        }
    } catch (error) {
        console.error(error);
        alert('编辑失败，请重试');
    }
}
