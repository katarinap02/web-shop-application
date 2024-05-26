package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.ChocolateFactory;
import beans.WorkingHours;

public class ChocolateFactoryDAO {
	private HashMap<Integer, ChocolateFactory> factories = new HashMap<>();
	private String path = "";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public ChocolateFactoryDAO(){	
	}
	
	public ChocolateFactoryDAO(String contextPath)
	{
		loadFactories(contextPath);
		path = contextPath;
	}
	
	public Collection<ChocolateFactory> findAll()
	{
		 ArrayList<ChocolateFactory> factoryList = new ArrayList<>(factories.values());
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
                    if (!chocolateIdsToken.isEmpty()) {
                        String[] chocolateIdTokens = chocolateIdsToken.split(",");
                        for (String chocolateId : chocolateIdTokens) {
                            chocolateIds.add(Integer.parseInt(chocolateId.trim()));
                        }
                    }
					
					LocalTime startHour = LocalTime.parse(st.nextToken().trim(), formatter);
					LocalTime endHour = LocalTime.parse(st.nextToken().trim(), formatter);
					WorkingHours working = new WorkingHours(startHour, endHour);
					Boolean isWorking = Boolean.parseBoolean(st.nextToken().trim());
					int locationId = Integer.parseInt(st.nextToken().trim());
					String url = st.nextToken().trim();
					double rate = Double.parseDouble(st.nextToken().trim());
					
					
					factories.put(id, new ChocolateFactory(id, name, chocolateIds, working, isWorking, locationId, url, rate));
					
				
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
