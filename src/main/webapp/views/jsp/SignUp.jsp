<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - MovieGO!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <div class="auth-box">
            <div class="logo">
                <h1><span class="movie">Movie</span><span class="go">GO!</span></h1>
                <p>Tạo tài khoản để đặt vé dễ dàng</p>
            </div>
            
            <h2>Tạo tài khoản mới</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                <div class="form-row">
                    <div class="form-group">
                        <label for="fullName">Họ và tên <span class="required">*</span></label>
                        <div class="input-with-icon">
                            <i class="fas fa-user"></i>
                            <input type="text" id="fullName" name="fullName" placeholder="Nhập họ tên" required value="${param.fullName}">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email <span class="required">*</span></label>
                        <div class="input-with-icon">
                            <i class="fas fa-envelope"></i>
                            <input type="email" id="email" name="email" placeholder="Nhập email" required value="${param.email}">
                        </div>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Mật khẩu <span class="required">*</span></label>
                        <div class="input-with-icon">
                            <i class="fas fa-lock"></i>
                            <i class="fas fa-eye password-toggle" id="togglePassword"></i>
                            <input type="password" id="password" name="password" placeholder="Ít nhất 6 ký tự" required minlength="6">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Xác nhận mật khẩu <span class="required">*</span></label>
                        <div class="input-with-icon">
                            <i class="fas fa-lock"></i>
                            <i class="fas fa-eye password-toggle" id="toggleConfirmPassword"></i>
                            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>
                        </div>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="phone">Số điện thoại</label>
                        <div class="input-with-icon">
                            <i class="fas fa-phone"></i>
                            <input type="tel" id="phone" name="phone" placeholder="Nhập số điện thoại" value="${param.phone}">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="gender">Giới tính <span class="required">*</span></label>
                        <div class="input-with-icon">
                            <i class="fas fa-venus-mars"></i>
                            <select id="gender" name="gender" required>
                                <option value="">Chọn giới tính</option>
                                <option value="Nam" ${param.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                                <option value="Nu" ${param.gender == 'Nu' ? 'selected' : ''}>Nữ</option>
                                <option value="Khac" ${param.gender == 'Khac' ? 'selected' : ''}>Khác</option> <!-- rất cay khi thấy có giới tính khác trong db -->
                            </select>
                        </div>
                    </div>
                </div>
                
                <div class="terms">
                    <label>
                        <input type="checkbox" name="agreeTerms" required>
                        Tôi đồng ý với <a href="#">Điều khoản dịch vụ</a> và <a href="#">Chính sách bảo mật</a> <span class="required">*</span>
                    </label>
                </div>
                
                <button type="submit" class="btn-primary">Đăng ký</button>
            </form>
            
            <div class="links">
                <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a></p>
                <p><a href="${pageContext.request.contextPath}/home"><i class="fas fa-arrow-left"></i> Quay lại trang chủ</a></p>
            </div>
        </div>
    </div>
    
    <script>
        // Validate form đăng ký
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu xác nhận không khớp!');
                document.getElementById('confirmPassword').focus();
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                alert('Mật khẩu phải có ít nhất 6 ký tự!');
                document.getElementById('password').focus();
                return false;
            }
            
            return true;
        });
        
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
        
        document.getElementById('toggleConfirmPassword').addEventListener('click', function() {
            const confirmInput = document.getElementById('confirmPassword');
            const icon = this;
            
            if (confirmInput.type === 'password') {
                confirmInput.type = 'text';
                icon.className = 'fas fa-eye-slash password-toggle';
            } else {
                confirmInput.type = 'password';
                icon.className = 'fas fa-eye password-toggle';
            }
        });
    </script>
</body>
</html>