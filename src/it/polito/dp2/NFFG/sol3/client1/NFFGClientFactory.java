package it.polito.dp2.NFFG.sol3.client1;

import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.lab3.NFFGClient;
import it.polito.dp2.NFFG.lab3.NFFGClientException;

/**
 * Created by Francesco Longo (223428) on 12/01/2017.
 */
public class NFFGClientFactory extends it.polito.dp2.NFFG.lab3.NFFGClientFactory {

    /**
     * Creates a new instance of a {@link NFFGClient} implementation.
     *
     * @return a new instance of a {@link NFFGClient} implementation.
     * @throws NFFGClientException if an implementation of {@link NFFGClient} cannot be created.
     */
    @Override
    public NFFGClient newNFFGClient() throws NFFGClientException {
        NFFGClient myFLNffgClient1 = null;

        try {
            myFLNffgClient1 = new FLNFFGClient1();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException Error: " + e.getMessage());
            e.printStackTrace();
        } catch (NffgVerifierException e) {
            System.err.println("NffgVerifierException Error: " + e.getMessage());
            e.printStackTrace();
        }

        return myFLNffgClient1;
    }
}
