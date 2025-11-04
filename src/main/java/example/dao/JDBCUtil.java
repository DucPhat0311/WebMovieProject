package example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root"; 
	private static final String PASSWORD = "123456"; 

	public static Connection getConnection() {
		Connection c = null;
		try {
			// Dang ky MySQL Driver voi DriverManager
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());

			// Cac thong so
			String URL = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
			String USER = "root"; 
			String PASSWORD = "123456"; 
			
			// Tao ket noi 
			c = DriverManager.getConnection(URL,USER,PASSWORD);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static void closeConnection(Connection c) {
		try {
			if(c!=null) {
				c.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
