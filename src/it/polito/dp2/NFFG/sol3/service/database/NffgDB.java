package it.polito.dp2.NFFG.sol3.service.database;

import it.polito.dp2.NFFG.sol3.service.models.Nffg;
import it.polito.dp2.NFFG.sol3.service.models.Policy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FLDeviOS on 13/01/2017.
 */
public class NffgDB {
    private static Map<Long, Policy> policyes = new HashMap<>();
    private static Map<Long, Nffg> nffgs = new HashMap<>();

    public static Map<Long, Policy> getPolicyes() {
        return policyes;
    }

    public static Map<Long, Nffg> getNffgs() {
        return nffgs;
    }
}
