package models;

public class Comment {
	private int id;
	private int posterId;
	private int picId;
	private String posterName;
	private String posterLastName;
	private String comment;
	private String date;
	private String time;

	public Comment(int id, int posterId, int picId, String posterName, String posterLastName, String comment, String date, String time) {
		super();
		this.id = id;
		this.posterId = posterId;
		this.picId = picId;
		this.posterName = posterName;
		this.posterLastName = posterLastName;
		this.comment = comment;
		this.date = date;
		this.time = time;
	}
	
	public String getPosterName() {
		return posterName;
	}

	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}

	public String getPosterLastName() {
		return posterLastName;
	}

	public void setPosterLastName(String posterLastName) {
		this.posterLastName = posterLastName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPosterId() {
		return posterId;
	}
	public void setPosterId(int posterId) {
		this.posterId = posterId;
	}
	public int getPicId() {
		return picId;
	}
	public void setPicId(int picId) {
		this.picId = picId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
}