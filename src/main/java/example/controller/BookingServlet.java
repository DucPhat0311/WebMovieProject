package example.controller;

import example.dao.BookingDAO;
import example.dao.ShowtimeDAO;
import example.model.Booking;
import example.model.BookingDetail;
import example.model.Constant;
import example.model.Showtime;
import example.model.User;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 1. Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // 2. Lấy tham số từ form
        String showtimeIdParam = request.getParameter("showtimeId");
        String selectedSeatsParam = request.getParameter("selectedSeats");
        
        if (showtimeIdParam == null || selectedSeatsParam == null || selectedSeatsParam.isEmpty()) {
            session.setAttribute("error", "Vui lòng chọn ghế!");
            response.sendRedirect(request.getContextPath() + "/seat-selection?showtimeId=" + showtimeIdParam);
            return;
        }
        
        try {
            int showtimeId = Integer.parseInt(showtimeIdParam);
            
            // 3. Lấy thông tin suất chiếu
            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
            
            if (showtime == null) {
                session.setAttribute("error", "Suất chiếu không tồn tại!");
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            
            // 4. Parse danh sách ghế đã chọn (format: A1,A2,B3,...)
            List<String> selectedSeats = Arrays.asList(selectedSeatsParam.split(","));
            
            // 5. Kiểm tra ghế có còn trống không
            BookingDAO bookingDAO = new BookingDAO();
            List<Integer> bookedSeatIds = bookingDAO.getBookedSeatIds(showtimeId);
            
            // 6. Tính tổng tiền
            double basePrice = showtime.getBasePrice();
            double totalAmount = selectedSeats.size() * basePrice;
            
            // 7. Tạo booking
            Booking booking = new Booking();
            booking.setUserId(user.getUserId());
            booking.setShowtimeId(showtimeId);
            booking.setTotalAmount(totalAmount);
            booking.setStatus(Constant.BOOKING_PENDING);
            
            int bookingId = bookingDAO.createBooking(booking);
            
            if (bookingId > 0) {
                // 8. Tạo booking details
                boolean allDetailsAdded = true;
                int roomId = showtime.getRoomId();
                
                for (String seatCode : selectedSeats) {
                    // Parse seatCode thành seatRow và seatNumber
                    String seatRow = seatCode.substring(0, 1); // A
                    int seatNumber = Integer.parseInt(seatCode.substring(1)); // 1
                    
                    // Lấy seatId từ database
                    int seatId = bookingDAO.getSeatIdByCode(roomId, seatRow, seatNumber);
                    
                    if (seatId > 0) {
                        BookingDetail detail = new BookingDetail(0, bookingId, seatId, basePrice);
                        if (!bookingDAO.addBookingDetail(detail)) {
                            allDetailsAdded = false;
                            break;
                        }
                    } else {
                        allDetailsAdded = false;
                        break;
                    }
                }
                
                if (allDetailsAdded) {
                    // 9. Chuyển đến trang thanh toán
                    session.setAttribute("bookingId", bookingId);
                    session.setAttribute("success", "Đặt vé thành công! Vui lòng thanh toán.");
                    response.sendRedirect(request.getContextPath() + "/payment?bookingId=" + bookingId);
                } else {
                    // Xóa booking đã tạo nếu có lỗi
                    session.setAttribute("error", "Có lỗi xảy ra khi đặt ghế! Vui lòng thử lại.");
                    response.sendRedirect(request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId);
                }
            } else {
                session.setAttribute("error", "Không thể tạo booking! Vui lòng thử lại.");
                response.sendRedirect(request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId);
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}