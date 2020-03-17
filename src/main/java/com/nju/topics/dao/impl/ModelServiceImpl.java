package com.nju.topics.dao.impl;

import com.nju.topics.dao.ModelService;
import com.nju.topics.dao.Tools;
import com.nju.topics.entity.ModelEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class ModelServiceImpl implements ModelService{
    @Autowired
    private Tools tools;

    @Override
    public ModelEntity getModelByName(String indexName, String modelName){
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.should(QueryBuilders.termQuery("modelName.keyword", modelName));
        SearchHits hits = tools.getSearchHits(indexName,boolBuilder,0,20000);
        return transferHitToModel(hits).get(0);
    }

    @Override
    public void runModel(String indexName, String modelName, String modelPath){
        ModelEntity me = getModelByName(indexName, modelName);
        try {
            //java模型
            if(me.getLanguage().equals("java")){
                //调用jar包
                System.out.println("执行结果:" + 1);
            }
            //python模型
            else{
                int size = 2 + me.getParameter().size();
                String[] cmdArr = new String[size];
                cmdArr[0] = "python";
                cmdArr[1] = me.getPath();
                cmdArr[2] = modelPath;
                for(int i = 3; i < size; i++){
                    cmdArr[i] = me.getParameter().get(i-3);
                }
                Process process = Runtime.getRuntime().exec(cmdArr);
                //line表示接收到python代码输出的值
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while( ( line = in.readLine() ) != null ) {
                    System.out.println(line);
                }
                in.close();
                //0，表示python代码正常执行;
                //1，表示python代码报错了;
                //2，表示没有找到python文件
                int result = process.waitFor();
                System.out.println("执行结果:" + result);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ModelEntity> transferHitToModel(SearchHits hits){
        ArrayList<ModelEntity> modelEntities=new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit s:searchHits){
            ModelEntity modelEntity=new ModelEntity();
            modelEntity.setParameter(new ArrayList<>(Arrays.asList(String.valueOf(s.getSourceAsMap().get("parameter")).split(","))));
            modelEntity.setPath(String.valueOf(s.getSourceAsMap().get("path")));
            modelEntity.setLanguage(String.valueOf(s.getSourceAsMap().get("language")));
            modelEntity.setDescription(new ArrayList<>(Arrays.asList(String.valueOf(s.getSourceAsMap().get("description")).split(","))));
            modelEntity.setStatus(String.valueOf(s.getSourceAsMap().get("status")));
            modelEntity.setModelName(String.valueOf(s.getSourceAsMap().get("modelName")));
            modelEntities.add(modelEntity);
        }
        return modelEntities;
    }
}
