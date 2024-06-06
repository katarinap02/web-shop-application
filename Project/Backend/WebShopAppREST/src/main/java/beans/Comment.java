package beans;

public class Comment {
	
	private int id;
	private int buyerId;
	private int factoryId;
	private String commentText;
	private int rate;
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Comment(int id, int buyerId, int factoryId, String commentText, int rate) {
		super();
		this.id = id;
		this.buyerId = buyerId;
		this.factoryId = factoryId;
		this.commentText = commentText;
		this.rate = rate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(int buyerId) {
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
