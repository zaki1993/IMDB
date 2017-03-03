package model.user;

import model.movie.Movie;
import model.post.Post;

public interface IUser {
	//boolean methods so we can check for success
	//removed register to make it static
	boolean vote(Movie toRate);
	boolean comment(Post post);
	boolean addToWatchList(Movie toAdd);
}