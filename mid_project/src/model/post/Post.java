package model.post;
import java.util.ArrayList;

import model.movie.Movie;
import model.user.User;

public class Post {
	private Movie movie;
	private ArrayList<String> comments;
	
	public Post(Movie movie) {
		super();
		this.movie = movie;
		this.comments = new ArrayList<>();
	}
	
	public void addComment(User user, String msg){
		String comment = user.getName() + ": " + msg;
		comments.add(comment);
	}
	
}