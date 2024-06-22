package dao;
//import com.google.gson.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Shopping;
import beans.User;
import enums.Gender;
import enums.Role;

public class ChocolateDAO {
	
	private HashMap<Integer, Chocolate> chocolates = new HashMap<>();
	//private ChocolateFactoryDAO dao = new ChocolateFactoryDAO();
//	Gson gson = new Gson();

	private String path = "";
	private ShoppingDAO shoppingDao;
	public ChocolateDAO(String contextPath)
	{
		loadChocolates(contextPath);
		path = contextPath;
	}
	
	public Collection<Chocolate> findAll()
	{
		return chocolates.values();
	}
	
	public Chocolate findById(int id)
	{
		for(Chocolate c: chocolates.values())
		{
			if(c.getId() == id)
				return c;
		}
		return null;
	}
	
	public Chocolate deleteChocolate(int id)
	{
		Chocolate c = findById(id);
		chocolates.remove(id);
		saveChocolates(path);
		return c;
	}
	
	public void increaseChocolateAmount(String orderId)
	{
       
		shoppingDao = new ShoppingDAO(path);
		 Shopping order = shoppingDao.findById(orderId);
		
		
		if(order != null)
		{
			List<Integer> chocolateIds = order.getChocolateIds();
	        HashMap<Integer, Integer> counts = new HashMap<>();
			
			
			for(int id : chocolateIds)
			{
				counts.put(id, counts.getOrDefault(id, 0) + 1);
			}
			
			 for (HashMap.Entry<Integer, Integer> entry : counts.entrySet()) {
		            System.out.println("Element: " + entry.getKey() + ", Count: " + entry.getValue());
		            
		            for(Chocolate choco : chocolates.values())
		            {
		            	if(choco.getId() == entry.getKey())
		            	{
		            		choco.setNumber(choco.getNumber() + entry.getValue());
		            		
		            		updateChocolate(choco.getId(), choco);
		            	}
		            }
		        }
			 
			 
			 
			
		}
		
	}
	
	public Chocolate addChocolate(Chocolate chocolate)
	{
		Integer maxId = -1;
		for(int id : chocolates.keySet())
		{
			if(id > maxId)
				maxId = id;
		}
		maxId++;
		chocolate.setId(maxId);
		chocolate.setNumber(0);
		chocolate.setStatus(false);
		chocolate = validateChocolate(chocolate);
		if(chocolate != null)
		{
			chocolates.put(chocolate.getId(), chocolate);
			saveChocolates(path);
			
			return chocolate;
		}
		else
			return null;
		
	}
	
	private Chocolate validateChocolate(Chocolate chocolate) {
		// TODO Auto-generated method stub
		if(chocolate.getName().isEmpty())
			return null;
		if(chocolate.getType().isEmpty())
			return null;
		if(chocolate.getKind().isEmpty())
			return null;
		if(chocolate.getDescription().isEmpty())
			return null;
		if(chocolate.getImageUrl().isEmpty())
			return null;
		if(chocolate.getPrice() <=0 || chocolate.getGrams() <= 0 || chocolate.getNumber() < 0)
			return null;
		
		return chocolate;
	}

	public Chocolate updateChocolate(int id, Chocolate chocolate)
	{
		int key = id;
		Chocolate c = chocolates.containsKey(key) ? chocolates.get(key) : null;
		chocolate = validateChocolate(chocolate);
		
		if(c != null && chocolate != null)
		{
			c.setName(chocolate.getName());
			c.setPrice(chocolate.getPrice());
			c.setKind(chocolate.getKind());
			c.setType(chocolate.getType());
			c.setGrams(chocolate.getGrams());
			c.setDescription(chocolate.getDescription());
			c.setImageUrl(chocolate.getImageUrl());
			c.setNumber(chocolate.getNumber());
			if(chocolate.getNumber() > 0)
				c.setStatus(true);
			else
				c.setStatus(false);
			
			saveChocolates(path);
			return c;
		}
		
		return null;
		
	}
	

	private void loadChocolates(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolates.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			System.out.println("Loading from: " + file.getAbsolutePath());
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					int id = Integer.parseInt(st.nextToken().trim());
					String name = st.nextToken().trim();
					double price = Double.parseDouble(st.nextToken().trim());
					String kind = st.nextToken().trim();
					int factoryId = Integer.parseInt(st.nextToken().trim());
					String type = st.nextToken().trim();
					double grams = Double.parseDouble(st.nextToken().trim());
					String description = st.nextToken().trim();
					String imageUrl = st.nextToken().trim();
					Boolean status = Boolean.parseBoolean(st.nextToken().trim());
					int number = Integer.parseInt(st.nextToken().trim());
					
					chocolates.put(id, new Chocolate(id, name, price, kind, factoryId, type, grams, description, imageUrl, status, number));
					
				
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
	
	private void saveChocolates(String contextPath) {
	    BufferedWriter out = null;
	    try {
	        File file = new File(contextPath + "/chocolates.txt");
	        out = new BufferedWriter(new FileWriter(file));
	        System.out.println("Saving to: " + file.getAbsolutePath());
	        
	        for (Chocolate chocolate : chocolates.values()) {
	            StringBuilder sb = new StringBuilder();
	            sb.append(chocolate.getId()).append(";");
	            sb.append(chocolate.getName()).append(";");
	            sb.append(chocolate.getPrice()).append(";");
	            sb.append(chocolate.getKind()).append(";");
	            sb.append(chocolate.getFactory()).append(";");
	            sb.append(chocolate.getType()).append(";");
	            sb.append(chocolate.getGrams()).append(";");
	            sb.append(chocolate.getDescription()).append(";");
	            sb.append(chocolate.getImageUrl()).append(";");
	            sb.append(chocolate.getStatus()).append(";");               
	            sb.append(chocolate.getNumber()).append("\n");

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


	public Collection<Chocolate> findByFactoryId(int id) {
		// TODO Auto-generated method stub
		
		ArrayList<Chocolate> tmpList = new ArrayList<>();
		for(Chocolate c: chocolates.values())
		{
			if(c.getFactory() == id)
				tmpList.add(c);
		}
		return tmpList;
	}
	
	

	
	
	

}
