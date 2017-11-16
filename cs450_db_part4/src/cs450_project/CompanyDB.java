/**
 * 
 */
package cs450_project;
import java.sql.*;
import java.io.*;

/**
 * @author ivan
 *
 */
public class CompanyDB {
	private String db_url = "";
	private String pass = "";
	private String username="";
	private Connection conn;
	private PreparedStatement p;
	private ResultSet r;
	
	public CompanyDB(String db,String pw, String dbUsername) throws SQLException, IOException {
		this.db_url = "jdbc:oracle:thin:"+db;
		this.pass = pw;
		this.username = dbUsername;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException x) {
			System.out.println("Driver could not be loaded");
		}
		
		this.conn = DriverManager.getConnection(this.db_url,this.pass,this.username);
		
	}
	
	public boolean isManager(String ssn) throws SQLException {
		String query = "select * from department where mgrssn = ?";
		String mgrSsn = null;
		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1,ssn);
		r = p.executeQuery();
		while(r.next()) {
			mgrSsn = r.getString(1);
			System.out.println(mgrSsn);
		}
		if (mgrSsn == null) {
			return false;
		}
		if (!mgrSsn.isEmpty())
			return true;
		return false;
	}

	

}
