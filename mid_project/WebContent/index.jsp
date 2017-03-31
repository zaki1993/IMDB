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
		
		#my-form-redirect{
			display: inline;
			padding: -5px;
		}
		
		.maincontainer{
			width: 1000px;
			margin: auto;
		}
		
		.mainbody{
			background-color: white;
			height: 145vh;
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
		#dialog-form2{
			max-width: 350px;
			max-height: 350px;
		}
	</style>
<title>IMDB</title>
</head>
<body>
	<%
		boolean home = true;
		boolean post = false;
		if((Boolean) session.getAttribute("home") == null){
			home = true;
		}
		else{
			home = (Boolean) session.getAttribute("home");
		}
		
		if((Boolean) session.getAttribute("post") == null){
			post = false;
		}
		else{
			post = (Boolean) session.getAttribute("post");
		}
	%>
	<div class="container">
	  <div class="row justify-content-md-center maincontainer">
	    <div class="col-10 col-md-auto">
	       <div class="mainbody">
			<jsp:include page="/WEB-INF/navbar.jsp" /> 
			<% if(home && !post){ %>
				<jsp:include page="/WEB-INF/main.jsp" /> 
			<% } else if(post){ %>
				<jsp:include page="/WEB-INF/moviePost.jsp" />
			<% } %>
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
	  
	  $( function() {
		    var dialog = $( "#dialog-form2" ).dialog({
		      autoOpen: false,
		      resizable: false,
		      height: 350,
		      width: 350,
		      modal: true
		    });
		 
		    $( "#watchlist" ).button().on( "click", function() {
		      dialog.dialog( "open" );
		    });
		  } );
	</script>
</body>
</html>