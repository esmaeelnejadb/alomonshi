package com.alomonshi.object.uiobjects.payment.verification;

import java.util.List;

public class VerificationResponse {
    private VerificationResponseData data;
    private List<VerificationResponseError> errors;

    public VerificationResponseData getData() {
        return data;
    }

    public List<VerificationResponseError> getErrors() {
        return errors;
    }

    public void setData(VerificationResponseData data) {
        this.data = data;
    }

    public void setErrors(List<VerificationResponseError> errors) {
        this.errors = errors;
    }
}
