package com.nju.topics.dao;

import com.nju.topics.domain.StatisticsInfo;

import java.util.ArrayList;
import java.util.List;

public interface AuthorService {

    /**
     * 根据搜索的作者名称找到作者信息
     * @param indexName
     * @param authorName
     * @return
     */
    List getAuthorInfoByName(String indexName,String authorName);

    /**
     * 根据搜索的作者名称找到与之合作的作者
     * @param indexName
     * @param authorName
     * @return
     */
    ArrayList<StatisticsInfo> getRelativeAuthorByAuthor(String indexName,String authorName);

    /**
     * 根据论文名称找到作者
     * @param indexName
     * @param keyWord
     * @return
     */
    List getAuthorByPaper(String indexName,String keyWord);
}
