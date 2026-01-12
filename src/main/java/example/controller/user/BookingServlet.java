package example.controller.user;

import example.dao.impl.BookingDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.model.transaction.Booking;
import example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check Login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // 1. Lấy dữ liệu từ Form
            String showtimeIdStr = request.getParameter("showtimeId");
            String[] selectedSeats = request.getParameterValues("selectedSeats"); // ["A1", "B5"...]

            // Validate dữ liệu đầu vào
            if (showtimeIdStr == null || selectedSeats == null || selectedSeats.length == 0) {
                request.setAttribute("errorMessage", "Vui lòng chọn ít nhất 1 ghế!");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }

            int showtimeId = Integer.parseInt(showtimeIdStr);
            List<String> seatCodeList = new ArrayList<>(Arrays.asList(selectedSeats));

            // 2. Kiểm tra Suất chiếu
            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);

            if (showtime == null) {
                request.setAttribute("errorMessage", "Suất chiếu không tồn tại!");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }

            // 3. Tạo đối tượng Booking 
            Booking booking = new Booking();
            booking.setUserId(user.getUserId());
            booking.setShowtimeId(showtimeId);
            booking.setStatus(Constant.BOOKING_PENDING);

            // 4. Gọi DAO: Check trùng ghế + Tính tiền + Lưu DB
            BookingDAO bookingDAO = new BookingDAO();
            
            int bookingId = bookingDAO.createBookingWithSeats(booking, seatCodeList, showtime.getRoomId());

            if (bookingId > 0) {
                Booking savedBooking = bookingDAO.getBookingById(bookingId);

                session.setAttribute("bookingId", bookingId);
                session.setAttribute("booking", savedBooking);
                session.setAttribute("showtimeId", showtimeId);
                session.setAttribute("selectedSeats", seatCodeList);
                session.setAttribute("totalAmount", savedBooking.getTotalAmount()); 

                response.sendRedirect(request.getContextPath() + "/checkout");

            } else if (bookingId == -1) {
                String errorUrl = request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId
                        + "&error=ghe_da_duoc_dat";
                response.sendRedirect(errorUrl);
            } else {
                request.setAttribute("errorMessage", "Đặt vé thất bại. Vui lòng kiểm tra lại thông tin.");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
        }
    }
}