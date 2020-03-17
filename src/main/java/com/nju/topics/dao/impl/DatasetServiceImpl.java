package com.nju.topics.dao.impl;

import com.nju.topics.dao.DatasetService;
import com.nju.topics.dao.Tools;
import com.nju.topics.entity.DatasetEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatasetServiceImpl implements DatasetService {
    @Autowired
    private Tools tools;

    @Override
    public ArrayList<DatasetEntity> getDatasetByModel(String indexName, String modelName){
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.should(QueryBuilders.termQuery("modelName.keyword", modelName));
        SearchHits hits = tools.getSearchHits(indexName,boolBuilder,0,20000);
        return transferHitToDataset(hits);
    }

    @Override
    public DatasetEntity getDatasetByName(String indexName, String datasetName){
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.should(QueryBuilders.termQuery("datasetName.keyword", datasetName));
        SearchHits hits = tools.getSearchHits(indexName,boolBuilder,0,20000);
        return transferHitToDataset(hits).get(0);
    }

    private ArrayList<DatasetEntity> transferHitToDataset(SearchHits hits){
        ArrayList<DatasetEntity> datasetEntities=new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit s:searchHits){
            DatasetEntity datasetEntity=new DatasetEntity();
            datasetEntity.setDatasetName(String.valueOf(s.getSourceAsMap().get("datasetName")));
            datasetEntity.setDatasetPath(String.valueOf(s.getSourceAsMap().get("datasetPath")));
            datasetEntity.setModelName(String.valueOf(s.getSourceAsMap().get("modelName")));
            datasetEntities.add(datasetEntity);
        }
        return datasetEntities;
    }
}
