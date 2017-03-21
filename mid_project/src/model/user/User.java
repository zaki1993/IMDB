package model.user;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import model.dao.IMDbConnect;
import model.dao.Request;
import model.exceptions.InvalidMovieException;
import model.exceptions.InvalidUserException;
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
	private String password;
	
	// we need this constructor, because only the ADMIN can create users with roles
	// make the constructor protected so we can call it only from the classes that inherit this class
	private User(String name, byte age, String location, role status, String password) throws InvalidUserException{
		if(name == null || name.isEmpty() || location == null || location.isEmpty() || age < 0){
			throw new InvalidUserException();
		}
		this.name = name;
		this.age = age;
		this.location = location;
		this.watchList = new HashSet<>();
		this.ratedList = new ArrayList<>();
		this.status = name.equals("admin") && password.equals("admin") ? role.ADMIN : status;
		this.password = password;
	}
	
	public User(String name, byte age, String location, String password) throws InvalidUserException{
		this(name, age, location, role.USER, password);
	}
	
	public static synchronized void createPost(Movie movie){
		Post post = new Post(movie);
		// TODO
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public String getStatus(){
		return status.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public byte getAge(){
		return this.age;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	@Override
	public synchronized boolean vote(Movie toRate, int vote) {
		if (ratedList.contains(toRate.getName())) {
			System.out.println("Already voted for that movie!");
			return false;
		}
		ratedList.add(toRate.getName());
		return (toRate == null) ? false : toRate.rate(vote);
	}
	
	@Override
	public synchronized boolean comment(Post post, String msg) {
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
	public synchronized boolean addToWatchList(Movie toAdd) {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((ratedList == null) ? 0 : ratedList.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((watchList == null) ? 0 : watchList.hashCode());
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
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (ratedList == null) {
			if (other.ratedList != null)
				return false;
		} else if (!ratedList.equals(other.ratedList))
			return false;
		if (status != other.status)
			return false;
		if (watchList == null) {
			if (other.watchList != null)
				return false;
		} else if (!watchList.equals(other.watchList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", location=" + location + ", watchList=" + watchList
				+ ", ratedList=" + ratedList + ", status=" + status + ", password=" + password + "]";
	}
	
	
	
}
