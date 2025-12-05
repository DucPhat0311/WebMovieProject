<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="https://fonts.googleapis.com/css?family=Roboto&display=swap"
	rel="stylesheet" />
<link href="login.css" rel="stylesheet" />
<title>Đăng Ký</title>
</head>
<body>
	<div class="login-container">
		<div class="logo-area">
			<img src="images/luf.png" alt="Cinema Ticket Booking"
				class="logo-image-circle" />
		</div>

		<h1 class="login-title">Đăng Ký Tài Khoản</h1>

		<!-- Hiển thị thông báo lỗi -->
		<c:if test="${not empty errorMessage}">
			<div class="error-message">${errorMessage}</div>
		</c:if>

		<!-- Hiển thị thông báo thành công -->
		<c:if test="${not empty successMessage}">
			<div class="success-message">${successMessage}</div>
		</c:if>

		<form class="login-form" action="register" method="POST">
			<div class="form-group">
				<label for="username" class="form-label">Tên đăng nhập *</label>
				<div class="input-group">
					<input type="text" id="username" name="username"
						placeholder="Nhập tên đăng nhập" class="input-field"
						value="${param.username}" required />
				</div>
			</div>

			<div class="form-group">
				<label for="email" class="form-label">Email *</label>
				<div class="input-group">
					<input type="email" id="email" name="email"
						placeholder="Nhập Email" class="input-field"
						value="${param.email}" required />
				</div>
			</div>

			<div class="form-group">
				<label for="password" class="form-label">Mật khẩu *</label>
				<div class="input-group password-group">
					<input type="password" id="password" name="password"
						placeholder="Nhập Mật khẩu" class="input-field" required />
					<button type="button" class="password-toggle"
						aria-label="Hiển thị mật khẩu">
						<span class="toggle-icon"></span>
					</button>
				</div>
			</div>

			<div class="form-group">
				<label for="confirmPassword" class="form-label">Xác nhận mật
					khẩu *</label>
				<div class="input-group password-group">
					<input type="password" id="confirmPassword" name="confirmPassword"
						placeholder="Nhập lại mật khẩu" class="input-field" required />
					<button type="button" class="password-toggle"
						aria-label="Hiển thị mật khẩu">
						<span class="toggle-icon"></span>
					</button>
				</div>
			</div>

			<div class="form-group">
				<label for="phoneNumber" class="form-label">Số điện thoại</label>
				<div class="input-group">
					<input type="tel" id="phoneNumber" name="phoneNumber"
						placeholder="Nhập số điện thoại" class="input-field"
						value="${param.phoneNumber}" />
				</div>
			</div>

			<div class="form-group">
				<label for="birthDate" class="form-label">Ngày sinh</label>
				<div class="input-group">
					<input type="date" id="birthDate" name="birthDate"
						class="input-field" value="${param.birthDate}" />
				</div>
			</div>

			<div class="form-group">
				<label class="form-label">Giới tính</label>
				<div class="radio-group">
					<label class="radio-label"> <input type="radio"
						name="gender" value="true"
						${param.gender == 'true' ? 'checked' : ''} /> Nam
					</label> <label class="radio-label"> <input type="radio"
						name="gender" value="false"
						${param.gender == 'false' ? 'checked' : ''} /> Nữ
					</label>
				</div>
			</div>

			<button type="submit" class="btn-login">
				<span class="btn-login-text">ĐĂNG KÝ</span>
			</button>
		</form>

		<div class="signup-prompt">
			<span class="signup-text">Bạn đã có tài khoản?</span> <a
				href="SignIn.jsp" class="btn-signup"> <span
				class="btn-signup-text">Đăng nhập</span>
			</a>
		</div>
	</div>

	<!-- JavaScript để toggle password và validation -->
	<script>
        document.addEventListener('DOMContentLoaded', function() {
            // Toggle password visibility
            const toggleButtons = document.querySelectorAll('.password-toggle');
            toggleButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const passwordInput = this.parentElement.querySelector('input[type="password"]');
                    if (passwordInput.type === 'password') {
                        passwordInput.type = 'text';
                        this.classList.add('active');
                    } else {
                        passwordInput.type = 'password';
                        this.classList.remove('active');
                    }
                });
            });

            // Password confirmation validation
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            
            function validatePassword() {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity("Mật khẩu xác nhận không khớp!");
                } else {
                    confirmPassword.setCustomValidity("");
                }
            }

            password.addEventListener('change', validatePassword);
            confirmPassword.addEventListener('keyup', validatePassword);
        });
    </script>
</body>
</html>