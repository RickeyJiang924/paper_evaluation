package com.nju.topics.domain;

public class ResponseInfo {

    private String result;
    private String description;

    public String getDescription() {
        return description;
    }

    public String getResult() {
        return result;
    }

    public void setDescription(String describtion) {
        this.description = describtion;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
