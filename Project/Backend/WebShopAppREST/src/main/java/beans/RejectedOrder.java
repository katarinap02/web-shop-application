package beans;

public class RejectedOrder {
	
	private int id;
	private String orderId;
	private String comment;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public RejectedOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RejectedOrder(int id, String orderId, String comment) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.comment = comment;
	}
	
	

}
