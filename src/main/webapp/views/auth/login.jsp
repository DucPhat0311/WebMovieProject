<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - MovieGO!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth/login&register.css" />
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
            
            <!-- Thông báo lỗi/success -->
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
            
            <!-- Form đăng nhập -->
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="email">Email <span class="required">*</span></label>
                    <div class="input-with-icon">
                        <i class="fas fa-envelope"></i>
                        <%
                            String rememberedEmail = "";
                            Cookie[] cookies = request.getCookies();
                            if (cookies != null) {
                                for (Cookie cookie : cookies) {
                                    if ("rememberedEmail".equals(cookie.getName())) {
                                        rememberedEmail = cookie.getValue();
                                        break;
                                    }
                                }
                            }
                            
                            // Ưu tiên: param từ form trước > cookie > empty
                            String emailParam = request.getParameter("email");
                            String emailValue = "";
                            
                            if (emailParam != null && !emailParam.trim().isEmpty()) {
                                emailValue = emailParam;
                            } else if (!rememberedEmail.isEmpty()) {
                                emailValue = rememberedEmail;
                            }
                        %>
                        <input type="email" id="email" name="email" 
                               placeholder="Nhập email của bạn" required
                               value="<%= emailValue %>">
                    </div>
                    <c:if test="${not empty emailError}">
                        <div class="field-error">
                            <i class="fas fa-exclamation-circle"></i> ${emailError}
                        </div>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="password">Mật khẩu <span class="required">*</span></label>
                    <div class="input-with-icon">
                        <i class="fas fa-lock"></i>
                        <input type="password" id="password" name="password" 
                               placeholder="Nhập mật khẩu" required minlength="6">
                        <i class="fas fa-eye toggle-password" onclick="togglePwd('password', this)"></i>
                    </div>
                    <c:if test="${not empty passwordError}">
                        <div class="field-error">
                            <i class="fas fa-exclamation-circle"></i> ${passwordError}
                        </div>
                    </c:if>
                </div>
                
                <div class="remember-forgot">
                    <label>
                        <input type="checkbox" name="rememberMe" value="true" 
                               <%= !rememberedEmail.isEmpty() ? "checked" : "" %>>
                        Ghi nhớ đăng nhập
                    </label>
                    <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
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
        function togglePwd(id, icon) {
            let input = document.getElementById(id);
            if (input.type === "password") {
                input.type = "text";
                icon.classList.replace("fa-eye", "fa-eye-slash");
            } else {
                input.type = "password";
                icon.classList.replace("fa-eye-slash", "fa-eye");
            }
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');
            
            if (emailInput.value.trim() !== '' && passwordInput.value === '') {
                passwordInput.focus();
            }
        });
    </script>
</body>
</html>