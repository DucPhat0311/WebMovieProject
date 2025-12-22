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
@WebServlet("/load-more-movies")
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

		// 1. Lấy số lượng phim ĐANG hiển thị trên màn hình (Client gửi lên)
		String amountExistStr = request.getParameter("existed");
		int amountExist = 0;
		try {
			amountExist = Integer.parseInt(amountExistStr);
		} catch (Exception e) {
		}

		// 2. Gọi DAO lấy 4 phim TIẾP THEO
		MovieDAO dao = new MovieDAO();
		List<Movie> list = dao.getNextMovies(4, amountExist);

		// 3. Đẩy list này sang file JSP Fragment
		request.setAttribute("newList", list);

		// 4. Trả về đoạn HTML (Không chuyển trang, chỉ trả về nội dung)
		request.getRequestDispatcher("ajax-movie-list.jsp").forward(request, response);
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
