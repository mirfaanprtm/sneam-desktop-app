package application.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.config.Conn;
import application.model.User;

public class UserProcess {

	public User login(String username, String password) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE `email`= ? AND `password`=?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, username);
		pst.setString(2, password);
		
		ResultSet rs =  pst.executeQuery();
		
		if(rs.next()) {
			return new User(rs.getString(1),
					rs.getString(2), 
					rs.getString(3), 
					rs.getString(4), 
					rs.getString(5), 
					rs.getString(6));
		}
			
		return null;
	}
	
	public List<User> getAll() throws SQLException{
		String sql = "SELECT * FROM user";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		ResultSet rs =  pst.executeQuery();
		
		List<User> users = new ArrayList<>();
		
		while(rs.next()) {
			User user = new User(rs.getString("userID"),
								rs.getString("username"), 
								rs.getString("password"), 
								rs.getString("phoneNumber"),
								rs.getString("email"),
								rs.getString("role"));
			
			users.add(user);
		}
			
		return users;
	}
	
	public User getByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM user WHERE email = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, email);
		
		ResultSet rs =  pst.executeQuery();
		
		if(rs.next()) {
			return new User(rs.getString("userID"),
					rs.getString("username"), 
					rs.getString("password"), 
					rs.getString("phoneNumber"),
					rs.getString("email"),
					rs.getString("role"));
		}
			
		return null;
	}
	
	public User getById(String userId) throws SQLException {
		String sql = "SELECT * FROM user WHERE userID = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, userId);
		
		ResultSet rs =  pst.executeQuery();
		
		if(rs.next()) {
			return new User(rs.getString("userID"),
					rs.getString("username"), 
					rs.getString("password"), 
					rs.getString("phoneNumber"),
					rs.getString("email"),
					rs.getString("role"));
		}
			
		return null;
	}
	
	public boolean store(User user) throws SQLException {
		String sql = "INSERT INTO `user`(`userID`,`username`,`password`,`phoneNumber`,`email`,`role`) VALUES(?,?,?,?,?,?)";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, user.getUserId());
		pst.setString(2, user.getUsername());
		pst.setString(3, user.getPassword());
		pst.setString(4, user.getPhoneNumber());
		pst.setString(5, user.getEmail());
		pst.setString(6, user.getRole());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
	
	
	
	public boolean update(User user) throws SQLException {
		String sql = "UPDATE `user` SET `username` = ?, `password` = ?, `phoneNumber` ?, `email` = ?, `role` = ? WHERE `userID` = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, user.getUsername());
		pst.setString(2, user.getPassword());
		pst.setString(3, user.getPhoneNumber());
		pst.setString(4, user.getEmail());
		pst.setString(5, user.getRole());
		pst.setString(6, user.getUserId());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
	
	public boolean remove(String userId) throws SQLException {
		String sql = "DELETE FROM `user` WHERE `userID` = ?";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, userId);
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			return true;
		}
			
		return false;
	}
}
