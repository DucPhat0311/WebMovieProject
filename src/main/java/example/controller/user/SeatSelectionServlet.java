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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			String redirectUrl = request.getRequestURL().toString();
			String queryString = request.getQueryString();
			if (queryString != null && !queryString.isEmpty()) {
				redirectUrl += "?" + queryString;
			}
			session.setAttribute("redirectAfterLogin", redirectUrl);
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String showtimeIdParam = request.getParameter("showtimeId");
		if (showtimeIdParam == null || showtimeIdParam.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Thiếu thông tin suất chiếu");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		try {
			int showtimeId = Integer.parseInt(showtimeIdParam);

			if (showtimeId <= 0) {
				request.setAttribute("errorMessage", "ID suất chiếu không hợp lệ");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			ShowtimeDAO showtimeDAO = new ShowtimeDAO();
			Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);

			if (showtime == null) {
				request.setAttribute("errorMessage", "Suất chiếu không tồn tại!");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			if (!showtime.isActive()) {
				request.setAttribute("errorMessage", "Suất chiếu đã bị hủy!");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			MovieDAO movieDAO = new MovieDAO();
			Movie movie = movieDAO.getMovieById(showtime.getMovieId());

			if (movie == null || !movie.isActive()) {
				request.setAttribute("errorMessage", "Phim không tồn tại hoặc đã ngừng chiếu!");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			BookingDAO bookingDAO = new BookingDAO();
			bookingDAO.cancelExpiredPendingBookings(Constant.BOOKING_TIMEOUT_MINUTES);

			List<Integer> bookedSeatIds = bookingDAO.getBookedSeatIds(showtimeId);

			SeatDAO seatDAO = new SeatDAO();
			List<example.model.cinema.Seat> allSeats = seatDAO.getSeatsByRoomId(showtime.getRoomId());

			if (allSeats == null || allSeats.isEmpty()) {
				request.setAttribute("errorMessage", "Phòng chiếu không có ghế!");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
				return;
			}

			request.setAttribute("showtime", showtime);
			request.setAttribute("movie", movie);
			request.setAttribute("bookedSeatIds", bookedSeatIds);
			request.setAttribute("allSeats", allSeats);
			request.setAttribute("user", user);

			request.getRequestDispatcher("/views/user/pages/seat.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "ID suất chiếu không hợp lệ!");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}