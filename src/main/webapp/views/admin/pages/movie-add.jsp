<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Thêm Phim Mới</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/pages/movie-add.css" />
</head>
<body>
	<div class="container">
		<h2 align="center">Thêm Phim Mới</h2>

		<form method="post" action="add-movie" enctype="multipart/form-data">

			<label>Tên phim:</label> <input type="text" name="title" required
				placeholder="Ví dụ: Avengers: Endgame" /> <label>Mô tả:</label>
			<textarea name="description" rows="4"></textarea>

			<label>Thời lượng (phút):</label> <input type="number"
				name="duration" required value="90" /> <label>Ngày khởi
				chiếu:</label> <input type="date" name="releaseDate" required /> <label>Phân
				loại tuổi:</label> <select name="ageWarning" required
				style="width: 100%; padding: 10px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px;">
				<option value="" disabled selected>-- Chọn phân loại --</option>
				<option value="P">P - Phổ biến mọi độ tuổi</option>
				<option value="T13">T13 - Khán giả trên 13 tuổi</option>
				<option value="T16">T16 - Khán giả trên 16 tuổi</option>
				<option value="T18">T18 - Khán giả trên 18 tuổi</option>
			</select> <label>Ảnh:</label> <input type="file" name="poster" required
				accept="image/*" /> <label>Trailer URL:</label> <input type="text"
				name="trailerUrl" />

			<button type="submit" class="btn-submit">Lưu Phim</button>
		</form>

		<br>
		<div style="text-align: center;">
			<a href="${pageContext.request.contextPath}/admin/manage-movies">Quay
				lại danh sách</a>
		</div>
	</div>
</body>
</html>