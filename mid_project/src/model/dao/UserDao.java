package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.exceptions.InvalidUserException;
import model.user.User;
import model.user.User.role;

public class UserDao{
	private static UserDao instance = null;
	private static HashMap<String, User> loggedUsers = new HashMap<>();
	private static final HashMap<String, User> allUsers = new HashMap<>();
	private UserDao(){
	}
	public static synchronized UserDao getInstance(){
		if(instance == null){
			instance = new UserDao();
			if(allUsers.isEmpty()){
				String query = "SELECT name, password, age, location FROM IMDb_user";
				try {
					PreparedStatement stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
					ResultSet rs = stmt.executeQuery();
					while(rs.next()){
						User newUser = null;
						String name = rs.getString("name");
						byte age = (byte) rs.getInt("age");
						String location = rs.getString("location");
						String password = rs.getString("password");
						try {
							newUser = new User(name, age, location, password);
						} catch (InvalidUserException e) {
							System.out.println("Almost sure it wont throw here!");
						}
						allUsers.put(newUser.getName(), newUser);
					}
				} catch (SQLException e) {
					System.out.println("UserDao->AddUser->FillTable: " + e.getMessage());
				}
			}	
		}
		return instance;
	}
	
	public synchronized void addUser(User toAdd){
		// add to db
		System.out.println(getAllUsers().toString());
		try {
			String query = "INSERT INTO `IMDb_user`(`name`, `password`, `age`, `location`, `Status_id`) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
			stmt.setString(1, toAdd.getName());
			stmt.setString(2, toAdd.getPassword());
			stmt.setInt(3, toAdd.getAge());
			stmt.setString(4, toAdd.getLocation());
			stmt.setInt(5, 2);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("UserDao: " + e.getMessage());
		} 
		allUsers.put(toAdd.getName(), toAdd);
	}
	
	public synchronized HashMap<String, User> getAllUsers(){
		return allUsers;
	}
	
	public synchronized boolean validLogin(String username, String passord) throws InvalidUserException{
		if(getInstance().getAllUsers().containsKey(username)){
			return getAllUsers().get(username).getPassword().equals(passord);
		}
		return false;
	}
	
	public synchronized HashMap<String, User> getLoggedUsers(){
		return (HashMap<String, User>) Collections.unmodifiableMap(loggedUsers);
	}
	
}