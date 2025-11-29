//package example.controller;
//
//import example.dao.MovieDAO;
//import example.model.Movie;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//
//public class HomeServlet extends HttpServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        MovieDAO dao = MovieDAO.getInstance();
//
//        List<Movie> nowShowingList = dao.getNowShowingMovies();
//        List<Movie> willShowList = dao.willShowMovies();
//
//
//        request.setAttribute("nowShowingList", nowShowingList);
//        request.setAttribute("willShowList", willShowList);
//
//
//        request.getRequestDispatcher("homepage.jsp").forward(request, response);
//    }
//}
