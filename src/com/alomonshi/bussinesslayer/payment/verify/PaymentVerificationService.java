package com.alomonshi.bussinesslayer.payment.verify;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.configuration.ConfigurationParameter;
import com.alomonshi.datalayer.dataaccess.payment.TablePaymentRequest;
import com.alomonshi.datalayer.dataaccess.payment.TablePaymentVerified;
import com.alomonshi.object.tableobjects.payment.PaymentRequest;
import com.alomonshi.object.tableobjects.payment.PaymentVerified;
import com.alomonshi.object.uiobjects.payment.ZarinStatus;
import com.alomonshi.object.uiobjects.payment.verification.Verification;
import com.alomonshi.object.uiobjects.payment.verification.VerificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;

public class PaymentVerificationService {
    private Verification verification;
    private PaymentVerified paymentVerified;
    private PaymentRequest paymentRequest;
    private VerificationResponse verificationResponse;
    private ServiceResponse serviceResponse;

    public PaymentVerificationService(String authorityCode, ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
        paymentRequest = TablePaymentRequest.getPaymentRequest(authorityCode);
        verification = new Verification();
        mapClientRequestToVerificationRequest();

    }

    /**
     * Mapping client request to verification object to be send to payment server
     */
    private void mapClientRequestToVerificationRequest () {
        verification.setMerchantID(ConfigurationParameter.merchantId);
        verification.setAmount(paymentRequest.getAmount());
        verification.setAuthority(paymentRequest.getAuthority());
    }

    /**
     * Handling verification request
     * @return service response
     */
    public ServiceResponse handleVerificationRequest () {
        PaymentVerificationHandler paymentVerificationHandler = new PaymentVerificationHandler(verification);
        Response response = paymentVerificationHandler.sendPaymentVerification();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                verificationResponse = mapper.readValue(response.readEntity(String.class), VerificationResponse.class);
                this.mapVerificationResponseToTableObject();
                checkAndInsertVerificationObject(serviceResponse, paymentVerified);
                return serviceResponse;
            } catch (IOException e) {
                e.printStackTrace();
                serviceResponse.setResponse(false);
            }
        }else
            serviceResponse.setResponse(false);
        return serviceResponse;
    }

    /**
     * Mapping object got from payment server to table object
     */
    private void mapVerificationResponseToTableObject () {
        paymentVerified = new PaymentVerified();
        paymentVerified.setAuthority(paymentRequest.getAuthority());
        paymentVerified.setCode(verificationResponse.getData().getCode());
        paymentVerified.setMessage(verificationResponse.getData().getMessage());
        paymentVerified.setCardHash(verificationResponse.getData().getCard_hash());
        paymentVerified.setCardPan(verificationResponse.getData().getCard_pan());
        paymentVerified.setRefID(verificationResponse.getData().getRef_id());
        paymentVerified.setFeeType(verificationResponse.getData().getFee_type());
        paymentVerified.setFee(verificationResponse.getData().getFee());
        paymentVerified.setDateTime(LocalDateTime.now());
    }

    /**
     * Checking and inserting payment verified object
     * @param serviceResponse returned response
     * @param paymentVerified check not to be inserted if exist in database
     */
    private void checkAndInsertVerificationObject(ServiceResponse serviceResponse, PaymentVerified paymentVerified) {
        PaymentVerified paymentVerifiedForCheck = TablePaymentVerified.getPaymentRequest(paymentVerified.getAuthority());
        if (paymentVerifiedForCheck.getID() == 0) {
            if (TablePaymentVerified.insertVerifiedTransaction(paymentVerified)){
                serviceResponse.setResponse(true);
            }else
                serviceResponse.setResponse(false);
        }else {
            serviceResponse.setResponse(true).setResponseData(ZarinStatus.OK.getCode());
        }
    }
}
