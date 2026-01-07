<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<header>
		<div class="logo">
			<a href="${pageContext.request.contextPath}/"
				style="text-decoration: none; cursor: pointer;"> <span
				class="movie">Movie</span><b><span class="go">GO!</span></b>
			</a>
		</div>

		<nav class="glass-nav">
			<ul>
				<li><a href="#" class="active">Trang chủ</a></li>
				<li><a href="#">Phim</a></li>
				<li><a href="#">Rạp</a></li>
				<li><a href="#">Giới thiệu</a></li>
			</ul>
		</nav>

		<div class="search-login">
			<input type="text" placeholder="Tìm kiếm" />
			<c:choose>
				<c:when test="${not empty sessionScope.user}">

					<div class="user-dropdown">

						<div class="user-info">
							<i class="fas fa-user-circle avatar-icon"></i> <i
								class="fas fa-caret-down" style="font-size: 12px;"></i> <span>${sessionScope.user.fullName}</span>
						</div>

						<div class="dropdown-content">

							<c:if test="${sessionScope.user.role == 'admin'}">
								<a href="${pageContext.request.contextPath}/admin/dashboard">
									<i class="fas fa-cogs"></i> Trang quản trị
								</a>
							</c:if>

							<hr style="margin: 0; border: 0; border-top: 1px solid #eee;">
							<a href="${pageContext.request.contextPath}/logout"> <i
								class="fas fa-sign-out-alt"></i> Đăng xuất
							</a>
						</div>
					</div>

				</c:when>

				<c:otherwise>
					<a href="login" class="login-btn"
						style="color: white; text-decoration: none; font-weight: bold;">Đăng
						nhập</a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>
</body>
</html>