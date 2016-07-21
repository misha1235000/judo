package controllers;

import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.h2.engine.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;

import models.Comment;
import models.Message;
import models.Picture;
import models.User;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.FormFactoryModule;
import play.libs.Json;
import play.mvc.*;

public class mainCont extends Controller {
	// C:
	Connection con = null;
	String st = "";
	String dtStr = "";
	String nHoursToAdd;
	String nMinutesToAdd;
	String nSecondsToAdd;
	String nDaysToAdd;
	String nMonthsToAdd;
	
	private void getConn() {
		if (con == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			try {////
				Properties props = new Properties();
				props.setProperty("user", "judorsa_1440");
				props.setProperty("password", "dvpuX2KrnZDJAoI--T5u");
				props.setProperty("sslmode", "disable");
	//		 con = DriverManager.getConnection(
	//			 "jdbc:postgresql://judorsa-1440.postgresql.dbs.appsdeck.eu:30556/judorsa_1440", props);
				// con = DriverManager.getConnection(
				// "jdbc:postgresql://judorsa-1440.postgresql.dbs.appsdeck.eu:30556/judorsa_1440",
				// "judorsa_1440", "dvpuX2KrnZDJAoI--T5u");
				// con = DriverManager.getConnection(
				// "jdbc:postgresql://judorsa-1440.postgresql.dbs.appsdeck.eu:30556/judorsa_1440?user=judorsa_1440&password=dvpuX2KrnZDJAoI--T5u&ssl=false");
		con = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:10000/judorsa_1440", props);
				/*
				 */
				// con = DriverManager.getConnection(
				// "jdbc:postgresql://adminukvryp3:UDrcy6LHnakS@judonow-meitav.rhcloud.com:5432/judonow");
				// con = DriverManager.getConnection(
				// "jdbc:postgresql://localhost/mydb", "postgres", "postgres");
				if (con != null) {
					System.out.println("connected!@~#!@#@~!$~$~@!~(*");
				} else {
					System.out.println("NOT CONNECTED AT ALL FOK FOK FOK");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void Filldtst() {
		Date dt = new Date();
		if (dt.getHours() < 10) {
			nHoursToAdd = "0" + dt.getHours();
		} else {
			nHoursToAdd = Integer.toString(dt.getHours());
		}
		if (dt.getMinutes() < 10) {
			nMinutesToAdd = "0" + dt.getMinutes();
		} else {
			nMinutesToAdd = Integer.toString(dt.getMinutes());
		}
		if (dt.getSeconds() < 10) {
			nSecondsToAdd = "0" + dt.getSeconds();
		} else {
			nSecondsToAdd = Integer.toString(dt.getSeconds());
		}
		if (dt.getDate() < 10) {
			nDaysToAdd = "0" + dt.getDate();
		} else {
			nDaysToAdd = "" + dt.getDate();
		}
		if (dt.getMonth() < 10) {
			nMonthsToAdd = "0" + (dt.getMonth() + 1);
		} else {
			nMonthsToAdd = "" + (dt.getMonth() + 1);
		}

		st = nHoursToAdd + ":" + nMinutesToAdd + ":" + nSecondsToAdd;
		dtStr = nDaysToAdd + "/" + nMonthsToAdd + "/" + (dt.getYear() + 1900);
	}
	/*
	 * public Result index() { if (session().get("name") == null ||
	 * session().get("name") == "") { session().put("name", ""); } return
	 * redirect("/main"); }
	 */

	public Result index() {
		if (session().get("id") == null) {
			session().put("id", "0");
			session().put("user", "");
			session().put("pass", "");
			session().put("name", "");
			session().put("lastname", "");
			session().put("email", "");
			session().put("perm", "0");
		}
		return redirect("/main");
	}

	public Result login() {
		session().put("countcom", "0");
		DynamicForm requestData = Form.form().bindFromRequest();
		String username  	= requestData.get("username");
		String pass = requestData.get("pass");
		
		Date dt = new Date();
		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM t_users");
				while (rs.next()) {
					String iusername = rs.getString("username");
					String ipass = rs.getString("pass");
					if (username.compareTo(iusername) == 0 && pass.compareTo(ipass) == 0) {
						session().put("id", Integer.toString(rs.getInt("id")));
						session().put("user", rs.getString("username"));
						session().put("pass", rs.getString("pass"));
						session().put("name", rs.getString("firstname"));
						session().put("lastname", rs.getString("lastname"));
						session().put("email", rs.getString("email"));
						session().put("perm", rs.getString("perm"));
						session().put("profilepic", rs.getString("profilepic"));
						return ok(rs.getString("firstname"));
					}
				}
			} catch (Exception ex) {
			}
		}
		return ok("");
	}

	public Result logout() {
		session().put("id", "0");
		session().put("user", "");
		session().put("pass", "");
		session().put("name", "");
		session().put("lastname", "");
		session().put("email", "");
		session().put("perm", "0");
		session().put("profilepic", "");
		return ok("good");
	}

	/************ ALL NEWS METHODS *************/
	public Result addNews() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String message  	= requestData.get("message");
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		getConn();
		if (con != null) {
			int nIndex = 0;
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nextval('news_seq')");
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}

				Filldtst();
				int nRows = stmt.executeUpdate("INSERT INTO t_news values(" + nIndex + ", '" + session().get("name")
						+ "','" + message + "', '" + dtStr + "', '" + st + "')");

				if (nRows > 0) {
					Message msg = new Message(nIndex, session().get("name"), message, dtStr, st);
					return ok(Json.toJson(msg));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ok("BAD IN SERVER (NO CONNECTION TO DB)");
	}

	public Result getAllNews() {
		getConn();
		if (con != null) {
			try {
				List<Message> lstMessages = new ArrayList<Message>();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM t_news");
				while (rs.next()) {
					int id = rs.getInt("id");
					String author_name = rs.getString("author_name");
					String describtion = rs.getString("describtion");
					String date = rs.getString("date");
					String time = rs.getString("time");
					Message msg = new Message(id, author_name, describtion, date, time);
					lstMessages.add(msg);
				}
				return ok(Json.toJson(lstMessages));

			} catch (Exception ex) {
			}
		}
		return ok();
	}

	public Result deleteANew() {
		DynamicForm requestData = Form.form().bindFromRequest();
		int id = Integer.parseInt(requestData.get("id"));
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				int nRows = stmt.executeUpdate("DELETE FROM t_news WHERE id = " + id);
				return ok(Integer.toString(nRows));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ok();
	}

	public Result deleteUser() {
		DynamicForm requestData = Form.form().bindFromRequest();
		int id = Integer.parseInt(requestData.get("id"));
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				int nRows = stmt.executeUpdate("DELETE FROM t_users WHERE id = " + id);
				return ok(Integer.toString(nRows));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ok();
	}
	
	public Result updateNew() {
		DynamicForm requestData = Form.form().bindFromRequest();
		int id  = Integer.parseInt(requestData.get("[0"));
		return ok();
	}

	/*************** ALL NEWS METHODS END!! ***************/

	/*************** ALL PICS METHODS *****************/
	public Result getAllPics() {
		getConn();
		if (con != null) {
			try {
				List<Picture> lstPictures = new ArrayList<Picture>();
				Statement stmt = con.createStatement();
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
		getConn();
		if (con != null) {
			try {
				int nIndex = 0;
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nextval('gallery_seq')");

				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}

				String src = "/assets/images/gallery/pic" + Integer.toString(nIndex) + ".png";
				Filldtst();
				int nRow = stmt.executeUpdate("INSERT INTO t_gallery values(" + nIndex + ", '" + src + "', '" + info
						+ "', '" + title + "', '" + dtStr + "', '" + st + "')");
				if (nRow > 0) {
					return ok("התמונה התווספה בהצלחה");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ok("ERR");
	}

	////////////////////////////////
	public Result removePicture(int id) {
		return ok();
	}
	////////////////////////////////

	////////////////////////////////
	public Result updatePicture(int id) {
		return ok();
	}

	////////////////////////////////
	/*************** ALL PICS METHODS END!! **************/

	@SuppressWarnings("deprecation")
	public Result register() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String name = requestData.get("name");
		String lastname = requestData.get("lastname");
		String email = requestData.get("email");
		String user = requestData.get("username");
		String pass = requestData.get("pass");
		String passconf = requestData.get("passconf");
		getConn();
		if (con != null) {
			int nIndex = 0;
			try {
				Statement stmt = con.createStatement();
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
				/*
				 * String enc = ""; char a; for (int i = 0; i < pass.length();
				 * i++) { a = (char) (pass.charAt(i) ^ 'p'); enc += a; }
				 */

				int nRows = stmt.executeUpdate("INSERT INTO t_users values(" + nIndex + ", '" + user + "', '" + pass
						+ "', '" + name + "' ,'" + lastname + "', '" + email + "', 1, '/assets/images/profile/unknown.jpg')");

				if (nRows > 0) {
					return ok("המשתמש נוצר בהצלחה");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return ok("BAD IN SERVER");
	}

	public Result updateProfile() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String name = requestData.get("name");
		String lastname = requestData.get("lastname");
		String email = requestData.get("email");
		String pass = requestData.get("pass");
		String passconf = requestData.get("passconf");
		
		if (pass.compareTo(passconf) != 0) {
			return ok("סיסמאות לא זהות");
		}
		
		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery("SELECT * FROM t_users");

				while (rs.next()) {
					String myemail = rs.getString("email");
					if (myemail.compareTo(email) == 0 && email.compareTo(session().get("email")) != 0) {
						return ok("דואר אלקטרוני כבר קיים");
					}
				}

				int id = Integer.parseInt(session().get("id"));
				int nRows = stmt.executeUpdate("UPDATE t_users SET(pass, firstname, lastname, email) = ('" + pass
						+ "', '" + name + "' ,'" + lastname + "', '" + email + "') WHERE id = " + id);

				if (nRows > 0) {
					return ok("המשתמש עודכן בהצלחה");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return ok("BAD IN SERVER");
	}
	public Result getUser(int id) {
		if (session().get("name").compareTo("") == 0) {
			return unauthorized();
		}
		
		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery("SELECT * FROM t_users WHERE id = " + id);
				User usr = null;
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
	
	public Result getAllUsers() {
		if (session().get("name").compareTo("") == 0) {
			return unauthorized();
		}

		getConn();
		if (con != null) {
			List<User> lstUsers = new ArrayList<User>();
			try {
				Statement stmt = con.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery("SELECT * FROM t_users");

				while (rs.next()) {
					User usr = new User(rs.getInt("id"), "", "",
							rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"),
							rs.getInt("perm"), rs.getString("profilepic"));
					lstUsers.add(usr);
				}

				return ok(Json.toJson(lstUsers));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return badRequest();
	}

	public Result setPerm(int id, int perm) {
		if (Integer.parseInt(session().get("perm")) != 3) {
			return unauthorized();
		}

		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
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

		return ok("BAD IN SERVER");
	}
	public Result checkUpdate(String id) {
		if (id.compareTo("0") == 0) {
			session().put("countcom", "0");
		} else {
		
		int nCountNow = 0;
		int nCountComments = Integer.parseInt(session().get("countcom"));
		getConn();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
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
		return ok("BAD IN SERVER");
	}
	
	public Result getComments(String id) {
		getConn();
		if (con != null) {
			List<Comment> lstComments = new ArrayList<Comment>();
			try {
				Statement stmt = con.createStatement();
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
		
		return ok();
	}

	public Result addComment() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String id  	= requestData.get("id");
		String comment = requestData.get("comment");
		
		getConn();
		if (con != null) {
			int nIndex = 0;
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nextval('comments_seq')");
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}

				Filldtst();

				int nRows = stmt.executeUpdate("INSERT INTO t_comments values(" + nIndex + ", "
						+ Integer.parseInt(session().get("id")) + "," + id + ", '" + session().get("name") + "', '"
						+ session().get("lastname") + "', '" + dtStr + "', '" + st + "', '" + comment + "', '"+session().get("profilepic")+"')");

				if (nRows > 0) {
					Comment cmt = new Comment(nIndex, Integer.parseInt(session().get("id")), id, session().get("name"),
							session().get("lastname"), comment, dtStr, st, session().get("profilepic"));
					return ok(Json.toJson(cmt));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ok("BAD IN SERVER (NO CONNECTION TO DB)");
	}

	public Result check() {
		DynamicForm requestData = Form.form().bindFromRequest();
		int amount = Integer.parseInt(requestData.get("amount"));
		getConn();
		
		if (con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			String fileName = "";
			int nRows = 0;
			
			try {
				int currAmount = 0;
				stmt = con.createStatement();
				List<Message> lstMsg = new ArrayList<Message>();
				rs = stmt.executeQuery("SELECT * FROM t_news");
				while (rs.next()) {
					currAmount++;
					Message msg = new Message(rs.getInt("id"), rs.getString("author_name"),
											  rs.getString("describtion"), rs.getString("date"),
											  rs.getString("time"));
					lstMsg.add(msg);
				}
				
				if (currAmount > amount) {
					return ok(Json.toJson(lstMsg.get(currAmount - 1)));
				} else {
					return ok("bad");
				}
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			    	e.printStackTrace();
			} catch (Exception ex) {
			}
		}
		
		return ok("BAD IN SERVER");
	}
	
	public Result uploadprof() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String src = requestData.get("src");
		getConn();
		
		if (con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			String fileName = "";
			int nRows = 0;
			
			try {
				stmt = con.createStatement();
				nRows = stmt.executeUpdate("UPDATE t_users SET profilepic = '"+src+"' WHERE id = "+Integer.parseInt(session().get("id")));
				if (nRows > 0) {
					session().put("profilepic", src);
					return ok("GOOD");

					// return redirect("/");
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ex) {
			}
		}
		
		return ok("BAD IN SERVER");
	}
	
	public Result upload() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String title = requestData.get("title");
		String desc = requestData.get("desc");
		String src = requestData.get("src");
		int	   isvideo = Integer.parseInt(requestData.get("isvideo"));
		
		getConn();
		Filldtst();
		int nRows;
		if (con != null) {
			Statement stmt = null;
			ResultSet rs = null;
			String fileName = "";
			int nIndex = 0;
			/*
			 * MultipartFormData<File> body =
			 * request().body().asMultipartFormData(); FilePart<File> picture =
			 * body.getFile("picture");
			 */
			// if (picture != null) {
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery("SELECT nextval('gallery_seq')");
				while (rs.next()) {
					nIndex = rs.getInt("nextval");
				}
				nRows = stmt.executeUpdate("INSERT INTO t_gallery values(" + nIndex + ", '"+src+"','"+desc+"', '"+title+"', '"
						+ dtStr + "', '" + st + "', " + Integer.parseInt(session().get("id")) + ", '"
						+ session().get("name") + "', '" + session().get("lastname") + "', "+isvideo+", '"+session().get("profilepic")+"')");
				if (nRows > 0) {
					Picture pic = new Picture(nIndex, src,
							desc, title, dtStr, st, Integer.parseInt(session().get("id")), session().get("name"),
							session().get("lastname"), isvideo, session().get("profilepic"));

					return ok(Json.toJson(pic));

					// return redirect("/");
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ex) {
			}

			/*
			 * fileName = picture.getFilename(); String contentType =
			 * picture.getContentType(); File file = picture.getFile();
			 */
			// File dest = new File("/assets/images/mypic.jpg");
			// try {
			// Files.copy(file, dest);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			/*
			 * String uploadPath =
			 * Play.application().configuration().getString("upload.path",
			 * "/tmp/"); file.renameTo(new File(uploadPath + "pic" +
			 * Integer.toString(nIndex) + ".png"));
			 */
		}
		return ok("BAD IN SERVER");
	}

	public Result getSession() {
		User usr = new User(Integer.parseInt(session().get("id")), session().get("user"), session().get("pass"),
				session().get("name"), session().get("lastname"), session().get("email"),
				Integer.parseInt(session().get("perm")), session().get("profilepic"));
		return ok(Json.toJson(usr));
	}
}
