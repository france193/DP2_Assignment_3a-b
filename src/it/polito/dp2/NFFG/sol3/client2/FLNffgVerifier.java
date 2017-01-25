package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.*;
import it.polito.dp2.NFFG.sol3.client2.models.NffgService.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLNffgVerifier implements NffgVerifier {
    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";
    private static int counter = 0;

    private WebTarget target;
    private String baseURL;
    private Client client;

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> allNffgs;
    private HashMap<String, PolicyReader> allPolicies;
    private HashMap<String, FLNffg> allFLNffgs;
    private HashMap<String, FLPolicy> allFLPolicies;

    StringBuilder debug1 = new StringBuilder();

    public FLNffgVerifier() {
        allNffgs = new HashMap<>();
        allPolicies = new HashMap<>();
        allFLNffgs = new HashMap<>();
        allFLPolicies = new HashMap<>();

        if ((baseURL = System.getProperty("it.polito.dp2.NFFG.lab3.URL")) == null) {
            baseURL = DEFAULT_URL;
        }

        baseURL = baseURL + "/resource";

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));
    }

    private void retrieveNffgs() {
        allFLNffgs.clear();
        allNffgs.clear();

        Response response = target.path("nffgs")
                .request()
                .accept("application/xml")
                .get();

        debug1.append("***********************************************\n");

        int status = response.getStatus();

        debug1.append("NFFGS RESPONSE - " + status + "\n");
        debug1.append("------------------------------------------------\n");
        if (status == 200) {
            FLNffgs nffgs = response.readEntity(FLNffgs.class);
            for (FLNffg nffg : nffgs.getFLNffg()) {
                debug1.append("NAME: " + nffg.getName() + "\n");
                debug1.append("FLNodes # - " + nffg.getFLNode().size() + "\n");
                debug1.append("FLLinks # - " + nffg.getFLLink().size() + "\n");
                //debug1.append("FLPolicies # - " + nffg.getFLPolicy().size() + "\n");
                debug1.append("------------------------------------------------\n");
                allFLNffgs.put(nffg.getName(), nffg);
            }
            debug1.append("***********************************************\n");
            convertNffgInNffgReaders();
            debug1.append("FLNffgs # - " + allFLNffgs.size() + "\n");
            debug1.append("NffgReades # - " + allNffgs.size() + "\n");
            debug1.append("***********************************************\n");
            logFile(debug1.toString());
        }
    }

    private void retrievePolicies() {
        allPolicies.clear();
        allFLPolicies.clear();

        Response response = target.path("policies")
                .request()
                .accept("application/xml")
                .get();

        StringBuilder debug2 = new StringBuilder();
        debug2.append("***********************************************\n");

        int status = response.getStatus();

        debug2.append("POLICIES RESPONSE - " + status + "\n");

        if (status == 200) {
            FLPolicies policies = response.readEntity(FLPolicies.class);

            for (FLPolicy policy : policies.getFLPolicy()) {
                allFLPolicies.put(policy.getName(), policy);
            }

            debug2.append("FLPolicies # - " + allFLPolicies.size() + "\n");
            convertPoliciesInPolicyReaders();
            debug2.append("PolicyReaders # - " + allPolicies.size() + "\n");
            debug2.append("***********************************************\n");
            logFile(debug2.toString());
        }
    }

    private void retrieveData() {
        retrieveNffgs();
        retrievePolicies();
    }

    @Override
    public Set<NffgReader> getNffgs() {
        retrieveData();
        return new LinkedHashSet(allNffgs.values());
    }

    @Override
    public NffgReader getNffg(String s) {
        retrieveData();
        return allNffgs.get(s);
    }

    @Override
    public Set<PolicyReader> getPolicies() {
        retrieveData();
        return new LinkedHashSet(allFLPolicies.values());
    }

    @Override
    public Set<PolicyReader> getPolicies(String s) {
        retrieveData();

        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, PolicyReader> entry : allPolicies.entrySet()) {
            if (entry.getValue().getNffg().getName().contains(s)) {
                policies.add(entry.getValue());
            }
        }

        return (policies.size() == 0) ? (null) : policies;
    }

    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        retrieveData();

        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, PolicyReader> entry : allPolicies.entrySet()) {
            if (entry.getValue().getResult().getVerificationTime().after(calendar)) {
                policies.add(entry.getValue());
            }
        }

        return (policies.size() == 0) ? (null) : policies;
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private void convertNffgInNffgReaders() {
        HashMap<String, String> namOfNode = new HashMap<>();

        for (FLNffg nffg : allFLNffgs.values()) {
            FLNffgReader nffgReader = new FLNffgReader(nffg.getName(), nffg.getLastUpdatedTime().toGregorianCalendar());

            // NODE
            for (FLNode node : nffg.getFLNode()) {
                FLNodeReader nodeReader = new FLNodeReader(FunctionalType.fromValue(node.getFunctionalType().value()), node.getName());

                //debug1.append("Added: " + node.getId() + " - " + node.getName() + "\n");
                namOfNode.put(node.getId(), node.getName());
                //debug1.append("--node-> " + nodeReader.getName() + ", " + nodeReader.getFuncType() + "\n");
                //debug1.append("NodeREaders # - " + nffgReader.getNodes().size() + "\n");
                nffgReader.addNode(nodeReader);
                //debug1.append("NodeREaders # - " + nffgReader.getNodes().size() + "\n");
            }
            debug1.append("NodeReaders # - " + nffgReader.getNodes().size() + "\n");

            int c = 0;
            //LINK
            for (FLLink link : nffg.getFLLink()) {
                //debug1.append("I have node # - " + nffgReader.getNodes().size() + "\n");
                //debug1.append("src - " + link.getSourceNode() + "\n");
                //debug1.append("dst - " + link.getDestinationNode() + "\n");
                FLLinkReader linkReader = new FLLinkReader(link.getId(),
                        retriveNodeReaderFromID(namOfNode.get(link.getSourceNode()), nffgReader.getNodes()),
                        retriveNodeReaderFromID(namOfNode.get(link.getDestinationNode()), nffgReader.getNodes()));

                retriveNodeReaderFromID(namOfNode.get(link.getSourceNode()), nffgReader.getNodes()).getLinks().add(linkReader);
                c++;
            }
            debug1.append("LinkReaders # - " + c + "\n");
            debug1.append("------------------------------------------------\n");

            allNffgs.put(nffgReader.getName(), nffgReader);
        }
    }

    private NodeReader retriveNodeReaderFromID(String sourceNode, Set<NodeReader> nodeReaders) {
        for (NodeReader nodeReader : nodeReaders) {
            if (nodeReader.getName().equals(sourceNode)) {
                return nodeReader;
            }
        }

        return null;
    }

    private void convertPoliciesInPolicyReaders() {
        for (FLPolicy policy : allFLPolicies.values()) {
            FLReachabilityPolicyReader flReachabilityPolicyReader = new FLReachabilityPolicyReader(policy.getName(),
                    retrieveNffgReaderFromName(policy.getNffgName()),
                    policy.isIsPositive(),
                    retriveNodeReaderFromID(policy.getSourceNode(), retrieveNffgReaderFromName(policy.getNffgName()).getNodes()),
                    retriveNodeReaderFromID(policy.getSourceNode(), retrieveNffgReaderFromName(policy.getNffgName()).getNodes()));

            if (policy.getFLVResult() != null) {
                FLVerificationResultReader flVerificationResultReader = new FLVerificationResultReader(flReachabilityPolicyReader,
                        policy.getFLVResult().isResult(),
                        policy.getFLVResult().getTime().toGregorianCalendar(),
                        policy.getFLVResult().getMessage());
                flReachabilityPolicyReader.setVerificationResult(flVerificationResultReader);
            }

            if (policy.getFLTraversalRequestedNode().size() != 0) {
                FLTraversalPolicyReader x = (FLTraversalPolicyReader) flReachabilityPolicyReader;

                for (FLPolicy.FLTraversalRequestedNode traversalRequestedNode : policy.getFLTraversalRequestedNode()) {
                    x.getTraversedFuctionalTypes().add(FunctionalType.fromValue(traversalRequestedNode.toString()));
                }
            }

            allPolicies.put(flReachabilityPolicyReader.getName(), flReachabilityPolicyReader);
        }
    }

    private NffgReader retrieveNffgReaderFromName(String nffgName) {
        for (NffgReader nffgReader : allNffgs.values()) {
            if (nffgReader.getName().equals(nffgName)) {
                return nffgReader;
            }
        }
        return null;
    }

    public void logFile(String toWtrite) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File("/Users/FLDeviOS/Desktop/log/Client2/FLNffgVerifier/" + "C2_VERIFIER_" + counter + ".txt");
            counter++;

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

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
