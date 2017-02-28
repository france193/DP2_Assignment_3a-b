package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.sol3.service.database.NffgDB;
import it.polito.dp2.NFFG.sol3.service.models.Neo4jXML.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLNode.FLLink;

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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Francesco Longo (223428) on 21/02/2017.
 */
public class NffgServices {

    private ConcurrentHashMap<String, FLNffg> nffgsServices = NffgDB.getNffgs();
    private ConcurrentHashMap<String, FLPolicy> policiesServices = NffgDB.getPolicies();
    private ConcurrentHashMap<String, FLVResult> vresultsServices = NffgDB.getVresults();

    private static String defaultNeo4JXMLURL = "http://localhost:8080/Neo4JXML/rest";
    private String baseURL;

    private WebTarget target;
    private Client client;
    private int counter = 0;

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

        // delete all nodes on Neo4j
        /*
		if (NffgDB.getFirstBoot()) {
			Response response = target.path("nodes").request().accept("application/xml").delete();

			// if it is not possible to delete all nodes on Neo4JXML
			if (response.getStatus() != 200) {
				throw new ServiceUnavailableException();
			}
			NffgDB.setFirstBoot(false);
		}
		*/
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    public synchronized FLNffgs getNffgs() {
        FLNffgs nffgs_to_show = new FLNffgs();

        // nffg
        for (FLNffg nffg : nffgsServices.values()) {
            nffgs_to_show.getFLNffg().add(nffg);
        }

        // policies
        for (FLPolicy policy : policiesServices.values()) {
            nffgs_to_show.getFLPolicy().add(policy);
        }

        // vresults
        for (FLVResult vresult : vresultsServices.values()) {
            nffgs_to_show.getFLVResult().add(vresult);
        }

        return nffgs_to_show;
    }

    public synchronized FLNffgs postNffgs(FLNffgs nffgs_to_post)
            throws ElementAlreadyLoadedException, DatatypeConfigurationException, Neo4JServiceException {
        FLNffgs nffgs_loaded = new FLNffgs();

        // check if at least one nffg is a duplicate before saving
        for (FLNffg nffg_temp : nffgs_to_post.getFLNffg()) {
            if (nffgsServices.get(nffg_temp.getName()) != null) {
                throw new ElementAlreadyLoadedException("(!) - One nffg with same name already exists!");
            }
        }

        // NFFG
        for (FLNffg nffg_temp : nffgs_to_post.getFLNffg()) {
            Response response;

            XMLGregorianCalendar date2 = null;
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

            // create new nffg and set name and updated time
            FLNffg newNffg = new FLNffg();
            newNffg.setName(nffg_temp.getName());
            newNffg.setLastUpdatedTime(date2);

            // Create the NFFG as a NODE on Neo4J and insert it
            Node nffgNodeToUpload = new Node();

            Property p = new Property();
            p.setName("name");
            p.setValue(nffg_temp.getName());
            nffgNodeToUpload.getProperty().add(p);

            // upload to Neo4JXML
            response = target.path("node").request().accept("application/xml")
                    .post(Entity.entity(nffgNodeToUpload, "application/xml"));

            if (response.getStatus() != 200) {
                throw new Neo4JServiceException("(!) - error uploading nffg to neo4j");
            }

            // take the id of the Nffg on Neo4JXML and save it
            newNffg.setId(response.readEntity(Node.class).getId());

            // set the label as requested for the just loaded nffg
            Labels labels = new Labels();
            labels.getValue().add("NFFG");

            response = target.path("node").path(newNffg.getId()).path("label").request().accept("application/xml")
                    .post(Entity.entity(labels, "application/xml"));

            if (response.getStatus() != 204) {
                throw new Neo4JServiceException("(!) - error setting nffg label on neo4j");
            }

            // NODES
            for (FLNode node_temp : nffg_temp.getFLNode()) {
                FLNode newNode = new FLNode();
                newNode.setName(node_temp.getName());
                newNode.setFunctionalType(node_temp.getFunctionalType());

                // create the new node to insert on Neo4JXML
                Node nodeToUpload = new Node();

                Property p1 = new Property();
                p1.setName("name");
                p1.setValue(node_temp.getName());
                nodeToUpload.getProperty().add(p1);

                Property p2 = new Property();
                p2.setName("functional_type");
                p2.setValue(node_temp.getFunctionalType().toString());
                nodeToUpload.getProperty().add(p2);

                // upload node
                response = target.path("node").request().accept("application/xml")
                        .post(Entity.entity(nodeToUpload, "application/xml"));

                if (response.getStatus() != 200) {
                    throw new Neo4JServiceException("(!) - error uploading node to neo4j");
                }

                // save id given by Neo4JXML for the node
                newNode.setId(response.readEntity(Node.class).getId());

                // node belongs to the nffg on Neo4JXML
                Relationship r = new Relationship();

                r.setType("belongs");
                r.setDstNode(newNode.getId());

                response = target.path("node").path(newNffg.getId()).path("relationship").request()
                        .accept("application/xml").post(Entity.entity(r, "application/xml"));

                if (response.getStatus() != 200) {
                    throw new Neo4JServiceException("(!) - error setting relationship between node and nffg on neo4j");
                }

                // LINKS (just add link in the service - local)
                for (FLLink link_temp : node_temp.getFLLink()) {
                    FLLink newLink = new FLLink();

                    newLink.setName(link_temp.getName());
                    newLink.setDestinationNode(link_temp.getDestinationNode());

                    newNode.getFLLink().add(newLink);
                }

                // add node to newNffg
                newNffg.getFLNode().add(newNode);
            }

            // NODES
            for (FLNode node_temp : newNffg.getFLNode()) {

                // LINKS
                for (FLLink link_temp : node_temp.getFLLink()) {
                    String destinationNodeId;
                    try {
                        destinationNodeId = getNodeIdFromName(newNffg.getFLNode(), link_temp.getDestinationNode());
                    } catch (NodeNotFoundException e) {
                        throw new Neo4JServiceException(e.getMessage());
                    }
                    Relationship relationship = new Relationship();

                    relationship.setType("Link");
                    relationship.setDstNode(destinationNodeId);

                    response = target.path("node").path(node_temp.getId()).path("relationship").request()
                            .accept("application/xml").post(Entity.entity(relationship, "application/xml"));

                    if (response.getStatus() != 200) {
                        throw new Neo4JServiceException("(!) -error adding relationship to node on neo4j ");
                    }

                    Labels l = new Labels();
                    // add the id of all relationship for this node
                    l.getValue().add(response.readEntity(Relationship.class).getId());

                    response = target.path("node").path(destinationNodeId).path("label").request()
                            .accept("application/xml").post(Entity.entity(l, "application/xml"));

                    if (response.getStatus() != 204) {
                        throw new Neo4JServiceException("(!) -error setting node relationships label on neo4j");
                    }
                }
            }

            // save nffg on my local DB and on the nffgs to return
            nffgsServices.put(newNffg.getName(), newNffg);
            nffgs_loaded.getFLNffg().add(newNffg);
        }

        FLPolicies policies = new FLPolicies();

        for (FLPolicy p : nffgs_to_post.getFLPolicy()) {
            policies.getFLPolicy().add(p);
        }

        for (FLVResult v : nffgs_to_post.getFLVResult()) {
            policies.getFLVResult().add(v);
        }

        postPolicies(policies);

        return nffgs_loaded;
    }

    private String getNodeIdFromName(List<FLNode> flNode, String destinationNode) throws NodeNotFoundException {
        for (FLNode node : flNode) {
            if (node.getName().equals(destinationNode)) {
                return node.getId();
            }
        }

        throw new NodeNotFoundException("(!) - node not found in the list");
    }

    public FLNffg getNffg(String nffg_id) {
        return nffgsServices.get(nffg_id);
    }

    public FLNodes getNffgNodes(String nffg_id) {
        FLNodes nodes = new FLNodes();
        FLNffg nffg = nffgsServices.get(nffg_id);

        if (nffg == null) {
            return null;
        }

        for (FLNode node : nffg.getFLNode()) {
            nodes.getFLNode().add(node);
        }

        return nodes;
    }

    public FLNode getNffgNode(String nffg_id, String node_id) {
        FLNffg nffg = nffgsServices.get(nffg_id);

        if (nffg == null) {
            return null;
        }

        for (FLNode node : nffg.getFLNode()) {
            if (node.getName().equals(node_id)) {
                return node;
            }
        }

        return null;
    }

    public FLPolicies getNffgPolicies(String nffg_id) {
        FLPolicies policies = new FLPolicies();

        if (nffgsServices.get(nffg_id) == null) {
            return null;
        }

        for (Entry<String, FLPolicy> entry : policiesServices.entrySet()) {
            if (entry.getValue().getNffgName().equals(nffg_id)) {
                policies.getFLPolicy().add(entry.getValue());

                for (Entry<String, FLVResult> entry1 : vresultsServices.entrySet()) {
                    if (entry1.getValue().getPolicy().equals(entry.getValue().getName())) {
                        policies.getFLVResult().add(entry1.getValue());
                    }
                }
            }
        }

        return policies;
    }

    public FLPolicy getNffgPolicy(String nffg_id, String policy_id) {
        FLPolicy policy = new FLPolicy();

        if (nffgsServices.get(nffg_id) == null) {
            return null;
        }

        for (Entry<String, FLPolicy> entry : policiesServices.entrySet()) {
            if ((entry.getValue().getNffgName().equals(nffg_id)) && (entry.getValue().getName().equals(policy_id))) {
                policy = entry.getValue();
                for (Entry<String, FLVResult> entry1 : vresultsServices.entrySet()) {
                    if (entry1.getValue().getPolicy().equals(entry.getValue().getName())) {
                        policy.setFLVResult(entry1.getValue());
                    }
                }
                return policy;
            }
        }

        return null;
    }

    public FLPolicies getPolicies() {
        FLPolicies policies = new FLPolicies();

        for (Entry<String, FLPolicy> entry : policiesServices.entrySet()) {
            policies.getFLPolicy().add(entry.getValue());

            for (Entry<String, FLVResult> entry1 : vresultsServices.entrySet()) {
                if (entry1.getValue().getPolicy().equals(entry.getValue().getName())) {
                    policies.getFLVResult().add(entry1.getValue());
                }
            }
        }

        return policies;
    }

    public FLPolicy getPolicy(String policy_id) {
        FLPolicy policy = new FLPolicy();

        for (Entry<String, FLPolicy> entry : policiesServices.entrySet()) {
            if (entry.getValue().getName().equals(policy_id)) {
                policy = entry.getValue();
                for (Entry<String, FLVResult> entry1 : vresultsServices.entrySet()) {
                    if (entry1.getValue().getPolicy().equals(entry.getValue().getName())) {
                        policy.setFLVResult(entry1.getValue());
                    }
                }
                return policy;
            }
        }

        return null;
    }

    public FLVResults verifyPolicies(FLVResults flvResults)
            throws DatatypeConfigurationException, Neo4JServiceException {
        FLVResults flvresults = new FLVResults();

        for (FLVResult flvresult : flvResults.getFLVResult()) {
            Response response;
            XMLGregorianCalendar date2;
            FLPolicy policy;

            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

            flvresult.setTime(date2);

            policy = policiesServices.get(flvresult.getPolicy());

            if (policy == null) {
                continue;
            }

            String src, dst;
            src = getNodeIdFromNodeName(policy.getNffgName(), policy.getSourceNode());
            dst = getNodeIdFromNodeName(policy.getNffgName(), policy.getDestinationNode());

            response = target.path("node").path(src).path("paths").queryParam("dst", dst).request()
                    .accept("application/xml").get();

            if (response.getStatus() != 200) {
                throw new Neo4JServiceException("(!) - Error verifying policy");
            }

            Paths paths = response.readEntity(Paths.class);

            if (paths.getPath().size() == 0) {
                flvresult.setResult(false);
                flvresult.setMessage("Policy is not verified");
            } else {
                flvresult.setResult(true);
                flvresult.setMessage("Policy is verified");
            }

            // add result
            vresultsServices.put(flvresult.getPolicy(), flvresult);
            flvresults.getFLVResult().add(flvresult);
        }

        return flvresults;
    }

    public FLPolicies postPolicies(FLPolicies policies) {
        FLPolicies saved = new FLPolicies();

        for (FLPolicy policy : policies.getFLPolicy()) {

            if (nffgsServices.get(policy.getNffgName()) == null) {
                continue;
            }

            if ((nodeExists(nffgsServices.get(policy.getNffgName()), policy.getSourceNode()) == false) ||
                    (nodeExists(nffgsServices.get(policy.getNffgName()), policy.getDestinationNode()) == false)) {
                continue;
            }

            // if a policy with same name exist, delete it
            if (policiesServices.get(policy.getName()) != null) {
                policiesServices.remove(policy.getName());
                vresultsServices.remove(policy.getName());
            }

            policiesServices.put(policy.getName(), policy);
            saved.getFLPolicy().add(policy);
        }

        for (FLVResult result : policies.getFLVResult()) {
            vresultsServices.put(result.getPolicy(), result);
            saved.getFLVResult().add(result);
        }

        return saved;
    }

    private boolean nodeExists(FLNffg n, String nodeName) {
        for (FLNode node : n.getFLNode()) {
            if (node.getName().equals(nodeName)) {
                return true;
            }
        }
        return false;
    }

    public FLPolicies updatePolicies(FLPolicies policies) {
        return postPolicies(policies);
    }

    public FLPolicy deletePolicy(String policy_id) {
        FLPolicy deleted = policiesServices.get(policy_id);

        if (deleted == null) {
            return null;
        }

        policiesServices.remove(policy_id);
        vresultsServices.remove(policy_id);

        return deleted;
    }

    private String getNodeIdFromNodeName(String nffgName, String nodeName) throws Neo4JServiceException {
        for (FLNode node : nffgsServices.get(nffgName).getFLNode()) {
            if (node.getName().equals(nodeName)) {
                return node.getId();
            }
        }

        throw new Neo4JServiceException("(!) - " + nodeName + " not found in " + nffgName);
    }

    @SuppressWarnings("unused")
    private void logFile(String toWtrite, String name) {
        BufferedWriter writer = null;
        try {
            // create a temporary file
            String timeLog = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());
            File logFile = new File(
                    "/Users/FLDeviOS/Desktop/log/Client1/FLNFFGClient1/" + name + counter + "_" + timeLog + ".txt");
            counter++;

            // This will output the full path where the file will be written
            // to...
            // System.out.println(logFile.getCanonicalPath());

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

	/*
	 * public synchronized FLNffgs removeNffgs(FLNffgs flNffgs) { FLNffgs x =
	 * new FLNffgs(); FLNffg y;
	 * 
	 * for (FLNffg f : flNffgs.getFLNffg()) { if ((y = removeNffg(f.getId())) !=
	 * null) { x.getFLNffg().add(y); } else { return null; } }
	 * 
	 * return x; }
	 * 
	 * public synchronized FLNffg removeNffg(String nffg_id) { Response
	 * response;
	 * 
	 * // retrieve all data FLNffg x = allNffgs.get(nffg_id);
	 * 
	 * // remove from local DB allNffgs.remove(nffg_id);
	 * 
	 * for (FLNode n : x.getFLNode()) {
	 * 
	 * // get node info response = target.path("node") .path(n.getId())
	 * .request() .accept("application/xml") .get();
	 * 
	 * if (response.getStatus() != 200) { return null; }
	 * 
	 * // get all label and delete all relationship Node n1 =
	 * response.readEntity(Node.class); List<String> labels =
	 * n1.getLabels().getValue();
	 * 
	 * for (String label : labels) { response = target.path("relationship")
	 * .path(label) .request() .accept("application/xml") .delete();
	 * 
	 * if (response.getStatus() != 204) { return null; } }
	 * 
	 * // remove from Neo4JXML response = target.path("node") .path(n.getId())
	 * .request() .accept("application/xml") .delete();
	 * 
	 * if (response.getStatus() != 204) { return null; } }
	 * 
	 * // remove from Neo4JXML response = target.path("node") .path(nffg_id)
	 * .request() .accept("application/xml") .delete();
	 * 
	 * if (response.getStatus() != 204) { return null; }
	 * 
	 * return x; }
	 */
}
