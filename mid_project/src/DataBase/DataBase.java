package DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class DataBase {
	private HashMap<Object, LinkedHashMap<String, Object>> database;
	private String[] fields;
	private String type;
	
	public DataBase(String type, String[] fields){
		this.database = new HashMap<>();
		this.fields = new String[fields.length];
		int size = 0;
		for(String i : fields){
			this.fields[size++] = i;
		}
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
	public void insert(Object object){
		/*
		if(!object.getClass().getName().equals(type)){
			System.out.println("Invalid entry data!");
			return;
		}
		*/
		if(database.containsKey(object)){
			return;
		}
		LinkedHashMap<String, Object> temp = new LinkedHashMap<>();
		String data = object.toString();
		String[] splitData = data.substring(1, data.length() - 1).split(", ");
		for (int i = 0; i < fields.length; i++) {
			temp.put(fields[i], splitData[i]);
		}
		database.put(object, temp);
		System.out.println("Successfully inserted!");
	}
	public void print(){
		System.out.println("Table: " + getType());
		for(Entry<Object, LinkedHashMap<String, Object>> i : database.entrySet()){
			for(Entry<String, Object> j : i.getValue().entrySet()){
				System.out.println("\t" + j.getKey() + ": " + j.getValue());
			}
			System.out.println("");
		}
	}
}
