package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.MovieDAO;
import model.user.User;

/**
 * Servlet implementation class AddMovieServlet
 */
@WebServlet("/addmovie")
public class AddMovieServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "index.jsp";
		HttpSession session = request.getSession(true);
		if(!(session == null || session.isNew() || session.getAttribute("logged") == null)){
			if((Boolean) session.getAttribute("logged")){
				User user = (User) session.getAttribute("user");
				if(user.getStatus().equals(User.role.ADMIN.toString())){
					MovieDAO.getInstance().addMovie(request.getParameter("movie-name"));
				}
			}
		}
		session.setAttribute("home", true);
		session.setAttribute("post", false);
		response.sendRedirect(url);
	}

}
