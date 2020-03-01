package com.nju.topics.dao;

import java.util.List;

public interface IndexInfoService {
    /**
     * 获取所有的表名以及设置的中文别名 如history-历史
     * @return
     */
    List getIndexInfos();
}
