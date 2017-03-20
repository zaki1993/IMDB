package model.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import model.exceptions.InvalidMovieException;
import model.movie.Actor;
import model.movie.Director;
import model.movie.Movie;

public class MovieDAO {

	private static MovieDAO instance = null;
	private static final HashMap<String, Movie> allMovies = new HashMap<>(); //movie.title -> movie
	
	private MovieDAO(){}
	
	public static synchronized MovieDAO getInstance(){
		if(instance == null){
			instance = new MovieDAO();
			if(allMovies.isEmpty()){
				String query = "SELECT id, poster, rating, description, date, name FROM imdb_movie;";
				try {
					PreparedStatement stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
					ResultSet rs = stmt.executeQuery();
					while(rs.next()){
						Movie newMovie = null;
						long id = rs.getLong("id");
						String name = rs.getString("name");
						String poster = rs.getString("poster");
						String descr = rs.getString("description");
						Date date = rs.getDate("date");
						ArrayList<String> genresList = new ArrayList<>();
						HashSet<Actor> actors = new HashSet<>();
						HashSet<Director> scenaristi = new HashSet<>();
						query = "SELECT genre_name FROM imdb.imdb_genre_movie WHERE movie_id = ?;";
						stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, id);
						ResultSet rs1 = stmt.executeQuery();
						while(rs1.next()){
							String genre = rs1.getString("genre_name");
							genresList.add(genre);
						}
						query = "SELECT name FROM imdb.imdb_actor WHERE id in (SELECT actor_id FROM imdb.imdb_actor_movie WHERE movie_id = ?)";
						stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, id);
						ResultSet rs2 = stmt.executeQuery();
						while(rs2.next()){
							String actorName = rs1.getString("name");
							Actor actor = new Actor(actorName);
							actors.add(actor);
						}
						query = "SELECT name FROM imdb.imdb_director WHERE id in (SELECT director_id FROM imdb.imdb_director_movie WHERE movie_id = ?)";
						stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, id);
						ResultSet rs3 = stmt.executeQuery();
						while(rs3.next()){
							String directorName = rs1.getString("name");
							Director director = new Director(directorName);
							scenaristi.add(director);
						}
						try {
							newMovie = new Movie(name, poster, genresList, actors, scenaristi, descr, date);
						} catch (InvalidMovieException e) {
							System.out.println("Creating movie cashing failed in MovieDAO");
						}
						allMovies.put(newMovie.getName(), newMovie);
					}
				} catch (SQLException e) {
					System.out.println("MovieDAO->getInstance: " + e.getMessage());
				}
			}	
		}
		return instance;
	}
	
	
}

