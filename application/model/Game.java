package application.model;

public class Game {

	private String gameId;
	private String gameName;
	private String gameDescription;
	private int price;
	
	
	public Game(String gameId, String gameName, String gameDescription, int price) {
		this.gameId = gameId;
		this.gameName = gameName;
		this.gameDescription = gameDescription;
		this.price = price;
	}
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameDescription() {
		return gameDescription;
	}
	public void setGameDescription(String gameDescription) {
		this.gameDescription = gameDescription;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
