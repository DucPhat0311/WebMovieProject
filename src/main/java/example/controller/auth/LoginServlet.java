package example.controller.auth;

import example.dao.impl.UserDAO;
import example.model.system.User;
import example.util.SecurityUtil;
import example.util.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String csrfToken = UUID.randomUUID().toString();
		request.getSession().setAttribute("csrfToken", csrfToken);
		request.setAttribute("csrfToken", csrfToken);

		HttpSession session = request.getSession(false);
		if (session != null) {
			String lastRegisteredEmail = (String) session.getAttribute("lastRegisteredEmail");
			if (lastRegisteredEmail != null && !lastRegisteredEmail.trim().isEmpty()) {
				request.setAttribute("email", lastRegisteredEmail);
				session.removeAttribute("lastRegisteredEmail");
			}
		}

		request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String sessionToken = (String) request.getSession().getAttribute("csrfToken");
		String requestToken = request.getParameter("csrfToken");

		if (sessionToken == null || !sessionToken.equals(requestToken)) {
			request.setAttribute("error", "Phiên làm việc không hợp lệ, vui lòng thử lại");
			// Tạo token mới trước khi forward
			String newCsrfToken = UUID.randomUUID().toString();
			request.getSession().setAttribute("csrfToken", newCsrfToken);
			request.setAttribute("csrfToken", newCsrfToken);
			request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
			return;
		}

		// Xóa token đã dùng
		request.getSession().removeAttribute("csrfToken");

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");

		Map<String, String> errors = new HashMap<>();

		if (email == null || email.trim().isEmpty()) {
			errors.put("emailError", "Email không được để trống");
		} else if (!Validator.isValidEmail(email)) {
			errors.put("emailError", "Email không đúng định dạng");
		}

		if (password == null || password.trim().isEmpty()) {
			errors.put("passwordError", "Mật khẩu không được để trống");
		}

		if (!errors.isEmpty()) {
			request.setAttribute("error", "Vui lòng kiểm tra lại thông tin");
			errors.forEach(request::setAttribute);
			request.setAttribute("email", email);
			// Tạo token mới cho lần thử tiếp theo
			String newCsrfToken = UUID.randomUUID().toString();
			request.getSession().setAttribute("csrfToken", newCsrfToken);
			request.setAttribute("csrfToken", newCsrfToken);
			request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
			return;
		}

		UserDAO userDAO = new UserDAO();
		User user = userDAO.findByEmail(email);

		boolean loginSuccess = false;

		if (user != null && SecurityUtil.checkPassword(password, user.getPassword())) {
			loginSuccess = true;
		}

		if (loginSuccess) {
			HttpSession session = request.getSession();
          	session.setAttribute("user", user);
			session.setMaxInactiveInterval(30 * 60); // 30 phút

			if ("on".equals(rememberMe)) {
				// 1. Cookie email
				Cookie emailCookie = new Cookie("rememberedEmail", email);
				emailCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
				emailCookie.setPath("/");
				response.addCookie(emailCookie);

				// 2. Cookie tên để hiển thị
				Cookie nameCookie = new Cookie("rememberedName", user.getFullName());
				nameCookie.setMaxAge(30 * 24 * 60 * 60);
				nameCookie.setPath("/");
				response.addCookie(nameCookie);
			} else {
				// Xóa cookies cũ nếu có
				clearRememberCookies(request, response);
			}

			String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				session.removeAttribute("redirectAfterLogin");
				response.sendRedirect(redirectUrl);
			} else {
				if ("ADMIN".equalsIgnoreCase(user.getRole())) {
					response.sendRedirect(request.getContextPath() + "/admin/dashboard");
				} else {
					response.sendRedirect("home");
				}
			}
		} else {
			request.setAttribute("error", "Email hoặc mật khẩu không đúng");
			request.setAttribute("email", email);
			// Tạo token mới cho lần thử tiếp theo
			String newCsrfToken = UUID.randomUUID().toString();
			request.getSession().setAttribute("csrfToken", newCsrfToken);
			request.setAttribute("csrfToken", newCsrfToken);
			request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
		}
	}

	private void clearRememberCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("rememberedEmail".equals(cookie.getName()) || "rememberedName".equals(cookie.getName())) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}
	}
}