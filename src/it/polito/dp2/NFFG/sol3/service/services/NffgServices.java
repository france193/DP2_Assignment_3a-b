package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.database.TemporaryData;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Francesco Longo (s223428) on 13/01/2017.
 */
public class NffgServices {
    private final Boolean MYTEST = false;
    private final Boolean DEBUG = true;
    private final Boolean VERBOSE = false;

    private static int counter = 0;

    private static String defaultNeo4JXMLURL = "http://localhost:8080/Neo4JXML/rest";

    private ConcurrentHashMap<String, FLNffg> allNffgs = NffgDB.getNffgs();
    private ConcurrentHashMap<String, FLPolicy> policies = NffgDB.getPolicies();
    private ConcurrentHashMap<String, TemporaryData> tempName = NffgDB.getTempName();

    private WebTarget target;
    private String baseURL;
    private Client client;

    public NffgServices(String neo4jurl) {
        if (neo4jurl == null) {
            baseURL = defaultNeo4JXMLURL + "/resource";
        } else {
            baseURL = neo4jurl + "/resource";
        }

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));

        switch (init()) {
            case 1:
            case 2:
                throw new ServiceUnavailableException();
            default:
                break;
        }
    }

    public synchronized Integer init() {
        Integer t;

        // delete all nodes
        if (NffgDB.getFirstBoot()) {
            if ((t = initNeo4JDB()) != 200) {
                return errorSwitch(t);
            }
            NffgDB.setFirstBoot(false);
        }

        return 0;
    }

    /**
     * GET
     **/
    public FLNffgs getNffgs() {
        FLNffgs x = new FLNffgs();

        for (FLNffg f : allNffgs.values()) {
            f.getFLPolicy().clear();
            for (FLPolicy p1 : policies.values()) {
                if (f.getName().equals(p1.getNffgName())) {
                    f.getFLPolicy().add(p1);
                }
            }
            x.getFLNffg().add(f);
        }

        return x;
    }

    public FLNffg getNffg(String nffg_id) {
        FLNffg x = allNffgs.get(nffg_id);
        x.getFLPolicy().clear();

        for (FLPolicy p1 : policies.values()) {
            if (x.getName().equals(p1.getNffgName())) {
                x.getFLPolicy().add(p1);
            }
        }

        return x;
    }

    public FLNodes getNffgNodes(String nffg_id) {
        FLNffg n;
        FLNodes nodes = new FLNodes();

        if ((n = allNffgs.get(nffg_id)) != null) {
            nodes.getFLNode().addAll(n.getFLNode());
        } else {
            return null;
        }

        return nodes;
    }

    public FLNode getNffgNode(String nffg_id, String node_id) {
        FLNffg n;

        if ((n = allNffgs.get(nffg_id)) != null) {
            for (FLNode node : n.getFLNode()) {
                if (node.getId().contains(node_id)) {
                    return node;
                }
            }
        } else {
            return null;
        }

        return null;
    }

    public FLLinks getNffgNodeLinks(String nffg_id, String node_id) {
        FLNffg n;
        FLLinks links = new FLLinks();

        if ((n = allNffgs.get(nffg_id)) != null) {

            for (FLLink link : n.getFLLink()) {
                if (link.getSourceNode().equals(node_id)) {
                    links.getFLLink().add(link);
                }
            }
        } else {
            return null;
        }

        return links;
    }

    public FLLink getNffgNodeLink(String nffg_id, String node_id, String link_id) {
        FLNffg n;

        if ((n = allNffgs.get(nffg_id)) != null) {
            for (FLLink link : n.getFLLink()) {
                if (link.getSourceNode().equals(node_id) && link.getId().equals(link_id)) {
                    return link;
                }
            }
        } else {
            return null;
        }

        return null;
    }

    public FLLinks getNffgLinks(String nffg_id) {
        FLNffg n;
        FLLinks links = new FLLinks();

        if ((n = allNffgs.get(nffg_id)) != null) {
            for (FLLink link : n.getFLLink()) {
                links.getFLLink().add(link);
            }
        } else {
            return null;
        }

        return links;
    }

    public FLLink getNffgLink(String nffg_id, String link_id) {
        FLNffg n;

        if ((n = allNffgs.get(nffg_id)) != null) {
            for (FLLink link : n.getFLLink()) {
                if (link.getId().equals(link_id)) {
                    return link;
                }
            }
        } else {
            return null;
        }

        return null;
    }

    public FLPolicies getNffgPolicies(String nffg_id) {
        FLPolicies policies1 = new FLPolicies();

        for (FLPolicy p : policies.values()) {
            if (p.getNffgName().equals(allNffgs.get(nffg_id).getName())) {
                policies1.getFLPolicy().add(p);
            }
        }

        return policies1;
    }

    public FLPolicy getNffgPolicy(String nffg_id, String policy_id) {
        FLPolicies policies1 = getNffgPolicies(nffg_id);

        for (FLPolicy p : policies1.getFLPolicy()) {
            if (p.getName().equals(policy_id)) {
                return p;
            }
        }

        return null;
    }

    public FLPolicies getPolicies() {
        FLPolicies policies1 = new FLPolicies();

        for (FLPolicy p : policies.values()) {
            policies1.getFLPolicy().add(p);
        }

        return policies1;
    }

    public FLPolicy getPolicy(String policy_id) {
        for (FLPolicy p : policies.values()) {
            if (p.getName().equals(policy_id)) {
                return p;
            }
        }

        return null;
    }

    /**
     * POST
     **/
    public synchronized FLNffgs postNffgs(FLNffgs nffgs_t) {
        FLNffgs x = new FLNffgs();
        FLNffg f;

        // check if at least one nffg is a duplicate
        for (FLNffg n : nffgs_t.getFLNffg()) {
            if (tempName.get(n.getName()) != null) {
                FLNffg x1 = new FLNffg();
                x1.setId("" + -1);
                x.getFLNffg().add(x1);
                return x;
            }
        }

        // here it is the FLNffgs uploaded
        for (FLNffg n : nffgs_t.getFLNffg()) {
            if ((f = postNffg(n)) != null) {
                x.getFLNffg().add(f);
            } else {
                return null;
            }
        }

        return x;
    }

    public synchronized FLNffg postNffg(FLNffg nffgToPost) {
        Response response;
        FLNffg nffgToReturn = new FLNffg();
        String oldNodeID;

        // check (with the ned ID) if a nffg already exists
        if (tempName.get(nffgToPost.getName()) != null) {
            FLNffg x = new FLNffg();
            x.setId("" + -1);
            return x;
        }

        nffgToReturn.setName(nffgToPost.getName());
        nffgToReturn.setLastUpdatedTime(nffgToPost.getLastUpdatedTime());

        TemporaryData temporaryData = new TemporaryData();
        temporaryData.setNffgName(nffgToPost.getName());

        // Create the NFFG as a NODE on Neo4J
        Node nffgNode = new Node();

        Property p = new Property();
        p.setName("name");
        p.setValue(nffgToPost.getName());
        nffgNode.getProperty().add(p);

        // upload to Neo4JXML
        response = target.path("node")
                .request()
                .accept("application/xml")
                .post(Entity.entity(nffgNode, "application/xml"));

        if (response.getStatus() != 200) {
            return null;
        }
        // success

        // take the id of the Nffg on Neo4JXML
        nffgToReturn.setId(response.readEntity(Node.class).getId());

        // set the label as requested for the just loaded nffg
        Labels labels = new Labels();
        labels.getValue().add("NFFG");

        response = target.path("node")
                .path(nffgToReturn.getId())
                .path("label")
                .request()
                .accept("application/xml")
                .post(Entity.entity(labels, "application/xml"));

        if (response.getStatus() != 204) {
            return null;
        }
        // success

        /*
        debug.append("I HAVE NODE#: " + nffgToPost.getFLNode().size() + "\n");
        logFile(debug.toString(), "NODE");
        */

        // NODES
        for (FLNode node : nffgToPost.getFLNode()) {
            oldNodeID = node.getId();

            FLNode nodeToReturn = new FLNode();
            nodeToReturn.setName(node.getName());
            nodeToReturn.setFunctionalType(node.getFunctionalType());

            // create the new node to insert on Neo4JXML
            Node nodeToUpload = new Node();

            Property p1 = new Property();
            p1.setName("name");
            p1.setValue(node.getName());
            nodeToUpload.getProperty().add(p1);

            Property p2 = new Property();
            p2.setName("functional_type");
            p2.setValue(node.getFunctionalType().toString());
            nodeToUpload.getProperty().add(p2);

            // upload node
            response = target.path("node")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(nodeToUpload, "application/xml"));

            if (response.getStatus() != 200) {
                return null;
            }
            // success

            // update the new id with the one given by Neo4JXML for the node just entered
            nodeToReturn.setId(response.readEntity(Node.class).getId());

            // create the relationship that indicate that the node belongs to the nffg on Neo4JXML
            Relationship r = new Relationship();

            r.setType("belongs");
            r.setSrcNode(nffgToReturn.getId());
            r.setDstNode(nodeToReturn.getId());

            response = target.path("node")
                    .path(nodeToReturn.getId())
                    .path("relationship")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(r, "application/xml"));

            if (response.getStatus() != 200) {
                return null;
            }
            // result

            /*
            // (not requested) add a label representing node belongs to nffg
            Labels l = new Labels();
            l.getValue().add(response.readEntity(Relationship.class).getId());

            response = target.path("node")
                    .path(n_id)
                    .path("label")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(l, "application/xml"));

            if (response.getStatus() != 204) {
                return null;
            }
            */

            String oldN, newN;

            oldN = node.getName();
            newN = nodeToReturn.getId();

            /*
            debug2.append("LOOKING FOR NODES " +
                    "\noldNodeName: " + oldN +
                    "\nnewNodeID: " + newN +
                    "\noldNodeID: " + oldNodeID +
                    "\n");

            logFile(debug2.toString(), "NODE_ASSOCIATION");
            */

            if (MYTEST) {
                // use for my client test with Postman
                temporaryData.getOldNodeID_newNodeID().put(oldNodeID, newN);
            } else {
                // use for client given by NFFGInfo
                temporaryData.getOldNodeID_newNodeID().put(oldN, newN);
            }

            nffgToReturn.getFLNode().add(nodeToReturn);
        }

        tempName.put(nffgToReturn.getName(), temporaryData);

        // LINKS
        for (FLLink link : nffgToPost.getFLLink()) {
            FLLink linkToReturn = new FLLink();

            String src_id, dst_id;

            src_id = tempName.get(nffgToReturn.getName()).getOldNodeID_newNodeID().get(link.getSourceNode());
            dst_id = tempName.get(nffgToReturn.getName()).getOldNodeID_newNodeID().get(link.getDestinationNode());

            /*
            debug3.append("LOOKING FOR POLICIES " +
                    "\nlinkSourceNode: " + link.getSourceNode() +
                    "\nlinkDestinationNode: " + link.getDestinationNode() + "\n");

            debug3.append("\nsrc_id: " + src_id +
                    "\ndst_id: " + dst_id +
                    "\nnffgToReturn.getName(): " + nffgToReturn.getName() +
                    "\ntempName.get(nffgToReturn.getName()): " + tempName.get(nffgToReturn.getName()) +
                    "\ntempName.get(nffgToReturn.getName()).getOldNodeID_newNodeID(): " + tempName.get(nffgToReturn.getName()).getOldNodeID_newNodeID() +
                    "\n");

            logFile(debug3.toString(), "POLCIES");
            */

            linkToReturn.setName(link.getName());
            linkToReturn.setSourceNode(src_id);
            linkToReturn.setDestinationNode(dst_id);

            Relationship r = new Relationship();

            r.setType("Link");
            r.setDstNode(dst_id);

            response = target.path("node")
                    .path(src_id)
                    .path("relationship")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(r, "application/xml"));

            if (response.getStatus() != 200) {
                return null;
            }
            // success

            linkToReturn.setId(response.readEntity(Relationship.class).getId());

            Labels l = new Labels();
            // add the id of all relationship for this node
            l.getValue().add(linkToReturn.getId());

            response = target.path("node")
                    .path(src_id)
                    .path("label")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(l, "application/xml"));

            if (response.getStatus() != 204) {
                return null;
            }
            // success

            nffgToReturn.getFLLink().add(linkToReturn);
        }

        // save nffg on my local DB
        allNffgs.put(nffgToReturn.getName(), nffgToReturn);

        for (FLPolicy p1 : nffgToPost.getFLPolicy()) {
            postPolicy(p1);
        }

        return nffgToReturn;
    }

    public synchronized FLPolicy postPolicy(FLPolicy policy) {
        FLPolicy p = new FLPolicy();

        p.setName(policy.getName());
        p.setNffgName(policy.getNffgName());

        String src_id, dst_id;

        src_id = tempName.get(policy.getNffgName()).getOldNodeID_newNodeID().get(policy.getSourceNode());
        dst_id = tempName.get(policy.getNffgName()).getOldNodeID_newNodeID().get(policy.getDestinationNode());

        p.setSourceNode(src_id);
        p.setDestinationNode(dst_id);
        p.setIsPositive(policy.isIsPositive());

        // save VerificationResult
        if (policy.getFLVResult() != null) {
            FLVResult result = new FLVResult();

            result.setPolicyName(policy.getFLVResult().getPolicyName());
            result.setResult(policy.getFLVResult().isResult());
            result.setTime(policy.getFLVResult().getTime());
            result.setMessage(policy.getFLVResult().getMessage());

            p.setFLVResult(result);
        } else {
            p.setFLVResult(null);
        }

        // save traversal node request
        if (policy.getFLTraversalRequestedNode().size() > 0) {
            p.getFLTraversalRequestedNode().addAll(policy.getFLTraversalRequestedNode());
        }

        if (DEBUG) {
            StringBuilder debug = new StringBuilder();

            String toload, saved;

            if (policy.getFLVResult() != null) {
                toload = "true";
            } else {
                toload = "null";
            }

            if (p.getFLVResult() != null) {
                saved = "true";
            } else {
                saved = "null";
            }

            debug.append("***************** --> POSTED " + policy.getName() + " *******************\n");
            debug.append("NAME - TOLOAD: " + policy.getName() + " SAVED: " + p.getName() + "\n" +
                    "NFFGNAME - TOLOAD: " + policy.getNffgName() + " SAVED: " + p.getNffgName() + "\n" +
                    "SOURCENODE - TOLOAD: " + src_id + " SAVED: " + p.getSourceNode() + "\n" +
                    "DESTNODE - TOLOAD: " + dst_id + " SAVED: " + p.getDestinationNode() + "\n" +
                    "ISPOSITIVE - TOLOAD: " + policy.isIsPositive() + " SAVED: " + p.isIsPositive() + "\n" +
                    "FLVRESULT - TOLOAD: " + toload + " SAVED: " + saved + "\n");
            if (policy.getFLVResult() != null) {
                debug.append("FLVRESULT(POLICYNAME) - TOLOAD: " + policy.getFLVResult().getPolicyName() + " SAVED: " + p.getFLVResult().getPolicyName() + "\n" +
                        "FLVRESULT(RESULT) - TOLOAD: " + policy.getFLVResult().isResult() + " SAVED: " + p.getFLVResult().isResult() + "\n" +
                        "FLVRESULT(TIME) - TOLOAD: " + policy.getFLVResult().getTime() + " SAVED: " + p.getFLVResult().getTime() + "\n" +
                        "FLVRESULT(MESSAGE) - TOLOAD: " + policy.getFLVResult().getMessage() + " SAVED: " + p.getFLVResult().getMessage() + "\n");
            }
            debug.append("# REQ. TRAV. NODE - TOLOAD: " + policy.getFLTraversalRequestedNode().size() + " SAVED: " + p.getFLTraversalRequestedNode().size() + "\n");
            debug.append("**************************************************\n");

            logFile(debug.toString(), "POSTED_" + policy.getName() + "@" + policy.getNffgName());
        }

        if (policies.get(policy.getName()) != null) {
            policies.remove(policy.getName());
        }
        policies.put(policy.getName(), p);

        return p;
    }

    public synchronized FLPolicies postPolicies(FLPolicies policies2) {
        FLPolicies policies1 = new FLPolicies();

        for (FLPolicy p : policies2.getFLPolicy()) {
            if (policies.get(p.getName()) != null) {
                policies.remove(p.getName());
            }
            policies.put(p.getName(), p);
            policies1.getFLPolicy().add(postPolicy(p));
        }

        return policies1;
    }

    /**
     * REMOVE
     **/
    public synchronized FLPolicy removePolicy(String policy_id) {
        for (FLPolicy p : policies.values()) {
            if (p.getName().equals(policy_id)) {

                if (DEBUG) {
                    StringBuilder debug = new StringBuilder();
                    debug.append("***************** --> REMOVED " + p.getName() + " *******************\n");
                    debug.append("NAME: " + p.getName() + "\n" +
                            "NFFGNAME: " + p.getNffgName() + "\n" +
                            "SRCNODE: " + p.getSourceNode() + "\n" +
                            "DSTNODE: " + p.getDestinationNode() + "\n");

                    if (p.getFLVResult() != null) {
                        debug.append("RESULT(POLICY): " + p.getFLVResult().getPolicyName() + "\n" +
                                "RESULT(RESULT): " + p.getFLVResult().isResult() + "\n" +
                                "RESULT(MESSAGE): " + p.getFLVResult().getMessage() + "\n" +
                                "RESULT(TIME): " + p.getFLVResult().getTime() + "\n");
                    }

                    if (p.getFLTraversalRequestedNode().size() > 0) {
                        for (FLPolicy.FLTraversalRequestedNode functionalType : p.getFLTraversalRequestedNode()) {
                            debug.append("REQUESTED NODE: " + functionalType.getFunctionalType().value() + "\n");
                        }
                    } else {
                        debug.append("REQUESTED NODE: 0\n");
                    }

                    debug.append("*************************************************\n");
                    logFile(debug.toString(), "REMOVED_" + p.getName() + "@" + p.getNffgName());
                }

                policies.remove(policy_id);
                return p;
            }
        }

        return null;
    }

    public synchronized FLPolicies removePolicies(FLPolicies policies) {
        FLPolicies f = new FLPolicies();
        FLPolicy x;

        for (FLPolicy p : policies.getFLPolicy()) {
            if ((x = removePolicy(p.getName())) != null) {
                f.getFLPolicy().add(x);
            } else {
                return null;
            }
        }

        return f;
    }

    /*
    public synchronized FLNffgs removeNffgs(FLNffgs flNffgs) {
        FLNffgs x = new FLNffgs();
        FLNffg y;

        for (FLNffg f : flNffgs.getFLNffg()) {
            if ((y = removeNffg(f.getId())) != null) {
                x.getFLNffg().add(y);
            } else {
                return null;
            }
        }

        return x;
    }

    public synchronized FLNffg removeNffg(String nffg_id) {
        Response response;

        // retrieve all data
        FLNffg x = allNffgs.get(nffg_id);

        // remove from local DB
        allNffgs.remove(nffg_id);

        for (FLNode n : x.getFLNode()) {

            // get node info
            response = target.path("node")
                    .path(n.getId())
                    .request()
                    .accept("application/xml")
                    .get();

            if (response.getStatus() != 200) {
                return null;
            }

            // get all label and delete all relationship
            Node n1 = response.readEntity(Node.class);
            List<String> labels = n1.getLabels().getValue();

            for (String label : labels) {
                response = target.path("relationship")
                        .path(label)
                        .request()
                        .accept("application/xml")
                        .delete();

                if (response.getStatus() != 204) {
                    return null;
                }
            }

            // remove from Neo4JXML
            response = target.path("node")
                    .path(n.getId())
                    .request()
                    .accept("application/xml")
                    .delete();

            if (response.getStatus() != 204) {
                return null;
            }
        }

        // remove from Neo4JXML
        response = target.path("node")
                .path(nffg_id)
                .request()
                .accept("application/xml")
                .delete();

        if (response.getStatus() != 204) {
            return null;
        }

        return x;
    }
    */

    /**
     * UPDATE
     **/
    public synchronized FLPolicy updatePolicy(String policy_id, FLPolicy flPolicy) {
        for (FLPolicy p : policies.values()) {
            if (p.getName().equals(policy_id)) {

                p.setName(flPolicy.getName());
                p.setSourceNode(tempName.get(flPolicy.getNffgName()).getOldNodeID_newNodeID().get(flPolicy.getSourceNode()));
                p.setDestinationNode(tempName.get(flPolicy.getNffgName()).getOldNodeID_newNodeID().get(flPolicy.getDestinationNode()));
                p.setIsPositive(flPolicy.isIsPositive());

                if (flPolicy.getFLVResult() != null) {
                    FLVResult flvResult = new FLVResult();

                    flvResult.setPolicyName(flPolicy.getFLVResult().getPolicyName());
                    flvResult.setResult(flPolicy.getFLVResult().isResult());
                    flvResult.setTime(flPolicy.getFLVResult().getTime());
                    flvResult.setMessage(flPolicy.getFLVResult().getMessage());

                    p.setFLVResult(flvResult);
                } else {
                    p.setFLVResult(null);
                }

                p.getFLTraversalRequestedNode().clear();
                p.getFLTraversalRequestedNode().addAll(flPolicy.getFLTraversalRequestedNode());

                if (DEBUG) {
                    StringBuilder debug = new StringBuilder();
                    debug.append("***************** --> UPDATED " + p.getName() + " *******************\n");
                    logFile(debug.toString(), "UPDATED_" + p.getName() + "@" + p.getNffgName());
                }

                return p;
            }
        }

        return null;
    }

    public synchronized FLPolicies updatePolicies(FLPolicies policies) {
        FLPolicies f = new FLPolicies();
        FLPolicy x;

        for (FLPolicy p : policies.getFLPolicy()) {
            if ((x = updatePolicy(p.getName(), p)) != null) {
                f.getFLPolicy().add(x);
            } else {
                return null;
            }
        }

        return f;
    }

    /**
     * VERIFY
     **/
    public synchronized FLVResult verifyPolicy(FLVResult flvResult) {
        Response response;
        XMLGregorianCalendar date2;

        String policyName = flvResult.getPolicyName();

        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            return null;
        }

        flvResult.setTime(date2);

        String src, dst;
        src = policies.get(policyName).getSourceNode();
        dst = policies.get(policyName).getDestinationNode();

        response = target.path("node")
                .path(src)
                .path("paths")
                .queryParam("dst", dst)
                .request()
                .accept("application/xml")
                .get();

        if (response.getStatus() != 200) {
            //flvResult.setMessage("errore nella richiesta a Neo4JXML " + response.getStatus());
            //return flvResult;
            return null;
        }

        Paths paths = response.readEntity(Paths.class);

        if (paths.getPath().size() == 0) {
            flvResult.setResult(false);
            flvResult.setMessage("Policy is not verified");
        } else {
            flvResult.setResult(true);
            flvResult.setMessage("Policy is verified");
        }

        // add result to policy
        policies.get(flvResult.getPolicyName()).setFLVResult(flvResult);

        return flvResult;
    }

    public synchronized FLVResults verifyPolicies(FLVResults flvResults) {
        FLVResults y = new FLVResults();

        for (FLVResult x : flvResults.getFLVResult()) {
            y.getFLVResult().add(verifyPolicy(x));
        }

        return y;
    }

    /**
     * OTHER METHODS
     **/
    private synchronized URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private synchronized Integer initNeo4JDB() {
        Response response = target.path("nodes")
                .request()
                .accept("application/xml")
                .delete();

        return response.getStatus();
    }

    private synchronized Integer errorSwitch(Integer status) {
        switch (status) {
            case 400:
                return 1;
            default:
                return 2;
        }
    }

    public void logFile(String toWtrite, String name) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());
            File logFile = new File("/Users/FLDeviOS/Desktop/log/Service/NffgService/" + name + "_" + counter + "_" + timeLog + ".txt");
            counter++;

            // This will output the full path where the file will be written to...
            //System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(toWtrite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
