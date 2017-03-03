package Model.Movie;

import java.util.Collections;
import java.util.HashSet;

import com.sun.javafx.UnmodifiableArrayList;

public class Artist {
	private String name;
	private byte age;
	private HashSet<Movie> movies;
	public Artist(String name, byte age, HashSet<Movie> movies) {
		super();
		this.name = name;
		this.age = age;
		this.movies = movies;
	}
	public String getName() {
		return this.name;
	}
	public byte getAge() {
		return this.age;
	}
	public HashSet<Movie> getMovies() {
		return (HashSet<Movie>) Collections.unmodifiableCollection(this.movies);
	}
}
