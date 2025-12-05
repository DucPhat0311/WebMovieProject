
package example.controller;

import example.dao.MovieDAO;
import example.model.Movie;
import example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;



public class HomeServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MovieDAO dao = new MovieDAO();
		
		List<Movie> nowShowingList = dao.getNowShowingMovies();
		
		List<Movie> comingSoonList = dao.getComingSoonMovies();

		request.setAttribute("nowShowingList", nowShowingList);
        request.setAttribute("comingSoonList", comingSoonList);

		request.getRequestDispatcher("homepage.jsp").forward(request, response);
	}
}
