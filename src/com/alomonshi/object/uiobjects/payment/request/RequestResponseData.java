package com.alomonshi.object.uiobjects.payment.request;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestResponseData {
    private int code;
    private String message;
    private String authority;
    private String fee_type;
    private int fee;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthority() {
        return authority;
    }

    @XmlAttribute(name = "fee_type")
    public String getFee_type() {
        return fee_type;
    }

    public int getFee() {
        return fee;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
