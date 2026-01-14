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
	href="${pageContext.request.contextPath}/assets/css/user/pages/home.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/pages/seat.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/header.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/footer.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user/common/base.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

<script>
document.addEventListener('DOMContentLoaded', function() {
    
    <fmt:formatNumber var="cleanBasePrice" value="${showtime.basePrice}" pattern="0" />
    
    const BASE_PRICE = ${cleanBasePrice}; 
    const MAX_SEATS = 8;
    const VIP_SURCHARGE = 15000;
    
    const seatButtons = document.querySelectorAll('.seat-btn.available, .seat-btn.selected');
    const checkoutBtn = document.querySelector('.checkout-btn');
    const form = document.getElementById('seatForm');

    // === XỬ LÝ CLICK GHẾ ===
    seatButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault(); 
            const checkbox = this.querySelector('input[type="checkbox"]');
            
            if (checkbox.checked) {
                checkbox.checked = false;
                this.classList.remove('selected');
                this.classList.add('available');
            } else {
                const currentSelected = document.querySelectorAll('input[name="selectedSeats"]:checked').length;
                if (currentSelected >= MAX_SEATS) {
                    alert('Bạn chỉ được chọn tối đa ' + MAX_SEATS + ' ghế!');
                    return;
                }
                checkbox.checked = true;
                this.classList.remove('available');
                this.classList.add('selected');
            }
            updateUI();
        });
    });

    // === HÀM TÍNH TIỀN ===
    function updateUI() {
        const selectedCheckboxes = document.querySelectorAll('input[name="selectedSeats"]:checked');
        const count = selectedCheckboxes.length;
        
        let total = 0;
        let seatCodes = [];
        let vipCount = 0;
        let standardCount = 0;

        selectedCheckboxes.forEach(cb => {
            const code = cb.value;
            seatCodes.push(code);
            const row = code.charAt(0);
            
            // Logic ghế VIP (Hàng E, F, G, H)
            if (['E', 'F', 'G', 'H'].includes(row)) {
                total += (BASE_PRICE + VIP_SURCHARGE);
                vipCount++;
            } else {
                total += BASE_PRICE;
                standardCount++;
            }
        });

        // Update Text
        document.getElementById('selectedSeatsList').textContent = seatCodes.length > 0 ? seatCodes.join(', ') : 'Chưa chọn ghế nào';
        
        // Hiển thị chi tiết số lượng (VD: 2 ghế (1 VIP))
        let countText = count + " ghế";
        if(count > 0) {
            countText += " (";
            if(standardCount > 0) countText += standardCount + " thường";
            if(standardCount > 0 && vipCount > 0) countText += ", ";
            if(vipCount > 0) countText += vipCount + " VIP";
            countText += ")";
        }
        document.getElementById('seatCount').textContent = countText;
        
        // Format tiền Việt Nam
        document.getElementById('totalPrice').textContent = total.toLocaleString('vi-VN') + ' VND';

        // Mở nút thanh toán
        if (count > 0) {
            checkoutBtn.classList.remove('disabled');
            checkoutBtn.removeAttribute('disabled');
        } else {
            checkoutBtn.classList.add('disabled');
            checkoutBtn.setAttribute('disabled', 'disabled');
        }
    }
    
    if(form) {
        form.addEventListener('submit', function(e) {
            if (document.querySelectorAll('input[name="selectedSeats"]:checked').length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn ít nhất 1 ghế!');
            }
        });
    }
});
</script>
</head>
<body>
	<jsp:include page="/views/user/common/header.jsp" />

	<main class="seat-main-container">
		<div class="seat-content-wrapper">

			<div class="showtime-info-card">
				<div class="movie-header">
					<img
						src="${pageContext.request.contextPath}/assets/img/movies/${movie.posterUrl}"
						alt="${movie.title}" class="movie-thumbnail" />

					<div class="movie-header-info">
						<h2>${movie.title}</h2>
						<div class="movie-meta">
							<span class="age-badge ${movie.ageWarning}">${movie.ageWarning}</span>
							<span><i class="fas fa-clock"></i> ${movie.duration} phút</span>
						</div>
					</div>
				</div>
				<div class="info-grid">
					<div class="info-row">
						<i class="fas fa-map-marker-alt"></i> <span>${showtime.cinemaName}</span>
					</div>
					<div class="info-row">
						<i class="fas fa-calendar"></i> <span><fmt:formatDate
								value="${showtime.showDate}" pattern="dd/MM/yyyy" /></span>
					</div>
					<div class="info-row">
						<i class="fas fa-clock"></i> <span>${showtime.startTime}</span>
					</div>
					<div class="info-row">
						<i class="fas fa-tag"></i> <span><fmt:formatNumber
								value="${showtime.basePrice}" type="number" /> VND</span>
					</div>
				</div>
			</div>

			<section class="seat-selection">
				<h1 class="section-title">
					<i class="fas fa-chair"></i> Chọn ghế
				</h1>

				<div class="screen-container">
					<div class="screen"></div>
					<p class="screen-label">MÀN HÌNH</p>
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

											<c:set var="currentSeat" value="" />
											<c:forEach items="${allSeats}" var="s">
												<c:if test="${s.seatRow == row && s.seatNumber == col}">
													<c:set var="currentSeat" value="${s}" />
												</c:if>
											</c:forEach>

											<c:set var="isBooked" value="false" />
											<c:if test="${not empty currentSeat}">
												<c:forEach items="${bookedSeatIds}" var="bId">
													<c:if test="${bId == currentSeat.seatId}">
														<c:set var="isBooked" value="true" />
													</c:if>
												</c:forEach>
											</c:if>

											<c:choose>
												<c:when test="${isBooked}">
													<label class="seat-btn booked"> ${col} </label>
												</c:when>
												<c:otherwise>
													<c:set var="seatCode" value="${row}${col}" />
													<c:set var="isVip"
														value="${row == 'E' || row == 'F' || row == 'G' || row == 'H'}" />

													<c:set var="isSelected" value="false" />
													<c:if
														test="${not empty param.selectedSeats && fn:contains(param.selectedSeats, seatCode)}">
														<c:set var="isSelected" value="true" />
													</c:if>

													<label
														class="seat-btn ${isSelected ? 'selected' : 'available'} ${isVip ? 'vip-seat' : ''}">
														<input type="checkbox" name="selectedSeats"
														value="${seatCode}" ${isSelected ? 'checked' : ''}
														style="display: none;"> ${col}
													</label>
												</c:otherwise>
											</c:choose>

										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</div>

						<div class="seat-legend">
							<div class="legend-title">Chú thích ghế</div>
							<div class="legend-items">
								<div class="seat-type available">
									<span class="seat"></span> <span class="seat-label">Ghế
										thường</span>
								</div>
								<div class="seat-type vip">
									<span class="seat"></span> <span class="seat-label">Ghế
										VIP</span>
								</div>
								<div class="seat-type selected">
									<span class="seat"></span> <span class="seat-label">Đang
										chọn</span>
								</div>
								<div class="seat-type booked">
									<span class="seat"></span> <span class="seat-label">Đã
										đặt</span>
								</div>
							</div>
						</div>
					</div>

					<div class="booking-summary">
						<div class="summary-row">
							<span class="summary-label">Ghế đã chọn:</span> <span
								class="summary-value" id="selectedSeatsList"> ${not empty param.selectedSeats ? param.selectedSeats : 'Chưa chọn ghế nào'}
							</span>
						</div>
						<div class="summary-row">
							<span class="summary-label">Số lượng:</span> <span
								class="summary-value" id="seatCount">0 ghế</span>
						</div>
						<div class="summary-row total">
							<span class="summary-label">Tổng tiền:</span> <span
								class="summary-value price-total" id="totalPrice">0 VND</span>
						</div>

						<div class="action-buttons">
							<a
								href="${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}"
								class="back-to-movie-btn"> <i class="fas fa-arrow-left"></i>
								Quay lại
							</a>
							<button type="submit" class="checkout-btn disabled" disabled>
								<span class="btn-text">Tiếp tục thanh toán</span> <i
									class="fas fa-arrow-right arrow-icon"></i>
							</button>
						</div>
					</div>
				</form>
			</section>
		</div>
	</main>
	<jsp:include page="/views/user/common/footer.jsp" />
</body>
</html>