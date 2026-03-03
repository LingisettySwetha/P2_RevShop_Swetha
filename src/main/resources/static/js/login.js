document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();

    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var errorMsg = document.getElementById('errorMsg');
    var successMsg = document.getElementById('successMsg');

    errorMsg.classList.add('d-none');
    successMsg.classList.add('d-none');

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email, password: password })
    })
        .then(function (response) {
            return response.json().then(function (data) {
                return { status: response.status, body: data };
            });
        })
        .then(function (result) {
            if (result.status === 200) {
                localStorage.setItem('jwtToken', result.body.token);
                localStorage.setItem('userRole', result.body.role);
                localStorage.setItem('userName', result.body.name);
                localStorage.setItem('userId', result.body.userId);

                successMsg.textContent = 'Login successful! Redirecting...';
                successMsg.classList.remove('d-none');

                setTimeout(function () {
                    if (result.body.role === 'SELLER') {
                        window.location.href = '/seller/dashboard';
                    } else {
                        window.location.href = '/home';
                    }
                }, 500);
            } else {
                errorMsg.textContent = result.body.error || 'Invalid email or password';
                errorMsg.classList.remove('d-none');
            }
        })
        .catch(function () {
            errorMsg.textContent = 'Connection error. Please try again.';
            errorMsg.classList.remove('d-none');
        });
});
