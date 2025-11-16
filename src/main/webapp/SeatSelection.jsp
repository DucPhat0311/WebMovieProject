<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="example.model.*, example.composite.*, java.util.*"%>
<%
Showtime showtime = (Showtime) request.getAttribute("showtime");
Map<String, List<Seat>> seatsByRow = (Map<String, List<Seat>>) request.getAttribute("seatsByRow");
Set<String> bookedSeatCodes = (Set<String>) request.getAttribute("bookedSeatCodes");

if (showtime == null) {
	response.sendError(400, "Không có dữ liệu suất chiếu");
	return;
}
%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Chọn Ghế - QuickShow</title>
<link href="SeatSelection.css" rel="stylesheet" />
<link
	href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap"
	rel="stylesheet" />
</head>
<body>
	<!-- Header -->
	<header class="header">
		<div class="header-content">
			<div class="logo">
				<img src="images/logofake.jpg" alt="QuickShow Logo" />
			</div>
			<nav class="navigation">
				<a href="homepage.jsp" class="nav-link">Home</a> <a
					href="all_movies.jsp" class="nav-link">Movies</a> <a href="#"
					class="nav-link">Theaters</a> <a href="#" class="nav-link">Releases</a>
			</nav>
			<div class="header-actions">
				<svg class="search-icon" xmlns="http://www.w3.org/2000/svg"
					width="24" height="24" viewBox="0 0 24 24" fill="none"
					stroke="currentColor" stroke-width="2" stroke-linecap="round"
					stroke-linejoin="round">
                    <path d="m21 21-4.34-4.34"></path>
                    <circle cx="11" cy="11" r="8"></circle>
                </svg>
				<button class="login-btn" onclick="location.href='SignIn.jsp'">Login</button>
			</div>
		</div>
	</header>

	<!-- Main Content -->
	<main class="seat-main-container">
		<div class="seat-content-wrapper">
			<!-- Timings Sidebar -->
			<aside class="timings-sidebar">
				<p class="timings-title">Suất Chiếu</p>
				<div class="timings-list">
					<div class="timing-item selected-timing">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
							class="clock-icon">
                            <circle cx="12" cy="12" r="10"></circle>
                            <polyline points="12 6 12 12 16 14"></polyline>
                        </svg>
						<p class="timing-text">
							<%=String.format("%tH:%tM", showtime.getStartTime(), showtime.getStartTime())%>
						</p>
					</div>
				</div>
				<div class="showtime-info">
					<p class="info-item">
						<strong>Phòng:</strong>
						<%=showtime.getRoomId()%>
					</p>
					<p class="info-item">
						<strong>Định dạng:</strong>
						<%=showtime.getFormat()%>
					</p>
					<p class="info-item">
						<strong>Giá vé:</strong>
						<%=String.format("%,d", showtime.getPrice().intValue())%>
						VNĐ
					</p>
				</div>
			</aside>

			<!-- Seat Selection -->
			<section class="seat-selection">
				<div class="background-blur-1"></div>
				<div class="background-blur-2"></div>

				<h1 class="section-title">Chọn ghế của bạn</h1>

				<div class="screen-container">
					<img class="screen-image"
						src="data:image/svg+xml,%3csvg%20width='585'%20height='29'%20viewBox='0%200%20585%2029'%20fill='none'%20xmlns='http://www.w3.org/2000/svg'%3e%3cpath%20d='M585%2029V17C585%2017%20406.824%200%20292.5%200C178.176%200%200%2017%200%2017V29C0%2029%20175.5%2012%20292.5%2012C404.724%2012%20585%2029%20585%2029Z'%20fill='%23F84565'%20fill-opacity='0.3'/%3e%3c/svg%3e"
						alt="screen" />
					<p class="screen-label">MÀN HÌNH</p>
				</div>

				<div class="seats-container">
					<div class="seats-grid">
						<%
						for (Map.Entry<String, List<Seat>> entry : seatsByRow.entrySet()) {
						%>
						<div class="seat-row">
							<div class="seat-buttons">
								<%
								for (Seat seat : entry.getValue()) {
									boolean isBooked = bookedSeatCodes.contains(seat.getName());
									String seatClass = "seat-btn ";
									if (isBooked) {
										seatClass += "booked";
									} else {
										seatClass += "available";
									}
								%>
								<button class="<%=seatClass%>" data-seat="<%=seat.getName()%>"
									data-seat-id="<%=seat.getId()%>"
									<%=isBooked ? "disabled" : ""%>>
									<%=seat.getName()%>
								</button>
								<%
								}
								%>
							</div>
						</div>
						<%
						}
						%>
					</div>
				</div>

				<!-- Seat Legend -->
				<div class="seat-legend">
					<div class="seat-type available">
						<span class="seat"></span> Ghế trống
					</div>
					<div class="seat-type selected">
						<span class="seat"></span> Ghế bạn chọn
					</div>
					<div class="seat-type booked">
						<span class="seat"></span> Ghế đã đặt
					</div>
				</div>

				<!-- Selection Summary -->
				<div class="selection-summary">
					<p id="selectedSeatsText" class="summary-text">Chưa chọn ghế
						nào</p>
					<p id="totalPrice" class="summary-price">Tổng cộng: 0 VNĐ</p>
				</div>

				<form id="bookingForm" action="booking" method="post">
					<input type="hidden" name="showtimeId"
						value="<%=showtime.getId()%>"> <input type="hidden"
						name="selectedSeats" id="selectedSeats">
					<button type="submit" class="checkout-btn" id="proceedBtn" disabled>
						Tiếp tục Thanh toán
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="3" stroke-linecap="round" stroke-linejoin="round"
							class="arrow-icon">
                            <path d="M5 12h14"></path>
                            <path d="m12 5 7 7-7 7"></path>
                        </svg>
					</button>
				</form>
			</section>
		</div>
	</main>

	<!-- Footer -->
	<footer class="footer">
		<div class="footer-content">
			<div class="footer-links">
				<a href="#" class="footer-link">Chính sách</a> <a href="#"
					class="footer-link">Lịch chiếu</a> <a href="#" class="footer-link">Tin
					tức</a> <a href="#" class="footer-link">Giá vé</a> <a href="#"
					class="footer-link">Hỏi đáp</a> <a href="#" class="footer-link">Đặt
					vé nhóm, tập thể</a> <a href="#" class="footer-link">Liên hệ</a>
			</div>
			<div class="footer-social-download">
				<div class="social-icons">
					<a href="#" class="social-icon"> <img src="images/fb.png"
						alt="Facebook" />
					</a> <a href="#" class="social-icon"> <img src="images/zl.png"
						alt="Zalo" />
					</a> <a href="#" class="social-icon"> <img src="images/yt.png"
						alt="YouTube" />
					</a>
				</div>

				<div class="app-downloads">
					<a href="#" class="download-btn"> <img src="images/ggplay.png"
						alt="Google Play" />
					</a> <a href="#" class="download-btn"> <img
						src="images/appstore.png" alt="App Store" />
					</a> <a href="#" class="download-btn"> <img
						src="images/thongbao.png" alt="Da Thong Bao" />
					</a>
				</div>
			</div>
			<div class="contact-info">
				<p>Cơ quan chủ quản: BỘ VĂN HÓA, THỂ THAO VÀ DU LỊCH</p>
				<p>Bản quyền thuộc Trung tâm Chiếu phim Quốc gia.</p>
				<p>Giấy phép số: 224/GP- TTĐT ngày 31/8/2010 - Chịu trách nhiệm:
					Vũ Đức Tùng – Giám đốc.</p>
				<p>Địa chỉ: Số 87 Láng Hạ, Phường Ô Chợ Dừa, TP.Hà Nội - Điện
					thoại: 024.35141791</p>
			</div>

			<div class="copyright">
				<p>Copyright 2023. NCC All Rights Reservered. Dev by Anvui.vn</p>
			</div>
		</div>
	</footer>

	<script>
        let selectedSeats = [];
        const ticketPrice = <%=showtime.getPrice().intValue()%>;
        
        document.querySelectorAll('.seat-btn.available').forEach(btn => {
            btn.addEventListener('click', function() {
                const seatCode = this.getAttribute('data-seat');
                const seatId = this.getAttribute('data-seat-id');
                
                if (this.classList.contains('selected')) {
                    this.classList.remove('selected');
                    selectedSeats = selectedSeats.filter(s => s.id !== seatId);
                } else {
                    this.classList.add('selected');
                    selectedSeats.push({ code: seatCode, id: seatId });
                }
                
                updateSelectionSummary();
            });
        });
        
        function updateSelectionSummary() {
            const selectedSeatsInput = document.getElementById('selectedSeats');
            const selectedSeatsText = document.getElementById('selectedSeatsText');
            const totalPriceElement = document.getElementById('totalPrice');
            const proceedBtn = document.getElementById('proceedBtn');
            
            if (selectedSeats.length > 0) {
                const seatCodes = selectedSeats.map(s => s.code).join(', ');
                const totalPrice = selectedSeats.length * ticketPrice;
                
                selectedSeatsText.textContent = 'Ghế đã chọn: ' + seatCodes;
                totalPriceElement.textContent = 'Tổng cộng: ' + totalPrice.toLocaleString() + ' VNĐ';
                selectedSeatsInput.value = JSON.stringify(selectedSeats);
                proceedBtn.disabled = false;
            } else {
                selectedSeatsText.textContent = 'Chưa chọn ghế nào';
                totalPriceElement.textContent = 'Tổng cộng: 0 VNĐ';
                selectedSeatsInput.value = '';
                proceedBtn.disabled = true;
            }
        }
    </script>
</body>
</html>