package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.Labels;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.Node;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.Property;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.Relationship;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import scala.util.parsing.combinator.testing.Str;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgServices {
    private ConcurrentHashMap<String, FLPolicy> policies = NffgDB.getPolicies();
    private Map<String, String> tempNodeIDs = new HashMap<>();

    private WebTarget target;
    private String baseURL = "http://localhost:8080/Neo4JXML/rest/resource";
    private Client client;
    private Response response;

    public NffgServices() {
        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));

        // delete all nodes
        if (NffgDB.getFirstBoot()) {
            initNeo4JDB();
            // TODO if connection failed
            NffgDB.setFirstBoot(false);
        }
    }

    // method for service
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

    public FLNodes getNffgNodes(String nffg_id) {
        FLNffg n;
        FLNodes nodes = new FLNodes();

        if (NffgDB.nffgs.get(nffg_id) != null) {
            n = NffgDB.nffgs.get(nffg_id);
            nodes.getFLNode().addAll(n.getFLNode());
        }

        return nodes;
    }

    public FLNode getNffgNode(String nffg_id, String node_id) {
        for (FLNode node : NffgDB.nffgs.get(nffg_id).getFLNode()) {
            if (node.getId().contains(node_id)) {
                return node;
            }
        }

        return null;
    }

    public FLLinks getNffgNodeLinks(String nffg_id, String node_id) {
        FLNffg n;
        FLLinks links = new FLLinks();

        if (NffgDB.nffgs.get(nffg_id) != null) {
            n = NffgDB.nffgs.get(nffg_id);

            for (FLLink link : n.getFLLink()) {
                if (link.getSourceNode().equals(node_id)) {
                    links.getFLLink().add(link);
                }
            }
        }

        return links;
    }

    public FLLink getNffgNodeLink(String nffg_id, String node_id, String link_id) {
        for (FLLink link : NffgDB.nffgs.get(nffg_id).getFLLink()) {
            if (link.getSourceNode().equals(node_id) && link.getId().equals(link_id)) {
                return link;
            }
        }

        return null;
    }

    public FLLinks getNffgLinks(String nffg_id) {
        FLNffg n;
        FLLinks links = new FLLinks();

        if (NffgDB.nffgs.get(nffg_id) != null) {
            n = NffgDB.nffgs.get(nffg_id);

            for (FLLink link : n.getFLLink()) {
                links.getFLLink().add(link);
            }
        }

        return links;
    }

    public FLLink getNffgLink(String nffg_id, String link_id) {
        for (FLLink link : NffgDB.nffgs.get(nffg_id).getFLLink()) {
            if (link.getId().equals(link_id)) {
                return link;
            }
        }

        return null;
    }

    public FLPolicies getNffgPolicies(String nffg_id) {
        FLNffg n;
        FLPolicies policies = new FLPolicies();

        if (NffgDB.nffgs.get(nffg_id) != null) {
            n = NffgDB.nffgs.get(nffg_id);
            policies.getFLPolicy().addAll(n.getFLPolicy());
        }

        return policies;
    }

    public FLPolicy getNffgPolicy(String nffg_id, String policy_id) {
        for (FLPolicy policy : NffgDB.nffgs.get(nffg_id).getFLPolicy()) {
            if (policy.getId().equals(policy_id)) {
                return policy;
            }
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

    public boolean addNffgs(FLNffgs nffgs_t) {

        //here it is the FLNffgs uploaded
        for (FLNffg n : nffgs_t.getFLNffg()) {

            // Create the NFFG as a NODE
            Node node_t = new Node();

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
                return false;
                // TODO Service Exception
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
                return false;
                // TODO Service Exception
            }

            // NODES
            for (FLNode node : n.getFLNode()) {

                // create the new node to insert on Neo4JXML
                Node n1 = new Node();

                Property p1 = new Property();
                p1.setName("name");
                p1.setValue(node.getName());
                n1.getProperty().add(p1);

                //Property p2 = new Property();
                //p2.setName("belongs");
                //p2.setValue(n.getId());
                //n1.getProperty().add(p2);

                // upload node
                response = target.path("node")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(n1, "application/xml"));

                if (response.getStatus() != 200) {
                    return false;
                    // TODO Service Exception
                }

                // update the new id with the one given by Neo4JXML
                String n_id = response.readEntity(Node.class).getId();
                tempNodeIDs.put(node.getId(), n_id);
                node.setId(n_id);

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
                    return false;
                    // TODO Service Exception
                }

            }

            // LINKS
            for (FLLink link : n.getFLLink()) {
                Relationship r = new Relationship();

                String src_id, dst_id;
                src_id = tempNodeIDs.get(link.getSourceNode());
                dst_id = tempNodeIDs.get(link.getDestinationNode());

                r.setType("link");
                r.setSrcNode(src_id);
                r.setDstNode(dst_id);

                response = target.path("node")
                        .path(src_id)
                        .path("relationship")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(r, "application/xml"));

                if (response.getStatus() != 200) {
                    return false;
                    // TODO Service Exception
                }

                String id_l = response.readEntity(Relationship.class).getId();
                link.setId(id_l);

                /*
                Labels l = new Labels();
                l.getValue().add(link.getDestinationNode() + " - R: " + id_l);

                response = target.path("node")
                        .path(link.getSourceNode())
                        .path("label")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(l, "application/xml"));

                if (response.getStatus() != 204) {
                    return false;
                    // TODO Service Exception
                }
                */
            }

            // save on my local DB
            NffgDB.nffgs.put(id, n);
        }

        return true;
        /*
        for (FLNffg nffg : nffgs_t.getFLNffg()) {

            // NODES


            // LINK
            for (FLLink link : nffg.getFLLink()) {
                Relationship r = new Relationship();

                r.setType("Link");
                r.setSrcNode(link.getSourceNode());
                r.setDstNode(link.getDestinationNode());

                response = target.path("node")
                        .path(link.getSourceNode())
                        .path("relationship")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(r, "application/xml"));

                if (response.getStatus() != 200) {
                    //throw new ServiceException("ERROR - 4");
                }

                r.setId(response.readEntity(Relationship.class).getId());

                Labels l = new Labels();
                l.getValue().add(link.getDestinationNode());

                response = target.path("node")
                        .path(link.getSourceNode())
                        .path("label")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(l, "application/xml"));

                if (response.getStatus() != 200) {
                    //throw new ServiceException("ERROR - 5");
                }
            }

            // Save Policies on the server
            for (FLPolicy policy : nffg.getFLPolicy()) {
                policies.put(policy.getId(), policy);
            }
        }

        return getFLNffgsFromListOfNffg();
        */
    }

    /**
     * OTHER METHODS
     **/

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private Boolean initNeo4JDB() {
        response = target.path("nodes")
                .request()
                .accept("application/xml")
                .delete();

        if (response.getStatus() != 200) {
            return false;
        }

        return true;
    }
}
