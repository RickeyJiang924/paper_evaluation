package com.nju.topics.dao.impl;

import com.nju.topics.config.EsConfiguration;
import com.nju.topics.dao.ESIndexService;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ESIndexImpl implements ESIndexService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public void createIndex(String name, String alias,String type) {

        try {
            //创建索引
            CreateIndexRequest createIndexRequest=new CreateIndexRequest(name);
            //创建的每个索引都可以有与之关联的特定设置。
            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 2)
            );
            //设置别名
            createIndexRequest.alias(new Alias(alias));
            //可选参数
            createIndexRequest.timeout(TimeValue.timeValueMinutes(2));//超时,等待所有节点被确认(使用TimeValue方式)
            createIndexRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));//连接master节点的超时时间(使用TimeValue方式)
            createIndexRequest.waitForActiveShards(2);//在创建索引API返回响应之前等待的活动分片副本的数量，以int形式表示。

            Map<String,Object> properties = new HashMap();
            Map<String,Object> propertie = new HashMap();
            propertie.put("type",type);
            propertie.put("index",true);
            properties.put("field_name",propertie);
            XContentBuilder builder = JsonXContent.contentBuilder();
            builder.startObject()
                    .startObject("mappings")
                    .startObject("index_name")
                    .field("properties",properties)
                    .endObject()
                    .endObject()
                    .endObject();
            createIndexRequest.source(builder);

            //同步执行
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            //指示是否所有节点都已确认请求
            boolean acknowledged = createIndexResponse.isAcknowledged();
            //指示是否在超时之前为索引中的每个分片启动了必需的分片副本数
            boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        }catch (IOException e){
            System.err.println("创建索引"+name+"时发生了IOException!");
            e.printStackTrace();
        }catch (ElasticsearchException elasticException){
            if (elasticException.status()== RestStatus.FOUND){
                System.err.println("index "+name+" has existed!");
                return;
            }
            elasticException.printStackTrace();
            System.err.println(elasticException.status());
        }
    }

    @Override
    public void deleteIndex(String name) {
        DeleteIndexRequest request = new DeleteIndexRequest(name);//指定要删除的索引名称
        //可选参数：
        request.timeout(TimeValue.timeValueMinutes(2)); //设置超时，等待所有节点确认索引删除（使用TimeValue形式）
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));////连接master节点的超时时间(使用TimeValue方式)
        //设置IndicesOptions控制如何解决不可用的索引以及如何扩展通配符表达式
        request.indicesOptions(IndicesOptions.lenientExpandOpen());

        AcknowledgedResponse deleteIndexResponse=null;
        //同步执行
        try {
            deleteIndexResponse = client.indices().delete(request,RequestOptions.DEFAULT);
        }catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
                //如果没有找到要删除的索引，要执行某些操作
                return;
            }
        } catch (IOException e){
            System.err.println("删除索引"+name+"时发生了IOException!");
            e.printStackTrace();
        }

        //Delete Index Response
        //返回的DeleteIndexResponse允许检索有关执行的操作的信息，如下所示：
        boolean acknowledged = deleteIndexResponse.isAcknowledged();//是否所有节点都已确认请求

    }
}
