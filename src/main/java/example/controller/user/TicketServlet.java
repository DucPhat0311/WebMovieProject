package example.controller.user;

import example.dao.impl.*;
import example.model.movie.Movie;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.model.transaction.Booking;
import example.model.transaction.Payment;
import example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {

	private BookingDAO bookingDAO;
	private PaymentDAO paymentDAO;
	private ShowtimeDAO showtimeDAO;
	private MovieDAO movieDAO;

	@Override
	public void init() throws ServletException {
		bookingDAO = new BookingDAO();
		paymentDAO = new PaymentDAO();
		showtimeDAO = new ShowtimeDAO();
		movieDAO = new MovieDAO();
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

		String bookingIdStr = request.getParameter("bookingId");
		if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}

		try {
			int bookingId = Integer.parseInt(bookingIdStr);
			Booking booking = bookingDAO.getBookingById(bookingId);

			if (booking == null) {
				request.setAttribute("errorMessage", "Không tìm thấy thông tin vé.");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			boolean isAdmin = Constant.ROLE_ADMIN.equalsIgnoreCase(user.getRole());
			if (booking.getUserId() != user.getUserId() && !isAdmin) {
				request.setAttribute("errorMessage", "Bạn không có quyền truy cập vé này.");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
			Showtime showtime = showtimeDAO.getShowtimeById(booking.getShowtimeId());
			Movie movie = (showtime != null) ? movieDAO.getMovieById(showtime.getMovieId()) : null;
			List<String> selectedSeats = bookingDAO.getSelectedSeats(bookingId);

			request.setAttribute("booking", booking);
			request.setAttribute("payment", payment);
			request.setAttribute("showtime", showtime);
			request.setAttribute("movie", movie);
			request.setAttribute("selectedSeats", selectedSeats);

			if (selectedSeats != null) {
				request.setAttribute("seatsCount", selectedSeats.size());
			}

			request.getRequestDispatcher("/views/user/pages/ticket.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/home");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Lỗi hệ thống.");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		}
	}
}