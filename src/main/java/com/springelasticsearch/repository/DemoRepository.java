package com.springelasticsearch.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springelasticsearch.entity.DemoBean;

@Repository
public interface DemoRepository extends JpaRepository<DemoBean, Integer> {

	@Query(value = "select * from cricketers", nativeQuery = true)
	List<Map<String, Object>> findAllRecordsFromDB();
}
