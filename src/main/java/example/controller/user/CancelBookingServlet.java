package example.controller.user;

import example.dao.impl.BookingDAO;
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
		String bookingIdStr = request.getParameter("bookingId");
		String action = request.getParameter("action");
		Integer showtimeId = (Integer) session.getAttribute("showtimeId");

		if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}

		try {
			int bookingId = Integer.parseInt(bookingIdStr);

			// Hủy booking
			boolean success = bookingDAO.cancelBookingAndReleaseSeats(bookingId);

			if (success) {
				session.removeAttribute("bookingId");
				session.removeAttribute("totalAmount");
				session.removeAttribute("selectedSeats");

				if ("back_to_seat".equals(action) && showtimeId != null) {
					response.sendRedirect(request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId);
				} else {
					session.removeAttribute("showtimeId");
					response.sendRedirect(
							request.getContextPath() + "/timeout?bookingId=" + bookingId + "&manual=true");
				}
			} else {
				request.setAttribute("errorMessage", "Hủy booking thất bại.");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		}
	}
}