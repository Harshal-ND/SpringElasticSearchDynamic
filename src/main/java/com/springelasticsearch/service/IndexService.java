package com.springelasticsearch.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.elasticsearch.client.RestClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springelasticsearch.entity.Cricketers;
import com.springelasticsearch.entity.CricketersDocument;
import com.springelasticsearch.entity.RequestBean;
import com.springelasticsearch.repository.CricketersDocumentRepository;
import com.springelasticsearch.repository.DemoRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonData;

@Service
public class IndexService {

	@Autowired
	private DemoRepository demoRepository;
	
	@Autowired
	private FetchFromDBService fetchFromDBService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CricketersDocumentRepository cricketersDocumentRepository;

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	@Autowired
	RestClient restClient;
	


	public void indexData() {
		// Fetch data from MySQL database
		List<Cricketers> cricketersList = fetchFromDBService.fetchData();

		List<CricketersDocument> cricketerDocuments = cricketersList.stream()
				.map(cricketer -> modelMapper.map(cricketer, CricketersDocument.class)).collect(Collectors.toList());

		cricketersDocumentRepository.saveAll(cricketerDocuments);

	}
	
	//2.
//Here the input data is in json format
// Here we are passing json data via post man 
	public String indexDataDynamically(RequestBean requestBean) {
	    // Create an ObjectMapper instance for JSON serialization
	    ObjectMapper objectMapper = new ObjectMapper();

	    // Iterate over each JSON data map in the list
	    for (Map<String, Object> dataMap : requestBean.getJsonDataList()) {
	        try {
	        	
//	        	 System.out.println(dataMap);
	            // Declare `idValue` at the start of each iteration
	            String idValue = dataMap.containsKey("id") ? dataMap.get("id").toString() : UUID.randomUUID().toString();

	            // If no ID is provided, generate a new one and add it to the data map
	            if (!dataMap.containsKey("id")) {
	                dataMap.put("id", idValue);
	            }

	           
//	             Convert the data map to a JSON string
	            String jsonDataString = objectMapper.writeValueAsString(dataMap);
//
//	            System.out.println(jsonDataString);
//	            // Convert the JSON string to JsonData
	            // What happens if we dont convert json string to json Data?
	            //It will throw an exception , because the system doesn't know the application content: application/json
	            JsonData jsonData = JsonData.fromJson(jsonDataString);
//	            System.out.println(jsonData);
//
	            // Perform the indexing operation within the try block
	            elasticsearchClient.index(i -> i
	                .index(requestBean.getIndexName())
	                .id(idValue)
	                .document(jsonData)
	            );

	            System.out.println("Data indexed successfully for ID: " + idValue);
	        } catch (Exception e) {
	            // Log exceptions and print details of the error
	            System.err.println("Error during indexing for ID: " + dataMap.get("id") + " - " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	    return "Indexing completed successfully.";
	}

//3.
// Fetching data from mysql and indexing to Elastic Cluster using Repository
	
	
	public void indexDataDbToElastic()
	{
		List<Map<String, Object>> allRecords=demoRepository.findAllRecordsFromDB();
		for (Map<String, Object> record : allRecords) {
		    System.out.println(record);
		}
	}

	//4.
// Fetching data from mysql and indexing to Elastic Cluster using JDBC Template

}
