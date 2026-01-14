package example.controller.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import example.dao.impl.MovieDAO;

@WebServlet("/admin/delete-movie")
public class DeleteMovieServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        try {
            int id = Integer.parseInt(idStr);
            
            MovieDAO dao = new MovieDAO();
            dao.deleteMovie(id);
            
            response.sendRedirect("manage-movies"); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}