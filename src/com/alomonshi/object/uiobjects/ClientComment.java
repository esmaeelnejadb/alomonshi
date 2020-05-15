package com.alomonshi.object.uiobjects;

import com.alomonshi.restwebservices.adaptors.LocalDateTimeToDateAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

public class ClientComment {
    private int commentID;
    private String comment;
    private String replyComment;
    private String companyName;
    private String unitName;
    private LocalDateTime commentDate;
    private LocalDateTime replyDate;
    private float commentRate;
    private boolean isActive;

    @JsonView(JsonViews.ClientViews.class)
    public int getCommentID() {
        return commentID;
    }

    @JsonView(JsonViews.ClientViews.class)
    public String getComment() {
        return comment;
    }

    @JsonView(JsonViews.ClientViews.class)
    public String getReplyComment() {
        return replyComment;
    }

    @JsonView(JsonViews.ClientViews.class)
    public String getCompanyName() {
        return companyName;
    }

    @JsonView(JsonViews.ClientViews.class)
    public String getUnitName() {
        return unitName;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlJavaTypeAdapter(LocalDateTimeToDateAdaptor.class)
    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    @JsonView(JsonViews.ClientViews.class)
    @XmlJavaTypeAdapter(LocalDateTimeToDateAdaptor.class)
    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    @JsonView(JsonViews.ClientViews.class)
    public float getCommentRate() {
        return commentRate;
    }

    @JsonView(JsonViews.ClientViews.class)
    public boolean isActive() {
        return isActive;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

    public void setCommentRate(float commentRate) {
        this.commentRate = commentRate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
