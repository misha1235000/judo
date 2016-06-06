package models;

import java.util.Date;

public class Message {
	private int    id;
	private String authorname;
	private String message;
	private String date;
	private String time;
	
	public Message(int id, String authorname, String message, String date, String time) {
		this.setId(id);
		this.setAuthorname(authorname);
		this.setMessage(message);
		this.time = time;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
