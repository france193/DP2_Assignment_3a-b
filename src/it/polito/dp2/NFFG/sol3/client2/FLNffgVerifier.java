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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLNffgVerifier implements NffgVerifier {
    private DateFormat dateFormat;

    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private WebTarget target;
    private String baseURL;
    private Client client;

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> allNffgs;
    private HashMap<String, PolicyReader> allPolicies;
    private HashMap<String, FLNffg> allFLNffgs;
    private HashMap<String, FLPolicy> allFLPolicies;

    public FLNffgVerifier() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss z");

        Response response;

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

        response = target.path("nffgs")
                .request()
                .accept("application/xml")
                .get();

        switch (response.getStatus()) {
            case 200:
                FLNffgs nffgs = response.readEntity(FLNffgs.class);

                for (FLNffg nffg : nffgs.getFLNffg()) {
                    allFLNffgs.put(nffg.getName(), nffg);
                }
                break;
            default:
            case 503:
                //TODO
                throw new NullPointerException();
        }

        response = target.path("policies")
                .request()
                .accept("application/xml")
                .get();

        switch (response.getStatus()) {
            case 200:
                FLPolicies policies = response.readEntity(FLPolicies.class);

                for (FLPolicy policy : policies.getFLPolicy()) {
                    allFLPolicies.put(policy.getName(), policy);
                }
                break;
            default:
            case 503:
                //TODO
                throw new NullPointerException();
        }

        StringBuilder debug = new StringBuilder();
        debug.append("***********************************************\n");
        debug.append("allnffgs - " + allFLNffgs.size() + "\n" +
                "allpolicies: " + allFLPolicies.size() + "\n");
        debug.append("***********************************************\n");
        logFile(debug.toString());

        convertNffgInNffgReaders();
        convertPoliciesInPolicyReaders();
    }

    @Override
    public Set<NffgReader> getNffgs() {
        return new LinkedHashSet(allNffgs.values());
    }

    @Override
    public NffgReader getNffg(String s) {
        return allNffgs.get(s);
    }

    @Override
    public Set<PolicyReader> getPolicies() {
        return new LinkedHashSet(allFLPolicies.values());
    }

    @Override
    public Set<PolicyReader> getPolicies(String s) {
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
        for (FLNffg nffg : allFLNffgs.values()) {
            FLNffgReader nffgReader = new FLNffgReader(nffg.getName(), nffg.getLastUpdatedTime().toGregorianCalendar());

            // NODE
            for (FLNode node : nffg.getFLNode()) {
                FLNodeReader nodeReader = new FLNodeReader(FunctionalType.fromValue(node.getFunctionalType().value()), node.getName());
                nffgReader.getNodes().add(nodeReader);
            }

            //LINK
            for (FLLink link : nffg.getFLLink()) {
                FLLinkReader linkReader = new FLLinkReader(link.getId(),
                        retriveNOdeReaderFromID(link.getSourceNode(), nffgReader.getNodes()),
                        retriveNOdeReaderFromID(link.getDestinationNode(), nffgReader.getNodes()));

                for (NodeReader nodeReader : nffgReader.getNodes()) {
                    if (nodeReader.getName().equals(link.getSourceNode())) {
                        nodeReader.getLinks().add(linkReader);
                    }
                }
            }

            StringBuilder debug = new StringBuilder();
            debug.append("***********************************************\n");
            debug.append("NFFG - " + nffgReader.getName() + "\n" +
                    "#nodes: " + nffgReader.getNodes().size() + "\n" +
                    "#policies: " + nffgReader.getPolicies().size() + "\n");
            debug.append("***********************************************\n");
            logFile(debug.toString());

            allNffgs.put(nffgReader.getName(), nffgReader);
        }
    }

    private NodeReader retriveNOdeReaderFromID(String sourceNode, Set<NodeReader> nodeReaders) {
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
                    retriveNOdeReaderFromID(policy.getSourceNode(), retrieveNffgReaderFromName(policy.getNffgName()).getNodes()),
                    retriveNOdeReaderFromID(policy.getSourceNode(), retrieveNffgReaderFromName(policy.getNffgName()).getNodes()));

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
            File logFile = new File("/Users/FLDeviOS/Desktop/log/" + "FLNffgVerifier" + timeLog + ".txt");

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
