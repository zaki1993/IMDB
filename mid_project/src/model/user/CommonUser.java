package model.user;

import exceptions.InvalidUserException;

public final class CommonUser extends User {

	private CommonUser(String name, byte age, String location) throws InvalidUserException {
		super(name, age, location);
	}
	
	public static CommonUser register(String name, byte age, String location) throws InvalidUserException{
		return new CommonUser(name, age, location);
	}
}
