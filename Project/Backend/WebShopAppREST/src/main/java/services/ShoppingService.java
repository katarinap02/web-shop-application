package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	
	@POST
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Shopping createOrder(Shopping order, @PathParam("username") String username)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		return dao.createOrder(order, username);
	}

}
