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
import stighbvm.uials.no.rubikkannonsesystem_v2.DatasourceProducer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author Stigus REST service class used for authentication
 */
@Path("auth")
@Stateless
@Log
public class AuthentictionService {

    @Inject
    KeyService keyService;

    @Inject
    IdentityStoreHandler identityStoreHandler;
    
    /** 
     * The application server will inject a DataSource as a way to communicate 
     * with the database.
     */
    @Resource(lookup = DatasourceProducer.JNDI_NAME)
    DataSource dataSource;

    @PersistenceContext
    EntityManager em;

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "issuer")
    String issuer;

    @Inject
    PasswordHash hasher;

    @Inject
    JsonWebToken principal;

    @GET
    @Path("login")
    public Response login(
            @QueryParam("userid") @NotBlank String userid,
            @QueryParam("password") @NotBlank String password,
            @Context HttpServletRequest request) {
        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(userid, password));
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            String token = issueToken(result.getCallerPrincipal().getName(),
                    result.getCallerGroups(), request);
            return Response
                    .ok(token)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    } // Login user

    /**
     *
     * @param name
     * @param groups
     * @param request
     * @return
     */
    private String issueToken(String name, Set<String> groups, HttpServletRequest request) {
        try {
            Date now = new Date();
            Date expiration = Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant());
            JwtBuilder jb = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("kid", "abc-1234567890")
                    .setSubject(name)
                    .setId("a-123")
                    //.setIssuer(issuer)
                    .claim("iss", issuer)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .claim("upn", name)
                    .claim("groups", groups)
                    .claim("aud", "aud")
                    .claim("auth_time", now)
                    .signWith(keyService.getPrivate());
            return jb.compact();
        } catch (InvalidKeyException t) {
            log.log(Level.SEVERE, "Failed to create token", t);
            throw new RuntimeException("Failed to create token", t);
        }
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@FormParam("userid") String userid,
            @FormParam("password") String password,
            @FormParam("email") String email) {
        User user = em.find(User.class, userid);
        if (user != null) {
            log.log(Level.INFO, "user already exists {0}", userid);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            user = new User();
            user.setUserid(userid);
            user.setPassword(hasher.generate(password.toCharArray()));
            user.setEmail(email);
            Group usergroup = em.find(Group.class, Group.USER);
            user.getGroups().add(usergroup);
            return Response.ok(em.merge(user)).build();
        }
    } // Create new user

    @GET
    @Path("currentuser")
    @RolesAllowed(value = {Group.USER})
    @Produces(MediaType.APPLICATION_JSON)
    public User getCurrentUser() {
        return em.find(User.class, principal.getName());
    } // Get information about current user

    /**
     * Change password of current user or any user if current user has the role
     * of administrator
     */
    @PUT
    @Path("changepassword")
    @RolesAllowed(value = {Group.USER})
    public Response changePassword(@QueryParam("userid") String userid,
            @QueryParam("password") String password,
            @Context SecurityContext sc) {
        String authuser = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        if (authuser == null || userid == null || password == null) {
            log.log(Level.SEVERE, "Failed to change password on user {0}", userid);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            User user = em.find(User.class, userid);
            user.setPassword(hasher.generate(password.toCharArray()));
            em.merge(user);
            return Response.ok().build();

        }
    }

}
