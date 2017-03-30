package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.MovieDAO;
import model.dao.UserDAO;
import model.user.User;

/**
 * Servlet implementation class AddToWatchListServlet
 */
@WebServlet("/addWatchList")
public class AddToWatchListServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String movie = request.getParameter("movieName");
		User user = (User) session.getAttribute("user");
		UserDAO.getInstance().addMovieToUser(user, movie);
		session.setAttribute("home", false);
		session.setAttribute("post", true);
		response.sendRedirect("index.jsp");
	}

}
