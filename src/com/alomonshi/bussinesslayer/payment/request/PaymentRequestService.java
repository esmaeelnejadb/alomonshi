package com.alomonshi.bussinesslayer.payment.request;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.configuration.ConfigurationParameter;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.datalayer.dataaccess.payment.TablePaymentRequest;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.tableobjects.payment.PaymentRequest;
import com.alomonshi.object.uiobjects.payment.fromclient.ClientUnitPaymentRequest;
import com.alomonshi.object.uiobjects.payment.request.MetaData;
import com.alomonshi.object.uiobjects.payment.request.Request;
import com.alomonshi.object.uiobjects.payment.request.RequestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;

public class PaymentRequestService {
    private Users user;
    private Request request;
    private RequestResponse requestResponse;
    private ClientUnitPaymentRequest clientUnitPaymentRequest;
    private ServiceResponse serviceResponse;
    private PaymentRequest paymentRequest;

    /**
     * Constructor
     * @param clientUnitPaymentRequest input parameter
     */
    public PaymentRequestService(ClientUnitPaymentRequest clientUnitPaymentRequest, ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
        this.clientUnitPaymentRequest = clientUnitPaymentRequest;
        request = new Request();
        requestResponse = new RequestResponse();
        user = TableClient.getUser(clientUnitPaymentRequest.getClientID());
        this.mapClientRequestToPaymentRequest(); //Mapping data got from client to request object
    }

    /**
     * Mapping payment request got from client to payment request type
     */
    private void mapClientRequestToPaymentRequest () {
        request.setMerchantId(ConfigurationParameter.merchantId);
        request.setAmount(clientUnitPaymentRequest.getAmount());
        request.setCallbackURL(getCallBackURL());
        request.setDescription(getDiscription());
        MetaData metaData = new MetaData(user.getPhoneNo(), user.getEmail());
        request.setMetadata(metaData);
    }

    /**
     * Handing payment request
     * @return service response
     */
    public ServiceResponse handlePaymentRequest () {
        PaymentRequestHandler paymentRequestHandler = new PaymentRequestHandler(request);
        Response response = paymentRequestHandler.sendPaymentRequest();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                requestResponse = mapper.readValue(response.readEntity(String.class), RequestResponse.class);
                mapRequestResponseToTableObject();
                if (TablePaymentRequest.insertRequest(paymentRequest)) {
                    serviceResponse.setResponse(true).setResponseData(getRedirectGateURL());
                }else
                    serviceResponse.setResponse(false);
            } catch (IOException e) {
                e.printStackTrace();
                serviceResponse.setResponse(false);
            }
        }else
            serviceResponse.setResponse(false);
        return serviceResponse;
    }


    /**
     * Mapping request response to object save into database
     */
    private void mapRequestResponseToTableObject() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setClientID(user.getClientID());
        paymentRequest.setAmount(request.getAmount());
        paymentRequest.setAuthority(requestResponse.getData().getAuthority());
        paymentRequest.setMessage(requestResponse.getData().getMessage());
        paymentRequest.setCode(requestResponse.getData().getCode());
        paymentRequest.setDatetime(LocalDateTime.now());
        paymentRequest.setUnitID(clientUnitPaymentRequest.getUnitID());
        paymentRequest.setRequestType(clientUnitPaymentRequest.getRequestType());
        paymentRequest.setReserveTimeID(clientUnitPaymentRequest.getReserveTimeID());
    }

    /**
     * Creating redirecting payment gate
     * @return payment gate
     */
    private String getRedirectGateURL () {
        return ConfigurationParameter.paymentGateURL + paymentRequest.getAuthority();
    }

    /**
     * Getting callback url
     * @return intended url
     */
    private String getCallBackURL () {
        return ConfigurationParameter.paymentCallbackURL + clientUnitPaymentRequest.getUnitID();
    }

    /**
     * Getting payment description
     * @return description
     */
    private String getDiscription () {
        return "پرداخت آنلاین کاربر شماره " + clientUnitPaymentRequest.getClientID() +
                " در واحد شماره ی " + clientUnitPaymentRequest.getUnitID();
    }
}
