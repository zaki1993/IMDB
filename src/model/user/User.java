package model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import model.movie.Movie;
import model.post.Post;

public class User implements IUser{
	private String name;
	private byte age;
	private String location;
	private HashSet<Movie> watchList;
	private ArrayList<String> voted;
	
	private User(String name, byte age, String location) {//don't want ppl able to create users
		this.name = name;
		this.age = age;
		this.location = location;
		this.watchList = new HashSet<>();
		this.voted = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public byte getAge(){
		return this.age;
	}
	
	public HashSet<Movie> getWatchList(){
		return (HashSet<Movie>) Collections.unmodifiableCollection(this.watchList);
	}
	
	public static User register(String name, byte age, String location) {
		User newUser = new User(name, age, location);
		return newUser;
	}
	
	@Override
	public boolean vote(Movie toRate, int vote) {
		if (voted.contains(toRate.getName())) {
			System.out.println("Already voted for that movie!");
			return false;
		}
		voted.add(toRate.getName());
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