/**
 * 
 */
package cs450_project;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.text.*;
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
	private String ssn;
	private boolean mgrCheck;
	
	@FXML
	protected void getSSNInput() throws IOException {
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
		}
		catch (SQLException se) {
			db_output.setText("ERROR: Cannot connect to database");
		}

		
	}

}
