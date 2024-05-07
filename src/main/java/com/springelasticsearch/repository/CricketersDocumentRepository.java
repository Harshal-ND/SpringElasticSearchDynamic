package com.springelasticsearch.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.springelasticsearch.entity.CricketersDocument;

@Repository
public interface CricketersDocumentRepository extends ElasticsearchRepository<CricketersDocument, Integer> {
//	@Query("{\"query\": {\"term\": {\"battingStyle\": \"left\"}}}")
//	List<CricketersDocument> findLeftHandedCricketers();



}
