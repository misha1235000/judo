package controllers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Picture;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The pics controller.
 * 
 * @author Michael Tsirulnikov
 *
 */
public class picsController extends Controller {
	/**
	 * 
	 * @return
	 */
	public Result getAll() {
		globals.getConn();
		if (globals.con != null) {
			try {
				List<Picture> lstPictures = new ArrayList<Picture>();
				Statement stmt = globals.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM t_gallery");
				while (rs.next()) {
					int id = rs.getInt("id");
					String src = rs.getString("src");
					String info = rs.getString("info");
					String title = rs.getString("title");
					String date = rs.getString("date");
					String time = rs.getString("time");
					int posterId = rs.getInt("poster_id");
					String posterName = rs.getString("poster_name");
					String posterLastName = rs.getString("poster_lastname");
					int isvideo = rs.getInt("isvideo");
					String posterimg = rs.getString("posterimg");
					Picture pic = new Picture(id, src, info, title, date,
											  time, posterId, posterName,
											  posterLastName, isvideo, posterimg);
					lstPictures.add(pic);
				}
				return ok(Json.toJson(lstPictures));

			} catch (Exception ex) {
			}
		}
		return ok("ERR");
	}
	
	public Result delete() {
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}
		
		DynamicForm requestData = Form.form().bindFromRequest();
		String id = requestData.get("id");
		globals.getConn();
		
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				int nRow = stmt.executeUpdate("DELETE FROM t_gallery WHERE id ='" + id + "'");
				if (nRow > 0) {
					return ok();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return badRequest();
	}
}