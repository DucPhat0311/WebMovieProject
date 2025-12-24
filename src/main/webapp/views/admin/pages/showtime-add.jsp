<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Thêm Lịch Chiếu Mới</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/pages/showtime-list.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/pages/showtime-add.css" />
</head>
<body>

	<div class="main-content" style="margin-left: 0; width: 100%;">
		<div class="form-container">
			<h2 style="text-align: center; margin-bottom: 25px; color: #0f172a;">Tạo
				Lịch Chiếu Mới</h2>

			<c:if test="${not empty error}">
				<div class="alert-error">${error}</div>
			</c:if>

			<form action="add-showtime" method="post">
				<div class="form-group">
					<label>Chọn Phim</label> <select name="movieId" required>
						<option value="">-- Chọn phim --</option>
						<c:forEach items="${movies}" var="m">
							<option value="${m.movieId}">${m.title}(Duration:
								${m.duration}p)</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group">
					<label>Chọn Phòng Chiếu (Rạp)</label> <select name="roomId"
						required>
						<option value="">-- Chọn phòng --</option>
						<option value="1">Galaxy Nguyễn Du - Room 1</option>
						<option value="2">Galaxy Nguyễn Du - Room 2</option>
						<option value="6">Galaxy Tân Bình - Room 6</option>
					</select>
				</div>

				<div class="form-group" style="display: flex; gap: 15px;">
					<div style="flex: 1;">
						<label>Ngày Chiếu</label> <input type="date" name="showDate"
							required>
					</div>
					<div style="flex: 1;">
						<label>Giờ Bắt Đầu</label> <input type="time" name="startTime"
							required>
					</div>
				</div>

				<div class="form-group" style="display: flex; gap: 15px;">
					<div style="flex: 1;">
						<label>Giá Vé (VND)</label> <input type="number" name="price"
							value="90000" min="0" step="1000" required>
					</div>
					<div style="flex: 1;">
						<label>Loại Chiếu</label> <select name="optionType">
							<option value="Phu De">Phụ Đề</option>
							<option value="Long tieng">Lồng Tiếng</option>
						</select>
					</div>
				</div>

				<button type="submit" class="btn-submit">Lưu Lịch Chiếu</button>
				<a href="manage-showtimes"
					style="display: block; text-align: center; margin-top: 15px; color: #64748b; text-decoration: none;">Hủy
					bỏ</a>
			</form>
		</div>
	</div>

</body>
</html>