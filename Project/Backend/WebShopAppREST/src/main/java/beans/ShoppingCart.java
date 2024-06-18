package beans;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
	
	private int id;
	private ArrayList<Integer> chocolateIds;
	private String customerName;
	private double price;
	private boolean isOpened;
	private int factoryId;
	

	public ShoppingCart() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShoppingCart(int id, ArrayList<Integer> chocolateIds, String customerId, double price, boolean isOpened, int factoryId) {
		super();
		this.id = id;
		this.chocolateIds = chocolateIds;
		this.customerName = customerId;
		this.price = price;
		this.isOpened = isOpened;
		this.factoryId = factoryId;
	}
	public boolean isOpened() {
		return isOpened;
	}
	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Integer> getChocolateIds() {
		return chocolateIds;
	}
	public void setChocolateIds(ArrayList<Integer> chocolateIds) {
		this.chocolateIds = chocolateIds;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerId) {
		this.customerName = customerId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	
	
	
	
	

}
