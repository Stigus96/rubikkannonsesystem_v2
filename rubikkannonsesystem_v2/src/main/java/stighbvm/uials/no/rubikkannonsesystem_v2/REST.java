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
import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import no.ntnu.tollefsen.chat.configuration.DatasourceProducer;

import org.eclipse.microprofile.config.inject.ConfigProperty;


import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author Stigus
 * REST service class to be used by the UI */
@Path("REST")
@Stateless
@Log
public class REST {
    

    
    @Inject
    IdentityStoreHandler identityStoreHandler;
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    PasswordHash hasher;

    @Inject
    JsonWebToken principal;
    
    
/**
* Public method that returns items with photos sold in the shop
*/
    public List<Item> getItems(){
        
    }
    
    
    
/**
* A registered user may purchase an Item. An email will be sent to the
* seller if the purchase is successful
*
* @param itemid unique id for item
* @return result of purchase request
*/
    public Response purchase(Long itemid) {
        
    }
    
 /**
 * A registered user may remove an item and associated photos owned by the
 * calling user. An user with administrator privileges may remove any item
 * and associated photos.
 *
 * @param itemid unique id for item to be deleted
 * @return result of delete request
 */
    public Response delete(Long itemid) {
        
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
    public Response addItem(String title, String description, BigDecimal price, FormDataMultiPart Photos) {
        Item item = em.find(Item.class, itemid);
        if (item != null){
            
        } else {
            item = new Item();
            item.setItemTitle(title);
            item.setItemdDescription(description);
            item.setPrice(price);
           
            
        }
        
        
    }
    
 /**
 * Streams an image to the browser (the actual compressed pixels). The image
 * will be scaled to the appropriate width if the width parameter is provided.
 * This is a public method available to all callers.
 *
 * @param name the filename of the image
 * @param width the required scaled with of the image
 *
 * @return the image in original format or in jpeg if scaled
 */
    public Response getPhoto(String name, int width) {
    }
    

}
    
    
    

