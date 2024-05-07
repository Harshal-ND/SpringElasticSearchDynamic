package com.springelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.springelasticsearch")
public class SpringElasticSearchMethod2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticSearchMethod2Application.class, args);
		
//		String  string1= "Harshal";
//		string1=null;
//		String  string2="Ganesh";
//		
//		System.out.println(string1!=null);
	}

}
