<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Vé xem phim - MovieGO!</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/header.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/footer.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/base.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/pages/ticket.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
	<jsp:include page="/views/user/common/header.jsp" />

	<div class="ticket-main-container">
		<div class="ticket-wrapper">
			<div class="ticket-container">
				<!-- Header -->
				<div class="ticket-header">
					<div class="ticket-logo">MOVIEGO!</div>
					<div class="ticket-subtitle">Cinema Ticket</div>

					<!-- QR Code Placeholder -->
					<div class="ticket-qrcode">
						<div class="qr-placeholder">
							<i class="fas fa-qrcode fa-3x"></i> <span>Scan QR Code</span>
						</div>
					</div>

					<div class="ticket-code">TICKET#${payment.bookingId}</div>
				</div>

				<!-- Body -->
				<div class="ticket-body">
					<div class="ticket-info-row">
						<div class="ticket-label">Phim:</div>
						<div class="ticket-value movie-title">${movie.title}</div>
					</div>

					<c:if test="${not empty showtime}">
						<div class="ticket-info-row">
							<div class="ticket-label">Ngày chiếu:</div>
							<div class="ticket-value">
								<fmt:formatDate value="${showtime.showDate}"
									pattern="dd/MM/yyyy" />
							</div>
						</div>

						<div class="ticket-info-row">
							<div class="ticket-label">Giờ chiếu:</div>
							<div class="ticket-value">${showtime.startTime}</div>
						</div>

						<div class="ticket-info-row">
							<div class="ticket-label">Rạp:</div>
							<div class="ticket-value">${showtime.cinemaName}</div>
						</div>

						<div class="ticket-info-row">
							<div class="ticket-label">Phòng:</div>
							<div class="ticket-value">${showtime.roomId}</div>
						</div>
					</c:if>

					<c:if test="${not empty selectedSeats}">
						<div class="ticket-seats-section">
							<div class="ticket-seats-title">
								<i class="fas fa-chair"></i> Ghế đã đặt
							</div>
							<div class="ticket-seats-display">
								<c:forEach var="seat" items="${selectedSeats}">
									<span class="ticket-seat-tag">${seat}</span>
								</c:forEach>
							</div>
						</div>
					</c:if>

					<c:if test="${not empty payment}">
						<div class="payment-info">
							<div class="ticket-info-row">
								<div class="ticket-label">Số tiền:</div>
								<div class="ticket-value amount">
									<fmt:formatNumber value="${payment.amount}" pattern="#,##0" />
									VND
								</div>
							</div>

							<div class="ticket-info-row">
								<div class="ticket-label">Phương thức:</div>
								<div class="ticket-value">
									<div class="payment-method">
										<span> <c:choose>
												<c:when test="${payment.paymentMethod == 'Momo'}">Ví MoMo</c:when>
												<c:when test="${payment.paymentMethod == 'VNPay'}">VNPay</c:when>
												<c:when test="${payment.paymentMethod == 'BankPro'}">Tiền mặt</c:when>
												<c:otherwise>${payment.paymentMethod}</c:otherwise>
											</c:choose>
										</span>
										<div
											class="payment-icon ${payment.paymentMethod.toLowerCase()}">
											<c:choose>
												<c:when test="${payment.paymentMethod == 'Momo'}">
													<i class="fas fa-mobile-alt"></i>
												</c:when>
												<c:when test="${payment.paymentMethod == 'VNPay'}">
													<i class="fas fa-qrcode"></i>
												</c:when>
												<c:when test="${payment.paymentMethod == 'BankPro'}">
													<i class="fas fa-money-bill-wave"></i>
												</c:when>
												<c:otherwise>
													<i class="fas fa-credit-card"></i>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
							</div>

							<div class="ticket-info-row">
								<div class="ticket-label">Trạng thái:</div>
								<div class="ticket-value">
									<span class="status-success"> <i
										class="fas fa-check-circle"></i> ${payment.status}
									</span>
								</div>
							</div>

							<div class="ticket-info-row">
								<div class="ticket-label">Thời gian:</div>
								<div class="ticket-value">
									<fmt:formatDate value="${payment.paymentDate}"
										pattern="HH:mm dd/MM/yyyy" />
								</div>
							</div>
						</div>
					</c:if>
				</div>

				<div class="ticket-perforated"></div>

				<!-- Footer với các nút hành động -->
				<div class="ticket-footer no-print">
					<div class="ticket-actions">
						<button onclick="window.print()" class="ticket-btn print">
							<i class="fas fa-print"></i> In vé
						</button>

						<button onclick="downloadTicket()" class="ticket-btn download">
							<i class="fas fa-download"></i> Tải về
						</button>

						<a href="${pageContext.request.contextPath}/"
							class="ticket-btn home"> <i class="fas fa-home"></i> Trang
							chủ
						</a> <a href="${pageContext.request.contextPath}/user/bookings"
							class="ticket-btn bookings"> <i class="fas fa-ticket-alt"></i>
							Vé của tôi
						</a>
					</div>

					<div class="print-message no-print">
						<i class="fas fa-info-circle"></i> Sử dụng tổ hợp Ctrl+P để in vé
					</div>
				</div>
			</div>
		</div>
	</div>

	<%-- THÊM FOOTER --%>
	<jsp:include page="/views/user/common/footer.jsp" />

	<!-- JavaScript -->
	<script>
		function downloadTicket() {
			alert('Chức năng tải vé sẽ được cập nhật sau!');
		}

		window.addEventListener('afterprint', function() {
			alert('In vé thành công! Hãy mang vé đến quầy soát vé.');
		});
	</script>
</body>
</html>