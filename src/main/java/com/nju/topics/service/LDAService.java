package com.nju.topics.service;

import com.nju.topics.domain.EvolutionDataInfo;
import org.json.JSONObject;

public interface LDAService {

    /**
     * 获取lda计算的演化图数据
     * @param fileName
     * @return
     */
    public JSONObject getLDARelation(String fileName);

    /**
     * 相比较上一个方法，得到的数据格式是JSON的String类型
     *
     * @param parentPath
     * @return
     */
    public String getLDARelationString(String parentPath);

    EvolutionDataInfo getEvolutionData(String parentPath,String id);
}
