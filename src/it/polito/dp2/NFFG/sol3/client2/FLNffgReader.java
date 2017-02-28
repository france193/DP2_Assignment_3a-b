package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Francesco Longo (223428) on 21/02/2017.
 */
public class FLNffgReader extends FLNamedEntityReader implements NffgReader {
    private HashMap<String, FLPolicyReader> policies;
    private HashMap<String, FLNodeReader> nodes;
    private HashMap<String, String> namOfNode;
    private Calendar last_updated_time;

    /**
     * Class' constructor
     *
     * @param nffg_name_id
     * @param last_updated_time
     */
    public FLNffgReader(String nffg_name_id, Calendar last_updated_time) {
        super(nffg_name_id);
        this.last_updated_time = last_updated_time;

        nodes = new HashMap<>();
        policies = new HashMap<>();
        namOfNode = new HashMap<>();
    }

    public HashMap<String, String> getNamOfNode() {
        return namOfNode;
    }

    public void addNameOfNode(String s1, String s2) {
        namOfNode.put(s1, s2);
    }

    /**
     * Gives the date and verificationTime of the last update of the NF-FG.
     *
     * @return
     */
    @Override
    public Calendar getUpdateTime() {
        return this.last_updated_time;
    }

    /**
     * Return a requested Node with the name node_name_id as a NodeReader
     *
     * @param node_name_id
     * @return
     */
    @Override
    public FLNodeReader getNode(String node_name_id) {
        return node_name_id != null && this.nodes != null ? this.nodes.get(node_name_id) : null;
    }

    /**
     * Return all the Node of the Nffg considered as a List of NodeReader
     *
     * @return
     */
    @Override
    public Set<NodeReader> getNodes() {
        return new LinkedHashSet<NodeReader>(this.nodes.values());
    }

    /**
     * Method that adds a node into the HashMap containing all the nodes of the considered Nffg
     *
     * @param node
     */
    public void addNode(FLNodeReader node) {
        if (node != null) {
            this.nodes.put(node.getName(), node);
        }
    }

    /**
     * Method that returns all the policies of the considered Nffg
     *
     * @return
     */
    public Set<PolicyReader> getPolicies() {
        return new LinkedHashSet<PolicyReader>(this.policies.values());
    }

    /**
     * Method that adds a policy into the HashMap containing all the policies of the considered Nffg
     *
     * @param policy
     */
    public void addPolicy(FLPolicyReader policy) {
        if (policy != null) {
            policies.put(policy.getName(), policy);
        }
    }
}
