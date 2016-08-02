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

/**
 * The upload Controller
 * 
 * @author Michael Tsirulnikov
 *
 */
public class uploadController extends Controller {
	/**
	 * This function uploads the profile picture and saves it.
	 * @return Result
	 */
	public Result pic() {
		// Gets the needed data from the request.
		DynamicForm requestData = Form.form().bindFromRequest();
		String src = requestData.get("src");
		
		// Gets the DB connection
		globals.getConn();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			Statement stmt = null;
			int nRows = 0;
			
			try {
				stmt = globals.con.createStatement();
				
				// Updates the profilepic.
				nRows = stmt.executeUpdate("UPDATE t_users SET profilepic = '"+src+"' "
										 + "WHERE id = "
										 + Integer.parseInt(session().get("id")));
				
				// Checks if the row has been affected.
				if (nRows > 0) {
					session().put("profilepic", src);
					return ok();
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
	 * This function uploads the picture and saves the post.
	 * @return Result
	 */
	public Result post() {
		// Gets the needed data from the request.
		DynamicForm requestData = Form.form().bindFromRequest();
		String 		title		= requestData.get("title");
		String 		desc 		= requestData.get("desc");
		String 		src 		= requestData.get("src");
		int	   		isvideo 	= Integer.parseInt(requestData.get("isvideo"));
		int 		nRows;
		
		// Gets the connection from the DB.
		globals.getConn();
		globals.Filldtst();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			int nIndex = 0;
			
			try {
				stmt = globals.con.createStatement();
				
				// Gets the nextval of the sequence.
				rs = stmt.executeQuery("SELECT nextval('gallery_seq')");
				
				// Gets the value of the output.
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}
				
				// Inserts the needed data into t_gallery.
				nRows = stmt.executeUpdate("INSERT INTO t_gallery values(" 
										  + nIndex + ", '"+src+"','"+desc+"', '"
										  + title +"', '" + globals.dtStr + "', '"
										  + globals.st + "', " 
										  + Integer.parseInt(session().get("id")) + ", '"
										  + session().get("name") + "', '" 
										  + session().get("lastname") + "', "
										  + isvideo +", '"
										  + session().get("profilepic")+ "')");
				
				// Checks if rows has been affected.
				if (nRows > 0) {
					Picture pic = new Picture(nIndex, src, desc, title,
											  globals.dtStr, globals.st, 
											  Integer.parseInt(session().get("id")),
											  session().get("name"),
											  session().get("lastname"),
											  isvideo, session().get("profilepic"));

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