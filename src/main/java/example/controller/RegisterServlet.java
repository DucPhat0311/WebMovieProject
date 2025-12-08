package example.controller;

import example.dao.UserDAO;
import example.model.Constant;
import example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	// Hiển thị trang đăng ký
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);//hay ông cũng sửa lại link theo cấu trúc mới đi 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String fullName = request.getParameter("fullName");
		String gender = request.getParameter("gender");
		String phone = request.getParameter("phone");

		// Kiểm tra mật khẩu khớp ko
		if (!password.equals(confirmPassword)) {
			request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
			request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);
			return;
		}

		UserDAO userDAO = new UserDAO();

		// Kiểm tra email đã tồn tại chưa
		if (userDAO.checkEmailExists(email)) {
			request.setAttribute("error", "Email đã tồn tại. Vui lòng sử dụng email khác.");
			request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);
			return;
		}

		// Tạo user mới
		User newUser = new User(email, password, fullName, gender, phone, new Timestamp(System.currentTimeMillis()),
				Constant.ROLE_USER);

		boolean success = userDAO.register(newUser);

		if (success) {
			request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
			request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
		} else {
			request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
			request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);
		}
	}
}