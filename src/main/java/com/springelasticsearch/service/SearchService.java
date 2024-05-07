package com.springelasticsearch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.springelasticsearch.entity.CricketersDocument;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.json.JsonData;

@Service
public class SearchService {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

//1. Find By Id

//	public CricketersDocument findById(int cricketerId) {
//		NativeQuery query = NativeQuery.builder()
//				.withQuery(q -> q.match(m -> m.field("cricketerId").query(cricketerId))).build();
//		SearchHit<CricketersDocument> searchHit = elasticsearchOperations.searchOne(query, CricketersDocument.class);
//		if (searchHit != null) {
//			return searchHit.getContent();
//		}
//		return null;
//	}
	public <T> T findById(int cricketerId, Class<T> documentClass) {

		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.match(m -> m.field("cricketerId").query(cricketerId))).build();

		SearchHit<T> searchHit = elasticsearchOperations.searchOne(query, documentClass);

		if (searchHit != null) {
			return searchHit.getContent();
		}

		return null;
	}

//2. Search By Full Name -> /findByFullName?fullName= virat kohli

	public CricketersDocument findByFullName(String fName, String lName) {
		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.bool(b -> b.must(m -> m.match(mm -> mm.field("firstName").query(fName)))
						.must(m -> m.match(mm -> mm.field("lastName").query(lName)))))
				.build();
		SearchHit<CricketersDocument> searchHit = elasticsearchOperations.searchOne(query, CricketersDocument.class);
		if (searchHit != null) {
			return searchHit.getContent();
		}
		return null;
	}

//3. Search By Country
	public List<CricketersDocument> findByCountry(String country) {
		NativeQuery query = NativeQuery.builder().withQuery(q -> q.match(m -> m.field("country").query(country)))
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// Lets see some complex Queries
	// 1. Find the list which contains the word provided by user
	// word--> batsman

//	NativeQuery query=NativeQuery.builder().withQuery(q->q.queryString(m->m.query("virat"))).build();

	public List<CricketersDocument> findByRandomWord(String word) {
//		NativeQuery query = NativeQuery.builder().withQuery(q -> q.match(m -> m.query(word)))
//				.build();
		NativeQuery query = NativeQuery.builder().withQuery(q -> q.queryString(m -> m.fields("*").query(word))).build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	public List<CricketersDocument> findByBatBowlStyle(String batStyle, String bowlStyle) {
		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.bool(b -> b
						.must(m -> m.match(mt -> mt.field("battingStyle").query(batStyle).operator(Operator.And))) // Specify
																													// AND
																													// operator
						.must(m -> m.match(mt -> mt.field("bowlingStyle").query(bowlStyle).operator(Operator.And))) // Specify
																													// AND
																													// operator
				)).build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

//****************************
	public List<CricketersDocument> findByRange1(int matches, int runs) {
		NativeQuery query = NativeQuery.builder()
				.withQuery(
						q -> q.bool(b -> b.filter(f -> f.range(r -> r.field("matchesPlayed").gt(JsonData.of(matches)))) // Matches
																														// played
																														// more
																														// than
																														// 100
								.filter(f -> f.range(r -> r.field("runsScored").gt(JsonData.of(runs)))) // Runs scored
																										// more than
																										// 5000
						)).withPageable(Pageable.ofSize(200)).build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> findByRange2(int wkts, String word1, String word2) {
		NativeQuery query = NativeQuery.builder()
				.withQuery(
						q -> q.bool(bt -> bt.filter(ft -> ft.range(r -> r.field("wicketsTaken").gt(JsonData.of(wkts))))
								.filter(ft1 -> ft1.bool(b1 -> b1
										.should(sh1 -> sh1.match(m1 -> m1.query(word1).field("description")))
										.should(sh1 -> sh1.match(m1 -> m1.query(word2).field("description")))))))
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> findByRange3(String word1, String word2) {
		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.bool(bt -> bt.filter(m -> m.match(mt -> mt.query(word2).field("country")))
						.must(mt2 -> mt2.matchPhrasePrefix(mf -> mf.query(word1).field("lastName")))))
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> startsWithQuery(String word1) {
		NativeQuery query = NativeQuery.builder().withQuery(
				q -> q.bool(bt -> bt.must(mt2 -> mt2.matchPhrasePrefix(mf -> mf.query(word1).field("battingStyle")))))
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> startsWithQuery2(String word1) {
		// Create a NativeQuery with a multi_match query
		List<String> fieldsToSearch = List.of("battingStyle", "bowlingStyle", "description", "firstName", "lastName",
				"country");

		// Create a NativeQuery with a multi_match query
		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.multiMatch(m -> m.query(word1).type(TextQueryType.PhrasePrefix) // Use the
																									// 'phrase_prefix'
																									// type for starts
																									// with search
						.fields(fieldsToSearch) // Specify the list of fields to search across
						.operator(Operator.Or))) // Use the OR operator to match any field
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> endsWithQuery3(String word1) {

		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.wildcard(w -> w.field("firstName").wildcard("*" + word1))).build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> endsWithQuery4(String word1) {
		// Create a NativeQuery with a multi_match query
		List<String> fieldsToSearch = List.of("battingStyle", "bowlingStyle", "description", "firstName", "lastName",
				"country");
		NativeQuery query = NativeQuery.builder().withQuery(q -> q.multiMatch(mm -> mm.query(word1) // Query string
																									// (word1)
				.fields(fieldsToSearch) // Fields to search across
				.type(TextQueryType.BestFields))) // Set query type to WILDCARD
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> containsQuery(String word1) {
		// Create a NativeQuery with a multi_match query
		List<String> fieldsToSearch = List.of("battingStyle", "bowlingStyle", "description", "firstName", "lastName",
				"country");
		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.multiMatch(m -> m.fields(fieldsToSearch).query(word1))).build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> containsPhraseQuery(String word1) {
		// Create a NativeQuery with a multi_match query
//		List<String> fieldsToSearch = List.of("battingStyle", "bowlingStyle", "description", "firstName", "lastName",
//				"country");
//		NativeQuery query = NativeQuery.builder()
//		        .withQuery(q -> q.matchPhrase(mp -> mp
//		            .field("description")
//		            .query(word1)))
//		        .build();
		NativeQuery query = NativeQuery.builder()
				.withQuery(q -> q.multiMatch(mm -> mm.query(word1).type(TextQueryType.Phrase))) // Use type "phrase" for
																								// exact phrase search
				.build();
		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> sortingQuery1(String fldName) {

		// 1. Sorting by one Field
//		Sort sort = Sort.by(Direction.ASC, fldName);
		Sort sort = Sort.by(Direction.ASC, fldName + ".keyword"); // Appending of ".keyword" is very important.
		// For sorting and aggregations to perform in elastic the targeted field must be
		// of type keyword
//			NativeQuery query = NativeQuery.builder().withQuery(q -> q.match(m->m.query("left").field("battingStyle"))) // Match all documents
//					.withSort(sort) // Specify the order (ascending)
//					.build();

		int pageSize = 15;
		// Creating a Pageable instance with page number 1 and the specified page size
		Pageable pageable = PageRequest.of(0, pageSize, sort);

		// Build the query with a match all condition
		NativeQuery query = NativeQuery.builder().withQuery(Query.findAll()) // Match all documents
				.withPageable(pageable) // Specify the pageable settings
				.build();

		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				// Extract the content (CricketersDocument) from each hit
				CricketersDocument cricketer = hit.getContent();

				// Add the cricketer to the list
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	// ****************************
	public List<CricketersDocument> sortingQuery2(String fldName1, String fldName2) {

		Sort sort = Sort.by(Sort.Direction.ASC, fldName1 + ".keyword")
				.and(Sort.by(Sort.Direction.DESC, fldName2 + ".keyword"));
		int pageSize = 30;
		Pageable pageable = PageRequest.of(0, pageSize, sort);
		NativeQuery query = NativeQuery.builder().withQuery(Query.findAll()).withPageable(pageable).build();

		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		if (searchHits != null) {
			for (SearchHit<CricketersDocument> hit : searchHits) {
				CricketersDocument cricketer = hit.getContent();
				cricketersList.add(cricketer);
			}
		}
		return cricketersList;
	}

	public List<CricketersDocument> partialMatchQuery(String word) {
		List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
		try {
//			List<String> fieldsToSearch = List.of("battingStyle", "bowlingStyle", "description", "firstName",
//					"lastName", "country", "cricketerId", "matchesPlayed", "runsScored", "wicketsTaken");
			List<String> fieldsToSearch = List.of("battingStyle", "bowlingStyle", "description", "firstName",
					"lastName", "country");
//			.fuzziness("AUTO")
			int pageSize = 100;
			Pageable pageable = PageRequest.of(0, pageSize);
//			NativeQuery query = NativeQuery.builder().withQuery(q -> q.queryString(m -> m.fields("*").query(word))).build();

			NativeQuery query = NativeQuery.builder()
					.withQuery(q -> q.multiMatch(m -> m.fields(fieldsToSearch).query(word).fuzziness("2")))
					.withPageable(pageable).build();

			SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);

			if (searchHits != null) {
				for (SearchHit<CricketersDocument> hit : searchHits) {
					CricketersDocument cricketer = hit.getContent();
					cricketersList.add(cricketer);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cricketersList;
	}
	

//	public List<CricketersDocument> aggregationQuery1() {
//
//	    int pageSize = 100;
//	    Pageable pageable = PageRequest.of(0, pageSize);
//	    Query query = NativeQuery.builder()
//				
//				 .withAggregation("Country", Aggregation.of(a -> a .max(ta ->
//				 ta.field("runsScored.keyword"))))
//				.build();

	    // Perform the search and retrieve search hits
	    //SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
//	    SearchPage<CricketersDocument> searchHits = elasticsearchOperations.multiSearch(query, CricketersDocument.class, "harshal_index_2");
//	    List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
//		if (searchHits != null) {
//			for (SearchHit<CricketersDocument> hit : searchHits) {
//				CricketersDocument cricketer = hit.getContent();
//				cricketersList.add(cricketer);
//			}
//		}
//		return cricketersList;
//	}

}

//AggregationsContainer<?> aggregations = searchHits.getAggregations();
//if (aggregations != null) {
//    // Retrieve the terms aggregation named "byCountries"
//    AggregationContainer<?> aggregationContainer = searchHits.getAggregations("byCountries");
// Process the aggregation results
//if (searchHits != null) {
//    AggregationContainer<T> aggregations = searchHits.getAggregations();
//    if (aggregations != null) {
//        TermsAggregation termsAggregation = aggregations.aggregation("byCountries", TermsAggregation.class);
//        if (termsAggregation != null) {
//            System.out.println("\nTop 10 Countries:");
//            for (TermsAggregation.Bucket bucket : termsAggregation.getBuckets()) {
//                System.out.println("Country: " + bucket.getKeyAsString() + ", Count: " + bucket.getDocCount());
//            }
//        }
//    }
//}
//List<CricketersDocument> cricketersList = new ArrayList<CricketersDocument>();
//if (searchHits != null) {
//	for (SearchHit<CricketersDocument> hit : searchHits) {
//		CricketersDocument cricketer = hit.getContent();
//		cricketersList.add(cricketer);
//	}
//}
//return cricketersList;

//if (searchHits != null) {
//    // Retrieve the Aggregations object
//    Aggregations aggregations = searchHits.getAggregations();
//
//    if (aggregations != null) {
//        // Use the asMap() method to retrieve a map of the aggregations
//        Map<String, Aggregation> aggMap = aggregations.asMap();
//        
//        // Retrieve the "byCountries" aggregation from the map
//        Terms termsAggregation = (Terms) aggMap.get("byCountries");
//        
//        // Check if the aggregation is of type Terms and iterate over buckets
//        if (termsAggregation != null) {
//            System.out.println("\nTop 10 Countries:");
//            for (Terms.Bucket bucket : termsAggregation.getBuckets()) {
//                System.out.println("Country: " + bucket.getKeyAsString() + ", Count: " + bucket.getDocCount());
//            }
//        }
//    }

//*****************************************************************************

