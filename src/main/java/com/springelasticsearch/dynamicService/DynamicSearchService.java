package com.springelasticsearch.dynamicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@Service
public class DynamicSearchService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	ObjectMapper objectMapper = new ObjectMapper();

	public List<Map<String, Object>> searchWord(String indexName, String searchWord) {
		// Create a NativeQuery to perform the search
		NativeQuery query = NativeQuery.builder().withQuery(q -> q.queryString(m -> m.fields("*").query(searchWord)))
				.build();

		// Specify the index coordinates using the index name
		IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

		// Perform the search operation on the specified index and get the search hits
		SearchHits<Map> searchHits = elasticsearchOperations.search(query, Map.class, indexCoordinates);

		// Create a list to store the results
		List<Map<String, Object>> resultsList = new ArrayList<>();

		// Iterate through the search hits and add the source (document) to the results
		// list
		if (searchHits != null) {
			for (SearchHit<Map> hit : searchHits) {
				// Extract the source (document) from each hit
				Map<String, Object> document = hit.getContent();

				// Add the document to the results list
				resultsList.add(document);
			}
		}

		// Return the list of documents
		return resultsList;
	}

	public List<Map<String, Object>> searchWordStartsWith(String indexName, String searchWord) {
		// Create a NativeQuery to perform the search

		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.queryString(m -> m.fields("*").query(searchWord + "*"))).build();

//		NativeQuery query = NativeQuery.builder()
//                .withQuery(q -> q.queryString(m -> m.fields("*").query("*" + searchWord)))
//                .build();
		// Specify the index coordinates using the index name
		IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

		// Perform the search operation on the specified index and get the search hits
		SearchHits<Map> searchHits = elasticsearchOperations.search(query, Map.class, indexCoordinates);

		// Create a list to store the results
		List<Map<String, Object>> resultsList = new ArrayList<>();

		// Iterate through the search hits and add the source (document) to the results
		// list
		if (searchHits != null) {
			for (SearchHit<Map> hit : searchHits) {
				// Extract the source (document) from each hit
				Map<String, Object> document = hit.getContent();

				// Add the document to the results list
				resultsList.add(document);
			}
		}

		// Return the list of documents
		return resultsList;
	}

	public List<Map<String, Object>> searchByWordEndsWith(String indexName, String searchWord) {
		// Create a NativeQuery to perform the search

		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.queryString(m -> m.fields("*").query("*" + searchWord))).build();

		// Specify the index coordinates using the index name
		IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

		// Perform the search operation on the specified index and get the search hits
		SearchHits<Map> searchHits = elasticsearchOperations.search(query, Map.class, indexCoordinates);

		// Create a list to store the results
		List<Map<String, Object>> resultsList = new ArrayList<>();

		// Iterate through the search hits and add the source (document) to the results
		// list
		if (searchHits != null) {
			for (SearchHit<Map> hit : searchHits) {
				// Extract the source (document) from each hit
				Map<String, Object> document = hit.getContent();

				// Add the document to the results list
				resultsList.add(document);
			}
		}

		// Return the list of documents
		return resultsList;
	}

	public List<Map<String, Object>> searchByPhrase(String indexName, String searchPhrase) {

		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.queryString(qs -> qs.query(searchPhrase).fields("*"))).build();

		// Specify the index coordinates using the index name
		IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

		// Perform the search operation on the specified index and get the search hits
		SearchHits<Map> searchHits = elasticsearchOperations.search(query, Map.class, indexCoordinates);

		// Create a list to store the results
		List<Map<String, Object>> resultsList = new ArrayList<>();

		// Iterate through the search hits and add the source (document) to the results
		// list
		if (searchHits != null) {
			for (SearchHit<Map> hit : searchHits) {
				// Extract the source (document) from each hit
				Map<String, Object> document = hit.getContent();

				// Add the document to the results list
				resultsList.add(document);
			}
		}

		// Return the list of documents
		return resultsList;
	}

	public List<Map<String, Object>> searchByPhraseExactMatch(String indexName, String searchPhrase) {

		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.simpleQueryString(sqs -> sqs.query("\"" + searchPhrase + "\"") // Use quotation marks
																									// for exact phrase
																									// match
						.fields("*") // Search in all fields
				)).build();
		// Specify the index coordinates using the index name
		IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

		// Perform the search operation on the specified index and get the search hits
		SearchHits<Map> searchHits = elasticsearchOperations.search(query, Map.class, indexCoordinates);

		// Create a list to store the results
		List<Map<String, Object>> resultsList = new ArrayList<>();

		// Iterate through the search hits and add the source (document) to the results
		// list
		if (searchHits != null) {
			for (SearchHit<Map> hit : searchHits) {
				// Extract the source (document) from each hit
				Map<String, Object> document = hit.getContent();

				// Add the document to the results list
				resultsList.add(document);
			}
		}

		// Return the list of documents
		return resultsList;
	}
}
