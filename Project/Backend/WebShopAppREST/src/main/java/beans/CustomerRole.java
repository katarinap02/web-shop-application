package beans;

public class CustomerRole {
	
	private String name;
	private double discount;
	private int requiredPoints;
	public CustomerRole(String name, double discount, int requiredPoints) {
		super();
		this.name = name;
		this.discount = discount;
		this.requiredPoints = requiredPoints;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getRequiredPoints() {
		return requiredPoints;
	}
	public void setRequiredPoints(int requiredPoints) {
		this.requiredPoints = requiredPoints;
	}
	
	

}
