package com.nju.topics.dao.impl;

import com.nju.topics.config.Config;
import com.nju.topics.dao.PaperService;
import com.nju.topics.dao.Tools;
import com.nju.topics.domain.TagInfo;
import com.nju.topics.domain.TreeDataInfo;
import com.nju.topics.entity.PapersEntity;
import com.nju.topics.utils.MapValueComparator;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class PaperImpl implements PaperService {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private Tools tools;
    @Autowired
    private Config config;

    @Override
    public List getPaperByName(String indexName, String paperName) {
        List papers=new ArrayList<>();

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.should(QueryBuilders.wildcardQuery("LYPM.keyword", "*"+paperName+"*"));
        boolBuilder.should(QueryBuilders.wildcardQuery("BYC.keyword", "*"+paperName+"*"));
        boolBuilder.should(QueryBuilders.wildcardQuery("Tags", "*"+paperName+"*"));

        SearchHits hits = tools.getSearchHits(indexName+"papers",boolBuilder,0,20000);
        return transferHitToPapers(hits);
    }

    @Override
    public List getPapersByAuthor(String indexName, String author) {
        List<PapersEntity> papers=new ArrayList<>();

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.fuzzyQuery("ZZMC.keyword", author));

        SearchHits hits = tools.getSearchHits(indexName+"authors",boolBuilder,0,20000);
        SearchHit[] searchHits = hits.getHits();
        ArrayList<String> snos=new ArrayList<>();
        for (SearchHit s:searchHits){
            snos.add(String.valueOf(s.getSourceAsMap().get("SNO")));
        }
        papers=getPapersBySNOS(indexName,snos);
        return papers;
    }

    @Override
    public TreeDataInfo getPapersByAuthorAggratedByYear(String indexName, String author) {
        TreeDataInfo resTreeData=new TreeDataInfo();
        resTreeData.setName(author);
        ArrayList<TreeDataInfo> resChildren=new ArrayList<>();

        List<PapersEntity> papersEntities=getPapersByAuthor(indexName,author);
        ArrayList<String> years=new ArrayList<>();
//        获取所有有文章发表的年份
        for (PapersEntity hpe:papersEntities){
            String year=hpe.getNIAN();
            boolean hasNian=false;
            if (years!=null && years.size()>0){
                for(String existYear:years){
                    if (existYear.equals(year)){
                        hasNian=true;
                        break;
                    }
                }
            }

            if (!hasNian){
                years.add(year);
            }
        }

        for(String eachYear:years){
//            System.err.println(eachYear);
            TreeDataInfo yearTreeData=new TreeDataInfo();
            yearTreeData.setName(eachYear);
            ArrayList<TreeDataInfo> yearChildren=new ArrayList<>();
            for(PapersEntity hpeByYear:papersEntities){
                if (hpeByYear.getNIAN().equals(eachYear)){
                    TreeDataInfo specialNianTreeData=new TreeDataInfo();
                    specialNianTreeData.setName(hpeByYear.getLYPM());
                    specialNianTreeData.setChildren(new ArrayList<>());
                    yearChildren.add(specialNianTreeData);
                }
            }
            yearTreeData.setChildren(yearChildren);
            resChildren.add(yearTreeData);
        }

        resTreeData.setChildren(resChildren);
        return resTreeData;
    }

    @Override
    public void bulkUpdateTags(String indexName, ArrayList<String> origin, String tag) {
        String pairFilePath=config.getPairFilePath();
        File pairFile=new File(pairFilePath);
        try {
            if (!pairFile.exists()){
                pairFile.createNewFile();
            }
            OutputStreamWriter pairWriter = new OutputStreamWriter(new FileOutputStream(pairFile,true));
            BufferedWriter pairBufferedWriter = new BufferedWriter(pairWriter);
            pairBufferedWriter.write(tag+":");
            for(String originS:origin){
                pairBufferedWriter.write(originS+",");
            }
            pairBufferedWriter.write("\r\n");
            pairBufferedWriter.flush();
            pairBufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

        for(String originName:origin){
            boolBuilder.should(QueryBuilders.wildcardQuery("LYPM.keyword", "*"+originName+"*"));
            boolBuilder.should(QueryBuilders.wildcardQuery("BYC.keyword", "*"+originName+"*"));

        }

        SearchHits hits = tools.getSearchHits(indexName,boolBuilder,0,20000);
        SearchHit[] searchHits = hits.getHits();
        ArrayList<String> snos=new ArrayList<>();
        BulkRequest updateBulkRequest=new BulkRequest();
        boolean hasNew=false;
        for (SearchHit s:searchHits){
            if (s.getSourceAsMap().get("Tags")==null || String.valueOf(s.getSourceAsMap().get("Tags")).contains(tag)==false){
                try {
                    XContentBuilder newTagBuilder= XContentFactory.jsonBuilder();
                    newTagBuilder.startObject();
                    {
                        if (s.getSourceAsMap().get("Tags")!=null){
                            newTagBuilder.field("Tags",(s.getSourceAsMap().get("Tags"))+tag+"/");
                        }else{
                            newTagBuilder.field("Tags",tag+"/");
                        }
                    }
                    hasNew=true;
                    newTagBuilder.endObject();
//                    UpdateRequest updateRequest=new UpdateRequest(indexName+"papers",)
                    UpdateRequest updateRequest=new UpdateRequest(indexName+"papers",config.getHistoryPaperType(),s.getId())
                            .doc(newTagBuilder);
                    updateBulkRequest.add(updateRequest);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

        if (hasNew){
            try {
                BulkResponse updateBulkResponse=client.bulk(updateBulkRequest,RequestOptions.DEFAULT);
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }
    }

    @Override
    public void bulkUpdateTagsByTitle(String indexName, String originName, ArrayList<String> tags) {
        String tagString="";
        for(String eachTag:tags){
            tagString=tagString+eachTag+"/";
        }

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();


        boolBuilder.should(QueryBuilders.wildcardQuery("LYPM.keyword", "*"+originName+"*"));

        SearchHits hits = tools.getSearchHits(indexName+"papers",boolBuilder,0,20000);
        SearchHit[] searchHits = hits.getHits();
        ArrayList<String> snos=new ArrayList<>();
        BulkRequest updateBulkRequest=new BulkRequest();
        boolean hasNew=false;
        for (SearchHit s:searchHits){
            try {
                XContentBuilder newTagBuilder= XContentFactory.jsonBuilder();
                newTagBuilder.startObject();
                {
                    if (s.getSourceAsMap().get("Tags")!=null){
                        newTagBuilder.field("Tags",(s.getSourceAsMap().get("Tags"))+tagString);
                    }else{
                        newTagBuilder.field("Tags",tagString);
                    }
                }
                hasNew=true;
                newTagBuilder.endObject();
                UpdateRequest updateRequest=new UpdateRequest(indexName+"papers",config.getHistoryPaperType(),s.getId())
                        .doc(newTagBuilder);
                updateBulkRequest.add(updateRequest);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if (hasNew){
            try {
                BulkResponse updateBulkResponse=client.bulk(updateBulkRequest,RequestOptions.DEFAULT);
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<String> getAllTags(String indexName) {
        ArrayList<String> tags=new ArrayList<>();
        Map<String,Integer> countMap=getAllTagsAndTimes(indexName);
        Map<String,Integer> sortMap=new TreeMap<String,Integer>(new MapValueComparator(countMap));
        sortMap.putAll(countMap);
        for (Map.Entry<String,Integer> sortEntry:sortMap.entrySet()){
            tags.add(sortEntry.getKey());
        }
        return tags;
    }

    @Override
    public ArrayList<TagInfo> getAllTagInfos(String indexName) {
        ArrayList<TagInfo> resTagInfos=new ArrayList<>();

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchAllQuery());
        SearchHits hits=tools.getSearchHits(indexName+"papers",boolBuilder,0,20000);
        resTagInfos=dealTagInfoTool(hits);
        return resTagInfos;
    }

    @Override
    public ArrayList<TagInfo> getTagInfosByBeginEnd(String indexName, int begin, int end) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchAllQuery());
        SearchHits hits=tools.getSearchHits(indexName+"papers",boolBuilder,begin,end);
        return dealTagInfoTool(hits);
    }

    @Override
    public ArrayList<TagInfo> getTagInfosByTag(String indexName, String tagName) {
        ArrayList<TagInfo> resTagInfos=new ArrayList<>();
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.should(QueryBuilders.wildcardQuery("BYC.keyword","*"+tagName+"*"));
        boolBuilder.should(QueryBuilders.wildcardQuery("Tags","*"+tagName+"*"));
        SearchHits hits=tools.getSearchHits(indexName+"papers",boolBuilder,0,20000);
        resTagInfos=dealTagInfoTool(hits);
        return resTagInfos;
    }

    @Override
    public Map<String, Integer> getAllTagsAndTimes(String indexName) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchAllQuery());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.fetchSource(new String[] { "BYC","Tags" }, new String[] {}); // 第一个是获取字段，第二个是过滤的字段，默认获取全部

        SearchHits hits = tools.getSearchHits(indexName+"papers",sourceBuilder,0,20000);
        SearchHit[] searchHits = hits.getHits();
        Map<String,Integer> countMap=new HashMap<>();
        for (SearchHit s:searchHits){
            String BYC=String.valueOf(s.getSourceAsMap().get("BYC"));
            String Tags=String.valueOf(s.getSourceAsMap().get("Tags"));

            HashMap<String,Integer> tempHashMap=new HashMap<>();
            if (BYC!=null && BYC.length()>0){
                String[] BYCSplit=BYC.split("/");
                for(String eachByc:BYCSplit){
                    eachByc=eachByc.trim();
                    if (eachByc!=null && eachByc.length()>0 && !eachByc.equals("null")){
                        tempHashMap.put(eachByc,1);
                    }
                }
            }

            if (Tags!=null && Tags.length()>0){
                String[] TagsSplit=Tags.split("/");
                for(String eachTag:TagsSplit){
                    eachTag=eachTag.trim();
                    if (eachTag!=null && eachTag.length()>0 && !eachTag.equals("null")){
                        tempHashMap.put(eachTag,1);
                    }
                }
            }


            for(Map.Entry<String ,Integer> entry:tempHashMap.entrySet()){
                if (countMap.containsKey(entry.getKey())){
                    countMap.put(entry.getKey(),countMap.get(entry.getKey())+entry.getValue());
                }else{
                    countMap.put(entry.getKey(),entry.getValue());
                }
            }

        }


        return countMap;
    }

    @Override
    public long getTotalRecordsNum(String indexName) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchAllQuery());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        SearchHits hits = tools.getSearchHits(indexName,boolBuilder);
        return hits.getTotalHits();
    }



    public List<PapersEntity> getPapersBySNOS(String indexName,ArrayList<String> snos){
        List<PapersEntity> papersEntities=new ArrayList<>();

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.termsQuery("SNO.keyword", snos));

        SearchHits hits = tools.getSearchHits(indexName+"papers",boolBuilder,0,20000);

        return transferHitToPapers(hits);
    }

    public List transferHitToPapers(SearchHits hits){
        List<PapersEntity> papersEntities=new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit s:searchHits){
            PapersEntity papersEntity=new PapersEntity();
            papersEntity.setSNO(String.valueOf(s.getSourceAsMap().get("SNO")));
            papersEntity.setLYPM(String.valueOf(s.getSourceAsMap().get("LYPM")));
            papersEntity.setBLPM(String.valueOf(s.getSourceAsMap().get("BLPM")));
            papersEntity.setQKNO(String.valueOf(s.getSourceAsMap().get("QKNO")));
            papersEntity.setBYC(String.valueOf(s.getSourceAsMap().get("BYC")));
            papersEntity.setNIAN(String.valueOf(s.getSourceAsMap().get("NIAN")));
            papersEntities.add(papersEntity);
        }
        return papersEntities;
    }

    public ArrayList<TagInfo> dealTagInfoTool(SearchHits hits){

        ArrayList<TagInfo> resTagInfos=new ArrayList<>();

        SearchHit[] searchHits = hits.getHits();
        for (SearchHit s:searchHits){
            TagInfo tagInfo=new TagInfo();
            tagInfo.setTitle(String.valueOf(s.getSourceAsMap().get("LYPM")));

            String BYC=String.valueOf(s.getSourceAsMap().get("BYC"));
            String Tags=String.valueOf(s.getSourceAsMap().get("Tags"));
            String[] BYCSplit=BYC.split("/");
            HashMap<String,Integer> tempHashMap=new HashMap<>();
            for(String eachByc:BYCSplit){
                if (eachByc!=null && eachByc.length()>0 && !eachByc.equals("null")){
                    tempHashMap.put(eachByc,1);
                }
            }
            if (Tags!=null && Tags.length()>0){
                String[] TagsSplit=Tags.split("/");
                for(String eachTag:TagsSplit){
                    if (eachTag!=null && eachTag.length()>0 && !eachTag.equals("null")){
                        tempHashMap.put(eachTag,1);
                    }
                }
            }

            ArrayList<String> tags=new ArrayList<>();
            for(Map.Entry<String ,Integer> entry:tempHashMap.entrySet()){
                tags.add(entry.getKey());
            }

            tagInfo.setTags(tags);
            resTagInfos.add(tagInfo);
        }
        return resTagInfos;
    }


}
