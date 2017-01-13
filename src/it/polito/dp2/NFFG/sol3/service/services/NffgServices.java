package it.polito.dp2.NFFG.sol3.service.services;

import it.polito.dp2.NFFG.sol3.service.models.*;

import java.util.List;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgServices {

    // method for service
    public List<Nffg> getNffgs() {
        updateDB(); //TODO
    }

    public Nffg getNffg(int nffg_id) {
        updateDB(); //TODO
    }

    public Nodes getNffgNodes(int nffg_id) {
        updateDB(); //TODO
    }

    public Node getNffgNode(int nffg_id, int node_id) {
        updateDB(); //TODO
    }

    public Links getNffgNodeLinks(int nffg_id, int node_id) {
        updateDB(); //TODO
    }

    public Link getNffgNodeLink(int nffg_id, int node_id, int link_id) {
        updateDB(); //TODO
    }

    public Links getNffgLinks(int nffg_id) {
        updateDB(); //TODO
    }

    public Link getNffgLink(int nffg_id, int link_id) {
        updateDB(); //TODO
    }

    public Policies getNffgPolicies(int nffg_id) {
        updateDB(); //TODO
    }

    public Policy getNffgPolicy(int nffg_id, int policy_id) {
        updateDB(); //TODO
    }

    public Policies getPolicies() {
        updateDB(); //TODO
    }

    public Policy getPolicy(int policy_id) {
        updateDB(); //TODO
    }

    private void updateDB () {
        // TODO
    }
}
