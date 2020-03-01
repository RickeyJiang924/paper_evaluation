package com.nju.topics.service;

import com.nju.topics.domain.PageDataInfo;
import com.nju.topics.domain.Segment;

import java.util.List;

public interface Segments {

    public PageDataInfo getAllSegments(String segName);

    public List<Segment> getSegmentsByPage(String segName,int pageNum);
    public List<Segment> getSegmentsNoneCondition(String segName);

    public String reRunPy(String segFileName);

    public List<Segment> getPreSegments(String segName);
}
