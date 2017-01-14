package it.polito.dp2.NFFG.sol3.service.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import it.polito.dp2.NFFG.sol3.service.services.NffgServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
@Path("")
public class NffgsResources {

    NffgServices service = new NffgServices();

    public NffgsResources() {
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String getTime() {
        return new Date().toString();
    }

    // ALL NFFGS
    @GET
    @Path("/nffgs")
    @ApiOperation(value = "get all the nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<FLNffg> getNffgs() {
       return service.getNffgs();
    }

    /*
    // A NFFG
    @GET
    @Path("/nffg/{nffg_id}")
    @ApiOperation(value = "get a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNffg getNffg(@PathParam("nffg_id") int nffg_id) {
        return service.getNffg(nffg_id);
    }

    // NODES OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/nodes")
    @ApiOperation(value = "get all nodes of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNodes getNffgNodes(@PathParam("nffg_id") int nffg_id) {
        return service.getNffgNodes(nffg_id);
    }

    // A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}")
    @ApiOperation(value = "get a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLNode getNffgNode(@PathParam("nffg_id") int nffg_id,
                        @PathParam("node_id") int node_id) {
        return service.getNffgNode(nffg_id, node_id);
    }

    // ALL LINKS OF A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}/links")
    @ApiOperation(value = "get all links of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgNodeLinks(@PathParam("nffg_id") int nffg_id,
                         @PathParam("node_id") int node_id) {
        return service.getNffgNodeLinks(nffg_id, node_id);
    }

    // A LINK OF A NODE OF AN NFFG
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}/link/{link_id}")
    @ApiOperation(value = "get a specific link of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLink getNffgNodeLink(@PathParam("nffg_id") int nffg_id,
                        @PathParam("node_id") int node_id,
                        @PathParam("link_id") int link_id) {
        return service.getNffgNodeLink(nffg_id, node_id, link_id);
    }

    // ALL LINKS OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/links")
    @ApiOperation(value = "get all links of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgLinks(@PathParam("nffg_id") int nffg_id) {
        return service.getNffgLinks(nffg_id);
    }

    // A LINK OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/link/{link_id}")
    @ApiOperation(value = "get a specific link of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLink getNffgLink(@PathParam("nffg_id") int nffg_id,
                        @PathParam("link_id") int link_id) {
        return service.getNffgLink(nffg_id, link_id);
    }

    // ALL POLICIES OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getNffgPolicies(@PathParam("nffg_id") int nffg_id) {
        return service.getNffgPolicies(nffg_id);
    }

    // A POLICY OF A NFFG
    @GET
    @Path("/nffg/{nffg_id}/policy/{policy_id}")
    @ApiOperation(value = "get a specific policy of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicy getNffgPolicy(@PathParam("nffg_id") int nffg_id,
                          @PathParam("policy_id") int policy_id) {
        return service.getNffgPolicy(nffg_id, policy_id);
    }

    // ALL POLICIES OF ALL NFFGS
    @GET
    @Path("/policies")
    @ApiOperation(value = "get all policies of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicy getNode(@PathParam("policy_id") int policy_id) {
        return service.getPolicy(policy_id);
    }
    */
}
