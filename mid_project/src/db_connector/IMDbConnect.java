package db_connector;

import java.sql.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

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
		} catch (MySQLIntegrityConstraintViolationException e){
			System.out.println("Veche ima user s takova ime!");
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		}
	}
}
