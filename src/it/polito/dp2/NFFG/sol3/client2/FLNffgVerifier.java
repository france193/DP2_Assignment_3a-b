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
 * Created by Francesco Longo (223428) on 21/02/2017.
 */
public class FLNffgVerifier implements NffgVerifier {
    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private WebTarget target;
    private String baseURL;
    private Client client;
    private int counter = 0;

    // set of Nffgs and Policies
    private HashMap<String, FLNffgReader> allNffgs;
    private HashMap<String, PolicyReader> allPolicies;

    private HashMap<String, FLNffg> allFLNffgs;
    private HashMap<String, FLPolicy> allFLPolicies;
    private HashMap<String, FLVResult> allFLVResults;

    public FLNffgVerifier() throws NffgVerifierException {
        allNffgs = new HashMap<>();
        allPolicies = new HashMap<>();

        allFLNffgs = new HashMap<>();
        allFLPolicies = new HashMap<>();
        allFLVResults = new HashMap<>();

        if ((baseURL = System.getProperty("it.polito.dp2.NFFG.lab3.URL")) == null) {
            baseURL = DEFAULT_URL;
        }

        baseURL = baseURL + "/resource";

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));

        retrieveAllDataFromServer();
    }

    private synchronized void retrieveAllDataFromServer() throws NffgVerifierException {
        Response response = target.path("nffgs").request().accept("application/xml").get();
        int status = response.getStatus();

        if (status == 200) {
            // retrieve all nffgs
            FLNffgs nffgs = response.readEntity(FLNffgs.class);

            for (FLNffg nffg : nffgs.getFLNffg()) {
                allFLNffgs.put(nffg.getName(), nffg);
            }

            // save policies separately
            for (FLPolicy policy : nffgs.getFLPolicy()) {
                allFLPolicies.put(policy.getName(), policy);
            }

            // save flvresults separately
            for (FLVResult result : nffgs.getFLVResult()) {
                allFLVResults.put(result.getPolicy(), result);
            }

            for (FLNffg nffg : allFLNffgs.values()) {
                FLNffgReader nffgReader = new FLNffgReader(nffg.getName(), nffg.getLastUpdatedTime().toGregorianCalendar());

                // NODES
                for (FLNode node : nffg.getFLNode()) {
                    FLNodeReader nodeReader = new FLNodeReader(FunctionalType.valueOf(node.getFunctionalType().value()), node.getName());

                    // LINKS
                    for (FLNode.FLLink link : node.getFLLink()) {
                        FLNodeReader dstNodeReader = findNodeFromName(link.getDestinationNode(), nffg);
                        if (dstNodeReader == null) {
                            throw new NffgVerifierException("dstNodeReader not found");
                        }
                        FLLinkReader linkReader = new FLLinkReader(link.getName(), nodeReader, dstNodeReader);

                        nodeReader.addLink(linkReader);
                    }

                    nffgReader.addNode(nodeReader);
                }

                allNffgs.put(nffgReader.getName(), nffgReader);
            }

            // POLICIES and VRESULTS
            String src, dst;

            for (FLPolicy policy : allFLPolicies.values()) {
                FLNffgReader nffgReader = retrieveNffgReaderFromName(policy.getNffgName());

                src = nffgReader.getNamOfNode().get(policy.getSourceNode());
                dst = nffgReader.getNamOfNode().get(policy.getDestinationNode());

                FLVerificationResultReader verificationResultReader = null;

                // Traversal
                if (policy.getTraversedNode().size() != 0) {
                    Set<FunctionalType> setTraversed = new LinkedHashSet<>();

                    for (String traversalRequestedNode : policy.getTraversedNode()) {
                        setTraversed.add(FunctionalType.fromValue(traversalRequestedNode));
                    }

                    FLTraversalPolicyReader t = new FLTraversalPolicyReader(policy.getName(), nffgReader,
                            policy.isIsPositive(), nffgReader.getNode(src), nffgReader.getNode(dst), setTraversed);

                    FLVResult result = allFLVResults.get(policy.getName());
                    if (result != null) {
                        verificationResultReader = new FLVerificationResultReader(t, result.isResult(),
                                result.getMessage(), result.getTime().toGregorianCalendar());
                    }

                    t.setVerificationResultReader(verificationResultReader);

                    allPolicies.put(t.getName(), t);
                } else {
                    // Reachability
                    FLReachabilityPolicyReader r = new FLReachabilityPolicyReader(policy.getName(), nffgReader,
                            policy.isIsPositive(), nffgReader.getNode(src), nffgReader.getNode(dst));

                    FLVResult result = allFLVResults.get(policy.getName());
                    if (result != null) {
                        verificationResultReader = new FLVerificationResultReader(r, result.isResult(),
                                result.getMessage(), result.getTime().toGregorianCalendar());
                    }

                    r.setVerificationResultReader(verificationResultReader);

                    allPolicies.put(r.getName(), r);
                }
            }
        }
    }

    /**
     * Gives access to the set of known NF-FGs.
     *
     * @return
     */
    @Override
    public Set<NffgReader> getNffgs() {
        return new LinkedHashSet<NffgReader>(this.allNffgs.values());
    }

    /**
     * Gives access to a single NF-FG given its entityName.
     *
     * @param s
     * @return
     */
    @Override
    public NffgReader getNffg(String s) {

        NffgReader nffg = this.allNffgs.get(s);

        if (nffg != null) {
            return nffg;
        } else {
            return null;
        }
    }

    /**
     * Gives access to the set of known policies to be verified.
     *
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies() {
        return new LinkedHashSet<PolicyReader>(this.allPolicies.values());
    }

    /**
     * Gives access to the set of known policies to be verified, filtered by
     * NF-FG's entityName.
     *
     * @param s
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies(String s) {
        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, PolicyReader> entry : allPolicies.entrySet()) {
            if (entry.getValue().getNffg().getName().contains(s)) {
                policies.add(entry.getValue());
            }
        }

        return policies;
    }

    /**
     * Gives access to the set of known policies to be verified, filtered by
     * verification verificationTime and date.
     *
     * @param calendar
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, PolicyReader> entry : allPolicies.entrySet()) {
            if (entry.getValue().getResult().getVerificationTime().after(calendar)) {
                policies.add(entry.getValue());
            }
        }

        return policies;
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private FLNffgReader retrieveNffgReaderFromName(String nffgName) {
        for (FLNffgReader nffgReader : allNffgs.values()) {
            if (nffgReader.getName().equals(nffgName)) {
                return nffgReader;
            }
        }
        return null;
    }

    private FLNodeReader findNodeFromName(String node, FLNffg nffg) {
        for (FLNode nodeType : nffg.getFLNode()) {
            if (nodeType.getName().equals(node)) {
                return new FLNodeReader(FunctionalType.valueOf(nodeType.getFunctionalType().value()), nodeType.getName());
            }
        }

        return null;
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
}
