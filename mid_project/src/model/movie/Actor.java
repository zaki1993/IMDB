package model.movie;

import java.util.HashSet;

public class Actor extends Artist{

	public Actor(String name, byte age, HashSet<Movie> movies) {
		super(name, age, movies);
	}
	
}
