package beans;

public class Chocolate {
	
	private int id; 
	private String name;
	private double price;
	private String kind; //ne treba enum kad nije naglaseno tacno koliko ih ima,
	private int factory;
	private String type; // opet ne znam da li enum, ima puno tipova
	private double grams;
	private String description;
	private String imageUrl;
	private Boolean status; 
    private int number; // broj cokolada na stanju
	public Chocolate() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Chocolate(int id, String name, double price, String kind, int factory, String type, double grams,
			String description, String imageUrl, Boolean status, int number) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.kind = kind;
		this.factory = factory;
		this.type = type;
		this.grams = grams;
		this.description = description;
		this.imageUrl = imageUrl;
		this.status = status;
		this.number = number;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public int getFactory() {
		return factory;
	}
	public void setFactory(int factory) {
		this.factory = factory;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getGrams() {
		return grams;
	}
	public void setGrams(double grams) {
		this.grams = grams;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
    
    
    
    
}
