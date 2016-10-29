function(programId,projectIds,verticalIds,startDate,endDate,userIds) {
	var criteria = {};
	criteria.$and = [];
	var statusCrit = {STATUS : { $ne :'D'}};
	criteria.$and.push(statusCrit);
	if(startDate && endDate){
		var dateCriteria={"DUE_DATE":{$exists:true,$gte:ISODate(startDate),$lte:ISODate(endDate)}};
		criteria.$and.push(dateCriteria);
	}
	var tskUnassComplClose={$and: [ {"TASK_STATUS":{$nin:["COMPLETE","UNASSIGNED","CLOSED"]}}]}; 
	criteria.$and.push(tskUnassComplClose);
	if(projectIds){
		var projectIdList = projectIds.split(",");
		var projectObjectIdList = [];
		for(index in projectIdList){
			projectObjectIdList.push(projectIdList[index]);
		}
		criteria.$and.push({"PROJECT.$id":{$in:projectObjectIdList}});
	}

	if(verticalIds){
		var verticalIdList = verticalIds.split(",");
		var verticalObjectIdList = [];
		for(index in verticalIdList){
			verticalObjectIdList.push(verticalIdList[index]);
		}
		criteria.$and.push({"VERTICAL.$id":{$in:verticalObjectIdList}});
	}

	if(userIds){
		var userIdList=userIds.split(",");
		var userObjectIdList = [];
		for(index in userIdList){
			userObjectIdList.push(userIdList[index]);
		}
		criteria.$and.push({"ASSIGNED_TO.$id":{$in:userObjectIdList}});
	}
		
	
	
	
	


	var i=0;
	//print total no of TASK having progIds
	var d1 = db.getCollection('task').aggregate([ 
	                                             {
	                                            	 $match : criteria
	                                             }]).map(
	                                            		 function(data){
	                                            			// print(data['TASK_ID']+"    count="+ ++i);
	                                            			 return data});

	/***         Functions            ***/

	function getUserName(userId){
		var userName=userId;
		if(!userId){
			return userName;
		}
		var userCount=db.getCollection('user').find({$and : [{"_id":ObjectId(userId)},{"STATUS":{$ne:'D'}}]}).count();
		if(userCount>0){
			var userObject=db.getCollection('user').find({$and : [{"_id":ObjectId(userId)},{"STATUS":{$ne:'D'}}]}).toArray();	
			userName=userObject[0]['PERSONAL_DETAILS']['FIRST_NAME']+" "+userObject[0]['PERSONAL_DETAILS']['LAST_NAME'];
		}
		return userName;	
	};

	var outData = db.getCollection('task').aggregate([ 
	                                                  {
	                                                	  $match : criteria

	                                                  }]).map(
	                                                		  function(data){
	                                                			  var taskObj={};
	                                                			  taskObj['TASK_ID']=data['TASK_ID'];   
	                                                			  
	                                                			  taskObj['TASK_STATUS']=data['TASK_STATUS'];  
	                                                			  taskObj['NAME']=data['NAME']; 
	                                                			  taskObj['DUE_DATE']=ISODate(data['DUE_DATE']['$date']).toString();//['$date'];
	                                                			 
	                                                			  if(taskObj['DESCRIPTION'])
	                                                			  taskObj['DESCRIPTION']=data['DESCRIPTION'];
	                                                			  else
	                                                				  taskObj['DESCRIPTION']="";
	                                                			  
	                                                			  taskObj['TASK_STATUS']=data['TASK_STATUS'];
	                                                			  if(data['TOTAL_TIME'])
	                                                				  taskObj['TOTAL_TIME']=data['TOTAL_TIME'].toString();
	                                                			  else
	                                                				  taskObj['TOTAL_TIME']="";

	                                                			  if(data['ASSIGNED_TO'])
	                                                			  { 
	                                                				  print("exist: "+data['ASSIGNED_TO']['$id']);
	                                                				  var user_name_id= (data['ASSIGNED_TO']['$id'])?data['ASSIGNED_TO']['$id']:null;
	                                                				  taskObj['USER_NAME']= getUserName(user_name_id);
	                                                			  }
	                                                			  else
	                                                				  taskObj['USER_NAME']="";

	                                                			  return taskObj; 
	                                                		  });
	//print(outData.count());
	return outData; 


}