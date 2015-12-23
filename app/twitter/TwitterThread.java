package twitter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import helpers.MBTMail;
import models.Bus;
import models.Bus.Location;
import models.ConfigArrays;
import helpers.MongoFactory;
import org.joda.time.DateTime;
import org.mongodb.morphia.query.Query;
import play.Logger;
import twitter4j.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class TwitterThread extends Thread {
	
	Bus bus;
	long tweetId = 0;
	Twitter twitter;
	ResponseList<Status> statuses;
	long lastupdate = 0, currenttime;
	
	Bus prt;
	boolean newPrt = false;
	
	int[] statusNumbers = {
			
	};
		
	Map<String, Bus> busMap;
	boolean cont = true;
	
	private static TwitterThread thread;
	
	public static void startThread(){
		thread = new TwitterThread();
		thread.start();
	}
	
	public static void stopThread(){
//		thread.cont = false;
	}
	
	
	public TwitterThread(){
		twitter = TwitterFactory.getSingleton();
		busMap = new HashMap<String, Bus>();
		for(int i = 0; i < ConfigArrays.busIds.length; i++){
			busMap.put(ConfigArrays.busIds[i], new Bus(
					ConfigArrays.busIds[i], 
					ConfigArrays.busNames[i], 
					ConfigArrays.busNumbers[i], 
					3, 
					ConfigArrays.busService[i],
					ConfigArrays.busFirstRun[i],
					ConfigArrays.busLastRun[i],
					ConfigArrays.busRunTime[i],
					ConfigArrays.busColors[i]));
		}
		
	}
	
	public void run(){
		startPRTThread();
		checkTwitterList();
		streamTwitter();
		Logger.info("finished twitter thread");
	}
	
	private void streamTwitter(){
		StatusListener listener = new StatusListener(){

			@Override
			public void onException(Exception arg0) {
				Logger.warn("got twitter exception " + arg0.getMessage());
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}

			@Override
			public void onScrubGeo(long arg0, long arg1) {	}

			@Override
			public void onStallWarning(StallWarning arg0) {
				Logger.warn("Got twitter stall warning");
			}
			
			@Override
			public void onTrackLimitationNotice(int arg0) {	}

			@Override
			public void onStatus(Status status) {
				updateStatus(status);
				if(newPrt){
					MongoFactory.getBusStore().save(TwitterThread.this.prt);
					Logger.info("saved new PRT");
                    MBTMail.send("slheavner", "slheavner@gmail.com", "New PRT at " + DateTime.now().toString());
                    newPrt = false;
				}
			}
		
		};
		TwitterStream stream = new TwitterStreamFactory().getInstance();
		stream.addListener(listener);
		stream.filter(new FilterQuery(ConfigArrays.busLongIds));
	}
	
	private void checkTwitterList(){

		try{
			twitter = TwitterFactory.getSingleton();
			for(String id : ConfigArrays.busIds){
				if(!id.equals("prt")){
					ResponseList<Status> s = twitter.getUserTimeline(id);
					checkStatuses(s);
				}
			}
			if(this.newPrt && this.prt != null){
				MongoFactory.getBusStore().save(TwitterThread.this.prt);
                MBTMail.send("slheavner", "slheavner@gmail.com", "New PRT at " + DateTime.now().toString());
				this.newPrt = false;
			}
		}catch(Exception e){
            Logger.error(e.getMessage());
		}
	}
	
	private void checkStatuses(ResponseList<Status> statusList){
		
		if(statusList.size() > 0){
			Logger.info("Returned " + statusList.size() + " new statuses");
			tweetId = statusList.get(0).getId();
		}
		for(Status s : statusList){
			updateStatus(s);
		}
	}
	
	private void updateStatus(Status s){
		String id = s.getUser().getScreenName().toLowerCase();
		Query<Bus> busQuery  = MongoFactory.getBusStore().find(Bus.class, "_id", id);
		if(busQuery.asList().size() == 1){
			bus = busQuery.get();
		}else{
			bus = busMap.get(id);
		}
		bus.updateLocations(s);
		MongoFactory.getBusStore().save(bus);
	}
	
	private void startPRTThread(){
		Thread t = new Thread(){
			
			public void run(){
				while(true){
//					Logger.info("getting prt");
					getPRTStatus();
//					Logger.info("sleeping");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
			
		};
		t.start();
	}
	
	private void getPRTStatus(){
		Bus getPrt = busMap.get("prt");
		try {
//			Logger.info("getting prt url");
			URL url = new URL("https://prtstatus.wvu.edu/cache/"+System.currentTimeMillis()+"/true/?json=true&callback=?");
			InputStream is = url.openStream();
			JsonReader jReader = new JsonReader(new InputStreamReader(is));
			JsonObject json = new JsonParser().parse(jReader).getAsJsonObject();
//			Logger.info(json.toString());
			Location loc = new Location();
			loc.setDesc(json.get("message").getAsString());
			loc.setBus(json.get("status").getAsInt());
			loc.setTime(0);
			loc.setLon(0);
			loc.setLat(0);
			Location[] locations = new Location[3];
			locations[0] = loc;
			getPrt.setLocations(locations);
			if(this.prt == null || !loc.getDesc().equals(this.prt.getLocations()[0].getDesc())){
				this.prt = getPrt;
				this.newPrt = true;
                MBTMail.send("slheavner", "slheavner@gmail.com", "New PRT at " + DateTime.now().toString());
				Logger.info("new PRT");
            }
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		
	}
	
}
