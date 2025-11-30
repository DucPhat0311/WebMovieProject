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