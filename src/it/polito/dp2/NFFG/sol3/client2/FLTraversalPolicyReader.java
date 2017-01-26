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
     */
    FLTraversalPolicyReader(String policy_name_id,
                            NffgReader nffg_refer,
                            boolean isPositive,
                            NodeReader nodeSource,
                            NodeReader nodeDestination) {

        super(policy_name_id, nffg_refer, isPositive, nodeSource, nodeDestination);

        this.listOfRequiredTraversedNode = new ArrayList<>();
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

    public void addTraversedFuctionalTypes(FunctionalType t) {
        listOfRequiredTraversedNode.add(t);
    }
}
