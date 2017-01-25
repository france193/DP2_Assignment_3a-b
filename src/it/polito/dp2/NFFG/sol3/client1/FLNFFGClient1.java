package it.polito.dp2.NFFG.sol3.client1;

import it.polito.dp2.NFFG.*;
import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;
import it.polito.dp2.NFFG.lab3.NFFGClient;
import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.lab3.UnknownNameException;
import it.polito.dp2.NFFG.sol3.client1.models.NffgService.*;

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
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by FLDeviOS on 12/01/2017.
 */
public class FLNFFGClient1 implements NFFGClient {

    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private NffgVerifier monitor;
    private WebTarget target;
    private String baseURL;
    private Client client;

    public FLNFFGClient1() throws NffgVerifierException {

        NffgVerifierFactory factory1 = NffgVerifierFactory.newInstance();
        monitor = factory1.newNffgVerifier();

        if ( (baseURL = System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file")) == null) {
            baseURL = DEFAULT_URL;
        }

        baseURL = baseURL + "/resource";

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));
    }

    /**
     * Loads the known NFFG with the given name into the remote service.
     * This operation can fail if an NFFG with the same name already exists in the remote service.
     *
     * @param name the name of the NFFG to be loaded
     * @throws UnknownNameException   if the name passed as argument does not correspond to a locally known NFFG. No alteration of data in the remote service occurs in this case.
     * @throws AlreadyLoadedException if an NFFG with the same name already exists in the remote service.
     * @throws ServiceException       if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadNFFG(String name) throws UnknownNameException, AlreadyLoadedException, ServiceException {
        Response response;

        response = target.path("nffg")
                .request()
                .accept("application/xml")
                .post(Entity.entity(convertNffgReaderToFLNffg(monitor.getNffg(name)), "application/xml"));

        switch (response.getStatus()) {
            case 201:
                break;
            case 400:
                throw new AlreadyLoadedException();
            case 503:
                throw new ServiceException();
            default:
                throw new UnknownNameException();
        }
    }

    /**
     * Loads all the known NFFGs and policies to the remote service (including verification results of policies when available).
     * This operation can fail if an NFFG with the same name as one of the known NFFGs already exists in the remote service.
     * If a policy with the same name as one of the known ones already exists in the remote service, the existing policy has to be overwritten.
     *
     * @throws AlreadyLoadedException if an NFFG with the same name already exists in the remote service.
     * @throws ServiceException       if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadAll() throws AlreadyLoadedException, ServiceException {
        Response response;
        HashMap<String, FLNffg> nffgName_Nffg = new HashMap<>();
        FLNffgs nffgs = new FLNffgs();

        StringBuilder debug = new StringBuilder();

        for (NffgReader nffg : monitor.getNffgs()) {
            FLNffg nffg1 = convertNffgReaderToFLNffg(nffg);

            debug.append("\n***********************************************\n");
            debug.append("NFFG\n");
            debug.append("OLD name: " + nffg.getName() + " - NEW id: " + nffg1.getId() + " name: " + nffg1.getName() + "\n");

            debug.append("\n-----------------------------------------------\n");
            for (FLNode node : nffg1.getFLNode()) {
                debug.append("NODES\n");
                debug.append("NEW id: " + node.getId() + " name: " + node.getName() + " functionalType: " + node.getFunctionalType() + "\n");
            }
            debug.append("\n-----------------------------------------------\n");
            for (FLLink link : nffg1.getFLLink()) {
                debug.append("LINKS\n");
                debug.append("NEW id: " + link.getId() + " name: " + link.getName() + " srcNode: " + link.getSourceNode() + " dstNode: " + link.getDestinationNode() + "\n");
            }
            debug.append("\n-----------------------------------------------\n");
            for (FLPolicy policy : nffg1.getFLPolicy()) {
                debug.append("POLICIES\n");
                debug.append("NEW name: " + policy.getName() + " nffgName: " + policy.getNffgName() + " srcNode: " + policy.getSourceNode() + " dstNode: " + policy.getDestinationNode() + "\n");
            }
            debug.append("\n***********************************************\n");

            nffgs.getFLNffg().add(nffg1);
        }

        debug.append("I have: " +
                nffgs.getFLNffg().size() + " nffgs ready to load"  + "\n");

        logFile(debug.toString());

        response = target.path("nffgs")
                .request()
                .accept("application/xml")
                .post(Entity.entity(nffgs, "application/xml"));

        switch (response.getStatus()) {
            case 201:
                for (FLNffg nffg1 : response.readEntity(FLNffgs.class).getFLNffg()) {
                    nffgName_Nffg.put(nffg1.getName(), nffg1);
                }

                FLPolicies policies = new FLPolicies();
                for (PolicyReader policy : monitor.getPolicies()) {
                    FLPolicy policy1 = convertPolicyReaderToFLPolicy(policy);

                    policies.getFLPolicy().add(policy1);
                }

                response = target.path("policies")
                        .request()
                        .accept("application/xml")
                        .post(Entity.entity(policies, "application/xml"));

                switch (response.getStatus()) {
                    case 201:
                        break;
                    case 400:
                        throw new AlreadyLoadedException();
                    default:
                        throw new ServiceException();
                }
                break;
            case 400:
                throw new AlreadyLoadedException();
            case 503:
                throw new ServiceException();
            default:
                System.out.println("ERROR: " + response.getStatus());
                break;
        }
    }

    /**
     * Loads a new reachability policy into the remote service given the policy properties.
     * If a policy with the given name already exists in the service, the new policy substitutes
     * the old one. The new policy is uploaded without a verification result.
     *
     * @param name        the name to be given to the new policy
     * @param nffgName    the name of a known NFFG the new policy refers to
     * @param isPositive  true if the new policy is positive
     * @param srcNodeName the name of the source node of the new policy
     * @param dstNodeName the name of the destination node of the new policy
     * @throws UnknownNameException if nffgName is not the name of a known NFFG, or srcNodeName and dstNodeName are not both nodes belonging to the known NFFG named nffgName.
     * @throws ServiceException     if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadReachabilityPolicy(String name, String nffgName, boolean isPositive, String srcNodeName, String dstNodeName) throws UnknownNameException, ServiceException {
        Response response;
        FLPolicy policy = new FLPolicy();

        policy.setName(name);
        policy.setNffgName(nffgName);
        policy.setIsPositive(isPositive);
        policy.setSourceNode(srcNodeName);
        policy.setDestinationNode(dstNodeName);

        response = target.path("policy")
                .request()
                .accept("application/xml")
                .post(Entity.entity(policy, "application/xml"));

        switch (response.getStatus()) {
            case 201:
                break;
            case 503:
                throw new ServiceException();
            default:
                throw new UnknownNameException();
        }
    }

    /**
     * Unloads the reachability policy with a given name from the remote service
     *
     * @param name the name of the reachability policy to be unloaded
     * @throws UnknownNameException if the policy name passed as argument does not correspond to a reachability policy already loaded in the remote service
     * @throws ServiceException     if any other error occurs when trying to unload the policy
     */
    @Override
    public void unloadReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
        Response response;

        response = target.path("policy")
                .path(name)
                .request()
                .accept("application/xml")
                .delete();

        switch (response.getStatus()) {
            case 200:
                break;
            case 503:
                throw new ServiceException();
            default:
                throw new UnknownNameException();
        }
    }

    /**
     * Asks the service to test one of the previously uploaded reachability policies
     *
     * @param name the name of the reachability policy to be tested
     * @throws UnknownNameException if the policy name passed as argument does not correspond to a reachability policy already loaded in the remote service
     * @throws ServiceException     if any other error occurs when trying to test reachability
     * @return the result of the verification of the policy
     */
    @Override
    public boolean testReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
        Response response;
        FLVResult flvResult = new FLVResult();

        flvResult.setPolicyName(name);

        response = target.path("verifyPolicy")
                .request()
                .accept("application/xml")
                .post(Entity.entity(flvResult, "application/xml"));

        switch (response.getStatus()) {
            case 200:
                if (response.readEntity(FLVResult.class).isResult()) {
                    return true;
                } else {
                    return false;
                }
            case 503:
                throw new ServiceException();
            default:
                throw new UnknownNameException();
        }
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }

    private XMLGregorianCalendar getXMLCal(Calendar calendar) {
        GregorianCalendar cal = (GregorianCalendar) calendar;
        XMLGregorianCalendar xmlCal = null;

        try {
            xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return xmlCal;
    }

    private FLNffg convertNffgReaderToFLNffg (NffgReader nffg) {
        FLNffg nffg1 = new FLNffg();

        nffg1.setName(nffg.getName());
        nffg1.setLastUpdatedTime(getXMLCal(nffg.getUpdateTime()));

        for (NodeReader node : nffg.getNodes()) {
            FLNode node1 = new FLNode();

            node1.setName(node.getName());
            node1.setFunctionalType(NodeFunctionalType.valueOf(node.getFuncType().value()));

            nffg1.getFLNode().add(node1);

            for (LinkReader link : node.getLinks()) {
                FLLink link1 = new FLLink();

                link1.setName(link.getName());
                link1.setSourceNode(link.getSourceNode().getName());
                link1.setDestinationNode(link.getDestinationNode().getName());

                nffg1.getFLLink().add(link1);
            }
        }

        return nffg1;
    }

    private FLPolicy convertPolicyReaderToFLPolicy(PolicyReader p) {
        FLPolicy policy1 = new FLPolicy();

        if ( !(p instanceof  TraversalPolicyReader) ) {
            ReachabilityPolicyReader policy = (ReachabilityPolicyReader) p;

            policy1.setName(policy.getName());
            policy1.setNffgName(policy.getNffg().getName());

            policy1.setSourceNode(policy.getSourceNode().getName());
            policy1.setDestinationNode(policy.getDestinationNode().getName());

            if (policy.getResult() != null) {
                policy1.setFLVResult(readVerificationResult(policy.getResult()));
            }

            policy1.setIsPositive(policy.isPositive());
        } else {
            TraversalPolicyReader policy = (TraversalPolicyReader) p;

            policy1.setName(policy.getName());
            policy1.setNffgName(policy.getNffg().getName());

            policy1.setSourceNode(policy.getSourceNode().getName());
            policy1.setDestinationNode(policy.getDestinationNode().getName());

            if (policy.getResult() != null) {
                policy1.setFLVResult(readVerificationResult(policy.getResult()));
            }

            if (policy.getTraversedFuctionalTypes().size() > 0) {
                for (FunctionalType functionalType : policy.getTraversedFuctionalTypes()) {
                    FLPolicy.FLTraversalRequestedNode traversalRequestedNode = new FLPolicy.FLTraversalRequestedNode();
                    traversalRequestedNode.setFunctionalType(NodeFunctionalType.valueOf(functionalType.value()));
                    policy1.getFLTraversalRequestedNode().add(traversalRequestedNode);
                }
            }
        }

        return policy1;
    }

    private FLVResult readVerificationResult(VerificationResultReader result) {
        FLVResult flvResult = new FLVResult();

        flvResult.setMessage(result.getVerificationResultMsg());
        flvResult.setPolicyName(result.getPolicy().getName());
        flvResult.setTime(getXMLCal(result.getVerificationTime()));
        flvResult.setResult(result.getVerificationResult());

        return  flvResult;
    }

    public void logFile(String toWtrite) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File("/Users/FLDeviOS/Desktop/log/Client1/FLNFFGClient1/" + "FLNFFGClient1_" + timeLog + ".txt");

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
