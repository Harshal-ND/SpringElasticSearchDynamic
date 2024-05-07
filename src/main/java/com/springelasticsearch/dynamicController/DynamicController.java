package com.springelasticsearch.dynamicController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springelasticsearch.dynamicService.DynamicDeleteService;
import com.springelasticsearch.dynamicService.DynamicIndexService;
import com.springelasticsearch.dynamicService.DynamicSearchService;
import com.springelasticsearch.dynamicService.DynamicUpdateService;

import co.elastic.clients.transport.TransportException;

@RestController
@RequestMapping("/dynamic")
public class DynamicController {

	@Autowired
	private DynamicIndexService dynamicIndexService;

	@Autowired
	private DynamicDeleteService dynamicDeleteService;

	@Autowired
	private DynamicUpdateService dynamicUpdateService;

	@Autowired
	private DynamicSearchService dynamicSearchService;

//	********************************* INDEXING ******************************************

	// Here we are indexing data t oElastic from database without creating bean
	// WE are fetching data from db in json format and then indexing to elastic
	// cluster
	@GetMapping("/index")
	public void indexDataDynamicallyFromDB() throws JsonProcessingException, TransportException {
		dynamicIndexService.indexDataFromDB();
	}

//********************Deleting index from the elastic cluster*******************	
	@GetMapping("/delete/{indexName}")
	public void deleteIndex(@PathVariable("indexName") String indexName) {
		dynamicDeleteService.deleteIndex(indexName);
	}

//	*******************Delete Particular Document in particular index **************************
	@GetMapping("/deleteDocument/{indexName}/{documentId}")
	public String deleteIndex(@PathVariable("indexName") String indexName,
			@PathVariable("documentId") String documentId) {
		return dynamicDeleteService.deleteDocument(indexName, documentId);
	}

//************************Update Particular Document in Particular Index*******************************
//Thinks to note while updating a document in elastic search
// You can add extra fields while updating
// You can modify any fields
// ****You can't just remove any existing fields-->  for that you have to give a null  value for that particular field

	@PostMapping("/updateDocument/{indexName}/{documentId}")
	public String updateDocument(@PathVariable("indexName") String indexName,
			@PathVariable("documentId") String documentId, @RequestBody Map<String, Object> updateFields) {
		return dynamicUpdateService.updateDocument(indexName, documentId, updateFields);
	}

//*******************************SEARCH OPERATIONS ****************************************
//1. Exact Word
	@GetMapping("/searchByWord/{indexName}/{searchWord}")
	public ResponseEntity<List<Map<String, Object>>> search(@PathVariable("indexName") String indexName,
			@PathVariable("searchWord") String searchWord) throws IOException {
		List<Map<String, Object>> results = dynamicSearchService.searchWord(indexName, searchWord);

//		System.out.println(results);
		// Return the results as a ResponseEntity
		return ResponseEntity.ok(results);
	}

//2.Starts With
	@GetMapping("/searchByWordStartsWith/{indexName}/{searchWord}")
	public ResponseEntity<List<Map<String, Object>>> searchByWordStartsWith(@PathVariable("indexName") String indexName,
			@PathVariable("searchWord") String searchWord) throws IOException {
		List<Map<String, Object>> results = dynamicSearchService.searchWordStartsWith(indexName, searchWord);

//		System.out.println(results);
		// Return the results as a ResponseEntity
		return ResponseEntity.ok(results);
	}

//3. Ends With
	@GetMapping("/searchByWordEndsWith/{indexName}/{searchWord}")
	public ResponseEntity<List<Map<String, Object>>> searchByWordEndsWith(@PathVariable("indexName") String indexName,
			@PathVariable("searchWord") String searchWord) throws IOException {
		List<Map<String, Object>> results = dynamicSearchService.searchByWordEndsWith(indexName, searchWord);

//		System.out.println(results);
		// Return the results as a ResponseEntity
		return ResponseEntity.ok(results);
	}
//4. Match phrase

	@PostMapping("/searchByPhrase")
	public ResponseEntity<List<Map<String, Object>>> searchByWordEndsWith(@RequestBody Map<String, String> requestBody)
			throws IOException {
		// Extract the index name and search word from the request body
		String indexName = requestBody.get("indexName");
		String searchPhrase = requestBody.get("searchPhrase");

		// Call the service to perform the search
		List<Map<String, Object>> results = dynamicSearchService.searchByPhrase(indexName, searchPhrase);

		// Return the results as a ResponseEntity
		return ResponseEntity.ok(results);
	}

	// 5. Exact Match phrase

	@PostMapping("/searchByPhraseExactMatch")
	public ResponseEntity<List<Map<String, Object>>> searchByPhraseExactMatch(
			@RequestBody Map<String, String> requestBody) throws IOException {
		// Extract the index name and search word from the request body
		String indexName = requestBody.get("indexName");
		String searchPhrase = requestBody.get("searchPhrase");

		// Call the service to perform the search
		List<Map<String, Object>> results = dynamicSearchService.searchByPhraseExactMatch(indexName, searchPhrase);

		// Return the results as a ResponseEntity
		return ResponseEntity.ok(results);
	}
}
