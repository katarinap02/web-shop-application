package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	@Consumes(MediaType.APPLICATION_JSON)
	public Chocolate addChocolate(Chocolate chocolate) {
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return chocolateDao.addChocolate(chocolate);
	}
	
	

}
