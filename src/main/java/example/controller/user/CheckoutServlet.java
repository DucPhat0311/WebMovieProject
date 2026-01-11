package example.controller.user;

import example.dao.impl.BookingDAO;
import example.dao.impl.PaymentDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.model.transaction.Booking;
import example.model.transaction.Payment;
import example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

	private BookingDAO bookingDAO;
	private PaymentDAO paymentDAO;
	private ShowtimeDAO showtimeDAO;

	@Override
	public void init() throws ServletException {
		bookingDAO = new BookingDAO();
		paymentDAO = new PaymentDAO();
		showtimeDAO = new ShowtimeDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// 1. Lấy thông tin booking từ session
		Integer bookingId = (Integer) session.getAttribute("bookingId");
		if (bookingId == null) {
			request.setAttribute("errorMessage", "Không tìm thấy thông tin đặt vé");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		// 2. Check timeout
		Booking booking = bookingDAO.getBookingById(bookingId);
		if (booking == null) {
			session.removeAttribute("bookingId");
			request.setAttribute("errorMessage", "Booking không tồn tại");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		if (bookingDAO.isBookingExpired(bookingId, Constant.BOOKING_TIMEOUT_MINUTES)) {
			bookingDAO.cancelBookingAndReleaseSeats(bookingId);
			session.removeAttribute("bookingId");
			request.setAttribute("errorMessage", "Đã hết thời gian giữ ghế. Vui lòng chọn lại");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		// 3. Tính thời gian còn lại để hiển thị
		Timestamp createdAt = bookingDAO.getBookingCreatedTime(bookingId);
		long expiryTimeMillis = createdAt.getTime() + (Constant.BOOKING_TIMEOUT_MINUTES * 60 * 1000);
		long remainingSeconds = Math.max(0, (expiryTimeMillis - System.currentTimeMillis()) / 1000);

		request.setAttribute("remainingSeconds", remainingSeconds);
		request.setAttribute("expiryTimeMillis", expiryTimeMillis);

		// 4. Lấy thông tin hiển thị
		Showtime showtime = showtimeDAO.getShowtimeById(booking.getShowtimeId());
		request.setAttribute("showtime", showtime);
		request.setAttribute("booking", booking);
		request.setAttribute("bookingId", bookingId);

		@SuppressWarnings("unchecked")
		List<String> selectedSeats = (List<String>) session.getAttribute("selectedSeats");
		request.setAttribute("selectedSeats", selectedSeats);

		request.getRequestDispatcher("/views/user/pages/checkout.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer bookingId = (Integer) session.getAttribute("bookingId");
		String paymentMethod = request.getParameter("paymentMethod");

		if (bookingId == null || paymentMethod == null) {
			request.setAttribute("errorMessage", "Thiếu thông tin thanh toán");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		try {
			// Check timeout lần cuối
			if (bookingDAO.isBookingExpired(bookingId, Constant.BOOKING_TIMEOUT_MINUTES)) {
				bookingDAO.cancelBookingAndReleaseSeats(bookingId);
				session.removeAttribute("bookingId");
				request.setAttribute("errorMessage", "Giao dịch thất bại do hết thời gian.");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			Booking booking = bookingDAO.getBookingById(bookingId);

			// Tạo Payment
			Payment payment = new Payment();
			payment.setBookingId(bookingId);
			payment.setAmount(booking.getTotalAmount());
			payment.setPaymentMethod(paymentMethod);
			payment.setStatus(Constant.PAYMENT_SUCCESS);
			payment.setPaymentDate(new Date());

			if (paymentDAO.createPayment(payment)) {
				bookingDAO.updateBookingStatus(bookingId, Constant.BOOKING_SUCCESS);

				session.removeAttribute("bookingId");
				session.removeAttribute("totalAmount");
				session.removeAttribute("showtimeId");
				session.removeAttribute("selectedSeats");

				response.sendRedirect(request.getContextPath() + "/ticket?bookingId=" + bookingId);
			} else {
				request.setAttribute("errorMessage", "Thanh toán thất bại");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		}
	}
}