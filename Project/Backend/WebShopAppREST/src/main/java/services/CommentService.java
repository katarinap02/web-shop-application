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

import beans.Chocolate;
import beans.Comment;
import dao.ChocolateDAO;
import dao.CommentDAO;

@Path("/comments")
public class CommentService {
	
	@Context
	ServletContext ctx;

	
	@PostConstruct
	public void init()
    {
        if(ctx.getAttribute("commentDAO") == null)
        {
            String eclipseLaunchPath = System.getProperty("user.dir");
            String finalPath = eclipseLaunchPath + "\\web\\WebShop\\Project\\Backend\\WebShopAppREST\\src\\main\\webapp\\";
            System.out.println("Combined path: " + finalPath);
            String contextPath = ctx.getRealPath("");
            System.out.println("Combined path: " + contextPath);
            ctx.setAttribute("commentDAO", new CommentDAO(finalPath));
        }
     }
	
	@GET
	@Path("/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getCommentsByFactory(@PathParam("factoryId") int factoryId)
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.getByFactoryId(factoryId);
	}

}
