package example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {
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
}
