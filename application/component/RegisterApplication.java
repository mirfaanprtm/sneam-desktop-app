package application.component;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import application.model.User;
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

public class RegisterApplication extends BaseApplication {
	
	@Override
	public Scene sourceScene(Stage primaryStage) {
		BorderPane root = new BorderPane();
		
		UserProcess userProcess = new UserProcess();
		
		// 
		setMenuApp(root, primaryStage);
		VBox loginRoot = new VBox();
		loginRoot.setAlignment(Pos.CENTER_LEFT);
		loginRoot.setSpacing(10);
		loginRoot.setPadding(new Insets(0, 100, 0, 100));
		
		BorderPane regPane = new BorderPane();
		Text regText = new Text("REGISTER");
		regText.setFont(Font.font("", FontWeight.BOLD, 40));
		regPane.setCenter(regText);
		
		Text usernameText = new Text("username");
		
		Text emailText = new Text("email");
		
		Text passwordText = new Text("password");
		
		Text confirmPasswordText = new Text("confirm password");
		
		Text phoneNumberText = new Text("phone number");
		
		TextField usernameField = new TextField();
		
		TextField emailField = new TextField();
		
		PasswordField passwordField = new PasswordField();
		
		PasswordField confirmPasswordField = new PasswordField();
		
		TextField phoneField = new TextField();
		
		Button signUpBtn = new Button("Sign Up");
		
		signUpBtn.setOnAction(event ->{
			List<User> user = new LinkedList<>();
			try {
				user = userProcess.getAll();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			// validasi
			if(usernameField.getText().length() < 4 
					|| usernameField.getText().length() > 20) {
				showAlert(AlertType.WARNING, "Invalid Input", "Username is invalid", "Username must contain 4-20");
				return;
			}
			
			if(passwordField.getText().length() < 6 
					|| passwordField.getText().length() > 20) {
				showAlert(AlertType.WARNING, "Invalid Input", "Password is invalid", "Password must contain 6-20");
				return;
			}
			
			for(int i =0;i < passwordField.getText().length();i++) {
				String chrPass = String.valueOf(passwordField.getText().charAt(i));
				
				int codeChar = chrPass.hashCode();
				// numeric base on ascii 0 - 9(48 - 57)
				// upper alphabet base on ascii A - Z(65 - 90)
				// lower alphabet base on ascii A - Z(97 - 122)
				
				if((codeChar < 48 || codeChar > 57)
						&& (codeChar < 65 || codeChar > 90)
						&& (codeChar < 97 || codeChar > 122)) {
					showAlert(AlertType.WARNING, "Invalid Input", "Password is invalid", "Password must be alphanumeric");
					return;
				}
				
			}
			
			if(!emailField.getText().contains("@")) {
				showAlert(AlertType.WARNING, "Invalid Input", "Email is invalid", "Email must contains  “@” in it.");
				return;
			}
			
			User userByEmail = null;
			try {
				userByEmail = userProcess.getByEmail(emailField.getText());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			if(userByEmail != null) {
				showAlert(AlertType.WARNING, "Invalid Input","Email is invalid", "Email must be unique");
				return;
			}
			
			if(phoneField.getText().length() < 9 
					|| phoneField.getText().length() > 20) {
				showAlert(AlertType.WARNING, "Invalid Input", "Phone Number is invalid", "Phone Number must contain 9-20");
				return;
			}
			
			for(int i =0;i < phoneField.getText().length();i++) {
				String chrPhone = String.valueOf(phoneField.getText().charAt(i));
				
				int codeChar = chrPhone.hashCode();
				// numeric base on ascii 0 - 9(48 - 57)
				
				if((codeChar < 48 || codeChar > 57)) {
					showAlert(AlertType.WARNING, "Invalid Input", "Phone Number is invalid", "Phone Number can only be numeric");
					return;
				}
				
			}
			
			if(!passwordField.getText().equals(confirmPasswordField.getText())) {
				showAlert(AlertType.WARNING, "Invalid Input", "Confirm Password", "Confirm Password must be the same as Password.");
				return;
			}
			
			int lastUser = user.size();
			
			// save
			String id = String.format("AC%03d", lastUser++);
			
			boolean isFindUser = true;
			while(isFindUser) {
				try {
					User userById = userProcess.getById(id);
					if(userById == null) {
						break;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 id = String.format("AC%03d", lastUser++);
			}
			
			//
			User newUser = new User(id, 
					usernameField.getText(), 
					passwordField.getText(), 
					phoneField.getText(),
					emailField.getText(),
					"customer");
			
			try {
				boolean isSave = userProcess.store(newUser);
				if(isSave) {
					showAlert(AlertType.INFORMATION, "Succesfully", "Saving", "Registration has been accept");
					primaryStage.setScene(new LoginApplication().sourceScene(primaryStage));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		});
		
		loginRoot.getChildren()
			.addAll(regPane,
					usernameText,
					usernameField,
					emailText,
					emailField,
					passwordText,
					passwordField,
					confirmPasswordText,
					confirmPasswordField,
					phoneNumberText,
					phoneField,
					signUpBtn);
		
		root.setCenter(loginRoot);
		
		return new Scene(root,500,500);
	}

}
