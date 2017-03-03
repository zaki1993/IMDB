package Model.User;

public interface IUser {
	// Не съм добавил параметрите все още
	// boolean за да проверяваме дали методите са били успешно изпълнени
	public boolean register();
	public boolean vote();
	public boolean comment();
	public boolean addToWatchList();
}