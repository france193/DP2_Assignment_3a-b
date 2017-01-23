package it.polito.dp2.NFFG.sol3.service.database;

import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Francesco Longo (s223428) on 13/01/2017.
 */
public class NffgDB {
    private static ConcurrentHashMap<String, FLNffg> nffgs = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, FLPolicy> policies = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, NameRefer> tempNffgIDs = new ConcurrentHashMap<>();

    private static Boolean firstBoot = true;

    public NffgDB() {
    }

    public static synchronized ConcurrentHashMap<String, NameRefer> getTempNffgIDs() {
        return tempNffgIDs;
    }

    public static synchronized ConcurrentHashMap<String, FLPolicy> getPolicies() {
        return policies;
    }

    public static synchronized ConcurrentHashMap<String, FLNffg> getNffgs() {
        return nffgs;
    }

    public static synchronized Boolean getFirstBoot() {
        return firstBoot;
    }

    public static synchronized void setFirstBoot(Boolean firstBoot) {
        NffgDB.firstBoot = firstBoot;
    }
}
