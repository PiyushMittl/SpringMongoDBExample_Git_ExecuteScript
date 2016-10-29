function(projectIds,startDate,endDate) {

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

        
       
       	
	


	var outData = db.getCollection('task').aggregate([ 
	                                                  {
	                                                	  $match : criteria

	                                                  }]).map(
	                                                		  function(data){
	                                                			  var taskObj={};
	                                                			  taskObj['TASk_ID']=data['TASK_ID'];   
                                                                                    taskObj['TASK_STATUS']=data['TASK_STATUS'];  

	                                                			  return taskObj; 
	                                                		  });
	return outData; 


}