package com.nju.topics.web;

import com.nju.topics.config.Config;
import com.nju.topics.dao.PaperService;
import com.nju.topics.domain.PageDataInfo;
import com.nju.topics.domain.TagInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private Config config;

    /***
     *
     * @param
     * @return
     */
    @RequestMapping("/bulkAddTags/{indexName}/{addInfo}")
    @ResponseBody
    public String addTags(@PathVariable("indexName")String indexName,@PathVariable("addInfo")String tagInfo){

        String[] tagInfoSplit=tagInfo.split(":");
        String tag=tagInfoSplit[0];
        String origins=tagInfoSplit[1];

        ArrayList<String> originList=new ArrayList<>();
        String[] originSplit=origins.split("-");
        for(String eachOriginSplit:originSplit){

            if (eachOriginSplit.length()>0){
                originList.add(eachOriginSplit);
            }
        }
        paperService.bulkUpdateTags(indexName,originList,tag);
        return "success";
    }

    @RequestMapping("/addTitleTag/{indexName}/{titleTagInfo}")
    @ResponseBody
    public String addTitleTag(@PathVariable("indexName")String indexName,@PathVariable("titleTagInfo")String  titleTagInfo){
        String[] titleTagInfoSplit=titleTagInfo.split(":");
        String titleName=titleTagInfoSplit[0];
        String[] titleTagSplit=titleTagInfoSplit[1].split("-");
        ArrayList<String> tagList=new ArrayList<>();
        for(String eachTagSplit:titleTagSplit){
            if (eachTagSplit.length()>0){
                tagList.add(eachTagSplit);
            }
        }

        paperService.bulkUpdateTagsByTitle(indexName,titleName,tagList);
        return "success";
    }

    @RequestMapping("/getAllTags/{indexName}")
    @ResponseBody
    public PageDataInfo getAllTags(@PathVariable("indexName")String indexName){
//        return historyPapersSerivce.getAllTagInfos();
        PageDataInfo pageDataInfo=new PageDataInfo();
        long totalRecords=paperService.getTotalRecordsNum(indexName+"papers");
        int page=(int)Math.ceil(totalRecords*1.0/config.getPerPageNum());
        pageDataInfo.setPageNum(page);
        ArrayList<TagInfo> tagInfos=paperService.getTagInfosByBeginEnd(indexName,0,config.getPerPageNum());
        pageDataInfo.setPageData(tagInfos);
        return pageDataInfo;
    }

    @RequestMapping("/getTagByPage/{indexName}/{pageNum}")
    @ResponseBody
    public List<TagInfo> getTagInfoByPage(@PathVariable("indexName")String indexName,@PathVariable("pageNum")String pageNum){
        int pageNumInt=Integer.parseInt(pageNum);
        int begin=(pageNumInt-1)*config.getPerPageNum();
        int end=begin+config.getPerPageNum();
        return paperService.getTagInfosByBeginEnd(indexName,begin,end);
    }

    @RequestMapping("/getTagInfosByTag/{indexName}/{tagName}")
    @ResponseBody
    public ArrayList<TagInfo> getTagInfosByTagName(@PathVariable("indexName")String indexName,@PathVariable("tagName")String tagName){
        return paperService.getTagInfosByTag(indexName,tagName);
    }

    @RequestMapping("/getTagList/{indexName}")
    @ResponseBody
    public ArrayList<String> getTagList(@PathVariable("indexName")String indexName){
        return paperService.getAllTags(indexName);
    }
}
