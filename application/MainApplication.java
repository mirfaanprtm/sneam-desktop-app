package application;
	
import application.component.LoginApplication;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApplication extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setScene(new LoginApplication().sourceScene(primaryStage));
			primaryStage.setTitle("SNEAM");
			primaryStage.setResizable(false);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
