package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.RejectedOrder;
import beans.Chocolate;

public class RejectedOrderDAO {

	private HashMap<Integer, RejectedOrder> cancellations = new HashMap<>();
    private String path = "";
	
	public RejectedOrderDAO(String contextPath)
	{
		loadCancellations(contextPath);
		path = contextPath;
	}
	
	public RejectedOrder addCancellation(RejectedOrder cancellation)
	{
		Integer maxId = -1;
		for(int id : cancellations.keySet())
		{
			if(id > maxId)
				maxId = id;
		}
		maxId++;
		cancellation.setId(maxId);
		
		if(cancellation != null)
		{
			cancellations.put(cancellation.getId(), cancellation);
			saveCancellations(path);
			
			return cancellation;
		}
		else
			return null;
		
	}
	
	private void loadCancellations(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/rejected_orders.txt");
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
					String orderId = st.nextToken().trim();
					String comment = st.nextToken().trim();
					
					cancellations.put(id, new RejectedOrder(id, orderId, comment));
					
				
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
	
	private void saveCancellations(String contextPath) {
	    BufferedWriter out = null;
	    try {
	        File file = new File(contextPath + "/rejected_orders.txt");
	        out = new BufferedWriter(new FileWriter(file));
	        System.out.println("Saving to: " + file.getAbsolutePath());
	        
	        for (RejectedOrder cancellation : cancellations.values()) {
	            StringBuilder sb = new StringBuilder();
	            sb.append(cancellation.getId()).append(";");
	            sb.append(cancellation.getOrderId()).append(";");
	            sb.append(cancellation.getComment()).append("\n");
	            

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

	public Collection<RejectedOrder> findAll()
	{
		return cancellations.values();
	}



}
