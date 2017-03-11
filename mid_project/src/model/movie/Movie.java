package model.movie;

import java.time.LocalDate;

import java.util.Collections;
import java.util.HashSet;

import exceptions.InvalidMovieException;

public class Movie {
	
	private static int id = 0;
	private String name;
	private String poster;
	private HashSet<String> genre; //can have more then 1
	private int voters;
	private double rating;
	private HashSet<Actor> actors;
	private HashSet<Director> directors;
	private String description;
	private LocalDate date;
	private int idx;
	
	public Movie(String name, String poster, String[] genres, HashSet<Actor> actors,
			HashSet<Director> scenaristi, String description, LocalDate date) throws InvalidMovieException {
		if(name.isEmpty() || poster.isEmpty() || genres == null || actors == null || scenaristi == null || date == null){
			throw new InvalidMovieException();
		}
		this.name = name;
		this.poster = poster;
		this.genre = new HashSet<>();
		for (String genre : genres) {
			this.genre.add(genre);
		}
		this.rating = 0;
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

	public HashSet<String> getGenre() {
		return (HashSet<String>) Collections.unmodifiableCollection(this.genre);
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

	public boolean rate(int vote) {
		if (vote < 0 || vote > 10) {
			return false;
		} 
		double ratingAvr = this.rating * this.voters;
		ratingAvr += vote;
		this.voters++;
		rating = ratingAvr / voters;
		return true;
	}
	


	@Override
	public String toString() {
		return "name: " + name + "\nposter: " + poster + "\ngenre: " + genre + "\nrating: " + rating + "\nactors: "
				+ actors + "\ndirectors: " + directors + "\ndescription: " + description + "\ndate: " + date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Movie other = (Movie) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
