package anthony.SuperCraftBrawl.playerdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	String hostName = "mysql.apexhosting.gdn:3306", username = "apexMC437310", password = "b6688f32d4";
	
	private Connection c;
	
	public DatabaseManager() {
		loadConnection();	
	}
	
	public void loadConnection() {
		System.out.print("Loading database connection!");
		try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager
                    .getConnection("jdbc:mysql://" + hostName
                            + "?user=" + username + "&password=" + password + "&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
            System.out.print("Loaded database connection!");
            c.setCatalog("apexMC437310");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return c;
	}
	
	public void executeUpdateCommand(String updateCommand) {
		try {
			Statement stmt = getConnection().createStatement();
			stmt.execute(updateCommand);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void executeQueryCommand(String updateCommand, ExecuteFunction func) {
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet set = stmt.executeQuery(updateCommand);
			func.execute(set);
			set.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
