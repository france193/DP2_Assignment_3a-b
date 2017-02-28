package it.polito.dp2.NFFG.sol3.service.database;

import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLNffg;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLPolicy;
import it.polito.dp2.NFFG.sol3.service.models.NffgService.FLVResult;

import java.util.concurrent.ConcurrentHashMap;

public class NffgDB {

    private static ConcurrentHashMap<String, FLNffg> nffgs = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, FLPolicy> policies = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, FLVResult> vresults = new ConcurrentHashMap<>();

    private static Boolean firstBoot = true;

    public NffgDB() {
    }

    public static synchronized ConcurrentHashMap<String, FLPolicy> getPolicies() {
        return policies;
    }

    public static synchronized ConcurrentHashMap<String, FLNffg> getNffgs() {
        return nffgs;
    }

    public static synchronized ConcurrentHashMap<String, FLVResult> getVresults() {
        return vresults;
    }

    public static synchronized Boolean getFirstBoot() {
        return firstBoot;
    }

    public static synchronized void setFirstBoot(Boolean firstBoot) {
        NffgDB.firstBoot = firstBoot;
    }
}
