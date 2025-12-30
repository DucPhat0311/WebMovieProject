package example.controller.admin;

import example.dao.impl.UserDAO;
import example.model.system.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/manage-accounts")
public class ManageAccountServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Lấy tham số tìm kiếm (chỉ search thôi, không cần filter phức tạp)
            String search = request.getParameter("search");
            
            // Lấy danh sách users
            List<User> users;
            
            if (search != null && !search.trim().isEmpty()) {
                users = userDAO.searchUsers(search);
            } else {
                users = userDAO.getAllUsers();
            }
            
            // Thống kê đơn giản
            int totalUsers = users.size();
            int adminCount = 0;
            int userCount = 0;
            
            for (User user : users) {
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    adminCount++;
                } else {
                    userCount++;
                }
            }
            
            // Đặt vào request
            request.setAttribute("users", users);
            request.setAttribute("search", search);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("adminCount", adminCount);
            request.setAttribute("userCount", userCount);
            
            // Chuyển đến view
            request.getRequestDispatcher("/views/admin/pages/account-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã có lỗi xảy ra khi tải danh sách tài khoản");
            request.getRequestDispatcher("/views/admin/pages/account-list.jsp").forward(request, response);
        }
    }
}