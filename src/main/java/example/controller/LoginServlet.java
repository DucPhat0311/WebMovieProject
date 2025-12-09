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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Map<String, String> errors = new HashMap<>();

		if (email == null || email.trim().isEmpty()) {
			errors.put("emailError", "Email không được để trống");
		}

		if (password == null || password.trim().isEmpty()) {
			errors.put("passwordError", "Mật khẩu không được để trống");
		}

		if (!errors.isEmpty()) {
			request.setAttribute("error", "Vui lòng kiểm tra lại thông tin");
			errors.forEach(request::setAttribute);
			request.setAttribute("email", email);
			request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
			return;
		}

		UserDAO userDAO = new UserDAO();
		User user = userDAO.login(email, password);

		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(30 * 60);

			String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				session.removeAttribute("redirectAfterLogin");
				response.sendRedirect(redirectUrl);
			} else {
				response.sendRedirect("home");
			}
		} else {
			request.setAttribute("error", "Email hoặc mật khẩu không đúng");
			request.setAttribute("email", email);
			request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
		}
	}
}