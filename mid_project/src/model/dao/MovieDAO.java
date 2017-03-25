package model.dao;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
							newMovie.setId(id);
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

	private synchronized static String customGsonParser(String json, String name){
		String temp = json.substring(json.indexOf(name) + name.length() + 3);
		return temp.substring(0, temp.indexOf("\""));
	}
	
	public synchronized void addMovie(String name){
		String[] names = name.split(" ");
		StringBuilder link = new StringBuilder("http://www.omdbapi.com/?t=");
		for (int i = 0; i < names.length; i++) {
			if(i == 0){
				link.append(names[i]);
				continue;
			}
			link.append("+");
			link.append(names[i]);
		}
		try {
			String movieJson = Request.read(link.toString());
			
			System.out.println(movieJson);
			
			String rs = movieJson.substring(movieJson.indexOf("\"Response\":\""));			
			if(rs.contains("False")){
				throw new InvalidMovieException();
			}
			else{
				IMDbConnect imdb = IMDbConnect.getInstance();
				PreparedStatement stmt;
				
				// first add the movie
				String movie = customGsonParser(movieJson, "Title");
				String poster = customGsonParser(movieJson, "Poster");
				String rating = customGsonParser(movieJson, "imdbRating");
				String description = customGsonParser(movieJson, "Plot");
				String date = customGsonParser(movieJson, "Released");
				long movieId = 0;
				if(rating.equals("N/A")){
					rating = "0";
				}
				try{
					String query = "INSERT IGNORE INTO `IMDb_movie`(`poster`, `rating`, `description`, `date`, `name`) VALUES (?, ?, ?, ?, ?)";
					stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					stmt.setString(1, poster);
					stmt.setDouble(2, Double.parseDouble(rating));
					stmt.setString(3, description);
					stmt.setString(4, date);
					stmt.setString(5, movie);
					stmt.executeUpdate();
					ResultSet rSet = stmt.getGeneratedKeys();
					while(rSet.next()){
						movieId = rSet.getLong(1);
					}
				} catch(SQLException ex){
					// dublicate fields
					// nothing to do
					// we set them to be unique
					System.out.println("Movie: " + ex);
				}
				
				// add the director
				String director = customGsonParser(movieJson, "Director");
					try {
						long directorId = 0;
						String query = "INSERT IGNORE INTO `IMDb_director`(`name`) VALUES (?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						stmt.setString(1, director);
						stmt.executeUpdate();
						ResultSet rSet = stmt.getGeneratedKeys();
						while(rSet.next()){
							directorId = rSet.getLong(1);
						}
						
						// now fill the director_movie table
						query = "INSERT IGNORE INTO `IMDb_director_movie` (`director_id`, `movie_id`) VALUES (?, ?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, directorId);
						stmt.setLong(2, movieId);
						stmt.executeUpdate();
						
					} catch (SQLException ex) {
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Director: " + ex);
					}
				
				// add all the actors
				String[] actors = customGsonParser(movieJson, "Actors").split(", ");
				for(int i = 0; i < actors.length; i++){
					try{
						long actorId = 0;
						String query = "INSERT IGNORE INTO `IMDb_actor`(`name`) VALUES (?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						stmt.setString(1, actors[i]);
						stmt.executeUpdate();
						ResultSet rSet = stmt.getGeneratedKeys();
						while(rSet.next()){
							actorId = rSet.getLong(1);
						}
						
						// fill actor_movie table
						query = "INSERT IGNORE INTO `IMDb_actor_movie` (`actor_id`, `movie_id`) VALUES (?, ?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, actorId);
						stmt.setLong(2, movieId);
						stmt.executeUpdate();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Actor: " + ex);
					}
				}
		
				// add genres
				String genre = customGsonParser(movieJson, "Genre");
				String[] genres = genre.split(", ");
				for(int i = 0; i < genres.length; i++){
					try{
						String genreName = null;
						String query = "INSERT IGNORE INTO `IMDb_genre`(`name`) VALUES (?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						stmt.setString(1, genres[i]);
						stmt.executeUpdate();
						ResultSet rSet = stmt.getGeneratedKeys();
						while(rSet.next()){
							genreName = rSet.getString(1);
						}
						
						// fill genre_movie table
						query = "INSERT IGNORE INTO `IMDb_genre_movie` (`genre_name`, `movie_id`) VALUES (?, ?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query);
						stmt.setString(1, genreName);
						stmt.setLong(2, movieId);
						stmt.executeUpdate();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Genre: " + ex);
					}
				}
				ArrayList<String> genresArray = new ArrayList<>();
				for(String i : genres){
					genresArray.add(i);
				}
				HashSet<Actor> actorsHash = new HashSet<>();
				for(String i : actors){
					actorsHash.add(new Actor(i));
				}
				HashSet<Director> directorsHash = new HashSet<>();
				directorsHash.add(new Director(director));
				allMovies.put(movie, new Movie(movie, poster, genresArray, actorsHash, directorsHash, description, new java.util.Date(date)));
			}
		} catch (IOException e) {
			System.out.println("In movie!");
			e.printStackTrace();
		} catch (InvalidMovieException e){
			System.out.println("Error occured: " + e.getMessage());
		}
	}
	
	public synchronized Map<String, Movie> allMovies() {
		return Collections.unmodifiableMap(allMovies);
	}
	
	
}

