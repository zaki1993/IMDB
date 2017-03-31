package model.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				System.out.println("asdadasdasdsa");
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
						String date = rs.getString("date");
						double rating = rs.getDouble("rating");
						ArrayList<String> genresList = new ArrayList<>();
						HashSet<Actor> actors = new HashSet<>();
						HashSet<Director> scenaristi = new HashSet<>();
						ResultSet rs1 = null;
						query = "SELECT genre_name FROM imdb.imdb_genre_movie WHERE movie_id = ?;";
						try{
							stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
							stmt.setLong(1, id);
							rs1 = stmt.executeQuery();
							while(rs1.next()){
								String genre = rs1.getString("genre_name");
								genresList.add(genre);
							}
						} catch(SQLException e1){
							System.out.println("MovieDAO->Genre: " + e1.getMessage());
						}
						query = "SELECT name FROM imdb.imdb_actor WHERE id in (SELECT actor_id FROM imdb.imdb_actor_movie WHERE movie_id = ?)";
						try{
							stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
							stmt.setLong(1, id);
							ResultSet rs2 = stmt.executeQuery();
							while(rs2.next()){
								String actorName = rs2.getString("name");
								Actor actor = new Actor(actorName);
								actors.add(actor);
							}
						} catch(SQLException e1){
							System.out.println("MovieDAO->Actor: " + e1.getMessage());
						}
						query = "SELECT name FROM imdb.imdb_director WHERE id in (SELECT director_id FROM imdb.imdb_director_movie WHERE movie_id = ?)";
						try{
							stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
							stmt.setLong(1, id);
							ResultSet rs3 = stmt.executeQuery();
							while(rs3.next()){
								String directorName = rs3.getString("name");
								Director director = new Director(directorName);
								scenaristi.add(director);
							}
						} catch(SQLException e1){
							System.out.println("MovieDAO->Genre: " + e1.getMessage());
						}
						try {
							newMovie = new Movie(name, poster, genresList, actors, scenaristi, descr, date, rating);
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

	private static String customGsonParser(String json, String name){
		String temp = json.substring(json.indexOf(name) + name.length() + 3);
		return temp.substring(0, temp.indexOf("\""));
	}
	
	public synchronized void addMovie(String name){
		if(allMovies.containsKey(name)){
			return;
		}
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
					IMDbConnect.getInstance().getConnection().setAutoCommit(false);
					String query = "INSERT IGNORE INTO `imdb_movie`(`poster`, `rating`, `description`, `date`, `name`) VALUES (?, ?, ?, ?, ?)";
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
					IMDbConnect.getInstance().getConnection().commit();
				} catch(SQLException ex){
					// dublicate fields
					// nothing to do
					// we set them to be unique
					System.out.println("Movie: " + ex);
					try {
						IMDbConnect.getInstance().getConnection().rollback();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("Fail pri rollbacka");
					}
				}
				finally{
					try {
						IMDbConnect.getInstance().getConnection().setAutoCommit(true);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// add the director
				String director = customGsonParser(movieJson, "Director");
					try {
						IMDbConnect.getInstance().getConnection().setAutoCommit(false);
						long directorId = 0;
						String query = "INSERT IGNORE INTO `imdb_director`(`name`) VALUES (?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						stmt.setString(1, director);
						stmt.executeUpdate();
						ResultSet rSet = stmt.getGeneratedKeys();
						while(rSet.next()){
							directorId = rSet.getLong(1);
						}
						
						// now fill the director_movie table
						query = "INSERT IGNORE INTO `imdb_director_movie` (`director_id`, `movie_id`) VALUES (?, ?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, directorId);
						stmt.setLong(2, movieId);
						stmt.executeUpdate();
						IMDbConnect.getInstance().getConnection().commit();
					} catch (SQLException ex) {
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Director: " + ex);
						try {
							IMDbConnect.getInstance().getConnection().rollback();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("Fail pri rollbacka");
						}
					}
					finally{
						try {
							IMDbConnect.getInstance().getConnection().setAutoCommit(true);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				// add all the actors
				String[] actors = customGsonParser(movieJson, "Actors").split(", ");
				for(int i = 0; i < actors.length; i++){
					try{
						long actorId = 0;
						String query = "INSERT IGNORE INTO `imdb_actor`(`name`) VALUES (?)";
						IMDbConnect.getInstance().getConnection().setAutoCommit(false);
						stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						stmt.setString(1, actors[i]);
						stmt.executeUpdate();
						ResultSet rSet = stmt.getGeneratedKeys();
						while(rSet.next()){
							actorId = rSet.getLong(1);
						}
						
						// fill actor_movie table
						query = "INSERT IGNORE INTO `imdb_actor_movie` (`actor_id`, `movie_id`) VALUES (?, ?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query);
						stmt.setLong(1, actorId);
						stmt.setLong(2, movieId);
						stmt.executeUpdate();
						IMDbConnect.getInstance().getConnection().commit();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Actor: " + ex);
						try {
							IMDbConnect.getInstance().getConnection().rollback();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("Fail pri rollbacka");
						}
					}
					finally{
						try {
							IMDbConnect.getInstance().getConnection().setAutoCommit(true);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
		
				// add genres
				String genre = customGsonParser(movieJson, "Genre");
				String[] genres = genre.split(", ");
				for(int i = 0; i < genres.length; i++){
					try{
						String genreName = null;
						String query = "INSERT IGNORE INTO `imdb_genre`(`name`) VALUES (?)";
						IMDbConnect.getInstance().getConnection().setAutoCommit(false);
						stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						stmt.setString(1, genres[i]);
						stmt.executeUpdate();
						ResultSet rSet = stmt.getGeneratedKeys();
						while(rSet.next()){
							genreName = rSet.getString(1);
						}
						
						// fill genre_movie table
						query = "INSERT IGNORE INTO `imdb_genre_movie` (`genre_name`, `movie_id`) VALUES (?, ?)";
						stmt = imdb.getInstance().getConnection().prepareStatement(query);
						stmt.setString(1, genreName);
						stmt.setLong(2, movieId);
						stmt.executeUpdate();
						IMDbConnect.getInstance().getConnection().commit();
					} catch(SQLException ex){
						// dublicate fields
						// nothing to do
						// we set them to be unique
						System.out.println("Genre: " + ex);
						try {
							IMDbConnect.getInstance().getConnection().rollback();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("Fail pri rollbacka");
						}
					}
					finally{
						try {
							IMDbConnect.getInstance().getConnection().setAutoCommit(true);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
				allMovies.put(movie, new Movie(movie, poster, genresArray, actorsHash, directorsHash, description, date, Double.parseDouble(rating)));
			}
		} catch (IOException e) {
			System.out.println("In movie!");
			e.printStackTrace();
		} catch (InvalidMovieException e){
			System.out.println("Error occured: " + e.getMessage());
		}
	}
	
	public Map<String, Movie> allMovies() {
		getInstance();
		return Collections.unmodifiableMap(allMovies);
	}
	
	public Movie getTopRatedMovie(){
		Movie topRated = null;
		double currentRating = 0.0;
		for(Entry<String, Movie> i : allMovies.entrySet()){
			double temp = i.getValue().getRating();
			if(temp >= currentRating){
				currentRating = temp;
				topRated = i.getValue();
			}
		}
		return topRated;
	}
	
	public synchronized Movie getMostCommentedMovie(){
		Movie mostCommented = null;
		int comments = 0;
		for(Entry<String, Movie> i : allMovies.entrySet()){
			// nai tupata zaqvka ever
			// joinva ot 5 tablici za da vzeme broq na komentari za daden film
			String query = "SELECT count(*) as broika FROM imdb_movie INNER JOIN imdb_movie_post ON imdb_movie.id = Movie_id INNER JOIN imdb_post ON imdb_movie_post.Post_id = imdb_post.id INNER JOIN imdb_comment_post ON imdb_comment_post.post_id = imdb_post.id INNER JOIN imdb_comment ON imdb_comment.id = comment_id WHERE imdb_movie.id = ?";
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
				stmt.setLong(1, i.getValue().getId());
				rs = stmt.executeQuery();
				rs.next();
				int result = rs.getInt("broika");
				if(result >= comments){
					comments = result;
					mostCommented = i.getValue();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mostCommented;
	}
	
	private void selectionSort(List<Movie> movies){
		int sortMovieCount = 11;
		if(movies.size() <= sortMovieCount){
			sortMovieCount = movies.size() - 1;
		}
		int n = 0;
		while(n <= sortMovieCount){
			int start = n;
			for(int i = n; i < movies.size(); i++){
				if(movies.get(i).getRating() >= movies.get(start).getRating()){
					start = i;
				}
			}
			Movie temp = movies.get(n);
			movies.set(n, movies.get(start));
			movies.set(start, temp);
			n++;
		}
	}

	public synchronized void updateRating(String movieName, double movieRating){
		String query = "UPDATE imdb_movie SET rating = ? WHERE name=?";
		PreparedStatement stmt = null;
		try {
			stmt = IMDbConnect.getInstance().getConnection().prepareStatement(query);
			stmt.setDouble(1, movieRating);
			stmt.setString(2, movieName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("updateRating: " + e.getMessage());
		}
	}
	
	public List<Movie> topTenRated(){
		List<Movie> toSort = new ArrayList<>(allMovies.values());
		selectionSort(toSort);
		// remove the most rated movie because we already show this movie at the most rated movie field
		toSort.remove(0);
		return toSort.subList(0, 10);
	}
	
}

