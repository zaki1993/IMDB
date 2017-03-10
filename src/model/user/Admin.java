package model.user;

public final class Admin extends User{
	private Admin(String name, byte age, String location){
		super(name, age, location, User.role.ADMIN);
	}

	public void promoteUser(User toPromote) {
		toPromote.setStatus(User.role.ADMIN);
	}
	
	public void demoteUser(User toDemote) {
		toDemote.setStatus(User.role.USER);
	}
}
