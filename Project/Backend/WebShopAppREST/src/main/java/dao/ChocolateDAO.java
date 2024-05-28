package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Collection;
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
		chocolates.put(chocolate.getId(), chocolate);
		saveChocolates(path);
		return chocolate;
	}
	

	private void loadChocolates(String contextPath) {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolates.txt");
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
	
	private void saveChocolates(String contextPath) {
        BufferedWriter out = null;
        try {
            File file = new File(contextPath + "chocolates.txt");
            out = new BufferedWriter(new FileWriter(file));
            System.out.println(contextPath + "chocolates.txt");
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
               
                System.out.println(sb.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    System.out.println("uslo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	

	
	
	

}
