package model.movie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import model.dao.MovieDAO;
import model.exceptions.InvalidMovieException;

public class Movie {
	
//	private static int id = 0; LOOK IN CONSTRUCTOR
	private String name;
	private String poster;
	private HashSet<String> genre; //can have more then 1
	private int voters = 100;
	private double rating;
	private HashSet<Actor> actors;
	private HashSet<Director> directors;
	private String description;
	private String date;
	private long id;
	
	public Movie(String name, String poster, ArrayList<String> genres, HashSet<Actor> actors,
			HashSet<Director> directors, String description, String date, double rating) throws InvalidMovieException {
		if(name == null || name.isEmpty() || poster == null || poster.isEmpty() || genres == null || date == null){
			throw new InvalidMovieException();
		}
		this.name = name;
		this.poster = poster;
		this.genre = new HashSet<>();
		for (String genre : genres) {
			this.genre.add(genre);
		}
		this.rating = rating;
		this.actors = actors;
		this.directors = directors;
		this.description = description;
		this.date = date;
		//this.idx = Movie.id++;  we want the generated from mySQL id
		// TODO for actors and directors to push movies 
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getPoster() {
		return poster;
	}

	public HashSet<String> getGenre() {
		return this.genre;
	}

	public double getRating() {
		return rating;
	}

	public HashSet<Actor> getActors() {
		return this.actors;
	}

	public HashSet<Director> getDirectors() {
		return this.directors;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
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
		MovieDAO.getInstance().updateRating(this.name, this.rating);
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
