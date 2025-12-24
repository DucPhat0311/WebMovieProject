package example.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import example.dao.core.DBConnection;
import example.model.cinema.Seat;
import example.model.cinema.SeatType;

public class SeatDAO {

    public List<Seat> getSeatsByRoomId(int roomId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.* FROM seat s WHERE s.room_id = ? ORDER BY s.seat_row, s.seat_number";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Seat seat = new Seat(rs.getInt("seat_id"), 
                                   rs.getInt("room_id"), 
                                   rs.getInt("seat_type_id"),
                                   rs.getString("seat_row"), 
                                   rs.getInt("seat_number"));
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public Seat getSeatById(int seatId) {
        Seat seat = null;
        String sql = "SELECT * FROM seat WHERE seat_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                seat = new Seat(rs.getInt("seat_id"), 
                              rs.getInt("room_id"), 
                              rs.getInt("seat_type_id"),
                              rs.getString("seat_row"), 
                              rs.getInt("seat_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seat;
    }

    public Seat getSeatByCode(String seatCode, int roomId) {
        Seat seat = null;
        String row = seatCode.substring(0, 1);
        int number = Integer.parseInt(seatCode.substring(1));
        
        String sql = "SELECT * FROM seat WHERE seat_row = ? AND seat_number = ? AND room_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, row);
            ps.setInt(2, number);
            ps.setInt(3, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                seat = new Seat(rs.getInt("seat_id"), 
                              rs.getInt("room_id"), 
                              rs.getInt("seat_type_id"),
                              rs.getString("seat_row"), 
                              rs.getInt("seat_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seat;
    }

    public SeatType getSeatTypeById(int seatTypeId) {
        SeatType seatType = null;
        String sql = "SELECT * FROM seattype WHERE seat_type_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                seatType = new SeatType(rs.getInt("seat_type_id"), 
                                      rs.getString("name"), 
                                      rs.getDouble("surcharge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatType;
    }
}