package controllers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Chat;
import models.ChatListen;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/***
 * 
 * @author Michael Tsirulnikov
 *
 */
public class chatController extends Controller {
	/**
	 * 
	 * @return
	 */
	public Result send() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String msg = requestData.get("msg");
		int msgto = Integer.parseInt(requestData.get("msgto"));
		String msgs = "";
		
		globals.getConn();
		globals.Filldtst();
		int nRows;
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			int index = 0;
			int amount = 0;
			try {
				stmt = globals.con.createStatement();
				rs = stmt.executeQuery("SELECT nextval('chat_seq')");
				while (rs.next()) {
					index = rs.getInt("nextval");
				}
				rs = stmt.executeQuery("SELECT newmsg FROM t_users WHERE id = " + msgto);
				
				while (rs.next()) {
					msgs = rs.getString("newmsg");
				}
				
				msgs += ", " + session().get("id");
				nRows = stmt.executeUpdate("INSERT INTO t_chat values("+index+", "+Integer.parseInt(session().get("id"))+", "+msgto+", '"+globals.dtStr+"', '"+globals.st+"', '"+msg+"', '"+session().get("profilepic")+"')");
				
				if (nRows > 0) {
					rs = stmt.executeQuery("SELECT amount FROM t_msgs WHERE msgto = "+msgto+" AND msgfrom = "+ session().get("id"));
					while(rs.next()) {
						amount = rs.getInt("amount");
					}
					if (amount == 0) {
						nRows = stmt.executeUpdate("INSERT INTO t_msgs values("+Integer.parseInt(session().get("id"))+", "+msgto+", 1)");
					} else {
						nRows = stmt.executeUpdate("UPDATE t_msgs SET amount = " + (amount + 1) + " WHERE msgto = "+msgto);
					}
					nRows = stmt.executeUpdate("UPDATE t_users SET newmsg='"+msgs+"' WHERE id = "+msgto);
					return ok(Json.toJson(new Chat(index, Integer.parseInt(session().get("id")), msgto, globals.dtStr, globals.st, msg, session().get("profilepic"))));
				}
				
			 } catch(Exception ex) {
				ex.printStackTrace();
			 }
		}
		return badRequest();
	}
	
	/**
	 * 
	 * @return
	 */
	public Result get() {
		DynamicForm requestData = Form.form().bindFromRequest();
		int msgto = Integer.parseInt(requestData.get("msgto"));
		
		globals.getConn();
		globals.Filldtst();
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			List<Chat> lstChat = new ArrayList<Chat>();
			try {
				stmt = globals.con.createStatement();
				rs = stmt.executeQuery("SELECT * from t_chat where (msgfrom = "+Integer.parseInt(session().get("id"))+" and msgto ="+msgto+") or (msgfrom = "+msgto+" and msgto = "+Integer.parseInt(session().get("id"))+")");
				while (rs.next()) {
					Chat currcht = new Chat(rs.getInt("id"),      rs.getInt("msgfrom"), 
							                rs.getInt("msgto"),   rs.getString("date"), 
								            rs.getString("time"), rs.getString("msg"), rs.getString("profilepic"));
					lstChat.add(currcht);
				}

					return ok(Json.toJson(lstChat));
			 } catch(Exception ex) {
				ex.printStackTrace();
			 }
		}
		return badRequest();
	}
	
	public Result listen() {
		globals.getConn();
		
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			String	  msgs = "";
			
			try {
				List<ChatListen> lstChatListen = new ArrayList<ChatListen>();
				stmt = globals.con.createStatement();
			//	rs = stmt.executeQuery("SELECT newmsg FROM t_users WHERE id = "+Integer.parseInt(session().get("id")));
				rs = stmt.executeQuery("SELECT * FROM t_msgs WHERE msgto = "+session().get("id"));
				
			while (rs.next()) {
				ChatListen cl = new ChatListen(rs.getInt("msgfrom"), rs.getInt("msgto"),
						   	                   rs.getInt("amount"));
				lstChatListen.add(cl);
			}
			
			return ok(Json.toJson(lstChatListen));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return badRequest();
	}
	
	public Result read() {
		globals.getConn();
		
		if (globals.con != null) {
			Statement stmt = null;
			String	  msgs = "";
			int 	  nRows = 0;
			
			try {
				stmt = globals.con.createStatement();
				nRows = stmt.executeUpdate("update t_users set newmsg = '' where id = "+session().get("id"));
				return ok();
			
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return badRequest();
	}
}