package com.nju.topics.dao;

/***
 * 该接口提供对elasticsearch中index的一些操作
 * createIndex是创建新的索引
 * deleteIndex是删除指定索引
 */
public interface ESIndexService {

    /***
     * 创建新的索引，以及设置别名
     * @param name 索引的名字
     * @param alias 索引别名
     * @param type
     */
    void createIndex(String name,String alias,String type);

    /***
     * 删除指定索引，暂时不开放该接口，风险太大
     * @param name 需要删除的索引的名字
     */
    void deleteIndex(String name);
}
