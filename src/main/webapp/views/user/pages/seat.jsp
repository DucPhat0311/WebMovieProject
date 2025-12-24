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
<script>
document.addEventListener('DOMContentLoaded', function() {
    const seatButtons = document.querySelectorAll('.seat-btn.available, .seat-btn.vip-seat');
    const checkoutBtn = document.querySelector('.checkout-btn');
    const seatForm = document.getElementById('seatForm');
    const maxSeats = 8;
    const basePrice = ${showtime.basePrice};
    const vipSurcharge = 15000;
    
    seatButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const checkbox = this.querySelector('input[type="checkbox"]');
            if (!checkbox) return;
            
            const isVipSeat = this.classList.contains('vip-seat') || this.classList.contains('vip-selected');
            const wasVipSeat = this.classList.contains('vip-seat'); // Lưu trạng thái VIP ban đầu
            const selectedSeats = document.querySelectorAll('input[name="selectedSeats"]:checked');
            
            if (checkbox.checked) {
                // Bỏ chọn ghế - SỬA LẠI PHẦN NÀY
                checkbox.checked = false;
                this.classList.remove('selected');
                if (isVipSeat) {
                    this.classList.remove('vip-selected'); // Xóa lớp vip-selected
                    if (wasVipSeat) {
                        this.classList.add('vip-seat'); // Thêm lại lớp vip-seat nếu ban đầu là VIP
                    }
                } else {
                    this.classList.add('available'); // Thêm lại lớp available cho ghế thường
                }
            } else {
                // Chọn ghế - SỬA LẠI PHẦN NÀY
                if (selectedSeats.length >= maxSeats) {
                    alert('Bạn chỉ có thể chọn tối đa ' + maxSeats + ' ghế!');
                    return;
                }
                checkbox.checked = true;
                this.classList.add('selected');
                if (isVipSeat) {
                    this.classList.remove('vip-seat'); // Xóa lớp vip-seat
                    this.classList.add('vip-selected'); // Thêm lớp vip-selected
                } else {
                    this.classList.remove('available'); // Xóa lớp available
                }
            }
            
            updateCheckoutButton();
            updatePriceSummary();
        });
    });
    
    // Các hàm khác giữ nguyên...
    function updateCheckoutButton() {
        const selectedCount = document.querySelectorAll('input[name="selectedSeats"]:checked').length;
        const checkoutBtn = document.querySelector('.checkout-btn');
        
        if (selectedCount > 0) {
            checkoutBtn.classList.remove('disabled');
            checkoutBtn.disabled = false;
            checkoutBtn.style.opacity = '1';
            checkoutBtn.style.cursor = 'pointer';
        } else {
            checkoutBtn.classList.add('disabled');
            checkoutBtn.disabled = true;
            checkoutBtn.style.opacity = '0.6';
            checkoutBtn.style.cursor = 'not-allowed';
        }
    }
    
    function updatePriceSummary() {
        const selectedSeats = document.querySelectorAll('input[name="selectedSeats"]:checked');
        let standardCount = 0;
        let vipCount = 0;
        
        selectedSeats.forEach(checkbox => {
            const seatCode = checkbox.value;
            // Kiểm tra ghế VIP (hàng E, F, G, H)
            if (seatCode.match(/^[EFGH]/)) {
                vipCount++;
            } else {
                standardCount++;
            }
        });
        
        const totalPrice = (standardCount * basePrice) + (vipCount * (basePrice + vipSurcharge));
        const selectedSeatsArray = Array.from(selectedSeats).map(cb => cb.value);
        
        // Cập nhật UI
        const selectedSeatsList = document.getElementById('selectedSeatsList');
        const seatCountElement = document.getElementById('seatCount');
        const totalPriceElement = document.getElementById('totalPrice');
        
        if (selectedSeatsArray.length > 0) {
            selectedSeatsList.textContent = selectedSeatsArray.join(', ');
            seatCountElement.textContent = `${selectedSeatsArray.length} ghế (${standardCount} thường, ${vipCount} VIP)`;
        } else {
            selectedSeatsList.textContent = 'Chưa chọn ghế nào';
            seatCountElement.textContent = '0 ghế';
        }
        
        totalPriceElement.textContent = new Intl.NumberFormat('vi-VN').format(totalPrice) + ' VND';
    }
    
    // Xử lý khi form được submit
    seatForm.addEventListener('submit', function(e) {
        const selectedSeats = document.querySelectorAll('input[name="selectedSeats"]:checked');
        if (selectedSeats.length === 0) {
            e.preventDefault();
            alert('Vui lòng chọn ít nhất một ghế!');
            return false;
        }
        
        if (selectedSeats.length > maxSeats) {
            e.preventDefault();
            alert(`Bạn chỉ có thể chọn tối đa ${maxSeats} ghế!`);
            return false;
        }
        
        // Hiển thị loading nếu cần
        const checkoutBtn = document.querySelector('.checkout-btn');
        checkoutBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
        checkoutBtn.disabled = true;
        
        return true;
    });
    
    // Khởi tạo trạng thái ban đầu
    updateCheckoutButton();
    
    // Nếu có ghế được chọn từ trước (qua URL), cập nhật summary
    const initialSelectedSeats = document.querySelectorAll('input[name="selectedSeats"]:checked');
    if (initialSelectedSeats.length > 0) {
        updatePriceSummary();
    }
    
    // Thêm xử lý cho việc xác định ghế VIP đã chọn từ ban đầu
    document.querySelectorAll('.seat-btn.vip-seat input[type="checkbox"]:checked').forEach(checkbox => {
        const label = checkbox.parentElement;
        if (label.classList.contains('selected')) {
            label.classList.remove('vip-seat');
            label.classList.add('vip-selected');
        }
    });
});
</script>
</head>
<body>
	

	<main class="seat-main-container">
		<div class="seat-content-wrapper">
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

			<section class="seat-selection">
				<div class="background-blur-1"></div>
				<div class="background-blur-2"></div>

				<h1 class="section-title">
					<i class="fas fa-chair"></i> Chọn ghế của bạn
				</h1>

				<c:if test="${not empty seatError}">
					<div class="alert alert-error">
						<i class="fas fa-exclamation-circle"></i> ${seatError}
					</div>
				</c:if>

				<div class="screen-container">
					<div class="screen"></div>
					<p class="screen-label">
						<i class="fas fa-chevron-up"></i> MÀN HÌNH <i
							class="fas fa-chevron-up"></i>
					</p>
				</div>

				<form id="seatForm"
					action="${pageContext.request.contextPath}/booking" method="post">
					<input type="hidden" name="showtimeId"
						value="${showtime.showtimeId}">

					<div class="seats-container">
						<div class="seats-grid">
							<c:set var="rowLetters" value="A,B,C,D,E,F,G,H" />
							<c:forEach items="${fn:split(rowLetters, ',')}" var="row">
								<div class="seat-row">
									<div class="row-label">${row}</div>
									<div class="seat-buttons">
										<c:forEach begin="1" end="9" var="col">
											<c:set var="seatCode" value="${row}${col}" />
											<c:set var="isBooked" value="false" />
											<c:set var="currentSeatId" value="0" />

											<c:forEach items="${allSeats}" var="seat">
												<c:if
													test="${seat.seatRow == row && seat.seatNumber == col}">
													<c:set var="currentSeatId" value="${seat.seatId}" />
													<c:forEach items="${bookedSeatIds}" var="bookedId">
														<c:if test="${bookedId == seat.seatId}">
															<c:set var="isBooked" value="true" />
														</c:if>
													</c:forEach>
												</c:if>
											</c:forEach>

											<c:choose>
												<c:when test="${isBooked}">
													<label class="seat-btn booked" data-seat="${seatCode}">${col}</label>
												</c:when>
												<c:otherwise>
													<c:set var="isSelected" value="false" />
													<c:if test="${not empty param.selectedSeats}">
														<c:set var="selectedSeatsArray"
															value="${fn:split(param.selectedSeats, ',')}" />
														<c:forEach items="${selectedSeatsArray}"
															var="selectedSeat">
															<c:if test="${selectedSeat eq seatCode}">
																<c:set var="isSelected" value="true" />
															</c:if>
														</c:forEach>
													</c:if>

													<c:set var="isVip"
														value="${row == 'E' || row == 'F' || row == 'G' || row == 'H'}" />
													<label
														class="seat-btn ${isSelected ? 'selected' : 'available'} ${isVip ? 'vip-seat' : ''}">
														<input type="checkbox" name="selectedSeats"
														value="${seatCode}" ${isSelected ? 'checked' : ''}
														style="display: none;" /> ${col}
													</label>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</div>

						<div class="seat-notes">
							<div class="note-row">
								<i class="fas fa-info-circle"></i> <span>Hàng A-D: Ghế
									thường | Hàng E-H: Ghế VIP (+15.000 VND)</span>
							</div>
							<div class="note-row">
								<i class="fas fa-info-circle"></i> <span>Chọn tối đa 8
									ghế cho mỗi lần đặt vé</span>
							</div>
						</div>
					</div>

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
								<span class="seat"></span> <span class="seat-label">Ghế
									đã đặt</span>
							</div>
							<div class="seat-type vip">
								<span class="seat"></span> <span class="seat-label">Ghế
									VIP</span>
							</div>
						</div>
					</div>

					<div class="booking-section">
						<div class="booking-summary">
							<h3>
								<i class="fas fa-receipt"></i> Thông tin đặt vé
							</h3>

							<div class="summary-content" id="summaryContent">
								<c:set var="selectedSeatsCount" value="0" />
								<c:set var="vipSeatsCount" value="0" />
								<c:set var="standardSeatsCount" value="0" />
								<c:set var="selectedSeatsList" value="" />
								<c:set var="totalPrice" value="0" />

								<c:if test="${not empty param.selectedSeats}">
									<c:set var="selectedSeatsArray"
										value="${fn:split(param.selectedSeats, ',')}" />
									<c:set var="selectedSeatsCount"
										value="${fn:length(selectedSeatsArray)}" />

									<c:forEach items="${selectedSeatsArray}" var="seat"
										varStatus="status">
										<c:set var="selectedSeatsList"
											value="${selectedSeatsList}${seat}${status.last ? '' : ', '}" />
										<c:if
											test="${fn:startsWith(seat, 'E') || fn:startsWith(seat, 'F') || fn:startsWith(seat, 'G') || fn:startsWith(seat, 'H')}">
											<c:set var="vipSeatsCount" value="${vipSeatsCount + 1}" />
										</c:if>
									</c:forEach>

									<c:set var="standardSeatsCount"
										value="${selectedSeatsCount - vipSeatsCount}" />
									<c:set var="totalPrice"
										value="${(standardSeatsCount * showtime.basePrice) + (vipSeatsCount * (showtime.basePrice + 15000))}" />
								</c:if>

								<div class="summary-row">
									<span class="summary-label">Ghế đã chọn:</span> <span
										class="summary-value" id="selectedSeatsList"> <c:choose>
											<c:when test="${selectedSeatsCount > 0}">${selectedSeatsList}</c:when>
											<c:otherwise>Chưa chọn ghế nào</c:otherwise>
										</c:choose>
									</span>
								</div>
								<div class="summary-row">
									<span class="summary-label">Số lượng:</span> <span
										class="summary-value" id="seatCount"> <c:choose>
											<c:when test="${selectedSeatsCount > 0}">${selectedSeatsCount} ghế (${standardSeatsCount} thường, ${vipSeatsCount} VIP)</c:when>
											<c:otherwise>0 ghế</c:otherwise>
										</c:choose>
									</span>
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
										class="summary-value price-total" id="totalPrice"> <fmt:formatNumber
											value="${totalPrice}" type="number" /> VND
									</span>
								</div>
							</div>
						</div>

						<div class="action-buttons">
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}"
								class="back-to-movie-btn"> <i class="fas fa-arrow-left"></i>
								Quay lại
							</a>

							<button type="submit"
								class="checkout-btn ${selectedSeatsCount == 0 ? 'disabled' : ''}"
								${selectedSeatsCount == 0 ? 'disabled' : ''}>
								<span class="btn-text">Tiếp tục thanh toán</span> <i
									class="fas fa-arrow-right arrow-icon"></i>
							</button>
						</div>
					</div>
				</form>
			</section>
		</div>
	</main>
</body>
</html>