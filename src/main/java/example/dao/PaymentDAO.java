package example.dao;

import example.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO payment (booking_id, method, status, amount, paid_at) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, payment.getBookingId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getStatus());
            ps.setDouble(4, payment.getAmount());
            
            if (payment.getPaymentDate() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            } else {
                ps.setTimestamp(5, null);
            }
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT * FROM payment WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setBookingId(rs.getInt("booking_id"));
                payment.setPaymentMethod(rs.getString("method"));
                payment.setStatus(rs.getString("status"));
                payment.setAmount(rs.getDouble("amount"));
                
                Timestamp paidAt = rs.getTimestamp("paid_at");
                if (paidAt != null) {
                    payment.setPaymentDate(paidAt.toLocalDateTime());
                }
                
                return payment;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updatePaymentStatus(int bookingId, String status) {
        String sql = "UPDATE payment SET status = ?, paid_at = ? WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            
            if ("Success".equals(status)) {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setTimestamp(2, null);
            }
            
            ps.setInt(3, bookingId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Payment> getPaymentsByUserId(int userId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.* FROM payment p " +
                    "JOIN booking b ON p.booking_id = b.booking_id " +
                    "WHERE b.user_id = ? ORDER BY p.paid_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setBookingId(rs.getInt("booking_id"));
                payment.setPaymentMethod(rs.getString("method"));
                payment.setStatus(rs.getString("status"));
                payment.setAmount(rs.getDouble("amount"));
                
                Timestamp paidAt = rs.getTimestamp("paid_at");
                if (paidAt != null) {
                    payment.setPaymentDate(paidAt.toLocalDateTime());
                }
                
                payments.add(payment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
}