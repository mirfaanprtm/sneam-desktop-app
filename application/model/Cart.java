package application.model;

public class Cart {

	private String userId;
	private String gameId;
	private int quantity;
	
	// join
	private Game game;
	private int total;
	
	public Cart(String userId, String gameId, int quantity) {
		this.userId = userId;
		this.gameId = gameId;
		this.quantity = quantity;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	
	
}
