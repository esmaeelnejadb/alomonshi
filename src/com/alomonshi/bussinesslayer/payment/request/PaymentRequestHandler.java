package com.alomonshi.bussinesslayer.payment.request;

import com.alomonshi.bussinesslayer.httpclient.HttpClientRequestProvider;
import com.alomonshi.configuration.ConfigurationParameter;
import com.alomonshi.object.uiobjects.payment.request.Request;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PaymentRequestHandler {
    private Request request;
    private HttpClientRequestProvider<Request> httpClientRequestProvider;

    /**
     * Constructor
     * @param request to send to payment server
     */
    public PaymentRequestHandler(Request request) {
        httpClientRequestProvider = new HttpClientRequestProvider<>(ConfigurationParameter.paymentRequestURL);
        this.request = request;
    }

    /**
     * Sending request to payment server
     * @return http response
     */
    public Response sendPaymentRequest () {
        return httpClientRequestProvider.post("", MediaType.APPLICATION_JSON, request);
    }

}
