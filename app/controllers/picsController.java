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

public class picsController extends Controller {
	public Result getAllPics() {
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
					Picture pic = new Picture(id, src, info, title, date, time, posterId, posterName, posterLastName, isvideo, posterimg);
					lstPictures.add(pic);
				}
				return ok(Json.toJson(lstPictures));

			} catch (Exception ex) {
			}
		}
		return ok("ERR");
	}

	public Result addPicture() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String info  	= requestData.get("[0");
		String title = requestData.get("[1");
		globals.getConn();
		if (globals.con != null) {
			try {
				int nIndex = 0;
				Statement stmt = globals.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nextval('gallery_seq')");

				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}

				String src = "/assets/images/gallery/pic" + Integer.toString(nIndex) + ".png";
				globals.Filldtst();
				int nRow = stmt.executeUpdate("INSERT INTO t_gallery values(" + nIndex + ", '" + src + "', '" + info
						+ "', '" + title + "', '" + globals.dtStr + "', '" + globals.st + "')");
				if (nRow > 0) {
					return ok("התמונה התווספה בהצלחה");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ok("ERR");
	}
}
