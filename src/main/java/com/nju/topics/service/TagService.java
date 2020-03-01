package com.nju.topics.service;

import com.nju.topics.domain.PageDataInfo;
import com.nju.topics.domain.TagInfo;

import java.util.List;

public interface TagService {

    public List<TagInfo> getTagByPage(int pageNum);

    public PageDataInfo getFirstPageTag();
}
