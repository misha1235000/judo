package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

/***
 * 
 * @author Michael Tsirulnikov
 *
 * the globals
 */
public class globals {
		public static Connection con     = null;
		public static String 	 st      = "";
		public static String 	 dtStr   = "";
		public static String 	 nHoursToAdd;
		public static String 	 nMinutesToAdd;
		public static String 	 nSecondsToAdd;
		public static String 	 nDaysToAdd;
		public static String 	 nMonthsToAdd;
		
		public static void getConn() {
			if (con == null) {
				try {
					Class.forName("org.postgresql.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				try {////
					Properties props = new Properties();
				/*	props.setProperty("user", "judorsa_1440");
					props.setProperty("password", "dvpuX2KrnZDJAoI--T5u");
					props.setProperty("sslmode", "disable");*/
					props.setProperty("user", "liorsjudo_8960");
					props.setProperty("password", "dcBIU8TiEfNngfG2g1ai");
					props.setProperty("sslmode", "disable");
				 con = DriverManager.getConnection(
					 "jdbc:postgresql://liorsjudo-8960.postgresql.dbs.appsdeck.eu:30853/liorsjudo_8960", props);
					
			/*	 con = DriverManager.getConnection(
					 "jdbc:postgresql://judorsa-1440.postgresql.dbs.appsdeck.eu:30556/judorsa_1440", props);*/
					// con = DriverManager.getConnection(
					// "jdbc:postgresql://judorsa-1440.postgresql.dbs.appsdeck.eu:30556/judorsa_1440",
					// "judorsa_1440", "dvpuX2KrnZDJAoI--T5u");
					// con = DriverManager.getConnection(
					// "jdbc:postgresql://judorsa-1440.postgresql.dbs.appsdeck.eu:30556/judorsa_1440?user=judorsa_1440&password=dvpuX2KrnZDJAoI--T5u&ssl=false");
	//		con = DriverManager.getConnection(
	//					"jdbc:postgresql://127.0.0.1:10000/judosite_3209", props);
					/*
					 */
					// con = DriverManager.getConnection(
					// "jdbc:postgresql://adminukvryp3:UDrcy6LHnakS@judonow-meitav.rhcloud.com:5432/judonow");
//					 con = DriverManager.getConnection(
//					 "jdbc:postgresql://localhost/mydb", "postgres", "postgres");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		public static void Filldtst() {
			Date dt = new Date();
			dt.setTime(dt.getTime() + dt.getTimezoneOffset());
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
}
