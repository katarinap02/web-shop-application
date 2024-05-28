package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
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
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Chocolate addChocolate(@PathParam("id") int id, Chocolate chocolate) {
		ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		chocolate.setFactory(id);
		chocolate.setNumber(0);
		chocolate.setStatus(false);
		return chocolateDao.addChocolate(chocolate);
	}
	
	

}
