//package com.springelasticsearch.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//
//import com.springelasticsearch.entity.CricketersDocument;
//
//@SpringBootTest
//public class SearchService {
//
//	@Autowired
//	private ElasticsearchOperations elasticsearchOperations;
//
////	@Autowired
////	private ElasticsearchClient elasticsearchClient;
//
//	// 1. Using Criteria Query
////	@Test
////	public void basicSearch() {
////		Criteria criteria = new Criteria("firstName").is("Shikhar").and("lastName").is("Dhawan");
////		CriteriaQuery query = new CriteriaQuery(criteria);
////		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
////		// Iterate through the search hits and fetch the JSON data
////		for (SearchHit<CricketersDocument> value : searchHits) {
//////           Fetch the JSON data
////			CricketersDocument jsonData = value.getContent();
////
////			// Process the JSON data as needed
////			System.out.println("JSON Data: " + jsonData.toString());
////		}
////	}
//
//	// 2. Using Native Query
//
////	1. Give players of specified country
//	@Test
//	public void basicSearch() {
////		NativeQuery query = NativeQuery.builder().withQuery(q -> q.match(m -> m.field("country").query("india")))
////				.build();
//
////		NativeQuery query=NativeQuery.builder().withQuery(q->q.multiMatch(m->m.query("opening").fields("description", "battingStyle","bowlingStyle","firstName"))).build();
//
//		NativeQuery query=NativeQuery.builder().withQuery(q->q.queryString(m->m.fields("*").query("virat"))).build();
//
////	Q:	Search by Batting Style and Bowling Style: Find cricketers who are right-handed batsmen and also bowl right-arm fast-medium
//
////		NativeQuery query=NativeQuery.builder()
////				.withQuery(q->q.bool(
////						b->b
////						.must(m->m.match(mt->mt.field("battingStyle").query("Right-handed")))
////								.must(m->m.match(mt->mt.field("bowlingStyle").query("Right-arm fast-medium"))))).build();
//
////		NativeQuery query = NativeQuery.builder()
////			    .withQuery(q -> q
////			        .bool(b -> b
////			            .must(m -> m.match(mt -> mt
////			                .field("battingStyle")
////			                .query("Right-handed")
////			                .operator(Operator.And))) // Specify AND operator
////			            .must(m -> m.match(mt -> mt
////			                .field("bowlingStyle")
////			                .query("Right-arm fast-medium")
////			                .operator(Operator.And))) // Specify AND operator
////			        )
////			    )
////			    .build();
//
////	Question:) Search by Matches Played and Runs Scored: Find cricketers who have played more than 100 matches and scored more than 5000 runs.	
//
////		NativeQuery query = NativeQuery.builder()
////			    .withQuery(q -> q
////			        .bool(b -> b
////			            .filter(f -> f
////			                .range(r -> r
////			                    .field("matchesPlayed")
////			                    .gt(JsonData.of(100)))) // Matches played more than 100
////			            .filter(f -> f
////			                .range(r -> r
////			                    .field("runsScored")
////			                    .gt(JsonData.of(500)))) // Runs scored more than 5000
////			        )
////			    )
////			    .build();	
//
////Question: Find cricketers who have taken more than 200 wickets 
////and have a description mentioning "skilled" or "specialist".		
//
////		NativeQuery query = NativeQuery.builder()
////				.withQuery(
////						q -> q.bool(bt -> bt.filter(ft -> ft.range(r -> r.field("wicketsTaken").gt(JsonData.of(200))))
////								.filter(ft1 -> ft1.bool(b1 -> b1
////										.should(sh1 -> sh1.match(m1 -> m1.query("skilled").field("description")))
////										.should(sh1 -> sh1.match(m1 -> m1.query("specialist").field("description")))))))
////				.build();
////		NativeQuery query = NativeQuery.builder()
////			    .withQuery(q -> q
////			        .bool(b -> b
////			            .filter(f -> f
////			                .range(r -> r
////			                    .field("wicketsTaken")
////			                    .gt(JsonData.of(200)))) // Wickets taken more than 200
////			            .must(m -> m
////			                .multiMatch(mm -> mm
////			                    .query("skilled specialist")
////			                    .fields("description"))) // Description mentioning "skilled" or "specialist"
////			        )
////			    )
////			    .build();
//
////		Question )Find cricketers with the last name starting with "D" and playing for India.
//
////		NativeQuery query = NativeQuery.builder()
////				.withQuery(q -> q.bool(bt -> bt.filter(m -> m.match(mt -> mt.query("india").field("country")))
////						.must(mt2 -> mt2.matchPhrasePrefix(mf -> mf.query("d").field("lastName")))))
////				.build();
//
////		Questions:Find cricketers who bat left-handed and have played fewer than 100 matches.
////		NativeQuery query = NativeQuery.builder()
////				.withQuery(q->q
////						.bool(b->b
////								.filter(f->f
////										.match(m->m
////												.query("left-handed").field("battingStyle")))
////								.filter(f->f
////										.range(m->m.field("matchesPlayed").lt(JsonData.of(100))))
////								
////								)).build();
//
////		NativeQuery query = NativeQuery.builder()
////			    .withQuery(q -> q
////			        .bool(b -> b
////			            .must(m -> m
////			                .match(mt -> mt
////			                    .field("battingStyle")
////			                    .query("Left-handed"))) // Batting style is left-handed
////			            .filter(f -> f
////			                .range(r -> r
////			                    .field("matchesPlayed")
////			                    .lt(JsonData.of(100)))) // Matches played fewer than 100
////			        )
////			    )
////			    .build();
//
////Question: finding the top 5 countries with the highest number of cricketers who have scored more than 5000 runs.	
////		NativeQuery query = NativeQuery.builder()
////				.withQuery(q -> q.range(r -> r.field("runsScored").gt(JsonData.of(5000)))) 
////				.withAggregation("top_countries", Aggregation.of(a -> a.terms(t -> t.field("country.keyword").size(5))))
////				.build();
//		SearchHits<CricketersDocument> searchHits = elasticsearchOperations.search(query, CricketersDocument.class);
//		System.out.println(searchHits);
//		for (SearchHit<CricketersDocument> value : searchHits) {
//
//			CricketersDocument jsonData = value.getContent();
//
//			System.out.println("JSON Data: " + jsonData.toString());
//		}
//	}
//
//}