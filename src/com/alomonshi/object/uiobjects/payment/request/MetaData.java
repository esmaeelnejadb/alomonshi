package com.alomonshi.object.uiobjects.payment.request;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlAttribute;

public class MetaData {
    private String mobile;
    private String email;

    public MetaData(String mobile, String email) {
        this.mobile = mobile;
        this.email = email;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlAttribute(name = "email")
    public String getEmail() {
        return email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
