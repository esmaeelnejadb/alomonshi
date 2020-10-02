package com.alomonshi.object.uiobjects.payment.verification;

import javax.xml.bind.annotation.XmlAttribute;

public class VerificationResponseData {
    private int code;
    private String message;
    private String card_hash;
    private String card_pan;
    private long ref_id;
    private String fee_type;
    private int fee;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getCard_hash() {
        return card_hash;
    }

    public String getCard_pan() {
        return card_pan;
    }

    public long getRef_id() {
        return ref_id;
    }

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

    public void setCard_hash(String card_hash) {
        this.card_hash = card_hash;
    }

    public void setCard_pan(String card_pan) {
        this.card_pan = card_pan;
    }

    public void setRef_id(long ref_id) {
        this.ref_id = ref_id;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
