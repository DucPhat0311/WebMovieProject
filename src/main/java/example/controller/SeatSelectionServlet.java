package example.controller;

import example.dao.BookingDAO;
import example.dao.MovieDAO;
import example.dao.SeatDAO;
import example.dao.ShowtimeDAO;
import example.model.Movie;
import example.model.Showtime;
import example.model.User;
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

		// 1. Kiểm tra đăng nhập
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// Lưu URL hiện tại để redirect sau khi login
			String redirectUrl = request.getRequestURL().toString();
			String queryString = request.getQueryString();
			if (queryString != null && !queryString.isEmpty()) {
				redirectUrl += "?" + queryString;
			}
			session.setAttribute("redirectAfterLogin", redirectUrl);

			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// 2. Lấy tham số showtimeId
		String showtimeIdParam = request.getParameter("showtimeId");
		if (showtimeIdParam == null || showtimeIdParam.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}

		try {
			int showtimeId = Integer.parseInt(showtimeIdParam);

			// 3. Lấy thông tin suất chiếu
			ShowtimeDAO showtimeDAO = new ShowtimeDAO();
			Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);

			if (showtime == null) {
				request.setAttribute("errorMessage", "Suất chiếu không tồn tại!");
				request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
				return;
			}

			// 4. Lấy thông tin phim từ movieId
			MovieDAO movieDAO = new MovieDAO();
			Movie movie = movieDAO.getMovieById(showtime.getMovieId());

			// 5. Lấy danh sách ghế đã đặt
			BookingDAO bookingDAO = new BookingDAO();
			List<Integer> bookedSeatIds = bookingDAO.getBookedSeatIds(showtimeId);

			// 6. Lấy tất cả ghế trong phòng
			SeatDAO seatDAO = new SeatDAO();
			List<example.model.Seat> allSeats = seatDAO.getSeatsByRoomId(showtime.getRoomId());

			// 7. Gửi dữ liệu đến JSP
			request.setAttribute("showtime", showtime);
			request.setAttribute("movie", movie); // Thêm movie object
			request.setAttribute("bookedSeatIds", bookedSeatIds);
			request.setAttribute("allSeats", allSeats);
			request.setAttribute("user", user);

			// 8. Forward đến trang chọn ghế
			request.getRequestDispatcher("/views/jsp/seat-selection.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "ID suất chiếu không hợp lệ!");
			request.getRequestDispatcher("/views/jsp/error.jsp").forward(request, response);
		}
	}
}