<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>${movie.title}|MovieGO</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/movie_detail_style.css" />
<!-- sửa tạm -->

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body>
	<section class="movie-detail">
		<div class="container">
			<div class="movie-hero">
				<div class="movie-poster">
					<img src="${pageContext.request.contextPath}/assets/img/movies/${movie.posterUrl}"
						alt="${movie.title}"> <!-- sua tam -->
						<a style="display: block; text-align: center; margin-top: 10px; color: #ff3366; text-decoration: none;">
					</a>
				</div>

				<div class="movie-info">
					<h1>${movie.title}</h1>
					<div class="meta-data">
						<span>${movie.ageWarning}</span> <span>${movie.duration}
							phút</span> <span><fmt:formatDate value="${movie.releaseDate}"
								pattern="dd/MM/yyyy" /></span>
					</div>
					<p class="desc">${movie.description}</p>
					<p>
					<p>
						<strong>Thể loại: </strong>
						<c:if test="${empty movie.genres}">
							<span>Đang cập nhật</span>
						</c:if>

						<c:forEach items="${movie.genres}" var="genre" varStatus="status">
        ${genre.name}${!status.last ? ', ' : ''}
    </c:forEach>
					</p>

					<strong>Đạo diễn: </strong>
					<c:if test="${empty movie.directors}">
						<span>Đang cập nhật</span>
					</c:if>
					<c:forEach items="${movie.directors}" var="director"
						varStatus="status">
            ${director.name}${!status.last ? ', ' : ''}
        </c:forEach>
					</p>

					<p>
						<strong>Diễn viên: </strong>
						<c:if test="${empty movie.actors}">
							<span>Đang cập nhật</span>
						</c:if>
						<c:forEach items="${movie.actors}" var="actor" varStatus="status"
							end="4">
            ${actor.name}${!status.last ? ', ' : ''}
        </c:forEach>

						<%-- Trailer --%>

						<c:if test="${movie.actors.size() > 5}">...</c:if>
					</p>
					<c:set var="embedUrl"
						value="${movie.trailerUrl.replace('watch?v=', 'embed/')}" />

					<div style="margin-top: 25px;">
						<button class="btn-watch-trailer" onclick="openTrailer()">
							<i class="fas fa-play-circle"></i> XEM TRAILER
						</button>
					</div>

				</div>
			</div>
		</div>
	</section>

	<div id="trailerModal" class="modal-overlay" onclick="closeTrailer()">
		<div class="modal-content" onclick="event.stopPropagation()">
			<span class="close-btn" onclick="closeTrailer()">Đóng</span>

			<iframe id="youtubeVideo" width="100%" height="100%"
				data-src="${embedUrl}?autoplay=1" frameborder="0"
				allow="autoplay; encrypted-media" allowfullscreen> </iframe>
		</div>
	</div>

	<script>
		function openTrailer() {
			var modal = document.getElementById("trailerModal");
			var iframe = document.getElementById("youtubeVideo");
			// Lấy link từ data-src gán vào src để chạy video
			iframe.src = iframe.getAttribute("data-src");
			modal.style.display = "flex";
		}

		function closeTrailer() {
			var modal = document.getElementById("trailerModal");
			var iframe = document.getElementById("youtubeVideo");
			// Xóa src để tắt tiếng video ngay lập tức
			iframe.src = "";
			modal.style.display = "none";
		}
	</script>


	<section class="showtime-section" id="booking-area">
		<div class="section-block">
			<h2>Lịch Chiếu</h2>

			<div class="options date-options">
				<c:if test="${empty showDates}">
					<p style="color: #ccc;">Chưa có lịch chiếu cho phim này.</p>
				</c:if>

				<c:forEach items="${showDates}" var="d">
					<c:set var="isActive"
						value="${d.toString() == selectedDate.toString() ? 'active' : ''}" />

					<a href="movie-detail?id=${movie.movieId}&date=${d}#booking-area"
						class="date-btn ${isActive}" style="text-decoration: none;"> <span
						class="day"><fmt:formatDate value="${d}" pattern="dd/MM" /></span>
						<span class="weekday"><fmt:formatDate value="${d}"
								pattern="E" /></span>
					</a>
				</c:forEach>
			</div>

			<div class="filters">
				<button class="filter-btn">Toàn quốc ▼</button>
				<button class="filter-btn">Tất cả rạp ▼</button>
			</div>
		</div>

		<c:set var="lastCinema" value="" />
		<c:set var="lastType" value="" />

		<c:if test="${empty showtimes && not empty showDates}">
			<div style="text-align: center; padding: 20px;">Vui lòng chọn
				ngày để xem giờ chiếu.</div>
		</c:if>

		<c:forEach items="${showtimes}" var="s" varStatus="status">

			<c:if test="${s.cinemaName ne lastCinema}">
				<c:if test="${not empty lastCinema}">
					</div>
					</div>
					</div>
				</c:if>

				<div class="theater-block">
					<h3>${s.cinemaName}</h3>
					<c:set var="lastCinema" value="${s.cinemaName}" />
					<c:set var="lastType" value="" />
			</c:if>

			<c:if test="${s.optionType ne lastType or empty lastType}">
				<c:if test="${not empty lastType}">
					</div>
					</div>
				</c:if>

				<div class="showtime-row">
					<div class="format">${s.optionType}</div>
					<div class="showtime-buttons">
						<c:set var="lastType" value="${s.optionType}" />
			</c:if>

			<a href="seat-selection?showtimeId=${s.showtimeId}" class="time-btn"
				style="text-decoration: none; display: inline-block;"> <fmt:formatDate
					value="${s.startTime}" pattern="HH:mm" />
			</a>

			<c:if test="${status.last}">
				</div>
				</div>
				</div>
			</c:if>

		</c:forEach>
	</section>
	</div>

</body>
</html>