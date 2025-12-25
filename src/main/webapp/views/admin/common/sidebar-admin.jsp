<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
</head>
<body>
	<div class="sidebar">
		<div class="sidebar-header">
			<h2>
				Movie<span>GO!</span>
			</h2>
		</div>

		<div class="sidebar-menu">
			<a href="${pageContext.request.contextPath}/admin/dashboard"
				class="${param.muc_hien_tai == 'dashboard' ? 'active' : ''}"> <i
				class="fas fa-home"></i> Tổng Quan
			</a> <a href="${pageContext.request.contextPath}/admin/manage-movies"
				class="${param.muc_hien_tai == 'movies' ? 'active' : ''}"> <i
				class="fas fa-film"></i> Quản Lý Phim
			</a> <a href="${pageContext.request.contextPath}/admin/manage-showtimes"
				class="${param.muc_hien_tai == 'showtimes' ? 'active' : ''}"> <i
				class="fas fa-calendar-alt"></i> Quản Lý Lịch Chiếu
			</a> <a href="${pageContext.request.contextPath}/admin/manage-bookings"
				class="${param.muc_hien_tai == 'bookings' ? 'active' : ''}"> <i
				class="fas fa-ticket-alt"></i> Quản Lý Vé
			</a> <a href="${pageContext.request.contextPath}/admin/manage-accounts"
				class="${param.muc_hien_tai == 'accounts' ? 'active' : ''}"> <i
				class="fas fa-users"></i> Quản Lý Khách Hàng
			</a>
		</div>

		<div class="sidebar-footer">
			<a href="${pageContext.request.contextPath}/home"><i
				class="fas fa-home"></i> Về trang chủ</a>
		</div>

	</div>
</body>
</html>