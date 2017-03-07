package DataBase;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class DataBase implements IDataBase{
	
	private HashSet<Object> database;
	private String type;
	
	public DataBase(String type){
		this.database = new HashSet();
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}
	
	public boolean insert(Object object){
		if (object == null) {
			return false;
		}
		this.database.add(object);
		return true;
	}
	
	public void print(){
		System.out.println("Table: " + getType());
		System.out.println();
		for (Object object : database) {
			System.out.println(object);
			System.out.println();
		}
	}
	
}
