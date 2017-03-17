package controller;

import java.io.IOException;

import DataBase.Request;
import exceptions.InvalidMovieException;
import exceptions.InvalidUserException;
import exceptions.UserNotFoundException;
import model.user.User;

public class Demo {

	public static void main(String[] args) throws InvalidUserException, InvalidMovieException, UserNotFoundException {
		//User.register("Goshod", "goshko123", (byte)20, "Germaniq");
//		User.register("admin", "admin", (byte)25, "Sofia");
//		User admin = User.login("admin", "admin");
//		admin.addMovie("logan");
//		User gosho = User.login("Gosho", "goshko123");
//		gosho.addMovie("Iron man");
		//User.addMovie("Logan");
		/*
		IMDbConnect connect = new IMDbConnect();
		connect.getData();
		System.out.println("--------DataBase--------");
		
		DataBase x = new DataBase("Movie");
		DataBase users = new DataBase("User");

		Actor brat = new Actor("Brat Pit", (byte)60);
		HashSet<Actor> actiorche = new HashSet<>();
		actiorche.add(brat);
		String[] genre = {"Action", "Drama"};
		Movie movie1 = new Movie("Pesho kalibrata", "Poster za pesho kalibrata",genre, actiorche, new HashSet<Director>(), "Mnogo qk film", LocalDate.of(2005, Month.AUGUST, 12));
		
		// System.out.println(movie1);
		Movie movie2 = new Movie("Pesho kalibrata2", "Poster za pesho kalibrata2",genre, new HashSet<Actor>(), new HashSet<Director>(), "Mnogo po qk film prosto", LocalDate.of(2005, Month.AUGUST, 12));
		x.insert(movie1);
		x.insert(movie2);
		x.print();
		
		User pesho = User.register("Pesho", (byte) 27, "Bulgaria");
		User gosho = User.register("Gosho", (byte) 50, "Polsha");
		users.insert(pesho);
		users.insert(gosho);
		
		pesho.vote(movie1, 5);
		pesho.vote(movie1, 4);
		gosho.vote(movie1, 9);
		System.out.println("-------------------After vote----------------------");
		//update database
		//works fine
		x.insert(movie1);
		x.print();
		System.out.println("-----------------------------------------");
		pesho.addToWatchList(movie2);
		pesho.addToWatchList(movie2);
		pesho.addToWatchList(movie1);
		
		//System.out.println(pesho);
		gosho.addToWatchList(movie1);
		users.insert(pesho);
		users.insert(gosho);
		users.print();
		//System.out.println("-----------------------------------------");
		//System.out.println(pesho.toString());
		//System.out.println("-----------------------------------------");
		//System.out.println(movie1.toString());
		System.out.println("-----------------------------------------");
		System.out.println("-----------------------------------------");
		Post post1 = new Post(movie1);
		Post post2 = new Post(movie2);
		pesho.comment(post1, "Basi qkiq film prosto ne moga da jiveq bez nego!");
		gosho.comment(post1, "Taka e brat");
		DataBase posts = new DataBase("Posts");
		posts.insert(post1);
		posts.insert(post2);
		posts.print();
		*/
	}	
}
