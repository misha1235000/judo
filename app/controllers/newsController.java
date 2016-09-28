package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Message;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The news controller.
 * 
 * @author Michael Tsirulnikov
 *
 */
public class newsController extends Controller {
	/**
	 * The addition of news function.
	 * @return Result
	 */
	public Result add() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String message  	= requestData.get("message");
		if (Integer.parseInt(session().get("perm")) < 2) {
			return unauthorized();
		}

		globals.getConn();
		if (globals.con != null) {
			int nIndex = 0;
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nextval('news_seq')");
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}

				globals.Filldtst();
				int nRows = stmt.executeUpdate("INSERT INTO t_news values(" + nIndex + ", '" + session().get("name")
						+ "','" + message + "', '" + globals.dtStr + "', '" + globals.st + "')");

				if (nRows > 0) {
					Message msg = new Message(nIndex, session().get("name"), message, globals.dtStr, globals.st);
					return ok(Json.toJson(msg));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return badRequest();
	}

	/**
	 * This function gets all the news.
	 * @return Result
	 */
	public Result get() {
		// Gets the DB connection
		globals.getConn();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			try {
				List<Message> lstMessages = new ArrayList<Message>();
				Statement stmt = globals.con.createStatement();
				
				// Gets all the news from the DB.
				ResultSet rs = stmt.executeQuery("SELECT * FROM t_news");
				
				// Runs on all the output of the DB news and saves adds each one
				// to the list of the news.
				while (rs.next()) {
					int     id		    = rs.getInt("id");
					String  author_name = rs.getString("author_name");
					String  describtion = rs.getString("describtion");
					String  date 	    = rs.getString("date");
					String  time 	    = rs.getString("time");
					Message msg 		= new Message(id, author_name,
													  describtion, date, time);
					lstMessages.add(msg);
				}
				
				// Returns the list of the messages in json.
				return ok(Json.toJson(lstMessages));
			} catch (Exception ex) {
			}
		}
		
		return badRequest();
	}

	/**
	 * This function deletes a new.
	 * @return Result
	 */
	public Result delete() {
		// Gets the needed data from the request.
		DynamicForm requestData = Form.form().bindFromRequest();
		int id = Integer.parseInt(requestData.get("id"));
		
		// Checks whether the user has permission to delete a new.
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		// Gets the DB connection
		globals.getConn();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				
				// Deletes the needed new.
				int nRows = stmt.executeUpdate("DELETE FROM t_news WHERE id = " + id);
				
				return ok(Integer.toString(nRows));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return badRequest();
	}
	
	/**
	 * This function listens to any change in the news.
	 * 
	 * @return
	 */
	public Result listen() {
		DynamicForm requestData = Form.form().bindFromRequest();
		int amount = Integer.parseInt(requestData.get("amount"));
		globals.getConn();
		
		if (globals.con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				int currAmount = 0;
				stmt = globals.con.createStatement();
				List<Message> lstMsg = new ArrayList<Message>();
				rs = stmt.executeQuery("SELECT * FROM t_news");
				while (rs.next()) {
					currAmount++;
					Message msg = new Message(rs.getInt("id"),
											  rs.getString("author_name"),
											  rs.getString("describtion"), 
											  rs.getString("date"),
											  rs.getString("time"));
					lstMsg.add(msg);
				}
				
				if (currAmount > amount) {
					return ok(Json.toJson(lstMsg.get(currAmount - 1)));
				} else {
					return ok("bad");
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
			    	e.printStackTrace();
			} catch (Exception ex) {
			}
		}
		
		return ok("BAD IN SERVER");
	}
}
