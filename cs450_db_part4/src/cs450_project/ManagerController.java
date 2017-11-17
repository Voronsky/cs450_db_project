/**
 * 
 */
package cs450_project;
import javafx.event.*;
import javafx.fxml.*;
import javafx.beans.*;
import javafx.beans.property.*;
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
	private TextField birth_date;
	
	@FXML
	private TextField dept_num;
	
	@FXML
	private TextField dep_first_name;
	
	@FXML
	private TextField salary_input;
	
	@FXML
	private TextArea output;

	
	@FXML
	private Button add_emp_btn;
	
	@FXML
	private Button assignProject;

	CompanyDB company;
	Employee employee;
	Pair pair;
	Pair projectPair;
	private static final double MAX_HOURS = 40.00;
	private ArrayList<String> listOfProjects;
	//private ArrayList<Pair<String, Double>> selectedProjects = new ArrayList<Pair<String, Double>>();
	private ArrayList<Pair> selectedProjects = new ArrayList<Pair>();
	private ArrayList<String> missingFields = new ArrayList<String>();
	private ObservableList<String> projectList = FXCollections.observableArrayList();
	private Alert alert = new Alert(AlertType.ERROR);
	private double totalHours = 0;
	private String missingValues = "";
	private String outputText = "";
	
	
	@FXML
	protected void initialize() throws IOException {
		int listSize = 0;
		try {
			
			//project_name_col.setCellFactory(new PropertyValueFactory<Project, String>("Test"));
			alert.setTitle("Employee Creation Error");
			alert.setHeaderText("Attention Required!");
			company = new CompanyDB("@apollo.vse.gmu.edu:1521:ite10g","idiaz3","oahiwh");
			listOfProjects = company.getProjectList();
			System.out.println("---List of Projects --- ");
			System.out.println(listOfProjects);
			assert listOfProjects.size() > 0;
			listSize = listOfProjects.size();
			for (int i = 0; i < listSize; i++) {
				projectList.add(listOfProjects.get(i));
			}
			System.out.println("---INfO ProjectList -----");
			for (int i = 0; i < projectList.size(); i++) {
				System.out.println(projectList.get(i));
			}
			projectsBox.getItems().addAll(projectList);

		}
		catch(SQLException se) {
			System.out.println("ERROR: Could not connect to the Database");
			System.exit(1);
		}
		
		add_emp_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				insertIntoDB();
			}
		});
		
		assignProject.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pair = new Pair();
				totalHours = 0.00;
				pair.setHours(Double.parseDouble(pHours.getText()));
				pair.setProject(projectsBox.getSelectionModel().getSelectedItem().toString());
				System.out.println("---- INFO: Project Selected and Hours assigned ---");
				System.out.println(pair.getProject()+"\t"+pair.getHours());
				selectedProjects.add(pair);
				for(int i=0; i<selectedProjects.size(); i++) {
					projectPair = selectedProjects.get(i);
					totalHours += projectPair.getHours();
					if(totalHours > MAX_HOURS) {
						selectedProjects.clear();
						outputText = "";
						alert.setHeaderText("More than max Hours allowed");
						alert.setContentText("No more than a total of 40 hours can be assigned to an employee. Try again");
						alert.showAndWait();
					}
				}
				outputText = outputText + "\n" + "Assigned " + pair.getProject() + " to Employee\t Hours: " + pair.getHours();
				output.setText(outputText);

			}
		});
		
	
		
	}
	
	@FXML
	protected void insertIntoDB() {
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
			
			if(missingValues.length()>0) {
				alert.setContentText(missingValues);
				alert.showAndWait();
			}
			else {
				employee = new Employee(fname_input.getText(),minit_input.getText(),lname_input.getText(),
				address_input.getText(), ssn_field.getText(),birth_date.getText(),
				Integer.parseInt(salary_input.getText()),Integer.parseInt(dept_num.getText()));
				employee.setAssignedProjects(selectedProjects);
				System.out.println(employee);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}


}
