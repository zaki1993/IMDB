package controller;

import java.time.LocalDate;
import java.time.Month;

import DataBase.DataBase;
import model.movie.*;
import model.user.User;

public class Demo {

	public static void main(String[] args) {
		
		String[] columns = {"Name", "Poster", "Genres", "Rating", "Actors", "Directors", "Description", "Date" };
		String[] userColumns = {"Name", "Age", "Location", "WatchList"};
		
		DataBase x = new DataBase("Movie", columns);
		DataBase users = new DataBase("User", userColumns);
		
		String[] genre = {"Action", "Drama"};
		Movie movie1 = new Movie("Pesho kalibrata", "Poster za pesho kalibrata",genre, null, null, "Mnogo qk film", LocalDate.of(2005, Month.AUGUST, 12));
		Movie movie2 = new Movie("Pesho kalibrata", "Poster za pesho kalibrata",genre, null, null, "Mnogo qk film", LocalDate.of(2005, Month.AUGUST, 12));
		x.insert(movie1);
		x.insert(movie2);
		x.print();
		
		User pesho = User.register("Pesho", (byte) 27, "Bulgaria");
		User gosho = User.register("Gosho", (byte) 50, "Polsha");
		users.insert(pesho);
		users.insert(gosho);
		
		pesho.vote(movie1, 5);
		pesho.vote(movie1, 4);//should fix that!
		gosho.vote(movie1, 9);
		
		//update database
		//works fine
		x.insert(movie1);
		x.print();
		
		users.print();
	}	
}
