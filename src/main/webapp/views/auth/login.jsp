<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - MovieGO!</title>
    <link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/auth/login&register.css" />
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
            
            <!-- Hiển thị thông báo lỗi/success từ server -->
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
                <!-- Email -->
                <div class="form-group">
                    <label for="email">Email <span class="required">*</span></label>
                    <div class="input-with-icon">
                        <i class="fas fa-envelope"></i>
                        <input type="email" id="email" name="email" 
                               placeholder="Nhập email của bạn" 
                               required
                               value="${param.email}"
                               title="Vui lòng nhập đúng định dạng email">
                        <c:if test="${not empty emailError}">
                            <div class="field-error">
                                <i class="fas fa-exclamation-circle"></i> ${emailError}
                            </div>
                        </c:if>
                    </div>
                </div>
                
                <!-- Password với toggle (FIXED) -->
                <div class="form-group">
                    <label for="password">Mật khẩu <span class="required">*</span></label>
                    
                    <!-- Checkbox ẩn để toggle password -->
                    <input type="checkbox" id="showPassword" class="password-toggle-checkbox">
                    
                    <div class="password-toggle-wrapper">
                        <div class="input-with-icon">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="password" name="password" 
                                   class="password-field"
                                   placeholder="Nhập mật khẩu" 
                                   required
                                   minlength="6"
                                   title="Mật khẩu phải có ít nhất 6 ký tự">
                            <label for="showPassword" class="password-toggle-label">
                                <i class="fas fa-eye"></i>
                                <i class="fas fa-eye-slash"></i>
                            </label>
                        </div>
                    </div>
                    
                    <c:if test="${not empty passwordError}">
                        <div class="field-error">
                            <i class="fas fa-exclamation-circle"></i> ${passwordError}
                        </div>
                    </c:if>
                </div>
                
                <!-- Remember me & Forgot password -->
                <div class="remember-forgot">
                    <label>
                        <input type="checkbox" name="remember" value="true"
                               ${param.remember == 'true' ? 'checked' : ''}>
                        Ghi nhớ đăng nhập
                    </label>
                    <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
                </div>
                
                <!-- Submit button -->
                <button type="submit" class="btn-primary">Đăng nhập</button>
            </form>
            
            <!-- Links -->
            <div class="links">
                <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
                <p><a href="${pageContext.request.contextPath}/home"><i class="fas fa-arrow-left"></i> Quay lại trang chủ</a></p>
            </div>
        </div>
    </div>
</body>
</html>