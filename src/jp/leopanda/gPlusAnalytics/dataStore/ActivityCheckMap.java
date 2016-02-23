package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

/**
 * アクテビティエンティティの内容をチェックするために使用するMap
 * @author LeoPanda
 *
 */
public class ActivityCheckMap{
	Map<String, Elements> map;
	public ActivityCheckMap(){
		this.map = new HashMap<String, Elements>();
	}
	public void put(String activityId,Integer numOfPlusOne,Key entityKey){
		this.map.put(activityId, new Elements(numOfPlusOne,entityKey));
	}
	public Integer getNumOfPlusOne(String activityId){
		Elements elements = map.get(activityId);
		if(elements == null){
			return null;
		}
		return elements.numOfPlusOne;
	}
	public Key getEntityKey(String activityId){
		Elements elements = map.get(activityId);
		if(elements == null){
			return null;
		}
		return elements.entityKey;
	}
		
	private class Elements{
		public Integer numOfPlusOne;
		public Key entityKey;
		Elements(Integer numOfPlusOne,Key entityKey){
			this.numOfPlusOne = numOfPlusOne;
			this.entityKey = entityKey;
		}
	}

}
