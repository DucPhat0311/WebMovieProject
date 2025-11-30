
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="example.model.*, java.math.BigDecimal, java.util.*"%>

<%
Showtime showtime = (Showtime) request.getAttribute("showtime");
Map<String, List<Seat>> seatsByRow = (Map<String, List<Seat>>) request.getAttribute("seatsByRow");
Set<String> bookedSeatCodes = (Set<String>) request.getAttribute("bookedSeatCodes");

if (showtime == null || seatsByRow == null || bookedSeatCodes == null) {
	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu suất chiếu không hợp lệ");
	return;
}

// Giá vé để JS tính tiền
BigDecimal ticketPrice = showtime.getPrice();
// Helper để hiển thị định dạng phim (tránh lỗi nếu chưa có method getFormat())
String formatDisplay = showtime.getFormatDisplayName(); // bạn sẽ thêm method này vào Showtime.java
%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Chọn Ghế - ${showtime.movie.title}</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/SeatSelection.css" />
<link
	href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap"
	rel="stylesheet" />
<style>
.seat {
	cursor: pointer;
	text-align: center;
	line-height: 40px;
	width: 40px;
	height: 40px;
	margin: 4px;
	border-radius: 6px;
}

.seat.standard {
	background-color: #ddd;
}

.seat.vip {
	background-color: #f8d64e;
}

.seat.couple {
	background-color: #ff69b4;
}

.seat.selected {
	background-color: #6feaf6;
}

.seat.booked {
	background-color: #999;
	cursor: not-allowed;
}

.screen {
	background: #333;
	color: white;
	text-align: center;
	padding: 10px;
	margin: 20px 0;
	border-radius: 8px;
}

.legend {
	margin: 20px 0;
	display: flex;
	justify-content: center;
	gap: 20px;
	flex-wrap: wrap;
}

.proceed-btn {
	padding: 12px 30px;
	font-size: 18px;
	background: #e71a0f;
	color: white;
	border: none;
	border-radius: 6px;
	cursor: pointer;
}

.proceed-btn:disabled {
	background: #777;
	cursor: not-allowed;
}
</style>
</head>
<body>

	<div class="container"
		style="max-width: 1100px; margin: 20px auto; padding: 20px;">

		<!-- Thông tin suất chiếu -->
		<div class="movie-info"
			style="text-align: center; margin-bottom: 20px;">
			<h2 style="margin: 0; color: #e71a0f;">${showtime.movie.title}</h2>
			<p style="margin: 8px 0; font-size: 1.1em;">
				<strong>Rạp:</strong> ${showtime.room.cinema.name} - <strong>Phòng:</strong>
				${showtime.room.name}
			</p>
			<p style="margin: 8px 0;">
				<strong>Thời gian:</strong> ${showtime.startTime} | <strong>Định
					dạng:</strong>
				<%=formatDisplay%>
			</p>
			<p style="margin: 8px 0; font-size: 1.2em;">
				Giá vé: <strong><%=ticketPrice.toPlainString()%> VNĐ</strong>
			</p>
		</div>

		<!-- Màn hình -->
		<div class="screen">MÀN HÌNH</div>

		<!-- Sơ đồ ghế -->
		<div class="seats-container" style="text-align: center;">
			<c:forEach var="entry" items="${seatsByRow}">
				<div class="row"
					style="display: flex; justify-content: center; align-items: center; margin: 8px 0;">
					<span style="width: 30px; font-weight: bold;">${entry.key}</span>
					<div class="seats" style="display: flex; gap: 4px;">
						<c:forEach var="seat" items="${entry.value}">
							<c:set var="seatCode" value="${seat.label}" />
							<!-- A1, B10... -->
							<div
								class="seat ${seat.type.toString().toLowerCase()}
                            <c:if test='${bookedSeatCodes.contains(seatCode)}'> booked</c:if>'
                             data-seat-code="
								${seatCode}"
                             data-seat-id="${seat.id}"
								title="${seatCode} - ${seat.type.displayName}">
								${seat.number}</div>
						</c:forEach>
					</div>
					<span style="width: 30px; font-weight: bold;">${entry.key}</span>
				</div>
			</c:forEach>
		</div>

		<!-- Chú thích -->
		<div class="legend">
			<div>
				<span class="seat standard"></span> Ghế thường
			</div>
			<div>
				<span class="seat vip"></span> Ghế VIP
			</div>
			<div>
				<span class="seat couple"></span> Ghế đôi
			</div>
			<div>
				<span class="seat selected"></span> Đang chọn
			</div>
			<div>
				<span class="seat booked"></span> Đã đặt
			</div>
		</div>

		<!-- Form gửi đặt vé -->
		<form id="bookingForm"
			action="<%=request.getContextPath()%>/booking" method="POST">
			<input type="hidden" name="showtimeId" value="${showtime.id}" /> <input
				type="hidden" name="selectedSeats" id="selectedSeats" value="" />

			<div class="summary" style="text-align: center; margin: 30px 0;">
				<p id="selectedSeatsText" style="font-size: 1.3em; margin: 10px 0;">Chưa
					chọn ghế nào</p>
				<p id="totalPrice"
					style="font-size: 1.5em; font-weight: bold; color: #e71a0f;">Tổng
					cộng: 0 VNĐ</p>
				<button type="submit" id="proceedBtn" class="proceed-btn" disabled>
					Tiếp tục thanh toán</button>
			</div>
		</form>
	</div>

	<script>
    const ticketPrice = <%=ticketPrice.doubleValue()%>;
    let selectedSeats = [];

    // Click chọn ghế
    document.querySelectorAll('.seat').forEach(seat => {
        seat.addEventListener('click', function () {
            if (this.classList.contains('booked')) return;

            const seatCode = this.dataset.seatCode;
            const seatId   = parseInt(this.dataset.seatId);

            if (this.classList.contains('selected')) {
                this.classList.remove('selected');
                selectedSeats = selectedSeats.filter(s => s.id !== seatId);
            } else {
                this.classList.add('selected');
                selectedSeats.push({ code: seatCode, id: seatId });
            }
            updateSummary();
        });
    });

    function updateSummary() {
        const input      = document.getElementById('selectedSeats');
        const text       = document.getElementById('selectedSeatsText');
        const priceEl    = document.getElementById('totalPrice');
        const btn        = document.getElementById('proceedBtn');

        if (selectedSeats.length === 0) {
            text.textContent = 'Chưa chọn ghế nào';
            priceEl.textContent = 'Tổng cộng: 0 VNĐ';
            input.value = '';
            btn.disabled = true;
        } else {
            const codes = selectedSeats.map(s => s.code).join(', ');
            const total = selectedSeats.length * ticketPrice;
            text.textContent = 'Ghế đã chọn: ' + codes;
            priceEl.textContent = 'Tổng cộng: ' + total.toLocaleString('vi-VN') + ' VNĐ';
            input.value = JSON.stringify(selectedSeats);
            btn.disabled = false;
        }
    }
</script>

</body>
</html>

