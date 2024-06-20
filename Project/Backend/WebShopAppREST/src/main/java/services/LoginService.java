package services;


import java.time.LocalDate;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.ChocolateFactory;
import beans.User;
import dao.ChocolateFactoryDAO;
import dao.UserDAO;
import dto.ManagerFactoryDTO;
import dto.UserDTO;
import enums.Gender;
import enums.Role;
import utils.TokenUtils;

@Path("")
public class LoginService {
	
	@Context
	ServletContext ctx;
	
	
	@PostConstruct
	public void init()
    {
        if(ctx.getAttribute("userDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("userDAO", new UserDAO(finalPath));
        }
        if(ctx.getAttribute("factoryDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("factoryDAO", new ChocolateFactoryDAO(finalPath));
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
	@Path("/login1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) {
	    UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
	    User loggedUser = userDao.find(user.getUsername(), user.getPassword());
	    if (loggedUser == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("{\"error\": \"Invalid username and/or password\"}")
	                       .build();
	    }
	    String token = TokenUtils.generateToken(user.getUsername());
	    System.out.println("Hello, world!");
	    return Response.ok().entity("{\"token\": \"" + token + "\"}").build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		if (loggedUser == null) {
			return Response.status(400).entity("Invalid username and/or password").build();
		}
		if(loggedUser.getBloked())
		{
			return Response.status(400).entity("This user is bloked").build();
		}
		request.getSession().setAttribute("user", loggedUser);
		
		return Response.status(Response.Status.OK)
                .entity(loggedUser)
                .build();
	}
	
	
	@POST
	@Path("/logout")
	public void logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@POST
	@Path("/editUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User editUser(User user)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.editUser(user);
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
		User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname(), gender, date, role);
		return dao.addUser(user);
	}
	
	@POST
	@Path("/manager") //kreira novog menagera ako je ulogovan administrator
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registerManager(UserDTO userDTO)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		LocalDate date = dao.convertToDate(userDTO.getDate());
		Gender gender = dao.convertToGender(userDTO.getGender());
		Role role = Role.MANAGER;
		User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname(), gender, date, role);
		return dao.addUser(user);
	}
	
	@POST
	@Path("/editManager") //izmena menadzera i radnika, tj dobavljanje odgovarajuce fabrike
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User editManager(ManagerFactoryDTO dto)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		ChocolateFactoryDAO factoryDao = (ChocolateFactoryDAO) ctx.getAttribute("factoryDAO");
		ChocolateFactory factory = factoryDao.findById(dto.getFactoryId());
		User user = dao.findByUsername(dto.getManagerUsername());
		return dao.editManager(user, factory, dto.getFactoryId());
		
	}
	
	
	@GET
	@Path("/getManagers") //dobavlja sve menadzere koji nemaju fabriku
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getManagers()
	{
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		return userDao.findManagers();
	}
	
	
	@POST //kreira novog radnika ako je ulogovan menadzer
	@Path("/worker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registerWorker(UserDTO userDTO)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		LocalDate date = dao.convertToDate(userDTO.getDate());
		Gender gender = dao.convertToGender(userDTO.getGender());
		Role role = Role.WORKER;
		User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname(), gender, date, role);
		return dao.addUser(user);
	}
	
	@GET
    @Path("/currentUser1")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser(@HeaderParam("Authorization") String token) {
        if (token == null || !TokenUtils.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String username = TokenUtils.getUsernameFromToken(token);
        UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
        User user = userDao.findByUsername(username);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok().entity(user).build();
    }
	
	@GET
	@Path("/currentUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
	}
	
	@GET
	@Path("/getLogedUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User GetLogedUser(@QueryParam("username") String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		return userDao.findByUsername(username);
	}
	
	@GET
	@Path("/blokUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User BlokUsers(@QueryParam("username") String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		return userDao.BlokUser(username);
	}
	
	

}
