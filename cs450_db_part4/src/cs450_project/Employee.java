/**
 * 
 */
package cs450_project;
import java.util.*;

/**
 * @author ivan
 *
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
	private ArrayList<String> assignedProjects;


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
	
	public String getAddress() {
		return this.address;
	}
	
	public String getBirthDate() {
		return this.birthDate;
	}
	
	public int getSalary() {
		return this.salary;
	}
	
	public int getDepartmentNum() {
		return this.dno;
	}
	
	public void assignProjects(ArrayList<String> listOfProjects) {
		this.assignedProjects = listOfProjects;
		
	}
	
	public void setManagerSSN(String mgrssn) {
		this.mgrSsn = mgrssn;
	}
	
}
