package model.user;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.mysql.jdbc.Statement;

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
	
	// we need this constructor, because only the ADMIN can create users with roles
	// make the constructor protected so we can call it only from the classes that inherit this class
	private User(String name, byte age, String location, role status) throws InvalidUserException{
		if(name == null || name.isEmpty() || location == null || location.isEmpty() || age < 0){
			throw new InvalidUserException();
		}
		this.name = name;
		this.age = age;
		this.location = location;
		this.watchList = new HashSet<>();
		this.ratedList = new ArrayList<>();
		this.status = status;
	}
	
	private User(String name, byte age, String location) throws InvalidUserException{
		this(name, age, location, role.USER);
	}
	
	public synchronized static User login(String username, String password) {
		IMDbConnect imdb = IMDbConnect.getInstance();
		try{
			String query = "SELECT * FROM IMDb_user";
			Statement st = (Statement) imdb.getConnection().createStatement();
			ResultSet rs = st.executeQuery(query);
			String uName = "", uPass = "", uLoc = "";
			int uAge = 0, uStatus = 0;
			while(rs.next()){
				uName = rs.getString("name");
				uPass = rs.getString("password");
				uAge = rs.getInt("age");
				uLoc = rs.getString("location");
				uStatus = rs.getInt("Status_id");
				if(username.equals(uName) && password.equals(uPass)){
					// successfully logged in
					return new User(username, (byte)uAge, uLoc, uStatus == 1 ? role.ADMIN : role.USER);
				}
			}
			throw new UserNotFoundException();
			
		}catch(SQLException e){
			// TODO
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Nqma takuv user");
		} catch (InvalidUserException e){
			// TODO
			System.out.println("Nevalidni user danni");
		}
		
		return null;
	}
	

	public synchronized static void register(String name, String pass, byte age, String location){
		try{
			if(name == null || name.isEmpty() || pass == null || pass.isEmpty() || location == null || location.isEmpty()){
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
				imdb.insertData(stmt);
			} catch (SQLException e) {
				System.out.println("Ima nqkakva greshka pri sql sintaksisa!");
			} 
		} catch(InvalidUserException e){
			System.out.println("Nevalidni user danni");
		}
	}

	// promote and demote user helper
	protected void setStatus(User.role status){
		this.status = status;
	}
	
	public void createPost(Movie movie){
		if(this.status == role.USER){
			return;
		}
		Post post = new Post(movie);
		// TODO
	}
	
	public void addMovie(String name) {
		if(this.status == role.USER){
			return;
		}
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
			String asd = Request.read(link.toString());
			System.out.println(asd);
			String rs = asd.substring(asd.indexOf("\"Response\":\""));
			
			Map<String, String> jdata;
			
			
			
			if(rs.contains("False")){
				throw new InvalidMovieException();
			}
			else{
				
			}
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		} catch (InvalidMovieException e){
			System.out.println("Ne e nameren film, trqbwa da go oprawim!");
		}
		
		
//		try {
//			//Movie movie = new Movie(name, poster, genres, actors, scenaristi, description, date);
//			// TODO
//		} catch (InvalidMovieException e) {
//			// TODO
//			e.printStackTrace();
//		}
	}
	
	public void addActor(String name, byte age){
		if(this.status == role.USER){
			return;
		}
		Actor actor = new Actor(name, age);
		// TODO
	}
	
	public void addDirector(String name, byte age){
		if(this.status == role.USER){
			return;
		}
		Director director = new Director(name, age);
		// TODO
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
