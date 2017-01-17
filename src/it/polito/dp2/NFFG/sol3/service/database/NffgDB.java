package it.polito.dp2.NFFG.sol3.service.database;

import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgDB {
    private static ConcurrentHashMap<String, FLPolicy> policies;
    private static ConcurrentHashMap<String, FLNffg> nffgs;

    public NffgDB() {
        policies = new ConcurrentHashMap<>();
        nffgs = new ConcurrentHashMap<>();
    }

    public static synchronized ConcurrentHashMap<String, FLPolicy> getPolicies() {
        return policies;
    }

    public static synchronized ConcurrentHashMap<String, FLNffg> getNffgs() {
        return nffgs;
    }
}
