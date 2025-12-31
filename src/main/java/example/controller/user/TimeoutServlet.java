package example.controller.user;

import example.dao.impl.BookingDAO;
import example.model.transaction.Booking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/timeout")
public class TimeoutServlet extends HttpServlet {
    
    private BookingDAO bookingDAO;
    
    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String bookingIdStr = request.getParameter("bookingId");
        String manual = request.getParameter("manual");
        
        if (bookingIdStr != null) {
            try {
                int bookingId = Integer.parseInt(bookingIdStr);
                Booking booking = bookingDAO.getBookingById(bookingId);
                
                if (booking != null) {
                    if ("true".equals(manual)) {
                        request.setAttribute("manualCancel", true);
                        request.setAttribute("message", "Bạn đã hủy thanh toán thành công. Ghế đã được giải phóng.");
                    } else {
                        request.setAttribute("manualCancel", false);
                        request.setAttribute("message", "Đã hết thời gian giữ ghế. Ghế đã được giải phóng.");
                    }
                    
                    request.setAttribute("bookingId", bookingId);
                }
            } catch (NumberFormatException e) {
            }
        }
        
        request.getRequestDispatcher("/views/auth/timeout.jsp").forward(request, response);
    }
}