package it.polito.dp2.NFFG.sol3.service.services;

public class Neo4JServiceException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -9068214749887711318L;

    public Neo4JServiceException() {
        super();
    }

    public Neo4JServiceException(String message) {
        super(message);
    }

    public Neo4JServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public Neo4JServiceException(Throwable cause) {
        super(cause);
    }
}