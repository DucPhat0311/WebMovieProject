<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Danh Sách Phim</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/base.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/header.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/footer.css" />

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/movie-card.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>

	<jsp:include page="/views/user/common/header.jsp" />


	<div class="container" style="padding-top: 100px;">
		<h1 style="color: white; margin-bottom: 20px;">
			<c:choose>
				<c:when test="${pageType == 'now'}">PHIM ĐANG CHIẾU</c:when>
				<c:otherwise>PHIM SẮP CHIẾU</c:otherwise>
			</c:choose>
		</h1>

		<div class="movie-grid" id="content-grid">
			<c:forEach items="${listM}" var="m">
				<div class="movie-card">
					<img
						src="${pageContext.request.contextPath}/assets/img/movies/${m.posterUrl}"
						alt="${m.title}" loading="lazy" />

					<div class="age-badge">${m.ageWarning}</div>

					<div class="overlay">
						<a
							href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
							class="buy-btn"> ${pageType == 'now' ? 'Mua vé' : 'Chi tiết'}
						</a>
					</div>

					<div class="movie-info">
						<h3>
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
								class="movie-title-link"> ${m.title} </a>
						</h3>
					</div>
				</div>
			</c:forEach>
		</div>

		<div style="text-align: center; margin: 30px 0;">
			<button onclick="loadMore()" id="btn-loadMore" class="explore-btn"
				style="cursor: pointer;">
				Xem thêm <i class="fas fa-chevron-down"></i>
			</button>
		</div>
	</div>

	<jsp:include page="/views/user/common/footer.jsp" />


	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script>
		function loadMore() {
			// Lấy loại phim từ biến server gửi sang (dùng EL)
			var type = '${pageType}';

			// Đếm số phim đang có trên màn hình
			var amount = document.getElementsByClassName("movie-card").length;

			$
					.ajax({
						url : "${pageContext.request.contextPath}/load-more",
						type : "get",
						data : {
							type : type,
							existed : amount
						},
						success : function(data) {
							if (data.trim() === "") {
								document.getElementById("btn-loadMore").style.display = "none";
								alert("Đã hiển thị hết phim!");
							} else {
								$("#content-grid").append(data);
							}
						},
						error : function(xhr) {
							console.log("Lỗi tải phim");
						}
					});
		}
	</script>
</body>
</html>