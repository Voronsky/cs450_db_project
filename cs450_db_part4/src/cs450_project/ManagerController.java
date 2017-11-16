/**
 * 
 */
package cs450_project;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author ivan
 *
 */
public class ManagerController {
	@FXML
	private TableView projects;
	
	CompanyDB company;
	private ArrayList<String> listOfProjects;
	
	protected void initialize() throws IOException {
		try {
			company = new CompanyDB("@apollo.vse.gmu.edu:1521:ite10g","idiaz3","oahiwh");
			listOfProjects = company.projectList();
			for (int i = 0; i < listOfProjects.size(); i++) {
				projects.getItems().add(1, listOfProjects.get(i));
			}
		}
		catch(SQLException se) {
			System.out.println("ERROR: Could not connect to the Database");
			System.exit(1);
		}
		
	}

}
