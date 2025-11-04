<%@ page import="java.util.ArrayList"%>
<%@ page import="example.model.Movie"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
ArrayList<Movie> list = (ArrayList<Movie>) request.getAttribute("movieList");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Movie Detail - QuickShow</title>
<link rel="stylesheet" href="all_movies_style.css" />
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

	<section class="movie-section">
		<div class="tabs">
			<h2>PHIM</h2>
			<div class="tab-menu">
				<span class="active">Đang chiếu</span> <span>Sắp chiếu</span>
			</div>
		</div>

		<!-- 🔻 Phần phim đang chiếu -->
		<section id="now-showing">
			<div class="movie-grid">
				<%
				for (Movie m : list) {
				%>
				<div class="movie-card">
					<img src="<%=m.getPosterUrl()%>">
					<h3><%=m.getTitle()%></h3>
				</div>
				<%
				}
				%>
			</div>
		</section>
		

		<!-- ===== FOOTER =====  -->
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

