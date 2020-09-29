package stighbvm.uials.no.rubikkannonsesystem_v2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("javaee8")
public class JavaEE8Resource {
    
    @GET
    @Path("ping")
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
}
