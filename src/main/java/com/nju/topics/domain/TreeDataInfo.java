package com.nju.topics.domain;

import java.util.ArrayList;

public class TreeDataInfo {
    private ArrayList<TreeDataInfo> children;
    private String name;

    public ArrayList<TreeDataInfo> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeDataInfo> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
