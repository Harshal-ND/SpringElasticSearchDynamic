package com.springelasticsearch.configuration;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springelasticsearch.repository.DemoRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchConfig {
	@Bean
	ElasticsearchClient elasticsearchClient() {
		// Create credentials provider
		//Using this in case of authentication
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", "")); // not setting username and password as no creds required to connect to our elastic server

		// Create the low-level REST client with authentication
//		localhost == http://192.168.59.170/
		RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("192.168.59.170", 9200, "http"))
				.setHttpClientConfigCallback(
						httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

		// Create RestClient
		RestClient restClient = restClientBuilder.build();

		// Create transport with RestClient and JSON mapper
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// Create and return ElasticsearchClient
		return new ElasticsearchClient(transport);
	}
	
	 

}
