
package example.controller;

import example.dao.UserDAO;
import example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String input = req.getParameter("email");
        String pass = req.getParameter("password");

        User user = UserDAO.login(input, pass);
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect("homepage"); // → HomeServlet
        } else {
            req.setAttribute("errorMessage", "Sai email/tên đăng nhập hoặc mật khẩu!");
            req.getRequestDispatcher("/SignIn.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/SignIn.jsp").forward(req, resp);
    }
}

//package example.controller;
//
//import example.dao.UserDAO;
//import example.model.User;
//import example.model.Role;
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//	private UserDAO userDAO;
//
//	/**
//	 * B1: Khởi tạo UserDAO khi servlet được load
//	 */
//	@Override
//	public void init() {
//		userDAO = new UserDAO();
//	}
//
//	/**
//	 * XỬ LÝ REQUEST ĐĂNG NHẬP (POST) B1: Lấy email và password từ form B2: Gọi DAO
//	 * để xác thực B3: Nếu thành công: tạo session và chuyển hướng B4: Nếu thất bại:
//	 * hiển thị lỗi
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		// B1: Lấy thông tin đăng nhập từ form
//		String email = request.getParameter("email");
//		String password = request.getParameter("password");
//
//		// B2: GỌI DAO ĐỂ XÁC THỰC ĐĂNG NHẬP
//		User user = userDAO.login(email, password);
//
//		// B3: XỬ LÝ KẾT QUẢ ĐĂNG NHẬP
//		if (user != null) {
//			// B4: ĐẢM BẢO USER CÓ ROLE (fallback nếu null)
//			if (user.getRole() == null) {
//				System.err.println("WARNING: User role is null! Setting default: CUSTOMER");
//				user.setRole(Role.CUSTOMER);
//			}
//
//			// B5: TẠO SESSION VÀ LƯU THÔNG TIN USER
//			HttpSession session = request.getSession();
//			session.setAttribute("user", user); // Toàn bộ user object
//			session.setAttribute("username", user.getUsername()); // Chỉ username
//			session.setAttribute("role", user.getRole().name()); // Role cho authorization
//
//			// B6: CHUYỂN HƯỚNG ĐẾN TRANG CHỦ
//			response.sendRedirect("homepage.jsp");
//
//		} else {
//			// B7: ĐĂNG NHẬP THẤT BẠI - HIỂN THỊ LỖI
//			System.out.println("LOGIN FAILED: Invalid credentials for - " + email);
//			request.setAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
//			request.getRequestDispatcher("SignIn.jsp").forward(request, response);
//		}
//	}
//}

