package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Comment;
import beans.Location;
import beans.User;
import beans.WorkingHours;

public class ChocolateFactoryDAO {
	private HashMap<Integer, ChocolateFactory> factories = new HashMap<>();
	private String path = "";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	private UserDAO userDao;
	private CommentDAO commentDao;
	private ChocolateDAO chocolateDao;
	
	public ChocolateFactoryDAO(){	
		ArrayList<Integer> list = new ArrayList<>();
		WorkingHours hours = new WorkingHours(LocalTime.of(8, 0, 0), LocalTime.of(17, 0, 0));
		Location location = new Location(0, 45.46, 19.2, "Sutjeska 3 NoviSad 21000");
		factories.put(0, new ChocolateFactory(0, "Stark", list, hours, true, location,
				"https://upload.wikimedia.org/wikipedia/sr/d/de/%D0%A8%D1%82%D0%B0%D1%80%D0%BA_%28%D0%BB%D0%BE%D0%B3%D0%BE%2C_2022%29.png", 3.3)); 
	}
	
	public ChocolateFactoryDAO(String contextPath)
	{
		loadFactories(contextPath);
		path = contextPath;
		calculateAverageGrades();
	}
	private void calculateAverageGrades() {
		// TODO Auto-generated method stub
		commentDao = new CommentDAO(path);
		
		for(ChocolateFactory f : factories.values())
		{
			Collection<Comment> comments = commentDao.getByFactoryId(f.getId());
			double sum = 0;
			double count = 0;
			
			for(Comment c : comments)
			{
				sum += c.getRate();
				count++;
			}
			
			if(count != 0)
				f.setRate(sum/count);
			else
				f.setRate(0);
			
		}
		
		saveFactory(path);
	}
	public ChocolateFactory getByManager(String username)
	{
		userDao = new UserDAO(path);
		User user = userDao.findByUsername(username);
		int factoryId = user.getFactory().getId();
		
		ChocolateFactory factory = factories.containsKey(factoryId) ? factories.get(factoryId) : null;
		return factory;
		
		
		
	}
	
	
	
	
	public ChocolateFactory addFactory(ChocolateFactory factory)
	{
		Integer maxId = -1;
		for(int id : factories.keySet())
		{
			if(id > maxId)
				maxId = id;
		}
		maxId++;
		factory.setId(maxId);
		factory.setChocolates(new ArrayList<Integer>()); //proveriti da li radi
		factory.setIsWorking(false);
		factory.setRate(-1.0);
		factory.setDeleted(false);
		factory = validateFactory(factory);
		if(factory != null)
		{
			factories.put(factory.getId(), factory);
			saveFactory(path);
			return factory;
		}
		else
			return null;
		
	}
	
	public ChocolateFactory deleteById(int id)
	{
		ChocolateFactory c = findById(id);
		c.setDeleted(true);
		saveFactory(path);
		return c;
	}
	
	private ChocolateFactory validateFactory(ChocolateFactory fc) {
		// TODO Auto-generated method stub
		if(fc.getName().isEmpty())
			return null;
		if(fc.getLocation().getAddress().isEmpty())
			return null;
		if(fc.getWorkingHours().getStartHour().isAfter(fc.getWorkingHours().getEndHour()))
			return null;
		if(fc.getLogoUrl().isEmpty())
			return null;
		
		return fc;
	}
	
	public Collection<ChocolateFactory> findAll()
	{
		
		 ArrayList<ChocolateFactory> factoryList = new ArrayList<>();
		 for(ChocolateFactory c : factories.values())
		 {
			 if(!c.isDeleted())
				 factoryList.add(c);
		 }
		Collections.sort(factoryList, Comparator.comparing(ChocolateFactory::getIsWorking).reversed()); //sortira po tome da li je isWorking true
		return factoryList;
		
	}
	
	private void loadFactories(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/factories.txt");
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
					
					ArrayList<Integer> chocolateIds = new ArrayList<>();
                    String chocolateIdsToken = st.nextToken().trim();
                    if (!chocolateIdsToken.isEmpty() && chocolateIdsToken.equals("-1")) {
                        String[] chocolateIdTokens = chocolateIdsToken.split(",");
                        for (String chocolateId : chocolateIdTokens) {
                            chocolateIds.add(Integer.parseInt(chocolateId.trim()));
                        }
                    }
					
					LocalTime startHour = LocalTime.parse(st.nextToken().trim(), formatter);
					LocalTime endHour = LocalTime.parse(st.nextToken().trim(), formatter);
					WorkingHours working = new WorkingHours(startHour, endHour);
					Boolean isWorking = Boolean.parseBoolean(st.nextToken().trim());
					int idLocation = Integer.parseInt(st.nextToken().trim());
					double latitude = Double.parseDouble(st.nextToken().trim());
					double longitude = Double.parseDouble(st.nextToken().trim());
					String address = st.nextToken().trim();
					Location location = new Location(idLocation, latitude, longitude, address);
					String url = st.nextToken().trim();
					double rate = Double.parseDouble(st.nextToken().trim());
					Boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					ChocolateFactory c = new ChocolateFactory(id, name, chocolateIds, working, isWorking, location, url, rate);
					c.setDeleted(deleted);
						factories.put(id, c);
					
					
					
				
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
	
	

	public void saveFactory(String contextPath) {
	    BufferedWriter out = null;
	    try {
	        File file = new File(contextPath + "/factories.txt");
	        out = new BufferedWriter(new FileWriter(file));
	        System.out.println("Saving to: " + file.getAbsolutePath());
	        
	        for (ChocolateFactory factory : factories.values()) {
	            StringBuilder sb = new StringBuilder();
	            sb.append(factory.getId()).append(";");
	            sb.append(factory.getName()).append(";");
	            
	            ArrayList<Integer> chocolates = factory.getChocolates();
	            if(chocolates.isEmpty())
	            {
	            	sb.append(-1).append(";");
	            }
	            else {
	            for (int i = 0; i < chocolates.size(); i++) {
	                sb.append(chocolates.get(i));
	                if (i < chocolates.size() - 1) {
	                    sb.append(",");
	                }
	            }
	            sb.append(";");
	            }
	            
	            // Working hours
	            WorkingHours wr = factory.getWorkingHours();
	            sb.append(wr.getStartHour()).append(";");
	            sb.append(wr.getEndHour()).append(";");
	            
	            // IsWorking flag
	            sb.append(factory.getIsWorking()).append(";");
	            
	            // Location
	            Location location = factory.getLocation();
	            sb.append(location.getId()).append(";");
	            sb.append(location.getLatitude()).append(";");
	            sb.append(location.getLongitude()).append(";");
	            sb.append(location.getAddress()).append(";");
	            
	            // URL and rate
	            sb.append(factory.getLogoUrl()).append(";");
	            sb.append(factory.getRate()).append(";");
	            sb.append(factory.isDeleted()).append("\n");
	            out.write(sb.toString());
	            
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

	public ChocolateFactory findById(int id) {
		// TODO Auto-generated method stub
		for(ChocolateFactory f: factories.values())
		{
			if(f.getId() == id)
				return f;
		}
		return null;
	}
	
	
	private ArrayList<String> getChocolateNames(int factoryId)
	{
		chocolateDao = new ChocolateDAO(path);
		ArrayList<String> result = new ArrayList<String>();
		
		for(Chocolate chocolate : chocolateDao.findAll())
		{
			if(chocolate.getFactory() == factoryId)
				result.add(chocolate.getName());
				
		}
		
		return result;
		
	}
	
	private boolean factoryContainsChocolate(ArrayList<String> chocolateNames, String chocolate)
	{
		for(String name : chocolateNames)
		{
			if(name.toLowerCase().contains(chocolate.toLowerCase()))
				return true;
		}
		
		return false;
	}
	
	public Collection<ChocolateFactory> searchFactories(String factory, String chocolate, String address, String rating)
	{
		if(!rating.isEmpty())
			return factories.values().stream().filter(x -> x.getName().toLowerCase().contains(factory.toLowerCase()) && x.getLocation().getAddress().toLowerCase().contains(address.toLowerCase()) && x.getRate() == Double.parseDouble(rating) && factoryContainsChocolate(getChocolateNames(x.getId()), chocolate)).collect(Collectors.toList());
		else
			return factories.values().stream().filter(x -> x.getName().toLowerCase().contains(factory.toLowerCase()) && x.getLocation().getAddress().toLowerCase().contains(address.toLowerCase()) && factoryContainsChocolate(getChocolateNames(x.getId()), chocolate)).collect(Collectors.toList());
	}
	

}
