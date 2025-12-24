<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản Lý Phim - MovieGO</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/base.css" />
</head>
<body>

	<jsp:include page="/views/admin/common/sidebar-admin.jsp">
		<jsp:param name="muc_hien_tai" value="movies" />
	</jsp:include>

	<div class="main-content">
		<div class="header-title">
			<h1>Quản Lý Phim</h1>
			<div class="user-admin">
				<i class="fas fa-user-circle"></i> Xin chào,
				${sessionScope.acc.username != null ? sessionScope.acc.username : 'Admin'}
			</div>
		</div>

		<div class="toolbar">
			<a href="add-movie" class="btn-add"> <i class="fas fa-plus"></i>
				Thêm Phim Mới
			</a>

			<form action="search-movie" method="get">
				<div class="search-box">
					<button type="submit"
						style="border: none; background: transparent; cursor: pointer;">
						<i class="fas fa-search" style="color: #94a3b8"></i>
					</button>
					<input type="text" name="txt" value="${searchValue}"
						placeholder="Tìm tên phim..." />
				</div>
			</form>
		</div>

		<div class="recent-grid">
			<table>
				<thead>
					<tr>
						<th>ID</th>
						<th>Poster</th>
						<th>Tên Phim</th>
						<th>Thời Lượng</th>
						<th>Ngày Chiếu</th>
						<th>Trạng Thái</th>
						<th>Hành Động</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${listM}" var="o">
						<tr>
							<td>#${o.movieId}</td>

							<td><img
								src="${pageContext.request.contextPath}/assets/img/movies/movie${o.movieId}.jpg"
								class="poster-img" alt="${o.title}"
								onerror="this.src='https://via.placeholder.com/150?text=No+Image';" />
							</td>

							<td><b>${o.title}</b><br /> <span
								style="color: #888; font-size: 12px">${o.ageWarning}</span></td>

							<td>${o.duration}phút</td>

							<td><fmt:formatDate value="${o.releaseDate}"
									pattern="dd/MM/yyyy" /></td>

							<td><jsp:useBean id="now" class="java.util.Date" /> <c:choose>
									<c:when test="${o.releaseDate le now}">
										<span class="status success">Đang chiếu</span>
									</c:when>
									<c:otherwise>
										<span class="status pending">Sắp chiếu</span>
									</c:otherwise>
								</c:choose></td>

							<td><a href="#" onclick="showDeleteConfirm('${o.movieId}')"
								class="action-btn btn-delete" title="Xóa"> <i
									class="fas fa-trash"></i>
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>

		<script>
			function showDeleteConfirm(id) {
				if (confirm("Bạn có chắc chắn muốn xóa phim có ID: " + id
						+ " không?")) {
					window.location.href = "delete-movie?pid=" + id;
				}
			}
		</script>
</body>
</html>