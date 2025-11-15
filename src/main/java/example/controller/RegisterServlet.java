package example.controller;

import example.dao.UserDAO;
import example.model.User;
import example.model.Role;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private UserDAO userDAO;

	@Override
	public void init() {
		userDAO = new UserDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("=== REGISTER DEBUG START ===");

		// Lấy thông tin từ form
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String phoneNumber = request.getParameter("phoneNumber");
		String birthDateStr = request.getParameter("birthDate");
		String genderStr = request.getParameter("gender");

		System.out.println("Username: " + username);
		System.out.println("Email: " + email);
		System.out.println("Password: " + password);
		System.out.println("Confirm Password: " + confirmPassword);
		System.out.println("Phone: " + phoneNumber);
		System.out.println("Birth Date: " + birthDateStr);
		System.out.println("Gender: " + genderStr);

		// VALIDATION
		String errorMessage = validateInput(username, email, password, confirmPassword);
		if (errorMessage != null) {
			System.out.println("Validation failed: " + errorMessage);
			request.setAttribute("errorMessage", errorMessage);
			// Giữ lại giá trị đã nhập
			setRequestAttributes(request, username, email, phoneNumber, birthDateStr, genderStr);
			request.getRequestDispatcher("SignUp.jsp").forward(request, response);
			return;
		}

		// Xử lý dữ liệu
		LocalDate birthDate = null;
		if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
			try {
				birthDate = LocalDate.parse(birthDateStr);
			} catch (DateTimeParseException e) {
				System.err.println("Invalid birth date format: " + birthDateStr);
			}
		}

		Boolean gender = null;
		if (genderStr != null && !genderStr.trim().isEmpty()) {
			gender = Boolean.parseBoolean(genderStr);
		}

		// Tạo user object
		User user = new User();
		user.setUsername(username.trim());
		user.setEmail(email.trim());
		user.setPassword(password); // Trong thực tế nên hash password
		user.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
		user.setBirthDate(birthDate);
		user.setGender(gender);
		user.setRole(Role.CUSTOMER); // Mặc định là CUSTOMER

		// Đăng ký user
		boolean isRegistered = userDAO.register(user);

		if (isRegistered) {
			System.out.println("Registration SUCCESS for: " + username);
			request.setAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
			request.getRequestDispatcher("SignIn.jsp").forward(request, response);
		} else {
			System.out.println("Registration FAILED for: " + username);
			request.setAttribute("errorMessage", "Đăng ký thất bại! Email hoặc tên đăng nhập đã tồn tại.");
			setRequestAttributes(request, username, email, phoneNumber, birthDateStr, genderStr);
			request.getRequestDispatcher("SignUp.jsp").forward(request, response);
		}

		System.out.println("=== REGISTER DEBUG END ===");
	}

	private String validateInput(String username, String email, String password, String confirmPassword) {
		if (username == null || username.trim().isEmpty()) {
			return "Tên đăng nhập không được để trống!";
		}
		if (username.trim().length() < 3) {
			return "Tên đăng nhập phải có ít nhất 3 ký tự!";
		}
		if (email == null || email.trim().isEmpty()) {
			return "Email không được để trống!";
		}
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return "Email không hợp lệ!";
		}
		if (password == null || password.isEmpty()) {
			return "Mật khẩu không được để trống!";
		}
		if (password.length() < 6) {
			return "Mật khẩu phải có ít nhất 6 ký tự!";
		}
		if (!password.equals(confirmPassword)) {
			return "Mật khẩu xác nhận không khớp!";
		}
		return null;
	}

	private void setRequestAttributes(HttpServletRequest request, String username, String email, String phoneNumber,
			String birthDate, String gender) {
		request.setAttribute("username", username);
		request.setAttribute("email", email);
		request.setAttribute("phoneNumber", phoneNumber);
		request.setAttribute("birthDate", birthDate);
		request.setAttribute("gender", gender);
	}
}