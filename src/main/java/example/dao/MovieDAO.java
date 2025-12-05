package example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import example.model.Movie;

public class MovieDAO {

    public List<Movie> getNowShowingMovies() {
        List<Movie> list = new ArrayList<>();
        
        String sql = "SELECT * FROM Movie WHERE release_date <= CURDATE() AND is_active = TRUE ORDER BY release_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie m = new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getDate("release_date"),
                    rs.getString("age_warning"),
                    rs.getString("poster_url"),
                    rs.getString("trailer_url"),
                    rs.getBoolean("is_active")
                );
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

  
    public List<Movie> getComingSoonMovies() {
        List<Movie> list = new ArrayList<>();
        
        String sql = "SELECT * FROM Movie WHERE release_date > CURDATE() AND is_active = TRUE ORDER BY release_date ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie m = new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getDate("release_date"),
                    rs.getString("age_warning"),
                    rs.getString("poster_url"),
                    rs.getString("trailer_url"),
                    rs.getBoolean("is_active")
                );
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
 
    public Movie getMovieById(int id) {
        String sql = "SELECT * FROM Movie WHERE movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getDate("release_date"),
                    rs.getString("age_warning"),
                    rs.getString("poster_url"),
                    rs.getString("trailer_url"),
                    rs.getBoolean("is_active")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}