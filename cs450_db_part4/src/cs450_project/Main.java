package cs450_project;
	
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.fxml.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import java.util.*;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			/*BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();*/
			
			Parent root = FXMLLoader.load(getClass().getResource("/Gui.fxml"));
			Scene scene = new Scene(root, 800, 600);
			primaryStage.setTitle("CS450 Company Database");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
