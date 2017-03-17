package db_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import model.user.User;

public class IMDbConnect {
	
	private static IMDbConnect imdb = null;
	private Connection con = null;

	public static Hashtable<String, User> loggedUsers = new Hashtable<>(); // hashtable<sessionID, user>
	
	private IMDbConnect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IMDb?autoReconnect=true&useSSL=false", "root", "injikipliok");
			//jdbc:mysql://localhost:3306/Peoples?autoReconnect=true&useSSL=false
		}catch(Exception ex){
			System.out.println(ex.getMessage());
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

}