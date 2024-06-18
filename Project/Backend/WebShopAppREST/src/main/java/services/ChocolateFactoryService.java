package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Location;
import beans.WorkingHours;
import dao.ChocolateDAO;
import dao.ChocolateFactoryDAO;
import dao.UserDAO;

@Path("/factories")
public class ChocolateFactoryService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("factoryDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("factoryDAO", new ChocolateFactoryDAO(finalPath));
        }
		
		if(ctx.getAttribute("userDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("userDAO", new UserDAO(finalPath));
        }
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ChocolateFactory> getAll()
	{
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findAll();
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ChocolateFactory addFactory(ChocolateFactory factory)
	{
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.addFactory(factory);
		
	}
	
	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ChocolateFactory deleteFactory(@QueryParam("id") int id)
	{
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("factoryDAO");
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		userDao.deleteChocolateId(id);
		return dao.deleteById(id);
		
	}
	
	
	
	@GET
	@Path("/getid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ChocolateFactory getById(@PathParam("id") int id)
	{
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findById(id);
	}
	

}
