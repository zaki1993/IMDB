package model.post;
import java.util.ArrayList;

import model.movie.Movie;
import model.user.User;

public class Post {
	
	private Movie movie;
	private ArrayList<String> comments;
	private long id;
	
	public Post(Movie movie) {
		this.movie = movie;
		this.comments = new ArrayList<>();
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void addComment(User user, String msg){
		String comment = user.getName() + ": " + msg;
		comments.add(comment);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movie == null) ? 0 : movie.hashCode());
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
		Post other = (Post) obj;
		if (movie == null) {
			if (other.movie != null)
				return false;
		} else if (!movie.equals(other.movie))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(this.movie.toString());
		str.append("\n Comments: \n");
		for (String string : comments) {
			str.append(string);
			str.append("\n");
		}
		return str.toString();		
	}
	
}