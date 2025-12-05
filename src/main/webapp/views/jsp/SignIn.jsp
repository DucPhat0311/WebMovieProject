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
<title>Đăng Nhập</title>
</head>
<body>
	<div class="login-container">
		<div class="logo-area">
			<img src="images/luf.png" alt="Cinema Ticket Booking"
				class="logo-image-circle" />
		</div>

		<h1 class="login-title">Đăng Nhập Tài Khoản</h1>

		<!-- Hiển thị thông báo lỗi bằng JSTL -->
		<c:if test="${not empty errorMessage}">
			<div class="error-message">${errorMessage}</div>
		</c:if>

		<!-- Form đăng nhập -->
		<form class="login-form" action="login" method="POST">
			<div class="form-group">
				<label for="email" class="form-label">Email</label>
				<div class="input-group">
					<input type="email" id="email" name="email"
						placeholder="Nhập Email" class="input-field"
						value="${param.email}" required />
				</div>
			</div>

			<div class="form-group">
				<label for="password" class="form-label">Mật khẩu</label>
				<div class="input-group password-group">
					<input type="password" id="password" name="password"
						placeholder="Nhập Mật khẩu" class="input-field" required />
					<button type="button" class="password-toggle"
						aria-label="Hiển thị mật khẩu">
						<span class="toggle-icon"></span>
					</button>
				</div>
			</div>

			<button type="submit" class="btn-login">
				<span class="btn-login-text">ĐĂNG NHẬP</span>
			</button>

			<div class="form-footer">
				<a href="#" class="link-forgot-password">Quên mật khẩu?</a>
			</div>
		</form>

		<div class="signup-prompt">
			<span class="signup-text">Bạn chưa có tài khoản?</span> <a
				href="SignUp.jsp" class="btn-signup"> <span
				class="btn-signup-text">Đăng ký</span>
			</a>
		</div>
	</div>

	<!-- JavaScript thuần - không Java -->
	<script>
        document.addEventListener('DOMContentLoaded', function() {
            const toggleButtons = document.querySelectorAll('.password-toggle');
            
            toggleButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const passwordInput = this.parentElement.querySelector('input[type="password"]');
                    const isPassword = passwordInput.type === 'password';
                    
                    passwordInput.type = isPassword ? 'text' : 'password';
                    this.classList.toggle('active', isPassword);
                });
            });
        });
    </script>
</body>
</html>