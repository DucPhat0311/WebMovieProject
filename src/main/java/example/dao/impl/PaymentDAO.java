package example.dao.impl;

import java.sql.*;
import example.dao.core.DBConnection;
import example.model.transaction.Payment;
import java.util.Date; 

public class PaymentDAO {
    
    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO payment (booking_id, method, status, amount, paid_at) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, payment.getBookingId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getStatus());
            ps.setDouble(4, payment.getAmount());
            
            if (payment.getPaymentDate() != null) {
                ps.setTimestamp(5, new Timestamp(payment.getPaymentDate().getTime()));
            } else {
                ps.setTimestamp(5, null);
            }
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy payment theo booking_id
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
                    payment.setPaymentDate(new Date(paidAt.getTime()));
                }
                
                return payment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}