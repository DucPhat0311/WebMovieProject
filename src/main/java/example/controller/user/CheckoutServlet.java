package example.controller.user;

import example.dao.impl.BookingDAO;
import example.dao.impl.PaymentDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.schedule.Showtime;
import example.model.system.User;
import example.model.transaction.Booking;
import example.model.transaction.Payment;
import example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final int TIMEOUT_MINUTES = 1; // 1 phút cho test
    
    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;
    private ShowtimeDAO showtimeDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
        showtimeDAO = new ShowtimeDAO();
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

        // 2. Lấy thông tin booking từ session
        Integer bookingId = (Integer) session.getAttribute("bookingId");
        if (bookingId == null) {
            request.setAttribute("errorMessage", "Không tìm thấy thông tin đặt vé");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            return;
        }

        // 3. Kiểm tra booking có tồn tại và chưa timeout
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            session.removeAttribute("bookingId");
            request.setAttribute("errorMessage", "Booking không tồn tại");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            return;
        }

        // 4. KIỂM TRA TIMEOUT CHÍNH XÁC - THÊM LOG ĐỂ DEBUG
        if (bookingDAO.isBookingExpired(bookingId, TIMEOUT_MINUTES)) {
            System.out.println("Booking " + bookingId + " đã hết hạn - tự động hủy");
            
            // Tự động hủy booking quá timeout
            boolean cancelled = bookingDAO.cancelBookingAndReleaseSeats(bookingId);
            System.out.println("Kết quả hủy booking: " + cancelled);
            
            session.removeAttribute("bookingId");

            request.setAttribute("errorMessage", "Đã hết thời gian giữ ghế. Vui lòng chọn lại");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            return;
        }

        // 5. Tính thời gian còn lại
        Timestamp createdAt = bookingDAO.getBookingCreatedTime(bookingId);
        if (createdAt == null) {
            request.setAttribute("errorMessage", "Không thể xác định thời gian tạo booking");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            return;
        }
        
        // Tính thời gian hết hạn chính xác
        long expiryTimeMillis = createdAt.getTime() + (TIMEOUT_MINUTES * 60 * 1000);
        long currentTimeMillis = System.currentTimeMillis();
        long remainingMillis = expiryTimeMillis - currentTimeMillis;
        
        long remainingSeconds = Math.max(0, remainingMillis / 1000);
        
        // DEBUG LOG
        System.out.println("=== DEBUG TIMER ===");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Created at: " + createdAt);
        System.out.println("Expiry time: " + new Timestamp(expiryTimeMillis));
        System.out.println("Current time: " + new Timestamp(currentTimeMillis));
        System.out.println("Remaining seconds: " + remainingSeconds);
        System.out.println("===================");
        
        request.setAttribute("remainingSeconds", remainingSeconds);
        request.setAttribute("expiryTimeMillis", expiryTimeMillis); // Gửi thời gian hết hạn đến client

        // 6. Lấy thông tin showtime để hiển thị
        Showtime showtime = showtimeDAO.getShowtimeById(booking.getShowtimeId());
        request.setAttribute("showtime", showtime);
        request.setAttribute("booking", booking);
        request.setAttribute("bookingId", bookingId);
        
        // Lấy ghế đã chọn từ session
        @SuppressWarnings("unchecked")
        List<String> selectedSeats = (List<String>) session.getAttribute("selectedSeats");
        request.setAttribute("selectedSeats", selectedSeats);

        // 7. Forward đến trang checkout
        request.getRequestDispatcher("/views/user/pages/checkout.jsp").forward(request, response);
    }

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

        // 2. Lấy thông tin từ request
        Integer bookingId = (Integer) session.getAttribute("bookingId");
        String paymentMethod = request.getParameter("paymentMethod");

        if (bookingId == null || paymentMethod == null) {
            request.setAttribute("errorMessage", "Thiếu thông tin thanh toán");
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            return;
        }

        try {
            // 3. Kiểm tra timeout trước khi thanh toán
            if (bookingDAO.isBookingExpired(bookingId, TIMEOUT_MINUTES)) {
                bookingDAO.cancelBookingAndReleaseSeats(bookingId);
                session.removeAttribute("bookingId");

                request.setAttribute("errorMessage", "Đã hết thời gian thanh toán");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }

            // 4. Lấy thông tin booking
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                request.setAttribute("errorMessage", "Booking không tồn tại");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
                return;
            }

            // 5. Tạo payment record
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus("Success");
            payment.setPaymentDate(new Date());

            // 6. Lưu payment và cập nhật trạng thái booking
            if (paymentDAO.createPayment(payment)) {
                bookingDAO.updateBookingStatus(bookingId, Constant.BOOKING_SUCCESS);

                // Xóa thông tin booking khỏi session
                session.removeAttribute("bookingId");
                session.removeAttribute("totalAmount");
                session.removeAttribute("showtimeId");
                session.removeAttribute("selectedSeats");

                // 7. Chuyển đến trang thành công
                response.sendRedirect(request.getContextPath() + "/ticket?bookingId=" + bookingId);
            } else {
                request.setAttribute("errorMessage", "Thanh toán thất bại");
                request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
        }
    }
}