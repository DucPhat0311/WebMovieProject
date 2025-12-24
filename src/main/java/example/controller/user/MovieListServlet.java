package example.controller.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.dao.impl.MovieDAO;
import example.model.movie.Movie;

/**
 * Servlet implementation class MovieListServlet
 */
@WebServlet("/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String type = request.getParameter("type");
        
        if (type == null) type = "now"; 

        MovieDAO dao = new MovieDAO();
        List<Movie> list = dao.getNextMovies(type, 8, 0);

        request.setAttribute("listM", list);
        request.setAttribute("pageType", type); 

        request.getRequestDispatcher("/views/user/pages/movie-list.jsp").forward(request, response);    }
}