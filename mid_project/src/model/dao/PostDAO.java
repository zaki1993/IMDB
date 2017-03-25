package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.movie.Movie;

public class PostDAO {

	private static PostDAO instance = null;
	
	private PostDAO(){}
	
	public static synchronized PostDAO getInstance(){
		if(instance == null){
			instance = new PostDAO();	
		}
		return instance;
	}
	
	public synchronized void addPost(String movieName){
		MovieDAO md = MovieDAO.getInstance();
		if (!md.allMovies().containsKey(movieName)) {
			md.addMovie(movieName);
		}
		Movie movie = md.allMovies().get(movieName);
		
		IMDbConnect imdb = IMDbConnect.getInstance();
		PreparedStatement stmt;
		long postId = 0l;
		//Create post
		try{
			String query = "INSERT INTO imdb_post () VALUES()";
			stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet rSet = stmt.getGeneratedKeys();
			while(rSet.next()){
				postId = rSet.getLong(1);
			}
		} catch(SQLException ex){
			System.out.println("Post: " + ex);
		}
		//Add to imdb_movie_post
		try{
			String query = "INSERT INTO imdb_movie_post (Movie_id, Post_id) VALUES( ?, ?)";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
//			stmt.setLong(1, movie.get);
			stmt.executeUpdate();
			ResultSet rSet = stmt.getGeneratedKeys();
			while(rSet.next()){
				postId = rSet.getLong(1);
			}
		} catch(SQLException ex){
			System.out.println("Post: " + ex);
		}
		
	}

}
