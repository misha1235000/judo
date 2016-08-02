package controllers;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/***
 * 
 * @author Michael Tsirulnikov
 * The user controller.
 */
public class userController extends Controller {
	/**
	 * Deletes a user.
	 * @return Result
	 */
	public Result deleteUser() {
		// Gets the requested data from the form.
		DynamicForm requestData = Form.form().bindFromRequest();
		int id = Integer.parseInt(requestData.get("id"));
		
		// Checks whether the user is authorized to delete a user.
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		// Gets the DB connection.
		globals.getConn();
		
		// Checks if the DB connected.
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				
				// Deletes the needed user according to the id.
				int nRows = stmt.executeUpdate("DELETE FROM t_users WHERE id = " + id);
				
				// Returns the rows that have changed
				return ok(Integer.toString(nRows));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return badRequest();
	}
	
	/**
	 * Updates a user.
	 * @return Result
	 */
	public Result updateProfile() {
		// Gets the requested data from the form.
		DynamicForm requestData = Form.form().bindFromRequest();
		String      name 		= requestData.get("name");
		String 		lastname 	= requestData.get("lastname");
		String 		email 		= requestData.get("email");
		String 		pass 		= requestData.get("pass");
		String 		passconf 	= requestData.get("passconf");
		
		// Checks whether the passwords are equal.
		if (pass.compareTo(passconf) != 0) {
			return ok("סיסמאות לא זהות");
		}
		
		// Gets the DB connection
		globals.getConn();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs;
				
				// Gets all the users.
				rs = stmt.executeQuery("SELECT * FROM t_users");

				// Running on all the rows and checking if the email already exists.
				while (rs.next()) {
					String myemail = rs.getString("email");
					if (myemail.compareTo(email) == 0 && email.compareTo(session().get("email")) != 0) {
						return ok("דואר אלקטרוני כבר קיים");
					}
				}

				int id = Integer.parseInt(session().get("id"));
				
				// Updates the needed rows in the DB.
				int nRows = stmt.executeUpdate("UPDATE t_users SET(pass, firstname, lastname, email) = ('" + pass
						+ "', '" + name + "' ,'" + lastname + "', '" + email + "') WHERE id = " + id);

				// If the change succeed.
				if (nRows > 0) {
					return ok("המשתמש עודכן בהצלחה");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return badRequest();
	}
	
	/**
	 * Gets a specific user according to the given id.
	 * @param id - the id of the user
	 * @return Result
	 */
	public Result getUser(int id) {
		// Checks whether the user is authorized to get a user.
		if (session().get("name").compareTo("") == 0) {
			return unauthorized();
		}
		
		// Gets the DB connection.
		globals.getConn();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs;
				
				// Gets the needed user in sql query.
				rs = stmt.executeQuery("SELECT * FROM t_users WHERE id = " + id);
				User usr = null;
				
				// Creating the user, to return it afterwards.
				while (rs.next()) {
					usr = new User(rs.getInt("id"), "", "",
							rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"),
							rs.getInt("perm"), rs.getString("profilepic"));
				}

				return ok(Json.toJson(usr));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return badRequest();
	}
	
	/**
	 * Gets all the users.
	 * @return
	 */
	public Result getAllUsers() {
		// Checks whether the user is authorized to get all users.
		if (session().get("name").compareTo("") == 0) {
			return unauthorized();
		}

		// Gets the DB connection.
		globals.getConn();
		
		// Checks if the DB is connected.
		if (globals.con != null) {
			List<User> lstUsers = new ArrayList<User>();
			try {
				Statement stmt = globals.con.createStatement();
				ResultSet rs;
				
				// Gets all the users.
				rs = stmt.executeQuery("SELECT * FROM t_users");

				// Runs on all the users and adds them to the arraylist.
				while (rs.next()) {
					User usr = new User(rs.getInt("id"), "", "",
							rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"),
							rs.getInt("perm"), rs.getString("profilepic"));
					lstUsers.add(usr);
				}

				// Returns the list of the users in json.
				return ok(Json.toJson(lstUsers));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return badRequest();
	}
}