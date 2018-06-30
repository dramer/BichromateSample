package bichromate.DBAccess;



import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.mysql.cj.jdbc.MysqlDataSource;

public class sTestSMSConnection {
	
	private static ResourceBundle resources;
	
	public sTestSMSConnection(ResourceBundle myResources){
		
		setupParameters(myResources);
			
	 
	}// sTestMySQLConnector
	public sTestSMSConnection(){
	
		setupParameters(resources);
			
	 
	}// sTestMySQLConnector

	private void setupParameters(ResourceBundle resources){
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("scott");
		dataSource.setPassword("tiger");
		dataSource.setServerName("myDBHost.example.org");

		Connection conn;
		try {
			conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ID FROM USERS");

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			
			
    	}//main
	}// Test
}// sTestSMSConnection
