package com.nju.topics.domain;

import java.util.ArrayList;

public class TagInfo {

    private String title;
    private ArrayList<String> tags;

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
