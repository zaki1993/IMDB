<%@page import="model.dao.Request"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="model.dao.UserDAO"%>
<%@page import="model.dao.IMDbConnect"%>
<%@page import="model.user.User"%>
<%@page import="model.movie.Movie"%>
<%@page import="model.dao.MovieDAO"%>
<%@page errorPage="error.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% 
 		boolean valid = true;
 		String status = null;
 		if(session == null || session.isNew() || session.getAttribute("logged") == null || (Boolean) session.getAttribute("logged") == false){
	 		// if the session is invalid then redirect
	 		// if someone tries to call this file without permission then also redirect (TODO)
 			valid = false;
 		}
	 	User user = (User) session.getAttribute("user");
 		if(user == null){
	 		valid = false;
 		}
 		else{
 			status = user.getStatus();
 		}
 	%> <!-- use this status for privileges -->
     <nav class="navbar navbar-default">
		<div class="container-fluid">
		  <div class="navbar-header">
		    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
		      <span class="sr-only">Toggle navigation</span>
		      <span class="icon-bar"></span>
		      <span class="icon-bar"></span>
		      <span class="icon-bar"></span>
		    </button>
		    <a class="navbar-brand" href="index.jsp"><% session.setAttribute("home", true); session.setAttribute("post", false); %>IMDB</a>
		    <form action="search" method="post" class="navbar-form navbar-left" role="search">
		      <div class="form-group">
		        <input class="form-control" name="movie-name" placeholder="Search" type="text">
		      </div>
		      <button type="submit" class="btn btn-default">Search</button>
		    </form>
		  </div>
		  <% if(valid){ %>
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav pull-right">
					<li class="dropdown">
					    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="true">
					    <%= user.getName() %> 
						<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" role="menu">
							 <% if(status != null && status.equals("ADMIN")) {
									// put admin functionality here
									try { %>
									<li><a id="add-movie">Add Movie</a></li>
									<%	
										} catch (IOException e) {
											// in case of error redirect to logout -> home
											response.sendRedirect("logout");
									 		return;
										}
									} 
								%>
						<% if(status != null && status.equals("USER")) {
							// put user functionality here
								try { %>
									<li><form action="addWatchList"><button  id="add-movie" type="submit" >View watchlist</button></form></li>
								<% 
									} catch (IOException e) {
										// in case of error redirect to logout -> home
										response.sendRedirect("logout");
								 		return;
									}
							}
						%>
							<form action="logout" method="post">
								<input class="btn btn-primary btn-sm col-md-12" role="button" type="submit" value="Logout"></input>
							</form>
						</ul>	
					</li>
				</ul>
			</div>
			<% } else{ %>
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav pull-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="true">Login <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<form action="login" method="post">
				  				Username: 
				  				<input type="text" name="username" placeholder="Username">
			 					<br>
			  					Password: 
			  					<input type="password" name="password" placeholder="Password">
								<br><br>
								<input class="btn btn-primary btn-sm col-md-12" type="submit" value="Login">
							</form>
			      			</ul>	
			     		</li>
			   		<li>
			   			<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="true">Register <span class="caret"></span></a>
			      			<ul class="dropdown-menu" role="menu">
			       			<form action="register" method="post">
			 						Username: 
								<input type="text" name="username" placeholder="Username">
								<br>
								Password: 
								<input type="password" name="password" placeholder="Password">
								<br>
								Age: 
								<input type="number" name="age" placeholder="Age">
								<br>
								Location: 
								<input type="text" name="location" placeholder="Location">
								<br><br>
								<input class="btn btn-primary btn-sm col-md-12" type="submit" value="Register">
							</form>
			      			</ul>	
			     		</li>
			 		</ul>
			  </div>
			  <% } %>
			<div id="movie-post">
			<% if(status != null && status.equals("ADMIN")){ %>
				//<!-- Add new movie field -->
				<div id="dialog-form" class="popup" title="Add new movie!">
					<form action="addmovie" type="post">
						Movie name
						<input type="text" name="movie-name" placeholder="Movie name" class="text ui-widget-content ui-corner-all">
						<input class="btn btn-primary btn-sm col-md-5" type="submit" value="Add movie"></input>
					</form>
				</div>
			<%
			}
			%>
			
			<% if(status != null && status.equals("ADMIN")){ %>
				//<!-- Create new post field -->
				<div id="dialog-form1" class="popup" title="Create new post!">
					<form action="createpost" type="post">
						Movie name
						<input type="text" name="movie-name" placeholder="Movie name" class="text ui-widget-content ui-corner-all">
						<input class="btn btn-primary btn-sm col-md-5" type="submit" value="Create post"></input>
					</form>
				</div>
			<%
			}
			%>
	 		</div>
		</div>
 	</nav>
</body>
</html>