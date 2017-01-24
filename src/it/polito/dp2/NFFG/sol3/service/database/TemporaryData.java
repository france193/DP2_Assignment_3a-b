package it.polito.dp2.NFFG.sol3.service.database;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class TemporaryData {
    private ConcurrentHashMap<String, String> oldNodeID_newNodeID = new ConcurrentHashMap<>();
    private String NffgName;

    public synchronized ConcurrentHashMap<String, String> getOldNodeID_newNodeID() {
        return oldNodeID_newNodeID;
    }

    public synchronized String getNffgName() {
        return NffgName;
    }

    public synchronized void setNffgName(String nffgName) {
        NffgName = nffgName;
    }

    public synchronized void insertANodeTempID(String newName, String oldName) {
        this.oldNodeID_newNodeID.put(newName, oldName);
    }
}
