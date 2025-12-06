//
//package example.controller;
//
//import example.dao.UserDAO;
//import example.model.User;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//import java.time.LocalDate;
//
//@WebServlet("/register")
//public class RegisterServlet extends HttpServlet {
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		req.setCharacterEncoding("UTF-8");
//		String username = req.getParameter("username");
//		String email = req.getParameter("email");
//		String password = req.getParameter("password");
//		String confirm = req.getParameter("confirmPassword");
//		String phone = req.getParameter("phoneNumber");
//		String birthStr = req.getParameter("birthDate");
//
//		if (!password.equals(confirm)) {
//			req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
//			req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
//			return;
//		}
//		if (UserDAO.isExists(email, username)) {
//			req.setAttribute("errorMessage", "Email hoặc tên đăng nhập đã tồn tại!");
//			req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
//			return;
//		}
//
//		User user = new User();
//		user.setUsername(username);
//		user.setEmail(email);
//		user.setPassword(password); //  tạm plaintext
//		user.setPhoneNumber(phone);
//		if (birthStr != null && !birthStr.isEmpty()) {
//			user.setBirthDate(LocalDate.parse(birthStr));
//		}
//
//		if (UserDAO.register(user)) {
//			req.setAttribute("successMessage", "Đăng ký thành công! Hãy đăng nhập.");
//			req.getRequestDispatcher("/SignIn.jsp").forward(req, resp);
//		} else {
//			req.setAttribute("errorMessage", "Đăng ký thất bại!");
//			req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
//		}
//	}
//
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		req.getRequestDispatcher("/SignUp.jsp").forward(req, resp);
//	}
//}
//
////package example.controller;
////
////import example.dao.UserDAO;
////import example.model.User;
////import example.model.Role;
////import java.io.IOException;
////import java.time.LocalDate;
////import java.time.format.DateTimeParseException;
////import javax.servlet.ServletException;
////import javax.servlet.annotation.WebServlet;
////import javax.servlet.http.HttpServlet;
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
////
////@WebServlet("/register")
////public class RegisterServlet extends HttpServlet {
////	private UserDAO userDAO;
////
////	/**
////	 * B1: Khởi tạo UserDAO khi servlet được load
////	 */
////	@Override
////	public void init() {
////		userDAO = new UserDAO();
////	}
////
////	/**
////	 * XỬ LÝ REQUEST ĐĂNG KÝ (POST) B1: Lấy thông tin từ form B2: Validate dữ liệu
////	 * đầu vào B3: Xử lý và convert dữ liệu B4: Tạo User object B5: Gọi DAO để lưu
////	 * vào database B6: Chuyển hướng theo kết quả
////	 */
////	protected void doPost(HttpServletRequest request, HttpServletResponse response)
////			throws ServletException, IOException {
////
////		// B1: Lấy tất cả parameters từ form
////		String username = request.getParameter("username");
////		String email = request.getParameter("email");
////		String password = request.getParameter("password");
////		String confirmPassword = request.getParameter("confirmPassword");
////		String phoneNumber = request.getParameter("phoneNumber");
////		String birthDateStr = request.getParameter("birthDate");
////		String genderStr = request.getParameter("gender");
////
////		// B2: VALIDATION - Kiểm tra dữ liệu bắt buộc
////		String errorMessage = validateInput(username, email, password, confirmPassword);
////		if (errorMessage != null) {
////			System.out.println("VALIDATION FAILED: " + errorMessage + " - " + email);
////
////			// B3: Nếu validation fail, giữ lại giá trị đã nhập và hiển thị lỗi
////			request.setAttribute("errorMessage", errorMessage);
////			setRequestAttributes(request, username, email, phoneNumber, birthDateStr, genderStr);
////			request.getRequestDispatcher("SignUp.jsp").forward(request, response);
////			return;
////		}
////
////		// B4: XỬ LÝ DỮ LIỆU - Convert từ String sang proper types
////		LocalDate birthDate = null;
////		if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
////			try {
////				birthDate = LocalDate.parse(birthDateStr);
////			} catch (DateTimeParseException e) {
////				System.err.println("INVALID BIRTH DATE: " + birthDateStr);
////			}
////		}
////
////		// B5: Xử lý gender từ String sang Boolean
////		Boolean gender = null;
////		if (genderStr != null && !genderStr.trim().isEmpty()) {
////			gender = Boolean.parseBoolean(genderStr);
////		}
////
////		// B6: TẠO USER OBJECT từ dữ liệu đã xử lý
////		User user = new User();
////		user.setUsername(username.trim());
////		user.setEmail(email.trim());
////		user.setPassword(password); // TODO: Nên hash password trong thực tế
////		user.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
////		user.setBirthDate(birthDate);
////		user.setGender(gender);
////		user.setRole(Role.CUSTOMER); // Mặc định role CUSTOMER
////
////		// B7: GỌI DAO ĐỂ ĐĂNG KÝ USER
////		boolean isRegistered = userDAO.register(user);
////
////		// B8: CHUYỂN HƯỚNG THEO KẾT QUẢ
////		if (isRegistered) {
////			// Thành công: chuyển đến trang đăng nhập với thông báo
////			request.setAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
////			request.getRequestDispatcher("SignIn.jsp").forward(request, response);
////		} else {
////			// Thất bại: quay lại form đăng ký với thông báo lỗi
////			request.setAttribute("errorMessage", "Đăng ký thất bại! Email hoặc tên đăng nhập đã tồn tại.");
////			setRequestAttributes(request, username, email, phoneNumber, birthDateStr, genderStr);
////			request.getRequestDispatcher("SignUp.jsp").forward(request, response);
////		}
////	}
////
////	/**
////	 * VALIDATION DỮ LIỆU ĐẦU VÀO B1: Kiểm tra các field bắt buộc không được trống
////	 * B2: Kiểm tra độ dài tối thiểu B3: Kiểm tra format email B4: Kiểm tra password
////	 * match
////	 */
////	private String validateInput(String username, String email, String password, String confirmPassword) {
////		// B1: Kiểm tra username
////		if (username == null || username.trim().isEmpty()) {
////			return "Tên đăng nhập không được để trống!";
////		}
////		if (username.trim().length() < 3) {
////			return "Tên đăng nhập phải có ít nhất 3 ký tự!";
////		}
////
////		// B2: Kiểm tra email
////		if (email == null || email.trim().isEmpty()) {
////			return "Email không được để trống!";
////		}
////		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
////			return "Email không hợp lệ!";
////		}
////
////		// B3: Kiểm tra password
////		if (password == null || password.isEmpty()) {
////			return "Mật khẩu không được để trống!";
////		}
////		if (password.length() < 6) {
////			return "Mật khẩu phải có ít nhất 6 ký tự!";
////		}
////		if (!password.equals(confirmPassword)) {
////			return "Mật khẩu xác nhận không khớp!";
////		}
////
////		// B4: Validation passed
////		return null;
////	}
////
////	/**
////	 * GIỮ LẠI GIÁ TRỊ ĐÃ NHẬP TRONG FORM B1: Set các attribute để JSP hiển thị lại
////	 * giá trị cũ B2: Giúp user không phải nhập lại khi validation fail
////	 */
////	private void setRequestAttributes(HttpServletRequest request, String username, String email, String phoneNumber,
////			String birthDate, String gender) {
////		request.setAttribute("username", username);
////		request.setAttribute("email", email);
////		request.setAttribute("phoneNumber", phoneNumber);
////		request.setAttribute("birthDate", birthDate);
////		request.setAttribute("gender", gender);
////	}
////}
//
