package application.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.config.Conn;
import application.model.TransactionDetail;
import application.model.TransactionHeader;

public class TransactionProcess {

	public List<TransactionHeader> getAll() throws SQLException{
		String sql = "SELECT * FROM transactionHeader";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		ResultSet rs =  pst.executeQuery();
		
		List<TransactionHeader> transactionHeaders = new ArrayList<>();
		
		while(rs.next()) {
			TransactionHeader transactionHeader = new TransactionHeader();
			transactionHeader.setTransactionId(rs.getString(1));
			transactionHeader.setUserId(rs.getString(2));
			
			transactionHeaders.add(transactionHeader);
		}
			
		return transactionHeaders;
	}
	
	public boolean store(TransactionHeader transactionHeader, List<TransactionDetail> transactionDetails) throws SQLException {
		String sql = "INSERT INTO `transactionHeader`(`transactionID`,`userID`) VALUES(?,?)";
		
		PreparedStatement pst =  Conn.conn().prepareStatement(sql);
		
		pst.setString(1, transactionHeader.getTransactionId());
		pst.setString(2, transactionHeader.getUserId());
		
		int save =  pst.executeUpdate();
		
		if(save > 0) {
			String sqlDetail = "INSERT INTO `transactionDetail`(`transactionID`,`gameID`,`quantity`) VALUES(?,?,?)";
			for(int i = 0; i < transactionDetails.size();i++) {
				TransactionDetail transactionDetail = transactionDetails.get(i);
				PreparedStatement pstDetail =  Conn.conn().prepareStatement(sqlDetail);
				
				pstDetail.setString(1, transactionDetail.getTransactionId());
				pstDetail.setString(2, transactionDetail.getGameId());
				pstDetail.setInt(3, transactionDetail.getQuantity());
				
				int saveDetail =  pstDetail.executeUpdate();
				if(saveDetail > 0) {
				}else {
					return false;
				}
			}
		}
			
		return true;
	}
}
