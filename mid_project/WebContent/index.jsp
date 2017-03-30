<%@page import="java.io.PrintWriter"%>
<%@page import="model.dao.UserDAO"%>
<%@page import="model.dao.IMDbConnect"%>
<%@page import="model.user.User"%>
<%@page import="model.movie.Movie"%>
<%@page import="model.dao.MovieDAO"%>
<%@page import="java.util.List"%>
<%@page errorPage="error.jsp"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256" import="java.io.IOException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="styles/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="styles/style.css">
		
	  <style>
	  	body{
			background-color: lightgray;
		}
		
		.maincontainer{
			width: 1000px;
			margin: auto;
		}
		
		.mainbody{
			background-color: white;
			height: 100vh;
		}
		
	    #dialog-form1: label, input, #dialog-form2: label, input {
	    	display: block;
	    }
	    
	    input.text { 
		    margin-bottom: 12px;
		    width: 95%; 
		    padding: .4em;
	    }
	    
	    fieldset { 
	    	padding: 0; 
	    	border: 0;
	    	margin-top: 25px;
	    }
	    
	    h1 { 
	    	font-size: 1.2em; 
	    	margin: .6em 0; 
	    }
	    
	    .ui-dialog .ui-state-error { 
	    	padding: .3em; 
	    }
	    
	    .validateTips { 
	    	border: 1px solid transparent;
	    	padding: 0.3em;
	     }
	    		
		#top-rated, #most-commented{
			text-align: center;		
		}
		
		#tops{
			margin-bottom: 20px;
		}
		
		h1 {
		    font-weight: bold;
		    font-size: 32px;
		}
		
		#movie-post{
			display: none;
		}
		
		#top-ten-rated{
			background-color: gray;
		}
		
		#top-ten-rated table td{
			width: 150px;
			heigth: 150px;
			overflow: hidden;
		}
		
		#top-ten-rated table img {
			background-position: center center;
   			background-size: cover;
			max-width: 150px;
			width: auto;
			height: auto;
			margin: auto;
			margin-left: 40px;
			margin-bottom: 20px;
			margin-top: 20px;
		}
		
	</style>
<title>IMDB</title>
</head>
<body>
	<div class="container">
	  <div class="row justify-content-md-center maincontainer">
	    <div class="col-10 col-md-auto">
	       <div class="mainbody">
			<jsp:include page="navbar.jsp" /> 
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
		   </div>
	    </div>
	  </div>
	</div>
	
	<!--  SCRIPTS  -->
	<script type="text/javascript" src="lib/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="lib/bootstrap.min.js"></script>
  	<script src="lib/jquery-ui.js"></script>
    <script>
	  $( function() {
	    var dialog = $( "#dialog-form" ).dialog({
	      autoOpen: false,
	      height: 200,
	      width: 300,
	      modal: true
	    });
	 
	    $( "#add-movie" ).button().on( "click", function() {
	      dialog.dialog( "open" );
	    });
	  } );
	  
	  $( function() {
		    var dialog = $( "#dialog-form1" ).dialog({
		      autoOpen: false,
		      height: 350,
		      width: 300,
		      modal: true
		    });
		 
		    $( "#create-post" ).button().on( "click", function() {
		      dialog.dialog( "open" );
		    });
		  } );
	</script>
</body>
</html>