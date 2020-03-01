package com.nju.topics.dao;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Tools {

    @Autowired
    private RestHighLevelClient client;

    public SearchHits getSearchHits(String indexName, BoolQueryBuilder boolBuilder, int begin, int end){
        System.err.println("在getSearchHits(String indexName, BoolQueryBuilder boolBuilder, int begin, int end)中寻找index为"+indexName+"的数据");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(begin);
        sourceBuilder.size(begin+end); // 获取记录数，默认10
//        sourceBuilder.fetchSource(new String[] { "LYPM", "BYC","Tags" }, new String[] {}); // 第一个是获取字段，第二个是过滤的字段，默认获取全部
        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        SearchResponse response=new SearchResponse();
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }

        SearchHits hits = response.getHits();
        return hits;
    }
    public SearchHits getSearchHits(String indexName, BoolQueryBuilder boolBuilder){
        System.err.println("在getSearchHits(String indexName, BoolQueryBuilder boolBuilder)中寻找index为"+indexName+"的数据");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        SearchResponse response=new SearchResponse();
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }

        SearchHits hits = response.getHits();
        return hits;
    }

    public SearchHits getSearchHits(String indexName, SearchSourceBuilder sourceBuilder, int begin, int end){
        System.err.println("在getSearchHits(String indexName, SearchSourceBuilder sourceBuilder, int begin, int end)中寻找index为"+indexName+"的数据");
        sourceBuilder.from(begin);
        sourceBuilder.size(begin+end); // 获取记录数，默认10
        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        SearchResponse response=new SearchResponse();
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }

        SearchHits hits = response.getHits();
        return hits;
    }

}
