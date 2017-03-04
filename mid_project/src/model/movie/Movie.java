package model.movie;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

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
			HashSet<Director> scenaristi, String description, LocalDate date) {
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
		String result = "[";
		result += this.name + ", ";
		result += this.poster + ", ";
		if(this.genre != null){
			result += "[";
			for(String i : this.genre){
				result += i + " ";
			}
			result += "], ";
		}
		else{
			result += null + ", ";
		}
		result += this.rating + ", ";
		if(this.actors != null){
			result += ", [";
			for(Actor i : this.actors){
				result += i.toString() + " ";
			}
			result += "], ";
		}
		else{
			result += null + ", ";
		}
		if(this.directors != null){
			result += "[";
			for(Director i : this.directors){
				result += i.toString() + " ";
			}
			result += "], ";
		}
		else{
			result += null + ", ";
		}
		result += this.description + ", ";
		result += this.date.toString();
		result += "]";
		return result;
	}
	
}
