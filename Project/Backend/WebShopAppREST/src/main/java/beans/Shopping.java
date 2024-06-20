package beans;

import java.time.LocalDateTime;
import java.util.List;

import enums.ShoppingStatus;

public class Shopping {

	private String id;
	private List<Integer> chocolateIds;
	private int factoryId;
	private LocalDateTime dateTime;
	private double price;
	private String customerName;
	private ShoppingStatus status;
	private String username;
	public Shopping(String id, List<Integer> chocolateIds, int factoryId, LocalDateTime dateTime, double price,
			String customerName, ShoppingStatus status, String username) {
		super();
		this.id = id;
		this.chocolateIds = chocolateIds;
		this.factoryId = factoryId;
		this.dateTime = dateTime;
		this.price = price;
		this.customerName = customerName;
		this.status = status;
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getId() {
		return id;
	}
	public Shopping() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Integer> getChocolateIds() {
		return chocolateIds;
	}
	public void setChocolateIds(List<Integer> chocolateIds) {
		this.chocolateIds = chocolateIds;
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public ShoppingStatus getStatus() {
		return status;
	}
	public void setStatus(ShoppingStatus status) {
		this.status = status;
	}
	
	
}
