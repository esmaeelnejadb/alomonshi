package com.alomonshi.object.uiobjects.payment.verification;
import javax.xml.bind.annotation.XmlAttribute;

public class VerificationResponseError {
    private int code;
    private String message;

    @XmlAttribute(name = "code")
    public int getCode() {
        return code;
    }

    @XmlAttribute(name = "message")
    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
