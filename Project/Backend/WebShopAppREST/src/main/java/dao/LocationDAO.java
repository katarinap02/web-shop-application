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
import beans.Location;
import beans.WorkingHours;

public class LocationDAO {
	
	private HashMap<Integer, Location> locations = new HashMap<>();
	private String path = "";
	
	public LocationDAO(){	
	}
	
	public LocationDAO(String contextPath)
	{
		loadLocations(contextPath);
		path = contextPath;
	}
	
	public Collection<Location> findAll()
	{
		 return locations.values();
	}
	
	private void loadLocations(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/locations.txt");
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
					double latitude = Double.parseDouble(st.nextToken().trim());
					double longitude = Double.parseDouble(st.nextToken().trim());
					String address = st.nextToken().trim();
					
					locations.put(id, new Location(id, latitude, longitude, address));
					
				
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
