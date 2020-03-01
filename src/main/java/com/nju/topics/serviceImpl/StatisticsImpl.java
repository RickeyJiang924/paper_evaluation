package com.nju.topics.serviceImpl;

import com.nju.topics.config.Config;
import com.nju.topics.dao.AuthorService;
import com.nju.topics.dao.IndexInfoService;
import com.nju.topics.dao.PaperService;
import com.nju.topics.domain.Segment;
import com.nju.topics.domain.StatisticsInfo;
import com.nju.topics.entity.AuthorEntity;
import com.nju.topics.entity.IndexEntity;
import com.nju.topics.service.Segments;
import com.nju.topics.service.StatisticsService;
import com.nju.topics.utils.MapValueComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;


@Component
public class StatisticsImpl implements StatisticsService {

    @Autowired
    private Config config;

    @Autowired
    private Segments segmentsService;


    @Autowired
    private AuthorService authorService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private IndexInfoService indexInfoService;


    @Override
    public List getAllStatisticsDictNames() {
//        ArrayList<String> allStatistics=new ArrayList<>();
//
//        allStatistics.add("historyES");
//
//        File statisticsFolder=new File(config.getStatisticsFolder());
//        if (statisticsFolder.exists() && statisticsFolder.isDirectory()){
//            String[] allDirectories=statisticsFolder.list();
//            if (allDirectories==null || allDirectories.length<1){
//                return null;
//            }
//
//            for(String eachFolder :allDirectories){
//                allStatistics.add(eachFolder);
//            }
//
//            return allStatistics;
//        }
//        return allStatistics;
        return indexInfoService.getIndexInfos();
    }

    @Override
    public ArrayList<StatisticsInfo> getAllKeyWords(String dictName, String attributeName) {
        ArrayList<StatisticsInfo> statisticsInfos=new ArrayList<>();
        Map<String,Integer> allKeyWords=new HashMap<>();
        Map<String,Integer> sortMap=new HashMap<>();


        String filePath=config.getStatisticsFolder()+dictName+"/"+dictName+"_"+attributeName;
        File statisticsFile=null;

        try {
            statisticsFile= ResourceUtils.getFile(filePath);
            if (statisticsFile.exists()){
                InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(statisticsFile),"utf-8");
                BufferedReader statisticsReader=new BufferedReader(inputStreamReader);
                String line=null;
                while ((line=statisticsReader.readLine())!=null){
                    String[] specialList=line.split("@@");
                    String keyWord=specialList[0];
                    if (specialList[1].equals("")){
                        specialList[1]="0";
                    }
                    Integer num=0;
                    try {
                        num=Integer.parseInt(specialList[1]);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        System.err.println(line);
                    }
                    allKeyWords.put(keyWord,num);
                }

                sortMap=new TreeMap<String,Integer>(new MapValueComparator(allKeyWords));
                sortMap.putAll(allKeyWords);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for (Map.Entry<String,Integer> entry:sortMap.entrySet()){
            boolean inNone=false;
            for(int i=0;i<config.noStatisticsWords.length;i++){
                if (config.noStatisticsWords[i].equals(entry.getKey())){
                    inNone=true;
                    break;
                }
            }
            if (!inNone){
                StatisticsInfo statisticsInfo=new StatisticsInfo(entry.getKey(),entry.getValue());
                statisticsInfos.add(statisticsInfo);
            }

        }
        return statisticsInfos;
    }

    @Override
    public ArrayList<StatisticsInfo> getAllKeyWordsByES(String indexName){
        ArrayList<StatisticsInfo> statisticsInfos=new ArrayList<>();
        Map<String,Integer> allKeyWords=paperService.getAllTagsAndTimes(indexName);
        Map<String,Integer> sortMap=new HashMap<>();

        List<Segment> segList = (segmentsService.getSegmentsNoneCondition(indexName+".txt"));

        for(int i=0;i<segList.size();i++){
            HashMap<String,Integer> tempSegMap=new HashMap<>();
            for(int j=0;j<segList.get(i).getSegments().size();j++) {
                String seg =((segList.get(i).getSegments()).get(j)).trim();
                if (!tempSegMap.containsKey(seg)){
                    tempSegMap.put(seg,new Integer(1));
                }
            }
            for (Map.Entry<String,Integer> tempSegEntry:tempSegMap.entrySet()){
                if (!allKeyWords.containsKey(tempSegEntry.getKey())){
                    allKeyWords.put(tempSegEntry.getKey(),tempSegEntry.getValue().intValue());
                }else{
                    allKeyWords.put((tempSegEntry.getKey()).toString(),allKeyWords.get(tempSegEntry.getKey()).intValue()+tempSegEntry.getValue().intValue() );
                }
            }
        }

        sortMap=new TreeMap<String,Integer>(new MapValueComparator(allKeyWords));
        sortMap.putAll(allKeyWords);

        for (Map.Entry<String,Integer> entry:sortMap.entrySet()){
            boolean inNone=false;
            for(int i=0;i<config.noStatisticsWords.length;i++){
                if (config.noStatisticsWords[i].equals(entry.getKey()) || entry.getKey().length()==1){
                    inNone=true;
                    break;
                }
            }
            if (!inNone){
                StatisticsInfo statisticsInfo=new StatisticsInfo(entry.getKey(),entry.getValue());
                statisticsInfos.add(statisticsInfo);
            }
        }

        return statisticsInfos;
    }


    @Override
    public ArrayList<StatisticsInfo> getOneAttribute(String dictName, String attributeName, String keyWord) {
        ArrayList<StatisticsInfo> statisticsInfos=new ArrayList<>();
        Map<String,Integer> allKeyWords=new HashMap<>();
        Map<String,Integer> sortMap=new HashMap<>();


        String filePath=config.getStatisticsFolder()+dictName+"/"+dictName+"_"+attributeName;
        File statisticsFile=null;

        try {
            statisticsFile= ResourceUtils.getFile(filePath);
            if (statisticsFile.exists()){
                InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(statisticsFile),"utf-8");
                BufferedReader statisticsReader=new BufferedReader(inputStreamReader);
                String line=null;
                while ((line=statisticsReader.readLine())!=null){

//                    早年-2-王以欣:1;李开军:1;
                    String[] specialList=line.split("@@");
                    if (specialList[0].equals(keyWord)){
                        String specificAttributes=specialList[2];

//                        王以欣:1;李开军:1;
                        String[] attributesCount=specificAttributes.split(";");
                        if (attributesCount.length>0){
                            for(String eachCount:attributesCount){
                                if (eachCount!=null && eachCount.length()>0){
                                    String[] splitAttributes=eachCount.split(":");
                                    String attri=splitAttributes[0];
                                    Integer attriNum=Integer.parseInt(splitAttributes[1]);
                                    allKeyWords.put(attri,attriNum);
                                }
                            }
                        }
                    }
                }

                sortMap=new TreeMap<String,Integer>(new MapValueComparator(allKeyWords));
                sortMap.putAll(allKeyWords);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for (Map.Entry<String,Integer> entry:sortMap.entrySet()){

            boolean inNone=false;
            for(int i=0;i<config.noStatisticsWords.length;i++){
                if (config.noStatisticsWords[i].equals(entry.getKey())){
                    inNone=true;
                    break;
                }
            }
            if (!inNone){
                StatisticsInfo statisticsInfo=new StatisticsInfo(entry.getKey(),entry.getValue());
                statisticsInfos.add(statisticsInfo);
            }
        }
        return statisticsInfos;
    }

    @Override
    public ArrayList<StatisticsInfo> getAuthorAttributeByES(String indexName,String keyWord){
        ArrayList<StatisticsInfo> statisticsInfos=new ArrayList<>();
        Map<String,Integer> allKeyWords=new HashMap<>();
        Map<String,Integer> sortMap=new HashMap<>();

        List<AuthorEntity> list = authorService.getAuthorByPaper(indexName,keyWord);

        for(AuthorEntity author:list){
            String authorName = author.getAuthorName();
            if(allKeyWords.containsKey(authorName)){
                int val = allKeyWords.get(authorName) + 1;
                allKeyWords.put(authorName, val);
            }else{
                allKeyWords.put(authorName, 1);
            }
        }
//        System.out.println("共找到多少作者："+allKeyWords.size());

        sortMap=new TreeMap<String,Integer>(new MapValueComparator(allKeyWords));
        sortMap.putAll(allKeyWords);

        for (Map.Entry<String,Integer> entry:sortMap.entrySet()){
            StatisticsInfo statisticsInfo=new StatisticsInfo(entry.getKey(),entry.getValue());
            statisticsInfos.add(statisticsInfo);
        }

        return statisticsInfos;
    }

    @Override
    public ArrayList<StatisticsInfo> getInstitutionAttributeByES(String indexName,String keyWord){
        ArrayList<StatisticsInfo> statisticsInfos=new ArrayList<>();
        Map<String,Integer> allKeyWords=new HashMap<>();
        Map<String,Integer> sortMap=new HashMap<>();

        List<AuthorEntity> list = authorService.getAuthorByPaper(indexName,keyWord);

        for(AuthorEntity author:list){
            String insName = author.getAuthorInstitution();
            if(allKeyWords.containsKey(insName)){
                int val = allKeyWords.get(insName) + 1;
                allKeyWords.put(insName, val);
            }else{
                allKeyWords.put(insName, 1);
            }
        }

        sortMap=new TreeMap<String,Integer>(new MapValueComparator(allKeyWords));
        sortMap.putAll(allKeyWords);

        for (Map.Entry<String,Integer> entry:sortMap.entrySet()){

            StatisticsInfo statisticsInfo=new StatisticsInfo(entry.getKey(),entry.getValue());
            statisticsInfos.add(statisticsInfo);

        }

        return statisticsInfos;
    }


    @Override
    public Map<String, Map<String, Integer>> getAllAtrribute(String dictName, String attributeName) {
        Map<String, Map<String, Integer>> allKeyWords=new HashMap<String, Map<String, Integer>>();
        return null;
    }
}
