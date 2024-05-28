package beans;

import java.util.ArrayList;

public class ChocolateFactory {
	
	private int id;
	private String name;
	private ArrayList<Integer> chocolateIds;
	private WorkingHours workingHours;
	private Boolean isWorking;
	private Location location;
	private String logoUrl;
	private double rate;
	public ChocolateFactory() {
		super();
		// TODO Auto-generated constructor stub
		chocolateIds = new ArrayList<Integer>();
	}
	public ChocolateFactory(int id, String name, ArrayList<Integer> chocolateIds, WorkingHours workingHours, Boolean isWorking,
			Location locationId, String logoUrl, double rate) {
		super();
		this.id = id;
		this.name = name;
		this.chocolateIds = chocolateIds;
		this.workingHours = workingHours;
		this.isWorking = isWorking;
		this.location = locationId;
		this.logoUrl = logoUrl;
		this.rate = rate;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Integer> getChocolates() {
		return chocolateIds;
	}
	public void setChocolates(ArrayList<Integer> chocolates) {
		this.chocolateIds = chocolates;
	}
	public WorkingHours getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(WorkingHours workingHours) {
		this.workingHours = workingHours;
	}
	public Boolean getIsWorking() {
		return isWorking;
	}
	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
	
	

}
