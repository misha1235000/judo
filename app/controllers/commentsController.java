package controllers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Comment;
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
public class commentsController extends Controller {
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Result get(String id) {
		globals.getConn();
		if (globals.con != null) {
			List<Comment> lstComments = new ArrayList<Comment>();
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery("SELECT * FROM t_comments");

				while (rs.next()) {
					if (rs.getString("pic_id").compareTo(id) == 0) {
						Comment comment = new Comment(rs.getInt("id"), rs.getInt("poster_id"), rs.getString("pic_id"),
								rs.getString("postername"), rs.getString("posterlastname"), rs.getString("comment"),
								rs.getString("date"), rs.getString("time"), rs.getString("src"));
						lstComments.add(comment);
					}
				}
				
				return ok(Json.toJson(lstComments));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return badRequest();
	}

	/**
	 * 
	 * @return
	 */
	public Result add() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String id  	= requestData.get("id");
		String comment = requestData.get("comment");
		
		globals.getConn();
		if (globals.con != null) {
			int nIndex = 0;
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nextval('comments_seq')");
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}

				globals.Filldtst();

				int nRows = stmt.executeUpdate("INSERT INTO t_comments values(" + nIndex + ", "
						+ Integer.parseInt(session().get("id")) + "," + id + ", '" + session().get("name") + "', '"
						+ session().get("lastname") + "', '" + globals.dtStr + "', '" + globals.st + "', '" + comment + "', '"+session().get("profilepic")+"')");

				if (nRows > 0) {
					Comment cmt = new Comment(nIndex, Integer.parseInt(session().get("id")), id, session().get("name"),
							session().get("lastname"), comment, globals.dtStr, globals.st, session().get("profilepic"));
					return ok(Json.toJson(cmt));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return badRequest();
	}
	
	public Result listen(String id) {
		if (id.compareTo("0") == 0) {
			session().put("countcom", "0");
		} else {
		
		int nCountNow = 0;
		int nCountComments = Integer.parseInt(session().get("countcom"));
		globals.getConn();
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery("SELECT * FROM t_comments");

				while (rs.next()) {
					if (rs.getString("pic_id").compareTo(id) == 0) {
						nCountNow++;
					}
				}
				
				String isNew = "old";
				
				if (nCountComments != nCountNow) {
					isNew = "new";
					session().put("countcom", Integer.toString(nCountNow));
				}
				
				return ok(isNew);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		}
		return badRequest();
	}
}