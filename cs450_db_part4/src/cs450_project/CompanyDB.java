/**
 * 
 */
package cs450_project;
import java.sql.*;
import java.io.*;
import java.util.*;

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
	
	/**
	 * Boolean method to check if the SSN corresponds to a manager in the database
	 * @param ssn
	 * @return true if it is, false if not
	 * @throws SQLException
	 */
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

	public ArrayList<String> getProjectList() throws SQLException {
		ArrayList<String> listOfProjects = new ArrayList<String>();
		String query = "select pname from project";
		String project = "";
		p = conn.prepareStatement(query);
		p.clearParameters();
		r = p.executeQuery();
		while(r.next()) {
			project = r.getString(1);
			listOfProjects.add(project);
		}
		/* Print out the list of projects to make sure things are right */
		Iterator<String> itr = listOfProjects.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
		return listOfProjects;

	}

}
