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
	private static final long serialVersionUID = 1L;
       
    /**-
     * @see HttpServlet#HttpServlet()
     */
    public AddMovieServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		if(session == null || session.getAttribute("logged") == null){
			session.invalidate();
			response.sendRedirect("index.html");
		}
		else{
			if((Boolean) session.getAttribute("logged")){
				User user = (User) session.getAttribute("user");
				if(user.getStatus().equals(User.role.ADMIN.toString())){
					MovieDAO.getInstance().addMovie(request.getParameter("movie-name"));
					response.sendRedirect("userLogged.jsp");
				}
				else{
					response.sendRedirect("userLogged.jsp");
				}
			}
			else{
				session.invalidate();
				response.sendRedirect("index.html");
			}
		}
	}

}
