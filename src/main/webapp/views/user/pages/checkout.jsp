<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html lang="vi" class="checkout-page">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Thanh toán - MovieGO!</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/Checkout.css">
<!-- Thêm timeout.css -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/timeout.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
	<!-- Timeout Warning -->
	<c:if test="${not empty remainingSeconds}">
		<div class="timeout-warning normal" id="timeoutWarning">
			<div class="timeout-content">
				<i class="fas fa-clock"></i> <span>Còn lại để hoàn tất thanh
					toán:</span> <span id="countdown" class="countdown-timer">15:00</span>
			</div>
			<div class="timeout-progress">
				<div class="timeout-progress-bar" id="progressBar"></div>
			</div>
			<div class="timeout-info">
				<i class="fas fa-info-circle"></i> Thời gian còn lại sẽ được tự động
				cập nhật
			</div>
		</div>

		<!-- Auto refresh notice -->
		<div class="auto-refresh-notice" id="autoRefreshNotice"
			style="display: none;">
			<i class="fas fa-sync-alt"></i> <span>Trang sẽ tự động làm mới
				khi hết giờ để giải phóng ghế</span>
		</div>
	</c:if>

	<!-- Loading overlay -->
	<div class="loading-overlay" id="loadingOverlay" style="display: none;">
		<div class="loading-spinner"></div>
		<div class="loading-message">Đang xử lý...</div>
	</div>

	<!-- Alert message container -->
	<div id="alertContainer"></div>

	<div
		class="checkout-container ${not empty remainingSeconds ? 'with-timeout' : ''}">
		<div class="checkout-layout">
			<!-- Left Column: Movie Info -->
			<div class="checkout-left-column">
				<div class="checkout-movie-card">
					<h2 class="checkout-section-title">
						<i class="fas fa-film"></i> Thông tin phim
					</h2>
					<div class="checkout-info-grid">
						<c:if test="${not empty movie}">
							<div class="checkout-info-row">
								<span class="checkout-info-label">Phim:</span> <span
									class="checkout-info-value">${movie.title}</span>
							</div>
						</c:if>

						<c:if test="${not empty showtime}">
							<div class="checkout-info-row">
								<span class="checkout-info-label">Suất chiếu:</span> <span
									class="checkout-info-value checkout-date">${showtime.showDate}</span>
							</div>

							<div class="checkout-info-row">
								<span class="checkout-info-label">Giờ chiếu:</span> <span
									class="checkout-info-value checkout-time">${showtime.startTime}</span>
							</div>

							<div class="checkout-info-row">
								<span class="checkout-info-label">Rạp:</span> <span
									class="checkout-info-value">${showtime.cinemaName}</span>
							</div>

							<div class="checkout-info-row">
								<span class="checkout-info-label">Phòng:</span> <span
									class="checkout-info-value">${showtime.roomId}</span>
							</div>
						</c:if>
					</div>
				</div>

				<!-- Selected Seats -->
				<c:if test="${not empty selectedSeats}">
					<div class="checkout-seats-card">
						<h2 class="checkout-section-title">
							<i class="fas fa-chair"></i> Ghế đã chọn
						</h2>
						<div class="checkout-seat-display">
							<c:forEach var="seat" items="${selectedSeats}">
								<span class="checkout-seat-tag">${seat}</span>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</div>

			<!-- Right Column: Payment -->
			<div class="checkout-right-column">
				<!-- Order Summary -->
				<div class="checkout-total-cost">
					<h2 class="checkout-cost-title">Tóm tắt thanh toán</h2>
					<div class="checkout-cost-breakdown">
						<div class="checkout-cost-row">
							<span class="checkout-cost-label">Mã đặt vé:</span> <span
								class="checkout-cost-value">#${bookingId}</span>
						</div>

						<div class="checkout-cost-row">
							<span class="checkout-cost-label">Số ghế:</span> <span
								class="checkout-cost-value"> <c:if
									test="${not empty selectedSeats}">
									${fn:length(selectedSeats)}
								</c:if>
							</span>
						</div>

						<div class="checkout-cost-row total">
							<span class="checkout-cost-label">Tổng tiền:</span> <span
								class="checkout-cost-value">${totalAmount} VND</span>
						</div>
					</div>
				</div>

				<!-- Payment Methods -->
				<div class="checkout-payment-methods">
					<h2 class="checkout-section-title">
						<i class="fas fa-credit-card"></i> Phương thức thanh toán
					</h2>

					<form action="${pageContext.request.contextPath}/payment"
						method="post" id="paymentForm">
						<input type="hidden" name="action" value="process">

						<div class="checkout-methods-container">
							<div class="checkout-payment-method">
								<input type="radio" name="paymentMethod" value="CASH" id="cash"
									class="checkout-method-radio" checked> <label
									for="cash" class="checkout-method-label">
									<div class="checkout-method-icon cash">
										<i class="fas fa-money-bill-wave"></i>
									</div> <span class="checkout-method-name">BankPro</span>
								</label>
							</div>

							<div class="checkout-payment-method">
								<input type="radio" name="paymentMethod" value="MOMO" id="momo"
									class="checkout-method-radio"> <label for="momo"
									class="checkout-method-label">
									<div class="checkout-method-icon momo">
										<i class="fas fa-mobile-alt"></i>
									</div> <span class="checkout-method-name">Ví MoMo</span>
								</label>
							</div>

							<div class="checkout-payment-method">
								<input type="radio" name="paymentMethod" value="VNPAY"
									id="vnpay" class="checkout-method-radio"> <label
									for="vnpay" class="checkout-method-label">
									<div class="checkout-method-icon vnpay">
										<i class="fas fa-qrcode"></i>
									</div> <span class="checkout-method-name">VNPay</span>
								</label>
							</div>
						</div>

						<div class="checkout-action-buttons">
							<button type="submit" class="checkout-pay-btn" id="submitBtn">
								<i class="fas fa-lock"></i> Xác nhận thanh toán
							</button>
							<a
								href="${pageContext.request.contextPath}/seat-selection?showtimeId=${showtime.showtimeId}"
								class="checkout-back-btn"> <i class="fas fa-arrow-left"></i>
								Quay lại chọn ghế
							</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- JavaScript -->
	<script>
	
	// Payment method selection
	document.querySelectorAll('.checkout-method-radio').forEach(radio => {
		radio.addEventListener('change', function() {
			document.querySelectorAll('.checkout-method-label').forEach(label => {
				label.classList.remove('selected');
			});
			
			if (this.checked) {
				const label = this.nextElementSibling;
				if (label && label.classList.contains('checkout-method-label')) {
					label.classList.add('selected');
				}
			}
		});
	});
	
	// Set first method as selected initially
	document.querySelector('.checkout-method-radio:checked')?.dispatchEvent(new Event('change'));
	
	// Form submission
	document.getElementById('paymentForm')?.addEventListener('submit', function(e) {
		const submitBtn = this.querySelector('.checkout-pay-btn');
		const originalText = submitBtn.innerHTML;
		
		// Show loading
		showLoading();
		submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
		submitBtn.disabled = true;
		
		// Allow form submission
		return true;
	});
	
	// ============ TIMEOUT COUNTDOWN ============
	<c:if test="${not empty remainingSeconds}">
		const totalTime = ${remainingSeconds}; // Thời gian còn lại tính bằng giây
		const totalDuration = 15 * 60; // 15 phút = 900 giây
		let timeLeft = totalTime;
		let countdownInterval;
		let isExpired = false;
		
		function updateCountdown() {
			if (timeLeft <= 0) {
				clearInterval(countdownInterval);
				isExpired = true;
				
				// Update UI khi hết giờ
				document.getElementById('countdown').textContent = 'HẾT GIỜ';
				document.getElementById('countdown').className = 'expired';
				document.getElementById('timeoutWarning').className = 'timeout-warning expired shake';
				document.getElementById('progressBar').style.width = '0%';
				
				// Hiển thị thông báo auto refresh
				document.getElementById('autoRefreshNotice').style.display = 'block';
				
				// Disable form
				document.getElementById('paymentForm').className = 'disabled';
				document.getElementById('submitBtn').disabled = true;
				document.getElementById('submitBtn').innerHTML = '<i class="fas fa-ban"></i> Đã hết thời gian';
				
				// Show alert
				showAlert('Thời gian thanh toán đã hết!', 'error');
				
				// Tự động refresh sau 5 giây
				setTimeout(() => {
					showAlert('Đang tải lại trang...', 'warning');
					setTimeout(() => {
						window.location.reload();
					}, 2000);
				}, 5000);
				
				return;
			}
			
			// Tính thời gian còn lại
			const minutes = Math.floor(timeLeft / 60);
			const seconds = timeLeft % 60;
			
			// Cập nhật countdown display
			const countdownElement = document.getElementById('countdown');
			countdownElement.textContent = 
				`\${minutes.toString().padStart(2, '0')}:\${seconds.toString().padStart(2, '0')}`;
			
			// Tính phần trăm thời gian còn lại
			const percentage = (timeLeft / totalDuration) * 100;
			document.getElementById('progressBar').style.width = percentage + '%';
			
			// Thay đổi màu sắc và trạng thái dựa trên thời gian còn lại
			const warningElement = document.getElementById('timeoutWarning');
			
			if (timeLeft < 60) { // Dưới 1 phút
				warningElement.className = 'timeout-warning expired';
				countdownElement.className = 'countdown-timer warning';
			} else if (timeLeft < 300) { // Dưới 5 phút
				warningElement.className = 'timeout-warning warning';
				countdownElement.className = 'countdown-timer';
			} else {
				warningElement.className = 'timeout-warning normal';
				countdownElement.className = 'countdown-timer';
			}
			
			// Hiển thị thông báo ở một số mốc thời gian
			if (timeLeft === 300) { // 5 phút
				showAlert('Chỉ còn 5 phút để hoàn tất thanh toán!', 'warning');
			} else if (timeLeft === 60) { // 1 phút
				showAlert('Còn 1 phút! Hãy nhanh chóng hoàn tất thanh toán.', 'warning');
			}
			
			timeLeft--;
		}
		
		// Khởi động countdown
		updateCountdown();
		countdownInterval = setInterval(updateCountdown, 1000);
		
		// Ngăn chặn người dùng rời khỏi trang khi chưa thanh toán
		window.addEventListener('beforeunload', function(e) {
			if (timeLeft > 0 && timeLeft < totalDuration && !isExpired) {
				e.preventDefault();
				e.returnValue = 'Bạn có chắc muốn rời khỏi? Đặt vé của bạn sẽ bị hủy sau 15 phút.';
			}
		});
	</c:if>
	
	// ============ HELPER FUNCTIONS ============
	function showLoading() {
		document.getElementById('loadingOverlay').style.display = 'flex';
	}
	
	function hideLoading() {
		document.getElementById('loadingOverlay').style.display = 'none';
	}
	
	function showAlert(message, type = 'info') {
		const container = document.getElementById('alertContainer');
		const alertDiv = document.createElement('div');
		
		let icon = 'fa-info-circle';
		if (type === 'warning') icon = 'fa-exclamation-triangle';
		if (type === 'error') icon = 'fa-times-circle';
		if (type === 'success') icon = 'fa-check-circle';
		
		alertDiv.className = `alert-message \${type}`;
		alertDiv.innerHTML = `
			<i class="fas \${icon}"></i>
			<span>\${message}</span>
		`;
		
		container.appendChild(alertDiv);
		
		// Tự động xóa sau 5 giây
		setTimeout(() => {
			alertDiv.classList.add('hide');
			setTimeout(() => {
				container.removeChild(alertDiv);
			}, 300);
		}, 5000);
	}
	
	// Kiểm tra khi trang tải xong
	window.addEventListener('load', function() {
	});
</script>
</body>
</html>