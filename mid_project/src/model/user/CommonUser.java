package model.user;

import exceptions.InvalidUserDataException;

public final class CommonUser extends User {

	private CommonUser(String name, byte age, String location) throws InvalidUserDataException {
		super(name, age, location);
	}
	
	public static CommonUser register(String name, byte age, String location) throws InvalidUserDataException{
		return new CommonUser(name, age, location);
	}
}
