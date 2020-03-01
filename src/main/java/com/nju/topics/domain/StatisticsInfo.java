package com.nju.topics.domain;

public class StatisticsInfo {
    private String key;
    private int num;

    public StatisticsInfo(String key,int num){
        this.key=key;
        this.num=num;
    }

    public int getNum() {
        return num;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
