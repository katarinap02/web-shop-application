package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Chocolate;
import beans.Comment;
import beans.Shopping;
import enums.ShoppingStatus;

public class CommentDAO {
	
	private HashMap<Integer, Comment> comments = new HashMap<>();
	
	private String path = "";
	private ShoppingDAO orderDAO;
	public CommentDAO(String contextPath)
	{
		loadComments(contextPath);
		path = contextPath;
	}

	public Collection<Comment> findAll()
	{
		return comments.values();
	}
	
	public Collection<Comment> getByFactoryId(int id)
	{
		return comments.values().stream().filter(x -> x.getFactoryId() == id).collect(Collectors.toList());
	}
	
	public Comment createComment(String orderId, Comment comment)
	{
		System.out.println("Test path: " + path);
		orderDAO = new ShoppingDAO(path);
		Shopping order = orderDAO.findById(orderId);
		int factoryId = order.getFactoryId();
		String username = order.getUsername();
		
	
		Integer maxId = -1;
		for(int id : comments.keySet())
		{
			if(id > maxId)
				maxId = id;
		}
		maxId++;
		comment.setId(maxId);
		comment.setBuyerId(username);
		comment.setFactoryId(factoryId);
		comment.setApproved(0);
		comment.setStatusSet(0);
		comment.setOrderId(orderId);
		if(comment != null && !comment.getCommentText().isEmpty() && comment.getRate() > 0 && comment.getRate() <= 5)
		{
			comments.put(comment.getId(), comment);
			saveComments(path);
		    order.setRated(1);
		    orderDAO.saveBuys(path);
			return comment;
		}
		else
			return null;
	}
	
	public void approveComment(int commentId)
	{
        Comment comment = comments.containsKey(commentId) ? comments.get(commentId) : null;
		
		if(comment != null)
		{
			comment.setApproved(1);
			comment.setStatusSet(1);
			saveComments(path);
		}
	}
	
	public void rejectComment(int commentId)
	{
        Comment comment = comments.containsKey(commentId) ? comments.get(commentId) : null;
		
		if(comment != null)
		{
			
			comment.setStatusSet(1);
			saveComments(path);
		}
	}
	
	
	private void saveComments(String contextPath) {
		 BufferedWriter out = null;
		    try {
		        File file = new File(contextPath + "/comments.txt");
		        out = new BufferedWriter(new FileWriter(file));
		        System.out.println("Saving to: " + file.getAbsolutePath());
		        
		        for (Comment comment : comments.values()) {
		            StringBuilder sb = new StringBuilder();
		            sb.append(comment.getId()).append(";");
		            sb.append(comment.getBuyerId()).append(";");
		            sb.append(comment.getFactoryId()).append(";");
		            sb.append(comment.getCommentText()).append(";");
		            sb.append(comment.getRate()).append(";");
		            sb.append(comment.getApproved()).append(";");
		            sb.append(comment.getStatusSet()).append(";");
		            sb.append(comment.getOrderId()).append(";");
		          

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


	private void loadComments(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/comments.txt");
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
					String buyerId = st.nextToken().trim();
					int factoryId = Integer.parseInt(st.nextToken().trim());
					String commentText = st.nextToken().trim();
					int rate = Integer.parseInt(st.nextToken().trim());
					int isApproved = Integer.parseInt(st.nextToken().trim());
					int isStatusSet = Integer.parseInt(st.nextToken().trim());
					String orderId = st.nextToken().trim();
					comments.put(id, new Comment(id, buyerId, factoryId, commentText, rate, isApproved, isStatusSet, orderId));
					
				
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
