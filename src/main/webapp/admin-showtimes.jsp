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

<style type="text/css">

@charset "UTF-8";

/* =========================================
   PHẦN I: LAYOUT & SIDEBAR (GIỮ NGUYÊN)
   ========================================= */
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: "Roboto", sans-serif;
}

body {
	display: flex;
	background-color: #f1f5f9;
}

.sidebar {
	width: 260px;
	background-color: #0f172a;
	color: white;
	display: flex;
	flex-direction: column;
	position: fixed;
	height: 100%;
}

.sidebar-header {
	padding: 30px 20px;
	text-align: center;
	border-bottom: 1px solid #334155;
}

.sidebar-header h2 {
	font-size: 24px;
	font-weight: 700;
}

.sidebar-header h2 span {
	color: #ff3366;
}

.sidebar-menu {
	padding: 20px 0px;
}

.sidebar-menu a {
	display: flex;
	align-items: center;
	padding: 15px 25px;
	color: #94a3b8;
	text-decoration: none;
	font-size: 16px;
	transition: 0.3s;
	border-left: 4px solid transparent;
}

.sidebar-menu a i {
	margin-right: 15px;
	width: 20px;
	text-align: center;
}

.sidebar-menu a:hover, .sidebar-menu a.active {
	background-color: #1e293b;
	color: #fff;
	border-left-color: #ff3366;
}

.main-content {
	margin-left: 260px;
	flex: 1;
	padding: 30px;
}

.header-title {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 30px;
}

.header-title h1 {
	font-size: 24px;
	color: #1e293b;
}

.user-admin {
	color: #64748b;
	font-weight: 500;
}

.toolbar {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 25px;
	background: white;
	padding: 20px; /* Tăng padding cho thoáng */
	border-radius: 10px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.btn-add {
	background-color: #ff3366;
	color: white;
	padding: 10px 20px;
	border-radius: 6px;
	text-decoration: none;
	font-weight: 600;
	display: flex;
	align-items: center;
	gap: 8px;
	border: none;
	cursor: pointer;
	transition: background 0.3s;
}

.btn-add:hover {
	background-color: #e11d48;
}

.search-box {
	display: flex;
	align-items: center;
	background: #f8fafc;
	border-radius: 6px;
	padding: 8px 15px;
	border: 1px solid #cbd5e1;
}

/* =========================================
   PHẦN II: TABLE & DATA (PHẦN MỚI THÊM)
   ========================================= */

/* 1. Khung chứa bảng */
.recent-grid {
	background: white;
	padding: 20px;
	border-radius: 10px;
	box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

/* 2. Định dạng bảng chung */
table {
	width: 100%;
	border-collapse: collapse; /* Quan trọng: Gộp đường viền để gọn */
	margin-top: 10px;
}

/* 3. Tiêu đề cột (thead) */
thead tr {
	border-bottom: 2px solid #e2e8f0;
	text-align: left;
}

th {
	padding: 15px 10px; /* Tạo khoảng cách trên dưới cho tiêu đề */
	color: #64748b;
	font-size: 0.85rem;
	font-weight: 700;
	text-transform: uppercase;
	letter-spacing: 0.5px;
}

/* 4. Nội dung dòng (tbody) */
td {
	padding: 15px 10px; /* QUAN TRỌNG: Dãn cách nội dung ko bị dính */
	color: #334155;
	font-size: 0.95rem;
	border-bottom: 1px solid #f1f5f9; /* Đường kẻ mờ giữa các dòng */
	vertical-align: middle; /* Căn giữa theo chiều dọc */
}

/* Hiệu ứng khi di chuột vào dòng */
tbody tr:hover {
	background-color: #f8fafc;
}

/* 5. Badge trạng thái (Giờ chiếu) */
.status {
	padding: 6px 12px;
	border-radius: 20px;
	font-size: 0.8rem;
	font-weight: 600;
}

.status.success {
	background: #dcfce7; /* Xanh nhạt */
	color: #166534; /* Chữ xanh đậm */
}

.status.pending {
	background: #fff7ed; /* Cam nhạt */
	color: #9a3412; /* Chữ cam đậm */
}

/* 6. Nút hành động (Sửa/Xóa) */
.action-btn {
	display: inline-flex;
	justify-content: center;
	align-items: center;
	width: 32px;
	height: 32px;
	border-radius: 6px;
	text-decoration: none;
	margin-right: 5px;
	transition: all 0.2s;
}

.btn-edit {
	background: #eff6ff;
	color: #3b82f6;
}

.btn-edit:hover {
	background: #3b82f6;
	color: white;
}

.btn-delete {
	background: #fef2f2;
	color: #ef4444;
}

.btn-delete:hover {
	background: #ef4444;
	color: white;
}
</style>

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
                        <th>Xóa</th>
                    </tr>
                </thead>
                
     <tbody>
                    <c:forEach items="${listS}" var="st">
                        <tr>
                            <td>${st.showtimeId}</td>
                            
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