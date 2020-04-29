package server;

public class User {

	private final String name;
    private final String pass;
	public User(String username, String useraddress){
		this.name = username;
        this.pass = useraddress;
	}
	public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }
}
