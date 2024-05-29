package services;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Chocolate;
import beans.User;
import dao.ChocolateDAO;
import dao.UserDAO;

@Path("/chocolates")
public class ChocolateService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("chocolateDAO") == null)
		{
			String eclipseLaunchPath = System.getProperty("user.dir");
	        
	        System.out.println("Path where Eclipse was launched from: " + eclipseLaunchPath);
	        String contextPath = ctx.getRealPath("");
			ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
		}
	    }
			
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Chocolate> getChocolates()
	{
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.findAll();
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Chocolate addChocolate(Chocolate chocolate)
	{
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.addChocolate(chocolate);
		
	}
	@POST
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Chocolate updateChocolate(@PathParam("id") String id, Chocolate chocolate)
	{
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.updateChocolate(id, chocolate);
		
	}
	
	@GET
	@Path("/getId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Chocolate getById(@PathParam("id") int id)
	{
		
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.findById(id);
		
		
	}
	
	@GET
	@Path("/getfactoryId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Chocolate> getByFactoryId(@PathParam("id") int id)
	{
		
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.findByFactoryId(id);
		
		
	}
	
	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Chocolate deleteChocolate(@QueryParam("id") int id)
	{
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.deleteChocolate(id);
	}
	
	@OPTIONS
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsNewProduct()
	{
		return true;
	}
	

	
	

}
