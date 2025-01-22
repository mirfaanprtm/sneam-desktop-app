package application.model;

public class TransactionHeader {
	
	private String transactionId;
	private String userId;
	
	// not apply
	private TransactionDetail transactionDetail;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	// not apply
	public TransactionDetail getTransactionDetail() {
		return transactionDetail;
	}
	public void setTransactionDetail(TransactionDetail transactionDetail) {
		this.transactionDetail = transactionDetail;
	}
	
	

}
