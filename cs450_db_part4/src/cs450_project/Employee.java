/**
 * 
 */
package cs450_project;
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
	private String address;
	private String birthDate;
	private String ssn;
	private String mgrSsn;
	private int salary;
	private int dno;
	private ArrayList<Pair> assignedProjects = new ArrayList<Pair>();


	public Employee(String firstName, String minit, String lastName, String Address, String ssn, String birthDate,
			int salary, int dno) {
		this.fname = firstName;
		this.minit = minit;
		this.lname = lastName;
		this.address = Address;
		this.ssn = ssn;
		this.birthDate = birthDate;
		this.salary = salary;
		this.dno = dno;
		
	}
	
	public String getEmployeeName() {
		return this.fname+" "+this.minit+". "+this.lname;
	}
	
	public String getFirstName() {
		return this.fname;
	}
	
	public String getMinit() {
		return this.minit;
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
	
	public String getManagerSSN() {
		return this.mgrSsn;
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
	
	public void setAssignedProjects(ArrayList<Pair> listOfProjects) {
		this.assignedProjects = listOfProjects;
		
	}
	
	public void setManagerSSN(String mgrssn) {
		this.mgrSsn = mgrssn;
	}
	
	/**
	 * Debug purposes only, print all the contents of the object
	 */
	public void printDebugEmployeeeInfo() {
		ArrayList<Pair> debug = new ArrayList<Pair>();
		Pair debugPair = new Pair();
		System.out.println("---EMPLOYEE OBJECT " + this + " -----");
		System.out.println(this.getEmployeeName());
		System.out.println(this.getAddress());
		System.out.println(this.getSSN());
		System.out.println(this.getBirthDate());
		System.out.println(this.getSalary());
		System.out.println(this.getDepartmentNum());
		debug = this.getAssignedProjects();
		for(int i = 0 ; i < debug.size(); i++) {
			debugPair = debug.get(i);
			System.out.println(debugPair.getProject()+"\t"+debugPair.getHours());
		}
		System.out.println("----END OF EMPLOYEE " + this + "DEBUG ------");
		
	}
	
}
