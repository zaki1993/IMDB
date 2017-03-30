package model.user;

import java.util.Collections;
import java.util.HashSet;

import model.movie.Movie;
import model.post.Post;

public interface IUser {
	//boolean methods so we can check for success
	//removed register to make it static
	boolean vote(Movie toRate, int vote);
	boolean addToWatchList(Movie toAdd);
	HashSet<Movie> getWatchList();
}