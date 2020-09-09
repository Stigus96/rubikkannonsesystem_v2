/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stighbvm.uials.no.rubikkannonsesystem_v2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.identitystore.PasswordHash;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import lombok.extern.java.Log;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
//import no.ntnu.tollefsen.chat.configuration.DatasourceProducer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Stigus REST service class to be used by the UI
 */
@Path("REST")
@Stateless
@Log
public class REST {

    @Inject
    IdentityStoreHandler identityStoreHandler;
    
    @Inject
    MailService mailService;
    
    @Context
    SecurityContext sc;

    @PersistenceContext
    EntityManager em;

    @Inject
    PasswordHash hasher;

    @Inject
    JsonWebToken principal;

    /**
     * Public method that returns items with photos sold in the shop
     */
    @GET
    @Path("items")
    public List<Item> getItems() {
        return em.createNamedQuery(Item.FIND_ALL_ITEMS, Item.class).getResultList();
    }
    
    private Item getItem(Long itemid){
        return em.createNamedQuery(Item.FIND_BY_ITEMID, Item.class)
                .setParameter("itemid", itemid)
                .setParameter("userid", sc.getUserPrincipal().getName())
                .getSingleResult();
    }

    
    private Listing getListing(Long listingid) {
        return em.createNamedQuery(Listing.FIND_BY_LISTINGID, Listing.class)
                .setParameter("listingid", listingid)
                .setParameter("userid", sc.getUserPrincipal().getName())
                .getSingleResult();
    }
    /**
     * A registered user may purchase an Item. An email will be sent to the
     * seller if the purchase is successful
     *
     * @param itemid unique id for item
     * @return result of purchase request
     */
    @RolesAllowed(value = {Group.USER})
    public Response purchase(Long listingid) {
        User user = em.find(User.class,sc.getUserPrincipal().getName());
        Listing listing = em.find(Listing.class, getListing(listingid));

        
        if ((listing.buyerid) == (user.userid)) {
            log.log(Level.INFO, "you can not buy your own item");
            return Response.status(Response.Status.BAD_REQUEST).build();
        } 
        if (listing.buyerid != null){
            log.log(Level.INFO, "item has already been bought");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            listing.buyerid = user.userid;
            
            Item item = em.find(Item.class, listing.Litemid);
            User userseller = em.find(User.class, listing.sellerid);
            String email = userseller.email;
            String subject = "Your item " + item.title + " has been sold";
            String body = "The item " + item.title + 
                    " you put up for sale has been bought by " + user.userid;
            mailService.sendEmail(email, subject, body);
                    
            //sendEmail(listing.sellerid, subject, body);
            return Response.ok(em.merge(listing)).build();
        }
        
    } 
   
      //  public void sendEmail(String sellerid, String subject, String body) {
      //      //User user = getUserBySellerid(sellerid);
      //     User user = em.find(User.class, sellerid);
      //     String email = user.email;
       //    mailService.sendEmail(email, subject, body);          
  //  }
        
    //    public User getUserBySellerid(String sellerid){
    //        
    //        return em.find(User.class, sellerid);
    //    }

    /**
     * A registered user may remove an item and associated photos owned by the
     * calling user. An user with administrator privileges may remove any item
     * and associated photos.
     *
     * @param itemid unique id for item to be deleted
     * @return result of delete request
     */
    @PUT
    @Path("delete")
    @RolesAllowed(value = {Group.USER})
    public Response delete(Long itemid) {
        if (itemid == null) {
            log.log(Level.SEVERE, "Failed to delete item {0}", itemid);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Item item = em.find(Item.class, itemid);
            em.remove(item);
            return Response.ok().build();
        }
    }

    /**
     * A registered user may add an item and photo(s) to Fant.
     *
     * @param title the title of Item
     * @param description the description of Item
     * @param price the price of Item
     * @param photos one or more photos associated with Item
     * @return result of the request. If successful, the request will include
     * the new unique ids of the Item and associated Photos
     */
    @POST
    @Path("addItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Group.USER})
    public Response addItem(@FormDataParam("itemid") Long itemid,
            @FormDataParam("title") String title,
            @FormDataParam("description") String description,
            @FormDataParam("price") BigDecimal price) {
        Item item = em.find(Item.class, itemid);
        if (item != null) {
            log.log(Level.INFO, "item already exists {0}", itemid);
            return Response.status(Response.Status.BAD_REQUEST).build();

        } else {
            item = new Item();
            item.setItemid(itemid);
            item.setTitle(title);
            item.setDescription(description);
            item.setPrice(price);
            return Response.ok(em.merge(item)).build();
        }
    }

    @POST
    @Path("addListing")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Group.USER})
    private Response makeListing(@FormDataParam("listingid") Long listingid,
            @FormDataParam("Litemid") Long Litemid,
            @FormDataParam("sellerid") String sellerid,
            //@FormDataParam("buyerid") String buyerid,
            FormDataMultiPart photos) {
        Listing listing = em.find(Listing.class, listingid);
        if (listing != null) {
            log.log(Level.INFO, "listing already exists {0}", listingid);
            return Response.status(Response.Status.BAD_REQUEST).build();

        } else {
            listing = new Listing();
            listing.setListingid(listingid);
            listing.setLitemid(Litemid);
            //listing.setBuyerid(buyerid);
            listing.setSellerid(sellerid);
            return Response.ok(em.merge(listing)).build();
        }

    }
    
    /**
     * Streams an image to the browser (the actual compressed pixels). The image
     * will be scaled to the appropriate width if the width parameter is
     * provided. This is a public method available to all callers.
     *
     * @param name the filename of the image
     * @param width the required scaled with of the image
     *
     * @return the image in original format or in jpeg if scaled
     */
 /**   @GET

    @Path("image/{name}")
    @Produces("image/jpeg")
    public Response getPhoto(@PathParam("name") String name,
            @QueryParam("width") int width) {
        if(em.find(Photo.class, name) !=null) {
            
        }
    }
    
*/

}
    
    
    

