package com.nju.topics.dao;

import com.nju.topics.domain.TagInfo;
import com.nju.topics.domain.TreeDataInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PaperService {

    /***
     * 根据搜索的字寻找论文
     * @param indexName 需要寻找的数据库归类名称，使用的时候需要加上papers，authors之类
     * @param paperName 搜索的内容
     * @return List<E>
     */
    List getPaperByName(String indexName,String paperName);

    /***
     * 根据作者名字来搜索论文
     * @param indexName
     * @param author 作者名字
     * @return
     */
    List getPapersByAuthor(String indexName,String author);

    /***
     * 搜索一个作者所有的文章，然后将搜索结果根据年份进行分类归并
     * @param indexName
     * @param author
     * @return 一个树形结构，根节点是作者名，第二层节点是年份，第三层是叶节点为论文名
     */
    TreeDataInfo getPapersByAuthorAggratedByYear(String indexName,String author);

    /***
     * 批量更新标签
     * @param indexName
     * @param origin 需要进行注入标签的词的list
     * @param tag 定义的标签
     */
    void bulkUpdateTags(String indexName,ArrayList<String> origin, String tag);

    /***
     * 更新某一个文章的标签
     * @param indexName
     * @param originName 文章标题
     * @param tags 需要打入的标签列表
     */
    void bulkUpdateTagsByTitle(String indexName,String originName,ArrayList<String> tags);

    /**
     * 得到 题目-标签 数据信息，用于初次获取的时候获取所有的
     * @param indexName
     * @return
     */
    ArrayList<TagInfo> getAllTagInfos(String indexName);

    /**
     * 得到 题目-标签 数据信息，用于分页的时候获取
     * @param indexName
     * @param begin
     * @param end
     * @return
     */
    ArrayList<TagInfo> getTagInfosByBeginEnd(String indexName,int begin,int end);

    /**
     * 根据需要搜索的标签内容获取对应的 题目-标签 数据
     * @param indexName
     * @param tagName
     * @return
     */
    ArrayList<TagInfo> getTagInfosByTag(String indexName,String tagName);

    /**
     * 获取所有的标签
     * @param indexName
     * @return
     */
    ArrayList<String> getAllTags(String indexName);

    /**
     * 获取标签名称以及每个标签对应的频率
     * @param indexName
     * @return
     */
    Map<String,Integer> getAllTagsAndTimes(String indexName);

    long getTotalRecordsNum(String indexName);
}
