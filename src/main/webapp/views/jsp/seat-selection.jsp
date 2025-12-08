<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Chọn ghế | MovieGO!</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/homepage_style.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/seatselection.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
	<!-- Header -->
	<header>
		<div class="logo">
			<span class="movie">Movie</span><b><span class="go">GO!</span></b>
		</div>

		<nav class="glass-nav">
			<ul>
				<li><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
				<li><a
					href="${pageContext.request.contextPath}/movies?type=now">Phim</a></li>
				<li><a href="#">Rạp</a></li>
				<li><a href="#">Giới thiệu</a></li>
			</ul>
		</nav>

		<div class="search-login">
			<input type="text" placeholder="Tìm kiếm" />
			<c:choose>
				<c:when test="${not empty sessionScope.user}">
					<div class="user-info">
						<span>Xin chào, ${sessionScope.user.fullName}</span> <a
							href="${pageContext.request.contextPath}/logout"
							class="logout-btn">Đăng xuất</a>
					</div>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/login"
						class="login-btn">Đăng nhập</a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>

	<!-- Main Content -->
	<main class="seat-main-container">
		<div class="seat-content-wrapper">
			<!-- Thông tin suất chiếu và phim -->
			<c:choose>
				<c:when test="${not empty showtime and not empty movie}">
					<div class="showtime-info-card">
						<div class="movie-header">
							<c:if test="${not empty movie.posterUrl}">
								<img src="${movie.posterUrl}" alt="${movie.title}"
									class="movie-thumbnail" />
							</c:if>
							<div class="movie-header-info">
								<h2>${movie.title}</h2>
								<div class="movie-meta">
									<span class="age-badge ${movie.ageWarning}">${movie.ageWarning}</span>
									<span><i class="fas fa-clock"></i> ${movie.duration}
										phút</span>
								</div>
							</div>
						</div>

						<div class="info-grid">
							<div class="info-row">
								<i class="fas fa-map-marker-alt"></i>
								<div class="info-content">
									<span class="info-label">Rạp:</span> <span class="info-value">${showtime.cinemaName}</span>
								</div>
							</div>
							<div class="info-row">
								<i class="fas fa-calendar"></i>
								<div class="info-content">
									<span class="info-label">Ngày:</span> <span class="info-value"><fmt:formatDate
											value="${showtime.showDate}" pattern="dd/MM/yyyy" /></span>
								</div>
							</div>
							<div class="info-row">
								<i class="fas fa-clock"></i>
								<div class="info-content">
									<span class="info-label">Giờ:</span> <span class="info-value">${showtime.startTime}</span>
								</div>
							</div>
							<div class="info-row">
								<i class="fas fa-film"></i>
								<div class="info-content">
									<span class="info-label">Loại:</span> <span class="info-value">${showtime.optionType}</span>
								</div>
							</div>
							<div class="info-row">
								<i class="fas fa-tag"></i>
								<div class="info-content">
									<span class="info-label">Giá vé:</span> <span
										class="info-value price"><fmt:formatNumber
											value="${showtime.basePrice}" type="number" /> VND</span>
								</div>
							</div>
							<div class="info-row">
								<i class="fas fa-chair"></i>
								<div class="info-content">
									<span class="info-label">Sức chứa:</span> <span
										class="info-value">72 ghế (8 hàng x 9 cột)</span>
								</div>
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="error-card">
						<i class="fas fa-exclamation-triangle"></i>
						<h3>Không tìm thấy thông tin suất chiếu</h3>
						<p>Vui lòng quay lại trang trước và thử lại.</p>
						<a href="${pageContext.request.contextPath}/" class="back-btn">
							<i class="fas fa-arrow-left"></i> Quay về trang chủ
						</a>
					</div>
				</c:otherwise>
			</c:choose>

			<!-- Seat Selection Interface -->
			<section class="seat-selection">
				<div class="background-blur-1"></div>
				<div class="background-blur-2"></div>

				<h1 class="section-title">
					<i class="fas fa-chair"></i> Chọn ghế của bạn
				</h1>

				<div class="screen-container">
					<div class="screen"></div>
					<p class="screen-label">
						<i class="fas fa-chevron-up"></i> MÀN HÌNH <i
							class="fas fa-chevron-up"></i>
					</p>
				</div>

				<div class="seats-container">
					<div class="seats-grid">
						<!-- Tạo hàng ghế từ A đến H -->
						<c:set var="rowLetters" value="A,B,C,D,E,F,G,H" />
						<c:forEach items="${fn:split(rowLetters, ',')}" var="row">
							<div class="seat-row">
								<div class="row-label">${row}</div>
								<div class="seat-buttons">
									<c:forEach begin="1" end="9" var="col">
										<c:set var="seatCode" value="${row}${col}" />
										<c:set var="isBooked" value="false" />
										<c:set var="currentSeatId" value="0" />

										<!-- Tìm seatId từ seatCode -->
										<c:forEach items="${allSeats}" var="seat">
											<c:if test="${seat.seatRow == row && seat.seatNumber == col}">
												<c:set var="currentSeatId" value="${seat.seatId}" />
												<!-- Kiểm tra ghế đã đặt -->
												<c:forEach items="${bookedSeatIds}" var="bookedId">
													<c:if test="${bookedId == seat.seatId}">
														<c:set var="isBooked" value="true" />
													</c:if>
												</c:forEach>
											</c:if>
										</c:forEach>

										<button class="seat-btn ${isBooked ? 'booked' : 'available'}"
											data-seat="${seatCode}" data-seat-id="${currentSeatId}"
											<c:if test="${isBooked}">disabled</c:if>>${col}</button>
									</c:forEach>
								</div>
							</div>
						</c:forEach>
					</div>

					<div class="seat-notes">
						<div class="note-row">
							<i class="fas fa-info-circle"></i> <span>Hàng A-G: Ghế
								thường | Hàng H: Ghế VIP (+15.000 VND)</span>
						</div>
						<div class="note-row">
							<i class="fas fa-info-circle"></i> <span>Chọn tối đa 8 ghế
								cho mỗi lần đặt vé</span>
						</div>
					</div>
				</div>

				<!-- Seat Legend -->
				<div class="seat-legend">
					<div class="legend-title">Chú thích:</div>
					<div class="legend-items">
						<div class="seat-type available">
							<span class="seat"></span> <span class="seat-label">Ghế
								trống</span>
						</div>
						<div class="seat-type selected">
							<span class="seat"></span> <span class="seat-label">Ghế
								bạn chọn</span>
						</div>
						<div class="seat-type booked">
							<span class="seat"></span> <span class="seat-label">Ghế đã
								đặt</span>
						</div>
						<div class="seat-type vip">
							<span class="seat"></span> <span class="seat-label">Ghế
								VIP</span>
						</div>
					</div>
				</div>

				<!-- Booking Summary và Form -->
				<div class="booking-section">
					<form id="seatForm"
						action="${pageContext.request.contextPath}/booking" method="post">
						<input type="hidden" name="showtimeId"
							value="${showtime.showtimeId}"> <input type="hidden"
							name="selectedSeats" id="selectedSeatsInput">

						<div class="booking-summary">
							<h3>
								<i class="fas fa-receipt"></i> Thông tin đặt vé
							</h3>

							<div class="summary-content">
								<div class="summary-row">
									<span class="summary-label">Ghế đã chọn:</span> <span
										class="summary-value" id="selectedSeatsText">Chưa chọn
										ghế nào</span>
								</div>
								<div class="summary-row">
									<span class="summary-label">Số lượng:</span> <span
										class="summary-value" id="seatCount">0 ghế</span>
								</div>
								<div class="summary-row">
									<span class="summary-label">Giá vé cơ bản:</span> <span
										class="summary-value"><fmt:formatNumber
											value="${showtime.basePrice}" type="number" /> VND/ghế</span>
								</div>
								<div class="summary-row">
									<span class="summary-label">Phụ thu VIP:</span> <span
										class="summary-value">15.000 VND/ghế</span>
								</div>
								<div class="summary-row total">
									<span class="summary-label">Tổng tiền:</span> <span
										class="summary-value price-total" id="totalPrice">0 VND</span>
								</div>
							</div>
						</div>

						<div class="action-buttons">
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}"
								class="back-to-movie-btn"> <i class="fas fa-arrow-left"></i>
								Quay lại
							</a>
							<button type="submit" class="checkout-btn" disabled
								id="checkoutButton">
								<span class="btn-text">Tiếp tục thanh toán</span> <i
									class="fas fa-arrow-right arrow-icon"></i>
							</button>
						</div>
					</form>
				</div>

				<!-- Warning message -->
				<c:if test="${not empty error}">
					<div class="alert alert-error">
						<i class="fas fa-exclamation-circle"></i> ${error}
					</div>
				</c:if>
			</section>
		</div>
	</main>

	<!-- Footer -->
	<footer>
		<div class="container">
			<div class="wrapper">
				<!-- LOGO + MÔ TẢ -->
				<div class="footer-widget">
					<a href="${pageContext.request.contextPath}/">
						<div class="logo">
							<span class="movie">Movie</span><b><span class="go">GO!</span></b>
						</div>
					</a>
					<p class="desc">MovieGO là nền tảng đặt vé xem phim trực tuyến
						hàng đầu, mang đến cho bạn trải nghiệm xem phim dễ dàng, nhanh
						chóng và tiện lợi. Cập nhật liên tục các suất chiếu, trailer và
						đánh giá phim mới nhất.</p>
					<ul class="socials">
						<li><a href="#"><i class="fab fa-facebook-f"></i></a></li>
						<li><a href="#"><i class="fab fa-twitter"></i></a></li>
						<li><a href="#"><i class="fab fa-instagram"></i></a></li>
						<li><a href="#"><i class="fab fa-linkedin-in"></i></a></li>
						<li><a href="#"><i class="fab fa-youtube"></i></a></li>
					</ul>
				</div>

				<!-- GIỚI THIỆU -->
				<div class="footer-widget">
					<h6>GIỚI THIỆU</h6>
					<ul class="links">
						<li><a href="#">Về Chúng Tôi</a></li>
						<li><a href="#">Thỏa Thuận Sử Dụng</a></li>
						<li><a href="#">Chính Sách Bảo Mật</a></li>
						<li><a href="#">Liên Hệ Hợp Tác</a></li>
						<li><a href="#">Điều Khoản Giao Dịch</a></li>
					</ul>
				</div>

				<!-- GÓC ĐIỆN ẢNH -->
				<div class="footer-widget">
					<h6>GÓC ĐIỆN ẢNH</h6>
					<ul class="links">
						<li><a
							href="${pageContext.request.contextPath}/movies?type=now">Thể
								Loại Phim</a></li>
						<li><a href="#">Bình Luận Phim</a></li>
						<li><a
							href="${pageContext.request.contextPath}/movies?type=now">Phim
								Đang Chiếu</a></li>
						<li><a
							href="${pageContext.request.contextPath}/movies?type=coming">Phim
								Sắp Chiếu</a></li>
						<li><a href="#">Top Phim Hot</a></li>
					</ul>
				</div>

				<!-- HỖ TRỢ -->
				<div class="footer-widget">
					<h6>HỖ TRỢ</h6>
					<ul class="links">
						<li><a href="#">Góp Ý & Liên Hệ</a></li>
						<li><a href="#">Hướng Dẫn Đặt Vé</a></li>
						<li><a href="#">Chính Sách Đổi / Hủy Vé</a></li>
						<li><a href="#">Rạp / Giá Vé</a></li>
						<li><a href="#">Tuyển Dụng</a></li>
						<li><a href="#">Câu Hỏi Thường Gặp (FAQ)</a></li>
					</ul>
				</div>
			</div>
		</div>
	</footer>

	<!-- JavaScript -->
	<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Khai báo biến
        const seatButtons = document.querySelectorAll('.seat-btn:not(.booked)');
        const selectedSeats = [];
        const selectedSeatIds = [];
        const basePrice = ${not empty showtime ? showtime.basePrice : 0};
        const vipSurcharge = 15000;
        
        // Các phần tử DOM
        const selectedSeatsInput = document.getElementById('selectedSeatsInput');
        const selectedSeatsText = document.getElementById('selectedSeatsText');
        const seatCountText = document.getElementById('seatCount');
        const totalPriceText = document.getElementById('totalPrice');
        const checkoutButton = document.getElementById('checkoutButton');
        
        // Function hiển thị thông báo (ĐÃ SỬA LỖI EL)
        function showAlert(message, type = 'info') {
            const alertDiv = document.createElement('div');
            alertDiv.className = 'alert alert-' + type;
            
            // Xác định icon
            let icon;
            if (type === 'warning') {
                icon = 'exclamation-triangle';
            } else if (type === 'error') {
                icon = 'exclamation-circle';
            } else {
                icon = 'info-circle';
            }
            
            alertDiv.innerHTML = '<i class="fas fa-' + icon + '"></i> ' + message;
            
            document.querySelector('.seat-selection').appendChild(alertDiv);
            
            // Tự động xóa sau 3 giây
            setTimeout(() => {
                if (alertDiv.parentNode) {
                    alertDiv.remove();
                }
            }, 3000);
        }
        
        // Xử lý chọn ghế
        seatButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                const seatCode = this.getAttribute('data-seat');
                const seatId = this.getAttribute('data-seat-id');
                const isVIP = seatCode.charAt(0) === 'H'; // Hàng H là VIP
                const index = selectedSeats.findIndex(seat => seat.code === seatCode);
                
                if (index === -1) {
                    // Nếu chưa chọn, thêm vào danh sách
                    if (selectedSeats.length < 8) {
                        selectedSeats.push({
                            code: seatCode,
                            id: seatId,
                            isVIP: isVIP
                        });
                        
                        // Thêm class selected và VIP nếu cần
                        this.classList.add('selected');
                        this.classList.remove('available');
                        if (isVIP) {
                            this.classList.add('vip-selected');
                        }
                    } else {
                        showAlert('Bạn chỉ có thể chọn tối đa 8 ghế!', 'warning');
                        return;
                    }
                } else {
                    // Nếu đã chọn, bỏ chọn
                    selectedSeats.splice(index, 1);
                    this.classList.remove('selected', 'vip-selected');
                    this.classList.add('available');
                }
                
                // Cập nhật form và hiển thị
                updateBookingSummary();
            });
        });
        
        // Cập nhật thông tin đặt vé
        function updateBookingSummary() {
            // Tạo mảng seat codes để gửi đi
            const seatCodes = selectedSeats.map(seat => seat.code);
            selectedSeatsInput.value = seatCodes.join(',');
            
            // Tính toán
            const standardSeats = selectedSeats.filter(seat => !seat.isVIP).length;
            const vipSeats = selectedSeats.filter(seat => seat.isVIP).length;
            const totalSeats = selectedSeats.length;
            
            // Tính tổng tiền
            const standardTotal = standardSeats * basePrice;
            const vipTotal = vipSeats * (basePrice + vipSurcharge);
            const totalAmount = standardTotal + vipTotal;
            
            // Cập nhật hiển thị
            if (totalSeats > 0) {
                selectedSeatsText.textContent = seatCodes.join(', ');
                seatCountText.textContent = totalSeats + ' ghế (' + standardSeats + ' thường, ' + vipSeats + ' VIP)';
                totalPriceText.textContent = formatCurrency(totalAmount) + ' VND';
                
                // Kích hoạt nút thanh toán
                checkoutButton.disabled = false;
                checkoutButton.classList.add('active');
            } else {
                selectedSeatsText.textContent = 'Chưa chọn ghế nào';
                seatCountText.textContent = '0 ghế';
                totalPriceText.textContent = '0 VND';
                
                // Vô hiệu hóa nút thanh toán
                checkoutButton.disabled = true;
                checkoutButton.classList.remove('active');
            }
        }
        
        // Format tiền tệ
        function formatCurrency(amount) {
            return amount.toLocaleString('vi-VN');
        }
        
        // Tính tổng tiền
        function calculateTotal() {
            const standardSeats = selectedSeats.filter(seat => !seat.isVIP).length;
            const vipSeats = selectedSeats.filter(seat => seat.isVIP).length;
            return (standardSeats * basePrice) + (vipSeats * (basePrice + vipSurcharge));
        }
        
        // Xử lý form submit
        document.getElementById('seatForm').addEventListener('submit', function(e) {
            if (selectedSeats.length === 0) {
                e.preventDefault();
                showAlert('Vui lòng chọn ít nhất một ghế!', 'warning');
                return false;
            }
            
            // Tạo thông báo xác nhận
            const seatCodes = selectedSeats.map(seat => seat.code);
            const vipCount = selectedSeats.filter(seat => seat.isVIP).length;
            const standardCount = selectedSeats.length - vipCount;
            const movieTitle = '${movie.title}'; // Sửa EL ở đây
            
            const confirmMessage = 'XÁC NHẬN ĐẶT VÉ\n\n' +
                                 'Phim: ' + movieTitle + '\n' +
                                 'Ghế: ' + seatCodes.join(', ') + '\n' +
                                 'Số lượng: ' + selectedSeats.length + ' ghế (' + standardCount + ' thường, ' + vipCount + ' VIP)\n' +
                                 'Tổng tiền: ' + formatCurrency(calculateTotal()) + ' VND\n\n' +
                                 'Bấm OK để tiếp tục thanh toán.';
            
            if (!confirm(confirmMessage)) {
                e.preventDefault();
                return false;
            }
            
            // Thêm loading state
            checkoutButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
            checkoutButton.disabled = true;
        });
        
        // Keyboard shortcuts
        document.addEventListener('keydown', function(e) {
            // ESC để bỏ chọn tất cả
            if (e.key === 'Escape' && selectedSeats.length > 0) {
                if (confirm('Bỏ chọn tất cả ghế?')) {
                    selectedSeats.length = 0;
                    seatButtons.forEach(btn => {
                        btn.classList.remove('selected', 'vip-selected');
                        if (!btn.classList.contains('booked')) {
                            btn.classList.add('available');
                        }
                    });
                    updateBookingSummary();
                    showAlert('Đã bỏ chọn tất cả ghế', 'info');
                }
            }
            
            // Ctrl+Enter để submit form
            if (e.ctrlKey && e.key === 'Enter' && !checkoutButton.disabled) {
                checkoutButton.click();
            }
        });
        
        // Khởi tạo
        updateBookingSummary();
        
        // Thêm tooltip cho ghế VIP
        seatButtons.forEach(btn => {
            const seatCode = btn.getAttribute('data-seat');
            if (seatCode && seatCode.charAt(0) === 'H') {
                btn.title = 'Ghế VIP (+15.000 VND)';
            }
        });
    });
</script>
</body>
</html>