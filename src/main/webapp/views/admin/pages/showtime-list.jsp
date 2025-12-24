<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="root" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý Lịch Chiếu</title>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/base.css" />

</head>
<body>

	<jsp:include page="/views/admin/common/sidebar-admin.jsp">
		<jsp:param name="muc_hien_tai" value="showtimes" />
	</jsp:include>

	<div class="admin-container" style="display: flex;">
		<div class="main-content" style="flex: 1; padding: 20px;">
			<div class="header-title">
				<h1>Quản lý lịch chiếu</h1>
				<div class="user-admin">
					<i class="fas fa-user-circle"></i> Xin chào,
					${sessionScope.currentUser.fullName != null ? sessionScope.currentUser.fullName : 'Admin'}
				</div>
			</div>

			<div class="toolbar">
				<a href="${root}/admin/add-showtime" class="btn-add"> <i
					class="fas fa-plus"></i> Tạo Lịch Chiếu
				</a>

				<form action="${root}/admin/manage-showtimes" method="get">
					<div class="search-box">
						<input type="date" name="date" value="${param.date}"
							style="border: none; color: #334155; outline: none; background: transparent;"
							onchange="this.form.submit()" />
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
							<th>Hành động</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${listS}" var="st">
							<tr>
								<td>${st.showtimeId}</td>

								<td style="font-weight: 600; color: #0f172a">
									${st.movie.title}</td>

								<td>${st.room.cinema.cinemaName}<br /> <small
									style="color: #64748b">${st.room.roomName}</small>
								</td>

								<td><fmt:formatDate value="${st.showDate}"
										pattern="dd/MM/yyyy" /></td>

								<td><span
									class="status ${st.startTime.toString() > '12:00:00' ? 'success' : 'pending'}">
										<fmt:formatDate value="${st.startTime}" pattern="HH:mm" />
								</span></td>

								<td style="font-weight: bold;"><fmt:formatNumber
										value="${st.basePrice}" type="currency" currencySymbol="₫" />
								</td>

								<td><a href="#" onclick="confirmDelete('${st.showtimeId}')"
									class="action-btn btn-delete"> <i class="fas fa-trash"></i>
								</a></td>
							</tr>
						</c:forEach>

						<c:if test="${empty listS}">
							<tr>
								<td colspan="7"
									style="text-align: center; padding: 20px; color: #888;">
									Không tìm thấy lịch chiếu nào.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script>
		function confirmDelete(id) {
			if (confirm("Bạn có chắc chắn muốn xóa lịch chiếu này không?")) {
				window.location.href = "${root}/admin/delete-showtime?id=" + id;
			}
		}
	</script>
</body>
</html>