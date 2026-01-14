<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Hết thời gian - MovieGO!</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/auth/error.css">
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/base.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/auth/timeout.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
	<div class="timeout-container">
		<div class="timeout-card">

			<div class="timeout-icon">
				<i class="fas fa-clock"></i>
			</div>

			<h1 class="timeout-title">Đã hết thời gian!</h1>

			<p class="timeout-message">
				<c:choose>
					<c:when test="${not empty manualCancel and manualCancel}">
            Bạn đã hủy thanh toán thành công.<br>
            Các ghế đã được giải phóng và có thể được đặt bởi người khác.
        </c:when>
					<c:otherwise>
            Rất tiếc, thời gian giữ ghế của bạn đã hết.<br>
            Các ghế đã được giải phóng và có thể được đặt bởi người khác.
        </c:otherwise>
				</c:choose>
			</p>

			<c:if test="${not empty bookingId}">
				<div class="booking-info">
					<div class="info-row">
						<span class="info-label">Mã đặt vé:</span> <span
							class="info-value">#${bookingId}</span>
					</div>
					<div class="info-row">
						<span class="info-label">Trạng thái:</span> <span
							class="info-value status-cancelled">Đã hủy</span>
					</div>
				</div>
			</c:if>

			<div class="action-buttons">
				<a href="${pageContext.request.contextPath}/"
					class="btn btn-primary"> <i class="fas fa-home"></i> Về trang
					chủ
				</a> <a href="${pageContext.request.contextPath}/movie-list?type=now"
					class="btn btn-secondary"> <i class="fas fa-film"></i> Xem phim
					khác
				</a>
			</div>
		</div>
	</div>
</body>
</html>
