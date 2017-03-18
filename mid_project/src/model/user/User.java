package model.user;

import java.beans.Statement;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import DataBase.Request;
import db_connector.IMDbConnect;
import exceptions.InvalidMovieException;
import exceptions.InvalidUserException;
import exceptions.UserNotFoundException;
import model.movie.Actor;
import model.movie.Director;
import model.movie.Movie;
import model.post.Post;

public class User implements IUser{
	public static enum role { ADMIN, USER };
	private String name;
	private byte age;
	private String location;
	private HashSet<Movie> watchList;
	private ArrayList<String> ratedList;
	private role status;
	private long id;
	
	// we need this constructor, because only the ADMIN can create users with roles
	// make the constructor protected so we can call it only from the classes that inherit this class
	private User(String name, byte age, String location, long id, role status) throws InvalidUserException{
		if(name == null || name.isEmpty() || location == null || location.isEmpty() || age < 0){
			throw new InvalidUserException();
		}
		this.name = name;
		this.age = age;
		this.location = location;
		this.watchList = new HashSet<>();
		this.ratedList = new ArrayList<>();
		this.status = status;
		this.id = id;
	}
	
	private User(String name, byte age, String location, long id) throws InvalidUserException{
		this(name, age, location, id, role.USER);
	}
	
	public String getStatus(){
		return status.toString();
	}
	
	public synchronized static User login(String username, String password) throws InvalidUserException, UserNotFoundException {
		IMDbConnect imdb = IMDbConnect.getInstance();
		try{
			String query = "SELECT id, name, password, age, location, status_id FROM IMDb_user WHERE name = ? and password = ?";
			PreparedStatement st = imdb.getInstance().getConnection().prepareStatement(query);
			st.setString(1, username);
			st.setString(2, password);
			ResultSet rs =  st.executeQuery();
			String uName = "", uPass = "", uLoc = "";
			int uAge = 0, uStatus = 0;
			long uId = 0l;
			if(rs.next()){
				uId = rs.getLong("id");
				uName = rs.getString("name");
				uPass = rs.getString("password");
				uAge = rs.getInt("age");
				uLoc = rs.getString("location");
				uStatus = rs.getInt("status_id");
				User newUser = new User(username, (byte)uAge, uLoc, uId, uStatus == 1 ? role.ADMIN : role.USER);
				return newUser;
			}
			throw new InvalidUserException();
			
		} catch(SQLException e){
			// todo probabbly redirect somewhere
		}
		return null;
	}

	public synchronized static void register(String name, String pass, byte age, String location) throws InvalidUserException{
		if(name == null || name.isEmpty() || pass == null || pass.isEmpty() || location == null || location.isEmpty() || age <= 0){
			throw new InvalidUserException();
		}
		IMDbConnect imdb = IMDbConnect.getInstance();
		try {
			PreparedStatement stmt = imdb.getConnection().prepareStatement("INSERT INTO `IMDb_user`(`name`, `password`, `age`, `location`, `Status_id`) VALUES (?, ?, ?, ?, ?)");
			stmt.setString(1, name);
			stmt.setString(2, pass);
			stmt.setInt(3, age);
			stmt.setString(4, location);
			stmt.setInt(5, 2);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Ima nqkakva greshka pri sql sintaksisa!");
		} 
	}
		
	public static synchronized void createPost(Movie movie){
		Post post = new Post(movie);
		// TODO
	}
	
	private static String customGsonParser(String json, String name){
		String temp = json.substring(json.indexOf(name) + name.length() + 3);
		return temp.substring(0, temp.indexOf("\""));
	}
	
	public static synchronized void addMovie(String name) {		
		String[] names = name.split(" ");
		StringBuilder link = new StringBuilder("http://www.omdbapi.com/?t=");
		for (int i = 0; i < names.length; i++) {
			if(i == 0){
				link.append(names[i]);
				continue;
			}
			link.append("+");
			link.append(names[i]);
		}
		try {
			String movieJson = Request.read(link.toString());
			
			System.out.println(movieJson);
			
			String rs = movieJson.substring(movieJson.indexOf("\"Response\":\""));			
			if(rs.contains("False")){
				throw new InvalidMovieException();
			}
			else{
				IMDbConnect imdb = IMDbConnect.getInstance();
				PreparedStatement stmt;
				// add the director
				String director = User.customGsonParser(movieJson, "Director");
					try {
						stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_director`(`name`) VALUES (?)");
						stmt.setString(1, director);
						stmt.executeUpdate();
					} catch (SQLException ex) {
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Director: " + ex);
					}
				// add all the actors
				String[] actors = User.customGsonParser(movieJson, "Actors").split(", ");
				for(int i = 0; i < actors.length; i++){
					try{
						stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_actor`(`name`) VALUES (?)");
						stmt.setString(1, actors[i]);
						stmt.executeUpdate();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Actor: " + ex);
					}
				}
				// add the movie
				String movie = User.customGsonParser(movieJson, "Title");
				String poster = User.customGsonParser(movieJson, "Poster");
				String rating = User.customGsonParser(movieJson, "imdbRating");
				String description = User.customGsonParser(movieJson, "Plot");
				String date = User.customGsonParser(movieJson, "Released");
				if(rating.equals("N/A")){
					rating = "0";
				}
				try{
					stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_movie`(`poster`, `rating`, `description`, `date`, `name`) VALUES (?, ?, ?, ?, ?)");
					stmt.setString(1, poster);
					stmt.setDouble(2, Double.parseDouble(rating));
					stmt.setString(3, description);
					stmt.setString(4, date);
					stmt.setString(5, movie);
					stmt.executeUpdate();
				} catch(SQLException ex){
					// dublicate fields
					// nothing to do
					// we set them to be unique
					System.out.println("Movie: " + ex);
				}
				// add genres
				String genre = User.customGsonParser(movieJson, "Genre");
				String[] genres = genre.split(", ");
				for(int i = 0; i < genres.length; i++){
					try{
						stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_genre`(`name`) VALUES (?)");
						stmt.setString(1, genres[i]);
						stmt.executeUpdate();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Genre: " + ex);
					}
				}
				
				// get movie id

				int movie_id = 0;
				try{
					// todo fix this
					
					stmt = imdb.getInstance().getConnection().prepareStatement("SELECT id FROM IMDb_movie WHERE name = ?");
					stmt.setString(1, movie);
					ResultSet rs1 = stmt.executeQuery();
					rs1.next();
					movie_id = rs1.getInt("id");
				} catch(SQLException ex){
					// dublicate fields
					// nothing to do
					// we set them to be unique
					System.out.println("Movie id table: " + ex);
				}
				
				// fill genre movie table
				for(int i = 0; i < genres.length; i++){
					try{					
						stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_genre_movie` (`genre_name`, `movie_id`) VALUES (?, ?)");
						stmt.setString(1, genres[i]);
						stmt.setInt(2, movie_id);
						stmt.executeUpdate();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Genre_Movie: " + ex);
					}
				}
				
				// fill movie director table
				int director_id = 0;
				try{
					// todo fix this
					
					stmt = imdb.getInstance().getConnection().prepareStatement("SELECT id FROM IMDb_director WHERE name = ?");
					stmt.setString(1, director);
					ResultSet rs1 = stmt.executeQuery();
					rs1.next();
					director_id = rs1.getInt("id");
				} catch(SQLException ex){
					// dublicate fields
					// nothing to do
					// we set them to be unique
					System.out.println("Movie id table: " + ex);
				}
				
				try{
					stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_director_movie` (`director_id`, `movie_id`) VALUES (?, ?)");
					stmt.setInt(1, director_id);
					stmt.setInt(2, movie_id);
					stmt.executeUpdate();
				} catch(SQLException ex){
					// dublicate fields
					// nothing to do
					// we set them to be unique
					System.out.println("Director_Movie: " + ex);
				}
				
				// fill movie actor table
				for(int i = 0; i < actors.length; i++){
					int actor_id = 0;
					try{
						stmt = imdb.getInstance().getConnection().prepareStatement("SELECT id FROM IMDb_actor WHERE name = ?");
						stmt.setString(1, actors[i]);
						ResultSet rs1 = stmt.executeQuery();
						rs1.next();
						actor_id = rs1.getInt("id");
						stmt = imdb.getInstance().getConnection().prepareStatement("INSERT IGNORE INTO `IMDb_actor_movie` (`actor_id`, `movie_id`) VALUES (?, ?)");
						stmt.setInt(1, actor_id);
						stmt.setInt(2, movie_id);
						stmt.executeUpdate();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Actor_Movie: " + ex);
					}
					
				}
			}
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		} catch (InvalidMovieException e){
			System.out.println("Ne e nameren film, trqbwa da go oprawim!");
		}
	}
	
	public String getName() {
		return name;
	}
	
	public byte getAge(){
		return this.age;
	}
	
	@Override
	public boolean vote(Movie toRate, int vote) {
		if (ratedList.contains(toRate.getName())) {
			System.out.println("Already voted for that movie!");
			return false;
		}
		ratedList.add(toRate.getName());
		return (toRate == null) ? false : toRate.rate(vote);
	}
	
	@Override
	public boolean comment(Post post, String msg) {
		if (post == null) {
			System.out.println("No such post!");
			return false;
		}
		if(msg == null || msg.isEmpty()){
			System.out.println("No valid comment!");
			return false;
		}
		post.addComment(this, msg);
		return true;
	}
	
	@Override
	public boolean addToWatchList(Movie toAdd) {
		if(toAdd == null){
			System.out.println("No such movie!");
			return false;
		}
		else if(watchList.contains(toAdd))
		{
			System.out.println("Already added!");
			return false;
		}
		watchList.add(toAdd);
		return true;
	}

	@Override
	public String toString() {
		String str = "name: " + name + "\nage: " + age + "\nlocation: " + location + "\nwatchList: ";
		for (Iterator iterator = watchList.iterator(); iterator.hasNext();) {
			Movie m = (Movie) iterator.next();
			if (!iterator.hasNext()) {
				str += m.getName();
			} else {
				str = str + m.getName() + ", ";
			}
		}
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
