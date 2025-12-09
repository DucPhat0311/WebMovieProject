<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Đăng ký - MovieGO!</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/login.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
	<div class="container">
		<div class="auth-box">
			<div class="logo">
				<h1>
					<span class="movie">Movie</span><span class="go">GO!</span>
				</h1>
				<p>Tạo tài khoản để đặt vé dễ dàng</p>
			</div>

			<h2>Tạo tài khoản mới</h2>

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

			<form action="${pageContext.request.contextPath}/register"
				method="post">
				<div class="form-row">
					<div class="form-group">
						<label for="fullName">Họ và tên <span class="required">*</span></label>
						<div class="input-with-icon">
							<i class="fas fa-user"></i> <input type="text" id="fullName"
								name="fullName" placeholder="Nhập họ tên đầy đủ" required
								value="${param.fullName}" minlength="2" maxlength="50">
						</div>
						<c:if test="${not empty fullNameError}">
							<div class="field-error">
								<i class="fas fa-exclamation-circle"></i> ${fullNameError}
							</div>
						</c:if>
					</div>

					<div class="form-group">
						<label for="email">Email <span class="required">*</span></label>
						<div class="input-with-icon">
							<i class="fas fa-envelope"></i> <input type="email" id="email"
								name="email" placeholder="Nhập email" required
								value="${param.email}">
						</div>
						<c:if test="${not empty emailError}">
							<div class="field-error">
								<i class="fas fa-exclamation-circle"></i> ${emailError}
							</div>
						</c:if>
					</div>
				</div>

				<div class="form-row">
					<div class="form-group">
						<label for="password">Mật khẩu <span class="required">*</span></label>
						<div class="input-with-icon">
							<i class="fas fa-lock"></i> <input type="password" id="password"
								name="password" placeholder="Ít nhất 6 ký tự" required
								minlength="6">
						</div>
						<c:if test="${not empty passwordError}">
							<div class="field-error">
								<i class="fas fa-exclamation-circle"></i> ${passwordError}
							</div>
						</c:if>
					</div>

					<div class="form-group">
						<label for="confirmPassword">Xác nhận mật khẩu <span
							class="required">*</span></label>
						<div class="input-with-icon">
							<i class="fas fa-lock"></i> <input type="password"
								id="confirmPassword" name="confirmPassword"
								placeholder="Nhập lại mật khẩu" required>
						</div>
						<c:if test="${not empty confirmPasswordError}">
							<div class="field-error">
								<i class="fas fa-exclamation-circle"></i>
								${confirmPasswordError}
							</div>
						</c:if>
					</div>
				</div>

				<div class="form-row">
					<div class="form-group">
						<label for="phone">Số điện thoại</label>
						<div class="input-with-icon">
							<i class="fas fa-phone"></i> <input type="tel" id="phone"
								name="phone" placeholder="Nhập số điện thoại"
								value="${param.phone}">
						</div>
						<c:if test="${not empty phoneError}">
							<div class="field-error">
								<i class="fas fa-exclamation-circle"></i> ${phoneError}
							</div>
						</c:if>
					</div>

					<div class="form-group">
						<label for="gender">Giới tính <span class="required">*</span></label>
						<div class="input-with-icon">
							<i class="fas fa-venus-mars"></i> <select id="gender"
								name="gender" required>
								<option value="">Chọn giới tính</option>
								<option value="Nam" ${param.gender == 'Nam' ? 'selected' : ''}>Nam</option>
								<option value="Nu" ${param.gender == 'Nu' ? 'selected' : ''}>Nữ</option>
								<option value="Khac" ${param.gender == 'Khac' ? 'selected' : ''}>Khác</option>
							</select>
						</div>
					</div>
				</div>

				<div class="terms">
					<label> <input type="checkbox" name="agreeTerms"
						value="true"
						required
                               ${param.agreeTerms=='true' ? 'checked' : ''}>
						Tôi đồng ý với <a href="${pageContext.request.contextPath}/terms">Điều
							khoản dịch vụ</a> và <a
						href="${pageContext.request.contextPath}/privacy">Chính sách
							bảo mật</a> <span class="required">*</span>
					</label>
					<c:if test="${not empty termsError}">
						<div class="field-error">
							<i class="fas fa-exclamation-circle"></i> ${termsError}
						</div>
					</c:if>
				</div>

				<button type="submit" class="btn-primary">Đăng ký</button>
			</form>

			<div class="links">
				<p>
					Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng
						nhập ngay</a>
				</p>
				<p>
					<a href="${pageContext.request.contextPath}/home"><i
						class="fas fa-arrow-left"></i> Quay lại trang chủ</a>
				</p>
			</div>
		</div>
	</div>
</body>
</html>