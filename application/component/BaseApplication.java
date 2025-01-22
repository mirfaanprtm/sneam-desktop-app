package application.component;

import java.sql.SQLException;
import java.util.Optional;

import application.model.Cart;
import application.model.Game;
import application.model.User;
import application.process.CartProcess;
import application.process.GameProcess;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class BaseApplication {
	
	public abstract Scene sourceScene(Stage primaryStage);
	
	private BaseApplication application;
	
	public static User myUser = null;

	protected void setMenuApp(BorderPane borderPane, Stage primaryStage) {
		if(myUser == null) {
			// Before Login
			MenuItem signInmenuItem = new MenuItem("Sign In");
			signInmenuItem.setOnAction(e ->{
				application = new LoginApplication();
				primaryStage.setScene(application.sourceScene(primaryStage));
			});
			
			MenuItem registrationMenuItem = new MenuItem("Registration");
			registrationMenuItem.setOnAction(e ->{
				application = new RegisterApplication();
				primaryStage.setScene(application.sourceScene(primaryStage));
					
			});
			
			Menu menu = new Menu("Menu",null,
					signInmenuItem,
					registrationMenuItem);
			
			MenuBar menuBar = new MenuBar(menu);
			borderPane.setTop(menuBar);
			return;
		}
		
		// After Login
		MenuItem homeMenuItem = new MenuItem("Home");
		homeMenuItem.setOnAction(e ->{
			application = new HomeApplication();
			primaryStage.setScene(application.sourceScene(primaryStage));
				
		});
		
		MenuItem cartMenuItem = new MenuItem("Cart");
		cartMenuItem.setOnAction(e ->{
			application = new CartApplication();
			primaryStage.setScene(application.sourceScene(primaryStage));
		});
		
		Menu menuSignIn = new Menu("Dashboard",null,
				homeMenuItem,
				cartMenuItem);
		
		MenuItem logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction(e ->{
			myUser = null;
			application = new LoginApplication();
			primaryStage.setScene(application.sourceScene(primaryStage));
		});
		
		Menu menuLogout = new Menu("Logout",null,
				logoutItem);
		
		MenuBar menuBar = new MenuBar(menuSignIn, menuLogout);
		borderPane.setTop(menuBar);
	}
	
	protected Optional<ButtonType> showAlert(AlertType alertType, String title, String header, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		
		return alert.showAndWait();
	}
	
	protected void showCart(String titleText, String contentText, String priceText) {
		Stage stage = new Stage();
		
		BorderPane root = new BorderPane();
		
		VBox body = new VBox();
		body.setAlignment(Pos.CENTER);
		body.setSpacing(15);
//		
		
		BorderPane headerPane = new BorderPane();
		Text header = new Text("Add To Cart");
		header.setFont(Font.font("", FontWeight.BOLD, 30));
		headerPane.setCenter(header);
		
		Text title = new Text(titleText);
		title.setFont(Font.font("", FontWeight.BOLD, 20));
		
		Text content = new Text(contentText);
		
		BorderPane footerPane = new BorderPane();
		footerPane.setPadding(new Insets(10, 10, 10, 10));
		
		VBox footerBox = new VBox();
		footerBox.setSpacing(10);
		Text price = new Text(priceText);
		Spinner<Integer> quantity = new Spinner<Integer>(0, 10, 1);
		
		Button addToCart = new Button("Add To Cart");
		footerBox.getChildren().addAll(price, quantity, addToCart);
		
		footerPane.setCenter(footerBox);
		
		addToCart.setOnAction(event->{
			GameProcess gameProcess = new GameProcess();
			CartProcess cartProcess = new CartProcess();
			
			try {
				Game gameByGameName = gameProcess.getByName(titleText);
				Cart cartByGame = cartProcess.getByGame(gameByGameName.getGameId());
				
				if(quantity.getValue() == 0 
						&& cartProcess.getByGame(gameByGameName.getGameId()) == null) {
					stage.close();
				}
				
				if(quantity.getValue() == 0 
						&& cartProcess.getByGame(gameByGameName.getGameId()) != null) {
					
					boolean delete = cartProcess.removeByGame(gameByGameName.getGameId());
					
					if(delete) {
						showAlert(AlertType.INFORMATION, "Succesfully", "Delete", "Remove game on cart");
						
						stage.close();
					}
				}
				
				if(quantity.getValue() != 0 
						&& cartProcess.getByGame(gameByGameName.getGameId()) != null) {
					
					Cart cart = new Cart(myUser.getUserId(), gameByGameName.getGameId(), quantity.getValue() + cartByGame.getQuantity());
					
					boolean update = cartProcess.update(cart);
					
					if(update) {
						showAlert(AlertType.INFORMATION, "Succesfully", "Update", "Update game on cart");
						
						stage.close();
					}
				}
				
				
				if(quantity.getValue() != 0 
						&& cartProcess.getByGame(gameByGameName.getGameId()) == null) {
					
					Cart cart = new Cart(myUser.getUserId(), gameByGameName.getGameId(), quantity.getValue());
					
					boolean save = cartProcess.store(cart);
					
					if(save) {
						showAlert(AlertType.INFORMATION, "Succesfully", "Saving", "Game added on cart");
						
						stage.close();
					}
				}
			} catch (SQLException e) {
				showAlert(AlertType.ERROR, "Failed", "Error", e.getMessage());
			}
		});
		
		body.getChildren().addAll(
				title,
				content);
		
		root.setTop(headerPane);
		root.setCenter(body);
		root.setBottom(footerPane);
		
		Scene scene = new Scene(root,300,400);
		
		stage.setScene(scene);
		stage.show();
	}
	
}
