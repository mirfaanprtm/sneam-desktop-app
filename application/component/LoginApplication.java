package application.component;

import java.sql.SQLException;

import application.process.UserProcess;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginApplication extends BaseApplication {

	@Override
	public Scene sourceScene(Stage primaryStage) {
		BorderPane root = new BorderPane();
		
		UserProcess userProcess = new UserProcess();
		
		// 
		setMenuApp(root, primaryStage);
		
		//
		VBox loginRoot = new VBox();
		loginRoot.setAlignment(Pos.CENTER_LEFT);
		loginRoot.setSpacing(10);
		loginRoot.setPadding(new Insets(0, 100, 0, 100));
		
		BorderPane loginPane = new BorderPane();
		Text loginText = new Text("LOGIN");
		loginText.setFont(Font.font("", FontWeight.BOLD, 40));
		loginPane.setCenter(loginText);
		
		Text emailText = new Text("email");
		
		Text passwordText = new Text("password");
		
		TextField emailField = new TextField();
		
		PasswordField passwordField = new PasswordField();
		
		Button signInBtn = new Button("Sign In");
		
		signInBtn.setOnAction(event ->{
			
			try {
				myUser = userProcess.login(emailField.getText(), passwordField.getText());
				
				if(myUser != null) {
					primaryStage.setScene(new HomeApplication().sourceScene(primaryStage));
					
					return;
				}
				
				showAlert(AlertType.WARNING, "Invalid Request", "Wrong Credentials", "Username and password is wrong");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		loginRoot.getChildren()
			.addAll(loginPane,
					emailText,
					emailField,
					passwordText,
					passwordField,
					signInBtn);
		
		root.setCenter(loginRoot);
		
		return new Scene(root,500,500);
	}

}
