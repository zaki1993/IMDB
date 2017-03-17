package controller;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;

import db_connector.IMDbConnect;
import exceptions.InvalidUserException;
import exceptions.UserNotFoundException;
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
			User newUser = User.login(user, password);
			HttpSession session = request.getSession(true);
			IMDbConnect.loggedUsers.put(session.getId(), newUser); // add the logged user into collection with session id
			session.setAttribute("IMDb_user", user);
			session.setMaxInactiveInterval(30); // set session time 30 seconds
			response.sendRedirect("userLogged.jsp"); //logged-in page 
		} catch(InvalidUserException | UserNotFoundException | IOException ex){
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

}
