package beans;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;
import enums.Role;

public class User {

	private String username;
	private String password;
	private String name;
	private String surname;
	private Gender gender;
	private LocalDate birthDate;
	private Role role;
	private ChocolateFactory factory; //ovo je uglavon null tako da nemoj da radis neprovereni get
	private boolean bloked; //kao deleted se ponasa
	private boolean isSuspicious;
	
	
	//ArrayList<Shopping> shoppings; 

	//int cartId;

	//Basket basket;

	private double points;
	// CustomerRole role;
	private int customerId;
	private ArrayList<LocalDate> rejectedOrders;
	

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String username, String password, String name, String surname, Gender gender,
			LocalDate birthDate, Role role, double points, int customerId) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.birthDate = birthDate;
		this.role = role;

        this.points = points;

		this.bloked = false;

		this.factory = null;
		this.customerId = customerId;
		this.isSuspicious = false;
		this.rejectedOrders = new ArrayList<LocalDate>();
	} 
	public ArrayList<LocalDate> getRejectedOrders() {
		return rejectedOrders;
	}

	public void setRejectedOrders(ArrayList<LocalDate> rejectedOrders) {
		this.rejectedOrders = rejectedOrders;
	}

	public boolean isSuspicious() {
		return isSuspicious;
	}

	public void setSuspicious(boolean isSuspicious) {
		this.isSuspicious = isSuspicious;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public ChocolateFactory getFactory() {
		return factory;
	}

	public void setFactory(ChocolateFactory factory) {
		this.factory = factory;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public boolean getBloked() {
		return bloked;
	}

	public void setBloked(boolean bloked) {
		this.bloked = bloked;
	}
	
	
	
}


