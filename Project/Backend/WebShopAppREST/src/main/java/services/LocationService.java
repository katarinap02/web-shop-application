package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chocolate;
import beans.Location;
import dao.ChocolateDAO;
import dao.LocationDAO;

@Path("/locations")
public class LocationService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("locationDAO") == null)
		{
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("locationDAO", new LocationDAO());
		}
	}
	
	@GET
	@Path("/id")
	@Produces(MediaType.APPLICATION_JSON)
	public Location getLocationById(@QueryParam("location") int id)
	{
		LocationDAO dao = (LocationDAO) ctx.getAttribute("locationDAO");
		return dao.findById(id);
	}

}
