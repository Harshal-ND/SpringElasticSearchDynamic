package com.springelasticsearch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springelasticsearch.entity.CricketersDocument;
import com.springelasticsearch.entity.RequestBean;
import com.springelasticsearch.service.IndexService;
import com.springelasticsearch.service.SearchService;

@RestController
public class CricketersController {

	@Autowired
	private IndexService indexService;

	@Autowired
	private SearchService searchService;

	@GetMapping("/indexData")
	public String indexData() {

		indexService.indexData();
		System.out.println("Indexing done successfully");
		return "Indexing done successfully";
	}

	// Second way of indexing data
	// Input is Json
	// Request Beam contains only two fields.
	// 1. Id and 2.List of Map
	@PostMapping("/indexDataDynamically")
	public String indexData(@RequestBody RequestBean requestBean) {
		try {
			indexService.indexDataDynamically(requestBean);
			System.out.println("Indexing done successfully");
			return "Indexing done successfully";
		} catch (Exception e) {
			// Handle any exception that may arise during the indexing process
			System.err.println("Error during indexing: " + e.getMessage());
			return "Error during indexing. Please check the server logs for more details.";
		}
	}

//*****INDEXING************
	@GetMapping("/indexDataDbToElastic")
	public void indexDataDbToElastic() {

		indexService.indexDataDbToElastic();
	}

//	@GetMapping("/findById/{id}")
//	public CricketersDocument findById(@PathVariable("id") int id) {
//
//		return searchService.findById(id);
//	}

//************Find By Id *************************

	@GetMapping("/findById/{id}")
	public <T> T findById(@PathVariable("id") int id) throws ClassNotFoundException {
		// Pass the id and the document class type to the service method
		Class<?> clazz = Class.forName("com.springelasticsearch.entity.CricketersDocument");

		return (T) searchService.findById(id, clazz);
	}

	@GetMapping("/findByFullName")
	public CricketersDocument findByFullName(@RequestParam("fullName") String fullName) {
		String[] nameParts = fullName.split(" ");
		if (nameParts.length < 2) {
			throw new IllegalArgumentException("Full name must contain both first name and last name.");
		}
		String fName = nameParts[0];
		String lName = nameParts[1];
		return searchService.findByFullName(fName, lName);
	}

	@GetMapping("/findByCountry/{country}")
	public List<CricketersDocument> findByCountry(@PathVariable("country") String country) {

		return searchService.findByCountry(country);
	}

	// Lets see some complex Queries
	// 1. Find the list which contains the word provided by user
	// Here it will search in all fields
//	word--> batsman

	@GetMapping("/searchRandomWord/{word}")
	public List<CricketersDocument> findByRandomWord(@PathVariable("word") String word) {

		return searchService.findByRandomWord(word);
	}

	// 2.Find cricketers who are right-handed batsmen and also bowl right-arm
	// fast-medium
	// batStyle--> right-handed
	// bowlStyle -> right-arm fast-medium

	@GetMapping("/findByBatBowlStyle/{batStyle}/{bowlStyle}")
	public List<CricketersDocument> findByBatBowlStyle(@PathVariable("batStyle") String batStyle,
			@PathVariable("bowlStyle") String bowlStyle) {

		return searchService.findByBatBowlStyle(batStyle, bowlStyle);
	}

//	3.Find cricketers who have played more than 100 matches and scored more than 5000 runs.

	@GetMapping("/findByRange1/{matches}/{runs}")
	public List<CricketersDocument> findByRange1(@PathVariable("matches") int matches, @PathVariable("runs") int runs) {

		return searchService.findByRange1(matches, runs);
	}

// 4.Find cricketers who have taken more than 200 wickets and have a description mentioning "skilled" or "specialist".	

	@GetMapping("/findByRange2/{wkts}/{word1}/{word2}")
	public List<CricketersDocument> findByRange1(@PathVariable("wkts") int wkts, @PathVariable("word1") String word1,
			@PathVariable("word2") String word2) {

		return searchService.findByRange2(wkts, word1, word2);
	}

//5.Find cricketers with the last name starting with "D" and playing for India.

	// word1--> name //word2->country
	@GetMapping("/findByRange3/{word1}/{word2}")
	public List<CricketersDocument> findByRange3(@PathVariable("word1") String word1,
			@PathVariable("word2") String word2) {

		return searchService.findByRange3(word1, word2);
	}

//6.************** STARTS WITH QUERY*************
	// Searching a word to starts with in a particular field ("battingStyle"
	// Example)
	@GetMapping("/startsWithQuery/{word}")
	public List<CricketersDocument> startsWithQuery(@PathVariable("word") String word) {

		return searchService.startsWithQuery(word);
	}

// 7.************** STARTS WITH QUERY*************
	// Searching a word to starts with in all the fields
	@GetMapping("/startsWithQuery2/{word}")
	public List<CricketersDocument> startsWithQuery2(@PathVariable("word") String word) {

		return searchService.startsWithQuery2(word);
	}

// 8.************** ENDS WITH QUERY*************
	// Searching a word to ends with a particular word in a particular field
	// ("firstName")
	@GetMapping("/endsWithQuery3/{word}")
	public List<CricketersDocument> endsWithQuery3(@PathVariable("word") String word) {

		return searchService.endsWithQuery3(word);
	}

// 9.************** ENDS WITH QUERY*************
	// Searching a word to ends with a particular word in all the fields
	// here that work requires exact match
	@GetMapping("/endsWithQuery4/{word}")
	public List<CricketersDocument> endsWithQuery4(@PathVariable("word") String word) {

		return searchService.endsWithQuery4(word);
	}

// 10. Contains with --> only word (that word should be exact match) --> will search in all fields
	// not exact phrase
	@GetMapping("/containsQuery/{word}")
	public List<CricketersDocument> containsQuery(@PathVariable("word") String word) {

		return searchService.containsQuery(word);
	}

// 10. Contains with --> exact phrase  -->will search in all fields
	@GetMapping("/containsPhraseQuery/{word}")
	public List<CricketersDocument> containsPhraseQuery(@PathVariable("word") String word) {

		return searchService.containsPhraseQuery(word);
	}
// 11. Sorting Query and Pageable (pageNo:0 & size:15)
	// Sorting on the basis of field provided

	@GetMapping("/sortingQuery1/{fldName}")
	public List<CricketersDocument> sortingQuery1(@PathVariable("fldName") String fldName) {

		return searchService.sortingQuery1(fldName);
	}

	// 12. Sorting Query and Pageable (pageNo:0 & size:15)
	// Sorting on the basis of two field provided
	// for second fldName2 sort comes into picture if the fldName1 have same values.
	@GetMapping("/sortingQuery2/{fldName1}/{fldName2}")
	public List<CricketersDocument> sortingQuery1(@PathVariable("fldName1") String fldName1,
			@PathVariable("fldName1") String fldName2) {

		return searchService.sortingQuery2(fldName1, fldName2);
	}

	// 13. Partial match // concept: fuzziness
	// This search is for all the fields
	@GetMapping("/partialMatchQuery/{word}")
	public List<CricketersDocument> partialMatchQuery(@PathVariable("word") String word) {

		return searchService.partialMatchQuery(word);
	}

//	***************************AGGREGATIONS ***********************************
//	--> Grouping documents by country field

//	@GetMapping("/aggregationQuery1")
//	public List<CricketersDocument> aggregationQuery1() {
//
//		return	searchService.aggregationQuery1();
//		
//	}
//	@GetMapping("/aggregationQuery1/{fldName}")
//	public List<CricketersDocument> aggregationQuery1(@PathVariable("fldName") String fldName) {
//
//		return searchService.aggregationQuery1(fldName);
//	}

}
