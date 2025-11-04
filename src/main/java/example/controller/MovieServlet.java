package example.controller;

import example.dao.MovieDAO;
import example.model.Movie;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MovieServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    MovieDAO dao = MovieDAO.getInstance();
	    ArrayList<Movie> list = dao.selectAll();

	    request.setAttribute("movieList", list);

	    request.getRequestDispatcher("all_movies.jsp").forward(request, response);
	}
}
