package it.polito.dp2.NFFG.sol3.service.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import it.polito.dp2.NFFG.sol3.service.models.*;
import it.polito.dp2.NFFG.sol3.service.services.NffgServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgsResources {

    NffgServices service = new NffgServices();

    public NffgsResources() {
    }

    // ALL NFFGS
    @GET
    @Path("/nffgs")
    @ApiOperation(value = "get all the nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Nffg> getNffgs() {
       return service.getNffgs();
    }

    // A NFFG
    @GET
    @Path("/nffgs/{nffg_id}")
    @ApiOperation(value = "get a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Nffg getNffg(@PathParam("nffg_id") int nffg_id) {
        return service.getNffg(nffg_id);
    }

    // NODES OF A NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes")
    @ApiOperation(value = "get all nodes of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Nodes getNffgNodes(@PathParam("nffg_id") int nffg_id) {
        return service.getNffgNodes(nffg_id);
    }

    // A NODE OF AN NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes/{node_id}")
    @ApiOperation(value = "get a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Node getNffgNode(@PathParam("nffg_id") int nffg_id,
                        @PathParam("node_id") int node_id) {
        return service.getNffgNode(nffg_id, node_id);
    }

    // ALL LINKS OF A NODE OF AN NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes/{node_id}/links")
    @ApiOperation(value = "get all links of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Links getNffgNodeLinks(@PathParam("nffg_id") int nffg_id,
                         @PathParam("node_id") int node_id) {
        return service.getNffgNodeLinks(nffg_id, node_id);
    }

    // A LINK OF A NODE OF AN NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes/{node_id}/links/{link_id}")
    @ApiOperation(value = "get a specific link of a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Link getNffgNodeLink(@PathParam("nffg_id") int nffg_id,
                        @PathParam("node_id") int node_id,
                        @PathParam("link_id") int link_id) {
        return service.getNffgNodeLink(nffg_id, node_id, link_id);
    }

    // ALL LINKS OF A NFFG
    @GET
    @Path("/nffgs/{nffg_id}/links")
    @ApiOperation(value = "get all links of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Links getNffgLinks(@PathParam("nffg_id") int nffg_id) {
        return service.getNffgLinks(nffg_id);
    }

    // A LINK OF A NFFG
    @GET
    @Path("/nffgs/{nffg_id}/links/{link_id}")
    @ApiOperation(value = "get a specific link of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Link getNffgLink(@PathParam("nffg_id") int nffg_id,
                        @PathParam("link_id") int link_id) {
        return service.getNffgLink(nffg_id, link_id);
    }

    // ALL POLICIES OF A NFFG
    @GET
    @Path("/nffgs/{nffg_id}/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Policies getNffgPolicies(@PathParam("nffg_id") int nffg_id) {
        return service.getNffgPolicies(nffg_id);
    }

    // A POLICY OF A NFFG
    @GET
    @Path("/nffgs/{nffg_id}/policies/{policy_id}")
    @ApiOperation(value = "get a specific policy of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Policy getNffgPolicy(@PathParam("nffg_id") int nffg_id,
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
    public Policies getPolicies() {
        return service.getPolicies();
    }

    // A POLICY OF ALL NFFGS
    @GET
    @Path("policies/{policy_id}")
    @ApiOperation(value = "get a specifc policy of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Policy getNode(@PathParam("policy_id") int policy_id) {
        return service.getPolicy(policy_id);
    }
}
