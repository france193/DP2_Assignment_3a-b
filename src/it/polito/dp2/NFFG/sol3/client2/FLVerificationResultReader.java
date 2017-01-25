package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLVerificationResultReader implements VerificationResultReader {
    /**
     * Class' attributes
     */
    private PolicyReader policy;
    private boolean result;
    private GregorianCalendar time;
    private String message;

    /**
     * Class' constructor
     *
     * @param policy
     * @param verificationResult
     * @param verificationMessage
     * @param verificationTime
     */
    public FLVerificationResultReader(PolicyReader policy,
                                      Boolean verificationResult,
                                      GregorianCalendar verificationTime,
                                      String verificationMessage) {

        this.policy = policy;
        this.result = verificationResult;
        this.message = verificationMessage;
        this.time = verificationTime;
    }

    /**
     * Gives the policy to which this verificationResult is associated.
     *
     * @return
     */
    @Override
    public PolicyReader getPolicy() {
        return this.policy;
    }

    /**
     * Gives the verificationResult of the verification. The policy is violated if the verification verificationResult is false, otherwise it is satisfied.
     *
     * @return
     */
    @Override
    public Boolean getVerificationResult() {
        return Boolean.valueOf(this.result);
    }

    /**
     * Gives a human-readable verificationMessage associated with the verificationResult of the verification.
     *
     * @return
     */
    @Override
    public String getVerificationResultMsg() {
        return this.message;
    }

    /**
     * Gives the date when the policy has been verified.
     *
     * @return
     */
    @Override
    public Calendar getVerificationTime() {
        return this.time != null ? (Calendar) this.time.clone() : null;
    }
}
