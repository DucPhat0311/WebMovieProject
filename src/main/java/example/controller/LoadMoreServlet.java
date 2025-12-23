package example.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.dao.MovieDAO;
import example.model.Movie;

/**
 * Servlet implementation class LoadMoreServlet
 */
@WebServlet("/load-more")
public class LoadMoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadMoreServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        String type = request.getParameter("type");
        int existed = Integer.parseInt(request.getParameter("existed"));
        
        MovieDAO dao = new MovieDAO();
        
        // SỬA SỐ 4 THÀNH SỐ 8 Ở ĐÂY
        List<Movie> list = dao.getNextMovies(type, 8, existed); 
        
        request.setAttribute("newList", list);
        request.getRequestDispatcher("ajax-movie-card.jsp").forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
