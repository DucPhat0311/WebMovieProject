package example.controller.user;

import example.dao.impl.*;
import example.model.movie.Movie;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.model.transaction.Booking;
import example.model.transaction.Payment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {
    
    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;
    private ShowtimeDAO showtimeDAO;
    private MovieDAO movieDAO;
    
    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
        showtimeDAO = new ShowtimeDAO();
        movieDAO = new MovieDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 1. Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String bookingIdStr = request.getParameter("bookingId");
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Thiếu thông tin booking");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            return;
        }
        
        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            
            // 2. Lấy thông tin booking
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin đặt vé");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }
            
            // 3. Kiểm tra quyền sở hữu
            if (booking.getUserId() != user.getUserId()) {
                request.setAttribute("errorMessage", "Bạn không có quyền xem thông tin này");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }
            
            // 4. Lấy thông tin payment
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            
            // 5. Lấy thông tin showtime
            Showtime showtime = showtimeDAO.getShowtimeById(booking.getShowtimeId());
            if (showtime == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin suất chiếu");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }
            
            // 6. Lấy thông tin movie
            Movie movie = movieDAO.getMovieById(showtime.getMovieId());
            
            // 7. Lấy danh sách ghế đã chọn
            List<String> selectedSeats = bookingDAO.getSelectedSeats(bookingId);
            
            // 8. Set attributes cho ticket.jsp
            request.setAttribute("booking", booking);
            request.setAttribute("payment", payment);
            request.setAttribute("showtime", showtime);
            request.setAttribute("movie", movie);
            request.setAttribute("selectedSeats", selectedSeats);
            request.setAttribute("seatsCount", selectedSeats.size());
            
            // 9. Forward đến trang ticket
            request.getRequestDispatcher("/views/user/pages/ticket.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID booking không hợp lệ");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
        }
    }
}