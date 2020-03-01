package com.nju.topics.domain;

import java.util.List;

public class EvolutionDataInfo {
    private String id;
    private String weight;
    private String year;

    private List topicWords;

    private List topicPaper;

    public List getTopicPaper() {
        return topicPaper;
    }

    public List getTopicWords() {
        return topicWords;
    }

    public String getId() {
        return id;
    }

    public String getWeight() {
        return weight;
    }

    public String getYear() {
        return year;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTopicPaper(List topicPaper) {
        this.topicPaper = topicPaper;
    }

    public void setTopicWords(List topicWords) {
        this.topicWords = topicWords;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
