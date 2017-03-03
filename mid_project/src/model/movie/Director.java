package model.movie;

import java.util.HashSet;

public class Director extends Artist{

	public Director(String name, byte age, HashSet<Movie> movies) {
		super(name, age, movies);
	}

}
