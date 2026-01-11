package example.controller.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.dao.impl.MovieDAO;
import example.model.movie.Movie;

/**
 * Servlet implementation class MovieListServlet
 */
@WebServlet("/movie-list")
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String type = request.getParameter("type");

		MovieDAO dao = new MovieDAO();
		
		List<Movie> list;

		if ("coming".equals(type)) {
			list = dao.get8ComingSoonMovies();
			request.setAttribute("pageType", "coming"); 
		} else {
			list = dao.get8NowShowingMovies();
			request.setAttribute("pageType", "now");
		}
		
		request.setAttribute("listM", list);
		request.getRequestDispatcher("/views/user/pages/movie-list.jsp").forward(request, response);
	}
}