package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.catalina.User;

import beans.Chocolate;
import beans.Shopping;
import beans.ShoppingCart;
import enums.Role;
import enums.ShoppingStatus;

public class ShoppingDAO {
	
	private HashMap<String, Shopping> buys = new HashMap<>();
	private String path = "";
	private UserDAO userDao;
	private ShoppingCartDAO cartDao;
	 private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	 
	public ShoppingDAO(String contextPath)
	{
		loadBuys(contextPath);
		path = contextPath;
		userDao = new UserDAO(path);
		cartDao = new ShoppingCartDAO(path);
	}
	
	public Collection<Shopping> findAll()
	{
		 return buys.values();
	}
	
	public Shopping createOrder(Shopping order, String username)
	{
		String id = generateRandomString(10);
		while(buys.keySet().contains(id))
		{
			id = generateRandomString(10);
		}
		
		order.setId(id);
		
		ShoppingCart cart = cartDao.findOpenedCart(username);
		beans.User user = userDao.findByUsername(username);
		
		ArrayList<Integer> chocolateIds = cart.getChocolateIds();
		if(chocolateIds.size() == 0)
			return null;
		
		ArrayList<Integer> uniqueChocolateIds = removeDuplicates(chocolateIds);
		
		order.setChocolateIds(uniqueChocolateIds);
		
		order.setFactoryId(cart.getFactoryId());
		order.setDateTime(LocalDateTime.now());
		order.setPrice(cart.getPrice());
		order.setCustomerName(user.getName() + " " + user.getSurname());
		order.setStatus(ShoppingStatus.PENDING);
		
		
		if(order != null)
		{
			buys.put(id, order);
			return order;
		}
		else
			return null;
			
		
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
					buys.put(id, new Shopping(id, chocolateIds, factoryId, dateTime, price, customerName, status));
					
				
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

}
