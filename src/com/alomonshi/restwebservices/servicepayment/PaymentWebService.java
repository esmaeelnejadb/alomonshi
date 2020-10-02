package com.alomonshi.restwebservices.servicepayment;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.payment.request.PaymentRequestService;
import com.alomonshi.bussinesslayer.payment.verify.PaymentVerificationService;
import com.alomonshi.object.uiobjects.payment.fromclient.ClientUnitPaymentRequest;
import com.alomonshi.object.uiobjects.payment.fromclient.ClientUnitPaymentVerification;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("unitPayment")
public class PaymentWebService {
    private ServiceResponse serviceResponse;
    private PaymentRequestService paymentRequestService;
    private PaymentVerificationService paymentVerificationService;

    /**
     * Payment request form client
     * @param clientUnitPaymentRequest request parameter
     * @return service response
     */
    @JsonView(JsonViews.ClientViews.class)
    @ClientSecured
    @POST
    @Path("/paymentRequest")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse requestPayment(ClientUnitPaymentRequest clientUnitPaymentRequest) {
        serviceResponse = new ServiceResponse();
        paymentRequestService = new PaymentRequestService(clientUnitPaymentRequest, serviceResponse);
        try {
            return paymentRequestService.handlePaymentRequest();
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }
    }

    @JsonView(JsonViews.ClientViews.class)
    @ClientSecured
    @POST
    @Path("/verifyRequest")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse verifyPayment(ClientUnitPaymentVerification clientUnitPaymentVerification) {
        try {
            if (clientUnitPaymentVerification.getStatus().equals("OK")) {
                serviceResponse = new ServiceResponse();
                paymentVerificationService = new PaymentVerificationService(
                                clientUnitPaymentVerification.getAuthorityCode(),
                                serviceResponse);
                return paymentVerificationService.handleVerificationRequest();
            }else {
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
            }
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }
    }
}
