package models;

public class Picture {
	private int	   id;
	private String src;
	private String info;
	private String title;
	private String date;
	private String time;
	
	public Picture(int id, String src, String info, String title, String date, String time) {
		this.id = id;
		this.src = src;
		this.info = info;
		this.title = title;
		this.date = date;
		this.time = time;
	}
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
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


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
