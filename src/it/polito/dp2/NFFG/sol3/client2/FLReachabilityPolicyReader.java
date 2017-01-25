package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLReachabilityPolicyReader extends FLPolicyReader implements ReachabilityPolicyReader {
    /**
     * Class' attributes
     */
    private NodeReader nodeSource;
    private NodeReader nodeDestination;

    /**
     * Class' constructor
     *
     * @param nffg_refer
     * @param isPositive
     * @param nodeSource
     * @param destination
     */
    FLReachabilityPolicyReader(String policy_name_id,
                               NffgReader nffg_refer,
                               boolean isPositive,
                               NodeReader nodeSource,
                               NodeReader destination) {

        super(policy_name_id, nffg_refer, isPositive);
        this.nodeSource = nodeSource;
        this.nodeDestination = destination;
    }

    /**
     * Gives the nodeSourceNode node of this policy.
     *
     * @return
     */
    @Override
    public NodeReader getSourceNode() {
        return this.nodeSource;
    }

    /**
     * Gives the destination node of this policy.
     *
     * @return
     */
    @Override
    public NodeReader getDestinationNode() {
        return this.nodeDestination;
    }

    /**
     * Method that assign a VerificationResult element to the policy (if it is verified)
     *
     * @param verificationResult
     */
    public void setVerificationResult(FLVerificationResultReader verificationResult) {
        super.setVerificationResult(verificationResult);
    }
}
