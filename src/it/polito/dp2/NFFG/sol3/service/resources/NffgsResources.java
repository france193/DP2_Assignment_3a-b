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
 * Created by Francesco Longo (s223428) on 13/01/2017.
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

    /**
     * ALL NFFGS
     *
     * @return
     */
    @GET
    @Path("/nffgs")
    @ApiOperation(value = "get all the nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
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
            throw new NotFoundException();
        }
    }

    /**
     * A NFFG
     *
     * @param nffg_id
     * @return
     */
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
            throw new NotFoundException();
        }
    }

    /**
     * ADD NFFGS
     *
     * @param nffgs
     * @return
     */
    @POST
    @Path("/nffgs")
    @ApiOperation(value = "insert an nffgs", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffgs(FLNffgs nffgs, @Context UriInfo uriInfo) {
        try {
            FLNffgs created = service.postNffgs(nffgs);

            if (created != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path("").build();
                return Response.created(u).entity(created).build();
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * ADD 1 NFFG
     *
     * @param nffg
     * @return
     */
    @POST
    @Path("/nffg")
    @ApiOperation(value = "insert an nffg", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffg(FLNffg nffg, @Context UriInfo uriInfo) {
        try {
            FLNffg created = service.postNffg(nffg);

            if (created != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path(created.getId()).build();
                return Response.created(u).entity(created).build();
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * ADD NFFGS
     *
     * @param nffgs
     * @return
     */
    @DELETE
    @Path("/nffgs")
    @ApiOperation(value = "delete a group of nffgs", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffgs deleteNffgs(FLNffgs nffgs) {
        try {
            FLNffgs deleted = service.removeNffgs(nffgs);

            if (deleted != null) { // success
                return deleted;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * ADD 1 NFFG
     *
     * @param nffg
     * @return
     */
    @DELETE
    @Path("/nffg/{nffg_id}")
    @ApiOperation(value = "delete a nffg", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffg deleteNffg(@PathParam("nffg_id") String nffg_id){
        try {
            FLNffg deleted = service.removeNffg(nffg_id);

            if (deleted != null) { // success
                return deleted;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * NODES OF A NFFG
     *
     * @param nffg_id
     * @return
     */
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
            throw new NotFoundException();
        }
    }

    /**
     * A NODE OF AN NFFG
     *
     * @param nffg_id
     * @param node_id
     * @return
     */
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
            FLNode x = service.getNffgNode(nffg_id, node_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * ALL LINKS OF A NODE OF AN NFFG
     *
     * @param nffg_id
     * @param node_id
     * @return
     */
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
            FLLinks x = service.getNffgNodeLinks(nffg_id, node_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * A LINK OF A NODE OF AN NFFG
     *
     * @param nffg_id
     * @param node_id
     * @param link_id
     * @return
     */
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
            FLLink x = service.getNffgNodeLink(nffg_id, node_id, link_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * ALL LINKS OF A NFFG
     *
     * @param nffg_id
     * @return
     */
    @GET
    @Path("/nffg/{nffg_id}/links")
    @ApiOperation(value = "get all links of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLLinks getNffgLinks(@PathParam("nffg_id") String nffg_id) {
        try {
            FLLinks x = service.getNffgLinks(nffg_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * A LINK OF A NFFG
     *
     * @param nffg_id
     * @param link_id
     * @return
     */
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
            FLLink x = service.getNffgLink(nffg_id, link_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * ALL POLICIES OF A NFFG
     *
     * @param nffg_id
     * @return
     */
    @GET
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getNffgPolicies(@PathParam("nffg_id") String nffg_id) {
        try {
            FLPolicies x = service.getNffgPolicies(nffg_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * A POLICY OF A NFFG
     *
     * @param nffg_id
     * @param policy_id
     * @return
     */
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
            FLPolicy x = service.getNffgPolicy(nffg_id, policy_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * ADD POLICIES
     *
     * @param policies
     * @return
     */
    @POST
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "insert a group of policies into relatively to a nffg", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffgPolicies(@PathParam("nffg_id") String nffg_id,
                                                  FLPolicies policies,
                                                  @Context UriInfo uriInfo) {
        try {
            FLPolicies created = service.postNffgPolicies(nffg_id, policies);

            if (created != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path("").build();
                return Response.created(u).entity(created).build();
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * ADD 1 POLICY
     *
     * @param policy
     * @return
     */
    @POST
    @Path("/nffg/{nffg_id}/policy")
    @ApiOperation(value = "insert a policy into nffg", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffgPolicy(@PathParam("nffg_id") String nffg_id,
                                                FLPolicy policy,
                                                @Context UriInfo uriInfo) {
        try {
            FLPolicy created = service.postNffgPolicy(nffg_id, policy);

            if (created != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path(created.getId()).build();
                return Response.created(u).entity(created).build();
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * ALL POLICIES OF ALL NFFGS
     *
     * @return
     */
    @GET
    @Path("/policies")
    @ApiOperation(value = "get all policies of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicies getPolicies() {
        try {
            FLPolicies x = service.getPolicies();

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * A POLICY OF ALL NFFGS
     *
     * @param policy_id
     * @return
     */
    @GET
    @Path("/policy/{policy_id}")
    @ApiOperation(value = "get a specifc policy of all nffgs ", notes = "xml and json formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FLPolicy getPolicy(@PathParam("policy_id") String policy_id) {
        try {
            FLPolicy x = service.getPolicy(policy_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Remove A policy
     *
     * @param nffg_id
     * @return
     */
    @DELETE
    @Path("/nffg/{nffg_id}/policy/{policy_id}")
    @ApiOperation(value = "remove a negotiate object", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy deletePolicy(@PathParam("nffg_id") String nffg_id,
                                              @PathParam("policy_id") String policy_id) {
        try {
            FLPolicy deleted = service.removePolicy(nffg_id, policy_id);

            if (deleted != null) { // success
                return deleted;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Remove A set of policies
     *
     * @return
     */
    @DELETE
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "remove a negotiate object", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies deletePolicies(@PathParam("nffg_id") String nffg_id,
                                                FLPolicies policies) {
        try {
            FLPolicies deleted = service.removePolicies(nffg_id, policies);

            if (deleted != null) { // success
                return deleted;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Update A policy
     *
     * @param nffg_id
     * @return
     */
    @PUT
    @Path("/nffg/{nffg_id}/policy/{policy_id}")
    @ApiOperation(value = "remove a negotiate object", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy updatePolicy(@PathParam("nffg_id") String nffg_id,
                                              @PathParam("policy_id") String policy_id,
                                              FLPolicy policy) {
        try {
            FLPolicy updated = service.updatePolicy(nffg_id, policy_id, policy);

            if (updated != null) { // success
                return updated;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Remove A set of policies
     *
     * @return
     */
    @PUT
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "remove a negotiate object", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies updatePolicies(@PathParam("nffg_id") String nffg_id,
                                                  FLPolicies policies) {
        try {
            FLPolicies updated = service.updatePolicies(nffg_id, policies);

            if (updated != null) { // success
                return updated;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Verify 1 policy
     *
     * @param policy_id
     * @return
     */
    @GET
    @Path("/verifyPolicy/{policy_id}")
    @ApiOperation(value = "verify a policy", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLVResult verifyPolicy(@PathParam("policy_id") String policy_id) {
        try {
            FLVResult verified = service.verifyPolicy(policy_id);

            if (verified != null) { // success
                return verified;
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * ADD 1 POLICY
     *
     * @param policy
     * @return
     */
    @POST
    @Path("/verifyPolicies")
    @ApiOperation(value = "verify a group of policies", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLVResults verifyPolicies(FLPolicies policies) {
        try {
            FLVResults verified = service.verifyPolicies(policies);

            if (verified != null) { // success
                return verified;
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

}
