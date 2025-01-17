package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.lang.model.element.ModuleElement.UsesDirective;

import beans.ChocolateFactory;
import beans.CustomerRole;
import beans.Shopping;
import beans.ShoppingCart;
import beans.User;
import enums.Gender;
import enums.Role;

public class UserDAO {
	
	private HashMap<String, User> users = new HashMap<>();
	private ChocolateFactoryDAO factoryDAO;
	String path = "";
	private ShoppingDAO shoppingDao;
	private CustomerRoleDAO roleDao;
	private ShoppingCartDAO cartDao;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public UserDAO()
	{}
	
	public UserDAO(String contextPath)
	{
		path = contextPath;
		factoryDAO = new ChocolateFactoryDAO(path);
		loadUsers(contextPath);
		
		
	}
	
	public ArrayList<User> getCustomersByFactory(int factoryId)
	{
		ArrayList<User> result = new ArrayList<>();
		
		shoppingDao = new ShoppingDAO(path);
		
		Collection<Shopping> shoppings = shoppingDao.findApprovedByManager(factoryId);
		
		for(User user : users.values())
		{
			for(Shopping shopping : shoppings)
			{
				if(user.getUsername().equals(shopping.getUsername()) && !containsUser(user.getUsername(), result))
					result.add(user);
			}
		}
		
		return result;
		
	}
	
	private boolean containsUser(String username, ArrayList<User> result) {
		// TODO Auto-generated method stub
		for(User u : result)
		{
			if(u.getUsername().equals(username))
				return true;
			
		}
		return false;
	}

	public Collection<User> findAll()
	{
		return users.values();
	}
	
	public Collection<User> findManagers()
	{
		//loadUsers(path);
		ArrayList<User> managers = new ArrayList<>();
		for(User u : users.values())
		{
			if(u.getRole() == Role.MANAGER && u.getFactory() == null)
			{
				managers.add(u);
			}
			else
			{
				System.out.println(u.getUsername());
			}
		}
		
		return managers;
	}
	
	public User findByUsername(String username)
	{
		for(User u: users.values())
		{
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}
	
	public User find(String username, String password) {
		
		for(User u: users.values())
		{
			System.out.println(u.getUsername());
		}
		
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}
	
	public User addUser(User user)
	{
		for(User u : users.values())
		{
			if(u.getUsername().equals(user.getUsername()))
				return null;
		}
		
		if(user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty() || user.getSurname().isEmpty())
		{
			return null;
		}
		
		
		users.put(user.getUsername(), user);
		saveUsers(path); 
		
		if(user.getRole() == Role.CUSTOMER)
		{
			cartDao = new ShoppingCartDAO(path);
			ShoppingCart cart = new ShoppingCart();
			
			cart.setCustomerName(user.getUsername());
			cart.setFactoryId(-1);
			cartDao.openCart(cart);
			
		}
		return user;
	}
	
	public User editUser(User user) {
		for(User u : users.values())
		{
			if(u.getUsername().equals(user.getUsername()))
			{
				if(user.getName() == "" || user.getSurname() == "")
				{
					return null;
				}
				u.setName(user.getName());
				u.setSurname(user.getSurname());
				u.setGender(user.getGender());
				u.setBirthDate(user.getBirthDate());
				saveUsers(path);
				return user;
			}
				
		}
		
		return null;
	}
	
	
	public User editManager(User user, ChocolateFactory factory ,int id)
	{
		System.out.println(id);
		if(factory == null)
			return null;
		for(User u : users.values())
		{
			if(user.getUsername().equals(u.getUsername()))
			{
				System.out.println(id);
				u.setFactory(factory);
				saveUsers(path);
				return u;
			}
		}
		return null;
	}
	
	public User BlokUser(String username) {
		
		for(User u : users.values())
		{
			if(u.getUsername().equals(username))
			{
				u.setBloked(true);
				saveUsers(path);
				return u;
			}
				
		}
		
		return null;
	}
	
	
	
	public void deleteChocolateId(int id) {
		
		for(User u: users.values())
		{
			if(u.getFactory() != null)
			{
				if(u.getFactory().getId() == id)
				{
					u.setFactory(null);
				}
			}
		}
		saveUsers(path);
	}
	
	
	public LocalDate convertToDate(String date)
	{

		LocalDate local = LocalDate.parse(date, formatter);
		return local;

	}
	
	public Gender convertToGender(String input)
	{
		if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            return Gender.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender: " + input);
        }
	}
	
	public Role convertToRole(String input)
	{
		if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            return Role.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender: " + input);
        }
	}
	
	public ChocolateFactory convertToFactory(int id)
	{
		if(id == -1)
			return null;
		ChocolateFactory factory = factoryDAO.findById(id);
		return factory;
		
		
	}
	
	private void loadUsers(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/users.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = convertToGender(st.nextToken().trim());
					LocalDate birthdate = LocalDate.parse(st.nextToken().trim(), formatter);
					Role role = convertToRole(st.nextToken().trim());
					int factoryId = Integer.parseInt(st.nextToken().trim());
					boolean bloked = Boolean.parseBoolean(st.nextToken().trim());
					ChocolateFactory factory = convertToFactory(factoryId);
					double points = Double.parseDouble(st.nextToken().trim());
					int customerId = Integer.parseInt(st.nextToken().trim());
					boolean isSuspicious = Boolean.parseBoolean(st.nextToken().trim());
					
					ArrayList<LocalDate> cancelDate = new ArrayList<>();
					
			            String datesTokens = st.nextToken().trim();
			            if (!datesTokens.isEmpty()) {
			                // Split the datesTokens string by commas
			                String[] dateTokens = datesTokens.split(",");
			                for (String dateStr : dateTokens) {
			                    // Parse each date string and add it to the list
			                    cancelDate.add(LocalDate.parse(dateStr.trim(), formatter));
			                }
			            }
			        
					User user = new User(username, password, firstName, lastName, gender, birthdate, role, points, customerId);
					user.setFactory(factory);
					if(bloked == true)
					{
						user.setBloked(true);
					}
					user.setSuspicious(isSuspicious);
					user.setRejectedOrders(cancelDate);
				users.put(username, user);
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	private void saveUsers(String contextPath) {
        BufferedWriter out = null;
        try {
            File file = new File(contextPath + "users.txt");
            out = new BufferedWriter(new FileWriter(file));
            System.out.println(contextPath + "users.txt");
            for (User user : users.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getUsername()).append(";");
                sb.append(user.getPassword()).append(";");
                sb.append(user.getName()).append(";");
                sb.append(user.getSurname()).append(";");
                sb.append(user.getGender().name()).append(";");
                sb.append(user.getBirthDate().format(formatter)).append(";");
                sb.append(user.getRole().name()).append(";");
                if(user.getFactory() != null)
                sb.append(user.getFactory().getId()).append(";");
                else 
                	sb.append(-1).append(";");

                


                if(user.getBloked() == true)
                {
                	sb.append("True").append(";");
                }
                else {
                	sb.append("False").append(";");
                }
                sb.append(user.getPoints()).append(";");
                sb.append(user.getCustomerId()).append(";");
                sb.append(user.isSuspicious()).append(";");
                
                ArrayList<LocalDate> cancelDates = user.getRejectedOrders();
	            if(cancelDates == null || cancelDates.isEmpty())
	            {
	            	sb.append("2023-01-01").append("\n");
	            }
	            else {
	            for (int i = 0; i < cancelDates.size(); i++) {
	                sb.append(cancelDates.get(i));
	                if (i < cancelDates.size() - 1) {
	                    sb.append(",");
	                }
	            }
	            sb.append("\n");
	            }
                
                out.write(sb.toString());
               
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    System.out.println("uslo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public void increaseCustomerPoints(String username, double price)
	{
		User user = users.containsKey(username) ? users.get(username) : null;
		
		if(user != null)
		{
		
			user.setPoints(user.getPoints() + (price/1000)*133);
			//System.out.println(user.getPoints());
			saveUsers(path);
			setCustomerRole(user);
		}
		
		
	}
	
	public void decreaseCustomerPoints(String username, double price) //ovde cu dodati da se stavi danasnji datum
	{
		User user = users.containsKey(username) ? users.get(username) : null;
		
		if(user != null)
		{
			user.setPoints(user.getPoints() - (price/1000)*133*4);
			ArrayList<LocalDate> rejectedOrders = user.getRejectedOrders();
			System.out.println("Initial Rejected Orders: " + rejectedOrders);
			rejectedOrders.add(LocalDate.now());
			user.setRejectedOrders(rejectedOrders);
			System.out.println("Updated Rejected Orders: " + user.getRejectedOrders());
			saveUsers(path);
			setCustomerRole(user);
			checkIfSuspicious(user);
		}
		
		
	}
	
	public void checkIfSuspicious(User user)
	{
		ArrayList<LocalDate> numDates = new ArrayList<LocalDate>();
		LocalDate today = LocalDate.now();

        // Calculate the date one month ago
        LocalDate oneMonthAgo = today.minusMonths(1);
		for(LocalDate date : user.getRejectedOrders())
		{
			if(date.isAfter(oneMonthAgo) && date.isBefore(today) || date.isEqual(today))
				numDates.add(date);
		}
		
		if(numDates.size() > 5)
		{
			user.setSuspicious(true);
			saveUsers(path);
		}
			
	}
	
	private void setCustomerRole(User user) {
		
		if(user.getPoints() >= 4000)
			user.setCustomerId(0);
		else if(user.getPoints() >= 3000)
			user.setCustomerId(1);
		else if(user.getPoints() >= 1500)
			user.setCustomerId(2);
		else
			user.setCustomerId(3);
		
		saveUsers(path);
		
		
	}

	public int getFactoryId(String username)
	{
		for(User u: users.values())
		{
			if(u.getUsername().equals(username))
			{	
				ChocolateFactory factory = u.getFactory();
				
				if(factory == null)
					return -1;
				else
					return factory.getId();
			
			    
			}
		}
		
		return -1;
	}
	
	
	public ChocolateFactory getFactory(String username)
	{
		for(User u: users.values())
		{
			if(u.getUsername().equals(username))
			{	
				return u.getFactory();
			
			    
			}
		}
		
		return null;
	}
	
	
	public double getDiscount(String username)
	{
       User user = users.containsKey(username) ? users.get(username) : null;
		
		if(user != null)
		{
			roleDao = new CustomerRoleDAO(path);
			CustomerRole role = roleDao.findById(user.getCustomerId());
			return role.getDiscount();
		}
		
		return 0;
	}
	
	public Collection<User> searchUsers(String name, String surname, String username)
	{
		return users.values().stream().filter(x -> x.getName().toLowerCase().contains(name.toLowerCase()) && x.getSurname().toLowerCase().contains(surname.toLowerCase()) && x.getUsername().toLowerCase().contains(username.toLowerCase())).collect(Collectors.toList());
	}
	

	

	

	

	

}
