package models;

public class Chat {
	private int id;
	private int msgfrom;
	private int msgto;
	private String date;
	private String time;
	private String msg;
	private String profilepic;
	
	public Chat(int id, int msgfrom, int msgto, String date, String time, String msg, String profilepic) {
		super();
		this.id = id;
		this.msgfrom = msgfrom;
		this.msgto = msgto;
		this.date = date;
		this.time = time;
		this.msg = msg;
		this.profilepic = profilepic;
	}
	
	public String getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMsgfrom() {
		return msgfrom;
	}
	public void setMsgfrom(int msgfrom) {
		this.msgfrom = msgfrom;
	}
	public int getMsgto() {
		return msgto;
	}
	public void setMsgto(int msgto) {
		this.msgto = msgto;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
