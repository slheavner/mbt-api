package twitter;

import helpers.MongoFactory;
import models.Bus.Location;
import models.Config;
import models.Config.TestLocation;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import twitter4j.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterParser {

	private static String locationString = "[Bb]us [0-9]{1,3}(.*?)(?:(?: at [0-9]{1,2}/[0-9]{1,2}/[0-9]{4} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2} [AP]M)|[:;])+";
	private static String dateString = "([0-9]{1,2}/[0-9]{1,2}/[0-9]{4} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2} [AP]M)";
	private static String numberString = "[Bb]us ([0-9]{1,3})";
	private static String bluegoldeString = "(.*) at [0-9]{1,2}/[0-9]{1,2}/[0-9]{4} [0-9]{1,2}";
	
	private static Long parseDate(Status status){
		Matcher m = Pattern.compile(dateString).matcher(status.getText());
		if(m.find()){
			String sdate = m.group(1);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss a");
			formatter.withZone(DateTimeZone.forID("EST"));
			DateTime date;
			try{
				date = formatter.parseDateTime(sdate);
			}catch(Exception e){
				date = null;
			}
			if(date != null){
				return date.getMillis();
			}
		}
		return status.getCreatedAt().getTime();
	}
	
	
	public static Location parseLocation(Status status){
		Config config = MongoFactory.getBusStore().find(Config.class).get();
		Location location = new Location();
		Matcher m = Pattern.compile(locationString).matcher(status.getText());
		boolean foundMatch = false;
		if(m.find()){
			String desc = m.group(1).trim();
			if(status.getUser().getScreenName().toLowerCase().equals("ml38bg")){
				Matcher bgMatcher = Pattern.compile(bluegoldeString).matcher(desc);
				if(bgMatcher.find()){
					desc = bgMatcher.group(1);
				}
			}
			if(config != null){
				String id = status.getUser().getScreenName().toLowerCase();
				if(config.getLocationList(id) != null){
					for(TestLocation loc : config.getLocationList(id)){
						if(desc.toLowerCase().contains(loc.getTest().toLowerCase())){
							location.setDesc(loc.getDesc());
							location.setLat(loc.getLat());
							location.setLon(loc.getLon());
							foundMatch = true;
							break;
						}
					}
				}
			}
			if(!foundMatch){
				location.setDesc(desc.trim());
				addMissing(status.getUser().getScreenName().toLowerCase(), location.getDesc());
			}
		}else{
			location.setDesc(status.getText());
		}
		m = Pattern.compile(numberString).matcher(status.getText());
		if(m.find()){
			try{
				location.setBus(Integer.parseInt(m.group(1)));
			}catch(Exception e){
				location.setBus(0);
				e.printStackTrace();
			}
		}else{
			location.setBus(0);
		}
		location.setTime(parseDate(status));
		return location;
	}
	

	
	private static void addMissing(String id, String desc){
		Config c = MongoFactory.getBusStore().find(Config.class).get();
		if (c != null){
			c.addMissingLocation(id, desc);
			MongoFactory.getBusStore().save(c);
		}
	}
}
