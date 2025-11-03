package example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import example.model.Movie;

public class MovieDAO implements DAOInterface<Movie> {

	public static MovieDAO getInstance() {
		return new MovieDAO();
	}

	@Override
	public void add(Movie t) {
	try {
		// Buoc 1: tao ket noi den CSDL
		String url = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
		String user = "root";
		String password = "123456"; 

		Connection conn = JDBCUtil.getConnection();
		
		// Buoc 2: tao ra doi tuong statement
		Statement st = conn.createStatement();
		
		// Buoc 3: thuc thi cau lenh SQL
		String sql = "INSERT INTO test (id, title, genre, duration, country, content, "
		        + "description_id, poster_url, rating, age_warning, video_url) "
		        + "VALUES (" 
		        + t.getId() + ", "
		        + "'" + t.getTitle() + "', "
		        + "'" + t.getGenre() + "', "
		        + t.getDuration() + ", "
		        + "'" + t.getCountry() + "', "
		        + "'" + t.getContent() + "', "
		        + t.getDescriptionId() + ", "
		        + "'" + t.getPosterUrl() + "', "
		        + t.getRating() + ", "
		        + "'" + t.getAgeWarning() + "', "
		        + "'" + t.getVideoUrl() + "'"
		        + ")";
		System.out.println(sql);	
		int result = st.executeUpdate(sql);
		
		// Buoc 4: 
		System.out.println("Ban da thuc thi: "+sql);
		System.out.println("Co "+result+" dong bi thay doi");

		// Buoc 5:
		JDBCUtil.closeConnection(conn);
	}
	catch (SQLException e) {
		e.printStackTrace();
	}

	}

	@Override
	public void update(Movie t) {
		try {
			// Buoc 1: tao ket noi den CSDL
			String url = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
			String user = "root"; 
			String password = "123456"; 

			Connection conn = JDBCUtil.getConnection();
			
			// Buoc 2: tao ra doi tuong statement
			Statement st = conn.createStatement();
			
			// Buoc 3: thuc thi cau lenh SQL
			String sql =  "UPDATE test SET "
			        + "title = '" + t.getTitle() + "', "
			        + "genre = '" + t.getGenre() + "', "
			        + "duration = " + t.getDuration() + ", "
			        + "country = '" + t.getCountry() + "', "
			        + "content = '" + t.getContent() + "', "
			        + "description_id = " + t.getDescriptionId() + ", "
			        + "poster_url = '" + t.getPosterUrl() + "', "
			        + "rating = " + t.getRating() + ", "
			        + "age_warning = '" + t.getAgeWarning() + "', "
			        + "video_url = '" + t.getVideoUrl() + "' "
			        + "WHERE id = " + t.getId();;
			System.out.println(sql);	
			int result = st.executeUpdate(sql);
			
			// Buoc 4: 
			System.out.println("Ban da thuc thi: "+sql);
			System.out.println("Co "+result+" dong bi thay doi");

			// Buoc 5:
			JDBCUtil.closeConnection(conn);
		}	
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Movie t) {
		try {
			// Buoc 1: tao ket noi den CSDL
			String url = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
			String user = "root"; 
			String password = "123456"; 

			Connection conn = JDBCUtil.getConnection();
			
			// Buoc 2: tao ra doi tuong statement
			Statement st = conn.createStatement();
			
			// Buoc 3: thuc thi cau lenh SQL
			String sql = "DELETE FROM test WHERE id = " + t.getId();
			System.out.println(sql);
			int result = st.executeUpdate(sql);
			
			// Buoc 4: 
			System.out.println("Ban da thuc thi: "+sql);
			System.out.println("Co "+result+" dong bi thay doi");

			// Buoc 5:
			JDBCUtil.closeConnection(conn);
		}	
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Movie> selectAll() {
		ArrayList<Movie> result = new ArrayList<>();
		try {
			// Buoc 1: tao ket noi den CSDL
			String url = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
			String user = "root"; 
			String password = "123456"; 

			Connection conn = JDBCUtil.getConnection();
			
			// Buoc 2: tao ra doi tuong statement
			Statement st = conn.createStatement();
			
			// Buoc 3: thuc thi cau lenh SQL
			String sql = "SELECT * FROM test";
			ResultSet rs = st.executeQuery(sql);
			
			// Buoc 4: 
			while (rs.next()) {
			    int id = rs.getInt("id");
			    String title = rs.getString("title");
			    String genre = rs.getString("genre");
			    int duration = rs.getInt("duration");
			    String country = rs.getString("country");
			    String content = rs.getString("content");
			    int descriptionId = rs.getInt("description_id");
			    String posterUrl = rs.getString("poster_url");
			    int rating = rs.getInt("rating");
			    String ageWarning = rs.getString("age_warning");
			    String videoUrl = rs.getString("video_url");

			    Movie movie = new Movie(
			        id, title, genre, duration, country, content,
			        descriptionId, posterUrl, rating, ageWarning, videoUrl
			    );

			    result.add(movie);
			}

			// Buoc 5:
			JDBCUtil.closeConnection(conn);
		}	
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}