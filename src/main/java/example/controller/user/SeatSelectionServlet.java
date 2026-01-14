package example.controller.user;

import example.dao.impl.BookingDAO;
import example.dao.impl.MovieDAO;
import example.dao.impl.SeatDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.movie.Movie;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.util.Constant;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/seat-selection")
public class SeatSelectionServlet extends HttpServlet {

	private BookingDAO bookingDAO;
	private SeatDAO seatDAO;
	private ShowtimeDAO showtimeDAO;
	private MovieDAO movieDAO;

	@Override
	public void init() throws ServletException {
		bookingDAO = new BookingDAO();
		seatDAO = new SeatDAO();
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

		String showtimeIdParam = request.getParameter("showtimeId");
		if (showtimeIdParam == null || showtimeIdParam.trim().isEmpty()) {
			handleError(request, response, "Thiếu thông tin suất chiếu");
			return;
		}

		try {
			int showtimeId = Integer.parseInt(showtimeIdParam);

			bookingDAO.cancelExpiredPendingBookings(Constant.BOOKING_TIMEOUT_MINUTES);

			Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
			if (showtime == null) {
				handleError(request, response, "Suất chiếu không tồn tại");
				return;
			}
			Movie movie = movieDAO.getMovieById(showtime.getMovieId());

			List<Integer> bookedSeatIds = bookingDAO.getBookedSeatIds(showtimeId);
			List<example.model.cinema.Seat> allSeats = seatDAO.getSeatsByRoomId(showtime.getRoomId());

			if (allSeats == null || allSeats.isEmpty()) {
				handleError(request, response, "Phòng chiếu chưa được thiết lập ghế!");
				return;
			}

			request.setAttribute("showtime", showtime);
			request.setAttribute("movie", movie);
			request.setAttribute("bookedSeatIds", bookedSeatIds);
			request.setAttribute("allSeats", allSeats);
			request.setAttribute("user", user);

			request.setAttribute("TIMEOUT_MINUTES", Constant.BOOKING_TIMEOUT_MINUTES);

			request.getRequestDispatcher("/views/user/pages/seat.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			handleError(request, response, "ID suất chiếu không hợp lệ!");
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