<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>${movie.title} | MovieGO</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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

        <div class="booking-section">
            <h3>Lịch Chiếu</h3>
            
            <div class="date-tabs">
                <c:if test="${empty showDates}">
                    <p>Chưa có lịch chiếu cho phim này.</p>
                </c:if>
                
                <c:forEach items="${showDates}" var="d">
                    <c:set var="isActive" value="${d.toString() == selectedDate.toString() ? 'active' : ''}" />
                    
                    <a href="movie-detail?id=${movie.movieId}&date=${d}" class="date-tab ${isActive}">
                        <fmt:formatDate value="${d}" pattern="dd/MM"/>
                        </a>
                </c:forEach>
            </div>

            <c:if test="${not empty showtimes}">
                <div class="showtime-grid">
                    <p class="format-label">Giờ chiếu:</p>
                    <div class="time-list">
                        <c:forEach items="${showtimes}" var="s">
                            <a href="seat-selection?showtimeId=${s.showtimeId}" class="time-btn">
                                <fmt:formatDate value="${s.startTime}" pattern="HH:mm"/>
                                <span>${s.optionType}</span>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${empty showtimes && not empty showDates}">
                <p style="margin-top:20px;">Vui lòng chọn ngày để xem giờ chiếu.</p>
            </c:if>
        </div>
    </div>

</body>
</html>