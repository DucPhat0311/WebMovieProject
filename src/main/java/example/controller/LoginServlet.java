package example.controller;

import example.dao.UserDAO;
import example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	// Hiển thị trang login
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
	}

	// Xử lý login
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserDAO userDAO = new UserDAO();
		User user = userDAO.login(email, password);

		if (user != null) {
			// Tạo session
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(30 * 60); // 30 phút t đá

			// Kiểm tra redirect URL(tạm)
			String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				session.removeAttribute("redirectAfterLogin");
				response.sendRedirect(redirectUrl);
			} else {
				response.sendRedirect("home");
			}
		} else {
			request.setAttribute("error", "Email hoặc mật khẩu không đúng");
			request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
		}
	}
}