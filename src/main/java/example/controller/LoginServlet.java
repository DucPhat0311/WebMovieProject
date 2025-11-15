package example.controller;

import example.dao.UserDAO;
import example.model.User;
import example.model.Role;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private UserDAO userDAO;

	@Override
	public void init() {
		userDAO = new UserDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		System.out.println("=== SERVLET DEBUG START ===");
		System.out.println("Servlet - Email: '" + email + "', Password: '" + password + "'");

		User user = userDAO.login(email, password);

		System.out.println("Servlet - User object: " + (user != null ? user.getUsername() : "null"));

		if (user != null) {
			System.out.println("Login SUCCESS - Redirecting to homepage");

			// TẠM THỜI: Kiểm tra role trước khi dùng
			if (user.getRole() == null) {
				System.err.println("WARNING: User role is null! Setting default: CUSTOMER");
				user.setRole(Role.CUSTOMER);
			}

			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			session.setAttribute("username", user.getUsername());
			session.setAttribute("role", user.getRole().name());

			response.sendRedirect("homepage.jsp");

		} else {
			System.out.println("Login FAILED - Forwarding to SignIn.jsp with error");
			request.setAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
			request.getRequestDispatcher("SignIn.jsp").forward(request, response);
		}

		System.out.println("=== SERVLET DEBUG END ===");
	}
}