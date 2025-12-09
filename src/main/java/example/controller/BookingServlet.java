//package example.controller;
//
//import example.dao.BookingDAO;
//import example.dao.SeatDAO;
//import example.dao.ShowtimeDAO;
//import example.dao.UserDAO;
//import example.model.Booking;
//import example.model.Constant;
//import example.model.Seat;
//import example.model.SeatType;
//import example.model.Showtime;
//import example.model.User;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.regex.Pattern;
//
//@WebServlet("/booking")
//public class BookingServlet extends HttpServlet {
//
//	private static final Pattern SEAT_CODE_PATTERN = Pattern.compile("^[A-Z][1-9][0-9]?$");
//	private static final int MAX_SEATS_PER_BOOKING = 8;
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		HttpSession session = request.getSession();
//		User user = (User) session.getAttribute("user");
//
//		if (user == null) {
//			session.setAttribute("redirectAfterLogin", request.getRequestURI() + "?" + request.getQueryString());
//			response.sendRedirect(request.getContextPath() + "/login");
//			return;
//		}
//
//		String showtimeIdParam = request.getParameter("showtimeId");
//		String[] selectedSeats = request.getParameterValues("selectedSeats");
//
//		if (showtimeIdParam == null || showtimeIdParam.trim().isEmpty()) {
//			request.setAttribute("errorMessage", "Thiếu thông tin suất chiếu");
//			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//			return;
//		}
//
//		if (selectedSeats == null || selectedSeats.length == 0) {
//			request.setAttribute("seatError", "Vui lòng chọn ít nhất một ghế");
//			request.setAttribute("showtimeId", showtimeIdParam);
//			request.getRequestDispatcher("/views/jsp/seat-selection.jsp").forward(request, response);
//			return;
//		}
//
//		try {
//			int showtimeId = Integer.parseInt(showtimeIdParam);
//
//			if (showtimeId <= 0) {
//				request.setAttribute("errorMessage", "ID suất chiếu không hợp lệ");
//				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//				return;
//			}
//
//			if (selectedSeats.length > MAX_SEATS_PER_BOOKING) {
//				request.setAttribute("seatError", "Chỉ được chọn tối đa " + MAX_SEATS_PER_BOOKING + " ghế");
//				request.setAttribute("showtimeId", showtimeId);
//				request.getRequestDispatcher("/views/jsp/seat-selection.jsp").forward(request, response);
//				return;
//			}
//
//			ShowtimeDAO showtimeDAO = new ShowtimeDAO();
//			Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
//
//			if (showtime == null) {
//				request.setAttribute("errorMessage", "Suất chiếu không tồn tại");
//				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//				return;
//			}
//
//			if (!showtime.isActive()) {
//				request.setAttribute("errorMessage", "Suất chiếu đã bị hủy");
//				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//				return;
//			}
//
//			UserDAO userDAO = new UserDAO();
//			User dbUser = userDAO.getUserById(user.getUserId());
//			if (dbUser == null) {
//				session.invalidate();
//				request.setAttribute("errorMessage", "Tài khoản không tồn tại. Vui lòng đăng nhập lại");
//				request.getRequestDispatcher("/views/jsp/login.jsp").forward(request, response);
//				return;
//			}
//
//			SeatDAO seatDAO = new SeatDAO();
//			BookingDAO bookingDAO = new BookingDAO();
//
//			for (String seatCode : selectedSeats) {
//				if (!isValidSeatCode(seatCode)) {
//					request.setAttribute("seatError", "Mã ghế '" + seatCode + "' không đúng định dạng (VD: A1, B12)");
//					request.setAttribute("showtimeId", showtimeId);
//					request.getRequestDispatcher("/views/jsp/seat-selection.jsp").forward(request, response);
//					return;
//				}
//
//				Seat seat = seatDAO.getSeatByCode(seatCode, showtime.getRoomId());
//				if (seat == null) {
//					request.setAttribute("seatError", "Ghế " + seatCode + " không tồn tại trong phòng chiếu");
//					request.setAttribute("showtimeId", showtimeId);
//					request.getRequestDispatcher("/views/jsp/seat-selection.jsp").forward(request, response);
//					return;
//				}
//
//				boolean isBooked = bookingDAO.isSeatBooked(seat.getSeatId(), showtimeId);
//				if (isBooked) {
//					request.setAttribute("seatError", "Ghế " + seatCode + " đã được đặt. Vui lòng chọn ghế khác");
//					request.setAttribute("showtimeId", showtimeId);
//					request.getRequestDispatcher("/views/jsp/seat-selection.jsp").forward(request, response);
//					return;
//				}
//			}
//
//			double totalAmount = calculateTotalPriceAccurate(selectedSeats, showtime.getBasePrice(),
//					showtime.getRoomId(), seatDAO);
//
//			if (totalAmount <= 0) {
//				request.setAttribute("errorMessage", "Tổng tiền không hợp lệ");
//				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//				return;
//			}
//
//			Booking booking = new Booking();
//			booking.setUserId(user.getUserId());
//			booking.setShowtimeId(showtimeId);
//			booking.setBookingDate(new Timestamp(new Date().getTime()));
//			booking.setTotalAmount(totalAmount);
//			booking.setStatus(Constant.BOOKING_PENDING);
//
//			int bookingId = bookingDAO.createBooking(booking, selectedSeats, showtime.getRoomId());
//
//			if (bookingId > 0) {
//				session.setAttribute("bookingId", bookingId);
//				session.setAttribute("totalAmount", totalAmount);
//				session.setAttribute("showtimeId", showtimeId);
//				response.sendRedirect(request.getContextPath() + "/payment");
//			} else {
//				request.setAttribute("errorMessage", "Đặt vé thất bại. Vui lòng thử lại");
//				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//			}
//
//		} catch (NumberFormatException e) {
//			request.setAttribute("errorMessage", "ID suất chiếu không hợp lệ");
//			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//		} catch (Exception e) {
//			request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
//			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
//		}
//	}
//
//	private boolean isValidSeatCode(String seatCode) {
//		return seatCode != null && SEAT_CODE_PATTERN.matcher(seatCode).matches();
//	}
//
//	private double calculateTotalPriceAccurate(String[] selectedSeats, double basePrice, int roomId, SeatDAO seatDAO) {
//		double total = 0;
//		for (String seatCode : selectedSeats) {
//			Seat seat = seatDAO.getSeatByCode(seatCode, roomId);
//			if (seat != null) {
//				SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
//				double seatPrice = basePrice;
//				if (seatType != null) {
//					seatPrice += seatType.getSurcharge();
//				}
//				total += seatPrice;
//			}
//		}
//		return total;
//	}
//}