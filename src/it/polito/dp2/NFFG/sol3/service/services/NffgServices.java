package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgServices {

    private Map<String, FLNffg> nffgs = NffgDB.getNffgs();
    private Map<String, FLPolicy> policies = NffgDB.getPolicyes();
    private HashMap<String, String> nodesID;

    private WebTarget target;
    private String baseURL = "http://localhost:8080/Neo4JXML/rest/resource";
    private Client client;
    private Response response;

    public NffgServices() {
        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));
    }

    // method for service
    public List<FLNffg> getNffgs() {
        return new ArrayList<>(nffgs.values());
    }

    public FLNffg getNffg(String nffg_id) {
        return nffgs.get(nffg_id);
    }

    public FLNodes getNffgNodes(String nffg_id) {
        FLNodes nodes = new FLNodes();
        nodes.getFLNode().addAll(nffgs.get(nffg_id).getFLNode());

        return nodes;
    }

    public FLNode getNffgNode(String nffg_id, String node_id) {
        for (FLNode node : nffgs.get(nffg_id).getFLNode()) {
            if (node.getId().contains(node_id)) {
                return node;
            }
        }

        return null;
    }

    public FLLinks getNffgNodeLinks(String nffg_id, String node_id) {
        FLLinks links = new FLLinks();

        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            if (link.getSourceNode().equals(node_id)) {
                links.getFLLink().add(link);
            }
        }

        return links;
    }

    public FLLink getNffgNodeLink(String nffg_id, String node_id, String link_id) {
        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            if (link.getSourceNode().equals(node_id) && link.getId().equals(link_id)) {
                return link;
            }
        }

        return null;
    }

    public FLLinks getNffgLinks(String nffg_id) {
        FLLinks links = new FLLinks();

        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            links.getFLLink().add(link);
        }

        return links;
    }

    public FLLink getNffgLink(String nffg_id, String link_id) {
        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            if (link.getId().equals(link_id)) {
                return  link;
            }
        }

        return null;
    }

    public FLPolicies getNffgPolicies(String nffg_id) {
        FLPolicies policies = new FLPolicies();

        for (FLPolicy policy : NffgDB.getNffgs().get(nffg_id).getFLPolicy()) {
            policies.getFLPolicy().add(policy);
        }

        return policies;
    }

    public FLPolicy getNffgPolicy(String nffg_id, String policy_id) {
        for (FLPolicy policy : NffgDB.getNffgs().get(nffg_id).getFLPolicy()) {
            if (policy.getId().equals(policy_id)) {
                return policy;
            }
        }

        return null;
    }

    public FLPolicies getPolicies() {
        FLPolicies policies = new FLPolicies();

        for (FLNffg nffg : nffgs.values()) {
            for (FLPolicy policy : nffg.getFLPolicy()) {
                policies.getFLPolicy().add(policy);
            }
        }

        return policies;
    }

    public FLPolicy getPolicy(String policy_id) {
        for (FLNffg nffg : nffgs.values()) {
            for (FLPolicy policy : nffg.getFLPolicy()) {
                if (policy.getId().equals(policy_id)) {
                    return policy;
                }
            }
        }

        return null;
    }

    public FLNffgs addNffgs(FLNffgs nffgs_t) throws ServiceException {
        for (FLNffg nffg : nffgs_t.getFLNffg()) {

            // Save Nffg, Node and Links on Neo4JXML

            // NFFG
            Node n = new Node();

            Property p = new Property();
            p.setName("name");
            p.setValue(nffg.getName());
            n.getProperty().add(p);

            n.getLabels().getValue().add("NFFG");

            // upload nffg-node
            response = target.path("node")
                    .request()
                    .accept("application/xml")
                    .post(Entity.entity(n, "application/xml"));

            if (response.getStatus() != 200) {
                throw new ServiceException();
            }

            String nffgID = response.readEntity(Node.class).getId();
            nffg.setId(nffgID);
            nffgs.put(nffgID, nffg);

            // NODES
            for (FLNode node : nffg.getFLNode()) {
                Node n1 = new Node();

                Property p1 = new Property();
                p1.setName("name");
                p1.setValue(node.getName());
                n1.getProperty().add(p1);

                Property p2 = new Property();
                p2.setName("belongs");
                p2.setValue(nffg.getId());
                n1.getProperty().add(p2);

                Property p3 = new Property();
                p3.setName("functionalType");
                p3.setValue(node.getFunctionalType().toString());
                n1.getProperty().add(p3);

                response = target.path("node")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(n, "application/xml"));

                if (response.getStatus() != 200) {
                    throw new ServiceException();
                }

                node.setId(response.readEntity(Node.class).getId());
                nffgs.get(nffgID).getFLNode().add(node);

                Relationship r = new Relationship();

                r.setType("belongs");
                r.setSrcNode(nffgID);
                r.setDstNode(node.getId());

                response = target.path("node")
                        .path(nffgID)
                        .path("relationship")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(r, "application/xml"));

                if (response.getStatus() != 200) {
                    throw new ServiceException();
                }
            }

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
                    throw new ServiceException();
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
                    throw new ServiceException();
                }
            }

            // Save Policies on the server
            for (FLPolicy policy : nffg.getFLPolicy()) {
                policies.put(policy.getId(), policy);
            }
        }

        return nffgs;
    }

    /**
     * OTHER METHODS
     **/

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }
}
