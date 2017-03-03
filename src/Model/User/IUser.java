package Model.User;

import Model.Movie.*;
import Model.Post.*;

public interface IUser {
	// Не съм добавил параметрите все още
	// boolean за да проверяваме дали методите са били успешно изпълнени
	public boolean register();
	public boolean vote(Movie toRate);
	public boolean comment(Post post);
	boolean addToWatchList(Movie toAdd);
}