package wzc.zcminer.global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WindchillDAO {

	public static String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static String JDBC_STRING = "jdbc:oracle:thin:@localhost:1521/wind"; // in case of 11g use '/' instead of :
	public static String USER_NAME = "plm10";
	public static String PASSWD = "plm10";
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    Connection conn = null;
	    ResultSet rs = null;
	    Statement stmt = null;
	    try{
	        Class.forName(JDBC_DRIVER);
	        conn = DriverManager.getConnection(JDBC_STRING, USER_NAME, PASSWD);
	        stmt = conn.createStatement();
	  
	        String query = "SELECT count(*) FROM WfProcess";
	        rs = stmt.executeQuery(query);
	    }catch(SQLException sqlEx){
	        sqlEx.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } finally{
	        try {
	            if(rs!=null) rs.close();
	            if(stmt !=null) stmt.close();
	            if(conn!=null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

}
