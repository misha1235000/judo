package controllers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/***
 * 
 * @author Michael Tsirulnikov
 * Authentication controller.
 *
 */
public class authenticationController extends Controller {
	/**
	 * 
	 * @return
	 */
	public Result login() {
		session().put("countcom", "0");
		DynamicForm requestData = Form.form().bindFromRequest();
		String username  	= requestData.get("username");
		String pass = requestData.get("pass");
		
		Date dt = new Date();
		globals.getConn();
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM t_users");
				while (rs.next()) {
					String iusername = rs.getString("username");
					String ipass = rs.getString("pass");
					if (username.toUpperCase().compareTo(iusername.toUpperCase()) == 0 && pass.compareTo(ipass) == 0) {
						session().put("id", Integer.toString(rs.getInt("id")));
						session().put("user", rs.getString("username"));
						session().put("pass", rs.getString("pass"));
						session().put("name", rs.getString("firstname"));
						session().put("lastname", rs.getString("lastname"));
						session().put("email", rs.getString("email"));
						session().put("perm", rs.getString("perm"));
						session().put("profilepic", rs.getString("profilepic"));
						session().put("newmsg", rs.getString("newmsg"));
						return ok(rs.getString("firstname"));
					}
				}
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
	public Result logout() {
		session().put("id", "0");
		session().put("user", "");
		session().put("pass", "");
		session().put("name", "");
		session().put("lastname", "");
		session().put("email", "");
		session().put("perm", "0");
		session().put("profilepic", "");
		session().put("newmsg", "0");
		return ok("good");
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Result register() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String name = requestData.get("name");
		String lastname = requestData.get("lastname");
		String email = requestData.get("email");
		String user = requestData.get("username");
		String pass = requestData.get("pass");
		globals.getConn();
		
		if (globals.con != null) {
			int nIndex = 0;
			try {
				Statement stmt = globals.con.createStatement();
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
				
				int nRows = stmt.executeUpdate("INSERT INTO t_users values(" + nIndex + ", '" + user + "', '" + pass
						+ "', '" + name + "' ,'" + lastname + "', '" + email + "', 1, '/assets/images/profile/unknown.jpg'), 0");

				if (nRows > 0) {
					return ok("המשתמש נוצר בהצלחה");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return badRequest();
	}
	
	/**
	 * 
	 * @param id
	 * @param perm
	 * @return
	 */
	public Result setPerm(int id, int perm) {
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		globals.getConn();
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				int nRows = stmt.executeUpdate("UPDATE t_users SET(perm) = (" + perm + ") WHERE id = " + id);

				if (nRows > 0) {
					if (id == Integer.parseInt(session().get("id"))) {
						session().put("perm", Integer.toString(perm));
					}
					return ok("המשתמש עודכן בהצלחה");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return badRequest();
	}
}