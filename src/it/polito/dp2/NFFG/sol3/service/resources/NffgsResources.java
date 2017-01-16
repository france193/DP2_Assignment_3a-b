package it.polito.dp2.NFFG.sol3.service.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import it.polito.dp2.NFFG.sol3.service.services.NffgServices;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
@Path("resource")
public class NffgsResources {

    NffgServices service = new NffgServices();

    public NffgsResources() {
    }

    // ALL NFFGS
    @GET
    @Path("/nffgs")
    @ApiOperation(value = "get all the nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<FLNffg> getNffgs() {
        return service.getNffgs();
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
        FLNffg nffg = service.getNffg(nffg_id);

        if (nffg == null) {
            throw new NotFoundException();
        } else {
            return nffg;
        }
    }

    // NODES OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/nodes")
    @ApiOperation(value = "get all nodes of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNodes getNffgNodes(@PathParam("nffg_id") String nffg_id) {
        return service.getNffgNodes(nffg_id);
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
        FLNode node = service.getNffgNode(nffg_id, node_id);

        if (node == null) {
            throw new NotFoundException();
        } else {
            return node;
        }
    }

    // ALL LINKS OF A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}/links")
    @ApiOperation(value = "get all links of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgNodeLinks(@PathParam("nffg_id") String nffg_id,
                                    @PathParam("node_id") String node_id) {
        return service.getNffgNodeLinks(nffg_id, node_id);
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
        FLLink link = service.getNffgNodeLink(nffg_id, node_id, link_id);

        if (link == null) {
            throw new NotFoundException();
        } else {
            return link;
        }
    }

    // ALL LINKS OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/links")
    @ApiOperation(value = "get all links of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgLinks(@PathParam("nffg_id") String nffg_id) {
        return service.getNffgLinks(nffg_id);
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
        FLLink link = service.getNffgLink(nffg_id, link_id);

        if (link == null) {
            throw new NotFoundException();
        } else {
            return link;
        }
    }

    // ALL POLICIES OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getNffgPolicies(@PathParam("nffg_id") String nffg_id) {
        return service.getNffgPolicies(nffg_id);
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
        return service.getNffgPolicy(nffg_id, policy_id);
    }

    // ALL POLICIES OF ALL NFFGS
    @GET
    @Path("/policies")
    @ApiOperation(value = "get all policies of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getPolicies() {
        return service.getPolicies();
    }

    // A POLICY OF ALL NFFGS
    @GET
    @Path("policy/{policy_id}")
    @ApiOperation(value = "get a specifc policy of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicy getNode(@PathParam("policy_id") String policy_id) {
        return service.getPolicy(policy_id);
    }

    // ADD ENTIRE NFFGS
    @POST
    @Path("nffgs")
    @ApiOperation(value = "insert an entire nffgs", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postNffgs(FLNffgs nffgs, @Context UriInfo uriInfo) {

        try {
            FLNffgs created = service.addNffgs(nffgs);

            if (created != null) {
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path("0").build();
                return Response.created(u).entity(created).build();
            } else {
                throw new NotAcceptableException();
            }
        } catch (ServiceException e) {
            throw new ServiceUnavailableException();
        }
    }
}
