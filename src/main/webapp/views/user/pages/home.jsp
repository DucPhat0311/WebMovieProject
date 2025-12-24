<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>MovieGO!</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/base.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/header.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/footer.css" />

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/movie-card.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/pages/home.css" />

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
	<jsp:include page="/views/user/common/header.jsp" />

	<section class="hero">
		<div class="hero-content">
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
			<a href="movie-list?type=now">Xem tất cả</a>
		</div>

		<div class="movie-grid">
			<c:forEach items="${now8ShowingList}" var="m">
				<div class="movie-card">
					<div class="movie-img-wrapper">
						<img
							src="${pageContext.request.contextPath}/assets/img/movies/${m.posterUrl}"
							alt="${m.title}" loading="lazy" />

						<div class="age-badge">${m.ageWarning}</div>

						<div class="overlay">
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
								class="buy-btn">Mua vé</a>
						</div>
					</div>

					<div class="movie-info">
						<h3>
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
								class="movie-title-link"> ${m.title} </a>
						</h3>
					</div>
				</div>
			</c:forEach>
		</div>
	</section>

	<section id="coming-soon">
		<div class="section-header">
			<h2>PHIM SẮP CHIẾU</h2>
			<a href="movie-list?type=coming">Xem tất cả</a>
		</div>

		<div class="movie-grid">
			<c:forEach items="${coming8SoonList}" var="m">
				<div class="movie-card">
					<div class="movie-img-wrapper">
						<img
							src="${pageContext.request.contextPath}/assets/img/movies/${m.posterUrl}"
							alt="${m.title}" loading="lazy" />

						<div class="age-badge">${m.ageWarning}</div>

						<div class="overlay">
							<a href="movie-detail?id=${m.movieId}" class="buy-btn">Mua vé</a>
						</div>
					</div>
					<div class="movie-info">
						<h3>
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
								class="movie-title-link"> ${m.title} </a>
						</h3>
					</div>
				</div>
			</c:forEach>
		</div>
	</section>

	<jsp:include page="/views/user/common/footer.jsp" />

</body>
</html>
