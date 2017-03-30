package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import model.exceptions.InvalidUserException;
import model.movie.Movie;
import model.user.User;
import model.user.User.role;

public class UserDAO{
	private static UserDAO instance = null;
	private static final HashMap<String, User> allUsers = new HashMap<>();
	private UserDAO(){
	}
	public static synchronized UserDAO getInstance(){
		if(instance == null){
			instance = new UserDAO();
			if(allUsers.isEmpty()){
				String query = "SELECT id, name, password, age, location FROM imdb_user";
				try {
					PreparedStatement stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
					ResultSet rs = stmt.executeQuery();
					while(rs.next()){
						User newUser = null;
						String name = rs.getString("name");
						byte age = (byte) rs.getInt("age");
						String location = rs.getString("location");
						String password = rs.getString("password");
						long id = rs.getLong("id");
						String movieName = null;
						HashSet<Movie> movies = new HashSet<>();
						//Get movies
						try{
							query = "SELECT M.name, UM.Movie_id FROM imdb.imdb_user_movie UM JOIN imdb.imdb_movie M ON Movie_id = id WHERE User_id = ?";
							stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
							stmt.setLong(1, id);
							stmt.executeQuery();
							ResultSet rSet = stmt.getResultSet();
							while(rSet.next()){
								movieName = rSet.getString("name");
								Movie m = MovieDAO.getInstance().allMovies().get(movieName);
								movies.add(m);
							}
						} catch(SQLException ex){
							System.out.println("PostDAO -> get Post ID: " + ex);
						}
						try {
							newUser = new User(name, age, location, password);
							newUser.setId(id);
							for (Movie movie : movies) {
								newUser.addToWatchList(movie);
							}
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
	
	public synchronized boolean addUser(User toAdd){
		if(allUsers.containsKey(toAdd.getName())){
			return false;
		}
		long id = 0;
		// add to db
		try {
			String query = "INSERT INTO `imdb_user`(`name`, `password`, `age`, `location`, `Status_id`) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, toAdd.getName());
			stmt.setString(2, toAdd.getPassword());
			stmt.setInt(3, toAdd.getAge());
			stmt.setString(4, toAdd.getLocation());
			stmt.setInt(5, 2);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getLong(1);
		} catch (SQLException e) {
			System.out.println("UserDao: " + e.getMessage());
			return false;
		} 
		toAdd.setId(id);
		allUsers.put(toAdd.getName(), toAdd);
		return true;
	}
	
	public Map<String, User> getAllUsers(){
		return Collections.unmodifiableMap(allUsers);
	}
	
	public boolean validLogin(String username, String passord) throws InvalidUserException{
		if(getInstance().getAllUsers().containsKey(username)){
			return getAllUsers().get(username).getPassword().equals(passord);
		}
		return false;
	}
	
	public void addMovieToUser(User user, String movie){
		String query = "INSERT IGNORE INTO imdb_user_movie(User_id, Movie_id) VALUES(?, ?)";
		PreparedStatement stmt = null;
		Movie m = MovieDAO.getInstance().allMovies().get(movie);
		user.addToWatchList(m);
		try {
			stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, user.getId());
			stmt.setLong(2, MovieDAO.getInstance().allMovies().get(movie).getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("addMovieToUser: " + e.getMessage());
		}
	}
}