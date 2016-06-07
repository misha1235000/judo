package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Message;
import models.Picture;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.FormFactoryModule;
import play.libs.Json;
import play.mvc.*;
import views.html.helper.form;

public class mainCont extends Controller {
	
	Date       dt    = new Date();
	Connection con = null;
	String 	   st    = "";
	String 	   dtStr = "";
	String 	   nHoursToAdd;
	String 	   nMinutesToAdd;
	String 	   nSecondsToAdd;
	String 	   nDaysToAdd;
	String 	   nMonthsToAdd;
	
	private void getConn() {
		if (con == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	
			try {
				con = DriverManager.getConnection(
						"postgresql://postgres:postgres@liorjudo-8524.postgresql.dbs.appsdeck.eu/liorjudo_8524");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Filldtst() {
		if (dt.getHours() < 10) {
			nHoursToAdd = "0" + dt.getHours();
		} else {
			nHoursToAdd = Integer.toString(dt.getHours());
		}
		if (dt.getMinutes() < 10) {
			nMinutesToAdd = "0" + dt.getMinutes();
		} else {
			nMinutesToAdd = Integer.toString(dt.getMinutes());
		}
		if (dt.getSeconds() < 10) {
			nSecondsToAdd = "0" + dt.getSeconds();
		} else {
			nSecondsToAdd = Integer.toString(dt.getSeconds());
		}
		if (dt.getDate() < 10) {
			nDaysToAdd = "0" + dt.getDate();
		} else {
			nDaysToAdd = "" + dt.getDate();
		}
		if (dt.getMonth() < 10) {
			nMonthsToAdd = "0" + (dt.getMonth() + 1);
		} else {
			nMonthsToAdd = "" + (dt.getMonth() + 1);
		}
		
		st += nHoursToAdd + ":" + nMinutesToAdd + ":" + nSecondsToAdd;
		dtStr += nDaysToAdd + "/" + nMonthsToAdd + "/" + (dt.getYear() + 1900);		 
	}
	/*public Result index() {
		if (session().get("name") == null || session().get("name") == "") {
			session().put("name", "");
		}
		return redirect("/main");
	}*/
	public Result login(String username, String pass) {
		Date dt=  new Date();
		getConn();
		if (con != null) {
			try {
		    Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT * FROM t_users");
		    while (rs.next()) {
		    	String iusername = rs.getString("username");
		    	String ipass = rs.getString("pass");
		    	if (username.compareTo(iusername) == 0 && pass.compareTo(ipass) == 0) {
		    		session().put("user", rs.getString("username"));
					session().put("pass", rs.getString("pass"));
					session().put("name", rs.getString("firstname")); 
					session().put("lastname", rs.getString("lastname"));
					session().put("email", rs.getString("email"));
		    		session().put("perm", rs.getString("perm"));
		    		return ok(rs.getString("firstname"));
		    	}
		    }
		    }catch(Exception ex){
		    }
		}
		return ok("");
	}
	
	public Result logout() {
		session().put("user", "");
		session().put("pass", "");
		session().put("name", ""); 
		session().put("lastname", "");
		session().put("email", "");
		session().put("perm", "");
		return ok("good");
	}
	
	/************ALL NEWS METHODS*************/
	public Result addNews(String message) {
		getConn();
		if (con != null) {
			int nIndex = 0;
			try {
		    Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT nextval('news_seq')");
		    while (rs.next()) {
		    	nIndex = rs.getInt("nextval");
		    }
	
			Filldtst();		 
			int nRows = stmt.executeUpdate("INSERT INTO t_news values("+ nIndex +", '"+session().get("name")+"','"+message+"', '"+dtStr+"', '"+st+"')");
			
		    if (nRows > 0) {
		    	Message msg = new Message(nIndex, session().get("name"), message, dtStr, st);
		    	return ok(Json.toJson(msg));
		    }
		    
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		}
		return ok("BAD IN SERVER (NO CONNECTION TO DB)");
	}
	
	public Result getAllNews() {
		getConn();
		if (con != null) {
			try {
				List<Message> lstMessages = new ArrayList<Message>();
			    Statement stmt = con.createStatement();
			    ResultSet rs = stmt.executeQuery("SELECT * FROM t_news");
			    while (rs.next()) {
			    	int id = rs.getInt("id");
			    	String author_name = rs.getString("author_name");
			    	String describtion = rs.getString("describtion");
			    	String date		   = rs.getString("date");
			    	String time		   = rs.getString("time");
			    	Message msg = new Message(id,  author_name, describtion, date, time);
			    	lstMessages.add(msg);
			    }
			    return ok(Json.toJson(lstMessages));
			    
		    }catch(Exception ex){
		    }
		}
		return ok();
	}
	
	public Result deleteANew(int id) {
		getConn();
		if (con != null) {
			try {
			    Statement stmt = con.createStatement();
			    int nRows = stmt.executeUpdate("DELETE FROM t_news WHERE id = "+id);
			    return ok(Integer.toString(nRows));
			    
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		}
		return ok();
	}
	
	public Result updateNew(int id) {
		return ok();
	}
	/***************ALL NEWS METHODS END!!***************/
	
	/***************ALL PICS METHODS*****************/
	public Result getAllPics() {
		getConn();
		if (con != null) {
			try {
				List<Picture> lstPictures = new ArrayList<Picture>();
			    Statement stmt = con.createStatement();
			    ResultSet rs = stmt.executeQuery("SELECT * FROM t_gallery");
			    while (rs.next()) {
			    	int id = rs.getInt("id");
			    	String src = rs.getString("src");
			    	String info = rs.getString("info");
			    	String title = rs.getString("title");
			    	String date = rs.getString("date");
			    	String time = rs.getString("time");
			    	Picture pic = new Picture(id,  src, info, title, date, time);
			    	lstPictures.add(pic);
			    }
			    return ok(Json.toJson(lstPictures));
			    
		    }catch(Exception ex){
		    }
		}
		return ok("ERR");
	}
	
	public Result addPicture(String info, String title) {
		getConn();
		if (con != null) {
			try {
				int       nIndex = 0;
			    Statement stmt   = con.createStatement();
			    ResultSet rs     = stmt.executeQuery("SELECT nextval('gallery_seq')");
			    
			    while (rs.next()) {
			    	nIndex = rs.getInt("nextval");
			    }
			    
			    String src = "/assets/images/gallery/pic" + Integer.toString(nIndex) + ".jpg";
			    Filldtst();
			    int nRow = stmt.executeUpdate("INSERT INTO t_gallery values("+nIndex+", '"+src+"', '"+info+"', '"+title+"', '"+dtStr+"', '"+st+"')");
			    if (nRow > 0) {
			    	return ok("התמונה התווספה בהצלחה");
			    }
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		}
		return ok("ERR");
	}
	
	////////////////////////////////
	public Result removePicture(int id) {
		return ok();
	}
	////////////////////////////////
	
	////////////////////////////////
	public Result updatePicture(int id) {
		return ok();
	}
	////////////////////////////////
	/***************ALL PICS METHODS END!!**************/
	
	@SuppressWarnings("deprecation")
	public Result register(String name, String lastname, String email,
						   String user, String pass, String passconf) {
		/*DynamicForm requestData = Form.form().bindFromRequest();
		String name  	= requestData.get("name");
		String lastname = requestData.get("lastname");
		String email 	= requestData.get("email");
		String user  	= requestData.get("user");
		String pass  	= requestData.get("pass");
		String passconf = requestData.get("passconf");
		System.out.println("Name = " + name + "\n"
						 + "Email = " + email);*/
		
		getConn();
		if (con != null) {
			int nIndex = 0;
			try {
		    Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT nextval('users_seq')");
		    while (rs.next()) {
		    	nIndex = rs.getInt("nextval");
		    }
		    
		    rs = stmt.executeQuery("SELECT * FROM t_users");
		    while (rs.next()) {
		    	String username = rs.getString("username");
		    	if (username.compareTo(user) == 0) {
		    		return ok("שם משתמש כבר קיים");
		    	}
		    }
		    
		    int nRows = stmt.executeUpdate("INSERT INTO t_users values("+nIndex+", '"+user+"', '"+pass+"', '"+name+"' ,'"+lastname+"', '"+email+"', 1)");
		    
		    if (nRows > 0){
		    	return ok("המשתמש נוצר בהצלחה");
		    }
		    
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		}
		
		return ok("BAD IN SERVER");
	}
	
	public Result getSession() {
		if (session().get("user") != "") {
			User usr = new User(session().get("user"),
							    session().get("pass"),
							    session().get("name"), 
							    session().get("lastname"), 
							    session().get("email"),
							    Integer.parseInt(session().get("perm")));
			return ok(Json.toJson(usr));
		} else {
			return ok();
		}
	}
}