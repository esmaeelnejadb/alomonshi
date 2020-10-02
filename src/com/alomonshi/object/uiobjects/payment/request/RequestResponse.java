package com.alomonshi.object.uiobjects.payment.request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class RequestResponse {

    private RequestResponseData data;
    private List<RequestResponseError> errors;

    public RequestResponseData getData() {
        return data;
    }

    public List<RequestResponseError> getErrors() {
        return errors;
    }

    public void setData(RequestResponseData data) {
        this.data = data;
    }

    public void setErrors(List<RequestResponseError> errors) {
        this.errors = errors;
    }
}
