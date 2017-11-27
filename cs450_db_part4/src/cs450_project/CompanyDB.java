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
	//SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
	private Connection conn;
	private PreparedStatement p;
	private ResultSet r;
	private static String DBINFO = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
	private static String DBUSERNAME = "idiaz3";
	private static String DBPASSW = "oahiwh";
	private static String DRIVERNAME = "oracle.jdbc.driver.OracleDriver";

	
	public CompanyDB() throws SQLException, IOException {

	}
	
	public void initiateConnection() throws SQLException, IOException {
		try {
			Class.forName(DRIVERNAME);
		}catch(ClassNotFoundException x) {
			System.out.println("Driver could not be loaded");
		}
		
		this.conn = DriverManager.getConnection(DBINFO, DBUSERNAME,DBPASSW);
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
		try {
			initiateConnection();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
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
		conn.close();
		r.close();
		p.close();
		return true;
		
	}

	/**
	 * Queries the SQL Database, and grabs a view of the current projects in 
	 * the PROJECT table.
	 * @return the listOfProjects
	 * @throws SQLException
	 */
	public ArrayList<String> getProjectList() throws SQLException {
		try {
			initiateConnection();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
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
		p.close();
		r.close();
		conn.close();
		return listOfProjects;

	}
	
	/**
	 * Grabs the Project Number from the given Project Name from the Database
	 * @param pName
	 * @return pno corresponding to the Project
	 * @throws SQLException
	 */
	public BigDecimal getProjectNumber(String pName) throws SQLException {
		try {
			initiateConnection();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		String query = "select pnumber from project where pname = ?";
		BigDecimal pno = null;
		System.out.print("Project Name to get PNO for:" + pName+"\n");
		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, pName);
		r = p.executeQuery();
		while(r.next()) {
			pno = r.getBigDecimal(1);
		}
		p.close();
		r.close();
		conn.close();
		return pno;
		
	}
	
	/**
	 * Grabs a list of available department numbers from the database
	 * @return  an ArrayList<Integer> of department numbers
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<Integer> getDepartmentNumbers() throws SQLException, IOException{
		ArrayList<Integer> depNums = new ArrayList<Integer>();
		String query = "select dnumber from department";
		initiateConnection();
		p = conn.prepareStatement(query);
		r = p.executeQuery();
		while(r.next()) {
			depNums.add(r.getInt(1));
		}
		conn.close();
		p.close();
		r.close();
		return depNums;
		
	}
	
	/**
	 * Get the department number corresponding to the project name
	 * @param pname
	 * @return dno - the project's corresponding department number
	 * @throws SQLException
	 */
	public int getDepartmentNumber(String pname) throws SQLException {
		int dno = 0;
		String query = "select dnum from project where pname = ?";
		try {
			initiateConnection();
		}catch(IOException ie) {
			ie.printStackTrace();
			
		}
		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, pname);
		r = p.executeQuery();
		while(r.next()) {
			dno = r.getInt(1);
		}
		r.close();
		p.close();
		conn.close();
		
		return dno;
		
	}
	
	/**
	 * Returns an Employee object with it's field members assigned to the values the
	 * Employee table has in store for the given ssn queried.
	 * Return columns are (fname,minit,lname,ssn,bdate,address,sex,salary,superssn,dno)
	 * @param ssn - the employee to grab info about
	 * @return e - an employee object
	 */
	public Employee getEmployeeInformation(String ssn) throws SQLException {
		System.out.println("---Entering EMPLOYEE INFORMATION METHOD---");
		System.out.println("SSN Entered: "+ssn);
		Employee e = new Employee();
		try {
			initiateConnection();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		String query = "select fname,minit,lname,ssn,bdate,address,sex,salary,superssn,dno " +
		"from EMPLOYEE where ssn = ?";
		p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, ssn);
		r = p.executeQuery();
		while(r.next()) {
			e.setFirstName(r.getString(1));
			e.setMiddleInitial(r.getString(2));
			e.setLastName(r.getString(3));
			e.setSSN(r.getString(4));
			e.setBirthDate(r.getString(5));
			e.setAddress(r.getString(6));
			e.setSex(r.getString(7));
			e.setSalary(r.getInt(8));
			e.setSupervisorSSN(r.getString(9));
			e.setDepartmentNumber(r.getInt(10));
		}
		e.printDebugEmployeeeInfo();
		r.close();
		p.close();
		conn.close();
		
		return e;
	}
	
	/**
	 * Get the Projects associated to the employee from the database
	 * @param e - Employee object
	 * @return ArrayList<Pair> type, an ArrayList of pairs
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<Pair> getEmployeeProjects(Employee e) throws SQLException, IOException {
		ArrayList<Pair> list = new ArrayList<Pair>();
		ArrayList<BigDecimal> pnos  = new ArrayList<BigDecimal>();
		initiateConnection();
		String pnoQuery = "select pno,hours from works_on where essn = ?";
		String pnameQuery = "select pname from project where pnumber = ?";
		p = conn.prepareStatement(pnoQuery);
		p.clearParameters();
		p.setString(1, e.getSSN());
		r = p.executeQuery();
		while(r.next()) {
			Pair pair = new Pair();
			pair.setProjectPno(r.getBigDecimal(1));
			pair.setHours(r.getBigDecimal(2).doubleValue());
			list.add(pair);
		}
		//System.out.println("PNOs Retreived:\n"+pnos);
		p.close();
		p = conn.prepareStatement(pnameQuery);
		for(int i=0; i<list.size();i++) {
			System.out.println("Querying hours for Pno: "+list.get(i).getProjectPno());
			p.clearParameters();
			//p.setBigDecimal(1, pnos.get(i));
			p.setBigDecimal(1, list.get(i).getProjectPno());
			r = p.executeQuery();
			while(r.next()) {
				list.get(i).setProject(r.getString(1));
			}
		}
		p.close();
		r.close();
		conn.close();
		return list;
	}
	
	/**
	 * Modifies the employee object to set the Employee's dependents.
	 * 	Uses setDependents() from the Employee class to append Dependents to
	 *  the passed  employee object
	 * @param e - Employee object whose dependent field member needs to be modified
	 * @throws SQLException
	 */
	public void getDependentsOfEmployee(Employee e) throws SQLException { 
		try {
			initiateConnection();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		String query = "select dependent_name, sex, bdate, relationship"
				+ " from dependent where essn = ?";
		p = conn.prepareStatement(query);
		p.clearParameters();
		System.out.println("P: "+p.toString());
		p.setString(1, e.getSSN());
		r = p.executeQuery();
		System.out.println("r: "+r.toString());
		while(r.next()) {
			e.setDependents(r.getString(1), 
					r.getString(2), 
					r.getDate(3).toString(), 
					r.getString(4));
		}
		p.close();
		r.close();
		conn.close();
		
	}

	/**
	 * This prepares all the Employee field members into appropriate oracle Datatypes
	 * if necessary. Then inserts the contents of the employee object into the DB
	 * @param e - of Employee type
	 * @throws SQLException
	 */
	public void insertIntoEmployeeTable(Employee e) throws IOException {
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

		String query = "INSERT INTO employee VALUES(?,?,?,?,?,?,?,?,?,?,null)";
		
		/**
		 * fname, minit, lname, ssn, date, address, sex,salary, superssn, deptNum, email
		 * varchar,varchar,vchar,char,date,varchar,char,number,char,number,varchar
		 */

		try {
			initiateConnection();
			p = conn.prepareStatement(query);
			p.clearParameters();
			p.setString(1, e.getFirstName());
			p.setString(2, e.getMinit());
			p.setString(3, e.getLastName());
			p.setString(4, e.getSSN());
			p.setDate(5, sqlDate);
			p.setString(6, e.getAddress());
			p.setString(7, e.getSex());
			p.setBigDecimal(8, numberSalary);
			p.setString(9, e.getSupervisorSSN());
			p.setBigDecimal(10, numberDeptNum);
			p.executeUpdate();
			System.out.println(r);
			conn.close();
			p.close();
			r.close();
		}
		catch(SQLException se) {
			System.out.println("ERROR CODE: "+se.getErrorCode());
			System.out.println(se.getSQLState());
			System.out.println(se.getCause());
			System.out.println(se.getMessage());
			System.out.println(se.getStackTrace());

		}
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
	public void insertIntoWorksOn(Employee e) throws SQLException, IOException {
		System.out.println("ENTERED INSERT WORKS_ON TABLE");
		ArrayList<ProjectPair> numOfProjects = new ArrayList<ProjectPair>();
		String pName = null;
		BigDecimal pno;
		BigDecimal hours;
		String insertQuery = "insert INTO works_on(essn,pno,hours) values(?,?,?)";

		for(int i = 0; i<e.getAssignedProjects().size();i++) {
			pName = e.getProjectName(i);
			pno = getProjectNumber(pName);
			hours = new BigDecimal(e.getProjectHours(i));
			ProjectPair pair = new ProjectPair(pno, hours);
			pair.debugPrint();
			numOfProjects.add(pair);
		}
		
		initiateConnection();
		for(int i = 0; i<numOfProjects.size();i++) {
			p = conn.prepareStatement(insertQuery);
			p.clearParameters();
			p.setString(1,e.getSSN());
			p.setBigDecimal(2, numOfProjects.get(i).getPno());
			p.setBigDecimal(3, numOfProjects.get(i).getHours());
			p.executeUpdate();
		}
		
		p.close();
		conn.close();
		
	}
	
	/**
	 * Inserts into the Dependent table the dependents of the given employee
	 * @param e - the employee
	 * @throws SQLException
	 */
	public void insertIntoDependents(Employee e) throws SQLException, IOException {
		System.out.println("ENTERED DEPENDENTS TABLE");
		ArrayList<Employee.Dependent> dependentList;
		Employee.Dependent dependent;
		String query = "insert into DEPENDENT(essn,dependent_name,sex,bdate,relationship)"
				+"values(?,?,?,?,?)";
		initiateConnection();
		dependentList = e.getDepedents();

		for(int i = 0; i<dependentList.size();i++) {
			dependent = dependentList.get(i);
			/*try {
				java.util.Date javaDate = sdf.parse(dependent.getBirthDate());
				System.out.println("javaDate: "+javaDate);
				sqlDate = new java.sql.Date(javaDate.getTime());
			}catch(Exception z) {
				z.printStackTrace();
			}*/
			p = conn.prepareStatement(query);
			p.clearParameters();
			p.setString(1, e.getSSN());
			p.setString(2, dependent.getName());
			p.setString(3, dependent.getSex());
			p.setDate(4, dependent.getSQLBirthdayFormat());
			p.setString(5,dependent.getRelationship());
			p.executeUpdate();
			
		}
		p.close();
		conn.close();

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
			System.out.println("---DEBUG PROJECTPAIR ---\n");
			System.out.println(this.pno);
			System.out.println(this.hours);
			System.out.println("----END OF DEBUG PROJECTPAIR --");
		}
	}
}
