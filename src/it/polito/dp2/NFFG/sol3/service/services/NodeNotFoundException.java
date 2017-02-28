package it.polito.dp2.NFFG.sol3.service.services;

public class NodeNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -5621940310393813293L;

    public NodeNotFoundException() {
        super();
    }

    public NodeNotFoundException(String message) {
        super(message);
    }

    public NodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodeNotFoundException(Throwable cause) {
        super(cause);
    }
}