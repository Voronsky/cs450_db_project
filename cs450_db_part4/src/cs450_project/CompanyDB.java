/**
 * 
 */
package cs450_project;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;

/**
 * @author ivan
 * This class handles formatting the Employee Class field members to be in 
 * Oracle Data types. Theres a few conversions that had to be done as there is no
 * natural 1:1 from Java data types to Oracle.
 * Thus NUMBER in Oracle could only be accepted from BigDecimal data types, thus
 * integers had to be converted into BigDecimal. Same applies to DATE
 * Oracle DATE had to be a conversion from a formated Java DATE datatype
 * Oracle Char had to be passed as a value from String.ValueOf as there is no
 * method for JDBC called 'setChar()'.
 */

public class CompanyDB {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
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
		String mgrSsn = "";
		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1,ssn);
		r = p.executeQuery();
		while(r.next()) {
			mgrSsn = r.getString(1);
			System.out.println("MGR SSN FOUND FOR FOLLOWING DEPARTMENT:");
			System.out.println(mgrSsn);
		}
		System.out.println(mgrSsn);
		System.out.println(mgrSsn.length());
		/*if(mgrSsn.length()==0) {
			return false;
		}*/
		return true;
		
	}

	/**
	 * Queries the SQL Database, and grabs a view of the current projects in 
	 * the PROJECT table.
	 * @return the listOfProjects
	 * @throws SQLException
	 */
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
	
	public BigDecimal getProjectNumber(String pName) throws SQLException {
		String query = "select pnumber from project where pname = ?";
		BigDecimal pno = null;
		System.out.print("Project Name to get PNO from:" + pName);
		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, pName);
		r = p.executeQuery();
		while(r.next()) {
			pno = r.getBigDecimal(1);
		}
		return pno;
		
	}

	/**
	 * This prepares all the Employee field members into appropriate oracle Datatypes
	 * if necessary. Then inserts the contents of the employee object into the DB
	 * @param e - of Employee type
	 * @throws SQLException
	 */
	public void insertIntoEmployeeTable(Employee e) throws SQLException {
		System.out.println("ENTER EMPLOYEE TABLE INSERT");
		e.printDebugEmployeeeInfo();
		BigDecimal numberSalary = new BigDecimal(e.getSalary());
		BigDecimal numberDeptNum = new BigDecimal(e.getDepartmentNum());
		java.sql.Date sqlDate = null;
		try {
			java.util.Date javaDate = sdf.parse(e.getBirthDate());
			System.out.println("javaDate: "+javaDate);
			sqlDate = new java.sql.Date(javaDate.getTime());
		}catch(Exception z) {
			z.printStackTrace();
		}

		String query = "INSERT INTO EMPLOYEE (fname,minit,lname,ssn,bdate,address,sex,salary,superssn,dno,email)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,null)";
		
		/**
		 * varchar,varchar,vchar,char,date,varchar,char,number,char,number,varchar
		 */

		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, e.getFirstName());
		p.setString(2, e.getMinit());
		p.setString(3, e.getLastName());
		p.setString(4, e.getSSN());
		//p.setDate(5, sqlDate); //birthdate
		//p.setDate(5, (Date)"27-JAN-93");
		p.setDate(5, null);
		p.setString(6, e.getAddress());
		p.setString(7, e.getSex());
		p.setBigDecimal(8, numberSalary);
		p.setString(9, e.getSupervisorSSN());
		p.setBigDecimal(10, numberDeptNum);
		//p.executeQuery();
		p.executeUpdate();
		System.out.println(r);
		System.out.println("---CHECKING VALUES---");
		//System.out.println(sqlDate);
		System.out.println("EXITED EMPLOYEE TABLE INSERT");
		
	}
	
	/**
	 * Formats project values of employee to be put into the works_on table
	 * The Oracle types are (Char, Number, Number)
	 * It will grab each project name and hours for that project from listOfProjects
	 * then grab each pno associated to that project 
	 * and then it will put into an object type of ProjectPair(Pno,Hours).
	 * From here it will make a list of ProjectPairs, and then for each pair
	 * insert individually into the table
	 * @param e
	 * @throws SQLException
	 */
	public void insertIntoWorksOn(Employee e) throws SQLException {
		System.out.println("ENTERED INSERTWORKS ON TABLE");
		ArrayList<ProjectPair> numOfProjects = new ArrayList<ProjectPair>();
		String pName = null;
		BigDecimal pno;
		BigDecimal hours;
		//String pnoQuery = "select pno from project where pname = ?";
		String insertQuery = "insert INTO works_on(essn,pno,hours) values(?,?,?)";
		for(int i = 0; i<e.getAssignedProjects().size();i++) {
			pName = e.getProjectName(i);
			pno = getProjectNumber(pName);
			hours = new BigDecimal(e.getProjectHours(i));
			ProjectPair pair = new ProjectPair(pno, hours);
			pair.debugPrint();
			numOfProjects.add(pair);
			/*p = conn.prepareStatement(pnoQuery);
			p.clearParameters();
			p.setString(1, pName);
			r = p.executeQuery(pnoQuery);
			while(r.next()) {
				pno = r.getBigDecimal(1);
				hours = new BigDecimal(e.getProjectHours(i));
				ProjectPair pair = new ProjectPair(pno, hours);
				pair.debugPrint();
				numOfProjects.add(pair);
			}*/
		}
		
		for(int i = 0; i<numOfProjects.size();i++) {
			p = conn.prepareStatement(insertQuery);
			p.clearParameters();
			p.setString(1,e.getSSN());
			p.setBigDecimal(2, numOfProjects.get(i).getPno());
			p.setBigDecimal(3, numOfProjects.get(i).getHours());
			p.executeUpdate();
		}
	}
	
	
	
	private class ProjectPair  {
		private BigDecimal pno;
		private BigDecimal hours;
		
		private ProjectPair(BigDecimal pno, BigDecimal hours) {
			this.pno = pno;
			this.hours = hours;
		}
		
		private BigDecimal getPno() {
			return this.pno;
		}
		
		private BigDecimal getHours() {
			return this.hours;
		}
		
		private void debugPrint() {
			System.out.println("---DEBUG PROJECTPAIR ---");
			System.out.println(this.pno);
			System.out.println(this.hours);
			System.out.println("----END OF DEBUG PROJECTPAIR --");
		}
	}
}
