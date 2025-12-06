<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
        /* CSS Cơ bản cho trang chi tiết */
        body { background-color: #0f172a; color: white; font-family: sans-serif; margin: 0; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        
        /* Phần thông tin phim */
        .movie-hero { display: flex; gap: 40px; margin-bottom: 40px; }
        .movie-poster img { width: 300px; border-radius: 10px; box-shadow: 0 0 20px rgba(0,0,0,0.5); }
        .movie-info h1 { font-size: 2.5em; color: #facc15; margin-top: 0; }
        .meta-data { color: #94a3b8; margin-bottom: 20px; font-size: 0.9em; }
        .meta-data span { margin-right: 15px; border: 1px solid #334155; padding: 5px 10px; border-radius: 5px; }
        .desc { line-height: 1.6; color: #cbd5e1; }
        
        /* Phần lịch chiếu */
        .booking-section { background: #1e293b; padding: 20px; border-radius: 10px; }
        
        /* Tabs Ngày */
        .date-tabs { display: flex; gap: 10px; overflow-x: auto; padding-bottom: 15px; border-bottom: 1px solid #334155; }
        .date-tab {
            padding: 10px 20px;
            background: #334155;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            white-space: nowrap;
            transition: 0.3s;
        }
        .date-tab:hover { background: #475569; }
        .date-tab.active { background: #facc15; color: #000; font-weight: bold; }
        
        /* Grid Suất chiếu */
        .showtime-grid { margin-top: 20px; }
        .format-label { color: #94a3b8; margin-bottom: 10px; font-weight: bold; }
        .time-list { display: flex; flex-wrap: wrap; gap: 15px; }
        
        .time-btn {
            background: white;
            color: #0f172a;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            border: 2px solid transparent;
        }
        .time-btn:hover { border-color: #facc15; transform: translateY(-2px); }
        .time-btn span { display: block; font-size: 0.8em; color: #64748b; font-weight: normal; }
    </style>
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
                <img src="${movie.posterUrl}" alt="${movie.title}">
                <a href="${movie.trailerUrl}" target="_blank" style="display:block; text-align:center; margin-top:10px; color:#facc15; text-decoration:none;">
                    <i class="fas fa-play-circle"></i> Xem Trailer
                </a>
            </div>
            
            <div class="movie-info">
                <h1>${movie.title}</h1>
                <div class="meta-data">
                    <span>${movie.ageWarning}</span>
                    <span>${movie.duration} phút</span>
                    <span><fmt:formatDate value="${movie.releaseDate}" pattern="dd/MM/yyyy"/></span>
                </div>
                <p class="desc">${movie.description}</p>
                <p><strong>Đạo diễn/Diễn viên:</strong> Đang cập nhật...</p>
            </div>
        </div>
        
      <section class="showtime-section" id="booking">
      <div class="container">
        
        <div class="section-block">
          <h2>Lịch Chiếu</h2>
          <div class="options date-options">
            <c:if test="${empty showDates}">
                <p style="color: #999;">Chưa có lịch chiếu nào.</p>
            </c:if>

            <c:forEach items="${showDates}" var="d">
                <c:set var="isActive" value="${d.toString() == selectedDate.toString() ? 'active' : ''}" />
                
                <fmt:formatDate value="${d}" pattern="dd/MM" var="dayFormat"/>
                <a href="movie-detail?id=${movie.movieId}&date=${d}#booking" class="date-btn ${isActive}" style="text-decoration: none;">
                   <span class="day">${dayFormat}</span>
                   <span class="weekday">
                        <c:if test="${d.time <= (now.time + 86400000)}">Đang chọn</c:if>
                   </span>
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
        
        <c:if test="${empty showtimes}">
            <div style="text-align:center; padding: 20px;">Vui lòng chọn ngày khác.</div>
        </c:if>

        <c:forEach items="${showtimes}" var="s" varStatus="status">
            
            <c:if test="${s.cinemaName ne lastCinema}">
                <c:if test="${not empty lastCinema}">
                        </div> </div> </div> </c:if>

                <div class="theater-block">
                    <h3>${s.cinemaName}</h3>
                    <c:set var="lastCinema" value="${s.cinemaName}" />
                    <c:set var="lastType" value="" /> </c:if>

            <c:if test="${s.optionType ne lastType}">
                <c:if test="${not empty lastType}">
                    </div> </div> </c:if>

                <div class="showtime-row">
                    <div class="format">2D ${s.optionType}</div>
                    <div class="showtime-buttons">
                    <c:set var="lastType" value="${s.optionType}" />
            </c:if>

            <a href="seat-selection?showtimeId=${s.showtimeId}" class="time-btn" style="text-decoration:none;">
                <fmt:formatDate value="${s.startTime}" pattern="HH:mm"/>
            </a>
            
            <c:if test="${status.last}">
                    </div> </div> </div> </c:if>
            
        </c:forEach>

      </div>
    </section>


</body>
</html>