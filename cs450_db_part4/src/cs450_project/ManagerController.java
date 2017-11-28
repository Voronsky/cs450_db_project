/**
 * 
 */
package cs450_project;
import javafx.event.*;
import javafx.fxml.*;
import javafx.beans.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.collections.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;


/**
 * @author ivan
 *
 */
public class ManagerController {
	@FXML
	private ComboBox<String> projectsBox;
	
	@FXML
	private ComboBox<String> depNum_comboBox;
	
	@FXML
	private TextField fname_input;

	@FXML
	private TextField lname_input;
	
	@FXML
	private TextField address_input;
	
	@FXML
	private TextField pHours;
	
	@FXML
	private TextField minit_input;
	
	@FXML
	private TextField ssn_field;
	
	@FXML
	private TextField superssn_input;
	
	@FXML
	private TextField birth_date;
	
	@FXML
	private TextField dept_num;
	
	@FXML
	private TextField dep_first_name;
	
	@FXML
	private TextField salary_input;
	
	@FXML
	private TextField sex_input;
	
	@FXML
	private TextArea output;
	
	@FXML
	private CheckBox dep_yes;
	
	@FXML
	private CheckBox dep_no;
	
	@FXML
	private TextField dep_sex;
	
	@FXML
	private TextField dep_bdate;
	
	@FXML
	private TextField dep_relationship;
	
	@FXML
	private Button add_emp_btn;
	
	@FXML
	private Button assignProject;
	
	@FXML
	private Button resetAssigned_btn;
	
	@FXML
	private Button add_dep_btn;

	CompanyDB company;
	Employee employee;
	Pair pair;
	Pair projectPair;
	private static final double MAX_HOURS = 40.00;
	private ArrayList<String> listOfProjects;
	private ArrayList<Integer> listOfDepNums;
	private ArrayList<Pair> selectedProjects = new ArrayList<Pair>();
	private ArrayList<String> missingFields = new ArrayList<String>();
	private ObservableList<String> projectList = FXCollections.observableArrayList();
	private ObservableList<Integer> depNums = FXCollections.observableArrayList();
	private Alert alert = new Alert(AlertType.ERROR);
	private double totalHours = 0;
	private String missingValues = "";
	private String outputText = "";
	GUIController loginGui = new GUIController();
	
	
	/**
	 * This will load values into the ComboBoxes before the Window is shown.
	 * The values in the combo Boxes are also pulled from the Company Database
	 * @throws IOException
	 */
	@FXML
	protected void initialize() throws IOException {
		int listSize = 0;
		try {
			
			employee = new Employee();
			//company = new CompanyDB("@apollo.vse.gmu.edu:1521:ite10g","idiaz3","oahiwh");
			company = new CompanyDB();
			listOfProjects = company.getProjectList();
			listOfDepNums = company.getDepartmentNumbers();
			for (int i = 0; i < listOfDepNums.size(); i++) {
				depNums.add(listOfDepNums.get(i));
			}
			System.out.println("---List of Department Numbers---");
			for (int i = 0; i<depNums.size(); i++) {
				System.out.println(depNums.get(i));
				depNum_comboBox.getItems().add(depNums.get(i).toString());
			}
			System.out.println("---List of Projects --- ");
			System.out.println(listOfProjects);
			assert listOfProjects.size() > 0;
			listSize = listOfProjects.size();
			for (int i = 0; i < listSize; i++) {
				projectList.add(listOfProjects.get(i));
			}
			System.out.println("---INFO ProjectList -----");
			for (int i = 0; i < projectList.size(); i++) {
				System.out.println(projectList.get(i));
			}
			projectsBox.getItems().addAll(projectList);

		}
		catch(SQLException se) {
			System.out.println("ERROR: Could not connect to the Database");
			System.exit(1);
		}
		
		/**
		 * Handles when the employee is to be added
		 */
		add_emp_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addEmployee();
			}
		});

		
		/**
		 * Creates a list of Pairs which contain (Project, Hours)
		 */
		assignProject.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pair = new Pair();
				int index = 0;
				totalHours = 0.00;
				try {
					pair.setHours(Double.parseDouble(pHours.getText()));
					pair.setProject(projectsBox.getSelectionModel().getSelectedItem().toString());
				
					System.out.println("---- INFO: Project Selected and Hours assigned ---");
					System.out.println(pair.getProject()+"\t"+pair.getHours());
					selectedProjects.add(pair);
					if(exceedsMaxHours(selectedProjects)) {
						selectedProjects.clear();
						alert.setHeaderText("More than max Hours allowed");
						alert.setContentText("No more than a total of 40 hours can be assigned to an employee. Try again");
						alert.showAndWait();
					}
					else if(duplicateProjects(selectedProjects) > 0) {
						index = duplicateProjects(selectedProjects);
						selectedProjects.remove(index);
						alert.setHeaderText("Duplicate Project");
						alert.setContentText("Project selected has already been assigned.");
						alert.showAndWait();
					}
					else {
						outputText = outputText + "\n" + "Assigned " + pair.getProject() + " to Employee\t Hours: " + pair.getHours();
						output.setText(outputText);
						projectsBox.getSelectionModel().clearSelection();
					}
				}catch(Exception e){
					alert.setHeaderText("Project error");
					alert.setContentText("No Project was Selected");
					alert.showAndWait();
				}
			}
		});
		
		/**
		 * Clears the ArrayList of selectedProjects, thus having to choose new ones
		 */
		resetAssigned_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				selectedProjects.clear();
				outputText = outputText + "Assigned projects cleared";
				output.setText(outputText);
			}
		});
		
		/**
		 * ActionEvent handler for when Dependents need to be added to an employee
		 */
		dep_yes.selectedProperty().addListener(new ChangeListener<Boolean>(){
			public void changed(ObservableValue<? extends Boolean> ov, 
					Boolean old_val, Boolean new_val) {
				System.out.println(new_val);
				if(new_val) {
					dep_first_name.setEditable(true);
					dep_sex.setEditable(true);
					dep_bdate.setEditable(true);
					dep_relationship.setEditable(true);
				}
				else {
					dep_first_name.setEditable(false);
					dep_sex.setEditable(false);
					dep_bdate.setEditable(false);
					dep_relationship.setEditable(false);
				}
			}
		});
		
		/**
		 * ActionEvent handler for when No Dependents are to be added to the employee
		 */
		dep_no.selectedProperty().addListener(new ChangeListener<Boolean>(){
			public void changed(ObservableValue<? extends Boolean> ov, 
					Boolean old_val, Boolean new_val) {
				System.out.println(new_val);
				if(new_val) {
					dep_first_name.setEditable(false);
					dep_sex.setEditable(false);
					dep_bdate.setEditable(false);
					dep_relationship.setEditable(false);
				}
				else {
					dep_first_name.setEditable(true);
					dep_sex.setEditable(true);
					dep_bdate.setEditable(true);
					dep_relationship.setEditable(true);
				}
			}
		});

		/**
		 * Adds the Dependent to the employee
		 */
		add_dep_btn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				employee.setDependents(dep_first_name.getText(),
						dep_sex.getText(), 
						dep_bdate.getText(), 
						dep_relationship.getText()
						);
				outputText = outputText + "\nAdded Dependent\n" + dep_first_name.getText()
				+ " "+ dep_sex.getText()+ " "+dep_bdate.getText()+ " "+dep_relationship.getText();
				output.setText(outputText);
				dep_first_name.clear();
				dep_sex.clear();
				dep_bdate.clear();
				dep_relationship.clear();
			}
		});
		
		
	}
	
	/**
	 * Creates an Employee object and fills in the object attributes to that of the input
	 * Then it will try to insert into the database directly
	 */
	@FXML
	protected void addEmployee() {
		output.setText("Checking input . . . ");
		missingValues = "";
		try {
			if(fname_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing First Name";
			}
			if(minit_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Middle Initial";
			}
			if(lname_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Last Name";
			}
			if(address_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Address";
			}
			if(ssn_field.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing SSN";
			}
			//Seems this method is short by 1 letter, so 9 letter length is 8??
			if(ssn_field.getText().length() != 9){
				System.out.println("SSN Length input: "+birth_date.getText().length());
				missingValues = missingValues +"\nMissing 9-digit SSN";
			}
			if(superssn_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Supervisor SSN";
			}
			if(superssn_input.getText().length() != 9) {
				missingValues = missingValues +"\nMissing 9-digit Supervisor SSN";
			}
			if(birth_date.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Birth date";
			}
			if(birth_date.getText().length() != 8) {
				missingValues = missingValues +"\nDate format dd-mm-yy";
			}
		
			if(salary_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Salary";
			}
			if(pHours.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Number of Hours for Project";
			}
			
			if(depNum_comboBox.getSelectionModel().getSelectedItem() == null) {
				missingValues = missingValues + "\nMissing Department Values";
			}
			
			if(sex_input.getText().isEmpty()){
				missingValues = missingValues + "\nMissing Sex";
			}
			
			if(!dep_yes.isSelected() && !dep_no.isSelected()
					|| dep_yes.isSelected() && dep_no.isSelected()) {
				missingValues = missingValues + "\nIndicate has Dependents or not";
			}
			if(dep_yes.isSelected() && dep_bdate.getText().length() != 8 ) {
						missingValues = missingValues + "\nDependet date format, dd-mm-yy";
			}
			if(dep_yes.isSelected() && dep_relationship.getText().isEmpty()) {
				missingValues = missingValues +"\nDependent relationship missing";
			}
			if(dep_yes.isSelected() && dep_first_name.getText().isEmpty()) {
				missingValues = missingValues +"\nMissing Dependent first name";
			}

			if(selectedProjects.size() == 0) {
				missingValues = missingValues +"\nPlease assign at least 1 project";
			}

			if(!minimumOneProject(selectedProjects,
					Integer.parseInt(
							depNum_comboBox.getSelectionModel().getSelectedItem()
							)) && selectedProjects.size() >= 2)
			{ 
				missingValues = missingValues +"\nat least 1 project from department";
			}
			/*if(multipleProjectsSameDept(selectedProjects)) {
				missingValues = missingValues +"\nMore than two projects are from same department";
			}*/
			
			if(missingValues.length()>0) {
				alert.setContentText(missingValues);
				alert.showAndWait();
				missingValues ="";
			}
			else {
				System.out.println("----DEBUG ATTEMPT TO ADD EMPLOYEE ---");
				employee.setFirstName(fname_input.getText());
				employee.setLastName(lname_input.getText());
				employee.setMiddleInitial(minit_input.getText());
				employee.setAddress(address_input.getText());
				employee.setBirthDate(birth_date.getText());
				employee.setSex(sex_input.getText());
				employee.setSSN(ssn_field.getText());
				employee.setSupervisorSSN(superssn_input.getText());
				employee.setSalary(Integer.parseInt(salary_input.getText()));
				//employee.setDepartmentNumber(Integer.parseInt(dept_num.getText()));
				employee.setDepartmentNumber(Integer.parseInt(
						depNum_comboBox.getSelectionModel().getSelectedItem())
						);
				employee.setAssignedProjects(selectedProjects);
				employee.printDebugEmployeeeInfo();
				output.setText("Connecting to Database, please wait . . . ");
				System.out.println("------------------------------------");
				company.insertIntoEmployeeTable(employee);
				System.out.println("Inserting Employee Succeeded!");
				company.insertIntoWorksOn(employee);
				System.out.println("Inserting into Works on succeeded");
				if(employee.getDepedents().size()>0) {
					company.insertIntoDependents(employee);
					System.out.println("Inserting into Dependents succeeded");
				}
				printResult(ssn_field.getText());
				cleanUp();
				clearScreen();
				employee.printDebugEmployeeeInfo();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			cleanUp();
			clearScreen();
			alert.setTitle("Fatal Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		
	}
	
	/**
	 * Checks to make sure total hours assigned to an employee <= 40 hours
	 * @param list
	 * @return
	 */
	private boolean exceedsMaxHours(ArrayList<Pair> list) {
				for(int i=0; i<list.size(); i++) {
					projectPair = list.get(i);
					totalHours += projectPair.getHours();
					if(totalHours > MAX_HOURS) {
						outputText = "";
						return true;
						
					}
				}
		return false;
	}
	
	/**
	 * Check for duplicate assigned projects,
	 * @param list
	 * @return
	 */
	private int duplicateProjects(ArrayList<Pair> list) {
		Pair projectName = new Pair();
		for (int i = 0; i < list.size(); i++) {
			projectName = list.get(i);
			for (int j=++i; j< list.size(); j++) {
				projectPair = list.get(j);
				System.out.println("projectName: "+projectName.getProject());
				System.out.println("projectPair: "+projectPair.getProject());
				if (projectName.getProject().equals(projectPair.getProject())) {
					System.out.println("ERROR DUPLICATE PROJECT FOUND");
					return j;
				}
			}
		}
		return 0;
	}
	
	/**
	 * Checks to make sure that no more than 2 projects assigned are from the same department
	 * @param selectedProjects
	 * @return false if no more than 2 were assigned, true if more than 2 were assigned
	 */
	private Boolean multipleProjectsSameDept(ArrayList<Pair> selectedProjects) {
		int dno = 0;
		int dno2 = 0;
		for(int i = 0; i < selectedProjects.size(); i++) {
			int count = 0;
			try {
				dno = company.getDepartmentNumber(selectedProjects.get(i).getProject());
			}
			catch(SQLException se) {
				se.printStackTrace();
			}
			for( int j = 0; i < selectedProjects.size(); j++) {
				try {
					dno2 = company.getDepartmentNumber(selectedProjects.get(j).getProject());
				}
				catch(SQLException se) {
					se.printStackTrace();
				}
				if(dno == dno2) count++;
				if(count>2) return true;
			}
		}

		return false;
	}
	
	/**
	 * Checks to see if at least one of the assigned projects corresponds to the
	 * Employee's Department Number. Needs to query the Company DB for each Project
	 * to grab their respective Project Numbers
	 * @param e
	 * @param dno
	 * @return true if at least one assigned project corresponds to DNO, false if not
	 */
	private Boolean minimumOneProject(ArrayList<Pair> selectedProjects, int dno) {
		//ArrayList<Pair> projs = new ArrayList<Pair>();
		ArrayList<Integer> projNums = new ArrayList<Integer>();
		String pname = "";
		for(int i = 0; i < selectedProjects.size(); i++) {
			pname = selectedProjects.get(i).getProject();
			try {
				dno = company.getDepartmentNumber(pname);
				projNums.add(dno);
			}
			catch(SQLException se) {
				se.printStackTrace();
			}
		}
		if(!projNums.contains(dno)) {
			return false;
		}
		return true;
	}
	
	private void printResult(String ssn) {
		String result = "-----Database REPORT -------\n";
		Employee e;
		Employee.Dependent d;
		ArrayList<Pair> projs;
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, 1900);
		
		try {
			e = company.getEmployeeInformation(ssn);
			result = result + e.getEmployeeName() + "\n"
					+ "SSN: "+e.getSSN()+"\n" + "Address: "+e.getAddress()+"\n"
					+ "Birth Date: " + e.getBirthDate()+"\n"
					+ "Sex: " + e.getSex() +"\n"
					+ "Department Number: "+e.getDepartmentNum()+"\n";
			result = result + "----Projects Assigned----\n";
			projs = company.getEmployeeProjects(e);
			for(int i=0; i<projs.size();i++) {
				result = result + projs.get(i).getProject()+"\t"+projs.get(i).getHours()
						+"\n";
			}
			company.getDependentsOfEmployee(e);
			if(e.getDepedents().size()>0) {
				result = result + "----Employee's Dependents----\n";
				for(int i=0; i<e.getDepedents().size(); i++) {
					d = e.getDepedents().get(i);
					result = result + d.getName() + " " + " "+
					d.getBirthDate()+" "+d.getRelationship() +"\n";
				}
			}
			output.setText(result);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Clears out the GUI inputs
	 */
	private void clearScreen() {
		fname_input.clear();
		lname_input.clear();
		address_input.clear();
		pHours.clear();
		minit_input.clear();
		ssn_field.clear();
		superssn_input.clear();
		birth_date.clear();
		depNum_comboBox.getSelectionModel().clearSelection();
		projectsBox.getSelectionModel().clearSelection();
		salary_input.clear();
		sex_input.clear();
	}
	
	private void cleanUp() {
		employee = new Employee();
		listOfProjects.clear();
		dep_no.setSelected(false);
		dep_yes.setSelected(false);
		selectedProjects.clear();
		outputText = "";
	}


}
