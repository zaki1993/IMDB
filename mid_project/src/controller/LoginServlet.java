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
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	doPost(req, resp);
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
				HttpSession session = request.getSession(true);
				session.setMaxInactiveInterval(30); // set session time 30 seconds
				User toAdd = UserDAO.getInstance().getAllUsers().get(user);
				session.setAttribute("user", toAdd);
				try {
					response.sendRedirect("userLogged.jsp");
				} catch (IOException e) {
					System.out.println("Could not redirect to userLogged.jsp: " + e.getMessage());
				}
			}
			else{
				throw new UserNotFoundException();
			}
		} catch(InvalidUserException | UserNotFoundException ex){
			// redirect to home page
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
            PrintWriter out;
			try {
				out = response.getWriter();
	            out.println("<script> alert(\"Please make sure you enter a valid username or password.\") </script>");
	            out.println("<script> window.location = 'http://localhost:8080/mid_project/index.html' </script>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				rd.include(request, response);
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
