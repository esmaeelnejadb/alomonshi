package com.alomonshi.bussinesslayer.payment.verify;

import com.alomonshi.bussinesslayer.httpclient.HttpClientRequestProvider;
import com.alomonshi.configuration.ConfigurationParameter;
import com.alomonshi.object.uiobjects.payment.verification.Verification;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PaymentVerificationHandler {
    private Verification verification;
    private HttpClientRequestProvider<Verification> httpClientRequestProvider;

    /**
     * Constructor
     * @param verification injected object
     */
    public PaymentVerificationHandler(Verification verification) {
        httpClientRequestProvider = new HttpClientRequestProvider<>(ConfigurationParameter.paymentVerifyURL);
        this.verification = verification;
    }

    /**
     * Sending verification request
     * @return http response
     */
    public Response sendPaymentVerification () {
        return httpClientRequestProvider.post("", MediaType.APPLICATION_JSON, verification);
    }
}
