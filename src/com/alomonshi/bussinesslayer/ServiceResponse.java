package com.alomonshi.bussinesslayer;

public class ServiceResponse {
    private Object responseData;
    private boolean response;
    private String message;

    public Object getResponseData() {
        return responseData;
    }


    public boolean getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public ServiceResponse setResponseData(Object responseData) {
        this.responseData = responseData;
        return this;
    }

    public ServiceResponse setResponse(boolean response) {
        this.response = response;
        return this;
    }

    public ServiceResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}