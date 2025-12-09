package example.controller;

import example.dao.UserDAO;
import example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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
        } else if (!isValidEmail(email)) {
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
        }
        
        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("[0-9]{10,11}")) {
                errors.put("phoneError", "Số điện thoại phải có 10-11 chữ số");
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
            request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setGender(gender);
        
        boolean success = userDAO.register(user);
        
        if (success) {
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập");
            request.getRequestDispatcher("/views/jsp/SignIn.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại");
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("gender", gender);
            request.getRequestDispatcher("/views/jsp/SignUp.jsp").forward(request, response);
        }
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}