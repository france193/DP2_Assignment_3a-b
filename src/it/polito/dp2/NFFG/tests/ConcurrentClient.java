package it.polito.dp2.NFFG.tests;

import it.polito.dp2.NFFG.sol3.client1.models.NffgService.FLPolicies;
import it.polito.dp2.NFFG.sol3.client1.models.NffgService.FLPolicy;
import it.polito.dp2.NFFG.sol3.client1.models.NffgService.FLVResult;
import it.polito.dp2.NFFG.sol3.client1.models.NffgService.FLVResults;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLNffg;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLNffgs;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLNode;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLNodeFunctionalType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class ConcurrentClient extends Thread {
    private int id;

    private Random rand = new Random();

    private int max = 5000;
    private int min = 200;

    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private WebTarget target;
    private String baseURL;
    private Client client;

    public ConcurrentClient(int i) {
        if ((baseURL = System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file")) == null) {
            baseURL = DEFAULT_URL;
        }

        baseURL = baseURL + "/resource";

        // create a new client
        client = ClientBuilder.newClient();

        // create a webtarget from the baseURL string
        target = client.target(getBaseURI(baseURL));

        this.id = i;
    }

    public void run() {
        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (loadNffg() != 201) {
            return;
        }

        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (loadPolicy() != 201) {
            return;
        }

        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (verifyPolicy() != 200) {
            return;
        }

        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (updatePolicy() != 200) {
            return;
        }

        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (verifyPolicy() != 200) {
            return;
        }

        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (deletePolicy() != 204) {
            return;
        }

        try {
            Thread.sleep(rand.nextInt(min) + max);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (sleep)");
            return;
        }

        if (verifyPolicy() != 200) {
            return;
        }

        System.out.println("*** CLIENT_" + id + " - TEST CORRECTLY FINISHED ***");
        return;
    }

    private int deletePolicy() {
        Response response;

        response = target.path("policy").path("Policy" + id).request().delete();

        switch (response.getStatus()) {
            case 204:
                // SUCCESS
                System.out.println("--> CLIENT_" + id + " - POLICY DELETED");
                return 204;
            case 404:
                System.out.println(" (!) --> CLIENT_" + id + " - POLICY NOT FOUND!");
                return 404;
            default:
            case 503:
                System.out.println(" (!) --> CLIENT_" + id + " - ERROR DELETING POLICY (" + response.getStatus() + ")! ");
                return 503;
        }
    }

    private int updatePolicy() {
        Response response;
        FLPolicy policy = new FLPolicy();
        FLPolicies policies = new FLPolicies();

        policy.setName("Policy" + id);
        policy.setNffgName("Nffg" + id);
        policy.setIsPositive(false);
        policy.setSourceNode("Node0");
        policy.setDestinationNode("Node1");

        policies.getFLPolicy().add(policy);

        response = target.path("policies").request().accept("application/xml")
                .put(Entity.entity(policies, "application/xml"));

        switch (response.getStatus()) {
            case 200:
                // SUCCESS
                System.out.println("--> CLIENT_" + id + " - UPDATED POLICY");
                return 200;
            default:
            case 503:
                System.out.println(" (!) --> CLIENT_" + id + " - FAILED UPDATING POLICY (" + response.getStatus() + ")! - ServiceException");
                return 503;
        }
    }

    private int loadNffg() {
        Response response;
        XMLGregorianCalendar date2 = null;
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        try {
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e1) {
            e1.printStackTrace();
            System.out.println(" (!) --> CLIENT_" + id + " - FAILED TEST! (DatatypeConfigurationException)");
            return 0;
        }

        FLNffgs nffgs = new FLNffgs();
        FLNffg nffg = new FLNffg();
        FLNode node0 = new FLNode();
        FLNode node1 = new FLNode();
        FLNode node2 = new FLNode();
        FLNode.FLLink link0 = new FLNode.FLLink();
        FLNode.FLLink link1 = new FLNode.FLLink();

        nffg.setName("Nffg" + id);
        nffg.setLastUpdatedTime(date2);

        node0.setName("Node0");
        node0.setFunctionalType(FLNodeFunctionalType.NAT);
        node1.setName("Node1");
        node1.setFunctionalType(FLNodeFunctionalType.DPI);
        node2.setName("Node2");
        node2.setFunctionalType(FLNodeFunctionalType.CACHE);

        link0.setName("Link0");
        link0.setDestinationNode("Node1");

        link1.setName("Link1");
        link1.setDestinationNode("Node2");

        node0.getFLLink().add(link0);
        if (rand.nextInt(10) % 2 == 0) {
            node1.getFLLink().add(link1);
            System.out.println("--> CLIENT_" + id + " - POLICY MUST BE TRUE");
        } else {
            System.out.println("--> CLIENT_" + id + " - POLICY MUST BE FALSE");
        }
        nffg.getFLNode().add(node0);
        nffg.getFLNode().add(node1);
        nffg.getFLNode().add(node2);
        nffgs.getFLNffg().add(nffg);

        response = target.path("nffgs").request().accept("application/xml")
                .post(Entity.entity(nffgs, "application/xml"));

        switch (response.getStatus()) {
            case 201:
                // SUCCESS
                System.out.println("--> CLIENT_" + id + " - INSERTED NFFG");
                return 201;
            case 409:
                System.out.println(" (!) --> CLIENT_" + id + " - FAILED INSERTING NFFG! - AlreadyLoadedException");
                return 409;
            case 400:
            default:
            case 503:
                System.out.println(" (!) --> CLIENT_" + id + " - FAILED INSERTING NFFG (" + response.getStatus() + ")! - ServiceException");
                return 0;
        }
    }

    private int loadPolicy() {
        Response response;
        FLPolicy policy = new FLPolicy();
        FLPolicies policies = new FLPolicies();

        policy.setName("Policy" + id);
        policy.setNffgName("Nffg" + id);
        policy.setIsPositive(true);
        policy.setSourceNode("Node0");
        policy.setDestinationNode("Node2");

        policies.getFLPolicy().add(policy);

        response = target.path("policies").request().accept("application/xml")
                .post(Entity.entity(policies, "application/xml"));

        switch (response.getStatus()) {
            case 201:
                // SUCCESS
                System.out.println("--> CLIENT_" + id + " - INSERTED POLICY");
                return 201;
            default:
            case 503:
                System.out.println(" (!) --> CLIENT_" + id + " - FAILED INSERTING POLICY (" + response.getStatus() + ")! - ServiceException");
                return 503;
        }
    }

    private int verifyPolicy() {
        Response response;
        FLVResults flvResults = new FLVResults();
        FLVResult flvResult = new FLVResult();

        flvResult.setPolicy("Policy" + id);
        flvResults.getFLVResult().add(flvResult);

        response = target.path("verifyPolicies").request().accept("application/xml")
                .post(Entity.entity(flvResults, "application/xml"));

        switch (response.getStatus()) {
            case 200:
                flvResults = response.readEntity(FLVResults.class);
                if (flvResults.getFLVResult().size() == 0) {
                    System.out.println("--> CLIENT_" + id + " - POLICY NOT FOUND");
                    return 200;
                } else {
                    // i have a 1 result
                    flvResult = flvResults.getFLVResult().get(0);
                    if (flvResult.isResult()) {
                        System.out.println("--> CLIENT_" + id + " - POLICY IS TRUE");
                        return 200;
                    } else {
                        System.out.println("--> CLIENT_" + id + " - POLICY IS FALSE");
                        return 200;
                    }
                }
            default:
            case 503:
                System.out.println(" (!) --> CLIENT_" + id + " - FAILED VERIFYING POLICY (" + response.getStatus() + ")! - ServiceException");
                return 503;
        }
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }
}
