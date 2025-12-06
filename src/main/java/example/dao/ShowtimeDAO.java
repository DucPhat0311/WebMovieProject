package example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import example.model.Showtime;

public class ShowtimeDAO {
	// lấy ngày trong của 1 phim
	public List<Date> getShowDates(int movieId) {
        List<Date> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT show_date FROM Showtime "
                   + "WHERE movie_id = ? AND show_date >= CURDATE() AND is_active = TRUE "
                   + "ORDER BY show_date ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dates.add(rs.getDate("show_date"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return dates;
    }
	
	public List<Showtime> getShowtimesByDate(int movieId, Date date) {
        List<Showtime> list = new ArrayList<>();
        
        String sql = "SELECT s.*, c.cinema_name " 
                + "FROM Showtime s "
                + "JOIN Room r ON s.room_id = r.room_id "
                + "JOIN Cinema c ON r.cinema_id = c.cinema_id "
                + "WHERE s.movie_id = ? "
                + "AND s.show_date = ? "
                + "AND s.is_active = TRUE "
                + "AND TIMESTAMP(s.show_date, s.start_time) > NOW() "
                + "ORDER BY c.cinema_name, s.option_type, s.start_time"; // phai lay cinema name bang cach join 
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, movieId);
            ps.setDate(2, date);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Showtime s = new Showtime(
                    rs.getInt("showtime_id"),
                    rs.getInt("movie_id"),
                    rs.getInt("room_id"),
                    rs.getDate("show_date"),
                    rs.getTime("start_time"),
                    rs.getTime("end_time"),
                    rs.getDouble("base_price"),
                    rs.getString("option_type")
                );
                s.setCinemaName(rs.getString("cinema_name"));
                list.add(s);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
	
}
