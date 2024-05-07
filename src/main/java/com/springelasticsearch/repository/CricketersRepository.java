package com.springelasticsearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springelasticsearch.entity.Cricketers;

@Repository
public interface CricketersRepository extends JpaRepository<Cricketers, Integer> {

}
