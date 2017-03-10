package model.user;

public final class CommonUser extends User {

	private CommonUser(String name, byte age, String location) {
		super(name, age, location);
	}
	
	public static CommonUser register(String name, byte age, String location){
		return new CommonUser(name, age, location);
	}
}
