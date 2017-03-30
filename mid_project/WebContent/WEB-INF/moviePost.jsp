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
			<p>Rating: <%= movie.getRating() %>
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
	<div class="container">
		<div class="col-md-2">
			<form action="addWatchList" method="post">
				<input type="hidden" value="<%=movie.getName() %>" name="movieName">
				<input class="btn btn-default" type="button" value="Add to Watchlist">
			</form>
		</div>
		<div id="vote" class="col-md-3">
			<form action="voteMovie" method="post">
				<select id="select-vote" name="item">
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
				<input class="btn btn-default" type="submit" value="Vote">
			</form>
		</div>
	</div>
	<br>
	<div>
		<%
		for(String comment : PostDAO.getInstance().getComments(movie.getName())){
		%>
			
			<div><%=comment %></div>
		<%
		}
		%>
	<form action="comment" method="post">
		<textarea name="commentar" rows="3" cols="120"></textarea>
		<input type="submit	" value="Comment">
	</>
</body>
</html>