package com.nju.topics.serviceImpl;

import com.nju.topics.domain.PageDataInfo;
import com.nju.topics.domain.TagInfo;
import com.nju.topics.service.TagService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService {
    @Override
    public List<TagInfo> getTagByPage(int pageNum) {
        return null;
    }

    @Override
    public PageDataInfo getFirstPageTag() {
        return null;
    }
}
