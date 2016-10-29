package com.mkyong.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mkyong.config.SpringMongoConfig;

public class ReadResourceFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");


		try{
			Resource resource = ctx.getResource("tun.js");
			StringBuilder text = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));

			try {
				while (true) {
					String line = br.readLine();
					if (line == null)
						break;
					text.append(line).append("\n");
				}
			} finally {
				try { br.close(); } catch (Exception ignore) {

					System.out.println(text);
				}
			}
			System.out.println(text);
		}
		catch(Exception e){System.out.println(e);}

	}}
