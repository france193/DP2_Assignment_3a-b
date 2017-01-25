package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLTraversalPolicyReader extends FLReachabilityPolicyReader implements TraversalPolicyReader {
    private List<FunctionalType> listOfRequiredTraversedNode;

    /**
     * Class' constructor
     *
     * @param policy_name_id
     * @param nffg_refer
     * @param isPositive
     * @param nodeSource
     * @param nodeDestination
     * @param listOfRequiredTraversedNode
     */
    FLTraversalPolicyReader(String policy_name_id,
                            NffgReader nffg_refer,
                            boolean isPositive,
                            NodeReader nodeSource,
                            NodeReader nodeDestination,
                            List<FunctionalType> listOfRequiredTraversedNode) {

        super(policy_name_id, nffg_refer, isPositive, nodeSource, nodeDestination);

        this.listOfRequiredTraversedNode = new ArrayList<>();
        this.listOfRequiredTraversedNode = listOfRequiredTraversedNode;
    }

    /**
     * Gives the set of network functionalities that must be traversed for the property of this policy to hold.
     *
     * @return
     */
    @Override
    public Set<FunctionalType> getTraversedFuctionalTypes() {
        return new LinkedHashSet(this.listOfRequiredTraversedNode);
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
