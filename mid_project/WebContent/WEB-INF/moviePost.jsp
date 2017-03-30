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
	%>
	<div class="container">
		<div class="col-md-4">			
			<a href="#"><img src="<%= movie.getPoster() %>"></a>
		</div>
		<div class="col-md-5">
			<h2><%= movie.getName() %></h2>
			<p><%
				for(String genre : movie.getGenre()){
					out.print(genre + " ");
				}%>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <%= movie.getDate() %>
				
			<p>Description : <%= movie.getDescription() %>
			<p>Rating: <%= String.format("%.2f", movie.getRating()) %>
			<p>Actors: <%
				for(Actor actor : movie.getActors()){
					out.print(actor.getName() + " ");
				}
			%>
			<p>Directors: <%
				for(Director director : movie.getDirectors()){
					out.print(director.getName() + " ");
				}
			%>
		</div>
	<%} %>
	</div>
	<% if(session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged")){ %>
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
		  			<div class="panel-body">
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