<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thanh toán - MovieGO!</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/header.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/footer.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/base.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/pages/checkout.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>


	<jsp:include page="/views/user/common/header.jsp" />

	<c:if test="${not empty remainingSeconds}">
		<div class="timer-floating" id="timer">
			<i class="fas fa-clock"></i> <span id="countdown">${remainingSeconds}</span>
			giây còn lại
		</div>
	</c:if>

	<div class="checkout-container">
		<div class="checkout-header">
			<h1 class="checkout-title">
				<i class="fas fa-shopping-cart"></i> Thanh toán
			</h1>

			<c:if test="${not empty booking and booking.status == 'Pending'}">
				<form action="${pageContext.request.contextPath}/cancel-booking"
					method="post"
					onsubmit="return confirm('Bạn có chắc chắn muốn hủy thanh toán? Ghế sẽ được giải phóng ngay lập tức.');">
					<input type="hidden" name="bookingId" value="${booking.bookingId}">
					<button type="submit" class="cancel-booking-btn">
						<i class="fas fa-times-circle"></i> Hủy thanh toán
					</button>
				</form>
			</c:if>
		</div>

		<div class="checkout-content">
			<!-- LEFT -->
			<div class="checkout-details">

				<div class="info-card">
					<h3>
						<i class="fas fa-film"></i> Thông tin phim
					</h3>

					<c:if test="${not empty showtime}">
						<div class="info-row">
							<span class="info-label">Rạp:</span> <span class="info-value">${showtime.cinemaName}</span>
						</div>
						<div class="info-row">
							<span class="info-label">Ngày chiếu:</span> <span
								class="info-value"> <fmt:formatDate
									value="${showtime.showDate}" pattern="dd/MM/yyyy" />
							</span>
						</div>
						<div class="info-row">
							<span class="info-label">Giờ chiếu:</span> <span
								class="info-value">${showtime.startTime}</span>
						</div>
						<div class="info-row">
							<span class="info-label">Phòng:</span> <span class="info-value">${showtime.roomId}</span>
						</div>
						<c:if test="${not empty movie}">
							<div class="info-row">
								<span class="info-label">Phim:</span> <span class="info-value">${movie.title}</span>
							</div>
						</c:if>
					</c:if>
				</div>

				<div class="info-card">
					<h3>
						<i class="fas fa-chair"></i> Ghế đã chọn
					</h3>
					<div class="selected-seats">
						<c:forEach var="seat" items="${selectedSeats}">
							<span class="seat-tag">${seat}</span>
						</c:forEach>
					</div>
				</div>
			</div>

			<!-- RIGHT -->
			<div class="checkout-summary">

				<div class="info-card">
					<h3>
						<i class="fas fa-receipt"></i> Tóm tắt đơn hàng
					</h3>
					<c:if test="${not empty booking}">
						<div class="info-row">
							<span class="info-label">Mã đặt vé:</span> <span
								class="info-value">#${booking.bookingId}</span>
						</div>
						<div class="info-row">
							<span class="info-label">Số ghế:</span> <span class="info-value">${fn:length(selectedSeats)}</span>
						</div>
						<div class="info-row">
							<span class="info-label">Trạng thái:</span> <span
								class="info-value status-warning">${booking.status}</span>
						</div>
					</c:if>
				</div>

				<div class="total-section">
					<div class="info-row">
						<span class="info-label total-label">Tổng cộng:</span> <span
							class="total-amount"> <fmt:formatNumber
								value="${booking.totalAmount}" type="number" /> VND
						</span>
					</div>
				</div>

				<div class="payment-methods">
					<h3>
						<i class="fas fa-credit-card"></i> Phương thức thanh toán
					</h3>

					<form action="${pageContext.request.contextPath}/checkout"
						method="post" id="paymentForm">
						<div class="payment-option">
							<input type="radio" name="paymentMethod" value="CASH" id="cash"
								checked> <label for="cash" class="cash">
								<div class="payment-icon">
									<i class="fas fa-money-bill-wave"></i>
								</div> <span>BankPro</span>
							</label>
						</div>

						<div class="payment-option">
							<input type="radio" name="paymentMethod" value="MOMO" id="momo">
							<label for="momo" class="momo">
								<div class="payment-icon">
									<i class="fas fa-mobile-alt"></i>
								</div> <span>Ví MoMo</span>
							</label>
						</div>

						<div class="payment-option">
							<input type="radio" name="paymentMethod" value="VNPAY" id="vnpay">
							<label for="vnpay" class="vnpay">
								<div class="payment-icon">
									<i class="fas fa-qrcode"></i>
								</div> <span>VNPay</span>
							</label>
						</div>

						<button type="submit" class="pay-button" id="payButton">
							<i class="fas fa-lock"></i> Xác nhận thanh toán
						</button>
					</form>

					<form action="${pageContext.request.contextPath}/cancel-booking"
						method="post" style="display: inline;">
						<input type="hidden" name="bookingId"
							value="${sessionScope.bookingId}"> <input type="hidden"
							name="action" value="back_to_seat">
						<button type="submit" class="back-link-btn"
							style="background: none; border: none; color: blue; cursor: pointer; text-decoration: underline;">
							<i class="fas fa-arrow-left"></i> Quay lại chọn ghế
						</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/views/user/common/footer.jsp" />

	<script>
		document.addEventListener('DOMContentLoaded', function() {
			const countdownElement = document.getElementById('countdown');
			const payButton = document.getElementById('payButton');
			const paymentForm = document.getElementById('paymentForm');
			
			if (countdownElement) {
				let seconds = parseInt(countdownElement.textContent);
				
				console.log('Bắt đầu đếm ngược: ' + seconds + ' giây');
				
				const timerInterval = setInterval(function() {
					seconds--;
					countdownElement.textContent = seconds;
					
					// Cập nhật màu sắc
					if (seconds <= 30) {
						countdownElement.style.color = '#ff4757';
						countdownElement.style.fontWeight = 'bold';
					} else if (seconds <= 60) {
						countdownElement.style.color = '#f39c12';
					}
					
					// Kiểm tra hết giờ
					if (seconds <= 0) {
						clearInterval(timerInterval);
						countdownElement.textContent = '0';
						
						// Hiển thị thông báo
						alert('ĐÃ HẾT THỜI GIAN THANH TOÁN! Ghế sẽ được giải phóng tự động.');
						
						// Tự động chuyển hướng
						setTimeout(function() {
							window.location.href = '${pageContext.request.contextPath}/timeout';
						}, 2000);
						
						// Vô hiệu hóa nút thanh toán
						if (payButton) {
							payButton.disabled = true;
							payButton.style.opacity = '0.5';
							payButton.style.cursor = 'not-allowed';
							payButton.innerHTML = '<i class="fas fa-ban"></i> Đã hết thời gian';
						}
						
						// Ngăn form submit
						if (paymentForm) {
							paymentForm.onsubmit = function(e) {
								e.preventDefault();
								alert('Đã hết thời gian thanh toán. Vui lòng chọn ghế mới.');
								return false;
							};
						}
					}
				}, 1000);
				
				// Kiểm tra timeout mỗi 10 giây
				setInterval(function() {
					fetch('${pageContext.request.contextPath}/check-timeout?bookingId=${booking.bookingId}')
						.then(response => response.json())
						.then(data => {
							if (data.expired) {
								clearInterval(timerInterval);
								window.location.href = '${pageContext.request.contextPath}/timeout';
							}
						})
						.catch(error => console.error('Lỗi kiểm tra timeout:', error));
				}, 10000);
			}
		});
	</script>
</body>
</html>