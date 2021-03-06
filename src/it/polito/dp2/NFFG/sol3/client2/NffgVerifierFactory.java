package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;

/**
 * Created by Francesco Longo (223428) on 21/02/2017.
 */
public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory {
    /**
     * Void constructor
     */
    public NffgVerifierFactory() {
    }

    /**
     * @return
     * @throws NffgVerifierException
     */
    @Override
    public NffgVerifier newNffgVerifier() throws NffgVerifierException {
        NffgVerifier myNffgVerifier;

        try {
            myNffgVerifier = new FLNffgVerifier();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException Error: " + e.getMessage());
            e.printStackTrace();
            throw new NullPointerException();
        }

        return myNffgVerifier;
    }
}
