package com.nju.topics.dao.impl;

import com.nju.topics.dao.IndexInfoService;
import com.nju.topics.dao.Tools;
import com.nju.topics.entity.IndexEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IndexInfoImpl implements IndexInfoService {
    @Autowired
    private Tools tools;

    @Override
    public List getIndexInfos() {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchAllQuery());

        SearchHits hits = tools.getSearchHits("indexrecords",boolBuilder,0,200);
        SearchHit[] searchHits = hits.getHits();

        List indexEntities=new ArrayList<>();
        for (SearchHit s:searchHits){
            String name=String.valueOf(s.getSourceAsMap().get("name"));
            String alias=String.valueOf(s.getSourceAsMap().get("alias"));
            System.err.println(name+":"+alias);
            IndexEntity indexEntity=new IndexEntity();
            indexEntity.setIndexName(name);
            indexEntity.setIndexAlias(alias);
            indexEntities.add(indexEntity);
        }
        return indexEntities;
    }
}
