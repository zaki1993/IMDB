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

/**
 * Servlet implementation class VoteServlet
 */
@WebServlet("/voteMovie")
public class VoteServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String voteSelect = request.getParameter("vote-select");
		String movieName = request.getParameter("movie-name");
		MovieDAO.getInstance().allMovies().get(movieName).rate(Integer.parseInt(voteSelect));
		HttpSession session = request.getSession();
		session.setAttribute("home", false);
		session.setAttribute("post", true);
		response.sendRedirect("index.jsp");
	}

}
