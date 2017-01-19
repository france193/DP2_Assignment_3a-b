package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.database.oldIDs;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Francesco Longo (s223428) on 13/01/2017.
 */
public class NffgServices {
    private ConcurrentHashMap<String, FLPolicy> policies = NffgDB.getPolicies();
    private ConcurrentHashMap<String, oldIDs> tempNffgIDs = NffgDB.getTempNffgIDs();

    private WebTarget target;
    private String baseURL;
    private Client client;

    public NffgServices(String neo4jurl) {
        baseURL = neo4jurl+"resource";

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));
    }

    public Integer init() {
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
     * METHODS FOR SERVICES
     **/
    public FLNffgs getNffgs() {
        FLNffgs x = new FLNffgs();

        for (FLNffg f : NffgDB.nffgs.values()) {
            x.getFLNffg().add(f);
        }

        return x;
    }

    public FLNffg getNffg(String nffg_id) {
        return NffgDB.nffgs.get(nffg_id);
    }

    public FLNffgs postNffgs(FLNffgs nffgs_t) {
        FLNffgs x = new FLNffgs();
        FLNffg f;

        //here it is the FLNffgs uploaded
        for (FLNffg n : nffgs_t.getFLNffg()) {
            if ( (f = postNffg(n)) != null ) {
                x.getFLNffg().add(f);
            } else {
                return null;
            }
        }

        return x;
    }

    public FLNffg postNffg(FLNffg n) {
        Response response;
        oldIDs oldIDs = new oldIDs();

        // Create the NFFG as a NODE
        Node node_t = new Node();

        oldIDs.setNffgOldID(n.getId());

        Property p = new Property();
        p.setName("name");
        p.setValue(n.getName());
        node_t.getProperty().add(p);

        // upload to Neo4JXML
        response = target.path("node")
                .request()
                .accept("application/xml")
                .post(Entity.entity(node_t, "application/xml"));

        if (response.getStatus() != 200) {
            return null;
        }

        // take the id of the Neo4JXML
        String id = response.readEntity(Node.class).getId();
        n.setId(id);

        // set the label as requested
        Labels labels = new Labels();
        labels.getValue().add("NFFG");

        response = target.path("node")
                .path(id)
                .path("label")
                .request()
                .accept("application/xml")
                .post(Entity.entity(labels, "application/xml"));

        if (response.getStatus() != 204) {
            return null;
        }

        // NODES
        for (FLNode node : n.getFLNode()) {

            // create the new node to insert on Neo4JXML
            Node n1 = new Node();

            Property p1 = new Property();
            p1.setName("name");
            p1.setValue(node.getName());
            n1.getProperty().add(p1);

            Property p2 = new Property();
            p2.setName("functional_type");
            p2.setValue(node.getFunctionalType().toString());
            n1.getProperty().add(p2);

            // upload node
            response = target.path("node")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(n1, "application/xml"));

            if (response.getStatus() != 200) {
                return null;
            }

            // update the new id with the one given by Neo4JXML
            String n_id = response.readEntity(Node.class).getId();
            oldIDs.tempNodeIDs.put(node.getId(), n_id);
            node.setId(n_id);

            tempNffgIDs.put(id, oldIDs);

            // create the relationship that indicate that the node belongs to the nffg on Neo4JXML
            Relationship r = new Relationship();

            r.setType("belongs");
            r.setSrcNode(id);
            r.setDstNode(n_id);

            response = target.path("node")
                    .path(id)
                    .path("relationship")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(r, "application/xml"));

            if (response.getStatus() != 200) {
                return null;
            }

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
        }

        // LINKS
        for (FLLink link : n.getFLLink()) {
            Relationship r = new Relationship();

            String src_id, dst_id;
            src_id = tempNffgIDs.get(id).tempNodeIDs.get(link.getSourceNode());
            dst_id = tempNffgIDs.get(id).tempNodeIDs.get(link.getDestinationNode());

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

            String id_l = response.readEntity(Relationship.class).getId();
            link.setId(id_l);
            link.setSourceNode(src_id);
            link.setDestinationNode(dst_id);

            Labels l = new Labels();
            // add the id of all relationship for this node
            l.getValue().add(id_l);

            response = target.path("node")
                    .path(link.getSourceNode())
                    .path("label")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(l, "application/xml"));

            if (response.getStatus() != 204) {
                return null;
            }
        }

        // save nffg on my local DB
        NffgDB.nffgs.put(id, n);

        // Save Policies on the server (on proper nffg and on a proper Map of only policies)
        for (FLPolicy policy : n.getFLPolicy()) {
            policy.setId("" + NffgDB.policyCounter);
            policy.setNffg(id);
            policy.setSourceNode(tempNffgIDs.get(id).tempNodeIDs.get(policy.getSourceNode()));
            policy.setDestinationNode(tempNffgIDs.get(id).tempNodeIDs.get(policy.getDestinationNode()));
            policies.put(policy.getId(), policy);
            NffgDB.policyCounter++;
        }

        return NffgDB.nffgs.get(n.getId());
    }

    public FLNffgs removeNffgs(FLNffgs flNffgs) {
        FLNffgs x = new FLNffgs();
        FLNffg y;

        for (FLNffg f : flNffgs.getFLNffg()) {
            if ( ( y = removeNffg(f.getId())) != null ) {
                x.getFLNffg().add(y);
            } else {
                return null;
            }
        }

        return x;
    }

    public FLNffg removeNffg(String nffg_id) {
        Response response;

        // retrieve all data
        FLNffg x = NffgDB.nffgs.get(nffg_id);

        // remove from local DB
        NffgDB.nffgs.remove(nffg_id);

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

    public FLNodes getNffgNodes(String nffg_id) {
        FLNffg n;
        FLNodes nodes = new FLNodes();

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
            nodes.getFLNode().addAll(n.getFLNode());
        } else {
            return null;
        }

        return nodes;
    }

    public FLNode getNffgNode(String nffg_id, String node_id) {
        FLNffg n;

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
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

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {

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

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
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

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
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

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
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
        FLNffg n;
        FLPolicies policies = new FLPolicies();

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
            policies.getFLPolicy().addAll(n.getFLPolicy());
        } else {
            return null;
        }

        return policies;
    }

    public FLPolicy getNffgPolicy(String nffg_id, String policy_id) {
        FLNffg n;

        if ((n = NffgDB.nffgs.get(nffg_id)) != null) {
            for (FLPolicy policy : n.getFLPolicy()) {
                if (policy.getId().equals(policy_id)) {
                    return policy;
                }
            }
        } else {
            return null;
        }

        return null;
    }

    public FLPolicies getPolicies() {
        FLPolicies policies = new FLPolicies();

        for (FLNffg f : NffgDB.nffgs.values()) {
            if (f != null) {
                policies.getFLPolicy().addAll(f.getFLPolicy());
            }
        }

        return policies;
    }

    public FLPolicy getPolicy(String policy_id) {
        for (FLNffg nffg : NffgDB.nffgs.values()) {
            for (FLPolicy policy : nffg.getFLPolicy()) {
                if (policy.getId().equals(policy_id)) {
                    return policy;
                }
            }
        }

        return null;
    }

    public FLPolicy postNffgPolicy(String nffg_id, FLPolicy policy)  {
        policy.setId("" + NffgDB.policyCounter);
        policy.setNffg(nffg_id);

        String src, dst;
        src = tempNffgIDs.get(nffg_id).tempNodeIDs.get(policy.getSourceNode());
        dst = tempNffgIDs.get(nffg_id).tempNodeIDs.get(policy.getDestinationNode());

        policy.setSourceNode(src);
        policy.setDestinationNode(dst);
        NffgDB.getPolicies().put(policy.getId(), policy);
        NffgDB.policyCounter++;

        NffgDB.nffgs.get(nffg_id).getFLPolicy().add(policy);

        return policy;
    }

    public FLPolicies postNffgPolicies(String nffg_id, FLPolicies policies) {
        FLPolicies policies1 = new FLPolicies();
        FLPolicy policy1;

        for (FLPolicy p : policies.getFLPolicy()) {
            if ( (policy1 = postNffgPolicy(nffg_id, p)) != null ) {
                policies1.getFLPolicy().add(policy1);
            } else {
                return null;
            }
        }

        return policies1;
    }

    public FLPolicy removePolicy(String nffg_id, String policy_id) {
        FLNffg f;

        if ( (f = NffgDB.nffgs.get(nffg_id)) != null ) {
            for (FLPolicy policy : f.getFLPolicy()) {
                if (policy.getId().equals(policy_id)) {
                    f.getFLPolicy().remove(policy);
                    policies.remove(policy);
                    return policy;
                }
            }
        }

        return null;
    }

    public FLPolicies removePolicies(String nffg_id, FLPolicies policies) {
        FLPolicies f = new FLPolicies();
        FLPolicy x;

        for (FLPolicy p : policies.getFLPolicy()) {
            if ( (x = removePolicy(nffg_id, p.getId())) != null ) {
                f.getFLPolicy().add(x);
            } else {
                return  null;
            }
        }

        return f;
    }

    public FLPolicy updatePolicy(String nffg_id, String policy_id, FLPolicy flPolicy) {
        FLNffg f;

        if ( (f = NffgDB.nffgs.get(nffg_id)) != null ) {
            for (FLPolicy policy : f.getFLPolicy()) {
                if (policy.getId().equals(policy_id)) {
                    policy.setSourceNode(NffgDB.getTempNffgIDs().get(nffg_id).tempNodeIDs.get(flPolicy.getSourceNode()));
                    policy.setDestinationNode(NffgDB.getTempNffgIDs().get(nffg_id).tempNodeIDs.get(flPolicy.getDestinationNode()));
                    policy.setIsPositive(flPolicy.isIsPositive());
                    policy.setFLVResult(flPolicy.getFLVResult());
                    policy.getFLTraversalRequestedNode().clear();
                    policy.getFLTraversalRequestedNode().addAll(flPolicy.getFLTraversalRequestedNode());
                    return policy;
                }
            }
        }

        return null;
    }

    public FLPolicies updatePolicies(String nffg_id, FLPolicies policies) {
        FLPolicies f = new FLPolicies();
        FLPolicy x;

        for (FLPolicy p : policies.getFLPolicy()) {
            if ( (x = updatePolicy(nffg_id, p.getId(), p)) != null ) {
                f.getFLPolicy().add(x);
            } else {
                return  null;
            }
        }

        return f;
    }

    public FLVResult verifyPolicy(String policy_id) {
        Response response;
        FLVResult flvResult = new FLVResult();
        XMLGregorianCalendar date2;

        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            return null;
        }

        flvResult.setPolicy(policy_id);
        flvResult.setTime(date2);

        String src, dst;
        src = NffgDB.getPolicies().get(policy_id).getSourceNode();
        dst = NffgDB.getPolicies().get(policy_id).getDestinationNode();

        response = target.path("node")
                .path(src)
                .path("paths")
                .queryParam("dst",dst)
                .request()
                .accept("application/xml")
                .get();

        if (response.getStatus() != 200) {
            flvResult.setMessage("errore nella richiesta a Neo4JXML " + response.getStatus());
            return flvResult;
            //return null;
        }

        Paths paths = response.readEntity(Paths.class);

        if (paths.getPath().size() == 0) {
            flvResult.setResult(false);
            flvResult.setMessage("Policy is not verified");
        } else {
            flvResult.setResult(true);
            flvResult.setMessage("Policy is verified");
        }

        return flvResult;
    }

    public FLVResults verifyPolicies(FLPolicies policies) {
        FLVResult y;
        FLVResults flvResults = new FLVResults();

        for (FLPolicy x : policies.getFLPolicy()) {
            if ( ( y = verifyPolicy(x.getId()) ) != null ) {
                flvResults.getFLVResult().add(y);
            } else {
                return null;
            }
        }

        return flvResults;
    }

    /**
     * OTHER METHODS
     **/
    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private Integer initNeo4JDB() {
        Response response = target.path("nodes")
                .request()
                .accept("application/xml")
                .delete();

        return response.getStatus();
    }

    private Integer errorSwitch(Integer status) {
        switch (status) {
            case 400:
                return 1;
            default:
                return 2;
        }
    }
}
