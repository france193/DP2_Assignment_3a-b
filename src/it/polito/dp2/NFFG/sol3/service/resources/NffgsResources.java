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

    private NffgServices service;

    public NffgsResources() {
        service = new NffgServices(System.getProperty("it.polito.dp2.NFFG.lab3.NEO4JURL"));
    }

    /** GET **/
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffgs getNffgs() {
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffg getNffg(@PathParam("nffg_id") String nffg_id) {
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNodes getNffgNodes(@PathParam("nffg_id") String nffg_id) {
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNode getNffgNode(@PathParam("nffg_id") String nffg_id,
                                           @PathParam("node_id") String node_id) {
        try {
            FLNode x = service.getNffgNode(nffg_id, node_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLLinks getNffgNodeLinks(@PathParam("nffg_id") String nffg_id,
                                                 @PathParam("node_id") String node_id) {
        try {
            FLLinks x = service.getNffgNodeLinks(nffg_id, node_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLLink getNffgNodeLink(@PathParam("nffg_id") String nffg_id,
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
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLLinks getNffgLinks(@PathParam("nffg_id") String nffg_id) {
        try {
            FLLinks x = service.getNffgLinks(nffg_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLLink getNffgLink(@PathParam("nffg_id") String nffg_id,
                                           @PathParam("link_id") String link_id) {
        try {
            FLLink x = service.getNffgLink(nffg_id, link_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies getNffgPolicies(@PathParam("nffg_id") String nffg_id) {
        try {
            FLPolicies x = service.getNffgPolicies(nffg_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy getNffgPolicy(@PathParam("nffg_id") String nffg_id,
                                               @PathParam("policy_id") String policy_id) {
        try {
            FLPolicy x = service.getNffgPolicy(nffg_id, policy_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies getPolicies() {
        try {
            FLPolicies x = service.getPolicies();

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
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
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy getPolicy(@PathParam("policy_id") String policy_id) {
        try {
            FLPolicy x = service.getPolicy(policy_id);

            if (x == null) {
                throw new NotFoundException();
            } else {
                return x;
            }
        } catch (NullPointerException e) {
            throw new ServiceUnavailableException();
        }
    }

    /** POST **/
    /**
     * ADD POLICIES
     *
     * @param policies
     * @return
     */
    @POST
    @Path("policies")
    @ApiOperation(value = "insert a group of policies", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postPolicies(FLPolicies policies,
                                              @Context UriInfo uriInfo) {
        try {
            FLPolicies created = service.postPolicies(policies);

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
    @Path("policy")
    @ApiOperation(value = "insert 1 policy", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postPolicy(FLPolicy policy,
                                            @Context UriInfo uriInfo) {
        try {
            FLPolicy created = service.postPolicy(policy);

            if (created != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path(created.getName()).build();
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
            FLNffgs created = service.postNffgs(nffgs);

            if (created != null) { // success
                if ((created.getFLNffg().size() == 1) && (created.getFLNffg().get(0).getId().contains("-1"))) {
                    throw new BadRequestException("Already loaded nffg");
                } else {
                    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                    URI u = builder.path("").build();
                    return Response.created(u).entity(created).build();
                }
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
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffg(FLNffg nffg, @Context UriInfo uriInfo) {
        try {
            FLNffg created = service.postNffg(nffg);

            if (created.getId().contains("-1")) {
                throw new BadRequestException("Already loaded nffg");
            }

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

    /** DELETE **/
    /**
     * DELETE 1 POLICY
     *
     * @param policy_id
     * @return
     */
    @DELETE
    @Path("policy/{policy_id}")
    @ApiOperation(value = "remove 1 policy", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy deletePolicy(@PathParam("policy_id") String policy_id) {
        try {
            FLPolicy deleted = service.removePolicy(policy_id);

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
     * DELETE A GROUP OF POLICIES
     *
     * @param policies
     * @return
     */
    @DELETE
    @Path("policies")
    @ApiOperation(value = "remove a set of policies", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies deletePolicies(FLPolicies policies) {
        try {
            FLPolicies deleted = service.removePolicies(policies);

            if (deleted != null) { // success
                return deleted;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /*
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

    @DELETE
    @Path("/nffg/{nffg_id}")
    @ApiOperation(value = "delete a nffg", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffg deleteNffg(@PathParam("nffg_id") String nffg_id) {
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
    */

    /** PUT **/
    /**
     * Update A policy
     *
     * @param policy_id
     * @param policy
     * @return
     */
    @PUT
    @Path("policy/{policy_id}")
    @ApiOperation(value = "update 1 policy", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy updatePolicy(@PathParam("policy_id") String policy_id,
                                              FLPolicy policy) {
        try {
            FLPolicy updated = service.updatePolicy(policy_id, policy);

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
    @Path("policies")
    @ApiOperation(value = "update a group of policies", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies updatePolicies(FLPolicies policies) {
        try {
            FLPolicies updated = service.updatePolicies(policies);

            if (updated != null) { // success
                return updated;
            } else {
                throw new NotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /** VERIFY **/
    /**
     * Verify 1 policy
     *
     * @param flvResult
     * @return
     */
    @POST
    @Path("/verifyPolicy")
    @ApiOperation(value = "verify 1 policy", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response verifyPolicy(FLVResult flvResult, @Context UriInfo uriInfo) {
        try {
            FLVResult verified = service.verifyPolicy(flvResult);

            if (verified != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI u = builder.path("").build();
                return Response.ok(u).entity(verified).build();
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Verify a group of policies
     *
     * @param flvResults
     * @return
     */
    @POST
    @Path("/verifyPolicies")
    @ApiOperation(value = "verify a group of policies", notes = "json and xml formats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response verifyPolicies(FLVResults flvResults, @Context UriInfo uriInfo) {
        try {
            FLVResults verified = service.verifyPolicies(flvResults);

            if (verified != null) { // success
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                URI uri = builder.path("").build();
                return Response.ok(uri).entity(verified).build();
            } else {
                throw new ServiceUnavailableException();
            }
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

}
