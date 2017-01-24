package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.sol3.client2.models.NffgService.FLPolicy;
import it.polito.dp2.NFFG.sol3.client2.models.NffgService.FLNffg;
import it.polito.dp2.NFFG.sol3.client2.models.NffgService.FLNffgs;
import it.polito.dp2.NFFG.sol3.client2.models.NffgService.FLPolicies;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLNffgVerifier implements NffgVerifier {

    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private NffgVerifier monitor;
    private WebTarget target;
    private String baseURL;
    private Client client;

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> allNffgs;
    private HashMap<String, PolicyReader> allPolicies;
    private HashMap<String, FLNffg> allFLNffgs;
    private HashMap<String, FLPolicy> allFLPolicies;


    public FLNffgVerifier() {
        Response response;

        allNffgs = new HashMap<>();
        allPolicies = new HashMap<>();
        allFLNffgs = new HashMap<>();
        allFLPolicies = new HashMap<>();

        if ( (baseURL = System.getProperty("it.polito.dp2.NFFG.lab3.URL")) == null) {
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
            case 503:
                System.exit(503);
            default:
                System.exit(000);
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
            case 503:
                System.exit(503);
            default:
                System.exit(000);
        }
    }

    @Override
    public Set<NffgReader> getNffgs() {
        return null;
    }

    @Override
    public NffgReader getNffg(String s) {
        return null;
    }

    @Override
    public Set<PolicyReader> getPolicies() {
        return null;
    }

    @Override
    public Set<PolicyReader> getPolicies(String s) {
        return null;
    }

    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        return null;
    }

    private URI getBaseURI(String url) {
        return UriBuilder.fromUri(url).build();
    }
}
