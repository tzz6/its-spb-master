package com.its.base.servers.es.rhl.impl;

import com.its.base.servers.es.rhl.ElasticSearchBase;
import com.its.common.es.rhl.util.IndexTools;
import com.its.common.es.rhl.util.MappingData;
import com.its.common.es.rhl.util.MetaData;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Description: ElasticsearchIndexImpl
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 17:01
 */
@Service
public class ElasticSearchBaseImpl<T> implements ElasticSearchBase<T> {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex(Class<T> clazz) {
        try {
            MetaData metaData = IndexTools.getMetaData(clazz);
            MappingData[] mappingDataList = IndexTools.getMappingData(clazz);
            if (metaData != null) {
                CreateIndexRequest request = new CreateIndexRequest(metaData.getIndexName());
                StringBuffer source = new StringBuffer();
                source.append("  {\n" +
                        "    \"" + metaData.getIndextype() + "\": {\n" +
                        "      \"properties\": {\n");

                boolean isAutocomplete = false;
                for (int i = 0; i < mappingDataList.length; i++) {
                    MappingData mappingData = mappingDataList[i];
                    if (mappingData == null || mappingData.getFieldName() == null) {
                        continue;
                    }
                    String datatype = mappingData.getDatatype();
                    source.append(" \"" + mappingData.getFieldName() + "\": {\n");
                    source.append(" \"type\": \"" + datatype + "\"\n");
                    if (!StringUtils.isEmpty(mappingData.getCopyTo())) {
                        source.append(" ,\"copy_to\": \"" + mappingData.getCopyTo() + "\"\n");
                    }
                    if (!mappingData.isAllowSearch()) {
                        source.append(" ,\"index\": false\n");
                    }
                    if (mappingData.isAutocomplete()) {
                        if (("text".equals(datatype) || "keyword".equals(datatype))) {
                            source.append(" ,\"analyzer\": \"autocomplete\"\n");
                            source.append(" ,\"search_analyzer\": \"standard\"\n");
                            isAutocomplete = true;
                        }
                    } else if ("text".equals(datatype)) {
                        source.append(" ,\"analyzer\": \"" + mappingData.getAnalyzer() + "\"\n");
                        source.append(" ,\"search_analyzer\": \"" + mappingData.getSearchAnalyzer() + "\"\n");
                    }
                    if (mappingData.isKeyword() && !"keyword".equals(datatype)) {
                        source.append(" \n");
                        source.append(" ,\"fields\": {\n");
                        source.append(" \"keyword\": {\n");
                        source.append(" \"type\": \"keyword\",\n");
                        source.append(" \"ignore_above\": " + mappingData.getIgnoreAbove());
                        source.append(" }\n");
                        source.append(" }\n");
                    } else if (mappingData.isSuggest()) {
                        source.append(" \n");
                        source.append(" ,\"fields\": {\n");
                        source.append(" \"suggest\": {\n");
                        source.append(" \"type\": \"completion\",\n");
                        source.append(" \"analyzer\": \"" + mappingData.getAnalyzer() + "\",\n");
                        source.append(" }\n");
                        source.append(" }\n");
                    }
                    if (i == mappingDataList.length - 1) {
                        source.append(" }\n");
                    } else {
                        source.append(" },\n");
                    }
                }
                source.append(" }\n");
                source.append(" }\n");
                source.append(" }\n");

                //设置Setting
                requestSettings(metaData, request, isAutocomplete);
                request.mapping(metaData.getIndextype(), source.toString(), XContentType.JSON);
                CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
                //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：指示是否所有节点都已确认请求
                boolean acknowledged = createIndexResponse.isAcknowledged();
                System.out.println(acknowledged);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestSettings(MetaData metaData, CreateIndexRequest request, boolean isAutocomplete) {
        if (isAutocomplete) {
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumberOfShards())
                    .put("index.number_of_replicas", metaData.getNumberOfReplicas())
                    .put("analysis.filter.autocomplete_filter.type", "edge_ngram")
                    .put("analysis.filter.autocomplete_filter.min_gram", 1)
                    .put("analysis.filter.autocomplete_filter.max_gram", 20)
                    .put("analysis.analyzer.autocomplete.type", "custom")
                    .put("analysis.analyzer.autocomplete.tokenizer", "standard")
                    .putList("analysis.analyzer.autocomplete.filter", new String[]{"lowercase", "autocomplete_filter"})
            );
        } else {
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumberOfShards())
                    .put("index.number_of_replicas", metaData.getNumberOfReplicas())
            );
        }
    }


    @Override
    public void delete(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Override
    public boolean exists(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        String indexType = metaData.getIndextype();
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        request.types(indexType);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }
}
