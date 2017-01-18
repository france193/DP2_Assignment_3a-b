package it.polito.dp2.NFFG.sol3.service.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FLDeviOS on 18/01/2017.
 */
public class oldIDs {
    private String NffgOldID;
    public Map<String, String> tempNodeIDs;

    public oldIDs() {
        tempNodeIDs = new HashMap<>();
    }

    public String getNffgOldID() {
        return NffgOldID;
    }

    public void setNffgOldID(String nffgOldID) {
        NffgOldID = nffgOldID;
    }
}
