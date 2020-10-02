package com.alomonshi.object.uiobjects.payment.request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class RequestResponseError {
    private int code;
    private String message;
    private List<Request> validations;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Request> getValidations() {
        return validations;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setValidations(List<Request> validations) {
        this.validations = validations;
    }
}
