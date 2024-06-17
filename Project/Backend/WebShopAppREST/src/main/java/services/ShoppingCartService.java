package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.CustomerRole;
import beans.ShoppingCart;
import dao.CustomerRoleDAO;
import dao.ShoppingCartDAO;

@Path("/carts")
public class ShoppingCartService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
    {
        if(ctx.getAttribute("shoppingCartDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("shoppingCartDAO", new ShoppingCartDAO(finalPath));
        }
     }
			
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ShoppingCart> getRoles()
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.findAll();
	}

}
