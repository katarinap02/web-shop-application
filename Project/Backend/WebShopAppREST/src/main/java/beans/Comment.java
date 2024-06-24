package beans;

public class Comment {
	
	private int id;
	private String buyerId;
	private int factoryId;
	private String commentText;
	private int rate;
	private int approved;
	private int statusSet;
	private String orderId;
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Comment(int id, String buyerId, int factoryId, String commentText, int rate, int isApproved, int isStatusSet, String orderId) {
		super();
		this.id = id;
		this.buyerId = buyerId;
		this.factoryId = factoryId;
		this.commentText = commentText;
		this.rate = rate;
		this.approved = isApproved;
		this.statusSet = isStatusSet;
		this.orderId = orderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getStatusSet() {
		return statusSet;
	}
	public void setStatusSet(int isStatusSet) {
		this.statusSet = isStatusSet;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int isApproved) {
		this.approved = isApproved;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}

	
	

}
