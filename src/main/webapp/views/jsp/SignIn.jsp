<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - MovieGO!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <div class="auth-box">
            <div class="logo">
                <h1><span class="movie">Movie</span><span class="go">GO!</span></h1>
                <p>Đặt vé phim nhanh chóng, tiện lợi</p>
            </div>
            
            <h2>Đăng nhập</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="email">Email</label>
                    <div class="input-with-icon">
                        <i class="fas fa-envelope"></i>
                        <input type="email" id="email" name="email" placeholder="Nhập email của bạn" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <div class="input-with-icon">
                        <i class="fas fa-lock"></i>
                        <i class="fas fa-eye password-toggle" id="togglePassword"></i>
                        <input type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
                    </div>
                </div>
                
                <div class="remember-forgot">
                    <label>
                        <input type="checkbox" name="remember"> Ghi nhớ đăng nhập
                    </label>
                    <a href="#">Quên mật khẩu?</a>
                </div>
                
                <button type="submit" class="btn-primary">Đăng nhập</button>
            </form>
            
            <div class="links">
                <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
                <p><a href="${pageContext.request.contextPath}/home"><i class="fas fa-arrow-left"></i> Quay lại trang chủ</a></p>
            </div>
        </div>
    </div>
    
    <script>
        // Toggle hiển thị mật khẩu
        document.getElementById('togglePassword').addEventListener('click', function() {
            const passwordInput = document.getElementById('password');
            const icon = this;
            
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.className = 'fas fa-eye-slash password-toggle';
            } else {
                passwordInput.type = 'password';
                icon.className = 'fas fa-eye password-toggle';
            }
        });
    </script>
</body>
</html>