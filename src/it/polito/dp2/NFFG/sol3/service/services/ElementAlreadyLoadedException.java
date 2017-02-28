package it.polito.dp2.NFFG.sol3.service.services;

public class ElementAlreadyLoadedException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -2136168889547954779L;

    public ElementAlreadyLoadedException() {
        super();
    }

    public ElementAlreadyLoadedException(String message) {
        super(message);
    }

    public ElementAlreadyLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementAlreadyLoadedException(Throwable cause) {
        super(cause);
    }
}