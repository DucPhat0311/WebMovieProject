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
 * Servlet implementation class ManageMovieServlet
 */
@WebServlet("/manage-movies")
public class ManageMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageMovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Gọi DAO để lấy danh sách phim
        MovieDAO dao = new MovieDAO();
        List<Movie> list = dao.getAllMovies(); // Hoặc hàm phân trang getMovieByIndex...

        // 2. Đẩy dữ liệu sang JSP
        request.setAttribute("listM", list); // "listM" phải trùng với items="${listM}" trong JSP

        // 3. Chuyển hướng về trang JSP
        request.getRequestDispatcher("admin-movies.jsp").forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
