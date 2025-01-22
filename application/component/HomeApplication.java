package application.component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.model.Game;
import application.process.GameProcess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeApplication extends BaseApplication {

	private Text titleGame;
	
	private Text detailGame;
	
	private Text priceGame;
	
	
	//
	private TextField gameTitleField;
	
	private TextArea gameDescField;
	
	private TextField priceField;
	
	
	// 
	
	private Button updateGame;
	
	private Button deleteGame;
	
	@Override
	public Scene sourceScene(Stage primaryStage) {
		BorderPane root = new BorderPane();
		
		GameProcess gameProcess = new GameProcess();
		
		// 
		setMenuApp(root, primaryStage);
		
		//
		VBox homeRoot = new VBox();
		homeRoot.setAlignment(Pos.TOP_LEFT);
		homeRoot.setSpacing(20);
		homeRoot.setPadding(new Insets(25, 60, 50, 60));
		
		BorderPane headerPane = new BorderPane();
		Text headerText = new Text("HELLO "+myUser.getUsername());
		headerText.setFont(Font.font("", FontWeight.BOLD, 20));
		headerPane.setCenter(headerText);
		
		
		HBox contentList = new HBox();
		contentList.setSpacing(10);
		contentList.setPadding(new Insets(0, 20, 0, 20));
		
		// not apply
		List<String> gameNames = new ArrayList<String>();
		try {
			gameNames = gameProcess.getByGameName();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		VBox detailContent = new VBox();
		detailContent.setSpacing(10);
		detailContent.setPadding(new Insets(0, 20, 0, 20));
		detailContent.setVisible(false);
		
		//
		ObservableList<String> games = FXCollections.observableArrayList(gameNames);
		ListView<String> gamesListView = new ListView<String>(games);
		gamesListView.setMinHeight(200);
		gamesListView.setMaxHeight(250);
		gamesListView.setMinWidth(200);
		
		gamesListView.setOnMouseClicked(new EventHandler<Event>() {
			public void handle(Event e) {
				String gameName = gamesListView.getSelectionModel().getSelectedItem();
				
				Game game = null;
				try {
					game = gameProcess.getByName(gameName);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				if(game != null) {
					titleGame.setText(game.getGameName());
					detailGame.setText(game.getGameDescription());
					priceGame.setText("Rp. "+String.valueOf(game.getPrice()));
					
					detailContent.setVisible(true);
					
				}
				
				if(myUser.getRole().equals("admin")) {
					if(game != null) {
						gameTitleField.setText(game.getGameName());
						gameDescField.setText(game.getGameDescription());
						priceField.setText(String.valueOf(game.getPrice()));
						
						updateGame.setVisible(true);
					}else {
						gameTitleField.setText("");
						gameDescField.setText("");
						priceField.setText("");
						
						updateGame.setVisible(false);
					}
				}
			};
		});
		
		//
		
		
		titleGame = new Text();
		titleGame.setFont(Font.font("", FontWeight.BOLD, 18));
		titleGame.setWrappingWidth(200);
		
		detailGame = new Text();
		detailGame.setWrappingWidth(200);
		
		priceGame = new Text();
		
		Button addCartButton = new Button("Add to Cart");
		addCartButton.setOnAction(e ->{
			showCart(titleGame.getText(), detailGame.getText(), priceGame.getText());			
		});
		
		detailContent.getChildren()
			.addAll(titleGame,
					detailGame,
					priceGame,
					addCartButton);
		
		
		contentList.getChildren()
			.addAll(gamesListView, 
					detailContent);

		
		if(myUser.getRole().equals("admin")) {
			// admin
			VBox adminRoot = new VBox();
			
			BorderPane adminPane = new BorderPane();
			Text adminText = new Text("ADMIN MENU");
			adminText.setFont(Font.font("", FontWeight.BOLD, 20));
			adminPane.setCenter(adminText);
			
			//
			HBox contentAdmin = new HBox();
			contentAdmin.setSpacing(10);
			
			//
			VBox formGame = new VBox();
			formGame.setSpacing(10);
			formGame.setMaxWidth(400);
			
			Text gameTitleText = new Text("Game title");
			
			Text gameDescText = new Text("Game Description");
			
			Text priceText = new Text("Price");
			
			gameTitleField = new TextField();
			
			gameDescField = new TextArea();
			
			priceField = new TextField();
			
			formGame.getChildren()
				.addAll(gameTitleText,
						gameTitleField,
						gameDescText,
						gameDescField,
						priceText,
						priceField);
			
			//
			VBox buttonForm = new VBox();
			buttonForm.setSpacing(20);
			
			Button addGame = new Button("Add");
			addGame.setMinWidth(75);
			addGame.setOnAction(event -> {
				List<Game> game = null;
				try {
					game = gameProcess.getAll();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				//validasi
				if(gameTitleField.getText().isEmpty()) {
					showAlert(AlertType.WARNING, "Invalid Input", "You haven't inputted a game name", "Please fill the game name form");
					return;
				}
				
				if(gameTitleField.getText().length() > 50) {
					showAlert(AlertType.WARNING, "Invalid Input", "Game name exceed 50 characters", "Game name is too high");
					return;
				}
				
				if(gameDescField.getText().isEmpty()) {
					showAlert(AlertType.WARNING, "Invalid Input", "You haven't inputted a game description", "Please fill the game description form");
					return;
				}
				
				if(gameDescField.getText().length() > 250) {
					showAlert(AlertType.WARNING, "Invalid Input", "Game description exceed 50 characters", "Game description is too high");
					return;
				}
				
				if(priceField.getText().isEmpty()) {
					showAlert(AlertType.WARNING, "Invalid Input", "You haven't inputted a price", "Please fill the price form");
					return;
				}
				
				if(priceField.getText().length() > 10) {
					showAlert(AlertType.WARNING, "Invalid Input", "Price exceed 10 characters", "Price to high");
					return;
				}
				
				try {
					Integer.parseInt(priceField.getText());
				}catch(NumberFormatException e) {
					showAlert(AlertType.WARNING, "No Price Warning", "You haven't inputted a price by alphabet and characters", "Please fill the price form by numeric");
					return;
				}
				
				int lastGame = game.size();
				String id = String.format("GA%03d", lastGame++);
				boolean isFind = true;
				while(isFind) {
					try {
						Game gameById = gameProcess.getById(id);
						if(gameById == null) {
							break;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					 id = String.format("GA%03d", lastGame++);
				}
				
				Game gameByName = null;
				try {
					gameByName = gameProcess.getByName(gameTitleField.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				if(gameByName == null) {
					Game newGame = new Game(id, 
							gameTitleField.getText(), 
							gameDescField.getText(), 
							Integer.parseInt(priceField.getText()));
					try {
						boolean isSave = gameProcess.store(newGame);
						if(isSave) {
							showAlert(AlertType.INFORMATION, "Succesfully", "Saving", "Game has been stored");
							games.add(newGame.getGameName());
							gamesListView.setItems(games);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					return;
				}
				
				showAlert(AlertType.INFORMATION, "Failed", "Error", "Game name has been available");
			});
			
			
			updateGame = new Button("Update");
			updateGame.setVisible(false);
			updateGame.setMinWidth(75);
			updateGame.setOnAction(event -> {
				Game gameByName = null;
				try {
					gameByName = gameProcess.getByName(gameTitleField.getText());
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				
				//validasi
				if(gameTitleText.getText().isEmpty() 
						&& gameDescText.getText().isEmpty()
						&& priceText.getText().isEmpty()) {
					
					showAlert(AlertType.WARNING, "Invalid Input", "You haven't inputted a game", "Please fill the game name, game description and price form");
					return;
				}
				
				if(gameTitleText.getText().length() > 50) {
					showAlert(AlertType.WARNING, "Invalid Input", "Game name exceed 50 characters", "Game name is too high");
					return;
				}
				
				if(gameDescText.getText().length() > 250) {
					showAlert(AlertType.WARNING, "Invalid Input", "Game description exceed 50 characters", "Game description is too high");
					return;
				}
				
				if(priceText.getText().length() > 10) {
					showAlert(AlertType.WARNING, "Invalid Input", "Price exceed 10 characters", "Price to high");
					return;
				}
				
				// vlidasi numerik
				
				if(gameByName != null) {
					// clean
					if(!gameTitleText.getText().isEmpty()) {
						gameByName.setGameName(gameTitleText.getText());
					}
					
					if(!gameDescText.getText().isEmpty()) {
						gameByName.setGameDescription(gameDescText.getText());
					}
					
					if(!priceText.getText().isEmpty()) {
						gameByName.setPrice(Integer.parseInt(priceText.getText()));
					}
					
					
					try {
						boolean update = gameProcess.update(gameByName);
						
						if(update) {
							showAlert(AlertType.INFORMATION, "Succesfully", "Updating", "Game has been updated");
							
							gamesListView.setItems(games);
							
							titleGame.setText(gameByName.getGameName());
							detailGame.setText(gameByName.getGameDescription());
							priceGame.setText("Rp. "+String.valueOf(gameByName.getPrice()));
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			});
			
			deleteGame = new Button("Delete");
			deleteGame.setVisible(true);
			deleteGame.setMinWidth(75);
			deleteGame.setOnAction(event -> {
				Game gameByName = null;
				try {
					gameByName = gameProcess.getByName(gameTitleField.getText());
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				
				try {
					Optional<ButtonType> buttonOk = showAlert(AlertType.CONFIRMATION, "Delete Game", "Are you sure you want to delete this game", "delete");
					
					if(buttonOk.isPresent() && buttonOk.get() == ButtonType.OK) {
						boolean delete = gameProcess.remove(gameByName.getGameId());
						
						if(delete) {
							showAlert(AlertType.INFORMATION, "Succesfully", "Deleting", "Game has been deleted");
							games.remove(gameByName.getGameName());
							gamesListView.setItems(games);
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			
			
			buttonForm.getChildren()
				.addAll(addGame,
						updateGame,
						deleteGame);
			
			
			contentAdmin.getChildren()
				.addAll(formGame,
						buttonForm);
			
			
			adminRoot.getChildren()
				.addAll(adminPane,
						contentAdmin);
			
			homeRoot.getChildren()
			.addAll(headerPane,
					contentList,
					adminRoot);
			
			root.setCenter(homeRoot);
			
			return new Scene(root,600,600);
		}
		
		homeRoot.getChildren()
		.addAll(headerPane,
				contentList);
		
		root.setCenter(homeRoot);
		
		Scene scene = new Scene(root,600,600);
		scene.getRoot().autosize();
		
		return scene;
	}

}
