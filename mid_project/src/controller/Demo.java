package controller;

import java.time.LocalDate;
import java.time.Month;

import DataBase.DataBase;
import model.movie.*;

public class Demo {

	public static void main(String[] args) {
		
		String[] columns = {"Name", "Poster", "Genres", "Rating", "Actors", "Directors", "Description", "Date" };
		DataBase x = new DataBase("Movie", columns);
		String[] genre = {"Action", "Drama" };
		Movie movie1 = new Movie("Pesho kalibrata", "Poster za pesho kalibrata",genre, null, null, "Mnogo qk film", LocalDate.of(2005, Month.AUGUST, 12));
		x.insert(movie1);
		x.print();
	}	
}
