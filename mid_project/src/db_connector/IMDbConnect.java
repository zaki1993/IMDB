package db_connector;

import java.sql.*;

public class IMDbConnect {
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	public IMDbConnect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IMDb", "root", "14eiuqhwdyeuq");
			st = con.createStatement();
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	public void getData(){
		try{
			String query = "select * from IMDb_movie";
			rs = st.executeQuery(query);
			while(rs.next()){
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("poster"));
				System.out.println(rs.getString("rating"));
				System.out.println(rs.getString("description"));
				System.out.println(rs.getString("date"));
				System.out.println(rs.getString("name"));
			}
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
}
