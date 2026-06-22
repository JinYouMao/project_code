document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const addForm = document.getElementById('addForm');

    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            e.preventDefault();
            doLogin();
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', function (e) {
            e.preventDefault();
            doRegister();
        });
    }

    if (addForm) {
        addForm.addEventListener('submit', function (e) {
            e.preventDefault();
            submitMessage();
        });
    }
});

async function doLogin() {
    const username = document.getElementById('loginUsername')?.value;
    const password = document.getElementById('loginPassword')?.value;

    if (!username || !password) {
        alert('请输入用户名和密码');
        return;
    }

    const formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);

    try {
        const response = await fetch('/user/doLogin', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('登录成功');
            window.location.href = '/user/index';
        } else {
            alert(result.message || '登录失败');
        }
    } catch (error) {
        console.error(error);
        alert('登录失败，请重试');
    }
}

async function doRegister() {
    const username = document.getElementById('regUsername')?.value;
    const password = document.getElementById('regPassword')?.value;
    const confirmPassword = document.getElementById('regConfirmPassword')?.value;
    const phone = document.getElementById('regPhone')?.value;

    if (!username || !password || !phone) {
        alert('请填写完整信息');
        return;
    }

    if (password !== confirmPassword) {
        alert('两次密码输入不一致');
        return;
    }

    const formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);
    formData.append('phone', phone);

    try {
        const response = await fetch('/user/doRegister', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('注册成功！');
            window.location.href = '/user/login';
        } else {
            alert(result.message || '注册失败');
        }
    } catch (error) {
        console.error(error);
        alert('注册失败，请重试');
    }
}

async function submitMessage() {
    const username = document.getElementById('username')?.value;
    const content = document.getElementById('content')?.value;
    const contact = document.getElementById('contact')?.value;

    if (!content || content.trim() === '') {
        alert('请输入留言内容');
        return;
    }

    const formData = new FormData();
    if (username) formData.append('username', username);
    formData.append('content', content);
    if (contact) formData.append('contact', contact);

    try {
        const response = await fetch('/user/add', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('留言提交成功');
            window.location.href = '/user/index';
        } else {
            alert(result.message || '提交失败');
        }
    } catch (error) {
        console.error(error);
        alert('提交失败，请重试');
    }
}

async function polishContent() {
    const content = document.getElementById('content')?.value;
    if (!content || content.trim() === '') {
        alert('请先输入内容');
        return;
    }

    try {
        const response = await fetch('/user/ai/polish', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'content=' + encodeURIComponent(content)
        });
        const result = await response.json();
        if (result.code === 200) {
            document.getElementById('content').value = result.data;
            alert('润色完成');
        } else {
            alert(result.message || '润色失败');
        }
    } catch (error) {
        console.error(error);
        alert('润色失败，请重试');
    }
}

async function checkRisk() {
    const content = document.getElementById('content')?.value;
    if (!content || content.trim() === '') {
        alert('请先输入内容');
        return;
    }

    try {
        const response = await fetch('/user/ai/checkRisk', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'content=' + encodeURIComponent(content)
        });
        const result = await response.json();
        if (result.code === 200) {
            alert(result.data ? '检测到内容可能违规' : '内容正常');
        } else {
            alert(result.message || '检测失败');
        }
    } catch (error) {
        console.error(error);
        alert('检测失败，请重试');
    }
}

async function loadTemplate(type) {
    try {
        const response = await fetch('/user/ai/template?type=' + encodeURIComponent(type), {
            method: 'GET'
        });
        const result = await response.json();
        if (result.code === 200) {
            document.getElementById('content').value = result.data;
        } else {
            alert(result.message || '获取模板失败');
        }
    } catch (error) {
        console.error(error);
        alert('获取模板失败，请重试');
    }
}
