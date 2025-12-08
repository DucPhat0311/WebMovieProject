package example.model;

import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int userId;
    private int showtimeId;
    private double totalAmount;
    private String status;
    private Timestamp createdAt;
    
    // Constructor mặc định
    public Booking() {}
    
    // Constructor đầy đủ
    public Booking(int bookingId, int userId, int showtimeId, double totalAmount, String status, Timestamp createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    // Constructor không có bookingId (dùng khi insert)
    public Booking(int userId, int showtimeId, double totalAmount, String status) {
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}