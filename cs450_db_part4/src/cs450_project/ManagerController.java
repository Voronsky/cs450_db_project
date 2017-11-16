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
	private TextField first_name;

	@FXML
	private TextField last_name;
	
	@FXML
	private TextField address_input;
	
	@FXML
	private TextField pHours;
	
	@FXML
	private TextField middle_initial;
	
	@FXML
	private TextField ssn_field;
	
	@FXML
	private TextField birth_date;

	
	@FXML
	private Button add_emp_btn;

	CompanyDB company;
	Employee employee;
	private ArrayList<String> listOfProjects;
	private ArrayList<String> selectedProjects;
	private ObservableList<String> projectList = FXCollections.observableArrayList();
	
	
	@FXML
	protected void initialize() throws IOException {
		int listSize = 0;
		try {
			
			//project_name_col.setCellFactory(new PropertyValueFactory<Project, String>("Test"));
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
		
		
	}
	
	@FXML
	protected void insertIntoDB() {
		employee = new Employee(first_name.getText(),middle_initial.getText(),last_name.getText(),address_input.getText(),
				ssn_field.getText(),birth_date.getText(),0,0);
		
	}


}
