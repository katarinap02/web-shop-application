package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chocolate;
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
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart openCart(ShoppingCart cart) {
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.openCart(cart);
	}
	
	@POST
	@Path("/addtocart")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart addToCart(@QueryParam("cartId") String cartId, @QueryParam("chocolateId") String chocolateId, @QueryParam("amount") String amount, @QueryParam("price") String price, @QueryParam("factoryId") String factoryId)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.addToCart(Integer.parseInt(cartId), Integer.parseInt(chocolateId), Integer.parseInt(amount), Double.parseDouble(price), Integer.parseInt(factoryId));
		
	}
	
	@POST
	@Path("/updateAmount")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart updateAmount(@QueryParam("cartId") String cartId, @QueryParam("chocolateId") String chocolateId, @QueryParam("amount") String amount, @QueryParam("price") String price)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.updateAmount(Integer.parseInt(cartId), Integer.parseInt(chocolateId), Integer.parseInt(amount), Double.parseDouble(price));
		
	}
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart findOpenedCart(@PathParam("username") String username)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.findOpenedCart(username);
	}
	
	@GET
	@Path("/getItems/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Chocolate> getItems(@PathParam("id") int cartId)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.getItems(cartId);
	}
	
	@GET
	@Path("/emptycarts")
	@Produces(MediaType.APPLICATION_JSON)
	public void emptyCarts(@QueryParam("username") String username)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		dao.emptyOutCarts(username);
		
	}
	
	@DELETE
	@Path("removeChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart removeChocolate(@QueryParam("cartId") String cartId, @QueryParam("chocolateId") String chocolateId)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.removeChocolatesById(Integer.parseInt(cartId), Integer.parseInt(chocolateId));
	}
	
	@GET
	@Path("/amount")
	@Produces(MediaType.APPLICATION_JSON)
	public int getAmount(@QueryParam("cartId") String cartId, @QueryParam("chocolateId") String chocolateId)
	{
		ShoppingCartDAO dao = (ShoppingCartDAO) ctx.getAttribute("shoppingCartDAO");
		return dao.getAmountOfChocolate(Integer.parseInt(cartId), Integer.parseInt(chocolateId));
	}
	


}
