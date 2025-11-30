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

@WebServlet(urlPatterns = { "/", "/homepage", "/home" })
public class HomeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Lấy danh sách phim đang chiếu & sắp chiếu từ DAO
		List<Movie> nowShowing = MovieDAO.getNowShowing();
		List<Movie> comingSoon = MovieDAO.getComingSoon();

		// 2. Đưa dữ liệu phim vào request để JSP dùng EL/JSTL
		request.setAttribute("nowShowing", nowShowing);
		request.setAttribute("comingSoon", comingSoon);

		// 3. Lấy thông tin user từ session (để hiển thị "Chào mừng ...")
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("user");

		if (currentUser != null) {
			// Dùng sessionScope.username và sessionScope.role trong JSP
			request.setAttribute("username", currentUser.getUsername());
			request.setAttribute("role", currentUser.getRole().toString());
		}

		// 4. Forward đến homepage.jsp
		request.getRequestDispatcher("/homepage.jsp").forward(request, response);
	}

	// Nếu ai đó POST vào đây (không nên), vẫn chuyển về trang chủ
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}