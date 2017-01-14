package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import scala.util.parsing.combinator.testing.Str;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

    private Map<Long, FLNffg> nffgs = NffgDB.getNffgs();
    private static HashMap<String, Node> myNodes;

    private WebTarget target;
    private String baseURL = "http://localhost:8080/Neo4JXML/rest/resource";
    private Client client;

    // method for service
    public List<FLNffg> getNffgs() {
        updateDB();
        return new ArrayList<>(nffgs.values());
    }

    /*
    public FLNffg getNffg(int nffg_id) {
        updateDB(); //TODO
    }

    public FLNodes getNffgNodes(int nffg_id) {
        updateDB(); //TODO
    }

    public FLNode getNffgNode(int nffg_id, int node_id) {
        updateDB(); //TODO
    }

    public FLLinks getNffgNodeLinks(int nffg_id, int node_id) {
        updateDB(); //TODO
    }

    public FLLink getNffgNodeLink(int nffg_id, int node_id, int link_id) {
        updateDB(); //TODO
    }

    public FLLinks getNffgLinks(int nffg_id) {
        updateDB(); //TODO
    }

    public FLLink getNffgLink(int nffg_id, int link_id) {
        updateDB(); //TODO
    }

    public FLPolicies getNffgPolicies(int nffg_id) {
        updateDB(); //TODO
    }

    public FLPolicy getNffgPolicy(int nffg_id, int policy_id) {
        updateDB(); //TODO
    }

    public FLPolicies getPolicies() {
        updateDB(); //TODO
    }

    public FLPolicy getPolicy(int policy_id) {
        updateDB(); //TODO
    }
    */

    private void updateDB () {
        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));

        Response response = target.path("nodes")
                .request()
                .accept("application/xml")
                .get();

        System.out.println("--> Getting all nodes: " + response.getStatus());

        if (response.getStatus() == 200) {
            myNodes = new HashMap<>();
            String name = null;

            // for each node
            for (Nodes.Node node : response.readEntity(Nodes.class).getNode()) {

                // create a new node in local setting the id of the one received from Neo4JXML
                Node n = new Node();
                n.setId(node.getId());

                // save all properties of the node
                for (Property property : node.getProperty()) {
                    name = property.getValue();

                    Property p = new Property();
                    p.setName(property.getName());
                    p.setValue(name);
                    n.getProperty().add(p);
                }

                // save all labels pf the node
                node.setLabels(node.getLabels());

                myNodes.put(name, n);
            }

            // empty the local DB
            nffgs.clear();

            for (Node node : myNodes.values()) {
                // is a NFFG, add the new nffg
                if ( getCorrectProperty(node.getProperty(), "NFFG").contains("NFFG") ) {
                    FLNffg nffg = new FLNffg();
                    nffg.setId(Long.parseLong(node.getId()));
                    nffg.setName(getCorrectProperty(node.getProperty(), "name"));
                    nffgs.put(nffg.getId(), nffg);
                } else {
                    // is a node
                    FLNode node_temp = new FLNode();
                    node_temp.setId(Long.parseLong(node.getId()));
                    node_temp.setName(getCorrectProperty(node.getProperty(), "name"));

                    NodeFunctionalType functionalType = NodeFunctionalType.valueOf((getCorrectProperty(node.getProperty(), "functionalType")));
                    node_temp.setFunctionalType(functionalType);

                    getCorrectNffg(getCorrectProperty(node.getProperty(), "belongs")).getFLNode().add(node_temp);

                    for (String s : node.getLabels().getValue()) {
                        FLLink flLink = new FLLink();
                        flLink.setId(0);
                        flLink.setName(s);
                        flLink.setSourceNode(getCorrectNodeID(getCorrectProperty(node.getProperty(), "name")));
                        flLink.setDestinationNode(getCorrectNodeID(s));

                        getCorrectNffg(getCorrectProperty(node.getProperty(), "belongs")).getFLLink().add(flLink);
                    }
                }
            }
        }


    }

    private long getCorrectNodeID(String name) {
        for (Node n : myNodes.values()) {
            if (getCorrectProperty(n.getProperty(), "name").contains(name)) {
                return Long.parseLong(n.getId());
            }
        }

        return -1;
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private String getCorrectProperty(List<Property> properies, String string) {
        for (Property property : properies) {
            if (property.getName().contains(string)) {
                return property.getValue();
            }
        }
        return "-1";
    }

    private FLNffg getCorrectNffg(String string) {
        for (FLNffg flNffg : nffgs.values()) {
            if (flNffg.getName().contains(string)) {
                return flNffg;
            }
        }
        return null;
    }
}
