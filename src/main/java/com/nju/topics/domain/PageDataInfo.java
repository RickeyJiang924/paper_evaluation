package com.nju.topics.domain;

import java.util.List;

public class PageDataInfo {
    private int pageNum;
    private List pageData;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageData(List pageData) {
        this.pageData = pageData;
    }

    public List getPageData() {
        return pageData;
    }
}

