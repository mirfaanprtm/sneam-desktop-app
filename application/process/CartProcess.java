package application.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.component.BaseApplication;
import application.config.Conn;
import application.model.Cart;
import application.model.Game;

public class CartProcess {
	
	public Cart getByGame(String gameId) throws SQLException {
		String sql = "SELECT * FROM cart WHERE gameID = ? AND userId = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, gameId);
		pst.setString(2, BaseApplication.myUser.getUserId());
		
		ResultSet rs =  pst.executeQuery();
		
		
		if(rs.next()) {
			Cart cart = new Cart(rs.getString("userID"),
								rs.getString("gameID"), 
								rs.getInt("quantity"));
			
			return cart;
		}
			
		return null;
	}
	
	public List<Cart> getAll() throws SQLException{
		String sql = "SELECT game.gameID as gameID,game.gameName as gameName, game.price as price,cart.quantity as quantity, (game.price * cart.quantity) as total  FROM cart JOIN game on cart.gameID = game.gameID WHERE userID = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, BaseApplication.myUser.getUserId());
		
		ResultSet rs =  pst.executeQuery();
		
		List<Cart> carts = new ArrayList<>();
		
		while(rs.next()) {
			Cart cart = new Cart(BaseApplication.myUser.getUserId(),
								null, 
								rs.getInt("quantity"));
			
			Game game = new Game(rs.getString("gameID"), rs.getString("gameName"), null, rs.getInt("price"));
			
			cart.setGame(game);
			cart.setTotal(cart.getQuantity() * game.getPrice());
			
			carts.add(cart);
		}
			
		return carts;
	}
	
	public boolean store(Cart cart) throws SQLException {
		String sql = "INSERT INTO `cart`(`userID`,`gameID`,`quantity`) VALUES(?,?,?)";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, cart.getUserId());
		pst.setString(2, cart.getGameId());
		pst.setInt(3, cart.getQuantity());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
	
	
	public boolean update(Cart cart) throws SQLException {
		String sql = "UPDATE `cart` SET `quantity`= ? WHERE gameID = ? AND userID= ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setInt(1, cart.getQuantity());
		pst.setString(2, cart.getGameId());
		pst.setString(3, BaseApplication.myUser.getUserId());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
	
	public boolean remove() throws SQLException {
		String sql = "DELETE FROM `cart` WHERE userID=?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, BaseApplication.myUser.getUserId());
		
		int delete =  pst.executeUpdate();
		
		if(delete > 0) {
			return true;
		}
			
		return false;
	}
	
	public boolean removeByGame(String gameId) throws SQLException {
		String sql = "DELETE FROM `cart` WHERE gameID = ? AND userID=?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, gameId);
		pst.setString(2, BaseApplication.myUser.getUserId());
		
		int delete =  pst.executeUpdate();
		
		if(delete > 0) {
			return true;
		}
			
		return false;
	}
	
}
