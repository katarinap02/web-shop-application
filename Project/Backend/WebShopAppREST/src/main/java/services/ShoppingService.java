package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Shopping;
import beans.ShoppingCart;
import dao.ShoppingCartDAO;
import dao.ShoppingDAO;

@Path("/buys")
public class ShoppingService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
    {
        if(ctx.getAttribute("shoppingDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("shoppingDAO", new ShoppingDAO(finalPath));
        }
     }
			
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Shopping> getRoles()
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		return dao.findAll();
	}
	
	@GET
	@Path("/customer/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Shopping> getByCustomer(@PathParam("username") String username)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		return dao.findByCustomer(username);
		
	}
	
	@GET
	@Path("/manager/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Shopping> getByManager(@PathParam("id") int factoryId)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		return dao.findByManager(factoryId);
		
	}
	
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Shopping createOrder(@PathParam("username") String username)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		return dao.createOrder(username);
	}
	
	@GET
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void cancelOrder(@PathParam("id") String orderId)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		dao.cancelOrder(orderId);
		
	}
	
	@GET
	@Path("/approve/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void approveOrder(@PathParam("id") String orderId)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		dao.approveOrder(orderId);
		
	}
	
	@GET
	@Path("/reject")
	@Produces(MediaType.APPLICATION_JSON)
	public void rejectOrder(@QueryParam("id") String orderId, @QueryParam("comment") String comment)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		dao.rejectOrder(orderId, comment);
		
	}
	
	

}
