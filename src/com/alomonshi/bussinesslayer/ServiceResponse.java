package com.alomonshi.bussinesslayer;

import java.util.List;

public class ServiceResponse {
    private List<Object> responseData;
    private boolean response;
    private String message;

    public List<Object> getResponseData() {
        return responseData;
    }

    public boolean getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public ServiceResponse setResponseData(List<Object> responseData) {
        this.responseData = responseData;
        return this;
    }

    public ServiceResponse setResponse(boolean response) {
        this.response = response;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}