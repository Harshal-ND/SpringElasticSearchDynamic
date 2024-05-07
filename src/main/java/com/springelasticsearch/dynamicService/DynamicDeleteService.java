package com.springelasticsearch.dynamicService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;

@Service
public class DynamicDeleteService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	public String deleteIndex(String indexName) {
		try {
			// Creating a request to delete the specified index
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(indexName).build();

			// Sending the request to Elasticsearch using ElasticsearchClient
			DeleteIndexResponse response = elasticsearchClient.indices().delete(deleteIndexRequest);

			// Check the response for success
			if (response.acknowledged()) {
				return "Index " + indexName + " was deleted successfully.";
			} else {
				return "Failed to delete index " + indexName;
			}
		} catch (IOException e) {
			// Handle IO exceptions that may occur during the request
			e.printStackTrace();
			return "An error occurred while deleting index " + indexName;
		}

//		return "An error occurred while deleting index " + indexName;
	}

	public String deleteDocument(String indexName, String documentId) {
		try {
			// Creating a request to delete the document from the specified index
			DeleteRequest deleteRequest = new DeleteRequest.Builder().index(indexName) // Specifying the index name
					.id(documentId) // Specifying the document ID
					.build();

			// Sending the request to Elasticsearch using ElasticsearchClient
			DeleteResponse response = elasticsearchClient.delete(deleteRequest);

			// Check the response for the result
			Result result = response.result();
			if (result == Result.Deleted) {
				return "Document with ID " + documentId + " was deleted successfully from index " + indexName + ".";
			} else if (result == Result.NotFound) {
				return "Document with ID " + documentId + " was not found in index " + indexName + ".";
			} else {
				return "Unexpected result when trying to delete document with ID " + documentId + " from index "
						+ indexName + ": " + result;
			}
		} catch (IOException e) {
			// Handle IO exceptions that may occur during the request
			e.printStackTrace();
			System.err.println("An error occurred while trying to delete document with ID " + documentId
					+ " from index " + indexName + ".");
		}
		return "An error occurred while trying to delete document with ID " + documentId + " from index " + indexName
				+ ".";
	}

}
