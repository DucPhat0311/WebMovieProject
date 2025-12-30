<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard - MovieGO Admin</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/base.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/pages/dashboard.css" />
</head>
<body>
	<jsp:include page="/views/admin/common/sidebar-admin.jsp">
		<jsp:param name="muc_hien_tai" value="dashboard" />
	</jsp:include>

	<div class="main-content">
		<!-- Dashboard Container -->
		<div class="dashboard-container">

			<!-- Dashboard Header -->
			<div class="dashboard-header">
				<div class="welcome-section">
					<h2>Xin chào, ${sessionScope.acc.username != null ? sessionScope.acc.username : 'Admin'}
						👋</h2>
					<p>Đây là tổng quan về hệ thống MovieGO của bạn</p>
				</div>
				<div class="date-display">
					<i class="fas fa-calendar-alt"></i>
					<jsp:useBean id="now" class="java.util.Date" />
					<fmt:formatDate value="${now}" pattern="EEEE, dd/MM/yyyy" />
				</div>
			</div>

			<!-- Stats Grid -->
			<div class="stats-grid">
				<!-- Revenue Card -->
				<div class="stat-card revenue">
					<div class="stat-icon">
						<i class="fas fa-money-bill-wave"></i>
					</div>
					<div class="stat-info">
						<div class="stat-value">
							<fmt:formatNumber value="${totalRevenue}" pattern="#,##0" />
							đ
						</div>
						<div class="stat-label">Doanh thu hôm nay</div>
						<div class="stat-change positive">
							<i class="fas fa-arrow-up"></i> Tổng doanh thu:
							<fmt:formatNumber value="${allTimeRevenue}" pattern="#,##0" />
							đ
						</div>
					</div>
				</div>

				<!-- Bookings Card -->
				<div class="stat-card bookings">
					<div class="stat-icon">
						<i class="fas fa-ticket-alt"></i>
					</div>
					<div class="stat-info">
						<div class="stat-value">${todayBookings}</div>
						<div class="stat-label">Vé đặt hôm nay</div>
						<div class="stat-change positive">
							<i class="fas fa-chart-line"></i> Đã thanh toán: ${successCount}
							| Chờ thanh toán: ${pendingCount}
						</div>
					</div>
				</div>

				<!-- Users Card -->
				<div class="stat-card users">
					<div class="stat-icon">
						<i class="fas fa-users"></i>
					</div>
					<div class="stat-info">
						<div class="stat-value">${totalUsers}</div>
						<div class="stat-label">Tổng người dùng</div>
						<div class="stat-change positive">
							<i class="fas fa-user-plus"></i> Người dùng hệ thống
						</div>
					</div>
				</div>

				<!-- Movies Card -->
				<div class="stat-card movies">
					<div class="stat-icon">
						<i class="fas fa-film"></i>
					</div>
					<div class="stat-info">
						<div class="stat-value">${totalMovies}</div>
						<div class="stat-label">Phim đang chiếu</div>
						<div class="stat-change positive">
							<i class="fas fa-play-circle"></i> Phim đang công chiếu
						</div>
					</div>
				</div>
			</div>

			<!-- Charts Section -->
			<div class="chart-container">
				<!-- Revenue Chart -->
				<div class="chart-card">
					<div class="chart-header">
						<h3 class="chart-title">Thống kê trạng thái</h3>
						<span class="chart-period">Hôm nay</span>
					</div>
					<div class="chart-placeholder">
						<i class="fas fa-chart-pie"></i>
						<div style="margin-top: 15px; text-align: left;">
							<div>
								<span class="dot" style="background: #10b981;"></span> Đã thanh
								toán: ${successCount}
							</div>
							<div>
								<span class="dot" style="background: #f59e0b;"></span> Chờ thanh
								toán: ${pendingCount}
							</div>
							<div>
								<span class="dot" style="background: #ef4444;"></span> Đã hủy:
								${cancelledCount}
							</div>
						</div>
					</div>
				</div>

				<!-- Booking Status Chart -->
				<div class="chart-card">
					<div class="chart-header">
						<h3 class="chart-title">Tổng quan doanh thu</h3>
						<span class="chart-period">Tất cả thời gian</span>
					</div>
					<div class="chart-placeholder">
						<i class="fas fa-chart-line"></i>
						<div style="margin-top: 15px;">
							<p style="font-size: 24px; font-weight: bold; color: #4f46e5;">
								<fmt:formatNumber value="${allTimeRevenue}" pattern="#,##0" />
								đ
							</p>
							<small>Tổng doanh thu hệ thống</small>
						</div>
					</div>
				</div>
			</div>

			<!-- Recent Activity & Top Movies -->
			<div class="recent-grid">
				<!-- Recent Activity -->
				<div class="activity-card">
					<div class="card-header">
						<h3 class="card-title">Hoạt động gần đây</h3>
						<a href="${pageContext.request.contextPath}/admin/manage-bookings"
							class="view-all"> Xem tất cả <i class="fas fa-arrow-right"></i>
						</a>
					</div>

					<div class="activity-list">
						<c:forEach items="${recentActivities}" var="activity"
							varStatus="status">
							<div class="activity-item">
								<div class="activity-icon ${activity.type}">
									<i class="${activity.icon}"></i>
								</div>
								<div class="activity-content">
									<div class="activity-title">${activity.title}</div>
									<div class="activity-time">${activity.time}</div>
								</div>
							</div>
						</c:forEach>

						<c:if test="${empty recentActivities}">
							<div class="empty-placeholder">
								<div class="empty-icon">
									<i class="fas fa-clock"></i>
								</div>
								<div class="empty-message">Chưa có hoạt động nào hôm nay</div>
							</div>
						</c:if>
					</div>
				</div>

				<!-- Top Movies -->
				<div class="top-movies-card">
					<div class="card-header">
						<h3 class="card-title">Phim đang chiếu</h3>
						<a href="${pageContext.request.contextPath}/admin/manage-movies"
							class="view-all"> Xem tất cả <i class="fas fa-arrow-right"></i>
						</a>
					</div>

					<div class="movies-list">
						<c:forEach items="${topMovies}" var="movie" varStatus="status">
							<div class="movie-item">
								<c:choose>
									<c:when test="${not empty movie.posterUrl}">
										<img
											src="${pageContext.request.contextPath}/assets/img/movies/${movie.posterUrl}"
											alt="${movie.title}" class="movie-poster">
									</c:when>
									<c:otherwise>
										<div class="movie-poster placeholder">
											<i class="fas fa-film"></i>
										</div>
									</c:otherwise>
								</c:choose>
								<div class="movie-info">
									<div class="movie-title">${movie.title}</div>
									<div class="movie-stats">
										<span><i class="far fa-clock"></i> ${movie.duration}
											phút</span>
										<c:if test="${not empty movie.releaseDate}">
											<span><i class="far fa-calendar"></i> <fmt:formatDate
													value="${movie.releaseDate}" pattern="dd/MM" /> </span>
										</c:if>
									</div>
								</div>
							</div>
						</c:forEach>

						<c:if test="${empty topMovies}">
							<div class="empty-placeholder">
								<div class="empty-icon">
									<i class="fas fa-film"></i>
								</div>
								<div class="empty-message">Chưa có phim nào đang chiếu</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>

	<style>
.dot {
	display: inline-block;
	width: 12px;
	height: 12px;
	border-radius: 50%;
	margin-right: 10px;
}

.movie-poster.placeholder {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	display: flex;
	align-items: center;
	justify-content: center;
	color: white;
	font-size: 20px;
}

.alert {
	margin-bottom: 20px;
}
</style>

	<script>
		// Cập nhật thời gian hiển thị
		function updateTime() {
			const now = new Date();
			const days = ['Chủ nhật', 'Thứ hai', 'Thứ ba', 'Thứ tư', 'Thứ năm', 'Thứ sáu', 'Thứ bảy'];
			const day = days[now.getDay()];
			const date = now.getDate().toString().padStart(2, '0');
			const month = (now.getMonth() + 1).toString().padStart(2, '0');
			const year = now.getFullYear();
			
			document.querySelector('.date-display').innerHTML = 
				`<i class="fas fa-calendar-alt"></i> ${day}, ${date}/${month}/${year}`;
		}
		
		// Cập nhật thời gian ban đầu và mỗi phút
		updateTime();
		setInterval(updateTime, 60000);
		
		// Thêm hiệu ứng hover cho thẻ thống kê
		document.addEventListener('DOMContentLoaded', function() {
			const statCards = document.querySelectorAll('.stat-card');
			statCards.forEach(card => {
				card.addEventListener('mouseenter', function() {
					this.style.transform = 'translateY(-5px)';
					this.style.boxShadow = '0 5px 15px rgba(0,0,0,0.12)';
				});
				
				card.addEventListener('mouseleave', function() {
					this.style.transform = 'translateY(0)';
					this.style.boxShadow = '0 2px 8px rgba(0,0,0,0.08)';
				});
			});
			
			// Tự động ẩn thông báo lỗi sau 5 giây
			setTimeout(function() {
				const alerts = document.querySelectorAll('.alert');
				alerts.forEach(alert => {
					alert.style.transition = 'opacity 0.5s';
					alert.style.opacity = '0';
					setTimeout(() => alert.remove(), 500);
				});
			}, 5000);
		});
	</script>
</body>
</html>