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

    public FLNffg getNffg(String nffg_id) {
        updateDB();

        return nffgs.get(nffg_id);
    }

    public FLNodes getNffgNodes(String nffg_id) {
        updateDB();

        FLNodes nodes = new FLNodes();
        nodes.getFLNode().addAll(nffgs.get(nffg_id).getFLNode());

        return nodes;
    }

    public FLNode getNffgNode(String nffg_id, String node_id) {
        updateDB();

        for (FLNode node : nffgs.get(nffg_id).getFLNode()) {
            if (node.getId().contains(node_id)) {
                return node;
            }
        }

        return null;
    }

    public FLLinks getNffgNodeLinks(String nffg_id, String node_id) {
        updateDB();

        FLLinks links = new FLLinks();

        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            if (link.getSourceNode().equals(node_id)) {
                links.getFLLink().add(link);
            }
        }

        return links;
    }

    public FLLink getNffgNodeLink(String nffg_id, String node_id, String link_id) {
        updateDB();

        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            if (link.getSourceNode().equals(node_id) && link.getId().equals(link_id)) {
                return link;
            }
        }

        return null;
    }

    public FLLinks getNffgLinks(String nffg_id) {
        updateDB();

        FLLinks links = new FLLinks();

        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            links.getFLLink().add(link);
        }

        return links;
    }

    public FLLink getNffgLink(String nffg_id, String link_id) {
        updateDB();

        for (FLLink link : nffgs.get(nffg_id).getFLLink()) {
            if (link.getId().equals(link_id)) {
                return  link;
            }
        }

        return null;
    }

    /*
    public FLPolicies getNffgPolicies(String nffg_id) {
        updateDB(); //TODO
    }

    public FLPolicy getNffgPolicy(String nffg_id, String policy_id) {
        updateDB(); //TODO
    }

    public FLPolicies getPolicies() {
        updateDB(); //TODO
    }

    public FLPolicy getPolicy(String policy_id) {
        updateDB(); //TODO
    }
    */

    /**
     * OTHER METHODS
     **/

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
                        if (!s.contains("NFFG")) {
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
