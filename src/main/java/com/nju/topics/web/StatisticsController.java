package com.nju.topics.web;

import com.nju.topics.dao.AuthorService;
import com.nju.topics.dao.PaperService;
import com.nju.topics.domain.StatisticsInfo;
import com.nju.topics.domain.TreeDataInfo;
import com.nju.topics.entity.AuthorEntity;
import com.nju.topics.entity.IndexEntity;
import com.nju.topics.entity.PapersEntity;
import com.nju.topics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private AuthorService authorService;
//    @Autowired
//    private HistoryPapersSerivce historyPapersSerivce;
//    @Autowired
//    private HistoryAuthorsService historyAuthorsService;

    @RequestMapping("/getAllDirections")
    @ResponseBody
    public List getAllDirections(){
        return statisticsService.getAllStatisticsDictNames();
    }

    @RequestMapping("/getAllKeyWords/{indexName}/{attribute}")
    @ResponseBody
    public ArrayList<StatisticsInfo> getAllKeyWords(@PathVariable("indexName")String indexName, @PathVariable("attribute")String attribute){
        if(indexName.equals("historyES")){
            return statisticsService.getAllKeyWordsByES("history");
        }else{
            return statisticsService.getAllKeyWords(indexName,attribute);
        }
    }
    @RequestMapping("/getAllKeyWords/{indexName}")
    @ResponseBody
    public ArrayList<StatisticsInfo> getAllKeyWords(@PathVariable("indexName")String indexName){
        return statisticsService.getAllKeyWordsByES(indexName);
    }

    @RequestMapping("/getOneKeyAttribute/{indexName}/{attributeName}/{keyword}")
    @ResponseBody
    public ArrayList<StatisticsInfo> getOneKeyAttribute(@PathVariable("indexName")String indexName, @PathVariable("attributeName")String attributeName,
                                                        @PathVariable("keyword")String keyword){
        if(attributeName.equals("author")){
            return statisticsService.getAuthorAttributeByES(indexName,keyword);
        }else if(attributeName.equals("institution")){
            return  statisticsService.getInstitutionAttributeByES(indexName,keyword);
        }else{
            return null;
        }
//        if(dictName.equals("historyES")){
//            if(attributeName.equals("author")){
//                return statisticsService.getAuthorAttributeByES(keyword);
//            }else if(attributeName.equals("institution")){
//                return  statisticsService.getInstitutionAttributeByES(keyword);
//            }else{
//                return null;
//            }
//        }else{
//            return statisticsService.getOneAttribute(dictName,attributeName,keyword);
//        }

    }

    @RequestMapping("/getPapersByName/{indexName}/{paperName}")
    @ResponseBody
    public List<PapersEntity> getPapersByName(@PathVariable("indexName")String indexName,@PathVariable("paperName")String paperName){
        return paperService.getPaperByName(indexName,paperName);
    }

    @RequestMapping("/getPapersByAuthor/{indexName}/{author}")
    @ResponseBody
    public List<PapersEntity> getPapersByAuthor(@PathVariable("indexName")String indexName,@PathVariable("author")String author){
        return paperService.getPapersByAuthor(indexName,author);
    }

    @RequestMapping("/getAuthorInfo/{indexName}/{author}")
    @ResponseBody
    public List<AuthorEntity> getAuthorInfo(@PathVariable("indexName")String indexName, @PathVariable("author")String author){
        return authorService.getAuthorInfoByName(indexName,author);
    }

    @RequestMapping("/getAuthorPapersTreeData/{indexName}/{author}")
    @ResponseBody
    public TreeDataInfo getAuthorInfoTreeData(@PathVariable("indexName")String indexName,@PathVariable("author")String author){
        return paperService.getPapersByAuthorAggratedByYear(indexName,author);
    }

    @RequestMapping("/getRelativeAuthors/{indexName}/{author}")
    @ResponseBody
    public ArrayList<StatisticsInfo> getRelativeAuthors(@PathVariable("indexName")String indexName,@PathVariable("author")String author){
        return authorService.getRelativeAuthorByAuthor(indexName,author);
    }

}
