package com.postingservice.demo.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Posting implements Serializable {
    @Id
    private String postingId;
    private String userId;
    private String content;
    private String emotion;
    private Date creationDate;

    public Posting(){
    }

    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
