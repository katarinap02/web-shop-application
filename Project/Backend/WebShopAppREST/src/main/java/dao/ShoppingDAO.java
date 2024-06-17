package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Shopping;
import beans.ShoppingCart;
import enums.Role;
import enums.ShoppingStatus;

public class ShoppingDAO {
	
	private HashMap<String, Shopping> buys = new HashMap<>();
	private String path = "";
	
	public ShoppingDAO(String contextPath)
	{
		loadBuys(contextPath);
		path = contextPath;
	}
	
	public Collection<Shopping> findAll()
	{
		 return buys.values();
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
