package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import beans.Chocolate;
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
	@Path("/managerapproved/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Shopping> geApprovedByManager(@PathParam("id") int factoryId)
	{
		ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		return dao.findApprovedByManager(factoryId);
		
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
	
	@GET
	@Path("/searchmanager")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Shopping> searchShoppingsManager(@QueryParam("name") String factoryName, @QueryParam("startprice") String startPrice, @QueryParam("endprice") String endPrice, @QueryParam("startdate") String startDate, @QueryParam("enddate") String endDate, @QueryParam("managername") String managername)
	{
		String factoryNameFilter = (factoryName != null && !factoryName.trim().isEmpty()) ? factoryName : null;
	    String startPriceFilter = (startPrice != null && !startPrice.trim().isEmpty()) ? startPrice : null;
	    String endPriceFilter = (endPrice != null && !endPrice.trim().isEmpty()) ? endPrice : null;
	    String startDateFilter = (startDate != null && !startDate.trim().isEmpty()) ? startDate : null;
	    String endDateFilter = (endDate != null && !endDate.trim().isEmpty()) ? endDate : null;
	    
	    // Log the parameters
	    System.out.println("FactoryName: " + factoryNameFilter + ", StartPrice: " + startPriceFilter + ", StartDate: " + startDateFilter);
	    
	    // Access the DAO and perform the search with the filtered parameters
	    ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
	    return dao.searchShoppingsManager(factoryNameFilter, startPriceFilter, endPriceFilter, startDateFilter, endDateFilter, managername);
	}
	
	@GET
	@Path("/searchcustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Shopping> searchShoppingsCustomer(@QueryParam("name") String factoryName, @QueryParam("startprice") String startPrice, @QueryParam("endprice") String endPrice, @QueryParam("startdate") String startDate, @QueryParam("enddate") String endDate, @QueryParam("customername") String customername)
	{
		String factoryNameFilter = (factoryName != null && !factoryName.trim().isEmpty()) ? factoryName : null;
	    String startPriceFilter = (startPrice != null && !startPrice.trim().isEmpty()) ? startPrice : null;
	    String endPriceFilter = (endPrice != null && !endPrice.trim().isEmpty()) ? endPrice : null;
	    String startDateFilter = (startDate != null && !startDate.trim().isEmpty()) ? startDate : null;
	    String endDateFilter = (endDate != null && !endDate.trim().isEmpty()) ? endDate : null;
	    
	    // Log the parameters
	    System.out.println("FactoryName: " + factoryNameFilter + ", StartPrice: " + startPriceFilter + ", StartDate: " + startDateFilter);
	    
	    // Access the DAO and perform the search with the filtered parameters
	    ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
	    return dao.searchShoppingsCustomer(factoryNameFilter, startPriceFilter, endPriceFilter, startDateFilter, endDateFilter, customername);
	}
	
	@GET
	@Path("/getitems/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Chocolate> getItems(@PathParam("orderId") String orderId)
	{
		  ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		  return dao.getItemsByOrder(orderId);
	}
	
	@GET
	@Path("/getids/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getIds(@PathParam("orderId") String orderId)
	{
		  ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		  return dao.getChocolateIdsByOrder(orderId);
	}
	
	@GET
	@Path("/getprice/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public double getPrice(@PathParam("orderId") String orderId)
	{
		  ShoppingDAO dao = (ShoppingDAO) ctx.getAttribute("shoppingDAO");
		  return dao.getPriceByOrder(orderId);
	}
	
	
}
