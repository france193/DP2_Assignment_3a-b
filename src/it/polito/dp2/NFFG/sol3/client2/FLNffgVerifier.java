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
 * Created by Francesco Longo (223428) on 23/01/2017.
 */
public class FLNffgVerifier implements NffgVerifier {
    private final Boolean DEBUG = false;
    private final Boolean VERBOSE = false;

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

    private StringBuilder debug1 = new StringBuilder();

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

        retrieveAllDataFromServer();
    }

    private synchronized void retrieveAllDataFromServer() {
        allFLNffgs.clear();
        allNffgs.clear();
        allPolicies.clear();
        allFLPolicies.clear();

        Response response = target.path("nffgs")
                .request()
                .accept("application/xml")
                .get();

        if (DEBUG) {
            debug1.append("***********************************************\n");
        }

        int status = response.getStatus();

        if (DEBUG) {
            debug1.append("NFFGS RESPONSE - " + status + "\n");
            debug1.append("------------------------------------------------\n");
        }

        if (status == 200) {

            //retrieve all nffgs
            FLNffgs nffgs = response.readEntity(FLNffgs.class);
            for (FLNffg nffg : nffgs.getFLNffg()) {
                if (DEBUG) {
                    debug1.append("NAME: " + nffg.getName() + "\n");
                    debug1.append("FLNodes # - " + nffg.getFLNode().size() + "\n");
                    debug1.append("FLLinks # - " + nffg.getFLLink().size() + "\n");
                    //debug1.append("FLPolicies # - " + nffg.getFLPolicy().size() + "\n");
                    debug1.append("------------------------------------------------\n");
                }

                allFLNffgs.put(nffg.getName(), nffg);

                //save policies separately
                for (FLPolicy policy : nffg.getFLPolicy()) {
                    allFLPolicies.put(policy.getName(), policy);
                }
            }
            if (DEBUG) {
                debug1.append("***********************************************\n");
            }

            convertNffgInNffgReaders();
            if (DEBUG) {
                debug1.append("FLNffgs # - " + allFLNffgs.size() + "\n");
                debug1.append("NffgReades # - " + allNffgs.size() + "\n");
                debug1.append("***********************************************\n");
                logFile(debug1.toString(), "VERIFIER_downloaded_Nffgs_");
            }

            convertPoliciesInPolicyReaders();
            if (DEBUG) {
                StringBuilder debug3 = new StringBuilder();
                debug3.append("***********************************************\n");
                debug3.append("FLPolicies #: " + allFLPolicies.size() + "\n");
                debug3.append("PolicyReaders #: " + allPolicies.size() + "\n");
                debug3.append("***********************************************\n");
                logFile(debug3.toString(), "#POLICIES_");
            }
        }
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
        return new LinkedHashSet(allPolicies.values());
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
                if (DEBUG && VERBOSE) {
                    debug1.append("Added: " + node.getId() + " - " + node.getName() + "\n");
                }
                nffgReader.addNameOfNode(node.getId(), node.getName());
                if (DEBUG && VERBOSE) {
                    debug1.append("--node-> " + nodeReader.getName() + ", " + nodeReader.getFuncType() + "\n");
                    debug1.append("NodeREaders # - " + nffgReader.getNodes().size() + "\n");
                }
                nffgReader.addNode(nodeReader);
                if (DEBUG && VERBOSE) {
                    debug1.append("NodeREaders # - " + nffgReader.getNodes().size() + "\n");
                }
            }
            if (DEBUG) {
                debug1.append("NodeReaders # - " + nffgReader.getNodes().size() + "\n");
            }

            int c = 0;
            //LINK
            for (FLLink link : nffg.getFLLink()) {
                if (DEBUG && VERBOSE) {
                    debug1.append("I have node # - " + nffgReader.getNodes().size() + "\n");
                    debug1.append("src - " + link.getSourceNode() + "\n");
                    debug1.append("LINKID - " + link.getId() + "\n");
                }
                FLLinkReader linkReader = new FLLinkReader(link.getName(),
                        nffgReader.getNode(nffgReader.getNamOfNode().get(link.getSourceNode())),
                        nffgReader.getNode(nffgReader.getNamOfNode().get(link.getDestinationNode())));

                nffgReader.getNode(nffgReader.getNamOfNode().get(link.getSourceNode())).addLink(linkReader);
                c++;
            }
            if (DEBUG) {
                debug1.append("LinkReaders # - " + c + "\n");
                debug1.append("------------------------------------------------\n");
            }

            allNffgs.put(nffgReader.getName(), nffgReader);
        }
    }

    private void convertPoliciesInPolicyReaders() {
        String src, dst;

        for (FLPolicy policy : allFLPolicies.values()) {
            StringBuilder debug2 = new StringBuilder();

            if (DEBUG) {
                debug2.append("***********************************************************************\n");
            }
            FLNffgReader nffgReader = retrieveNffgReaderFromName(policy.getNffgName());
            if (DEBUG && VERBOSE) {
                debug2.append("Want nffg: " + policy.getNffgName() + " - returned: " + nffgReader.getName() + "\n");
            }

            src = nffgReader.getNamOfNode().get(policy.getSourceNode());
            dst = nffgReader.getNamOfNode().get(policy.getDestinationNode());

            if (DEBUG && VERBOSE) {
                debug2.append("Found - src: " + src + " - dst: " + dst + "\n");
            }

            FLVerificationResultReader verificationResultReader;

            if (policy.getFLTraversalRequestedNode().size() != 0) {
                Set<FunctionalType> setTraversed = new LinkedHashSet<>();

                for (FLPolicy.FLTraversalRequestedNode traversalRequestedNode : policy.getFLTraversalRequestedNode()) {
                    setTraversed.add(FunctionalType.fromValue(traversalRequestedNode.getFunctionalType().value()));
                }

                // TRAVERSAL
                FLTraversalPolicyReader t = new FLTraversalPolicyReader(policy.getName(),
                        nffgReader,
                        policy.isIsPositive(),
                        nffgReader.getNode(src),
                        nffgReader.getNode(dst),
                        setTraversed);

                if (policy.getFLVResult() != null) {
                    verificationResultReader = new FLVerificationResultReader(t,
                            policy.getFLVResult().isResult(),
                            policy.getFLVResult().getMessage(),
                            policy.getFLVResult().getTime().toGregorianCalendar());
                } else {
                    verificationResultReader = null;
                }

                t.setVerificationResultReader(verificationResultReader);

                allPolicies.put(t.getName(), t);
            } else {
                // REACHABILITY
                FLReachabilityPolicyReader r = new FLReachabilityPolicyReader(policy.getName(),
                        nffgReader,
                        policy.isIsPositive(),
                        nffgReader.getNode(src),
                        nffgReader.getNode(dst));

                if (policy.getFLVResult() != null) {
                    verificationResultReader = new FLVerificationResultReader(r,
                            policy.getFLVResult().isResult(),
                            policy.getFLVResult().getMessage(),
                            policy.getFLVResult().getTime().toGregorianCalendar());
                } else {
                    verificationResultReader = null;
                }

                r.setVerificationResultReader(verificationResultReader);

                allPolicies.put(r.getName(), r);
            }

            /*
            if (DEBUG) {
                debug2.append("Name - FLPolicy: " + policy.getName() + " * PolicyReader: " + y.getName() + "\n");
                debug2.append("NffgNAme - FLPolicy: " + policy.getNffgName() + " * PolicyReader: " + y.getNffg().getName() + "\n");
                debug2.append("IsPositive - FLPolicy: " + policy.isIsPositive() + " * PolicyReader: " + y.isPositive() + "\n");
                debug2.append("srcNode - FLPolicy: " + src + " * PolicyReader: " + y.getSourceNode().getName() + "\n");
                debug2.append("dstNode - FLPolicy: " + dst + " * PolicyReader: " + y.getDestinationNode().getName() + "\n");
            }


            if (DEBUG) {
                debug2.append("ResultPolicy - FLPolicy: " + policy.getFLVResult().getPolicyName() + " * PolicyReader: " + y.getResult().getPolicy().getName() + "\n");
                debug2.append("ResultResult - FLPolicy: " + policy.getFLVResult().isResult() + " * PolicyReader: " + y.getResult().getVerificationResult() + "\n");
                debug2.append("ResultTime - FLPolicy: " + policy.getFLVResult().getTime() + " * PolicyReader: " + y.getResult().getVerificationTime().getTime() + "\n");
                debug2.append("ResultMessage - FLPolicy: " + policy.getFLVResult().getMessage() + " * PolicyReader: " + y.getResult().getVerificationResultMsg() + "\n");
            }


            if (DEBUG) {
                debug2.append("TraversalRequestedNode - FLPolicy: " + policy.getFLTraversalRequestedNode().size() + " * PolicyReader: " + x.getTraversedFuctionalTypes().size() + "\n");
                logFile(debug2.toString(), "DOWNLOADED_" + policy.getName() + "_");
            }
            */
        }
    }

    private FLNffgReader retrieveNffgReaderFromName(String nffgName) {
        for (FLNffgReader nffgReader : allNffgs.values()) {
            if (nffgReader.getName().equals(nffgName)) {
                return nffgReader;
            }
        }
        return null;
    }

    private void logFile(String toWtrite, String name) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());
            File logFile = new File("/Users/FLDeviOS/Desktop/log/Client2/FLNffgVerifier/" + name + counter + "_" + timeLog + ".txt");
            counter++;

            // This will output the full path where the file will be written to...
            //System.out.println(logFile.getCanonicalPath());

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
