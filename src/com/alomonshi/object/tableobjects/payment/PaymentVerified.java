package com.alomonshi.object.tableobjects.payment;

import java.time.LocalDateTime;

public class PaymentVerified {
    private int ID;
    private String authority;
    private int code;
    private String message;
    private String cardHash;
    private String cardPan;
    private long refID;
    private String feeType;
    private int fee;
    private LocalDateTime dateTime;

    public int getID() {
        return ID;
    }

    public String getAuthority() {
        return authority;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getCardHash() {
        return cardHash;
    }

    public String getCardPan() {
        return cardPan;
    }

    public long getRefID() {
        return refID;
    }

    public String getFeeType() {
        return feeType;
    }

    public int getFee() {
        return fee;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
    }

    public void setCardPan(String cardPan) {
        this.cardPan = cardPan;
    }

    public void setRefID(long refID) {
        this.refID = refID;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
