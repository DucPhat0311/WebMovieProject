package example.controller.auth;

import example.dao.impl.UserDAO;
import example.model.system.User;

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
		request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
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
			request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
			return;
		}

		UserDAO userDAO = new UserDAO();
		User user = userDAO.findByEmail(email);

		boolean loginSuccess = false;

		if (user != null) {
			if (example.util.SecurityUtil.checkPassword(password, user.getPassword())) {
				loginSuccess = true;
			}
		} 
		
		if (loginSuccess) {
			HttpSession session = request.getSession();
            session.setAttribute("user", user);

            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectUrl);
            } else {
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect("home");
                }
            }
		}	
		else {
			request.setAttribute("error", "Email hoặc mật khẩu không đúng");
			request.setAttribute("email", email);
			request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
		}
	}
}