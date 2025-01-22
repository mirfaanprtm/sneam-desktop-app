package application.component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import application.model.Cart;
import application.model.Game;
import application.model.TransactionDetail;
import application.model.TransactionHeader;
import application.process.CartProcess;
import application.process.TransactionProcess;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CartApplication extends BaseApplication {

	@SuppressWarnings("unchecked")
	@Override
	public Scene sourceScene(Stage primaryStage) {
		BorderPane root = new BorderPane();
		
		CartProcess cartProcess = new CartProcess();
		
		TransactionProcess transactionProcess = new TransactionProcess();
		
		setMenuApp(root, primaryStage);
		
		VBox loginRoot = new VBox();
		loginRoot.setAlignment(Pos.CENTER_LEFT);
		loginRoot.setSpacing(10);
		loginRoot.setPadding(new Insets(0, 60, 0, 60));
		
		BorderPane regPane = new BorderPane();
		Text regText = new Text("YOUR CART");
		regText.setFont(Font.font("", FontWeight.BOLD, 20));
		regPane.setCenter(regText);
		
		BorderPane tablePane = new BorderPane();
		
		TableView<Cart> table = new TableView<>();
		table.setMinWidth(50);
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		
		TableColumn<Cart, String> nameCol = new TableColumn<Cart, String>("Name");
		nameCol.setCellValueFactory(cellData -> {
		    Cart cart = cellData.getValue();
		    
		    Game game = cart.getGame();
		    
		    String gameName = (game != null) ? game.getGameName() : "";
		    return new SimpleStringProperty(gameName);
		});
		nameCol.setMinWidth(100);
		nameCol.setMaxWidth(120);
		
		TableColumn<Cart, String> priceCol = new TableColumn<Cart, String>("Price");
		priceCol.setCellValueFactory(cellData -> {
		    Cart cart = cellData.getValue();
		    
		    Game game = cart.getGame();
		    
		    String priceStr = (game != null) ? String.valueOf(game.getPrice()) : "";
		    return new SimpleStringProperty(priceStr);
		});
		priceCol.setMinWidth(60);
		priceCol.setMaxWidth(60);
		
		TableColumn<Cart, Integer> quantityCol = new TableColumn<Cart, Integer>("Qty");
		quantityCol.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("quantity"));
		quantityCol.setMinWidth(50);
		quantityCol.setMaxWidth(60);
		
		TableColumn<Cart, Integer> totalCol = new TableColumn<Cart, Integer>("Total");
		totalCol.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("total"));
		totalCol.setMinWidth(50);
		totalCol.setMaxWidth(75);
		
		table.getColumns().addAll(nameCol,priceCol,quantityCol,totalCol);
		
		ObservableList<Cart> carts = FXCollections.observableArrayList();
		try {
			carts = FXCollections.observableArrayList(cartProcess.getAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.getItems().addAll(carts);
		
		Text grossText = new Text("Gross price: 0");
		grossText.setFont(Font.font("", FontWeight.LIGHT, 20));
		
		Button checkOutBtn = new Button("Check Out");
		checkOutBtn.setOnAction(event->{
			List<Cart> cartss = new ArrayList<>();
			try {
				cartss = cartProcess.getAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(cartss.isEmpty()) {
				showAlert(AlertType.WARNING, "Invalid Input", "Cart is empty", "Transaction not possible");
				return;
			}
			
			String id = "";
			try {
				id = String.format("TR%03d", transactionProcess.getAll().size()+1);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			TransactionHeader transactionHeader = new TransactionHeader();
			transactionHeader.setTransactionId(id);
			transactionHeader.setUserId(BaseApplication.myUser.getUserId());
			
			// add save
			List<TransactionDetail> transactionDetails = new LinkedList<>();
			int sumOfPrice = 0;
			for (int i = 0; i < cartss.size(); i++) {
				TransactionDetail transactionDetail = new TransactionDetail();
				
				Cart cart = cartss.get(i);
				Game game = cart.getGame();
				int total = cart.getQuantity() * game.getPrice();
				
				transactionDetail.setTransactionId(id);
				transactionDetail.setGameId(game.getGameId());
				transactionDetail.setQuantity(total);
				
				transactionDetails.add(transactionDetail);
				sumOfPrice +=total;
			}
			
			try {
				if(transactionProcess.store(transactionHeader, transactionDetails)) {
					showAlert(AlertType.WARNING, "Succesfully", "Transaction", "Transaction success");
					cartProcess.remove();
					
					ObservableList<Cart> cartsUpt = null;
					try {
						cartsUpt = FXCollections.observableArrayList(cartProcess.getAll());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					table.setItems(cartsUpt);
					grossText.setText("Gross price: "+sumOfPrice);
				}
			} catch (SQLException e) {
				grossText.setText("Gross price: Failed to check out");
				e.printStackTrace();
			}
		});
		
		tablePane.setCenter(table);
		
		loginRoot.getChildren()
		.addAll(regPane,
				tablePane,
				grossText,
				checkOutBtn);
	
		root.setCenter(loginRoot);
		
		return new Scene(root,600,600);
	}

}
