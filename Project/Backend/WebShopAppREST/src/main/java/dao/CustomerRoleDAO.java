package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.CustomerRole;
import beans.Location;

public class CustomerRoleDAO {
	
	private HashMap<Integer, CustomerRole> roles = new HashMap<>();
	private String path = "";
	
	public CustomerRoleDAO(String contextPath)
	{
		loadCustomerRoles(contextPath);
		path = contextPath;
	}
	
	public Collection<CustomerRole> findAll()
	{
		 return roles.values();
	}
	
	private void loadCustomerRoles(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/customer_roles.txt");
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
					double discount = Double.parseDouble(st.nextToken().trim());
					int requiredPoints = Integer.parseInt(st.nextToken().trim());
					
					roles.put(id, new CustomerRole(id, name, discount, requiredPoints));
					
				
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

	public CustomerRole findById(int customerId) {
		// TODO Auto-generated method stub
		CustomerRole role = roles.containsKey(customerId) ? roles.get(customerId) : null;
		return role;
	}

}
