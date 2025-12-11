<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
<link href="admin-showtimes.css" rel="stylesheet" />
</head>
<body>
	<div class="sidebar">
		<div class="sidebar-header">
			<h2>
				Movie<span>GO!</span>
			</h2>
		</div>

		<div class="sidebar-menu">
			<a href="admin_dashboard.html"><i class="fas fa-home"></i> Tổng
				Quan</a> <a href="admin_movies.html"><i class="fas fa-film"></i>
				Quản Lý Phim</a> <a href="admin_showtimes.html" class="active"><i
				class="fas fa-calendar-alt"></i> Lịch Chiếu</a> <a
				href="admin_bookings.html"><i class="fas fa-ticket-alt"></i> Đơn
				Hàng</a> <a href="admin_users.html"><i class="fas fa-users"></i>
				Khách Hàng</a>
		</div>
	</div>

	<div class="main-content">
		<div class="header-title">
			<h1>Quan ly lich chieu</h1>
			<div class="user-admin">
				<i class="fas fa-user-circle"></i> Xin chào, check user hoac admin 
			</div>
		</div>

		<div class="toolbar">
			<a href="add-showtime" class="btn-add">
                <i class="fas fa-plus"></i> Tạo Lịch Chiếu
            </a>
            
            <form action="manage-showtimes" method="get">
            	<div class="search-box">
            		<input type="date" name"date" value="$param.date" style="border: none; color: #334155; outline: none; background: transparent;" onchange="this.form.submit()"/>		 
            	</div>	
            </form>
		</div>
		
		<div class="recent-grid">
            <table>
                <thead>
                    <tr>
                        <th>Mã Lịch</th>
                        <th>Phim</th>
                        <th>Rạp / Phòng</th>
                        <th>Ngày Chiếu</th>
                        <th>Giờ Bắt Đầu</th>
                        <th>Giá Vé</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                
     <tbody>
                    <c:forEach items="${listS}" var="st">
                        <tr>
                            <td>#${st.showtimeId}</td>
                            
                            <td style="font-weight: 600; color: #0f172a">
                                ${st.movie.title}
                            </td>
                            
                            <td>
                                ${st.room.cinema.cinemaName}<br />
                                <small style="color: #64748b">${st.room.roomName}</small>
                            </td>
                            
                            <td><fmt:formatDate value="${st.showDate}" pattern="dd/MM/yyyy"/></td>
                            
                            <td>
                                <span class="status ${st.startTime.toString() > '12:00:00' ? 'success' : 'pending'}">
                                    <fmt:formatDate value="${st.startTime}" pattern="HH:mm"/>
                                </span>
                            </td>
                            
                            <td style="font-weight: bold;">
                                <fmt:formatNumber value="${st.basePrice}" type="currency" currencySymbol="₫"/>
                            </td>
                            
                            <td>
                                <a href="edit-showtime?id=${st.showtimeId}" class="action-btn btn-edit">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="#" onclick="confirmDelete('${st.showtimeId}')" class="action-btn btn-delete">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty listS}">
                        <tr>
                            <td colspan="7" style="text-align: center; padding: 20px; color: #888;">
                                Không tìm thấy lịch chiếu nào.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
    
    <script>
        function confirmDelete(id) {
            if(confirm("Bạn có chắc chắn muốn xóa lịch chiếu này không?")) {
                window.location.href = "delete-showtime?id=" + id;
            }
        }
    </script>
</body>
</html>