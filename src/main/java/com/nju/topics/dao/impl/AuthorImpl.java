package com.nju.topics.dao.impl;

import com.nju.topics.dao.AuthorService;
import com.nju.topics.dao.PaperService;
import com.nju.topics.dao.Tools;
import com.nju.topics.domain.StatisticsInfo;
import com.nju.topics.entity.AuthorEntity;
import com.nju.topics.entity.PapersEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class AuthorImpl implements AuthorService {
    @Autowired
    private Tools tools;
    @Autowired
    private PaperService paperService;

    @Override
    public ArrayList<StatisticsInfo> getRelativeAuthorByAuthor(String indexName, String authorName) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery("ZZMC.keyword", authorName));

        SearchHits hits = tools.getSearchHits(indexName+"authors",boolBuilder,0,20000);
        SearchHit[] searchHits = hits.getHits();

        ArrayList<String> snos=new ArrayList<>();
        for (SearchHit s:searchHits){
            boolean hasExistSno=false;
            String sno=String.valueOf(s.getSourceAsMap().get("SNO"));
            for(String existSNO:snos){
                if (existSNO.equals(sno)){
                    hasExistSno=true;
                    break;
                }
            }
            if (!hasExistSno){

                snos.add(sno);
            }

        }

        BoolQueryBuilder boolBuilder2 = QueryBuilders.boolQuery();
        boolBuilder2.must(QueryBuilders.termsQuery("SNO.keyword", snos));
        boolBuilder2.mustNot(QueryBuilders.matchQuery("ZZMC.keyword", authorName));

        ArrayList<StatisticsInfo> relativeAuthors=new ArrayList<>();

        SearchHits hits2 = tools.getSearchHits(indexName+"authors",boolBuilder2);
        SearchHit[] searchHits2 = hits2.getHits();
        List<HashMap<String,String>> snoAuthorMapList=new ArrayList<>();
        for(SearchHit otherAuthor:searchHits2){
            boolean hasSameAuthor=false;
            String otherAuthorSNO=String.valueOf(otherAuthor.getSourceAsMap().get("SNO"));
            String otherAuthorName=String.valueOf(otherAuthor.getSourceAsMap().get("ZZMC"));
            HashMap<String,String> tempMap=new HashMap<>();
            tempMap.put(otherAuthorSNO,otherAuthorName);
            boolean hasSameMap=false;
            if (snoAuthorMapList!=null && snoAuthorMapList.size()>0){
                for(HashMap<String,String> existMap:snoAuthorMapList){
                    if (existMap.containsKey(otherAuthorSNO) && existMap.containsValue(otherAuthorName)){
                        hasSameMap=true;
                        break;
                    }
                }
            }

            if (!hasSameMap){
                snoAuthorMapList.add(tempMap);
                if (relativeAuthors!=null && relativeAuthors.size()>0){
                    for(StatisticsInfo st:relativeAuthors){
                        if ((st.getKey()).equals(otherAuthorName)){
                            hasSameAuthor=true;
                            st.setNum(st.getNum()+1);
                            break;
                        }
                    }

                    if (!hasSameAuthor){
                        StatisticsInfo statisticsInfo=new StatisticsInfo(otherAuthorName,1);
                        relativeAuthors.add(statisticsInfo);
                    }
                }else{
                    StatisticsInfo statisticsInfo=new StatisticsInfo(otherAuthorName,1);
                    relativeAuthors.add(statisticsInfo);
                }
            }
        }

        return relativeAuthors;
    }

    @Override
    public List getAuthorByPaper(String indexName, String keyWord) {
        List<AuthorEntity> authorEntities=new ArrayList<>();

        ArrayList<String> snos=new ArrayList<>();

        List<PapersEntity> paperList = paperService.getPaperByName(indexName,keyWord);

        for (PapersEntity paper: paperList){
            snos.add(paper.getSNO());
        }

        authorEntities=getAuthorsBySNOs(indexName,snos);
        return authorEntities;
    }

    @Override
    public List getAuthorInfoByName(String indexName, String authorName) {
        List<AuthorEntity> authorEntities=new ArrayList<>();

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery("ZZMC.keyword", authorName));

        SearchHits hits = tools.getSearchHits(indexName+"authors",boolBuilder,0,20000);

        return transferHitsToAuthors(hits);
    }

    public List getAuthorsBySNOs(String indexName,ArrayList<String> snos){
        List<AuthorEntity> authorEntities=new ArrayList<>();

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.termsQuery("SNO.keyword", snos));

        SearchHits hits = tools.getSearchHits(indexName+"authors",boolBuilder,0,20000);

        return transferHitsToAuthors(hits);
    }

    public List transferHitsToAuthors(SearchHits hits){
        List<AuthorEntity> authorEntities=new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit s:searchHits){
            AuthorEntity authorEntity=new AuthorEntity();
            authorEntity.setAuthorName(String.valueOf(s.getSourceAsMap().get("ZZMC")));
            authorEntity.setAuthorInstitution(String.valueOf(s.getSourceAsMap().get("JGMC")));
            authorEntity.setAuthorDepartment(String.valueOf(s.getSourceAsMap().get("TXDZ")));
            boolean hasSameAuthor=false;
            if (authorEntities!=null && authorEntities.size()>0){
                for (AuthorEntity ha:authorEntities){
                    if (ha.getAuthorName().equals(authorEntity.getAuthorName()) &&
                            ha.getAuthorDepartment().equals(authorEntity.getAuthorDepartment()) &&
                            ha.getAuthorInstitution().equals(authorEntity.getAuthorInstitution())){
                        hasSameAuthor=true;
                        break;
                    }
                }
            }
            if (!hasSameAuthor){
                authorEntities.add(authorEntity);
            }
        }
        return authorEntities;
    }
}
