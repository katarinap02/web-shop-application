package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.User;
import enums.Gender;
import enums.Role;

public class UserDAO {
	
	private HashMap<String, User> users = new HashMap<>();
	String path = "";
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public UserDAO()
	{}
	
	public UserDAO(String contextPath)
	{
		loadUsers(contextPath);
		path = contextPath;
	}
	
	public Collection<User> findAll()
	{
		return users.values();
	}
	
	public User findByUsername(String username)
	{
		for(User u: users.values())
		{
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}
	
	public User find(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}
	
	public User addUser(User user)
	{
		for(User u : users.values())
		{
			if(u.getUsername().equals(user.getUsername()))
				return null;
		}
		
		if(user.getUsername().isEmpty() || user.getPassword().isEmpty())
		{
			return null;
		}
		
		users.put(user.getUsername(), user);
		saveUsers(path); //nisam uspela da sacuvam u fajl
		return user;
	}
	
	public LocalDate convertToDate(String date)
	{

		LocalDate local = LocalDate.parse(date, formatter);
		return local;

	}
	
	public Gender convertToGender(String input)
	{
		if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            return Gender.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender: " + input);
        }
	}
	
	public Role convertToRole(String input)
	{
		if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            return Role.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender: " + input);
        }
	}
	
	private void loadUsers(String contextPath) {
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
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = convertToGender(st.nextToken().trim());
					LocalDate birthdate = LocalDate.parse(st.nextToken().trim(), formatter);
					Role role = convertToRole(st.nextToken().trim());
					
				users.put(username, new User(username, password, firstName, lastName, gender, birthdate, role));
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
	
	private void saveUsers(String contextPath) {
        BufferedWriter out = null;
        try {
            File file = new File(contextPath + "/users.txt");
            out = new BufferedWriter(new FileWriter(file));

            for (User user : users.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getUsername()).append(";");
                sb.append(user.getPassword()).append(";");
                sb.append(user.getName()).append(";");
                sb.append(user.getName()).append(";");
                sb.append(user.getGender().name()).append(";");
                sb.append(user.getBirthDate().format(formatter)).append(";");
                sb.append(user.getRole().name()).append("\n");

                out.write(sb.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
