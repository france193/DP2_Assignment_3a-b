package it.polito.dp2.NFFG.sol3.client1;

import it.polito.dp2.NFFG.lab3.NFFGClient;
import it.polito.dp2.NFFG.lab3.NFFGClientException;
import it.polito.dp2.NFFG.lab3.NFFGClientFactory;

/**
 * Created by FLDeviOS on 12/01/2017.
 */
public class FLNFFGClientFactory extends NFFGClientFactory {
    /**
     * Creates a new instance of a {@link NFFGClient} implementation.
     *
     * @return a new instance of a {@link NFFGClient} implementation.
     * @throws NFFGClientException if an implementation of {@link NFFGClient} cannot be created.
     */
    @Override
    public NFFGClient newNFFGClient() throws NFFGClientException {
        FLNFFGClient myFLNffgClient = null;

        try {
            myFLNffgClient = new FLNFFGClient();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException Error: " + e.getMessage());
            e.printStackTrace();
        }

        return myFLNffgClient;
    }
}
