<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>MovieGO!</title>
<link rel="stylesheet" href="homepage_style.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
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

		<div class="search-login">
			<input type="text" placeholder="Tìm kiếm" />
			<button class="login-btn">Đăng nhập</button>
		</div>
	</header>

	<section class="hero">
		<div class="hero-content">
			<img src="../picture/Marvel_Studios_logo-removebg-preview.png"
				alt="Marvel Studios Logo" class="studio-logo" />
			<h1>
				Avengers<br />Doomsday
			</h1>

			<div class="movie-info">Hành động | Phiêu lưu | Khoa học viễn
				tưỡng • 2026 • ⏱ 2h 8m</div>

			<p class="description">Sau những mất mát tàn khốc, các Avenger
				còn lại đối mặt với kẻ thù mạnh nhất mà vũ trụ từng biết — Doomsday,
				sinh vật hủy diệt mang sức mạnh vượt ngoài mọi giới hạn. Trái đất
				đang trên bờ diệt vong… và lần này, không phải ai cũng trở về.</p>

			<a href="#now-showing" class="explore-btn"> XEM THÊM <span>→</span>
			</a>
		</div>
	</section>

	<!-- QUICK BOOKING BAR -->
	<section class="quick-booking">
		<div class="booking-container">
			<select>
				<option>1. Chọn Phim</option>
				<option>Avengers: Doomsday</option>
				<option>Inside Out 2</option>
				<option>Venom: The Last Dance</option>
			</select> <select>
				<option>2. Chọn Rạp</option>
				<option>Cinema Tân Bình</option>
				<option>Cinema Đống Đa</option>
				<option>Cinema Thủ Đức</option>
				<option>Cinema Đà Nẵng</option>
				<option>Cinema Vũng Tàu</option>
			</select> <select>
				<option>3. Chọn Ngày</option>
				<option>20/10/2025</option>
				<option>21/10/2025</option>
				<option>22/10/2025</option>
				<option>23/10/2025</option>
				<option>24/10/2025</option>
			</select> <select>
				<option>4. Chọn Suất</option>
				<option>09:30</option>
				<option>13:45</option>
				<option>19:00</option>
			</select>

			<button class="buy-btn">Mua vé nhanh</button>
		</div>
	</section>

	<section id="now-showing">
		<div class="section-header">
			<h2>PHIM ĐANG CHIẾU</h2>
			<a href="movies?type=now">Xem tất cả</a>
		</div>

		<div class="movie-grid">
			<c:forEach items="${nowShowingList}" var="m">
				<div class="movie-card">
					<img src="${m.posterUrl}" alt="${m.title}" />

					<div class="age-badge">${m.ageWarning}</div>

					<div class="overlay">
						<a href="movie-detail?id=${m.movieId}" class="buy-btn">Mua vé</a>
					</div>

					<div class="movie-info">
						<h3>${m.title}</h3>
					</div>
				</div>
			</c:forEach>
			<c:if test="${empty nowShowingList}">
				<p style="color: white; text-align: center;">Hiện chưa có phim
					đang chiếu.</p>
			</c:if>
		</div>
	</section>

	<section id="coming-soon">
		<div class="section-header">
			<h2>PHIM SẮP CHIẾU</h2>
			<a href="movies?type=coming">Xem tất cả</a>
		</div>

		<div class="movie-grid">
			<c:forEach items="${comingSoonList}" var="m">
				<div class="movie-card">
					<img src="${m.posterUrl}" alt="${m.title}" />

					<div class="age-badge">${m.ageWarning}</div>

					<div class="overlay">
						<a href="movie-detail?id=${m.movieId}" class="buy-btn">Chi
							tiết</a>
					</div>

					<div class="movie-info">
						<h3>${m.title}</h3>
					</div>
				</div>
			</c:forEach>
		</div>
	</section>


	<footer>
		<div class="container">
			<div class="wrapper">
				<!-- LOGO + MÔ TẢ -->
				<div class="footer-widget">
					<a href="#"> <img src="../picture/change_it_into_my_logo.jpeg"
						class="logo" alt="MovieGO Logo" />
					</a>
					<p class="desc">MovieGO là nền tảng đặt vé xem phim trực tuyến
						hàng đầu, mang đến cho bạn trải nghiệm xem phim dễ dàng, nhanh
						chóng và tiện lợi. Cập nhật liên tục các suất chiếu, trailer và
						đánh giá phim mới nhất.</p>
					<ul class="socials">
						<li><a href="#"><i class="fab fa-facebook-f"></i></a></li>
						<li><a href="#"><i class="fab fa-twitter"></i></a></li>
						<li><a href="#"><i class="fab fa-instagram"></i></a></li>
						<li><a href="#"><i class="fab fa-linkedin-in"></i></a></li>
						<li><a href="#"><i class="fab fa-youtube"></i></a></li>
					</ul>
				</div>

				<!-- GIỚI THIỆU -->
				<div class="footer-widget">
					<h6>GIỚI THIỆU</h6>
					<ul class="links">
						<li><a href="#">Về Chúng Tôi</a></li>
						<li><a href="#">Thỏa Thuận Sử Dụng</a></li>
						<li><a href="#">Chính Sách Bảo Mật</a></li>
						<li><a href="#">Liên Hệ Hợp Tác</a></li>
						<li><a href="#">Điều Khoản Giao Dịch</a></li>
					</ul>
				</div>

				<!-- GÓC ĐIỆN ẢNH -->
				<div class="footer-widget">
					<h6>GÓC ĐIỆN ẢNH</h6>
					<ul class="links">
						<li><a href="#">Thể Loại Phim</a></li>
						<li><a href="#">Bình Luận Phim</a></li>
						<li><a href="#">Phim Đang Chiếu</a></li>
						<li><a href="#">Phim Sắp Chiếu</a></li>
						<li><a href="#">Top Phim Hot</a></li>
					</ul>
				</div>

				<!-- HỖ TRỢ -->
				<div class="footer-widget">
					<h6>HỖ TRỢ</h6>
					<ul class="links">
						<li><a href="#">Góp Ý & Liên Hệ</a></li>
						<li><a href="#">Hướng Dẫn Đặt Vé</a></li>
						<li><a href="#">Chính Sách Đổi / Hủy Vé</a></li>
						<li><a href="#">Rạp / Giá Vé</a></li>
						<li><a href="#">Tuyển Dụng</a></li>
						<li><a href="#">Câu Hỏi Thường Gặp (FAQ)</a></li>
					</ul>
				</div>

				<div class="copyright-wrapper">
					<p>
						© 2025 MovieGO! - Design and Developed by <a href="#"
							target="_blank">MovieGO Team</a>. All rights reserved.
					</p>
					<p>Giấy phép kinh doanh số: 1234/GP-STTTT - Cấp bởi Sở Thông
						tin và Truyền thông TP.HCM</p>
				</div>
			</div>
	</footer>
</body>
</html>
