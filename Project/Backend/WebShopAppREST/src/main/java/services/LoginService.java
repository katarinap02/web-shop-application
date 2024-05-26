package services;

import java.time.LocalDate;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dao.UserDAO;
import dto.UserDTO;
import enums.Gender;
import enums.Role;

@Path("")
public class LoginService {
	
	@Context
	ServletContext ctx;
	
	
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("userDAO") == null)
		{
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getUsers()
	{
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		return userDao.findAll();
	}
	
	@OPTIONS
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsLogin() {
		return true;
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		if (loggedUser == null) {
			return Response.status(400).entity("Invalid username and/or password").build();
		}
		request.getSession().setAttribute("user", loggedUser);
		return Response.status(200).build();
	}
	
	
	@POST
	@Path("/logout")
	public void logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@POST
	@Path("/customer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registerCustomer(UserDTO userDTO)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		LocalDate date = dao.convertToDate(userDTO.getDate());
		Gender gender = dao.convertToGender(userDTO.getGender());
		Role role = Role.CUSTOMER;
		User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getLastname(), gender, date, role);
		return dao.addUser(user);
	}
	
	@GET
	@Path("/currentUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
	}
	
	

}
