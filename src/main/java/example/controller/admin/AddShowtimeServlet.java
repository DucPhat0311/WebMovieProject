package example.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.dao.impl.MovieDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.movie.Movie;
import example.model.schedule.Showtime;

/**
 * Servlet implementation class AddShowtimeServlet
 */
@WebServlet("/admin/add-showtime")
public class AddShowtimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddShowtimeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MovieDAO movieDAO = new MovieDAO();
		request.setAttribute("movies", movieDAO.getAllMovies());		
		request.getRequestDispatcher("/views/admin/pages/showtime-add.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    }
}
