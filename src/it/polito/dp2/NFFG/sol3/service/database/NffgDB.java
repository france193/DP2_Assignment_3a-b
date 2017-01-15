package it.polito.dp2.NFFG.sol3.service.database;

import it.polito.dp2.NFFG.sol3.service.models.NffgService.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgDB {
    private static Map<String, FLPolicy> policyes = new HashMap<>();
    private static Map<String, FLNffg> nffgs = new HashMap<>();

    public static Map<String, FLPolicy> getPolicyes() {
        return policyes;
    }

    public static Map<String, FLNffg> getNffgs() {
        return nffgs;
    }
}
