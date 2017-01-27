package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.*;

import java.util.Set;

/**
 * Created by Francesco Longo (223428) on 27/01/2017.
 */
public class FLTraversalPolicyReader extends FLReachabilityPolicyReader implements TraversalPolicyReader {
    private Set<FunctionalType> traversedFunctionalType;

    /**
     *
     * @param entityName
     * @param nffgReader
     * @param positive
     * @param sourceNode
     * @param destinationNode
     * @param traversedFunctionalType
     */
    public FLTraversalPolicyReader(String entityName,
                                   NffgReader nffgReader,
                                   Boolean positive,
                                   NodeReader sourceNode,
                                   NodeReader destinationNode,
                                   Set<FunctionalType> traversedFunctionalType) {
        super(entityName, nffgReader, positive, sourceNode, destinationNode);
        this.traversedFunctionalType = traversedFunctionalType;
    }

    @Override
    public Set<FunctionalType> getTraversedFuctionalTypes() {
        return traversedFunctionalType;
    }

    public void setVerificationResultReader(VerificationResultReader verificationResultReader) {
        super.setVerificationResultReader(verificationResultReader);
    }
}
