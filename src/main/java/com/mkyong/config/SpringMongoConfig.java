package com.mkyong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {

	@Override
	public String getDatabaseName() {
		return "wapgrg";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient("ec2-54-86-108-177.compute-1.amazonaws.com", 27017);
	}
}