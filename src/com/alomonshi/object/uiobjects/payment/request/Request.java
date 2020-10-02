package com.alomonshi.object.uiobjects.payment.request;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlAttribute;

public class Request {
    private String merchantId;
    private String amount;
    private String callbackURL;
    private String description;
    private MetaData metadata;

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "merchant_id")
    public String getMerchantId() {
        return merchantId;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "amount")
    public String getAmount() {
        return amount;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "callback_url")
    public String getCallbackURL() {
        return callbackURL;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "description")
    public String getDescription() {
        return description;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "metadata")
    public MetaData getMetadata() {
        return metadata;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }
}
