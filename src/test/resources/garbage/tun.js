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