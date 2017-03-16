package db_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IMDbConnect {
	private static IMDbConnect imdb;
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	private IMDbConnect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IMDb?autoReconnect=true&useSSL=false", "streetzaki", "14eiuqhwdyeuQ*");
			//jdbc:mysql://localhost:3306/Peoples?autoReconnect=true&useSSL=false
			st = con.createStatement();
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	public synchronized Connection getConnection(){
		return IMDbConnect.imdb.con;
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
			System.out.println("Ima greshak v zaqvkat aza insert!");
		}
	}
}
