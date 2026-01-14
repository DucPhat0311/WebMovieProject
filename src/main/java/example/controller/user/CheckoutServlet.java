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
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		Integer bookingId = (Integer) session.getAttribute("bookingId");
		if (bookingId == null) {
			request.setAttribute("errorMessage", "Không tìm thấy thông tin đặt vé");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		Booking booking = bookingDAO.getBookingById(bookingId);
		if (booking == null) {
			session.removeAttribute("bookingId");
			request.setAttribute("errorMessage", "Booking không tồn tại");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		// Kiểm tra booking đã hết hạn chưa
		if (bookingDAO.isBookingExpired(bookingId, Constant.BOOKING_TIMEOUT_MINUTES)) {
			bookingDAO.cancelBookingAndReleaseSeats(bookingId);
			session.removeAttribute("bookingId");
			request.setAttribute("errorMessage", "Đã hết thời gian giữ ghế. Vui lòng chọn lại");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		Timestamp createdAt = bookingDAO.getBookingCreatedTime(bookingId);
		long expiryTimeMillis = createdAt.getTime() + (Constant.BOOKING_TIMEOUT_MINUTES * 60 * 1000);

		// Thêm server time để client đồng bộ
		long serverTime = System.currentTimeMillis();
		long remainingSeconds = Math.max(0, (expiryTimeMillis - serverTime) / 1000);

		request.setAttribute("serverTime", serverTime);
		request.setAttribute("remainingSeconds", remainingSeconds);
		request.setAttribute("expiryTimeMillis", expiryTimeMillis);

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
		User user = (User) session.getAttribute("user");

		Integer bookingId = (Integer) session.getAttribute("bookingId");
		if (bookingId == null) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}

		try {
			String paymentMethod = request.getParameter("paymentMethod");
			Booking booking = bookingDAO.getBookingById(bookingId);

			if (booking == null) {
				handleError(request, response, "Đơn hàng không tồn tại.");
				return;
			}

			Payment payment = new Payment();
			payment.setBookingId(bookingId);
			payment.setAmount(booking.getTotalAmount());
			payment.setPaymentMethod(paymentMethod);
			payment.setStatus(Constant.PAYMENT_SUCCESS);

			boolean success = bookingDAO.completeCheckout(bookingId, payment);

			if (success) {
				session.removeAttribute("bookingId");
				session.removeAttribute("totalAmount");
				session.removeAttribute("showtimeId");
				session.removeAttribute("selectedSeats");

				response.sendRedirect(request.getContextPath() + "/ticket?bookingId=" + bookingId);
			} else {
				session.removeAttribute("bookingId");
				handleError(request, response, "Giao dịch thất bại hoặc vé đã hết hạn giữ chỗ. Vui lòng đặt lại.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			handleError(request, response, "Lỗi hệ thống khi thanh toán.");
		}
	}

	private void handleError(HttpServletRequest req, HttpServletResponse resp, String msg)
			throws ServletException, IOException {
		req.setAttribute("errorMessage", msg);
		req.getRequestDispatcher("/views/auth/error.jsp").forward(req, resp);
	}
}