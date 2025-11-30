package example.controller;

import example.dao.UserDAO;
import example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirmPassword");
		String phone = req.getParameter("phoneNumber");
		String birthStr = req.getParameter("birthDate");

		if (!password.equals(confirm)) {
			req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
			req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
			return;
		}
		if (UserDAO.isExists(email, username)) {
			req.setAttribute("errorMessage", "Email hoặc tên đăng nhập đã tồn tại!");
			req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
			return;
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password); //  tạm plaintext
		user.setPhoneNumber(phone);
		if (birthStr != null && !birthStr.isEmpty()) {
			user.setBirthDate(LocalDate.parse(birthStr));
		}

		if (UserDAO.register(user)) {
			req.setAttribute("successMessage", "Đăng ký thành công! Hãy đăng nhập.");
			req.getRequestDispatcher("/SignIn.jsp").forward(req, resp);
		} else {
			req.setAttribute("errorMessage", "Đăng ký thất bại!");
			req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
	}
}