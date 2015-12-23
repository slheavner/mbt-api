package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Bus.Location;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("config")
public class Config {

	@Id() ObjectId id;
	
	@Embedded
	Map<String, List<TestLocation>> locations;
	
	Map<String, List<String>> missingLocations;
	
	public Config(){
		
	}
	
	public Config(boolean b){
		locations = new HashMap<String, List<TestLocation>>();
		for(String id : ConfigArrays.busIds){
			locations.put(id, new ArrayList<TestLocation>());
		}
		id = ObjectId.get();
	}
	
	public List<TestLocation> getLocationList(String busId){
		return locations.get(busId);
	}
	
	public void setLocationList(String id, List<TestLocation> list){
		locations.put(id, list);
	}
	
	public void addLocation(String id, TestLocation desc){
		if(locations.get(id) == null){
			locations.put(id, new ArrayList<TestLocation>());
		}
		locations.get(id).add(desc);
	}
	
	private Map<String, List<String>> safeGetMissing(){
		if(missingLocations == null){
			missingLocations = new HashMap<String, List<String>>();
		}
		return missingLocations;
	}
	
	public void addMissingLocation(String id, String desc){
		safeGetMissing();
		if(missingLocations.get(id) == null){
			missingLocations.put(id, new ArrayList<String>());
		}
		if(!missingLocations.get(id).contains(desc)){
			missingLocations.get(id).add(desc);
		}
	}

	public List<String> getMissingLocations(String id){
		List<String> locs = missingLocations.get(id);
		if(locs == null){
			locs = new ArrayList<String>();
		}
		return locs;
	}
	
	@Embedded
	public class TestLocation extends Bus.Location{
		String test;
		
		public TestLocation(){
			super();
			this.test = "";
		}

		public TestLocation(String desc, String test){

			this.desc = desc;
			this.test = test;
		}
		
		public void setTest(String test){
			this.test = test;
		}
		
		public String getTest(){
			return this.test;
		}
		
	}
	
}
