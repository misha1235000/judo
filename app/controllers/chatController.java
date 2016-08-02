package controllers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Chat;
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
	public Result chatSend() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String msg = requestData.get("msg");
		int msgto = Integer.parseInt(requestData.get("msgto"));
		
		globals.getConn();
		globals.Filldtst();
		int nRows;
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			int index = 0;
			try {
				stmt = globals.con.createStatement();
				rs = stmt.executeQuery("SELECT nextval('chat_seq')");
				while (rs.next()) {
					index = rs.getInt("nextval");
				}
				nRows = stmt.executeUpdate("INSERT INTO t_chat values("+index+", "+Integer.parseInt(session().get("id"))+", "+msgto+", '"+globals.dtStr+"', '"+globals.st+"', '"+msg+"', '"+session().get("profilepic")+"')");
				
				if (nRows > 0) {
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
	public Result chat() {
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
}