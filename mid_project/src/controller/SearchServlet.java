package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.MovieDAO;
import model.dao.PostDAO;
import model.movie.Movie;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String movieName = request.getParameter("movie-name");
		//System.out.println(MovieDAO.getInstance().allMovies());
		HttpSession session = request.getSession();
		if(!MovieDAO.getInstance().allMovies().containsKey(movieName)){
			session.setAttribute("movie", null);
		}
		else{
			Movie toSearch = MovieDAO.getInstance().allMovies().get(movieName);
			session.setAttribute("movie", toSearch);
		}
		if (!PostDAO.getInstance().hasPost(movieName)) {
			PostDAO.getInstance().addPost(movieName);
		}
		session.setAttribute("home", false);
		session.setAttribute("post", true);
		response.sendRedirect("index.jsp");
	}

}
