package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import scala.Int;
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

    private Map<String, FLNffg> nffgs = NffgDB.getNffgs();
    private HashMap<String, String> nodesID;

    private WebTarget target;
    private String baseURL = "http://localhost:8080/Neo4JXML/rest/resource";
    private Client client;

    public NffgServices() {
        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));
    }

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
        // empty the local DB
        nffgs.clear();

        getNodeIDs("nodes");
        getAllNodesInfo("node");
    }

    private void getNodeIDs(String path) {
        Response response = target.path(path)
                .request()
                .accept("application/xml")
                .get();

        if (response.getStatus() == 200) {
            nodesID = new HashMap<>();

            for (Nodes.Node node : response.readEntity(Nodes.class).getNode()) {
                nodesID.put(node.getId(), node.getProperty().get(0).getValue());
            }
        }
    }

    private void getAllNodesInfo(String path) {
        Response response;
        String NffgID = null;
        String id;
        String nodeName = null;

        // Save Nffgs
        for (Map.Entry<String, String> entry : nodesID.entrySet()) {

            response = target.path(path)
                    .path(entry.getKey().toString())
                    .request()
                    .accept("application/xml")
                    .get();

            if (response.getStatus() == 200) {
                Node node = response.readEntity(Node.class);

                if (node.getProperty().size() > 0) {

                    // NFFG
                    if (isNffg(node.getProperty())) {
                        FLNffg nffg = new FLNffg();

                        id = node.getId();
                        nffg.setId(id);
                        nffg.setName(node.getProperty().get(1).getValue());

                        nffgs.put(id, nffg);
                    }
                }
            } else {
                // TODO EXCEPTION CONNECTION NEO4JXML
            }
        }

        // Save Nodes
        for (Map.Entry<String, String> entry : nodesID.entrySet()) {

            response = target.path(path)
                    .path(entry.getKey().toString())
                    .request()
                    .accept("application/xml")
                    .get();

            if (response.getStatus() == 200) {
                Node node = response.readEntity(Node.class);

                if (node.getProperty().size() > 0) {

                    // Node
                    if (!isNffg(node.getProperty())) {
                        FLNode n = new FLNode();
                        n.setId(node.getId());

                        for (Property p : node.getProperty()) {

                            switch (p.getName()) {
                                case "name":
                                    n.setName(p.getValue());
                                    break;

                                case "belongs":
                                    NffgID = findNffgID(p.getValue());
                                    break;

                                case "functionalType":
                                    n.setFunctionalType(NodeFunctionalType.valueOf(p.getValue()));
                                    break;

                                default:
                                    break;
                            }
                        }
                        nffgs.get(NffgID).getFLNode().add(n);
                    }
                }
            } else {
                // TODO EXCEPTION CONNECTION NEO4JXML
            }
        }

        // Save Links
        for (Map.Entry<String, String> entry : nodesID.entrySet()) {

            response = target.path(path)
                    .path(entry.getKey().toString())
                    .request()
                    .accept("application/xml")
                    .get();

            if (response.getStatus() == 200) {
                Node node = response.readEntity(Node.class);

                for (Property p : node.getProperty()) {

                    switch (p.getName()) {

                        case "name":
                            nodeName = p.getValue();
                            break;
                            
                        case "belongs":
                            NffgID = findNffgID(p.getValue());
                            break;

                        default:
                            break;
                    }
                }

                if (node.getLabels() != null) {
                    for (String s : node.getLabels().getValue()) {
                        if ( !s.contains("NFFG") ) {
                            FLLink link = new FLLink();
                            link.setId("FROM_" + nodeName + "_TO_" + s);
                            link.setSourceNode(node.getId());
                            link.setDestinationNode(s);

                            nffgs.get(NffgID).getFLLink().add(link);
                        }
                    }
                }

            } else {
                // TODO EXCEPTION CONNECTION NEO4JXML
            }
        }

        /*
        else {
                        FLNode n = new FLNode();
                        n.setId(node.getId());

                        // NODE
                        for (Property p : node.getProperty()) {

                            switch (p.getName()) {
                                case "name":
                                    n.setName(p.getValue());
                                    break;

                                case "belongs":
                                    FLNffg nffg1 = new FLNffg();
                                    if (findNffgID(p.getValue()) == null) {
                                        nffg1.setName(node.getId() + " null " + i);
                                    } else {
                                        nffg1.setName(node.getId() + " not null " + findNffgID(p.getValue()) + " " + i);
                                    }
                                    nffgs.put("" + i, nffg1);
                                    break;

                                case "functionalType":
                                    n.setFunctionalType(NodeFunctionalType.valueOf(p.getValue()));
                                    break;

                                default:
                                    break;
                            }

                            //nffgs.get(NffgID).getFLNode().add(n);
                        }

                        // NODE has some links
                        if (node.getLabels() != null) {
                            for (String s : node.getLabels().getValue()) {
                                FLLink link = new FLLink();
                                link.setId("FROM" + node.getId() + "TO" + s);
                                link.setSourceNode(node.getId());
                                link.setDestinationNode(s);

                                //nffgs.get(NffgID).getFLLink().add(link);
                            }
                        }
                    }
        */
    }

    private String findNffgID(String name) {
        for (Map.Entry<String, FLNffg> entry : nffgs.entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getValue().getId();
            }
        }
        return null;
    }

    private boolean isNffg(List<Property> properties) {
        for (Property p : properties) {
            if (p.getName().equals("NFFG") && p.getValue().equals("NFFG")) {
                return true;
            }
        }
        return false;
    }
}
