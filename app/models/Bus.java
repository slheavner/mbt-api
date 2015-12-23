package models;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import play.Logger;
import twitter.TwitterParser;
import twitter4j.Status;

@Entity("buses")
public class Bus {

	@Id String id;

	String name, number, service, firstrun, lastrun, runtime, polylineColor;
	
	long updateInfo, updateLocations, updateLine;
	
	@Embedded
	Location[] locations;
	
	@Embedded
	Coordinate[] polyline;
	
	public Bus(){
		
	}
	
	public Bus(String id, String name, String number, int nLoc, String service, String firstrun, String lastrun, String runtime, String polylineColor){
		this.id = id;
		this.name = name;
		this.number = number;
		this.updateInfo = System.currentTimeMillis();
		this.updateLine = System.currentTimeMillis();
		this.updateLocations = System.currentTimeMillis();
		this.firstrun = firstrun;
		this.lastrun = lastrun;
		this.runtime = runtime;
		this.service = service;
		this.polylineColor = polylineColor;
		this.locations = new Location[nLoc];
	}
	
	public void updateLocations(Status status){
		Location parsedLocation = TwitterParser.parseLocation(status);
		int index = findLocationByBus(parsedLocation.bus);
		if(index != -1){
			if(locations[index].getTime() < parsedLocation.getTime()){
				locations[index] = parsedLocation;
				Logger.info(this.id + " :: Sucessfuly updated status");
			}else{
				Logger.info("location update failed for " + this.id);
				Logger.info(locations[index].getTime() + " > " + parsedLocation.getTime());
			}
		}else{
			for(int i = 0; i < locations.length; i ++){
				Location l = locations[i];
				if(l != null){
					if(l.time < parsedLocation.getTime()){
						locations[i] = parsedLocation;
						Logger.info(this.id + " :: Sucessfuly updated status");
						break;
					}else{
						Logger.info("location update failed for " + this.id);
						Logger.info(l.time + " > " + parsedLocation.getTime());
					}
				}else{
					locations[i] = parsedLocation;
					break;
				}
			}
		}
		locations = sortLocations();
	}
	
	private int findLocationByBus(int bus){
		if(bus == 0){
			return -1;
		}
		for(int i = 0; i < locations.length; i++){
			if(locations[i] != null && locations[i].getBus() == bus){
				return i;
			}
		}
		return -1;
	}
	
	private Location[] sortLocations(){
		Location tLocation = null;
		Location[] tlocs = locations;
		for(int i = 1; i < tlocs.length; i ++){
			for(int j = i - 1; j < tlocs.length - 1; j ++){
				if(tlocs[j+1] == null || tlocs[j] != null &&
						tlocs[j].getTime() > tlocs[i].getTime()){
					tLocation = tlocs[j];
					tlocs[j] = tlocs[j+1];
					tlocs[j+1] = tLocation;
				}
			}
		}
		return tlocs;
	}
	
	@Embedded
	public static class Coordinate{
		double lat;
		double lon;
		
		public Coordinate(){
			
		}
		
		public Coordinate(double lat, double lon){
			this.lat = lat;
			this.lon = lon;
		}
		
		public double getLat(){
			return lat;
		}
		
		public double getLon(){
			return lon;
		}
	}
	
	public void setPolyline(Coordinate[] coordinates){
		this.polyline = coordinates;
	}
	
	public Coordinate[] getPolyline(){
		return polyline;
	}

	@Embedded
	public static class Location{
		
		String desc;
		double lat;
		double lon;
		long time;
		int bus;
		
		public Location(){
			
		}
		
		public Location(Status status){
			this.desc = status.getText();
			this.time = status.getCreatedAt().getTime();
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double d) {
			this.lat = d;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}
		
		public int getBus(){
			return bus;
		}
		
		public void setBus(int bus){
			this.bus = bus;
		}
		
		
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(long updateInfo) {
		this.updateInfo = updateInfo;
	}

	public long getUpdateLocations() {
		return updateLocations;
	}

	public void setUpdateLocations(long updateLocations) {
		this.updateLocations = updateLocations;
	}

	public long getUpdateLine() {
		return updateLine;
	}

	public void setUpdateLine(long updateLine) {
		this.updateLine = updateLine;
	}

	public Location[] getLocations() {
		return locations;
	}

	public void setLocations(Location[] locations) {
		this.locations = locations;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getFirstrun() {
		return firstrun;
	}

	public void setFirstrun(String firstrun) {
		this.firstrun = firstrun;
	}

	public String getLastrun() {
		return lastrun;
	}

	public void setLastrun(String lastrun) {
		this.lastrun = lastrun;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getPolylineColor() {
		return polylineColor;
	}

	public void setPolylineColor(String polylineColor) {
		this.polylineColor = polylineColor;
	}
	
	
	
}
