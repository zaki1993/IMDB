package db_connector;

import java.sql.*;

public class IMDbConnect {
	private static IMDbConnect imdb;
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	private IMDbConnect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IMDb", "root", "14eiuqhwdyeuq");
			st = con.createStatement();
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	public synchronized Connection getConnection(){
		return this.con;
	}
	
	public synchronized static IMDbConnect getInstance(){
		if(IMDbConnect.imdb == null){
			IMDbConnect.imdb = new IMDbConnect();
		}
		return IMDbConnect.imdb;
	}
	
	public synchronized void insertData(PreparedStatement stmt){
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	public void getData(){
		try{
			String query = "select * from IMDb_movie";
			rs = st.executeQuery(query);
			ResultSetMetaData mt = rs.getMetaData();
			int number = mt.getColumnCount();
			for(int i = 1; i <= number; i++){
				System.out.print(" | " + mt.getColumnName(i) + " | ");
			}
			System.out.println();
			while(rs.next()){
				System.out.print(rs.getString("id") + " ");
				System.out.print(rs.getString("poster") + " ");
				System.out.print(rs.getString("rating") + " ");
				System.out.print(rs.getString("description") + " ");
				System.out.print(rs.getString("date") + " ");
				System.out.print(rs.getString("name") + " ");
			}
			System.out.println();
		}catch(Exception ex){
			System.out.println(ex);
		}
		finally{
			try {
				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
