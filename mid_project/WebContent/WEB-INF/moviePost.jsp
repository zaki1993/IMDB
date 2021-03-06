<%@page import="model.dao.PostDAO"%>
<%@page import="model.movie.Director"%>
<%@page import="model.movie.Actor"%>
<%@page import="model.movie.Movie"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie post</title>
<style>
	body{
		margin: 0 auto;
		padding: 0 auto;
	}
	
	#vote{
		margin-left: -20px;
	}
	
	#select-vote{
		border-style: solid;
		border-color: black;
		width: 50px;
	}
	
	#comments{
		margin-top: -8px;
		resize: none;
	}
	
	#comment-section, #vote-section, #comment-button{
		margin-top: 5px;
	}
	
	#comment{
		margin-top: 3px;
		margin-bottom: 5px;
	}
	
	#main-container{
		margin-left: 11px;
		margin-right: 11px;
	}
	
</style>
</head>
<body>
	<%
		Movie movie = null;
		if(session.getAttribute("movie") == null){
			out.print("<h2>There is no such movie in here.</h2>");
			out.print("<h4>Check your spelling maybe?</h4>");
			session.setAttribute("home", true);
			session.setAttribute("post", false);
			return;
		}else{
			movie = (Movie) session.getAttribute("movie");
			session.setAttribute("home", false);
			session.setAttribute("post", true);
			int count = 0;
	%>
	<div class="jumbotron">
		<div class="container-fluid">
			<div class="col-xs-5">			
				<img src="<%= movie.getPoster() %>">
			</div>
			<div class="col-xs-7">
				<h2><%= movie.getName() %></h2>
				<p>
					<% if(!(movie.getGenre() == null || movie.getGenre().isEmpty() || movie.getGenre().toArray()[0].equals(""))){ %>
						<b>Genres: </b>
							<%
								count = 0;
								for(String genre : movie.getGenre()){
									out.print(genre);
									if(count < movie.getGenre().size() - 1){
										out.print(", ");
									}
									count++;
								}
							%>	
						<br>
					<% } %>
					<% if(!(movie.getDate() == null || movie.getDate().isEmpty())){ %>
						<b>Release: </b> <%= movie.getDate() %>
						<br>
					<% } %>
					<b>Description:</b> <%= movie.getDescription() %>
					<br>
					<b>Rating:</b> <%= String.format("%.2f", movie.getRating()) %>
					<br>
					<b>Actors:</b>
						 <%
						 	count = 0;
							for(Actor actor : movie.getActors()){
								out.print(actor.getName());
								if(count < movie.getActors().size() - 1){
									out.print(", ");
								}
								count++;
							}
						%>
					<br>
					<b>Directors:</b> 
						<%
							count = 0;
							for(Director director : movie.getDirectors()){
								out.print(director.getName());
								if(count < movie.getDirectors().size() - 1){
									out.print(", ");
								}
								count++;
							}
						%>
				</p>
		</div>
	</div>
	<%} %>
	</div>
	<% if(session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged") && session.getAttribute("user") != null){ %>
		<div id="main-container">
			<div id="vote-section" class="container">
				<div class="col-md-2">
					<form action="addWatchList" method="post">
						<input type="hidden" value="<%=movie.getName() %>" name="movieName">
						<input class="btn btn-default" type="submit" value="Add to Watchlist">
					</form>
				</div>
				<div id="vote" class="col-md-3">
					<form action="voteMovie" method="post">
						<select id="select-vote" name="vote-select">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
						</select>
						<input type="hidden" name="movie-name" value="<%= movie.getName() %>">
						<input class="btn btn-default" type="submit" value="Vote">
					</form>
				</div>
			</div>
			<br>
			<div id="comment-section" class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">Comment section</div>
		  			<div class="panel-body" style="max-height: 250px;min-height: 250px;overflow-y: scroll;">
						<%
						for(String comment : PostDAO.getInstance().getComments(movie.getName())){
							String user = comment.substring(0, comment.indexOf(": "));
							String content = comment.substring(comment.indexOf(": "));
						%>
							<div id="comment"><b><%= user %></b><%=content %></div>
						<%
						}
						%>
						<br>
					</div>
				</div>
				<form action="comment" method="post">
					<textarea placeholder="Comment about this movie!" id="comments" name="commentar" rows="3" cols="115"></textarea>
					<input type="hidden" name="movieName" value="<%= movie.getName() %>">
					<input id="comment-button" class="btn btn-default" type="submit" value="Comment">		
				</form>
			</div>
		</div>
	<% } %>
</body>
</html>