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
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> findAll()
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.findAll();
	}
	
	
	@GET
	@Path("/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getCommentsByFactory(@PathParam("factoryId") int factoryId)
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.getByFactoryId(factoryId);
	}
	
	@POST
	@Path("/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment createComment(@PathParam("orderId") String orderId, Comment comment)
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.createComment(orderId, comment);
	}
	
	@GET
	@Path("/approve/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void approveComment(@PathParam("commentId") int commentId)
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		dao.approveComment(commentId);
	}
	
	@GET
	@Path("/reject/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void rejectComment(@PathParam("commentId") int commentId)
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		dao.rejectComment(commentId);
	}

}
