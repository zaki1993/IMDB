package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
			return;
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
			System.out.println("PostDAO -> Post: " + ex);
		}
		//Add to imdb_movie_post
		try{
			String query = "INSERT INTO imdb_movie_post (Movie_id, Post_id) VALUES( ?, ?)";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, movie.getId());
			stmt.setLong(2, postId);
			stmt.executeUpdate();
		} catch(SQLException ex){
			System.out.println("PostDAO -> Movie_post: " + ex);
		}
	}
	
	public synchronized void addComment(String movieName, long userId, String content){
		System.out.println(movieName + " " + userId + " " + content);
		MovieDAO md = MovieDAO.getInstance();
		if (!md.allMovies().containsKey(movieName)) {
			return;
		}
		Movie movie = md.allMovies().get(movieName);
		
		IMDbConnect imdb = IMDbConnect.getInstance();
		PreparedStatement stmt;
		long postId = 0l;
		long commentId = 0l;
		//Create comment
		try{
			String query = "INSERT INTO imdb_comment (User_id, content) VALUES(?, ?)";
			stmt = imdb.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, userId);
			stmt.setString(2, content);
			stmt.executeUpdate();
			ResultSet rSet = stmt.getGeneratedKeys();
			while(rSet.next()){
				commentId = rSet.getLong(1);
			}
		} catch(SQLException ex){
			System.out.println("PostDAO -> Comment: " + ex);
		}
		//Get postId
		try{
			String query = "SELECT Post_id FROM imdb.imdb_movie_post WHERE Movie_id = ?";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, movie.getId());
			stmt.executeQuery();
			ResultSet rSet = stmt.getResultSet();
			while(rSet.next()){
				postId = rSet.getLong("Post_id");
			}
		} catch(SQLException ex){
			System.out.println("PostDAO -> get Post ID: " + ex);
		}
		//Add to imdb_comment_post
		try{
			String query = "INSERT INTO imdb_comment_post (comment_id, post_id) VALUES( ?, ?)";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, commentId);
			stmt.setLong(2, postId);
			stmt.executeUpdate();
		} catch(SQLException ex){
			System.out.println("PostDAO -> comment_post: " + ex);
		}
	}
	
	public synchronized ArrayList<String> getComments(String movieName){
		ArrayList<String> comments = new ArrayList<>();
		MovieDAO md = MovieDAO.getInstance();
		if (!md.allMovies().containsKey(movieName)) {
			return null;
		}
		Movie movie = md.allMovies().get(movieName);
		
		IMDbConnect imdb = IMDbConnect.getInstance();
		PreparedStatement stmt;
		long postId = 0l;
		//Get postId
		try{
			String query = "SELECT Post_id FROM imdb.imdb_movie_post WHERE Movie_id = ?";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, movie.getId());
			stmt.executeQuery();
			ResultSet rSet = stmt.getResultSet();
			while(rSet.next()){
				postId = rSet.getLong("Post_id");
			}
		} catch(SQLException ex){
			System.out.println("PostDAO -> get Post ID: " + ex);
		}
		//Get all comments
		try{
			String query = "SELECT Content, User_id FROM imdb.imdb_comment WHERE id in (SELECT comment_id FROM imdb.imdb_comment_post WHERE post_id = ?)";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, postId);
			stmt.executeQuery();
			ResultSet rSet = stmt.getResultSet();
			while(rSet.next()){
				String comment = rSet.getString("Content");
				long userId = rSet.getLong("User_id");
				query = "SELECT name FROM imdb.imdb_user WHERE id = " + userId;
				stmt = imdb.getInstance().getConnection().prepareStatement(query);
				stmt.executeQuery();
				ResultSet rs = stmt.getResultSet();
				String userComment = null;
				while(rs.next()){
					String name = rs.getString("name");
					userComment = name + " : " + comment;
				}
				comments.add(userComment);
			}
		} catch(SQLException ex){
			System.out.println("PostDAO -> get comments: " + ex);
		}
		return comments;
	}
	
	public boolean hasPost(String movieName){
		MovieDAO md = MovieDAO.getInstance();
		if (!md.allMovies().containsKey(movieName)) {
			return false;
		}
		Movie movie = md.allMovies().get(movieName);
		
		IMDbConnect imdb = IMDbConnect.getInstance();
		PreparedStatement stmt;
		long postId = 0l;
		
		//Get postId
		try{
			String query = "SELECT Post_id FROM imdb.imdb_movie_post WHERE Movie_id = ?";
			stmt = imdb.getInstance().getConnection().prepareStatement(query);
			stmt.setLong(1, movie.getId());
			stmt.executeQuery();
			ResultSet rSet = stmt.getResultSet();
			while(rSet.next()){
				postId = rSet.getLong("Post_id");
			}
		} catch(SQLException ex){
			System.out.println("PostDAO -> get Post ID: " + ex);
		}
		if (postId == 0) {
			return false;
		}
		return true;
	}
	
}
