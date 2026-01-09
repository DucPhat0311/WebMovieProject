package example.controller.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate();
        }

        // Xóa tất cả cookies Remember Me
        clearRememberMeCookies(response);
        
        clearSessionCookie(response);

        response.sendRedirect(request.getContextPath() + "/home");
    }
    
    private void clearRememberMeCookies(HttpServletResponse response) {
        // Xóa remember email cookie
        Cookie emailCookie = new Cookie("rememberedEmail", "");
        emailCookie.setMaxAge(0);
        emailCookie.setPath("/");
        response.addCookie(emailCookie);
        
        // Xóa remember name cookie (nếu có)
        Cookie nameCookie = new Cookie("rememberedName", "");
        nameCookie.setMaxAge(0);
        nameCookie.setPath("/");
        response.addCookie(nameCookie);
        
        System.out.println("Remember Me cookies cleared");
    }
    
    private void clearSessionCookie(HttpServletResponse response) {
        // Xóa JSESSIONID cookie (session cookie)
        Cookie sessionCookie = new Cookie("JSESSIONID", "");
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }
}