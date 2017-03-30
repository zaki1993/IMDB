package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.PostDAO;
import model.user.User;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String commentar = request.getParameter("commentar");
		commentar = commentar.trim();
		System.out.println(commentar.isEmpty());
		String movieName = request.getParameter("movieName");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(commentar != null && !commentar.isEmpty() && !commentar.equals("\n")){
			PostDAO.getInstance().addComment(movieName, user.getId(), commentar);
		}
		session.setAttribute("home", false);
		session.setAttribute("post", true);
		response.sendRedirect("index.jsp");
	}

}
