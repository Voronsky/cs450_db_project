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
	private String sex;
	private String address;
	private String birthDate;
	private String ssn;
	private String superSsn;
	private int salary;
	private int dno;
	private ArrayList<Pair> assignedProjects = new ArrayList<Pair>();
	private ArrayList<Dependent> deps = new ArrayList<Dependent>();


	public Employee() {
		
		
	}
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
	
	public String getEmployeeName() {
		return this.fname+" "+this.minit+". "+this.lname;
	}
	
	public String getFirstName() {
		return this.fname;
	}
	
	public String getMinit() {
		return this.minit;
	}
	
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
	
	public ArrayList<Dependent> getDepedent() {
		return this.deps;
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
	

	public void setDependents(String fname, String sex, String bdate, String relationship) {
		Dependent dep = new Dependent(fname,sex,bdate,relationship);
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
	
	private class Dependent {
		private String name;
		private String sex;
		private String bdate;
		private String relationship;
		
		private Dependent() {
			
		}
		
		private Dependent( String name, String sex, 
				String bdate, String relationship) 
		{
			this.name = name;
			this.sex = sex;
			this.bdate = bdate;
			this.relationship = relationship;
		}
		
		private String getName() {
			return this.name;
		}
		
		private String getSex() {
			return this.sex;
		}
		
		private String getBirthDate() {
			return this.bdate;
		}
		
		private String getRelationship() {
			return this.relationship;
		}
		
	}
	
}
