
package example.controller.user;

import example.dao.impl.MovieDAO;
import example.model.movie.Movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MovieDAO dao = new MovieDAO();

		List<Movie> now8ShowingList = dao.get8NowShowingMovies();
		List<Movie> nowShowingList = dao.getNowShowingMovies();

		List<Movie> coming8SoonList = dao.get8ComingSoonMovies();
		List<Movie> comingSoonList = dao.getComingSoonMovies();		

		request.setAttribute("now8ShowingList", now8ShowingList);
		request.setAttribute("nowShowingList", nowShowingList);

		request.setAttribute("coming8SoonList", coming8SoonList);
		request.setAttribute("comingSoonList", comingSoonList);


		request.getRequestDispatcher("/views/user/pages/home.jsp").forward(request, response);
	}
}
