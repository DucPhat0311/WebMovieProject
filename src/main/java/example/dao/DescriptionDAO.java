package example.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import example.model.Description;

public class DescriptionDAO implements DAOInterface<Description> {
	
	public static DescriptionDAO getInstance() {
		return new DescriptionDAO();
	}

	@Override
	public void add(Description t) {
	try {
		// Buoc 1: tao ket noi den CSDL
		String url = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
		String user = "root";
		String password = "123456"; 

		Connection conn = DBConnection.getConnection();
		
		// Buoc 2: tao ra doi tuong statement
		Statement st = conn.createStatement();
		
		// Buoc 3: thuc thi cau lenh SQL
		  String sql = "INSERT INTO descriptions (producer, director, actor, release_date) VALUES ("
	                + "'" + t.getProducer() + "', "
	                + "'" + new org.json.JSONArray(t.getDirector()).toString() + "', "
	                + "'" + new org.json.JSONArray(t.getActor()).toString() + "', "
	                + "'" + t.getReleaseDate() + "'"
	                + ")";

		System.out.println(sql);	
		int result = st.executeUpdate(sql);
		
		// Buoc 4: 
		System.out.println("Ban da thuc thi: "+sql);
		System.out.println("Co "+result+" dong bi thay doi");

		// Buoc 5:
		DBConnection.closeConnection(conn);
	}
	catch (SQLException e) {
		e.printStackTrace();
	}

	}

	@Override
	public void update(Description t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Description t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Description> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
