package com.nju.topics.dao;

import com.nju.topics.entity.DatasetEntity;

import java.util.ArrayList;

public interface DatasetService {
    //根据模型名称查找数据集
    ArrayList<DatasetEntity> getDatasetByModel(String indexName, String modelName);

    //根据数据集名称查找数据集
    DatasetEntity getDatasetByName(String indexName, String datasetName);
}
