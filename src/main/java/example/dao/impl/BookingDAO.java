package example.dao.impl;

import example.dao.core.DBConnection;
import example.model.cinema.Seat;
import example.model.cinema.SeatType;
import example.model.system.User;
import example.model.transaction.Booking;
import example.model.transaction.Payment;
import example.util.Constant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BookingDAO {
    private static final int MAX_RETRIES = 3;

    // TẠO BOOKING MỚI (CÓ CHECK TRÙNG GHẾ & TÍNH TIỀN)
    public int createBookingWithSeats(Booking booking, List<String> seatCodes, int roomId) {
        int retryCount = 0;

        while (retryCount < MAX_RETRIES) {
            Connection conn = null;
            PreparedStatement psLock = null;
            PreparedStatement psCheck = null;
            PreparedStatement psBooking = null;
            PreparedStatement psDetail = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                // --- BƯỚC 1: LOCK CÁC GHẾ ĐANG CHỌN ---
                StringBuilder lockSql = new StringBuilder();
                lockSql.append("SELECT seat_id FROM seat WHERE room_id = ? AND (");
                for (int i = 0; i < seatCodes.size(); i++) {
                    lockSql.append(i == 0 ? "CONCAT(seat_row, seat_number) = ?" 
                        : " OR CONCAT(seat_row, seat_number) = ?");
                }
                lockSql.append(") FOR UPDATE");

                psLock = conn.prepareStatement(lockSql.toString());
                psLock.setInt(1, roomId);
                for (int i = 0; i < seatCodes.size(); i++) {
                    psLock.setString(i + 2, seatCodes.get(i));
                }

                List<Integer> seatIds = new ArrayList<>();
                rs = psLock.executeQuery();
                while (rs.next())
                    seatIds.add(rs.getInt("seat_id"));

                // Validate số ghế
                if (seatIds.size() != seatCodes.size()) {
                    conn.rollback();
                    return -1; // Mã ghế không hợp lệ
                }

                // --- BƯỚC 2: KIỂM TRA GHẾ ĐÃ ĐƯỢC ĐẶT CHƯA ---
                StringBuilder checkSql = new StringBuilder();
                checkSql.append("SELECT 1 FROM bookingdetail bd ");
                checkSql.append("JOIN booking b ON bd.booking_id = b.booking_id ");
                checkSql.append("WHERE b.showtime_id = ? ");
                checkSql.append("AND b.status IN (?, ?) ");
                checkSql.append("AND bd.seat_id IN (");
                for (int i = 0; i < seatIds.size(); i++)
                    checkSql.append(i == 0 ? "?" : ",?");
                checkSql.append(")");

                psCheck = conn.prepareStatement(checkSql.toString());
                psCheck.setInt(1, booking.getShowtimeId());
                psCheck.setString(2, Constant.BOOKING_PENDING);
                psCheck.setString(3, Constant.BOOKING_SUCCESS);
                for (int i = 0; i < seatIds.size(); i++)
                    psCheck.setInt(i + 4, seatIds.get(i));

                rs = psCheck.executeQuery();
                if (rs.next()) {
                    conn.rollback();
                    return -2; // Ghế đã bị đặt
                }

                // --- BƯỚC 3: TÍNH TỔNG TIỀN ---
                double totalAmount = calculateTotalPrice(booking.getShowtimeId(), seatIds);

                // --- BƯỚC 4: INSERT BOOKING ---
                String sqlBooking = "INSERT INTO booking (user_id, showtime_id, total_amount, status, created_at) VALUES (?, ?, ?, ?, ?)";
                psBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
                psBooking.setInt(1, booking.getUserId());
                psBooking.setInt(2, booking.getShowtimeId());
                psBooking.setDouble(3, totalAmount);
                psBooking.setString(4, Constant.BOOKING_PENDING);
                psBooking.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

                psBooking.executeUpdate();
                rs = psBooking.getGeneratedKeys();
                int bookingId = 0;
                if (rs.next())
                    bookingId = rs.getInt(1);

                // --- BƯỚC 5: INSERT BOOKING DETAIL ---
                String sqlDetail = "INSERT INTO bookingdetail (booking_id, seat_id, price) VALUES (?, ?, ?)";
                psDetail = conn.prepareStatement(sqlDetail);
                
                // Tính giá từng ghế
                double basePrice = getBasePriceByShowtimeId(conn, booking.getShowtimeId());
                SeatDAO seatDAO = new SeatDAO();
                
                for (Integer seatId : seatIds) {
                    Seat seat = seatDAO.getSeatById(seatId);
                    double seatPrice = calculateSeatPrice(seat, basePrice);
                    
                    psDetail.setInt(1, bookingId);
                    psDetail.setInt(2, seatId);
                    psDetail.setDouble(3, seatPrice);
                    psDetail.addBatch();
                }
                psDetail.executeBatch();

                conn.commit();
                return bookingId;

            } catch (SQLException e) {
                // Deadlock retry logic
                if (e.getErrorCode() == 1213) {
                    System.out.println("[DEADLOCK] Retry " + (retryCount + 1) + "/" + MAX_RETRIES);
                    retryCount++;
                    try {
                        Thread.sleep(100 * retryCount);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return 0;
                    }
                    continue;
                }

                e.printStackTrace();
                try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
                return 0;
            } finally {
                closeResources(rs, psLock, psCheck, psBooking, psDetail, conn);
            }
        }
        System.out.println("Failed after " + MAX_RETRIES + " retries due to deadlock");
        return 0;
    }

    // LẤY ID GHẾ ĐÃ ĐẶT
    public List<Integer> getBookedSeatIds(int showtimeId) {
        List<Integer> seatIds = new ArrayList<>();
        String sql = "SELECT bd.seat_id FROM bookingdetail bd " + 
                     "JOIN booking b ON bd.booking_id = b.booking_id " +
                     "WHERE b.showtime_id = ? AND b.status IN (?, ?)";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, showtimeId);
            ps.setString(2, Constant.BOOKING_PENDING);
            ps.setString(3, Constant.BOOKING_SUCCESS);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    seatIds.add(rs.getInt("seat_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatIds;
    }

    public List<String> getSelectedSeats(int bookingId) {
        List<String> seats = new ArrayList<>();
        String sql = "SELECT s.seat_row, s.seat_number FROM bookingdetail bd " +
                     "JOIN seat s ON bd.seat_id = s.seat_id WHERE bd.booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seats.add(rs.getString("seat_row") + rs.getInt("seat_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // CÁC HÀM XỬ LÝ CHECKOUT / TIMEOUT
    public java.sql.Timestamp getBookingCreatedTime(Integer bookingId) {
        String sql = "SELECT created_at FROM booking WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("created_at");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isBookingExpired(Integer bookingId, int timeoutMinutes) {
        Booking booking = getBookingById(bookingId);
        return isBookingExpired(booking, timeoutMinutes);
    }

    public boolean isBookingExpired(Booking booking, int timeoutMinutes) {
        if (booking == null || !Constant.BOOKING_PENDING.equals(booking.getStatus())) {
            return true;
        }
        long timeoutMillis = timeoutMinutes * 60 * 1000L;
        long elapsedMillis = System.currentTimeMillis() - booking.getCreatedAt().getTime();
        return elapsedMillis >= timeoutMillis;
    }

    public boolean cancelBookingAndReleaseSeats(Integer bookingId) {
        return updateBookingStatus(bookingId, Constant.BOOKING_CANCELLED);
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getRemainingSeconds(Integer bookingId, int timeoutMinutes) {
        java.sql.Timestamp createdAt = getBookingCreatedTime(bookingId);
        if (createdAt == null) return 0;

        long expiryTime = createdAt.getTime() + (timeoutMinutes * 60 * 1000L);
        long remaining = expiryTime - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    public void cancelExpiredPendingBookings(int timeoutMinutes) {
        // Tính mốc thời gian cutoff bằng Java (tránh lệch giờ Server DB)
        long timeoutMillis = timeoutMinutes * 60 * 1000L;
        Timestamp cutoffTime = new Timestamp(System.currentTimeMillis() - timeoutMillis);

        String sql = "UPDATE booking SET status = ? WHERE status = ? AND created_at < ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, Constant.BOOKING_CANCELLED);
            ps.setString(2, Constant.BOOKING_PENDING);
            ps.setTimestamp(3, cutoffTime);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[System Cleanup] Đã hủy " + rows + " booking quá hạn");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSeatBooked(int seatId, int showtimeId) {
        String sql = "SELECT COUNT(*) FROM booking b " + 
                     "JOIN bookingdetail bd ON b.booking_id = bd.booking_id " +
                     "WHERE b.showtime_id = ? AND bd.seat_id = ? AND b.status IN (?, ?)";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, showtimeId);
            ps.setInt(2, seatId);
            ps.setString(3, Constant.BOOKING_PENDING);
            ps.setString(4, Constant.BOOKING_SUCCESS);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setShowtimeId(rs.getInt("showtime_id"));
                    b.setTotalAmount(rs.getDouble("total_amount"));
                    b.setStatus(rs.getString("status"));
                    b.setCreatedAt(rs.getTimestamp("created_at"));
                    return b;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean completeCheckout(int bookingId, Payment payment) {
        Connection conn = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psPayment = null;

        try {
            // Kiểm tra trạng thái booking trước
            Booking booking = getBookingById(bookingId);
            if (booking == null) {
                System.out.println("Booking " + bookingId + " not found");
                return false;
            }
            
            if (Constant.BOOKING_SUCCESS.equals(booking.getStatus())) {
                System.out.println("Booking " + bookingId + " already completed");
                return true;
            }
            
            if (Constant.BOOKING_CANCELLED.equals(booking.getStatus())) {
                System.out.println("Booking " + bookingId + " was cancelled");
                return false;
            }

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Update booking status (chỉ thành công nếu vẫn là Pending)
            String sqlUpdate = "UPDATE booking SET status = ? WHERE booking_id = ? AND status = ?";
            psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, Constant.BOOKING_SUCCESS);
            psUpdate.setInt(2, bookingId);
            psUpdate.setString(3, Constant.BOOKING_PENDING);

            int rowsUpdated = psUpdate.executeUpdate();
            if (rowsUpdated == 0) {
                conn.rollback();
                System.out.println("Booking " + bookingId + " status changed before payment");
                return false;
            }

            // 2. Insert payment record
            String sqlPayment = "INSERT INTO payment (booking_id, amount, method, status, paid_at) VALUES (?, ?, ?, ?, ?)";
            psPayment = conn.prepareStatement(sqlPayment);
            psPayment.setInt(1, bookingId);
            psPayment.setDouble(2, payment.getAmount());
            psPayment.setString(3, payment.getPaymentMethod());
            psPayment.setString(4, Constant.PAYMENT_SUCCESS);
            psPayment.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            psPayment.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, psUpdate, psPayment, conn);
        }
    }

    // ========== PRIVATE HELPER METHODS ==========
    private double calculateTotalPrice(int showtimeId, List<Integer> seatIds) {
        double total = 0;
        try (Connection conn = DBConnection.getConnection()) {
            double basePrice = getBasePriceByShowtimeId(conn, showtimeId);
            SeatDAO seatDAO = new SeatDAO();
            
            for (int seatId : seatIds) {
                Seat seat = seatDAO.getSeatById(seatId);
                if (seat != null) {
                    total += calculateSeatPrice(seat, basePrice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private double calculateSeatPrice(Seat seat, double basePrice) {
        if (seat == null) return basePrice;
        
        SeatDAO seatDAO = new SeatDAO();
        SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
        return (seatType != null) ? basePrice + seatType.getSurcharge() : basePrice;
    }

    private double getBasePriceByShowtimeId(Connection conn, int showtimeId) throws SQLException {
        String sql = "SELECT base_price FROM showtime WHERE showtime_id = ?";
        boolean isNewConnection = (conn == null);
        Connection localConn = isNewConnection ? DBConnection.getConnection() : conn;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = localConn.prepareStatement(sql);
            ps.setInt(1, showtimeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("base_price");
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (isNewConnection && localConn != null) localConn.close();
        }
        return 0;
    }

    private void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            try {
                if (resource != null) resource.close();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    // ========== DANH SÁCH & TÌM KIẾM ==========
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, " +
                     "c.cinema_name, r.room_name, st.show_date, st.start_time " + 
                     "FROM booking b " +
                     "JOIN user u ON b.user_id = u.user_id " +
                     "JOIN showtime st ON b.showtime_id = st.showtime_id " +
                     "JOIN movie m ON st.movie_id = m.movie_id " +
                     "JOIN room r ON st.room_id = r.room_id " +
                     "JOIN cinema c ON r.cinema_id = c.cinema_id " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, " +
                     "c.cinema_name, r.room_name, st.show_date, st.start_time " + 
                     "FROM booking b " +
                     "JOIN user u ON b.user_id = u.user_id " +
                     "JOIN showtime st ON b.showtime_id = st.showtime_id " +
                     "JOIN movie m ON st.movie_id = m.movie_id " +
                     "JOIN room r ON st.room_id = r.room_id " +
                     "JOIN cinema c ON r.cinema_id = c.cinema_id " +
                     "WHERE b.status = ? " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> searchBookings(String keyword) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, " +
                     "c.cinema_name, r.room_name, st.show_date, st.start_time " + 
                     "FROM booking b " +
                     "JOIN user u ON b.user_id = u.user_id " +
                     "JOIN showtime st ON b.showtime_id = st.showtime_id " +
                     "JOIN movie m ON st.movie_id = m.movie_id " +
                     "JOIN room r ON st.room_id = r.room_id " +
                     "JOIN cinema c ON r.cinema_id = c.cinema_id " +
                     "WHERE b.booking_id LIKE ? OR u.full_name LIKE ? OR u.email LIKE ? OR m.title LIKE ? " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getBookingsByDateRange(Date fromDate, Date toDate) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, " +
                     "c.cinema_name, r.room_name, st.show_date, st.start_time " + 
                     "FROM booking b " +
                     "JOIN user u ON b.user_id = u.user_id " +
                     "JOIN showtime st ON b.showtime_id = st.showtime_id " +
                     "JOIN movie m ON st.movie_id = m.movie_id " +
                     "JOIN room r ON st.room_id = r.room_id " +
                     "JOIN cinema c ON r.cinema_id = c.cinema_id " +
                     "WHERE DATE(b.created_at) BETWEEN ? AND ? " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public int getTotalSeatsByBooking(int bookingId) {
        String sql = "SELECT COUNT(*) as total_seats FROM bookingdetail WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_seats");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setShowtimeId(rs.getInt("showtime_id"));
        booking.setBookingDate(rs.getTimestamp("created_at"));
        booking.setTotalAmount(rs.getDouble("total_amount"));
        booking.setStatus(rs.getString("status"));
        booking.setCreatedAt(rs.getTimestamp("created_at"));

        User user = new User();
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));

        return booking;
    }

    // ========== THỐNG KÊ DASHBOARD ==========
    public double getTodayRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as revenue " + 
                     "FROM booking " +
                     "WHERE DATE(created_at) = CURDATE() AND status = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, Constant.BOOKING_SUCCESS);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total_revenue " + 
                     "FROM booking WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, Constant.BOOKING_SUCCESS);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayBookingsCount() {
        String sql = "SELECT COUNT(*) as count FROM booking WHERE DATE(created_at) = CURDATE()";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBookingCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM booking WHERE status = ? AND DATE(created_at) = CURDATE()";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Booking> getTodayBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE DATE(created_at) = CURDATE() ORDER BY created_at DESC LIMIT 10";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setShowtimeId(rs.getInt("showtime_id"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setStatus(rs.getString("status"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class SeatPriceDTO {
        int seatId;
        double price;

        SeatPriceDTO(int seatId, double price) {
            this.seatId = seatId;
            this.price = price;
        }
    }
}