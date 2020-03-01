package com.nju.topics.domain;

public class DictFileInfo {
    private String name;
    private long size;
    private String path;

    public DictFileInfo(String dName,long dSize,String dPath){
        this.name=dName;
        this.size=dSize;
        this.path=dPath;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
