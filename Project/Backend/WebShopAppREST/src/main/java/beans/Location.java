package beans;

public class Location {
	
	private int id;
	private double latitude;
	private double longitude;
	private String address; // za sad ovako, pa neki format definisati ili praviti klasu 
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Location(int id, double latitude, double longitude, String address) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
