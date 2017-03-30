package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.UserDAO;
import model.exceptions.InvalidUserException;
import model.user.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String user = request.getParameter("username");
		String password  = request.getParameter("password");
		String age = request.getParameter("age");
		String location = request.getParameter("location");		
		try{
			User newUser = new User(user, (byte) Integer.parseInt(age), location, password, 2);
			if(UserDAO.getInstance().addUser(newUser)){
				response.sendRedirect("registered.html");
			}
			else{
				response.sendRedirect("index.jsp");
			}
		} catch(InvalidUserException | IOException ex){
			try {
				response.sendRedirect("index.jsp");
			} catch (IOException e) {
				System.out.println("Register counld not redirect to index.jsp: " + e.getMessage());
			}
		}
	}

}
