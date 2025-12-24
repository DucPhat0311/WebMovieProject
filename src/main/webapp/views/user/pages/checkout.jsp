<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="vi" class="checkout-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - MovieGO!</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/pages/checkout.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/components/timeout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/common/header.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/common/footer.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/views/user/common/header.jsp" />

    <c:if test="${not empty remainingSeconds}">
        <div class="timer-floating" id="floatingTimer">
            <div class="timer-content">
                <i class="fas fa-clock"></i>
                <span>Thanh toán trong:</span>
                <span id="countdown">15:00</span>
            </div>
        </div>
    </c:if>
    <div class="loading-overlay" id="loadingOverlay" style="display: none;">
        <div class="loading-spinner"></div>
        <div class="loading-message">Đang xử lý...</div>
    </div>

    <div id="alertContainer"></div>

    <div class="checkout-container">
        <div class="checkout-layout">
            <div class="checkout-left-column">
                <div class="checkout-movie-card">
                    <h2 class="checkout-section-title">
                        <i class="fas fa-film"></i> Thông tin phim
                    </h2>
                    <div class="checkout-info-grid">
                        <c:if test="${not empty movie}">
                            <div class="checkout-info-row">
                                <span class="checkout-info-label">Phim:</span> <span class="checkout-info-value">${movie.title}</span>
                            </div>
                        </c:if>

                        <c:if test="${not empty showtime}">
                            <div class="checkout-info-row">
                                <span class="checkout-info-label">Suất chiếu:</span> <span class="checkout-info-value checkout-date">${showtime.showDate}</span>
                            </div>
                            <div class="checkout-info-row">
                                <span class="checkout-info-label">Giờ chiếu:</span> <span class="checkout-info-value checkout-time">${showtime.startTime}</span>
                            </div>
                            <div class="checkout-info-row">
                                <span class="checkout-info-label">Rạp:</span> <span class="checkout-info-value">${showtime.cinemaName}</span>
                            </div>
                            <div class="checkout-info-row">
                                <span class="checkout-info-label">Phòng:</span> <span class="checkout-info-value">${showtime.roomId}</span>
                            </div>
                        </c:if>
                    </div>
                </div>

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

            <div class="checkout-right-column">
                <div class="checkout-total-cost">
                    <h2 class="checkout-cost-title">Tóm tắt thanh toán</h2>
                    <div class="checkout-cost-breakdown">
                        <div class="checkout-cost-row">
                            <span class="checkout-cost-label">Mã đặt vé:</span> <span class="checkout-cost-value">#${bookingId}</span>
                        </div>
                        <div class="checkout-cost-row">
                            <span class="checkout-cost-label">Số ghế:</span> 
                            <span class="checkout-cost-value"> 
                                <c:if test="${not empty selectedSeats}">${fn:length(selectedSeats)}</c:if>
                            </span>
                        </div>
                        <div class="checkout-cost-row total">
                            <span class="checkout-cost-label">Tổng tiền:</span> <span class="checkout-cost-value">${totalAmount} VND</span>
                        </div>
                    </div>
                </div>

                <div class="checkout-payment-methods">
                    <h2 class="checkout-section-title">
                        <i class="fas fa-credit-card"></i> Phương thức thanh toán
                    </h2>

                    <form action="${pageContext.request.contextPath}/payment" method="post" id="paymentForm">
                        <input type="hidden" name="action" value="process">

                        <div class="checkout-methods-container">
                            <div class="checkout-payment-method">
                                <input type="radio" name="paymentMethod" value="CASH" id="cash" class="checkout-method-radio" checked> 
                                <label for="cash" class="checkout-method-label">
                                    <div class="checkout-method-icon cash"><i class="fas fa-money-bill-wave"></i></div> 
                                    <span class="checkout-method-name">BankPro</span>
                                </label>
                            </div>
                            <div class="checkout-payment-method">
                                <input type="radio" name="paymentMethod" value="MOMO" id="momo" class="checkout-method-radio"> 
                                <label for="momo" class="checkout-method-label">
                                    <div class="checkout-method-icon momo"><i class="fas fa-mobile-alt"></i></div> 
                                    <span class="checkout-method-name">Ví MoMo</span>
                                </label>
                            </div>
                            <div class="checkout-payment-method">
                                <input type="radio" name="paymentMethod" value="VNPAY" id="vnpay" class="checkout-method-radio"> 
                                <label for="vnpay" class="checkout-method-label">
                                    <div class="checkout-method-icon vnpay"><i class="fas fa-qrcode"></i></div> 
                                    <span class="checkout-method-name">VNPay</span>
                                </label>
                            </div>
                        </div>

                        <div class="checkout-action-buttons">
                            <button type="submit" class="checkout-pay-btn" id="submitBtn">
                                <i class="fas fa-lock"></i> Xác nhận thanh toán
                            </button>
                            <a href="${pageContext.request.contextPath}/seat-selection?showtimeId=${showtime.showtimeId}" class="checkout-back-btn"> 
                                <i class="fas fa-arrow-left"></i> Quay lại chọn ghế
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/views/user/common/footer.jsp" />

    <script>
    // Payment method selection UI logic
    document.querySelectorAll('.checkout-method-radio').forEach(radio => {
        radio.addEventListener('change', function() {
            document.querySelectorAll('.checkout-method-label').forEach(label => label.classList.remove('selected'));
            if (this.checked) this.nextElementSibling.classList.add('selected');
        });
    });
    // Trigger initial selection
    document.querySelector('.checkout-method-radio:checked')?.dispatchEvent(new Event('change'));

    // Form submission loading
    document.getElementById('paymentForm')?.addEventListener('submit', function(e) {
        const submitBtn = this.querySelector('.checkout-pay-btn');
        document.getElementById('loadingOverlay').style.display = 'flex';
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
        submitBtn.disabled = true;
    });

    // ============ LOGIC TIMER ĐƠN GIẢN ============
    <c:if test="${not empty remainingSeconds}">
        const totalTime = ${remainingSeconds}; 
        let timeLeft = totalTime;
        let countdownInterval;

        function updateCountdown() {
            const timerElement = document.getElementById('floatingTimer');
            const countdownElement = document.getElementById('countdown');
            const submitBtn = document.getElementById('submitBtn');

            if (timeLeft <= 0) {
                clearInterval(countdownInterval);
                
                // Update UI hết giờ
                countdownElement.textContent = '00:00';
                timerElement.classList.add('expired'); // Chuyển màu đỏ
                
                // Khóa nút thanh toán
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-ban"></i> Đã hết thời gian';
                submitBtn.style.backgroundColor = '#6c757d';
                
                // Thông báo và reload
                showAlert('Hết thời gian giữ ghế!', 'error');
                setTimeout(() => window.location.reload(), 3000);
                return;
            }

            // Tính toán phút:giây
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;
            countdownElement.textContent = `\${minutes.toString().padStart(2, '0')}:\${seconds.toString().padStart(2, '0')}`;

            // Cảnh báo khi còn ít thời gian (đổi màu vàng)
            if (timeLeft < 300) { // Dưới 5 phút
                timerElement.classList.add('warning');
            }

            timeLeft--;
        }

        // Chạy timer
        updateCountdown();
        countdownInterval = setInterval(updateCountdown, 1000);
    </c:if>

    // Alert đơn giản
    function showAlert(message, type) {
        const container = document.getElementById('alertContainer');
        const div = document.createElement('div');
        div.className = `alert-message \${type}`;
        div.innerHTML = `<i class="fas fa-info-circle"></i> <span>\${message}</span>`;
        container.appendChild(div);
        setTimeout(() => div.remove(), 4000);
    }
    </script>
</body>
</html>