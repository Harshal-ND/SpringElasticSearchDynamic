package com.springelasticsearch.dynamicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.transport.TransportException;

@Service
public class DynamicIndexService {

	@Autowired
	private DataSource dataSource;

//	@Autowired
//	private RestClient restclient;

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	public void indexDataFromDB() throws JsonProcessingException, TransportException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
// Mention the table from where we need to fetch the data--> convert it to json format 
//		--> then index it to elastic search

		String sqlQuery = "SELECT * FROM cricketers";
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) jdbcTemplate.query(sqlQuery, resultSet -> {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> rowMap = new HashMap<>();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					rowMap.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
				}
				rows.add(rowMap);
			}
			return rows;
		});

		// Creating an instance of ObjectMapper to handle JSON serialization
		ObjectMapper objectMapper = new ObjectMapper();

		// Iterate through each row in the list of results
		for (Map<String, Object> rowMap : resultList) {
			// Extract the document ID from the rowMap (e.g., using "cricketer_id")
			String documentId = rowMap.get("cricketer_id").toString();

			// Convert the row map to a JSON string
			String jsonData = objectMapper.writeValueAsString(rowMap);

			// Create an IndexRequest for indexing the data in Elasticsearch
			IndexRequest<Map<String, Object>> indexRequest = new IndexRequest.Builder<Map<String, Object>>()
					.index("harshal_dynamic_index") // Specify the index name
					.id(documentId) // Specify the document ID
					.document(rowMap) // Use the map as the document
					.build();

			try {
				// Sending the request to ElasticsearchCluster using ElasticsearchClient

				elasticsearchClient.index(indexRequest);
				System.out.println("Document indexed successfully: ID " + documentId);

			} catch (ElasticsearchException | IOException e) {

				e.printStackTrace();
				System.err.println("Failed to index document: ID " + documentId);
			}
//		System.out.println(resultList);

//		for (Map<String, Object> map : resultList) {
//			System.out.println(map);
//			String id = map.get("cricketer_id").toString();
//			Request request = new Request("POST", "/" + "category2" + "/_doc/" + id);
//			ObjectMapper objectMapper2 = new ObjectMapper();
//			String json = objectMapper2.writeValueAsString(map);
//			request.setJsonEntity(json);
//			try {
//				restclient.performRequest(request);
//			} catch (IOException e) {
//				e.printStackTrace();
//
//			}
//
//		}

		}

	}
}
