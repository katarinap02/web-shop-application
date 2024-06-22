package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.catalina.User;

import beans.Chocolate;
import beans.RejectedOrder;
import beans.Shopping;
import beans.ShoppingCart;
import enums.Role;
import enums.ShoppingStatus;

public class ShoppingDAO {
	
	private HashMap<String, Shopping> buys = new HashMap<>();
	private String path = "";
	private UserDAO userDao;
	private ShoppingCartDAO cartDao;
	private RejectedOrderDAO rejectionDao;
	private ChocolateDAO chocolateDao;
	private ChocolateFactoryDAO factoryDao;
	
	 private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	 
	public ShoppingDAO(String contextPath)
	{
		loadBuys(contextPath);
		path = contextPath;
		
	}
	
	
	
	
	public Collection<Shopping> findAll()
	{
		 return buys.values();
	}
	
	public Collection<Shopping> findByCustomer(String username)
	{
		return buys.values().stream().filter(x -> x.getUsername().equals(username)).collect(Collectors.toList());
	}
	
	public Collection<Shopping> findByManager(int factoryId)
	{
		return buys.values().stream().filter(x -> x.getFactoryId() == factoryId).collect(Collectors.toList());
	}
	
	public Shopping createOrder(String username)
	{
		factoryDao = new ChocolateFactoryDAO(path);
		
		String id = generateRandomString(10);
		while(buys.keySet().contains(id))
		{
			id = generateRandomString(10);
		}
		
		Shopping order = new Shopping();
		order.setId(id);
		userDao = new UserDAO(path);
		cartDao = new ShoppingCartDAO(path);
		ShoppingCart cart = cartDao.findOpenedCart(username);
		beans.User user = userDao.findByUsername(username);
		
		ArrayList<Integer> chocolateIds = cart.getChocolateIds();
		/*if(chocolateIds.size() == 0)
			return null;
		
		ArrayList<Integer> uniqueChocolateIds = removeDuplicates(chocolateIds);
		*/
		order.setChocolateIds(chocolateIds);
		
		order.setFactoryId(cart.getFactoryId());
		order.setDateTime(LocalDateTime.now());
		order.setPrice(cart.getPrice());
		order.setCustomerName(user.getName() + " " + user.getSurname());
		order.setStatus(ShoppingStatus.PENDING);
		order.setUsername(username);
		order.setFactoryName(factoryDao.findById(cart.getFactoryId()).getName());
		System.out.println(cart.getPrice());
		if(order != null)
		{
			
			buys.put(id, order);
			saveBuys(path);
			
			return order;
		}
		else
			return null;
			
		
	}
	
	public void cancelOrder(String orderId)
	{
		Shopping order = buys.containsKey(orderId) ? buys.get(orderId) : null;
		
		if(order != null)
		{
			order.setStatus(ShoppingStatus.CANCELLED);
			saveBuys(path);
		}
	}
	
	public void approveOrder(String orderId)
	{
	  Shopping order = buys.containsKey(orderId) ? buys.get(orderId) : null;
		
		if(order != null)
		{
			order.setStatus(ShoppingStatus.APPROVED);
			saveBuys(path);
		}
		
	}
	
	public void rejectOrder(String orderId, String comment)
	{
	  Shopping order = buys.containsKey(orderId) ? buys.get(orderId) : null;
		
		if(order != null)
		{
			order.setStatus(ShoppingStatus.REJECTED);
			
			saveBuys(path);
			
			rejectionDao = new RejectedOrderDAO(path);
			RejectedOrder rejection = new RejectedOrder();
			rejection.setOrderId(orderId);
			rejection.setComment(comment);
			
			rejectionDao.addCancellation(rejection);
			
		    
			
			
		}
		
	}
	
	private ArrayList<Integer> removeDuplicates(ArrayList<Integer> list)
	{
		HashSet<Integer> set = new HashSet<>(list);
		return new ArrayList<>(set);
	}
	
	private String generateRandomString(int length)
	{
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder(length);
		
		for(int i = 0; i < length; i++)
		{
			int index = random.nextInt(CHARACTERS.length());
			stringBuilder.append(CHARACTERS.charAt(index));
			
		}
		
		return stringBuilder.toString();
		
	}
	
	public void saveBuys(String contextPath) {
	    BufferedWriter out = null;
	    try {
	        File file = new File(contextPath + "/buys.txt");
	        out = new BufferedWriter(new FileWriter(file));
	        System.out.println("Saving to: " + file.getAbsolutePath());
	        
	        for (Shopping buy : buys.values()) {
	            StringBuilder sb = new StringBuilder();
	            sb.append(buy.getId()).append(";");
	          
	            
	            List<Integer> chocolates = buy.getChocolateIds();
	            for (int i = 0; i < chocolates.size(); i++) {
	                sb.append(chocolates.get(i));
	                if (i < chocolates.size() - 1) {
	                    sb.append(",");
	                }
	            }
	            sb.append(";");
	            
	      
	            sb.append(buy.getFactoryId()).append(";");
	            sb.append(buy.getDateTime()).append(";");
	            sb.append(buy.getPrice()).append(";");
	            sb.append(buy.getCustomerName()).append(";");
	            sb.append(buy.getStatus()).append(";");
	            sb.append(buy.getUsername()).append(";");
	            sb.append(buy.getFactoryName()).append("\n");
	           
	            
	            

	            out.write(sb.toString());
	            System.out.println("Written: " + sb.toString());
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    } finally {
	        if (out != null) {
	            try {
	            	out.flush();
	                out.close();
	                System.out.println("BufferedWriter closed successfully.");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	private void loadBuys(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/buys.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String id = st.nextToken().trim();
					
					ArrayList<Integer> chocolateIds = new ArrayList<>();
                    String chocolateIdsToken = st.nextToken().trim();
                    if (!chocolateIdsToken.isEmpty()) {
                        String[] chocolateIdTokens = chocolateIdsToken.split(",");
                        for (String chocolateId : chocolateIdTokens) {
                            chocolateIds.add(Integer.parseInt(chocolateId.trim()));
                        }
                    }
                    
                    int factoryId = Integer.parseInt(st.nextToken().trim());
					LocalDateTime dateTime = LocalDateTime.parse(st.nextToken().trim());
					double price = Double.parseDouble(st.nextToken().trim());
					String customerName = st.nextToken().trim();
					ShoppingStatus status = convertToStatus(st.nextToken().trim());
					String username = st.nextToken().trim();
					String factoryName = st.nextToken().trim();
					buys.put(id, new Shopping(id, chocolateIds, factoryId, dateTime, price, customerName, status, username, factoryName));
					
				
				}
				
			}
		} 
		catch (Exception ex) {
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

	private ShoppingStatus convertToStatus(String input) {
		// TODO Auto-generated method stub
		if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            return ShoppingStatus.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + input);
        }
	}


   public Collection<Shopping> searchShoppingsManager(String factoryName, double startPrice, double endPrice, String startDate, String endDate, String managerName)
   {
	   
	 
	   LocalDate convertedStartDate = LocalDate.parse(startDate);
	   LocalDate convertedEndDate = LocalDate.parse(endDate);
	   userDao = new UserDAO(path);
	   beans.User manager = userDao.findByUsername(managerName);
	
	   return buys.values().stream().filter(x -> x.getFactoryName().toLowerCase().contains(factoryName.toLowerCase()) && x.getPrice() >= startPrice && x.getPrice() <= endPrice && x.getDateTime().toLocalDate().isAfter(convertedStartDate)  && x.getDateTime().toLocalDate().isBefore(convertedEndDate)  && x.getFactoryId() == manager.getFactory().getId()).collect(Collectors.toList());
   }
   
   public Collection<Shopping> searchShoppingsCustomer(String factoryName, double startPrice, double endPrice, String startDate, String endDate, String customerName)
   {
	   
	 
	   LocalDate convertedStartDate = LocalDate.parse(startDate);
	   LocalDate convertedEndDate = LocalDate.parse(endDate);
	
	   return buys.values().stream().filter(x -> x.getFactoryName().toLowerCase().contains(factoryName.toLowerCase()) && x.getPrice() >= startPrice && x.getPrice() <= endPrice && x.getDateTime().toLocalDate().isAfter(convertedStartDate)  && x.getDateTime().toLocalDate().isBefore(convertedEndDate)  && x.getUsername().equals(customerName)).collect(Collectors.toList());
   }

	public Shopping findById(String orderId) {
		// TODO Auto-generated method stub
		for(Shopping b: buys.values())
		{
			if(b.getId().equals(orderId))
				return b;
		}
		return null;
	}

}
