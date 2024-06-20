package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.CustomerRole;
import beans.Location;
import beans.ShoppingCart;
import beans.WorkingHours;

public class ShoppingCartDAO {

	private HashMap<Integer, ShoppingCart> carts = new HashMap<>();
	private String path = "";
	private ChocolateDAO chocolateDao;
	public ShoppingCartDAO(String contextPath)
	{
		loadShoppingCarts(contextPath);
		path = contextPath;
		chocolateDao = new ChocolateDAO(path);
	}
	
	public Collection<ShoppingCart> findAll()
	{
		 return carts.values();
	}
	
	public ShoppingCart openCart(ShoppingCart cart)
	{
		Integer maxId = -1;
		for(int id : carts.keySet())
		{
			if(id > maxId)
				maxId = id;
		}
		maxId++;
		cart.setId(maxId);
		ArrayList<Integer> startList = new ArrayList<Integer>();
		startList.add(-1);
	    System.out.println(startList.indexOf(0));
		cart.setChocolateIds(startList);
		cart.setOpened(true);
		System.out.println(maxId);
		if(cart != null)
		{
			carts.put(cart.getId(), cart);
			saveCarts(path);
			
			return cart;
		}
		else
			return null;
		
		
		
	}
	
	public void emptyOutCarts()
	{
		for(ShoppingCart cart : carts.values())
		{
			ArrayList<Integer> startList = new ArrayList<Integer>();
			startList.add(-1);
		    System.out.println(startList.indexOf(0));
			cart.setChocolateIds(startList);
			cart.setPrice(0);
			
			
		}
		
		saveCarts(path);
	}
	public ShoppingCart addToCart(int cartId, int chocolateId, int amount, double price, int factoryId)
	{
		int key = cartId;
		ShoppingCart c = carts.containsKey(key) ? carts.get(key) : null;
		
		
		if(c != null)
		{
			
			ArrayList<Integer> tmpList = c.getChocolateIds();
			for(int i = 1; i <= amount; i++)
				c.getChocolateIds().add(chocolateId);
			if(tmpList.get(0) == -1)
				tmpList.remove(0);
			c.setChocolateIds(tmpList);
			c.setPrice(c.getPrice() + amount * price);
			c.setFactoryId(factoryId);
			saveCarts(path);
			return c;
		}
		
		return null;
		
	}
	
	public ShoppingCart updateAmount(int cartId, int chocolateId, int amount, double price)
	{
		int key = cartId;
		ShoppingCart c = carts.containsKey(key) ? carts.get(key) : null;
		
		
		if(c != null)
		{
			
			ArrayList<Integer> tmpList = c.getChocolateIds();
			
			for(int id : tmpList)
			{
				if(id == chocolateId)
					c.setPrice(c.getPrice() - price);
			}
			tmpList.removeIf(id -> id == chocolateId);
			
			
			c.setChocolateIds(tmpList);
			
			for(int i = 1; i <= amount; i++)
				c.getChocolateIds().add(chocolateId);
			if(tmpList.get(0) == -1)
				tmpList.remove(0);
			c.setChocolateIds(tmpList);
			c.setPrice(c.getPrice() + amount * price);
			
			saveCarts(path);
			return c;
		}
		
		return null;
	}
	
	public int getAmountOfChocolate(int cartId, int chocolateId)
	{
		int key = cartId;
		ShoppingCart c = carts.containsKey(key) ? carts.get(key) : null;
		
		
		if(c != null)
		{
			
			ArrayList<Integer> chocolateIds = c.getChocolateIds();
			int count = 0;
			
			for(int id : chocolateIds)
			{
				if(id == chocolateId)
					count++;
			}
			return count;
		}
		
		return 0;
	}
	public ShoppingCart removeChocolatesById(int cartId, int chocolateId)
	{
		int key = cartId;
		ShoppingCart c = carts.containsKey(key) ? carts.get(key) : null;
		
		
		if(c != null)
		{
			
			ArrayList<Integer> chocolateIds = c.getChocolateIds();
			chocolateIds.removeIf(id -> id == chocolateId);
			
			c.setChocolateIds(chocolateIds);
			c.setPrice(calculateNewPrice(c));
			
			if(c.getPrice() == 0)
			{
				ArrayList<Integer> startList = new ArrayList<Integer>();
				startList.add(-1);
			    System.out.println(startList.indexOf(0));
				c.setChocolateIds(startList);
			}
			saveCarts(path);
			return c;
		}
		
		return null;
	}
	
	private double calculateNewPrice(ShoppingCart c) {
		ArrayList<Integer> chocolateIds = c.getChocolateIds();
		double price = 0;
		
		for(int id : chocolateIds)
		{
			price += chocolateDao.findById(id).getPrice();
		}
		
		return price;
	}

	public ShoppingCart findOpenedCart(String username)
	{
		for(ShoppingCart cart : carts.values())
		{
			if(cart.getCustomerName().equals(username))
				return cart;
		}
		
		return null;
	}
	
	public ArrayList<Chocolate> getItems(int cartId)
	{
		int key = cartId;
		
		ShoppingCart c = carts.containsKey(key) ? carts.get(key) : null;
		ArrayList<Integer> chocolateIds =  new ArrayList<>();
		if(c != null)
		{
			chocolateIds = c.getChocolateIds();
		    ArrayList<Chocolate> result = new ArrayList<>();
		    for(Chocolate chocolate : chocolateDao.findAll())
		    {
		    	if(chocolateIds.contains(chocolate.getId()))
		    		result.add(chocolate);
		    	
		    }
		    
		    return result;
		    
		}
		
		return null;
	}
	
	public void saveCarts(String contextPath) {
	    BufferedWriter out = null;
	    try {
	        File file = new File(contextPath + "/shopping_carts.txt");
	        out = new BufferedWriter(new FileWriter(file));
	        System.out.println("Saving to: " + file.getAbsolutePath());
	        
	        for (ShoppingCart cart : carts.values()) {
	            StringBuilder sb = new StringBuilder();
	            sb.append(cart.getId()).append(";");
	          
	            
	            List<Integer> chocolates = cart.getChocolateIds();
	            for (int i = 0; i < chocolates.size(); i++) {
	                sb.append(chocolates.get(i));
	                if (i < chocolates.size() - 1) {
	                    sb.append(",");
	                }
	            }
	            sb.append(";");
	            
	      
	            
	            // URL and rate
	            sb.append(cart.getCustomerName()).append(";");
	            sb.append(cart.getPrice()).append(";");
	            sb.append(cart.isOpened()).append(";");
	            sb.append(cart.getFactoryId()).append("\n");

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
	private void loadShoppingCarts(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/shopping_carts.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					int id = Integer.parseInt(st.nextToken().trim());
					
					ArrayList<Integer> chocolateIds = new ArrayList<>();
                    String chocolateIdsToken = st.nextToken().trim();
                    if (!chocolateIdsToken.isEmpty()) {
                        String[] chocolateIdTokens = chocolateIdsToken.split(",");
                        for (String chocolateId : chocolateIdTokens) {
                            chocolateIds.add(Integer.parseInt(chocolateId.trim()));
                        }
                    }
                    
                    String customerName = (st.nextToken().trim());
					
					double price = Double.parseDouble(st.nextToken().trim());
					
					boolean isOpened = Boolean.parseBoolean(st.nextToken().trim());
					
					int factoryId = Integer.parseInt(st.nextToken().trim());
					carts.put(id, new ShoppingCart(id, chocolateIds, customerName, price, isOpened, factoryId));
					
				
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
}
