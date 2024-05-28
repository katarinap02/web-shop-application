package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.ChocolateFactory;
import dao.ChocolateFactoryDAO;

@Path("/factories")
public class ChocolateFactoryService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("factoryDAO") == null)
		{
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("factoryDAO", new ChocolateFactoryDAO(contextPath));
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
	
	@GET
	@Path("/getid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ChocolateFactory getById(@PathParam("id") int id)
	{
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findById(id);
	}
	

}
