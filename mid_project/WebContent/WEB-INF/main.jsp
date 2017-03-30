<%@page import="java.io.PrintWriter"%>
<%@page import="model.dao.UserDAO"%>
<%@page import="model.dao.IMDbConnect"%>
<%@page import="model.user.User"%>
<%@page import="model.movie.Movie"%>
<%@page import="model.dao.MovieDAO"%>
<%@page import="java.util.List"%>
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
	<div id="container">
		<%
			Movie mostRated = MovieDAO.getInstance().getTopRatedMovie();
			Movie mostCommented = MovieDAO.getInstance().getMostCommentedMovie();
			String mostRatedPoster = mostRated.getPoster();
			String mostCommentedPoster = mostCommented.getPoster();
		%>
		<div id="tops" class="container">
			<div id="top-rated" class="col-md-5">
				<h1 style=""> TOP RATED </h1>
				<a href="#"><img src="<%= mostRatedPoster %>"></a>
			</div>
			<div id="most-commented" class="col-md-5">
				<h1 style=""> MOST COMMENTED </h1>
				<a href="#"><img src="<%= mostCommentedPoster %>"></a>
			</div>
		</div>
		<div id="top-ten-rated">
			<!-- random generated 10 movie posters -->
			<%
				List<Movie> topTenRated = MovieDAO.getInstance().topTenRated();
				int n = 0;
				out.println("<table>");
				out.println("<tr>");
				for(Movie i : topTenRated){
					if(n % 5 == 0 && n != 0){
						out.println("</tr>");
						out.println("<tr>");
					}
					out.println("<td>");
					out.println("<a href=\"#\"><img src=\""+ i.getPoster() + "\"></a>");
					out.println("</td>");
					n++;
				}
				out.println("</tr>");
				out.println("</table>");
			%>
		</div>
	 </div>
</body>
</html>