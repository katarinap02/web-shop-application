package beans;

import java.util.List;

public class ShoppingCart {
	
	private int id;
	private List<Integer> chocolateIds;
	private int customerId;
	private double price;
	public ShoppingCart(int id, List<Integer> chocolateIds, int customerId, double price) {
		super();
		this.id = id;
		this.chocolateIds = chocolateIds;
		this.customerId = customerId;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Integer> getChocolateIds() {
		return chocolateIds;
	}
	public void setChocolateIds(List<Integer> chocolateIds) {
		this.chocolateIds = chocolateIds;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	

}
