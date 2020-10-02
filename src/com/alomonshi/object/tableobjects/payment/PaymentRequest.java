package com.alomonshi.object.tableobjects.payment;

import java.time.LocalDateTime;

public class PaymentRequest {
    private int id;
    private int clientID;
    private String amount;
    private String authority;
    private String message;
    private int code;
    private LocalDateTime datetime;
    private int unitID;
    private boolean requestType;
    private int reserveTimeID;

    public int getId() {
        return id;
    }

    public int getClientID() {
        return clientID;
    }

    public String getAmount() {
        return amount;
    }

    public String getAuthority() {
        return authority;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public int getUnitID() {
        return unitID;
    }

    public boolean getRequestType() {
        return requestType;
    }

    public int getReserveTimeID() {
        return reserveTimeID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setRequestType(boolean requestType) {
        this.requestType = requestType;
    }

    public void setReserveTimeID(int reserveTimeID) {
        this.reserveTimeID = reserveTimeID;
    }
}
