package com.springelasticsearch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springelasticsearch.entity.Cricketers;
import com.springelasticsearch.repository.CricketersRepository;

@Service
public class FetchFromDBService {

	@Autowired
	private CricketersRepository cricketersRepository;

	public List<Cricketers> fetchData() {

		return cricketersRepository.findAll();
	}
}
