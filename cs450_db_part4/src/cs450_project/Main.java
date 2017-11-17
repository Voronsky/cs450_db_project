package cs450_project;
	
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.fxml.*;
import javafx.event.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import java.util.*;


public class Main extends Application {
	@FXML
	Button login_btn;

	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/Gui.fxml"));
			Scene scene = new Scene(root, 800, 600);
			primaryStage.setTitle("CS450 Company Database");
			primaryStage.setScene(scene);
			primaryStage.show();
			/*login_btn.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event) {
					;
				}
			});*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
