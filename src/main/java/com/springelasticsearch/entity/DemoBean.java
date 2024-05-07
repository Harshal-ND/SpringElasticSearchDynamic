package com.springelasticsearch.entity;

import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
//@AllArgsConstructor
//@NoArgsConstructor
public class DemoBean {

	@Id
	private int id;

//	private List<Map<String, Object>> mysqlData;
	
	
//	@Convert(converter = MapToGeoJsonLineStringConverter.class)
//	private Map<String, Object> mysqlData;
}
