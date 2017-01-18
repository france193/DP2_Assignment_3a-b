package it.polito.dp2.NFFG.sol3.service.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import it.polito.dp2.NFFG.sol3.service.services.NffgServices;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
@Path("resource")
public class NffgsResources {

    NffgServices service = new NffgServices();

    public NffgsResources() {
        switch (service.init()) {
            case 1:
            case 2:
                throw new ServiceUnavailableException();
            default:
                break;
        }
    }

    // ALL NFFGS
    @GET
    @Path("/nffgs")
    @ApiOperation(value = "get all the nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNffgs getNffgs() {
        try {
            FLNffgs x = service.getNffgs();

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    // A NFFG
    @GET
    @Path("/nffg/{nffg_id}")
    @ApiOperation(value = "get a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNffg getNffg(@PathParam("nffg_id") String nffg_id) {
        try {
            FLNffg x = service.getNffg(nffg_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    // NODES OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/nodes")
    @ApiOperation(value = "get all nodes of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNodes getNffgNodes(@PathParam("nffg_id") String nffg_id) {
        try {
            FLNodes x = service.getNffgNodes(nffg_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    // A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}")
    @ApiOperation(value = "get a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNode getNffgNode(@PathParam("nffg_id") String nffg_id,
                              @PathParam("node_id") String node_id) {
        try {
            return service.getNffgNode(nffg_id, node_id);
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // ALL LINKS OF A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}/links")
    @ApiOperation(value = "get all links of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgNodeLinks(@PathParam("nffg_id") String nffg_id,
                                    @PathParam("node_id") String node_id) {
        try {
            return service.getNffgNodeLinks(nffg_id, node_id);
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // A LINK OF A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}/link/{link_id}")
    @ApiOperation(value = "get a specific link of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLink getNffgNodeLink(@PathParam("nffg_id") String nffg_id,
                                  @PathParam("node_id") String node_id,
                                  @PathParam("link_id") String link_id) {
        try {
            FLLink link = service.getNffgNodeLink(nffg_id, node_id, link_id);

            if (link == null) {
                throw new NotFoundException();
            } else {
                return link;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // ALL LINKS OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/links")
    @ApiOperation(value = "get all links of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgLinks(@PathParam("nffg_id") String nffg_id) {
        try {
            return service.getNffgLinks(nffg_id);
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // A LINK OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/link/{link_id}")
    @ApiOperation(value = "get a specific link of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLink getNffgLink(@PathParam("nffg_id") String nffg_id,
                              @PathParam("link_id") String link_id) {
        try {
            FLLink link = service.getNffgLink(nffg_id, link_id);

            if (link == null) {
                throw new NotFoundException();
            } else {
                return link;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // ALL POLICIES OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getNffgPolicies(@PathParam("nffg_id") String nffg_id) {
        try {
            return service.getNffgPolicies(nffg_id);
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // A POLICY OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/policy/{policy_id}")
    @ApiOperation(value = "get a specific policy of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicy getNffgPolicy(@PathParam("nffg_id") String nffg_id,
                                  @PathParam("policy_id") String policy_id) {
        try {
            return service.getNffgPolicy(nffg_id, policy_id);
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // ALL POLICIES OF ALL NFFGS
    @GET
    @Path("/policies")
    @ApiOperation(value = "get all policies of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getPolicies() {
        try {
            return service.getPolicies();
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // A POLICY OF ALL NFFGS
    @GET
    @Path("/policy/{policy_id}")
    @ApiOperation(value = "get a specifc policy of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicy getNode(@PathParam("policy_id") String policy_id) {
        try {
            return service.getPolicy(policy_id);
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    // ADD ENTIRE NFFGS
    @POST
    @Path("/nffgs")
    @ApiOperation(value = "insert an nffgs", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffgs(FLNffgs nffgs, @Context UriInfo uriInfo) {
        try {
            switch (service.addNffgs(nffgs)) {
                case 0:
                    return Response.created(URI.create("NffgService/rest/resource/nffgs")).build();

                case 1:
                    throw new BadRequestException();

                default:
                case 2:
                    throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new BadRequestException();
        }
    }
}
