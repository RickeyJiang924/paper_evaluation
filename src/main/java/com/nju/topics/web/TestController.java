package com.nju.topics.web;

import com.nju.topics.config.Config;
import com.nju.topics.domain.EvolutionDataInfo;
import com.nju.topics.service.LDAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @Autowired
//    private HistoryRepository historyRepository;

//    @RequestMapping("")
//    public String test(){
//        return "Test";
//    }

//    @RequestMapping("/getPapersByName/{paperName}")
//    public List<HistoryPapersEntity> getPapersByName(@PathVariable("paperName")String paperName){
//        return historyPapersSerivce.getHistoryPaperByName(paperName);
//    }
//
//    @RequestMapping("/getPapersByAuthor/{author}")
//    public List<HistoryPapersEntity> getPapersByAuthor(@PathVariable("author")String author){
//        return historyPapersSerivce.getHistoryPapersByAuthor(author);
//    }
//
//    @RequestMapping("/getAuthorInfo/{author}")
//    public List<HistoryAuthorEntity> getAuthorInfo(@PathVariable("author")String author){
//        return historyAuthorsService.getAuthorInfoByName(author);
//    }
//
//    @RequestMapping("/getAuthorPapersTreeData/{author}")
//    public TreeDataInfo getAuthorInfoTreeData(@PathVariable("author")String author){
//        return historyPapersSerivce.getHistoryPapersByAuthorAggratedByYear(author);
//    }
//
//    @RequestMapping("/getRelativeAuthors/{author}")
//    public ArrayList<StatisticsInfo> getRelativeAuthors(@PathVariable("author")String author){
//        return historyAuthorsService.getRelativeAuthorByAuthor(author);
//    }
//
//    @RequestMapping("/test/modifyTags")
//    public void modifyTags(){
//        ArrayList<String> origin=new ArrayList<>();
//        origin.add("民国");
//        historyPapersSerivce.bulkUpdateTags(origin,"民国时期");
//    }
//
//    @RequestMapping("/test/getAllTags")
//    public ArrayList<TagInfo> getAllTags(){
//        return historyPapersSerivce.getAllTagInfos();
//    }
//    @RequestMapping("/test/getAllTags/{tagName}")
//    public ArrayList<TagInfo> getAllTagsByTag(@PathVariable("tagName")String tagName){
//        return historyPapersSerivce.getTagInfosByTag(tagName);
//    }

//    @RequestMapping("/testSave")
//    public String testSave(){
//        HistoryPaperInfo historyPaperInfo=new HistoryPaperInfo();
//        historyPaperInfo.setPaperId("11G0532010050001");
//        historyPaperInfo.setPaperName("中国近代资本主义的学术研究与中学历史教学");
//        ArrayList<String> authors=new ArrayList<>();
//        authors.add("薛伟强");
//        authors.add("高景龙");
////        historyPaperInfo.setPaperAuthors(authors);
//        historyPaperInfo.setPaperVenueNo("11G0532010050");
//
//        HistoryPaperInfo historyPaperInfo1=new HistoryPaperInfo();
//        historyPaperInfo1.setPaperId("11G0532010050002");
//        historyPaperInfo1.setPaperName("中国近代资本主义的学术研究与中学历史教学1");
////        historyPaperInfo.setPaperAuthors(authors);
//        historyPaperInfo1.setPaperVenueNo("11G05320100502");
//
//        historyRepository.save(historyPaperInfo1);
//
//        return "success";
//    }


//    @Autowired
//    private LDAService ldaService;
//
//    @Autowired
//    private Config config;
//
//    @RequestMapping("/lda/getJson")
//    public String getLdaJson(){
//        return ldaService.getLDARelationString(config.getEvolutionFolderPath());
//    }
//
//    @RequestMapping("/lda/getEvolutionData/{id}")
//    public EvolutionDataInfo getEvolutionData(@PathVariable("id")String id){
//        String parentPath=config.getEvolutionFolderPath();
//        return ldaService.getEvolutionData(parentPath,id);
//    }
}
