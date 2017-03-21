package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.UserDao;
import model.exceptions.InvalidUserException;
import model.user.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String user = request.getParameter("username");
		String password  = request.getParameter("password");
		String age = request.getParameter("age");
		String location = request.getParameter("location");		
		try{
			User newUser = new User(user, (byte) Integer.parseInt(age), location, password);
			UserDao.getInstance().addUser(newUser);
			response.sendRedirect("registered.html");
		} catch(InvalidUserException | IOException ex){
			try {
				response.sendRedirect("index.html");
			} catch (IOException e) {
				System.out.println("Register counld not redirect to index.html: " + e.getMessage());
			}
		}
	}

}
