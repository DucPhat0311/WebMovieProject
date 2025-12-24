<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
			<span class="movie">Movie</span><b><span class="go">GO!</span></b>
		</div>

		<nav class="glass-nav">
			<ul>
				<li><a href="#" class="active">Trang chủ</a></li>
				<li><a href="#">Phim</a></li>
				<li><a href="#">Rạp</a></li>
				<li><a href="#">Giới thiệu</a></li>
			</ul>
		</nav>
		<!-- Thay thế button đăng nhập nếu đăng nhập thành công -->
		<div class="search-login">
			<input type="text" placeholder="Tìm kiếm" />
			<!-- Kiểm tra nếu đã login -->
			<c:choose>
				<c:when test="${not empty sessionScope.user}">
					<div class="user-info">
						<span>Xin chào, ${sessionScope.user.fullName}</span> <a
							href="logout" class="logout-btn">Đăng xuất</a>
					</div>
				</c:when>
				<c:otherwise>
						<a href="login" class="login-btn">Đăng nhập</a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>
</body>
</html>