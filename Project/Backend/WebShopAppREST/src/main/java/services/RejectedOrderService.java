package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.RejectedOrder;
import beans.Chocolate;
import dao.RejectedOrderDAO;
import dao.ChocolateDAO;

@Path("/rejections")
public class RejectedOrderService {
	

	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
    {
        if(ctx.getAttribute("rejectedOrderDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("rejectedOrderDAO", new ChocolateDAO(finalPath));
        }
     }
			
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<RejectedOrder> getChocolates()
	{
		RejectedOrderDAO dao = (RejectedOrderDAO) ctx.getAttribute("rejectedOrderDAO");
		return dao.findAll();
	}

}
