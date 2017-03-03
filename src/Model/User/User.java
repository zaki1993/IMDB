package Model.User;

import java.util.HashSet;
import Model.Movie.*;
import Model.Post.*;

public class User implements IUser{
	private String name;
	private byte age;
	private String location; // Предлагам за сега да е стринг, а след това може да вземем файл с градове и да избира някой от тях
	private HashSet<Movie> watchList;
	
	public User(String name, byte age, String location) {
		super();
		this.name = name;
		this.age = age;
		this.location = location;
		this.watchList = new HashSet<>();
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean register() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean vote(Movie toRate) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean comment(Post post) {
		// TODO Auto-generated method stub
		return false;
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
