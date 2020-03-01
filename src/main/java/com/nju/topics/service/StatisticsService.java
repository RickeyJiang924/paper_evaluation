package com.nju.topics.service;

import com.nju.topics.domain.StatisticsInfo;
import com.nju.topics.entity.IndexEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StatisticsService {

    /***
     * |-history2014
     *  |-history2014_author
     *      |-keyword-num-author1:a_num1;author2:a_num2;
     * 每一个分词统计占据一个文件夹，文件夹中包括对应的作者，期刊，机构统计文件，
     * 对应的文件名都是  文件夹名_author，文件夹名_venue,文件夹名_institution
     * @return  此方法就是获取每一个文件夹的名字
     */
    public List getAllStatisticsDictNames();

    /***
     * 获取某一个属性分词文件里面所有的关键词，按照词频从大到小排序
     * @param dictName  分词文件夹的名字
     * @param attributeName 统计属性的名字，比如author venue institution
     * @return 按照词频从大到小排序的HashMap
     */
    public ArrayList<StatisticsInfo> getAllKeyWords(String dictName, String attributeName);

    /**
     * 获取ElasticSearch分词中所有关键词，按照词频从大到小排序
     * @return 按照词频从大到小排序的HashMap
     */
    public ArrayList<StatisticsInfo> getAllKeyWordsByES(String indexName);

    /***
     * 获取某一个文件夹下面某个属性的某个关键词的具体词频，比如说history2014 文件夹下面 history2014_author文件里面keyword1对应的具体词频
     * @param dictName
     * @param attributeName
     * @param keyWord
     * @return 按照词频从大到小排序的HashMap
     */
    public ArrayList<StatisticsInfo> getOneAttribute(String dictName,String attributeName,String keyWord);

    /***
     * 从ElasticSearch获取作者属性的某个关键词的具体词频
     * @param keyword
     * @return 按照词频从大到小排序的HashMap
     */
    public ArrayList<StatisticsInfo> getAuthorAttributeByES(String indexName,String keyword);

    /***
     * 从ElasticSearch获取机构属性的某个关键词的具体词频
     * @param keyword
     * @return 按照词频从大到小排序的HashMap
     */
    public ArrayList<StatisticsInfo> getInstitutionAttributeByES(String indexName,String keyword);

    /***
     * 获取某一个文件夹下面某个属性文件里面所有关键词的词频
     * @param dictName
     * @param attributeName
     * @return
     */
    public Map<String,Map<String,Integer>> getAllAtrribute(String dictName, String attributeName);
}
