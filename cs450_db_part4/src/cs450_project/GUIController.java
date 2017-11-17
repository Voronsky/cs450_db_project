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

/**
 * @author ivan
 *
 */
public class GUIController {
	@FXML
	private TextArea db_output;
	@FXML
	private TextField ssn_input;
	@FXML
	Button login_btn;
	@FXML
	Button add_emp_btn;
	private String ssn;
	private boolean mgrCheck;
	
	@FXML
	protected void initialize() {
		/*login_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event)  {
				getSSNInput();
				System.out.println("-----INFO: Launching MgrScreen ----");
				launchManagerScreen(event);
				
			}
		});*/
		
		login_btn.setOnAction(new EventHandler<ActionEvent>() {
			Boolean result;
			@Override
			public void handle(ActionEvent event) {
				Parent root;
				try {
					System.out.println("-----INFO: Launching MgrScreen ----");
					ssn = ssn_input.getText();
					result = getSSNInput(ssn);
					if(result == false) {
						System.exit(1);
					}
					root = FXMLLoader.load(getClass().getClassLoader().getResource("ManagerScreen.fxml"));
					System.out.println(root);
					Stage stage = new Stage();
					stage.setTitle("Manager Screen");
					stage.setScene(new Scene(root, 800, 600));
					stage.show();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@FXML
	private boolean getSSNInput(String ssn) {
		String checkSsn = ssn;
		try {
			CompanyDB company = new CompanyDB("@apollo.vse.gmu.edu:1521:ite10g","idiaz3","oahiwh");
			mgrCheck = company.isManager(checkSsn);
			if (mgrCheck) {
				db_output.setText("Welcome to the Company Database");
				System.out.println("Not a manager or SSN does not exist in DB");
			}
			else {
				System.out.println("Not a manager or SSN does not exist in DB");
				return false;
			}
		}
		catch (Exception se) {
				db_output.setText("ERROR: Cannot connect to database");
		}
		return true;
		
	}
	
}
