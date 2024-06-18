package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.CustomerRole;
import beans.ShoppingCart;

public class ShoppingCartDAO {

	private HashMap<Integer, ShoppingCart> carts = new HashMap<>();
	private String path = "";
	
	public ShoppingCartDAO(String contextPath)
	{
		loadShoppingCarts(contextPath);
		path = contextPath;
	}
	
	public Collection<ShoppingCart> findAll()
	{
		 return carts.values();
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
                    
                    int customerId = Integer.parseInt(st.nextToken().trim());
					
					double price = Double.parseDouble(st.nextToken().trim());
					
					
					carts.put(id, new ShoppingCart(id, chocolateIds, customerId, price));
					
				
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
