package com.springelasticsearch.entity;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestBean {

	private String indexName;
    private List<Map<String, Object>> jsonDataList;
}
