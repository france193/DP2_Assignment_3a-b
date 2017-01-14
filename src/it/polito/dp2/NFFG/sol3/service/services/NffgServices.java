package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
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

            for (Nodes.Node node : response.readEntity(Nodes.class).getNode()) {
                Node n = new Node();
                n.setId(node.getId());
                for (Property property : node.getProperty()) {
                    name = property.getValue();
                    Property p = new Property();
                    p.setName(property.getName());
                    p.setValue(name);
                    n.getProperty().add(p);
                }
                myNodes.put(name, n);
            }
        }


    }

    private static URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }
}
