package com.nju.topics.dao;

import com.nju.topics.entity.ModelEntity;

public interface ModelService {
    // 根据名称找模型
    ModelEntity getModelByName(String indexName, String modelName);

    //运行模型
    void runModel(String indexName, String modelName, String datasetPath);
}
