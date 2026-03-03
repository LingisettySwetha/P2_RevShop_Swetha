document.getElementById('registerForm').addEventListener('submit', function (e) {
    var password = document.querySelector('input[name="password"]').value;
    var email = document.querySelector('input[name="email"]').value;
    var errorMsg = document.getElementById('errorMsg');

    errorMsg.classList.add('d-none');

    if (password.length < 4) {
        e.preventDefault();
        errorMsg.textContent = 'Password must be at least 4 characters';
        errorMsg.classList.remove('d-none');
        return;
    }

    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
        e.preventDefault();
        errorMsg.textContent = 'Please enter a valid email address';
        errorMsg.classList.remove('d-none');
    }
});
