package model.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

// Creates request to IMDB api and pulls information about the movie!

public class Request {
	private Request(){
		
	}
	
	public static String read(String link) throws IOException{
		URL url = new URL(link);
	    URLConnection yc = url.openConnection();
	    BufferedReader in = new BufferedReader(
	                            new InputStreamReader(
	                            yc.getInputStream()));
	    String inputLine = "";
	    StringBuilder result = new StringBuilder("");
	    while ((inputLine = in.readLine()) != null){
	    	result.append(inputLine);
	    }
	    in.close();
	    return result.toString();
	}
}
