package it.polito.dp2.NFFG.sol3.service.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import it.polito.dp2.NFFG.sol3.service.services.ElementAlreadyLoadedException;
import it.polito.dp2.NFFG.sol3.service.services.Neo4JServiceException;
import it.polito.dp2.NFFG.sol3.service.services.NffgServices;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

/**
 * Created by Francesco Longo (s223428) on 19/02/2017.
 */
@Path("resource")
public class NffgResources {

    // class with all implemented services
    private NffgServices service;

    private Logger logger = Logger.getLogger(NffgResources.class.getName());

    /**
     * Class constructor
     */
    public NffgResources() {
        super();
        logger.log(Level.SEVERE, "STARTED");
        this.service = new NffgServices(System.getProperty("it.polito.dp2.NFFG.lab3.NEO4JURL"));
    }

    /**
     * AVAILABLE RESOURCES DIVIDED PER PATHS
     **/

	/* /nffgs */
    @GET
    @Path("/nffgs")
    @ApiOperation(value = "get all nffgs", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffgs getNffgs() {
        return service.getNffgs();
    }

    @POST
    @Path("/nffgs")
    @ApiOperation(value = "insert 1 or more nffgs", notes = "json and xml formats")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postNffgs(FLNffgs nffgs, @Context UriInfo uriInfo) {
        FLNffgs created = null;

        try {
            // create jaxb context
            JAXBContext jaxbContext = JAXBContext.newInstance(FLNffgs.class);

            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(NffgResources.class.getResource("/xsd/nffgVerifier.xsd"));

            // create Marshaller using JAXB context
            Marshaller m = jaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setSchema(schema);
            m.marshal(nffgs, System.out);

            checkConstraints(nffgs);

            created = service.postNffgs(nffgs);

        } catch (ElementAlreadyLoadedException e) {
            return Response.status(Response.Status.CONFLICT).entity("Already loaded").build();
        } catch (DatatypeConfigurationException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("(!) - DatatypeConfigurationException")
                    .build();
        } catch (Neo4JServiceException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("(!) - " + e.getMessage()).build();
        } catch (JAXBException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("(!) IllegalArgumentException! - " + e.getMessage()).build();
        } catch (SAXException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) SaxException! - " + e.getMessage()).build();
        }

        if (created != null) {
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            URI u = builder.path("").build();
            return Response.created(u).entity(created).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("some error occurred").build();
        }

    }

    /* /nffg/{nffg_id} */
    @GET
    @Path("/nffg/{nffg_id}")
    @ApiOperation(value = "get a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNffg getNffg(@PathParam("nffg_id") String nffg_id) {
        FLNffg nffg = service.getNffg(nffg_id);

        if (nffg == null) {
            throw new NotFoundException();
        }

        return nffg;
    }

    /* /nffg/{nffg_id}/nodes */
    @GET
    @Path("/nffg/{nffg_id}/nodes")
    @ApiOperation(value = "get all nodes of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNodes getNffgNodes(@PathParam("nffg_id") String nffg_id) {
        FLNodes nodes = service.getNffgNodes(nffg_id);

        if (nodes == null) {
            throw new NotFoundException();
        }

        return nodes;
    }

    /* /nffg/{nffg_id}/node/{node_id} */
    @GET
    @Path("/nffg/{nffg_id}/node/{node_id}")
    @ApiOperation(value = "get a specific node of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLNode getNffgNode(@PathParam("nffg_id") String nffg_id, @PathParam("node_id") String node_id) {

        FLNode node = service.getNffgNode(nffg_id, node_id);

        if (node == null) {
            throw new NotFoundException();
        }
        return node;
    }

    /* /nffg/{nffg_id}/policies */
    @GET
    @Path("/nffg/{nffg_id}/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies getNffgPolicies(@PathParam("nffg_id") String nffg_id) {
        FLPolicies policies = service.getNffgPolicies(nffg_id);

        if (policies == null) {
            throw new NotFoundException();
        }
        return policies;
    }

    /* /nffg/{nffg_id}/policy/{policy_id} */
    @GET
    @Path("/nffg/{nffg_id}/policy/{policy_id}")
    @ApiOperation(value = "get a specific policy of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy getNffgPolicy(@PathParam("nffg_id") String nffg_id,
                                               @PathParam("policy_id") String policy_id) {
        FLPolicy policy = service.getNffgPolicy(nffg_id, policy_id);

        if (policy == null) {
            throw new NotFoundException();
        }
        return policy;
    }

    /* /policies */
    @GET
    @Path("/policies")
    @ApiOperation(value = "get all policies of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicies getPolicies() {
        FLPolicies policies = service.getPolicies();

        if (policies == null) {
            throw new NotFoundException();
        }
        return policies;
    }

    @POST
    @Path("/policies")
    @ApiOperation(value = "insert 1 or more policies", notes = "json and xml formats")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response postPolicies(FLPolicies policies, @Context UriInfo uriInfo) {
        // create jaxb context
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(FLPolicies.class);
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(NffgResources.class.getResource("/xsd/nffgVerifier.xsd"));

            // create Marshaller using JAXB context
            Marshaller m = jaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setSchema(schema);
            m.marshal(policies, System.out);

            FLPolicies created = service.postPolicies(policies);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            URI u = builder.path("").build();
            return Response.created(u).entity(created).build();

        } catch (JAXBException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n")
                    .build();
        } catch (SAXException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n")
                    .build();
        }
    }

    @PUT
    @Path("/policies")
    @ApiOperation(value = "update 1 or more policies", notes = "json and xml formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response updatePolicy(FLPolicies policies) {
        // create jaxb context
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(FLPolicies.class);
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(NffgResources.class.getResource("/xsd/nffgVerifier.xsd"));

            // create Marshaller using JAXB context
            Marshaller m = jaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setSchema(schema);
            m.marshal(policies, System.out);

            FLPolicies updated = service.updatePolicies(policies);

            return Response.ok().entity(updated).build();

        } catch (JAXBException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n")
                    .build();
        } catch (SAXException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n")
                    .build();
        }
    }

    /* policy/{policy_id} */
    @GET
    @Path("/policy/{policy_id}")
    @ApiOperation(value = "get a specific policy of a specific nffg ", notes = "xml and json formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized FLPolicy getNffgPolicy(@PathParam("policy_id") String policy_id) {
        FLPolicy policy = service.getPolicy(policy_id);

        if (policy == null) {
            throw new NotFoundException();
        }
        return policy;
    }

    @DELETE
    @Path("/policy/{policy_id}")
    @ApiOperation(value = "remove 1 or more policies", notes = "json and xml formats")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response deletePolicy(@PathParam("policy_id") String policy_id) {
        FLPolicy deleted = service.deletePolicy(policy_id);

        if (deleted == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("(!) Policy not found!").build();
        }
        return Response.status(Response.Status.NO_CONTENT).entity(deleted).build();
    }

    /* /verifyPolicies */
    @POST
    @Path("/verifyPolicies")
    @ApiOperation(value = "verify a group of policies", notes = "json and xml formats")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 503, message = "Service Unavailable")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response verifyPolicies(FLVResults flvResults, @Context UriInfo uriInfo) {
        // create jaxb context
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(FLVResults.class);
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(NffgResources.class.getResource("/xsd/nffgVerifier.xsd"));

            // create Marshaller using JAXB context
            Marshaller m = jaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setSchema(schema);
            m.marshal(flvResults, System.out);

            FLVResults verified = service.verifyPolicies(flvResults);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            URI uri = builder.path("").build();
            return Response.ok(uri).entity(verified).build();
        } catch (JAXBException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n")
                    .build();
        } catch (SAXException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("(!) XML document malformed!\n")
                    .build();
        } catch (DatatypeConfigurationException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("(!) - DatatypeConfigurationException")
                    .build();
        } catch (Neo4JServiceException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("(!) - " + e.getMessage()).build();
        }
    }

    /**
     * OTHER METHODS
     **/
    private void checkConstraints(FLNffgs nffgs) throws JAXBException {
        for (FLPolicy p : nffgs.getFLPolicy()) {
            specificNffgContainsAllNodesWhichAPolicyRefersTo(p.getName(), p.getSourceNode(), p.getDestinationNode(),
                    p.getNffgName(), nffgs);
        }
    }

    private void specificNffgContainsAllNodesWhichAPolicyRefersTo(String pname, String srcNode, String dstNode,
                                                                  String nffgName, FLNffgs nffgs) throws JAXBException {
        FLNffg n = findNffg(nffgName, nffgs);

        if (n != null) {
            if (nodeExists(n, srcNode) == false) {
                throw new JAXBException("Error, in policy: \"" + pname + "\" can't find srcNode: \"" + srcNode
                        + "\" on Nffg: " + n.getName());
            }
            if (nodeExists(n, dstNode) == false) {
                throw new JAXBException("Error, in policy: \"" + pname + "\" can't find dstNode: \"" + dstNode
                        + "\" on Nffg: " + n.getName());
            }
        }
    }

    private boolean nodeExists(FLNffg n, String nodeName) {
        for (FLNode node : n.getFLNode()) {
            if (node.getName().equals(nodeName)) {
                return true;
            }
        }
        return false;
    }

    private FLNffg findNffg(String name, FLNffgs nffgs) {
        for (FLNffg n : nffgs.getFLNffg()) {
            if (n.getName().equals(name)) {
                return n;
            }
        }
        return null;
    }

	/*
     * @DELETE
	 * 
	 * @Path("/nffgs")
	 * 
	 * @ApiOperation(value = "delete a group of nffgs", notes =
	 * "json and xml formats")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = "OK"),
	 * 
	 * @ApiResponse(code = 404, message = "Not Found")})
	 * 
	 * @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	 * 
	 * @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) public
	 * synchronized FLNffgs deleteNffgs(FLNffgs nffgs) { try { FLNffgs deleted =
	 * service.removeNffgs(nffgs);
	 * 
	 * if (deleted != null) { // success return deleted; } else { throw new
	 * NotFoundException(); } } catch (NullPointerException e) { throw new
	 * NotFoundException(); } }
	 * 
	 * @DELETE
	 * 
	 * @Path("/nffg/{nffg_id}")
	 * 
	 * @ApiOperation(value = "delete a nffg", notes = "json and xml formats")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = "OK"),
	 * 
	 * @ApiResponse(code = 404, message = "Not Found")})
	 * 
	 * @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	 * 
	 * @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) public
	 * synchronized FLNffg deleteNffg(@PathParam("nffg_id") String nffg_id) {
	 * try { FLNffg deleted = service.removeNffg(nffg_id);
	 * 
	 * if (deleted != null) { // success return deleted; } else { throw new
	 * NotFoundException(); } } catch (NullPointerException e) { throw new
	 * ServiceUnavailableException(); } }
	 */
}
