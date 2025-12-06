package example.controller;

import example.dao.MovieDAO;
import example.dao.ShowtimeDAO;
import example.model.Movie;
import example.model.Showtime;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/movie-detail")
public class MovieDetailServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // lấy id phim từ URL (movie-detail?id=1)
            int movieId = Integer.parseInt(request.getParameter("id"));
            
            MovieDAO movieDAO = new MovieDAO();
            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            
            // lấy thông tin chi tiết phim
            Movie movie = movieDAO.getMovieById(movieId);
            if (movie == null) {
                response.sendRedirect("home"); // id sai ==> về trang chủ
                return;
            }
            
            // lấy danh sách các ngày có lịch chiếu 
            List<Date> showDates = showtimeDAO.getShowDates(movieId);
            
            // logic chọn ngày
            Date selectedDate = null;
            String dateParamater = request.getParameter("date");
            
            if (dateParamater != null) {
                // Nếu user bấm chọn ngày -> Lấy ngày đó
                selectedDate = Date.valueOf(dateParamater);
            } else if (!showDates.isEmpty()) {
                // Nếu user mới vào trang -> Mặc định lấy ngày đầu tiên có lịch
                selectedDate = showDates.get(0);
            }
            
            // Lấy danh sách suất chiếu của NGÀY ĐÃ CHỌN
            List<Showtime> showtimes = null;
            if (selectedDate != null) {
                showtimes = showtimeDAO.getShowtimesByDate(movieId, selectedDate);
            }
            
            // đẩy dữ liệu sang JSP
            request.setAttribute("movie", movie);
            request.setAttribute("showDates", showDates);     // danh sách các ngày
            request.setAttribute("selectedDate", selectedDate); // Ngày đang dc click vào
            request.setAttribute("showtimes", showtimes);     // Danh sách giờ chiếu
            
            request.getRequestDispatcher("detail.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}