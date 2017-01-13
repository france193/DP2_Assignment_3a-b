package it.polito.dp2.NFFG.sol3.client1;

import it.polito.dp2.NFFG.*;
import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;
import it.polito.dp2.NFFG.lab3.NFFGClient;
import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.lab3.UnknownNameException;

import java.util.Set;

/**
 * Created by FLDeviOS on 12/01/2017.
 */
public class FLNFFGClient implements NFFGClient {

    private static final String DEFAULT_URL = "http://localhost:8080/NffgService/rest";

    private NffgVerifier monitor;

    public FLNFFGClient() {

        // try to read default URL
        String fileName = System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file");

        if (fileName == null) {
            fileName = DEFAULT_URL;
        }
    }

    /**
     * Loads the known NFFG with the given name into the remote service.
     * This operation can fail if an NFFG with the same name already exists in the remote service.
     *
     * @param name the name of the NFFG to be loaded
     * @throws UnknownNameException   if the name passed as argument does not correspond to a locally known NFFG. No alteration of data in the remote service occurs in this case.
     * @throws AlreadyLoadedException if an NFFG with the same name already exists in the remote service.
     * @throws ServiceException       if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadNFFG(String name) throws UnknownNameException, AlreadyLoadedException, ServiceException {

    }

    /**
     * Loads all the known NFFGs and policies to the remote service (including verification results of policies when available).
     * This operation can fail if an NFFG with the same name as one of the known NFFGs already exists in the remote service.
     * If a policy with the same name as one of the known ones already exists in the remote service, the existing policy has to be overwritten.
     *
     * @throws AlreadyLoadedException if an NFFG with the same name already exists in the remote service.
     * @throws ServiceException       if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadAll() throws AlreadyLoadedException, ServiceException {

    }

    /**
     * Loads a new reachability policy into the remote service given the policy properties.
     * If a policy with the given name already exists in the service, the new policy substitutes
     * the old one. The new policy is uploaded without a verification result.
     *
     * @param name        the name to be given to the new policy
     * @param nffgName    the name of a known NFFG the new policy refers to
     * @param isPositive  true if the new policy is positive
     * @param srcNodeName the name of the source node of the new policy
     * @param dstNodeName the name of the destination node of the new policy
     * @throws UnknownNameException if nffgName is not the name of a known NFFG, or srcNodeName and dstNodeName are not both nodes belonging to the known NFFG named nffgName.
     * @throws ServiceException     if any other error occurs when trying to upload the NFFG.
     */
    @Override
    public void loadReachabilityPolicy(String name, String nffgName, boolean isPositive, String srcNodeName, String dstNodeName) throws UnknownNameException, ServiceException {

    }

    /**
     * Unloads the reachability policy with a given name from the remote service
     *
     * @param name the name of the reachability policy to be unloaded
     * @throws UnknownNameException if the policy name passed as argument does not correspond to a reachability policy already loaded in the remote service
     * @throws ServiceException     if any other error occurs when trying to unload the policy
     */
    @Override
    public void unloadReachabilityPolicy(String name) throws UnknownNameException, ServiceException {

    }

    /**
     * Asks the service to test one of the previously uploaded reachability policies
     *
     * @param name the name of the reachability policy to be tested
     * @throws UnknownNameException if the policy name passed as argument does not correspond to a reachability policy already loaded in the remote service
     * @throws ServiceException     if any other error occurs when trying to test reachability
     * @return the result of the verification of the policy
     */
    @Override
    public boolean testReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
        return false;
    }
}
