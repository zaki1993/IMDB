<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="https://bootswatch.com/sandstone/bootstrap.min.css">
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
	</style>
<title>IMDB</title>
</head>
<body>
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
			           	 <% if(session == null || session.isNew()){ response.sendRedirect("index.html");} %>
			             <% String currentUser = (String) (session.getAttribute("IMDb_user"));%> <%= currentUser %> 
			             <span class="caret"></span>
			           </a>
				          <ul class="dropdown-menu" role="menu">
				            <p>
				            	Personal info, functionality
				            	TODO
				            </p>
				            <form action="logout" method="post">
				            	<input role="button" type="submit" value="Logout"></input>
				            </form>
				          </ul>	
			           </li>
			          </ul>
		        	<div>
			    <p>
				   
			    </p>
			  </div>
			</nav>			
		   </div>
	    </div>
	  </div>
	</div>
	
	<!--  SCRIPTS  -->	
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="https://bootswatch.com/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>