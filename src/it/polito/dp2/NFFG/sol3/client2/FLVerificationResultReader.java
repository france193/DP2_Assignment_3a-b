package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

import java.util.Calendar;

/**
 * Created by FLDeviOS on 27/01/2017.
 */
public class FLVerificationResultReader implements VerificationResultReader {
    private PolicyReader policyReader;
    private Boolean verificationResult;
    private String verificationResultMsg;
    private Calendar verificationTime;

    public FLVerificationResultReader(PolicyReader policyReader,
                                      Boolean verificationResult,
                                      String verificationResultMsg,
                                      Calendar verificationTime) {
        this.policyReader = policyReader;
        this.verificationResult = verificationResult;
        this.verificationResultMsg = verificationResultMsg;
        this.verificationTime = verificationTime;
    }

    @Override
    public PolicyReader getPolicy() {
        return policyReader;
    }

    @Override
    public Boolean getVerificationResult() {
        return verificationResult;
    }

    @Override
    public String getVerificationResultMsg() {
        return verificationResultMsg;
    }

    @Override
    public Calendar getVerificationTime() {
        return verificationTime;
    }
}
