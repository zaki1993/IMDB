<%@page import="db_connector.IMDbConnect"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256" import="java.io.IOException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="style.css">
	<link rel="stylesheet" type="text/css" href="https://bootswatch.com/sandstone/bootstrap.min.css">
		
	  <style>
	    label, input { display:block; }
	    input.text { margin-bottom:12px; width:95%; padding: .4em; }
	    fieldset { padding:0; border:0; margin-top:25px; }
	    h1 { font-size: 1.2em; margin: .6em 0; }
	    .ui-dialog .ui-state-error { padding: .3em; }
	    .validateTips { border: 1px solid transparent; padding: 0.3em; }
	    
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
		
	</style>
<title>IMDB</title>
</head>
<body>
 	<% if(session == null || session.isNew() || IMDbConnect.loggedUsers == null || IMDbConnect.loggedUsers.isEmpty()){
 		// if the session is invalid then redirect
 		// if someone tries to call this file without permission then also redirect (TODO)
	 		response.sendRedirect("index.html");
	 		return;
 		}
 	%>
 	<% String status = IMDbConnect.loggedUsers.get(session.getId()).getStatus(); %> <!-- use this status for privileges -->
	
	<div class="container">
	  <div class="row justify-content-md-center maincontainer">
	    <div class="col-10 col-md-auto">
	       <div class="mainbody">
		     <nav class="navbar navbar-default">
			  <div class="container-fluid">
			    <div class="navbar-header">
			      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
			        <span class="sr-only">Toggle navigation</span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			      </button>
			      <a class="navbar-brand" href="#">IMDB</a>
			      <form class="navbar-form navbar-left" role="search">
			        <div class="form-group">
			          <input class="form-control" placeholder="Search" type="text">
			        </div>
			        <button type="submit" class="btn btn-default">Search</button>
			      </form>
			    </div>
   			    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			    	<ul class="nav navbar-nav pull-right">
				      <li class="dropdown">
			           <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="true">
			             <% String currentUser = (String) (session.getAttribute("IMDb_user"));%> <%= currentUser %> 
			             <span class="caret"></span>
			           </a>
			           <ul class="dropdown-menu" role="menu">
		            	 <% if(status.equals("ADMIN")) {
		            			// put admin functionality here
		            			try {
		            				out.println("<li>");
		            				out.println("<a>Create post</a>");
		            				out.println("</li>");
		            				out.println("<li>");
		            				out.println("<a id=\"add-movie\">Add Movie</a>");
		            				out.println("</li>");
		            			} catch (IOException e) {
		            				// in case of error redirect to logout -> home
		            				response.sendRedirect("logout");
		            		 		return;
		            			}
		            		} 
		            	 %>
		            	 <% if(status.equals("USER")) {
		            			// put user functionality here
		            			try {
		            				out.println("<li>");
		            				out.println("<a>Rate Movie</a>");
		            				out.println("</li>");
		            				out.println("<li>");
		            				out.println("<a>Comment Post</a>");
		            				out.println("</li>");
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
		        	<div>
		        	
		        	<!-- Add new movie field -->
				  	<div id="dialog-form" title="Add new movie!">
					  <form action="addmovie" type="post">
					      Movie name
					      <input type="text" name="movie-name" placeholder="Movie name" class="text ui-widget-content ui-corner-all">
					      <input type="submit" value="Add movie"></input>
					  </form>
					</div>
			  </div>
			</nav>
			</nav>
		   </div>
	    </div>
	  </div>
	</div>
	
	<!--  SCRIPTS  -->
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="https://bootswatch.com/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
  	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>
	  $( function() {
	    var dialog = $( "#dialog-form" ).dialog({
	      autoOpen: false,
	      height: 200,
	      width: 350,
	      modal: true
	    });
	 
	    $( "#add-movie" ).button().on( "click", function() {
	      dialog.dialog( "open" );
	    });
	  } );
	</script>
</body>
</html>