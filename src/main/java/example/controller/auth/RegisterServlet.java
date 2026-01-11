package example.controller.auth;

import example.dao.impl.UserDAO;
import example.model.system.User;
import example.util.Validator;
import example.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String csrfToken = UUID.randomUUID().toString();
		request.getSession().setAttribute("csrfToken", csrfToken);
		request.setAttribute("csrfToken", csrfToken);

		request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
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
			request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
			return;
		}

		// Xóa token đã dùng
		request.getSession().removeAttribute("csrfToken");

		// Lấy parameters
		String fullName = request.getParameter("fullName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String phone = request.getParameter("phone");
		String gender = request.getParameter("gender");
		String agreeTerms = request.getParameter("agreeTerms");

		Map<String, String> errors = new HashMap<>();

		if (fullName == null || fullName.trim().isEmpty()) {
			errors.put("fullNameError", "Họ tên không được để trống");
		} else if (fullName.length() < 2 || fullName.length() > 50) {
			errors.put("fullNameError", "Họ tên phải từ 2-50 ký tự");
		}

		if (email == null || email.trim().isEmpty()) {
			errors.put("emailError", "Email không được để trống");
		} else if (!Validator.isValidEmail(email)) {
			errors.put("emailError", "Email không đúng định dạng");
		} else {
			UserDAO userDAO = new UserDAO();
			if (userDAO.emailExists(email)) {
				errors.put("emailError", "Email đã được sử dụng");
			}
		}

		if (password == null || password.trim().isEmpty()) {
			errors.put("passwordError", "Mật khẩu không được để trống");
		} else if (password.length() < 6) {
			errors.put("passwordError", "Mật khẩu phải có ít nhất 6 ký tự");
		}

		if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
			errors.put("confirmPasswordError", "Vui lòng xác nhận mật khẩu");
		} else if (!confirmPassword.equals(password)) {
			errors.put("confirmPasswordError", "Mật khẩu xác nhận không khớp");
		} else if (confirmPassword.length() < 6) {
			errors.put("confirmPasswordError", "Mật khẩu xác nhận phải có ít nhất 6 ký tự");
		}

		if (phone != null && !phone.trim().isEmpty()) {
			if (!Validator.isValidPhone(phone)) {
				errors.put("phoneError", "Số điện thoại không hợp lệ");
			}
		}

		if (gender == null || gender.trim().isEmpty()) {
			errors.put("genderError", "Vui lòng chọn giới tính");
		}

		if (agreeTerms == null || !agreeTerms.equals("true")) {
			errors.put("termsError", "Bạn phải đồng ý với điều khoản dịch vụ");
		}

		if (!errors.isEmpty()) {
			request.setAttribute("error", "Vui lòng sửa các lỗi dưới đây");
			request.setAttribute("fullName", fullName);
			request.setAttribute("email", email);
			request.setAttribute("phone", phone);
			request.setAttribute("gender", gender);
			request.setAttribute("agreeTerms", agreeTerms);
			errors.forEach(request::setAttribute);
			// Tạo token mới cho lần thử tiếp theo
			String newCsrfToken = UUID.randomUUID().toString();
			request.getSession().setAttribute("csrfToken", newCsrfToken);
			request.setAttribute("csrfToken", newCsrfToken);
			request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
			return;
		}

		UserDAO userDAO = new UserDAO();
		User user = new User();
		user.setFullName(fullName);
		user.setEmail(email);

		String hashedPassword = SecurityUtil.hashPassword(password);
		user.setPassword(hashedPassword);

		user.setPhone(phone);
		user.setGender(gender);

		boolean success = userDAO.register(user);

		if (success) {
			HttpSession session = request.getSession();
			session.setAttribute("lastRegisteredEmail", email);
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		} else {
			request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại");
			request.setAttribute("fullName", fullName);
			request.setAttribute("email", email);
			request.setAttribute("phone", phone);
			request.setAttribute("gender", gender);
			// Tạo token mới cho lần thử tiếp theo
			String newCsrfToken = UUID.randomUUID().toString();
			request.getSession().setAttribute("csrfToken", newCsrfToken);
			request.setAttribute("csrfToken", newCsrfToken);
			request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
		}
	}
}