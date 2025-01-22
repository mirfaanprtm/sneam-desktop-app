package application.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.config.Conn;
import application.model.Game;

public class GameProcess {

	public List<Game> getAll() throws SQLException{
		String sql = "SELECT * FROM game";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		ResultSet rs =  pst.executeQuery();
		
		List<Game> games = new ArrayList<>();
		
		while(rs.next()) {
			Game game = new Game(rs.getString("gameId"),
								rs.getString("gameName"), 
								rs.getString("gameDescription"), 
								rs.getInt("price"));
			
			games.add(game);
		}
			
		return games;
	}
	
	public List<String> getByGameName() throws SQLException{
		String sql = "SELECT gameName FROM game";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		ResultSet rs =  pst.executeQuery();
		
		List<String> games = new ArrayList<>();
		
		while(rs.next()) {
			games.add(rs.getString("gameName"));
		}
			
		return games;
	}
	
	public Game getByName(String gameName) throws SQLException {
		String sql = "SELECT * FROM game WHERE gameName = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setNString(1, gameName);
		
		ResultSet rs =  pst.executeQuery();
		
		if(rs.next()) {
			return new Game(rs.getString("gameId"),
					rs.getString("gameName"), 
					rs.getString("gameDescription"), 
					rs.getInt("price"));
		}
			
		return null;
	}
	
	public Game getById(String gameId) throws SQLException {
		String sql = "SELECT * FROM game WHERE gameId = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setNString(1, gameId);
		
		ResultSet rs =  pst.executeQuery();
		
		if(rs.next()) {
			return new Game(rs.getString("gameId"),
					rs.getString("gameName"), 
					rs.getString("gameDescription"), 
					rs.getInt("price"));
		}
			
		return null;
	}
	
	public boolean store(Game game) throws SQLException {
		String sql = "INSERT INTO `game`(`gameID`,`gameName`,`gameDescription`,`price`) VALUES(?,?,?,?)";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, game.getGameId());
		pst.setString(2, game.getGameName());
		pst.setString(3, game.getGameDescription());
		pst.setInt(4, game.getPrice());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
	
	public boolean update(Game game) throws SQLException {
		String sql = "UPDATE `game` SET `gameName`= ?,`gameDescription`=?,`price`=? WHERE `gameID` = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, game.getGameName());
		pst.setString(2, game.getGameDescription());
		pst.setInt(3, game.getPrice());
		pst.setString(4, game.getGameId());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
	
	public boolean remove(String gameId) throws SQLException {
		String sql = "DELETE FROM `game` WHERE `gameID` = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, gameId);
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
}
