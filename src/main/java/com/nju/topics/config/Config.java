package com.nju.topics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
//import org.springframework.context.annotation.Configuration;

@Component
public class Config {
    public static final int DEFAULT_NUM = 10;

    @Value("${seg.segments.path}")
    private String segmentsPath;

    @Value("${seg.dict.path}")
    private String dictPath;

    @Value("${seg.py.path}")
    private String pyPath;

    @Value("${web.upload.file}")
    private String uploadPath;

    @Value("${web.download.file}")
    private String downloadPath;

    @Value("${seg.backup.path}")
    private String segBackUpPath;

    @Value("${dict.backup.path}")
    private String dictBackUpPath;

    @Value("${rerun.file.name}")
    private String reRunFileName;

    @Value("${statistics.parent.folder}")
    private String statisticsFolder;

    @Value("${dict.log.path}")
    private String dictLogPath;

    @Value("${rerun.interval.minutes}")
    private int intervalTime;

    @Value( "${history.paper.index}")
    private String historyPaperIndex;
    @Value( "${history.paper.type}")
    private String historyPaperType;
    @Value( "${history.author.index}")
    private String historyAuthorIndex;
    @Value( "${history.author.type}")
    private String historyAuthorType;
    @Value( "${history.reference.index}")
    private String historyReferenceIndex;
    @Value( "${history.reference.type}")
    private String historyReferenceType;

    @Value("${pair.file.path}")
    private String pairFilePath;

    @Value("${dict.statistics.path}")
    private String dictStatisticsPath;

    @Value("${evolution.data.path}")
    private String evolutionFolderPath;

    private long preGetNum=0;

    private int perPageNum=500;

    private int EVOLUTION_SUMMARY_PAPER=10;
    private int EVOLUTION_SUMMARY_TOPIC=20;

    public String[] noStatisticsWords={"的","为例","20","志","初探","考","说","读","观","前","题","卷","早期","形成","地区","研讨会","探析","理论","评","再","化","辩","文献","发展","研究","新","史学","历史","中","年","中心","时期","综述","史","笔谈","关系","°","世纪","º","ƒ","年代","古代",
    "∑","µ","±","Œ","÷","与","–","体系", "相关","∂","区域", "∞","π","Ω", "Ã", "™","Ø","≥","∫","ø","¡","œ","2013","˙","À","∆","ÿ"};

    private Long lastPyRunTime=new Date("1999/01/01 00:00:00").getTime();


    public String getSegmentsPath() {
        return segmentsPath;
    }

    public String getDictPath() {
        return dictPath;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public String getPyPath() {
        return pyPath;
    }

    public String getDictBackUpPath() {
        return dictBackUpPath;
    }

    public String getSegBackUpPath() {
        return segBackUpPath;
    }

    public Long getLastPyRunTime() {
        return lastPyRunTime;
    }

    public void setLastPyRunTime(Long lastPyRunTime) {
        this.lastPyRunTime = lastPyRunTime;
    }

    public String getReRunFileName() {
        return reRunFileName;
    }

    public String getStatisticsFolder() {
        return statisticsFolder;
    }

    public String getDictLogPath() {
        return dictLogPath;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public String getHistoryAuthorIndex() {
        return historyAuthorIndex;
    }

    public String getHistoryAuthorType() {
        return historyAuthorType;
    }

    public String getHistoryPaperIndex() {
        return historyPaperIndex;
    }

    public String getHistoryPaperType() {
        return historyPaperType;
    }

    public String getHistoryReferenceIndex() {
        return historyReferenceIndex;
    }

    public String getHistoryReferenceType() {
        return historyReferenceType;
    }

    public String getPairFilePath() {
        return pairFilePath;
    }

    public long getPreGetNum() {
        return preGetNum;
    }

    public int getPerPageNum() {
        return perPageNum;
    }

    public String getDictStatisticsPath() {
        return dictStatisticsPath;
    }

    public String getEvolutionFolderPath() {
        return evolutionFolderPath;
    }

    public int getEVOLUTION_SUMMARY_PAPER() {
        return EVOLUTION_SUMMARY_PAPER;
    }

    public int getEVOLUTION_SUMMARY_TOPIC() {
        return EVOLUTION_SUMMARY_TOPIC;
    }
}
