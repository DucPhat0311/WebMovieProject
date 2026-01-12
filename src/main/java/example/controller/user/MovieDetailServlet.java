package example.controller.user;

import example.dao.impl.MovieDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.movie.Movie;
import example.model.schedule.Showtime;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/movie-detail")
public class MovieDetailServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// lấy id phim từ URL (movie-detail?id=1)
			int movieId = Integer.parseInt(request.getParameter("id"));

			MovieDAO movieDAO = new MovieDAO();
			ShowtimeDAO showtimeDAO = new ShowtimeDAO();

			Movie movie = movieDAO.getMovieById(movieId);
			

			List<Date> showDates = showtimeDAO.getShowDates(movieId);

			Date selectedDate = null;
			String dateParamater = request.getParameter("date");

			if (dateParamater != null) {
				selectedDate = Date.valueOf(dateParamater);
			} else if (!showDates.isEmpty()) {
				selectedDate = showDates.get(0);
			}

			List<Showtime> showtimes = null;
			if (selectedDate != null) {
				showtimes = showtimeDAO.getShowtimesByDate(movieId, selectedDate);
			}

			request.setAttribute("movie", movie);
			request.setAttribute("showDates", showDates); 
			request.setAttribute("selectedDate", selectedDate); 
			request.setAttribute("showtimes", showtimes); 

			request.getRequestDispatcher("/views/user/pages/movie-detail.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("home");
		}
	}
}