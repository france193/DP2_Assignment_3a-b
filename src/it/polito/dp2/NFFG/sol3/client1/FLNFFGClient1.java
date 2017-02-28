package it.polito.dp2.NFFG.sol3.client1;

import it.polito.dp2.NFFG.*;
import it.polito.dp2.NFFG.lab3.*;
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
import java.util.Set;

/**
 * Created by Francesco Longo (223428) on 21/02/2017.
 */
public class FLNffgClient1 implements NFFGClient {

    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private NffgVerifier monitor;
    private WebTarget target;
    private String baseURL;
    private Client client;
    private int counter = 0;

    public FLNffgClient1() throws NFFGClientException {

        NffgVerifierFactory factory1 = NffgVerifierFactory.newInstance();
        try {
            monitor = factory1.newNffgVerifier();
        } catch (NffgVerifierException e) {
            throw new NFFGClientException("(!) - Cannot create client1");
        }

        if ((baseURL = System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file")) == null) {
            baseURL = DEFAULT_URL;
        }

        baseURL = baseURL + "/resource";

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));
    }

    /**
     * Loads the known NFFG with the given name into the remote service. This
     * operation can fail if an NFFG with the same name already exists in the
     * remote service.
     *
     * @param name the name of the NFFG to be loaded
     * @throws UnknownNameException   if the name passed as argument does not correspond to a
     *                                locally known NFFG. No alteration of data in the remote
     *                                service occurs in this case.
     * @throws AlreadyLoadedException if an NFFG with the same name already exists in the remote
     *                                service.
     * @throws ServiceException       if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadNFFG(String name) throws UnknownNameException, AlreadyLoadedException, ServiceException {
        Response response;

        NffgReader nffgReader = monitor.getNffg(name);

        if (nffgReader == null) {
            throw new UnknownNameException();
        }

        FLNffgs nffgs = new FLNffgs();
        nffgs.getFLNffg().add(convertNffgReaderToFLNffg(nffgReader));

        response = target.path("nffgs").request().accept("application/xml")
                .post(Entity.entity(nffgs, "application/xml"));

        switch (response.getStatus()) {
            case 201:
                // SUCCESS
                break;
            case 409:
                throw new AlreadyLoadedException();
            case 400:
            default:
            case 503:
                throw new ServiceException();
        }
    }

    /**
     * Loads all the known NFFGs and policies to the remote service (including
     * verification results of policies when available). This operation can fail
     * if an NFFG with the same name as one of the known NFFGs already exists in
     * the remote service. If a policy with the same name as one of the known
     * ones already exists in the remote service, the existing policy has to be
     * overwritten.
     *
     * @throws AlreadyLoadedException if an NFFG with the same name already exists in the remote
     *                                service.
     * @throws ServiceException       if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadAll() throws AlreadyLoadedException, ServiceException {
        Response response;
        HashMap<String, FLNffg> nffgName_Nffg = new HashMap<>();
        FLNffgs nffgs = new FLNffgs();

        for (NffgReader nffg : monitor.getNffgs()) {
            nffgs.getFLNffg().add(convertNffgReaderToFLNffg(nffg));
        }

        response = target.path("nffgs").request().accept("application/xml")
                .post(Entity.entity(nffgs, "application/xml"));

        if (response.getStatus() != 201) {
            switch (response.getStatus()) {
                case 409:
                    throw new AlreadyLoadedException();
                default:
                case 400:
                case 503:
                    throw new ServiceException(
                            "status code: " + response.getStatus() + "--> " + response.readEntity(String.class));
            }
        }

        for (FLNffg nffg1 : response.readEntity(FLNffgs.class).getFLNffg()) {
            nffgName_Nffg.put(nffg1.getName(), nffg1);
        }

        FLPolicies allPolicies = convertPolicyReadersToFLPolicies(monitor.getPolicies());

        response = target.path("policies").request().accept("application/xml")
                .post(Entity.entity(allPolicies, "application/xml"));

        if (response.getStatus() != 201) {
            throw new ServiceException();
        }
    }

    /**
     * Loads a new reachability policy into the remote service given the policy
     * properties. If a policy with the given name already exists in the
     * service, the new policy substitutes the old one. The new policy is
     * uploaded without a verification result.
     *
     * @param name        the name to be given to the new policy
     * @param nffgName    the name of a known NFFG the new policy refers to
     * @param isPositive  true if the new policy is positive
     * @param srcNodeName the name of the source node of the new policy
     * @param dstNodeName the name of the destination node of the new policy
     * @throws UnknownNameException if nffgName is not the name of a known NFFG, or srcNodeName
     *                              and dstNodeName are not both nodes belonging to the known
     *                              NFFG named nffgName.
     * @throws ServiceException     if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadReachabilityPolicy(String name, String nffgName, boolean isPositive, String srcNodeName,
                                       String dstNodeName) throws UnknownNameException, ServiceException {
        Response response;
        FLPolicy policy = new FLPolicy();
        FLPolicies policies = new FLPolicies();

        NffgReader nffgReader = monitor.getNffg(nffgName);

        if (nffgReader == null) {
            throw new UnknownNameException();
        }

        if (nffgReader.getNode(srcNodeName) == null) {
            throw new UnknownNameException();
        }

        if (nffgReader.getNode(dstNodeName) == null) {
            throw new UnknownNameException();
        }

        policy.setName(name);
        policy.setNffgName(nffgName);
        policy.setIsPositive(isPositive);
        policy.setSourceNode(srcNodeName);
        policy.setDestinationNode(dstNodeName);

        policies.getFLPolicy().add(policy);

        response = target.path("policies").request().accept("application/xml")
                .post(Entity.entity(policies, "application/xml"));

        switch (response.getStatus()) {
            case 201:
                // SUCCESS
                break;
            default:
            case 503:
                throw new ServiceException();
        }
    }

    /**
     * Unloads the reachability policy with a given name from the remote service
     *
     * @param name the name of the reachability policy to be unloaded
     * @throws UnknownNameException if the policy name passed as argument does not correspond to
     *                              a reachability policy already loaded in the remote service
     * @throws ServiceException     if any other error occurs when trying to unload the policy
     */
    @Override
    public void unloadReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
        Response response;

        response = target.path("policy").path(name).request().accept("application/xml").delete();

        switch (response.getStatus()) {
            case 204:
                break;
            case 404:
                throw new UnknownNameException("policy not found on service");
            default:
            case 503:
                throw new ServiceException();
        }
    }

    /**
     * Asks the service to test one of the previously uploaded reachability
     * policies
     *
     * @param name the name of the reachability policy to be tested
     * @return the result of the verification of the policy
     * @throws UnknownNameException if the policy name passed as argument does not correspond to
     *                              a reachability policy already loaded in the remote service
     * @throws ServiceException     if any other error occurs when trying to test reachability
     */
    @Override
    public boolean testReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
        Response response;
        FLVResults flvResults = new FLVResults();
        FLVResult flvResult = new FLVResult();

        flvResult.setPolicy(name);
        flvResults.getFLVResult().add(flvResult);

        response = target.path("verifyPolicies").request().accept("application/xml")
                .post(Entity.entity(flvResults, "application/xml"));

        switch (response.getStatus()) {
            case 200:
                flvResults = response.readEntity(FLVResults.class);
                if (flvResults.getFLVResult().size() == 0) {
                    // Policy not found
                    throw new UnknownNameException();
                } else {
                    // i have a 1 result
                    flvResult = flvResults.getFLVResult().get(0);
                    if (flvResult.isResult()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            default:
            case 503:
                throw new ServiceException();
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

    private FLNffg convertNffgReaderToFLNffg(NffgReader nffgReader) {
        FLNffg nffg = new FLNffg();

        nffg.setName(nffgReader.getName());
        nffg.setLastUpdatedTime(getXMLCal(nffgReader.getUpdateTime()));

        for (NodeReader node : nffgReader.getNodes()) {
            FLNode node1 = new FLNode();

            node1.setName(node.getName());
            node1.setFunctionalType(FLNodeFunctionalType.valueOf(node.getFuncType().value()));

            nffg.getFLNode().add(node1);

            for (LinkReader linkReader : node.getLinks()) {
                FLNode.FLLink link = new FLNode.FLLink();

                link.setName(linkReader.getName());
                link.setDestinationNode(linkReader.getDestinationNode().getName());

                node1.getFLLink().add(link);
            }
        }

        return nffg;
    }

    private FLPolicies convertPolicyReadersToFLPolicies(Set<PolicyReader> policyReaders) {
        FLPolicies policies = new FLPolicies();

        for (PolicyReader p : policyReaders) {
            FLPolicy policy1 = new FLPolicy();

            policy1.setName(p.getName());
            policy1.setNffgName(p.getNffg().getName());
            if (p.getResult() != null) {
                FLVResult flvResult = new FLVResult();

                flvResult.setMessage(p.getResult().getVerificationResultMsg());
                flvResult.setPolicy(p.getResult().getPolicy().getName());
                flvResult.setTime(getXMLCal(p.getResult().getVerificationTime()));
                flvResult.setResult(p.getResult().getVerificationResult());

                policies.getFLVResult().add(flvResult);
            }
            policy1.setIsPositive(p.isPositive());

            if (!(p instanceof TraversalPolicyReader)) {
                ReachabilityPolicyReader policyre = (ReachabilityPolicyReader) p;

                policy1.setSourceNode(policyre.getSourceNode().getName());
                policy1.setDestinationNode(policyre.getDestinationNode().getName());

            } else {
                TraversalPolicyReader policyt = (TraversalPolicyReader) p;

                policy1.setSourceNode(policyt.getSourceNode().getName());
                policy1.setDestinationNode(policyt.getDestinationNode().getName());

                if (policyt.getTraversedFuctionalTypes().size() != 0) {
                    for (FunctionalType functionalType : policyt.getTraversedFuctionalTypes()) {
                        String t = FLNodeFunctionalType.valueOf(functionalType.value()).toString();
                        policy1.getTraversedNode().add(t);
                    }
                }
            }
            policies.getFLPolicy().add(policy1);
        }

        return policies;
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
