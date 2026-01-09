package example.controller.user;

import example.dao.impl.BookingDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.model.transaction.Booking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		try {
			// 2. Lấy dữ liệu từ Form gửi lên
			String showtimeIdStr = request.getParameter("showtimeId");
			String[] selectedSeats = request.getParameterValues("selectedSeats"); // Mảng ghế: ["A1", "B5"...]

			// Validate cơ bản
			if (showtimeIdStr == null || selectedSeats == null || selectedSeats.length == 0) {
				request.setAttribute("errorMessage", "Vui lòng chọn ít nhất 1 ghế!");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			int showtimeId = Integer.parseInt(showtimeIdStr);
			List<String> seatCodeList = new ArrayList<>(Arrays.asList(selectedSeats));

			// 3. Lấy thông tin Suất chiếu (Showtime) -> ĐỂ LẤY ĐÚNG ROOM ID
			ShowtimeDAO showtimeDAO = new ShowtimeDAO();
			Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);

			if (showtime == null) {
				request.setAttribute("errorMessage", "Suất chiếu không tồn tại!");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			// 4. Tính tổng tiền sơ bộ (để lưu vào Booking)
			double totalAmount = 0;
			double basePrice = showtime.getBasePrice();
			double vipSurcharge = 15000; // Phụ thu VIP cố định

			for (String seatCode : seatCodeList) {
				// Logic check VIP: Nếu hàng ghế là E, F, G, H
				char row = seatCode.charAt(0);
				if (row == 'E' || row == 'F' || row == 'G' || row == 'H') {
					totalAmount += (basePrice + vipSurcharge);
				} else {
					totalAmount += basePrice;
				}
			}

			// 5. Tạo đối tượng Booking
			Booking booking = new Booking();
			booking.setUserId(user.getUserId());
			booking.setShowtimeId(showtimeId);
			booking.setTotalAmount(totalAmount);
			booking.setStatus("PENDING");

			// 6. Gọi DAO để lưu vào DB
			BookingDAO bookingDAO = new BookingDAO();

			// Truyền showtime.getRoomId()
			int bookingId = bookingDAO.createBookingWithSeats(booking, seatCodeList, showtime.getRoomId());

			if (bookingId > 0) {
				// THÀNH CÔNG: Lưu thông tin vào Session để trang Checkout dùng
				booking.setBookingId(bookingId);

				session.setAttribute("bookingId", bookingId);
				session.setAttribute("booking", booking);
				session.setAttribute("showtimeId", showtimeId);
				session.setAttribute("selectedSeats", seatCodeList);
				session.setAttribute("totalAmount", totalAmount);

				// Redirect sang trang thanh toán
				response.sendRedirect(request.getContextPath() + "/checkout");

			} else if (bookingId == -1) {
				// THẤT BẠI: Có ghế đã bị người khác đặt trước đó 1 tích tắc
				String errorUrl = request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId
						+ "&error=ghe_da_duoc_dat";
				response.sendRedirect(errorUrl);

			} else {
				// Lỗi hệ thống khác
				request.setAttribute("errorMessage", "Đặt vé thất bại do lỗi hệ thống.");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/home");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		}
	}
}