/**
 * 
 */
package cs450_project;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.TextArea;
import javafx.scene.text.*;

/**
 * @author ivan
 *
 */
public class GUIController {
	@FXML
	private TextArea db_output;
	
	@FXML
	protected void setDBText() {
		//db_output.appendText("Java is shit");
		db_output.setText("Python is a better language");
		
	}

}
