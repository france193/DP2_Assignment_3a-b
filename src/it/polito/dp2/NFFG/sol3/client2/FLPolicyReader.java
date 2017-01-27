package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

/**
 * Created by Francesco Longo (223428) on 27/01/2017.
 */
public class FLPolicyReader extends FLNamedEntityReader implements PolicyReader {
    private NffgReader nffgReader;
    private VerificationResultReader verificationResultReader;
    private Boolean positive;

    /**
     *
     * @param entityName
     * @param nffgReader
     * @param positive
     */
    public FLPolicyReader(String entityName,
                          NffgReader nffgReader,
                          Boolean positive) {
        super(entityName);
        this.nffgReader = nffgReader;
        this.positive = positive;
    }

    @Override
    public NffgReader getNffg() {
        return nffgReader;
    }

    @Override
    public VerificationResultReader getResult() {
        return verificationResultReader;
    }

    @Override
    public Boolean isPositive() {
        return positive;
    }

    public void setVerificationResultReader(VerificationResultReader verificationResultReader) {
        this.verificationResultReader = verificationResultReader;
    }
}
