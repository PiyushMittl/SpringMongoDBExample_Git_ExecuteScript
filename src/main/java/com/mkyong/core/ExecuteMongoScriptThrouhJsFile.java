package com.mkyong.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.data.mongodb.core.script.NamedMongoScript;

import com.mkyong.config.SpringMongoConfig;
import com.mkyong.model.User;
import com.mongodb.BasicDBObject;
//import org.springframework.context.support.GenericXmlApplicationContext;

public class ExecuteMongoScriptThrouhJsFile{

	public static void main(String[] args) {

		// For XML
		//ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");

		// For Annotation
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");


		/*		
		public Object fetchProgramList(String scriptName) {
			return mongoTemplate.scriptOps().call(scriptName);
		}
		*/
		//working
		//Object ob=mongoOperation.scriptOps().call("test1");


		//Working
		//Object ob=mongoOperation.scriptOps().call("test1","57ac5742b8840200cd8f654c","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z");


		//wroking
		//Object ob=mongoOperation.scriptOps().call("filterredTask","57ac5742b8840200cd8f654c","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z");;


		//working//proj
		//Object ob=mongoOperation.scriptOps().call("filterredTask","57ac5742b8840200cd8f654c","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z");;


		//running with vertical ID
		//Object ob=mongoOperation.scriptOps().call("filterredTask","57ac5742b8840200cd8f654c","57ac583cb8840200cd8f654f","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z");

		try{
			
			Resource resource = ctx.getResource("fetchTaskForCalendarView.js");
			StringBuilder text = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));

			
			/***      pick file other that resource file         ***/
			/*
			StringBuilder text = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(new File("C:\\piyush\\tun.js")));
			 */
			
			
			try {
				while (true) {
					String line = br.readLine();
					if (line == null)
						break;
					text.append(line).append("\n");
				}
			} finally {
				try { br.close(); } catch (Exception ignore) {

					System.out.println(ignore);
				}
			}

			
			// ExecutableMongoScript echoScript = new ExecutableMongoScript(text.toString());
			//  Object ob=mongoOperation.scriptOps().execute(echoScript, "57ac5742b8840200cd8f654c","",""); 
			//  System.out.println(ob);


			/*ExecutableMongoScript echoScript = new ExecutableMongoScript("function(x) { return x; }");
	  Object ob=mongoOperation.scriptOps().execute(echoScript, "directly execute script");  
			 */  
			/*  ExecutableMongoScript echoScript = new ExecutableMongoScript(text.toString());
	  Object ob=mongoOperation.scriptOps().execute(echoScript, "57ac5742b8840200cd8f654c","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z"); 
	    System.out.println(ob);
			 */
			ExecutableMongoScript echoScript = new ExecutableMongoScript(text.toString());
			/*  Object ob=mongoOperation.scriptOps().execute(echoScript, "57dece03d8117015ec085f95"); 
			 */	  
			/*** 			echo is the name to register our script in mongo db			   ***/
			mongoOperation.scriptOps().register(new NamedMongoScript("echo", echoScript)); 
			
			
			
			//working for projectids and dates   ("","","proj1,proj2","","sd","ed")
			//Object ob=mongoOperation.scriptOps().call("echo","progId","57ac5742b8840200cd8f654c,57c05498e66a80e633968c47","","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z");    
			
			
			//working for projectid and verticalid and dates ("","","proj1","vert1","sd","ed")
			//Object ob=mongoOperation.scriptOps().call("echo","progId","57ac5742b8840200cd8f654c","57ac583cb8840200cd8f654f","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z","");
			
			
			//testing for loggedin user
			Object ob=mongoOperation.scriptOps().call("echo","","","","2016-10-01T00:00:00.480Z","2016-11-31T23:59:59.480Z","57f0c1d0e66a80e633968d17");
			
			
			/*
		  BasicDBObject obj = new BasicDBObject();
		  Object ob= obj.append("$eval", text.toString());
		    System.out.println(mongoOperation.executeCommand(obj));
			 */    System.out.println(ob);

		}catch(Exception e){
			System.out.println(e);
		}	

		//ExecutableMongoScript echoScript = new ExecutableMongoScript("function(x) { return x; }");
		//Object ob=mongoOperation.scriptOps().execute(echoScript, "directly execute script");    


		//test user id    57decd89d8117015ec085f93
		//Object ob=mongoOperation.scriptOps().call("findUserName","57f0c2c1e66a80e633968d1a");


		System.out.println("hi");
		System.out.println("hi");	
		//System.out.println(ob);


		/*
		User user = new User("mkyong", "password123");

		// save
		mongoOperation.save(user);

		// now user object got the created id.
		System.out.println("1. user : " + user);

		// query to search user
		Query searchUserQuery = new Query(Criteria.where("username").is("mkyong"));

		// find the saved user again.
		User savedUser = mongoOperation.findOne(searchUserQuery, User.class);
		System.out.println("2. find - savedUser : " + savedUser);

		// update password
		mongoOperation.updateFirst(searchUserQuery, Update.update("password", "new password"),
				User.class);

		// find the updated user object
		User updatedUser = mongoOperation.findOne(
				new Query(Criteria.where("username").is("mkyong")), User.class);

		System.out.println("3. updatedUser : " + updatedUser);

		// delete
		mongoOperation.remove(searchUserQuery, User.class);

		// List, it should be empty now.
		List<User> listUser = mongoOperation.findAll(User.class);
		System.out.println("4. Number of user = " + listUser.size());
		 */
	}

}