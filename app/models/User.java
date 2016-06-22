package models;

public class User {
	private int	   id;
	private String username;
	private String pass;
	private String firstName;
	private String lastName;
	private String email;
	private	int	   perm;
	private String profilepic;

	public User(int id, String username, String pass, String firstName, String lastName, String email, int perm,
			String profilepic) {
		super();
		this.id = id;
		this.username = username;
		this.pass = pass;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.perm = perm;
		this.profilepic = profilepic;
	}
	
	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

	public int getPerm() {
		return perm;
	}

	public void setPerm(int perm) {
		this.perm = perm;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}