package example.controller;

import example.dao.*;
import example.model.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
	private PaymentDAO paymentDAO;
	private BookingDAO bookingDAO;
	private ShowtimeDAO showtimeDAO;
	private MovieDAO movieDAO;

	// Thời gian timeout (phút)
	private static final int BOOKING_TIMEOUT_MINUTES = 15;

	@Override
	public void init() throws ServletException {
		paymentDAO = new PaymentDAO();
		bookingDAO = new BookingDAO();
		showtimeDAO = new ShowtimeDAO();
		movieDAO = new MovieDAO();
	}

	// ================ DOGET ================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String action = request.getParameter("action");

		if ("success".equals(action)) {
			handlePaymentSuccess(request, response);
		} else if ("cancel".equals(action)) {
			handlePaymentCancel(request, response);
		} else {
			showPaymentPage(request, response);
		}
	}

	// ================ DOPOST ================
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String action = request.getParameter("action");

		if ("process".equals(action)) {
			processPayment(request, response);
		}
	}

	private void showPaymentPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		Integer bookingId = (Integer) session.getAttribute("bookingId");
		Double totalAmount = (Double) session.getAttribute("totalAmount");
		Integer showtimeId = (Integer) session.getAttribute("showtimeId");
		String[] selectedSeats = (String[]) session.getAttribute("selectedSeats");

		if (bookingId == null || totalAmount == null || showtimeId == null) {
			request.setAttribute("errorMessage", "Không tìm thấy thông tin đặt vé. Vui lòng đặt vé lại.");
			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
			return;
		}

		// ================ KIỂM TRA TIMEOUT ================
		int remainingSeconds = -1;
		try {
			Timestamp createdAt = bookingDAO.getBookingCreatedTime(bookingId);
			if (createdAt != null) {
				long currentTime = System.currentTimeMillis();
				long bookingTime = createdAt.getTime();
				long diffInSeconds = (currentTime - bookingTime) / 1000;
				long diffInMinutes = diffInSeconds / 60;

				if (diffInMinutes > BOOKING_TIMEOUT_MINUTES) {
					// HỦY BOOKING VÌ QUÁ TIMEOUT
					cancelTimedOutBooking(bookingId, session);

					request.setAttribute("errorMessage", "Phiên đặt vé đã hết hạn (quá " + BOOKING_TIMEOUT_MINUTES
							+ " phút). Vui lòng chọn ghế lại.");
					request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
					return;
				}

				// Tính thời gian còn lại (tính bằng giây)
				remainingSeconds = (int) ((BOOKING_TIMEOUT_MINUTES * 60) - diffInSeconds);
				if (remainingSeconds < 0)
					remainingSeconds = 0;

				// Gửi remainingSeconds đến JSP
				request.setAttribute("remainingSeconds", remainingSeconds);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Nếu có lỗi khi kiểm tra timeout, vẫn tiếp tục
		}
		// ================ HẾT TIMEOUT CHECK ================

		// Lấy thông tin showtime và movie
		Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
		if (showtime == null) {
			request.setAttribute("errorMessage", "Suất chiếu không tồn tại.");
			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
			return;
		}

		Movie movie = movieDAO.getMovieById(showtime.getMovieId());
		if (movie == null) {
			request.setAttribute("errorMessage", "Phim không tồn tại.");
			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
			return;
		}

		// Truyền dữ liệu đến JSP
		request.setAttribute("bookingId", bookingId);
		request.setAttribute("totalAmount", String.format("%,.0f", totalAmount));
		request.setAttribute("showtime", showtime);
		request.setAttribute("movie", movie);
		request.setAttribute("selectedSeats", selectedSeats);

		// Format booking date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		request.setAttribute("bookingDate", sdf.format(new Date()));

		request.getRequestDispatcher("/views/jsp/checkout.jsp").forward(request, response);
	}

	// ================ PROCESS PAYMENT (THÊM TIMEOUT CHECK) ================
	private void processPayment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		String paymentMethod = request.getParameter("paymentMethod");
		Integer bookingId = (Integer) session.getAttribute("bookingId");
		Double totalAmount = (Double) session.getAttribute("totalAmount");

		if (bookingId == null || totalAmount == null || paymentMethod == null) {
			request.setAttribute("errorMessage", "Thiếu thông tin thanh toán");
			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
			return;
		}

		// ================ KIỂM TRA TIMEOUT TRƯỚC KHI THANH TOÁN ================
		try {
			Timestamp createdAt = bookingDAO.getBookingCreatedTime(bookingId);
			if (createdAt != null) {
				long diffInMinutes = (System.currentTimeMillis() - createdAt.getTime()) / (60 * 1000);

				if (diffInMinutes > BOOKING_TIMEOUT_MINUTES) {
					// HỦY BOOKING VÌ QUÁ TIMEOUT
					cancelTimedOutBooking(bookingId, session);

					request.setAttribute("errorMessage", "Không thể thanh toán: Phiên đặt vé đã hết hạn (quá "
							+ BOOKING_TIMEOUT_MINUTES + " phút). Vui lòng chọn ghế lại.");
					request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ================ HẾT TIMEOUT CHECK ================

		// Tạo payment record
		Payment payment = new Payment();
		payment.setBookingId(bookingId);
		payment.setAmount(totalAmount);
		payment.setPaymentMethod(paymentMethod);

		if ("CASH".equals(paymentMethod)) {
			// Thanh toán tiền mặt
			payment.setStatus("Success");
			payment.setPaymentDate(LocalDateTime.now());

			if (paymentDAO.createPayment(payment)) {
				// Cập nhật trạng thái booking
				updateBookingStatus(bookingId, "Success");

				// Xóa session attributes
				cleanupSession(session);

				// Chuyển đến trang thành công
				response.sendRedirect(request.getContextPath() + "/payment?action=success&bookingId=" + bookingId);
			} else {
				request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý thanh toán!");
				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
			}
		} else if ("MOMO".equals(paymentMethod) || "VNPAY".equals(paymentMethod)) {
			// Thanh toán online
			payment.setStatus("Success");

			if (paymentDAO.createPayment(payment)) {
				// Cập nhật trạng thái booking
				updateBookingStatus(bookingId, "Success");

				// Xóa session attributes
				cleanupSession(session);

				// Chuyển đến trang thành công
				response.sendRedirect(request.getContextPath() + "/payment?action=success&bookingId=" + bookingId);
			} else {
				request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý thanh toán!");
				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
			}
		} else {
			request.setAttribute("errorMessage", "Phương thức thanh toán không hợp lệ!");
			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
		}
	}

	// ================ METHOD HỦY BOOKING QUÁ TIMEOUT ================
	private void cancelTimedOutBooking(int bookingId, HttpSession session) {
		System.out.println("Hủy booking " + bookingId + " vì quá timeout");

		try {
			// 1. Cập nhật trạng thái booking thành Cancelled
			bookingDAO.cancelBooking(bookingId);

			// 2. Cập nhật trạng thái payment thành Failed (nếu có)
			paymentDAO.updatePaymentStatus(bookingId, "Failed");

			// 3. Xóa bookingdetail (giải phóng ghế)
			deleteBookingDetails(bookingId);

			// 4. Xóa session
			cleanupSession(session);

		} catch (Exception e) {
			System.err.println("Lỗi khi hủy booking: " + e.getMessage());
		}
	}

	private void deleteBookingDetails(int bookingId) {
		String sql = "DELETE FROM bookingdetail WHERE booking_id = ?";

		try (java.sql.Connection conn = example.dao.DBConnection.getConnection();
				java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			int deleted = ps.executeUpdate();
			System.out.println("Đã xóa " + deleted + " bookingdetail");

		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}

	private void cleanupSession(HttpSession session) {
		if (session != null) {
			session.removeAttribute("bookingId");
			session.removeAttribute("totalAmount");
			session.removeAttribute("showtimeId");
			session.removeAttribute("selectedSeats");
		}
	}

	// ================ XỬ LÝ THANH TOÁN THÀNH CÔNG ================
	private void handlePaymentSuccess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookingIdParam = request.getParameter("bookingId");

		if (bookingIdParam != null) {
			try {
				int bookingId = Integer.parseInt(bookingIdParam);

				// Lấy thông tin payment
				Payment payment = paymentDAO.getPaymentByBookingId(bookingId);

				// Lấy thông tin booking để lấy showtimeId
				Booking booking = bookingDAO.getBookingById(bookingId);

				if (booking != null && payment != null) {
					// Lấy thông tin showtime và movie
					Showtime showtime = showtimeDAO.getShowtimeById(booking.getShowtimeId());
					Movie movie = null;

					if (showtime != null) {
						movie = movieDAO.getMovieById(showtime.getMovieId());
					}

					// Lấy danh sách ghế từ booking details
					List<String> selectedSeats = bookingDAO.getSelectedSeats(bookingId);

					// Set attributes cho JSP
					request.setAttribute("payment", payment);
					request.setAttribute("bookingId", bookingId);
					request.setAttribute("showtime", showtime);
					request.setAttribute("movie", movie);
					request.setAttribute("selectedSeats", selectedSeats.toArray(new String[0]));

					// Forward đến trang vé
					request.getRequestDispatcher("/views/jsp/ticket.jsp").forward(request, response);
					return;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("errorMessage", "Không thể xác nhận thanh toán!");
		request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
	}

	// ================ XỬ LÝ HỦY THANH TOÁN ================
	private void handlePaymentCancel(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer bookingId = (Integer) session.getAttribute("bookingId");

		if (bookingId != null) {
			// Cập nhật trạng thái payment thành Failed
			paymentDAO.updatePaymentStatus(bookingId, "Failed");

			// Cập nhật trạng thái booking thành Cancelled
			updateBookingStatus(bookingId, "Cancelled");

			// Xóa bookingdetail
			deleteBookingDetails(bookingId);

			// Xóa session attributes
			cleanupSession(session);
		}

		// Đặt thông báo vào session
		session.setAttribute("cancelMessage", "Bạn đã hủy thanh toán. Đặt vé đã bị hủy.");

		// Redirect về trang chủ
		response.sendRedirect(request.getContextPath() + "/");
	}

	// ================ CẬP NHẬT TRẠNG THÁI BOOKING ================
	private void updateBookingStatus(int bookingId, String status) {
		String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";

		try (java.sql.Connection conn = example.dao.DBConnection.getConnection();
				java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setInt(2, bookingId);
			ps.executeUpdate();

		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
}