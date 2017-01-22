package it.polito.dp2.NFFG.sol3.service.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FLDeviOS on 18/01/2017.
 */
public class NameRefer {
    private String nffgNewID;
    public Map<String, String> tempNodeIDs;

    public NameRefer() {
        tempNodeIDs = new HashMap<>();
    }

    public String getNffgNewID() {
        return nffgNewID;
    }

    public void setNffgNewID(String nffgNewID) {
        nffgNewID = nffgNewID;
    }
}
