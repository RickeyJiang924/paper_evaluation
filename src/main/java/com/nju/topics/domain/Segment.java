package com.nju.topics.domain;

import java.util.List;

public class Segment {

    private String title;

    private List<String> segments;

    public Segment(String title, List<String> segments) {
        this.title = title;
        this.segments = segments;
    }

    public Segment() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSegments() {
        return segments;
    }

    public void setSegments(List<String> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "title='" + title + '\'' +
                ", segments=" + segments +
                '}';
    }
}
