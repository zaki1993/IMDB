package model.user;

import java.util.HashSet;

import model.movie.Movie;
import model.post.Post;

public class User implements IUser{
	private String name;
	private byte age;
	private String location;
	private HashSet<Movie> watchList;
	
	private User(String name, byte age, String location) {//don't want ppl able to create users
		this.name = name;
		this.age = age;
		this.location = location;
		this.watchList = new HashSet<>();
	}
	
	public String getName() {
		return name;
	}
	
	public static User register(String name, byte age, String location) {
		User newUser = new User(name, age, location);
		return newUser;
	}
	
	@Override
	public boolean vote(Movie toRate, int vote) {
		if (toRate == null || (vote < 0 || vote > 10)) {
			
		}
		toRate.rate(vote);
		return true;
	}
	
	@Override
	public boolean comment(Post post, String msg) {
		if (post == null) {
			System.out.println("No such post!");
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
}
