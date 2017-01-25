package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLNodeReader implements NodeReader {
    /**
     * Class' attributes
     */
    private FunctionalType myFunctionalType;
    private HashMap<String, LinkReader> links;
    private String name;

    /**
     * Class' constructor
     *
     * @param type
     * @param node_name_id
     */
    FLNodeReader(FunctionalType type, String node_name_id) {
        this.name = node_name_id;
        this.myFunctionalType = type;
        links = new HashMap();
    }

    /**
     * Gives the functional type of the node.
     *
     * @return
     */
    @Override
    public FunctionalType getFuncType() {
        return this.myFunctionalType;
    }

    /**
     * Gives the known links that connect this node (i.e. have this node as the source node) in the network topology.
     *
     * @return
     */
    @Override
    public Set<LinkReader> getLinks() {
        return new LinkedHashSet(this.links.values());
    }

    /**
     * Add a link in the Hash Map
     *
     * @param link
     */
    public void addLink(LinkReader link) {
        if (link != null) {
            this.links.put(link.getName(), link);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
