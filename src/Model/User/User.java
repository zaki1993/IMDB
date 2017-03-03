package Model.User;

public class User implements IUser{
	private String name;
	private byte age;
	private String location; // Предлагам за сега да е стринг, а след това може да вземем файл с градове и да избира някой от тях
	
	public User(String name, byte age, String location) {
		super();
		this.name = name;
		this.age = age;
		this.location = location;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean register() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean vote() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean comment() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean addToWatchList() {
		// TODO Auto-generated method stub
		return false;
	}
}
