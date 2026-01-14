package example.controller.user;

import example.dao.impl.BookingDAO;
import example.model.system.User;
import example.model.transaction.Booking;
import example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/cancel-booking")
public class CancelBookingServlet extends HttpServlet {

	private BookingDAO bookingDAO;

	@Override
	public void init() throws ServletException {
		bookingDAO = new BookingDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String bookingIdStr = request.getParameter("bookingId");
		String action = request.getParameter("action");
		Integer showtimeId = (Integer) session.getAttribute("showtimeId");

		if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}

		try {
			int bookingId = Integer.parseInt(bookingIdStr);

			Booking booking = bookingDAO.getBookingById(bookingId);

			if (booking == null) {
				handleError(request, response, "Đơn hàng không tồn tại.");
				return;
			}

			if (booking.getUserId() != user.getUserId()) {
				handleError(request, response, "Bạn không có quyền hủy đơn hàng này!");
				return;
			}

			if (!Constant.BOOKING_PENDING.equalsIgnoreCase(booking.getStatus())) {
				handleError(request, response, "Không thể hủy vé đã thanh toán hoặc đã bị hủy trước đó.");
				return;
			}

			boolean success = bookingDAO.cancelBookingAndReleaseSeats(bookingId);

			if (success) {
				Integer currentSessionBookingId = (Integer) session.getAttribute("bookingId");
				if (currentSessionBookingId != null && currentSessionBookingId == bookingId) {
					session.removeAttribute("bookingId");
					session.removeAttribute("totalAmount");
					session.removeAttribute("selectedSeats");
				}

				if ("back_to_seat".equals(action) && showtimeId != null) {
					response.sendRedirect(request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId);
				} else {
					session.removeAttribute("showtimeId");
					response.sendRedirect(
							request.getContextPath() + "/timeout?bookingId=" + bookingId + "&manual=true");
				}
			} else {
				handleError(request, response, "Hủy booking thất bại. Vui lòng thử lại.");
			}

		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/home");
		} catch (Exception e) {
			e.printStackTrace();
			handleError(request, response, "Lỗi hệ thống: " + e.getMessage());
		}
	}

	private void handleError(HttpServletRequest req, HttpServletResponse resp, String msg)
			throws ServletException, IOException {
		req.setAttribute("errorMessage", msg);
		req.getRequestDispatcher("/views/auth/error.jsp").forward(req, resp);
	}
}