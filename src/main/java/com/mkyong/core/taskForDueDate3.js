function(projectIds,verticalIds,startDate,endDate) {

	var criteria = {};

	criteria.$and = [];

	var statusCrit = {STATUS : { $ne :'D'}};
	if(startDate && endDate){
		var dateCriteria={"DUE_DATE":{$exists:true,$gte:ISODate(startDate),$lte:ISODate(endDate)}};
		criteria.$and.push(dateCriteria);
            }	


var tskUnassComplClose={$and: [ {"TASK_STATUS":{$nin:["COMPLETE","UNASSIGNED","CLOSED"]}}]}; 

	criteria.$and.push(statusCrit);
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



	var outData = db.getCollection('task').aggregate([ 
	                                                  {
	                                                	  $match : criteria

	                                                  }]).map(
	                                                		  function(data){
	                                                			  var taskObj={};
	                                                			  taskObj['TASk_ID']=data['TASK_ID'];   
                                                                                  taskObj['TASK_STATUS']=data['TASK_STATUS'];  
                                                                                  taskObj['NAME']=data['NAME']; 
                                                                                  
                                                                                  try {
                                                                                	  var user_name_id= (data['ASSIGNED_TO']['$id'])?data['ASSIGNED_TO']['$id']:null;
                                                                                	  taskObj['USER_NAME']= user_name_id;
                                                                                  } catch (err) {
                                                                                	  taskObj['USER_NAME']= '';
                                                                                	}
                                                                                  
                                                                                  
                                                                                 
                                                                                  

	                                                			  return taskObj; 
	                                                		  });
	return outData; 


}