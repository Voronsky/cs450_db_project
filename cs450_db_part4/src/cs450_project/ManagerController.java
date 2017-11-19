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
	private Button add_dep_btn;

	CompanyDB company;
	Employee employee;
	Pair pair;
	Pair projectPair;
	private static final double MAX_HOURS = 40.00;
	private ArrayList<String> listOfProjects;
	private ArrayList<Pair> selectedProjects = new ArrayList<Pair>();
	private ArrayList<String> missingFields = new ArrayList<String>();
	private ObservableList<String> projectList = FXCollections.observableArrayList();
	private Alert alert = new Alert(AlertType.ERROR);
	private double totalHours = 0;
	private String missingValues = "";
	private String outputText = "";
	GUIController loginGui = new GUIController();
	
	
	@FXML
	protected void initialize() throws IOException {
		int listSize = 0;
		try {
			
			//alert.setTitle("Employee Creation Error");
			//alert.setHeaderText("Attention Required!");
			employee = new Employee();
			company = new CompanyDB("@apollo.vse.gmu.edu:1521:ite10g","idiaz3","oahiwh");
			listOfProjects = company.getProjectList();
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
				}
				catch(Exception e){
					alert.setHeaderText("Fatal Exception error");
					alert.setContentText(e.toString());
					alert.showAndWait();
					System.exit(1);
				}
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
				}

			}
		});
		
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
			if(superssn_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Supervisor SSN";
			}
			if(birth_date.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Birth date";
			}
			if(salary_input.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Salary";
			}
			if(pHours.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Number of Hours for Project";
			}
			
			if(dept_num.getText().isEmpty()) {
				missingValues = missingValues + "\nMissing Department Values";
			}
			
			if(sex_input.getText().isEmpty()){
				missingValues = missingValues + "\nMissing Sex";
			}
			
			if(!dep_yes.isSelected() && !dep_no.isSelected()
					|| dep_yes.isSelected() && dep_no.isSelected()) {
				missingValues = missingValues + "\nIndicate has Dependents or not";
			}
			
			if(missingValues.length()>0) {
				alert.setContentText(missingValues);
				alert.showAndWait();
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
				employee.setDepartmentNumber(Integer.parseInt(dept_num.getText()));
				employee.setAssignedProjects(selectedProjects);
				employee.printDebugEmployeeeInfo();
				System.out.println("------------------------------------");
				clearScreen();
				company.insertIntoEmployeeTable(employee);
				System.out.println("Inserting EMployee Succeeded!");
				cleanUp();
				employee.printDebugEmployeeeInfo();
				//company.insertIntoWorksOn(employee);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
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
		dept_num.clear();
		salary_input.clear();
		sex_input.clear();
	}
	
	private void cleanUp() {
		employee = new Employee();
		listOfProjects.clear();
		outputText = "";
	}


}
