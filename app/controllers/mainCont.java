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

import models.Chat;
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
	
	public Result getSession() {
		User usr = new User(Integer.parseInt(session().get("id")),      session().get("user"), 
											 session().get("pass"),	    session().get("name"), 
											 session().get("lastname"), session().get("email"),
							Integer.parseInt(session().get("perm")),    session().get("profilepic"));
		return ok(Json.toJson(usr));
	}
}