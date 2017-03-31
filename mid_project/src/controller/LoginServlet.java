package controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.IMDbConnect;
import model.dao.UserDAO;
import model.exceptions.InvalidUserException;
import model.exceptions.UserNotFoundException;
import model.user.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.setMaxInactiveInterval(60*2); // set session time 30 seconds
		session.setAttribute("home", true);
		session.setAttribute("post", false);
		resp.sendRedirect("index.jsp");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// TODO Auto-generated method stub
		String user = request.getParameter("username");
		String password = request.getParameter("password");
		try{
			if(UserDAO.getInstance().validLogin(user, password)){
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(60*2); // set session time 30 seconds
				User toAdd = UserDAO.getInstance().getAllUsers().get(user);
				session.setAttribute("user", toAdd);
				session.setAttribute("logged", true);
				response.setHeader("Pragma", "No-cache");
				response.setDateHeader("Expires", 0);
				response.setHeader("Cache-Control", "no-cache");
				try {
					session.setAttribute("home", true);
					session.setAttribute("post", false);
					response.sendRedirect("index.jsp");
				} catch (IOException e) {
					System.out.println("Could not redirect to index.jsp: " + e.getMessage());
				}
			}
			else{
				throw new UserNotFoundException();
			}
		} catch(InvalidUserException | UserNotFoundException ex){
			// redirect to home page
            PrintWriter out = null;
			try {
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(60*2); // set session time 30 seconds
				session.setAttribute("home", true);
				session.setAttribute("post", false);
				out = response.getWriter();
	            out.println("<script> alert(\"Please make sure you enter a valid username or password.\") </script>");
	            out.println("<script> window.location = 'http://192.168.6.171:8080/mid_project/index.jsp' </script>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void destroy() {
		try{
			IMDbConnect.getInstance().getConnection().close();
		} catch(SQLException ex){
			System.out.println("LOGIN: Closing connection failed!");
		}
	}

}
