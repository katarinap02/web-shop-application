package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
 
import java.time.LocalDate;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.User;
import enums.Gender;
import enums.Role;

public class ChocolateDAO {
	
	private HashMap<Integer, Chocolate> chocolates = new HashMap<>();

	private String path = "";
	public ChocolateDAO() {
		super();
		// TODO Auto-generated constructor stub
		
	}
	
	public ChocolateDAO(String contextPath)
	{
		loadChocolates(contextPath);
		path = contextPath;
	}
	
	

	private void loadChocolates(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/users.txt");
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
	
	

	
	
	

}
