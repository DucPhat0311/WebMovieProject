//package example.dao.core;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//public class DBConnection {
//    
//    // Khai báo DataSource (bể chứa kết nối)
//    private static HikariDataSource dataSource;
//
//    static {
//        try {
//            HikariConfig config = new HikariConfig();
//
//            config.setJdbcUrl("jdbc:mysql://mysql-c80eef2-ducphat0311-1c2c.i.aivencloud.com:24156/movie?useSSL=true&trustServerCertificate=true&serverTimezone=UTC&characterEncoding=UTF-8");
//            config.setUsername("avnadmin");
//            
//            
//            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//            config.setMaximumPoolSize(10); 
//            config.setMinimumIdle(2);      
//            config.setIdleTimeout(30000);  
//            config.setConnectionTimeout(20000); 
//
//            config.addDataSourceProperty("cachePrepStmts", "true");
//            config.addDataSourceProperty("prepStmtCacheSize", "250");
//            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//
//            dataSource = new HikariDataSource(config);
//            System.out.println("✅ Đã khởi tạo Connection Pool đến Aiven thành công!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("❌ Lỗi khởi tạo Connection Pool!");
//        }
//    }
//
//    // Hàm lấy kết nối từ Pool
//    public static Connection getConnection() {
//        try {
//            return dataSource.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    
//    // Hàm đóng Pool (thường dùng khi tắt server)
//    public static void closePool() {
//        if (dataSource != null) {
//            dataSource.close();
//        }
//    }
//
//    // Hàm Main để test thử kết nối
//    public static void main(String[] args) {
//        System.out.println("Đang thử kết nối đến Aiven...");
//        Connection conn = getConnection();
//        
//        if (conn != null) {
//            System.out.println("✅ KẾT NỐI THÀNH CÔNG!");
//            System.out.println("Bạn đã vào được database: movie");
//            // Đóng kết nối test (trả về pool)
//            try { conn.close(); } catch (SQLException e) {}
//        } else {
//            System.out.println("❌ KẾT NỐI THẤT BẠI. Hãy kiểm tra lại Console để xem lỗi.");
//        }
//    }
//}


package example.dao.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
	private static final String USER = "root";
	private static final String PASSWORD = "123456";

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection c) {
		try {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
