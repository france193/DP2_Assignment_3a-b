package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

/**
 * Created by FLDeviOS on 27/01/2017.
 */
public class FLReachabilityPolicyReader extends FLPolicyReader implements ReachabilityPolicyReader {
    private NodeReader sourceNode;
    private NodeReader destinationNode;

    /**
     *
     * @param entityName
     * @param nffgReader
     * @param positive
     * @param sourceNode
     * @param destinationNode
     */
    public FLReachabilityPolicyReader(String entityName,
                                      NffgReader nffgReader,
                                      Boolean positive,
                                      NodeReader sourceNode,
                                      NodeReader destinationNode) {
        super(entityName, nffgReader, positive);
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    @Override
    public NodeReader getSourceNode() {
        return sourceNode;
    }

    @Override
    public NodeReader getDestinationNode() {
        return destinationNode;
    }

    public void setVerificationResultReader(VerificationResultReader verificationResultReader) {
        super.setVerificationResultReader(verificationResultReader);
    }
}
