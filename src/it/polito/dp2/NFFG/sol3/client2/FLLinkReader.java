package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLLinkReader extends FLNamedEntityReader implements LinkReader {
    /**
     * Class' attributes
     */
    private NodeReader sourceNode;
    private NodeReader destinationNode;

    /**
     * Class' constructor
     *
     * @param link_name_id
     * @param sourceNode
     * @param destinationNode
     */
    FLLinkReader(String link_name_id, NodeReader sourceNode, NodeReader destinationNode) {
        super(link_name_id);
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    /**
     * Gives access to the information associated to the destination node of this link.
     *
     * @return
     */
    @Override
    public NodeReader getSourceNode() {
        return this.sourceNode;
    }

    /**
     * Gives access to the information associated to the sourceNode node of this link.
     *
     * @return
     */
    @Override
    public NodeReader getDestinationNode() {
        return this.destinationNode;
    }
}
