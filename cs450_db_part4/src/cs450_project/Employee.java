/**
 * 
 */
package cs450_project;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ivan
 * This class is an object containing every attribute that defines the entity relationship
 * of an employee. This information  pertains to the EMPLOYEE TABLE in our current DB
 */
public class Employee {
	private String fname;
	private String minit;
	private String lname;
	private String sex;
	private String address;
	private String birthDate;
	private String ssn;
	private String superSsn;
	private int salary;
	private int dno;
	private ArrayList<Pair> assignedProjects = new ArrayList<Pair>();
	private ArrayList<Dependent> deps = new ArrayList<Dependent>();

	/**
	 * Basic constructor to create an empty employee object
	 */
	public Employee() {
		
		
	}
	
	/**
	 * Creates a pre-initialized Employee Object
	 * @param firstName - String of FirstName
	 * @param minit - String of middle initial
	 * @param lastName - String of Last Name
	 * @param sex - String of Sex
	 * @param Address - String of Address
	 * @param ssn - String of SSN
	 * @param superSsn - String of Supervisor's SSN
	 * @param birthDate - String of Birth Date
	 * @param salary - Integer of Salary
	 * @param dno - Integer of Dno
	 */
	public Employee(String firstName, String minit, String lastName, String sex, 
			String Address, String ssn, String superSsn, String birthDate,
			int salary, int dno) {
		this.fname = firstName;
		this.minit = minit;
		this.lname = lastName;
		this.sex = sex;
		this.superSsn = superSsn;
		this.address = Address;
		this.ssn = ssn;
		this.birthDate = birthDate;
		this.salary = salary;
		this.dno = dno;
		
	}
	
	/**
	 * Grabs the Employee Object's String name
	 * @return String of the entire formatted name
	 */
	public String getEmployeeName() {
		return this.fname+" "+this.minit+". "+this.lname;
	}
	
	/**
	 * Grabs the Employee Object's String first name
	 * @return String of the First Name
	 */
	public String getFirstName() {
		return this.fname;
	}
	
	/**
	 * Returns the middle initial of the Employee
	 * @return - String of Employee's middle initial
	 */
	public String getMinit() {
		return this.minit;
	}
	
	/**
	 * Returns the Last Name of the Employee
	 * @return - String of the Last Name
	 */
	public String getLastName() {
		return this.lname;
	}
	
	public String getSSN() {
		return this.ssn;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getBirthDate() {
		return this.birthDate;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public String getSupervisorSSN() {
		return this.superSsn;
	}
	
	public int getSalary() {
		return this.salary;
	}
	
	public int getDepartmentNum() {
		return this.dno;
	}

	public ArrayList<Pair> getAssignedProjects(){
		return this.assignedProjects;
	}
	
	public ArrayList<Dependent> getDepedents() {
		return this.deps;
	}
	
	public String getProjectName(int index) {
		Pair project = new Pair();
		project = assignedProjects.get(index);
		return project.getProject();
	}
	
	public double getProjectHours(int index) {
		Pair project = new Pair();
		project = assignedProjects.get(index);
		return project.getHours();
	}
	
	public void setFirstName(String fname) {
		this.fname = fname;
	}
	
	public void setMiddleInitial(String minit) {
		this.minit = minit;
	}
	
	public void setLastName(String lname) {
		this.lname = lname;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setBirthDate(String bdate) {
		this.birthDate = bdate;
	}
	
	public void setSSN(String ssn) {
		this.ssn = ssn;
	}
	
	public void setSupervisorSSN(String superssn) {
		this.superSsn = superssn;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public void setDepartmentNumber(int dno) {
		this.dno = dno;
	}

	public void setAssignedProjects(ArrayList<Pair> listOfProjects) {
		this.assignedProjects = listOfProjects;
		
	}
	
	/**
	 * Creates a Dependent object and assigns it to the Employee's List of Dependents
	 * @param fname - Dependent's First Name
	 * @param sex - Sex
	 * @param bdate - Birthdate
	 * @param relationship - Relationship
	 */
	public void setDependents(String fname, String sex, String bdate, String relationship) {
		Dependent dep = new Dependent(fname,sex,bdate,relationship);
		dep.setSQLBdate(bdate);
		deps.add(dep);
	}
	
	/**
	 * Debug purposes only, print all the contents of the object
	 */
	public void printDebugEmployeeeInfo() {
		ArrayList<Pair> debug = new ArrayList<Pair>();
		Pair debugPair = new Pair();
		Dependent debugDep = new Dependent();
		System.out.println("---EMPLOYEE OBJECT " + this + " -----");
		System.out.println("Employee Name: "+this.getEmployeeName());
		System.out.println("Sex: "+this.getSex());
		System.out.println("Address: "+this.getAddress());
		System.out.println("Employee SSN: "+this.getSSN());
		System.out.println("Supervisor SSN: "+this.getSupervisorSSN());
		System.out.println("Employee Birth date: "+this.getBirthDate());
		System.out.println("Employee Salary: "+this.getSalary());
		System.out.println("Employee Department Number: "+this.getDepartmentNum());
		System.out.println("Assigned Projects:");
		debug = this.getAssignedProjects();
		for(int i = 0 ; i < debug.size(); i++) {
			debugPair = debug.get(i);
			System.out.println(debugPair.getProject()+"\t"+debugPair.getHours());
		}
		System.out.println("---DEPENDENTS----");
		for(int i=0; i<this.deps.size(); i++) {
			debugDep = deps.get(i);
			System.out.println(debugDep.getName()+" "+debugDep.getSex()+" "
			+debugDep.getBirthDate()+" "+debugDep.getRelationship());
		}
		System.out.println("----END OF EMPLOYEE " + this + "DEBUG ------");
		
	}
	
	public class Dependent {
		private String name;
		private String sex;
		private String bdate;
		private String relationship;
		private java.sql.Date sqlBdate;
		
		public Dependent() {
			
		}
		
		public Dependent( String name, String sex, 
				String bdate, String relationship) 
		{
			this.name = name;
			this.sex = sex;
			this.bdate = bdate;
			this.relationship = relationship;
		}
		
		public void setSQLBdate(String birthday) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
			try {
				java.util.Date javaDate = sdf.parse(this.getBirthDate());
				System.out.println("javaDate: "+javaDate);
				this.sqlBdate = new java.sql.Date(javaDate.getTime());
			}catch(Exception z) {
				z.printStackTrace();
			}
		}

		public String getName() {
			return this.name;
		}
		
		public String getSex() {
			return this.sex;
		}
		
		public String getBirthDate() {
			return this.bdate;
		}
		
		/**
		 * Retrieves the java.SQL.Date birthday value
		 * @return - (java.SQL.date) sqlBirthday
		 */
		public java.sql.Date getSQLBirthdayFormat(){
			return this.sqlBdate;
		}
		
		public String getRelationship() {
			return this.relationship;
		}
		
		
		
	}
	
}
