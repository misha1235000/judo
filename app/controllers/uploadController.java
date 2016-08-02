package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Picture;
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
public class uploadController extends Controller {
	/**
	 * 
	 * @return
	 */
	public Result uploadprof() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String src = requestData.get("src");
		globals.getConn();
		
		if (globals.con != null) {
			Statement stmt = null;
			int nRows = 0;
			
			try {
				stmt = globals.con.createStatement();
				nRows = stmt.executeUpdate("UPDATE t_users SET profilepic = '"+src+"' WHERE id = "+Integer.parseInt(session().get("id")));
				if (nRows > 0) {
					session().put("profilepic", src);
					return ok("GOOD");
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception ex) {
			}
		}
		
		return badRequest();
	}
	
	/**
	 * 
	 * @return
	 */
	public Result upload() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String title = requestData.get("title");
		String desc = requestData.get("desc");
		String src = requestData.get("src");
		int	   isvideo = Integer.parseInt(requestData.get("isvideo"));
		
		globals.getConn();
		globals.Filldtst();
		int nRows;
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			int nIndex = 0;
			
			try {
				stmt = globals.con.createStatement();
				rs = stmt.executeQuery("SELECT nextval('gallery_seq')");
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}
				nRows = stmt.executeUpdate("INSERT INTO t_gallery values(" + nIndex + ", '"+src+"','"+desc+"', '"+title+"', '"
						+ globals.dtStr + "', '" + globals.st + "', " + Integer.parseInt(session().get("id")) + ", '"
						+ session().get("name") + "', '" + session().get("lastname") + "', "+isvideo+", '"+session().get("profilepic")+"')");
				if (nRows > 0) {
					Picture pic = new Picture(nIndex, src,
							desc, title, globals.dtStr, globals.st, Integer.parseInt(session().get("id")), session().get("name"),
							session().get("lastname"), isvideo, session().get("profilepic"));

					return ok(Json.toJson(pic));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception ex) {
			}
		}
		
		return badRequest();
	}
}