package model.movie;

import java.util.HashSet;

public class Actor extends Artist{
	
	public Actor(String name){
		super(name, (byte) 0);
	}
	
	public Actor(String name, byte age){
		super(name, age);
	}

	public Actor(String name, byte age, HashSet<Movie> movies) {
		super(name, age, movies);
	}
	
}
