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
	private String ssn;
	private boolean mgrCheck;
	Parent root;
	
	@FXML
	protected void initialize() {
		login_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					getSSNInput();
					launchManagerScreen(event);
				}
				catch(IOException e) {
					System.out.println(e);
				}
				
			}
		});
	}
	
	@FXML
	private void getSSNInput() throws IOException {
		//db_output.appendText("Java is shit");
		//db_output.setText("Python is a better language");
		ssn = ssn_input.getText();
		try {
			CompanyDB company = new CompanyDB("@apollo.vse.gmu.edu:1521:ite10g","idiaz3","oahiwh");
			mgrCheck = company.isManager(ssn);
			if (mgrCheck)
				db_output.setText("Welcome to the Company Database");
			else
				System.out.println("Not a manager or SSN does not exist in DB");
				//System.exit(1);
				
			///* Open a new window */
			//launchManagerScreen(null);
			

		}
		catch (SQLException se) {
			db_output.setText("ERROR: Cannot connect to database");
		}
		
	}
	
	private void launchManagerScreen(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/ManagerScreen.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Manager Screen");
		stage.setScene(new Scene(root, 800, 600));
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
		
	}

}
