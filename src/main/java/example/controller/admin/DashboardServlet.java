package example.controller.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.dao.impl.BookingDAO;
import example.dao.impl.MovieDAO;
import example.dao.impl.UserDAO;
import example.model.movie.Movie;
import example.model.system.User;
import example.model.transaction.Booking;
import example.util.Constant;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BookingDAO bookingDAO;
    private MovieDAO movieDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        movieDAO = new MovieDAO();
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Lấy dữ liệu thống kê cơ bản
            double todayRevenue = bookingDAO.getTodayRevenue();
            int todayBookings = bookingDAO.getTodayBookingsCount();
            int totalUsers = userDAO.getTotalUserCount();
            int totalMovies = movieDAO.getTotalMoviesCount();

            // 2. Lấy thống kê trạng thái booking 
            int pendingCount = bookingDAO.getBookingCountByStatus(Constant.BOOKING_PENDING);
            int successCount = bookingDAO.getBookingCountByStatus(Constant.BOOKING_SUCCESS);
            int cancelledCount = bookingDAO.getBookingCountByStatus(Constant.BOOKING_CANCELLED);

            // 3. Lấy tổng doanh thu
            double allTimeRevenue = bookingDAO.getTotalRevenue();

            // 4. Lấy booking hôm nay cho hoạt động gần đây
            List<Booking> todayBookingsList = bookingDAO.getTodayBookings();
            List<Map<String, String>> recentActivities = new ArrayList<>();

            // Tạo danh sách hoạt động từ booking hôm nay
            if (todayBookingsList != null && !todayBookingsList.isEmpty()) {
                for (Booking booking : todayBookingsList) {
                    Map<String, String> activity = new HashMap<>();
                    
                    // Xác định loại hoạt động dựa trên status
                    if (Constant.BOOKING_SUCCESS.equals(booking.getStatus())) {
                        activity.put("type", "payment");
                        activity.put("icon", "fas fa-credit-card");
                    } else if (Constant.BOOKING_PENDING.equals(booking.getStatus())) {
                        activity.put("type", "booking");
                        activity.put("icon", "fas fa-ticket-alt");
                    } else {
                        activity.put("type", "booking");
                        activity.put("icon", "fas fa-times-circle");
                    }

                    // Lấy thông tin user
                    User user = userDAO.getUserById(booking.getUserId());
                    String userName = (user != null && user.getFullName() != null && !user.getFullName().isEmpty())
                            ? user.getFullName()
                            : "Khách hàng #" + booking.getUserId();

                    // Tạo tiêu đề hoạt động
                    String statusText = "";
                    if (Constant.BOOKING_SUCCESS.equals(booking.getStatus())) {
                        statusText = "đã thanh toán";
                    } else if (Constant.BOOKING_PENDING.equals(booking.getStatus())) {
                        statusText = "đã đặt";
                    } else if (Constant.BOOKING_CANCELLED.equals(booking.getStatus())) {
                        statusText = "đã hủy";
                    }
                    
                    activity.put("title", userName + " " + statusText + " vé #" + booking.getBookingId());
                    activity.put("time", formatTime(booking.getCreatedAt()));
                    recentActivities.add(activity);
                    
                    // Chỉ lấy 5 hoạt động gần nhất
                    if (recentActivities.size() >= 5) {
                        break;
                    }
                }
            }

            // Thêm một số hoạt động mẫu nếu không có hoạt động thật
            if (recentActivities.isEmpty()) {
                recentActivities.add(createActivity("user", "fas fa-user-plus", "Admin đã đăng nhập hệ thống", "Vừa xong"));
                recentActivities.add(createActivity("movie", "fas fa-film", "Phim mới đã được thêm vào hệ thống", "2 giờ trước"));
                recentActivities.add(createActivity("booking", "fas fa-ticket-alt", "Chưa có đơn đặt vé nào hôm nay", "Hôm nay"));
            }

            // 5. Lấy top phim
            List<Movie> topMovies = movieDAO.getTopMovies(5);

            // 6. Truyền dữ liệu sang JSP 
            request.setAttribute("totalRevenue", todayRevenue);
            request.setAttribute("todayBookings", todayBookings);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalMovies", totalMovies);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("successCount", successCount);
            request.setAttribute("cancelledCount", cancelledCount);
            request.setAttribute("allTimeRevenue", allTimeRevenue);
            request.setAttribute("recentActivities", recentActivities);
            request.setAttribute("topMovies", topMovies);

            // Forward đến dashboard.jsp
            request.getRequestDispatcher("/views/admin/pages/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải dữ liệu dashboard: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/pages/dashboard.jsp").forward(request, response);
        }
    }

    private Map<String, String> createActivity(String type, String icon, String title, String time) {
        Map<String, String> activity = new HashMap<>();
        activity.put("type", type);
        activity.put("icon", icon);
        activity.put("title", title);
        activity.put("time", time);
        return activity;
    }

    private String formatTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "Vừa xong";
        }

        long diff = System.currentTimeMillis() - timestamp.getTime();
        long minutes = diff / (60 * 1000);
        long hours = minutes / 60;
        long days = hours / 24;

        if (minutes < 1) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 7) {
            return days + " ngày trước";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return sdf.format(timestamp);
        }
    }
}