package com.alomonshi.object.uiobjects.payment.verification;

import javax.xml.bind.annotation.XmlAttribute;

public class Verification {
    private String merchantID;
    private String amount;
    private String authority;

    @XmlAttribute(name = "merchant_id")
    public String getMerchantID() {
        return merchantID;
    }

    @XmlAttribute(name = "amount")
    public String getAmount() {
        return amount;
    }

    @XmlAttribute(name = "authority")
    public String getAuthority() {
        return authority;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
