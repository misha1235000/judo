package models;

public class Picture {
	private int	   id;
	private String src;
	private String info;
	private String title;
	private String date;
	private String time;
	private int	   posterId;
	private String posterName;
	private String posterLastName;
	private int	   isvideo;
	private String posterimg;
	
	public Picture(int id, String src, String info, String title, String date, String time, int posterId, String posterName, String posterLastName, int isvideo, String posterimg) {
		this.id = id;
		this.src = src;
		this.info = info;
		this.title = title;
		this.date = date;
		this.time = time;
		this.posterId = posterId;
		this.posterName = posterName;
		this.posterLastName = posterLastName;
		this.isvideo = isvideo;
		this.posterimg = posterimg;
	}
	
	public String getPosterimg() {
		return posterimg;
	}

	public void setPosterimg(String posterimg) {
		this.posterimg = posterimg;
	}

	public int getIsvideo() {
		return isvideo;
	}

	public void setIsvideo(int isvideo) {
		this.isvideo = isvideo;
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

	public int getPosterId() {
		return posterId;
	}

	public void setPosterId(int posterId) {
		this.posterId = posterId;
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
