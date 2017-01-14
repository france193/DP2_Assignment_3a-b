package it.polito.dp2.NFFG.sol3.service.database;

import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgDB {
    private static Map<Long, FLPolicy> policyes = new HashMap<>();
    private static Map<Long, FLNffg> nffgs = new HashMap<>();

    public static Map<Long, FLPolicy> getPolicyes() {
        return policyes;
    }

    public static Map<Long, FLNffg> getNffgs() {
        return nffgs;
    }
}
