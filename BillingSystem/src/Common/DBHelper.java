package Common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBHelper {
	
	DBConnect conn = null;
	Statement stmt = null;
	
	public DBHelper() {
		conn = new DBConnect();
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet rs = null;
	    
	    try
	    {
	    	// open connection in database
	    stmt = conn.connect().createStatement();
	    // run query
	    rs = stmt.executeQuery(query);
	    // close connection in database
	    conn.connect().close();
	    }
	    
	    catch (SQLException e)
	    {
	     e.printStackTrace();
	    }
	     
	        return rs;
	}
	
	public void executeUpdate(String query) {
	    try
	    {
	    	// open connection in database
	    stmt = conn.connect().createStatement();
	    // run query
	    stmt.executeUpdate(query);
	    // close connection in database
	    conn.connect().close();
	    }
	    
	    catch (SQLException e)
	    {
	     e.printStackTrace();
	    }
	     
	}
	
}
