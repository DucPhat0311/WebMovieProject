package example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import example.model.Artist;
import example.model.Genre;
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
        Movie movie = null; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                movie = new Movie(
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
                movie.setGenres(getGenresByMovieId(id));
                movie.setActors(getActorsByMovieId(id)); 
                movie.setDirectors(getDirectorsByMovieId(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }
    
    public List<Genre> getGenresByMovieId(int movieId) {
        List<Genre> list = new ArrayList<>();

        String sql = "SELECT g.* FROM Genre g " +
                     "JOIN MovieGenre mg ON g.genre_id = mg.genre_id " +
                     "WHERE mg.movie_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Genre g = new Genre(
                    rs.getInt("genre_id"),
                    rs.getString("name")
                );
                list.add(g);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }
    
    public List<Artist> getActorsByMovieId(int movieId) {
        List<Artist> list = new ArrayList<>();
        
        // Join bảng Artist với bảng trung gian Actor
        String sql = "SELECT a.* FROM Artist a " +
                     "JOIN Actor ac ON a.artist_id = ac.artist_id " +
                     "WHERE ac.movie_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Artist artist = new Artist(
                    rs.getInt("artist_id"),
                    rs.getString("name")
                );
                list.add(artist);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Artist> getDirectorsByMovieId(int movieId) {
        List<Artist> list = new ArrayList<>();
        
        // Join bảng Artist với bảng trung gian Director
        String sql = "SELECT a.* FROM Artist a " +
                     "JOIN Director d ON a.artist_id = d.artist_id " +
                     "WHERE d.movie_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Artist artist = new Artist(
                    rs.getInt("artist_id"),
                    rs.getString("name")
                );
                list.add(artist);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}