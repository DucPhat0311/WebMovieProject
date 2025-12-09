<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/movie_detail_style.css" /><!-- sửa tạm -->

</head>
<body>
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

	<div class="container">
		<div class="movie-hero">
			<div class="movie-poster">
				<img src="${movie.posterUrl}" alt="${movie.title}"> <a
					href="${movie.trailerUrl}" target="_blank"
					style="display: block; text-align: center; margin-top: 10px; color: #facc15; text-decoration: none;">
					<i class="fas fa-play-circle"></i> Xem Trailer
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
					<strong>Đạo diễn/Diễn viên:</strong> Đang cập nhật...
				</p>
			</div>
		</div>
		
		<section class="showtime-section" id="booking-area">
            <div class="section-block">
                <h2>Lịch Chiếu</h2>

                <div class="options date-options">
                    <c:if test="${empty showDates}">
                        <p style="color: #ccc;">Chưa có lịch chiếu cho phim này.</p>
                    </c:if>

                    <c:forEach items="${showDates}" var="d">
                        <c:set var="isActive" value="${d.toString() == selectedDate.toString() ? 'active' : ''}" />
                        
                        <a href="movie-detail?id=${movie.movieId}&date=${d}#booking-area" class="date-btn ${isActive}" style="text-decoration:none;">
                            <span class="day"><fmt:formatDate value="${d}" pattern="dd/MM"/></span>
                            <span class="weekday"><fmt:formatDate value="${d}" pattern="E"/></span>
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
                <div style="text-align: center; padding: 20px;">Vui lòng chọn ngày để xem giờ chiếu.</div>
            </c:if>

            <c:forEach items="${showtimes}" var="s" varStatus="status">
                
                <c:if test="${s.cinemaName ne lastCinema}">
                    <c:if test="${not empty lastCinema}">
                            </div> </div> </div> </c:if>

                    <div class="theater-block">
                        <h3>${s.cinemaName}</h3>
                        <c:set var="lastCinema" value="${s.cinemaName}" />
                        <c:set var="lastType" value="" /> 
                </c:if>

                <c:if test="${s.optionType ne lastType or empty lastType}">
                    <c:if test="${not empty lastType}">
                        </div> </div> </c:if>

                    <div class="showtime-row">
                        <div class="format">2D ${s.optionType}</div> <div class="showtime-buttons">
                        <c:set var="lastType" value="${s.optionType}" />
                </c:if>

                <a href="seat-selection?showtimeId=${s.showtimeId}" class="time-btn" style="text-decoration:none; display:inline-block;">
                    <fmt:formatDate value="${s.startTime}" pattern="HH:mm"/>
                </a>

                <c:if test="${status.last}">
                        </div> </div> </div> </c:if>

            </c:forEach>
            </section>
    </div>
</body>
</html>