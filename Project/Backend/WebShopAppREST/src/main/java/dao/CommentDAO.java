package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Chocolate;
import beans.Comment;

public class CommentDAO {
	
	private HashMap<Integer, Comment> comments = new HashMap<>();
	
	private String path = "";
	
	public CommentDAO(String contextPath)
	{
		loadComments(contextPath);
		path = contextPath;
	}

	
	public Collection<Comment> getByFactoryId(int id)
	{
		return comments.values().stream().filter(x -> x.getFactoryId() == id).collect(Collectors.toList());
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
					int buyerId = Integer.parseInt(st.nextToken().trim());
					int factoryId = Integer.parseInt(st.nextToken().trim());
					String commentText = st.nextToken().trim();
					int rate = Integer.parseInt(st.nextToken().trim());;
					
					comments.put(id, new Comment(id, buyerId, factoryId, commentText, rate));
					
				
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
