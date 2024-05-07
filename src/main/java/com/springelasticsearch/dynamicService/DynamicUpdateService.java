package com.springelasticsearch.dynamicService;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;

@Service
public class DynamicUpdateService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	public String updateDocument(String indexName, String documentId, Map<String, Object> updateFields) {
		try {
			// Checking if the index exists
			boolean indexExists = elasticsearchClient.indices().exists(r -> r.index(indexName)).value();
			
			if (!indexExists) {
				return "Index " + indexName + " does not exist.";
			}

			// Checking if the document exists in the index
			GetRequest getRequest = new GetRequest.Builder().index(indexName).id(documentId).build();
			GetResponse<Map> getResponse = elasticsearchClient.get(getRequest, Map.class);

			if (!getResponse.found()) {
				return "Document with ID " + documentId + " was not found in index " + indexName + ".";
			}

			// Converting the update fields map to JSON using ObjectMapper
			ObjectMapper objectMapper = new ObjectMapper();

			// Create an UpdateRequest to update the document
			UpdateRequest<Map<String, Object>, Map<String, Object>> updateRequest = new UpdateRequest.Builder<Map<String, Object>, Map<String, Object>>()
					.index(indexName) // Specify the index name
					.id(documentId) // Specify the document ID
					.doc(updateFields) // Set the update fields
					.build();

			// Performing the update operation
			UpdateResponse<Map<String, Object>> response = elasticsearchClient.update(updateRequest, Map.class);

			// Check the result of the update operation
			Result result = response.result();
			if (result == Result.Updated) {
				return "Document with ID " + documentId + " was updated successfully in index " + indexName + ".";
			} else if (result == Result.NotFound) {
				return "Document with ID " + documentId + " was not found in index " + indexName + ".";
			} else if (result == Result.NoOp) {
				return "No changes were made to document with ID " + documentId + " in index " + indexName
						+ ". It may already contain the same values as provided.";
			} else {
				return "Unexpected result when trying to update document with ID " + documentId + " in index "
						+ indexName + ": " + result;
			}
		} catch (IOException e) {
			// Handle exceptions during the request
			e.printStackTrace();
			return "An error occurred while updating document with ID " + documentId + " in index " + indexName + ".";
		}
	}
}
