package Model.Movie;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

public class Movie {
	
	private static int id = 0;
	private String name;
	private String poster;
	private String genre;
	private double rating;
	private HashSet<Actor> actors;
	private HashSet<Director> directors;
	private String description;
	private LocalDate date;
	private int idx;
	
	public Movie(String name, String poster, String genre, double rating, HashSet<Actor> actors,
			HashSet<Director> scenaristi, String description, LocalDate date) {
		super();
		this.name = name;
		this.poster = poster;
		this.genre = genre;
		this.rating = rating;
		this.actors = actors;
		this.directors = scenaristi;
		this.description = description;
		this.date = date;
		this.idx = Movie.id++;
	}

	public String getName() {
		return name;
	}

	public String getPoster() {
		return poster;
	}

	public String getGenre() {
		return genre;
	}

	public double getRating() {
		return rating;
	}

	public HashSet<Actor> getActors() {
		return (HashSet<Actor>) Collections.unmodifiableCollection(this.actors);
	}

	public HashSet<Director> getDirectors() {
		return (HashSet<Director>) Collections.unmodifiableCollection(this.directors);
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getDate() {
		return date;
	}
}
