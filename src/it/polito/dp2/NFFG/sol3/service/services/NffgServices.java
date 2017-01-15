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

    private HashMap<Long, String> nodesID;

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

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private void updateDB() {

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));

        getNodeIDs("nodes");
        getAllNodesInfo("node");

        // empty the local DB
        nffgs.clear();
    }

    private void getNodeIDs(String path) {
        Response response = target.path(path)
                .request()
                .accept("application/xml")
                .get();

        if (response.getStatus() == 200) {
            nodesID = new HashMap<>();

            for (Nodes.Node node : response.readEntity(Nodes.class).getNode()) {
                nodesID.put(Long.parseLong(node.getId()), node.getProperty().get(0).getValue());
            }
        }
    }

    private void getAllNodesInfo(String path) {
        Response response;

        for(Map.Entry<Long, String> entry : nodesID.entrySet()) {
            response = target.path(path)
                    .path(entry.getKey().toString())
                    .request()
                    .accept("application/xml")
                    .get();

            if (response.getStatus() == 200) {
                Node node = response.readEntity(Node.class);

                if (node.getProperty().size() > 0) {

                    // NFFG
                    if ( isNffg(node.getProperty()) ) {
                        Property p = node.getProperty().get(1);
                        // System.out.println("  - " + p.getName() + ": " + p.getValue());
                    } else {

                        // NODE
                        for (Property p : node.getProperty()) {
                            //System.out.println("  - " + p.getName() + ": " + p.getValue());
                        }

                        if (node.getLabels() != null) {
                            for (String s : node.getLabels().getValue()) {
                                //System.out.println("  - Linked to: " + s);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isNffg(List<Property> properties) {
        for (Property p : properties) {
            if ( p.getName().equals("NFFG") && p.getValue().equals("NFFG") ) {
                return true;
            }
        }
        return false;
    }
}
