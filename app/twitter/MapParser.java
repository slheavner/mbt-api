package twitter;

import helpers.MongoFactory;
import models.Bus;
import models.Bus.Coordinate;
import models.Config;
import models.Config.TestLocation;
import models.ConfigArrays;
import org.apache.commons.io.IOUtils;
import play.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MapParser {

	private static String polylinePattern = "<LineString>.*?<coordinates>(.*?)</coordinates>";
	private static String placemarkPattern = "<Placemark>(.*?)</Placemark>";
	private static String placeNamePattern = "<name>(.*?)</name>";
	private static String placeDescPattern = "<description><!\\[CDATA\\[(.*?)\\]\\]></description>";
	private static String placePointPattern = "<Point>.*?<coordinates>(.*?,.*?),0\\.0</coordinates>";
	private static String latlonPattern = "(.*?),0.0\\s"; 
	private static String[] mapurls = {
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kUskxeI12cYQ", //campus
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kA2PlW1PQJts", //mall
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kFfJ7mZrPCvg", //green
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kSyfrPRAeIjA", //orange
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kkPqg6y9t12E", //gold
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kZJq3xXr2Yvg", //red
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.k6jWXVl2V8Ok", //tyrone
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kWvacVreLKrg", //purplepink
		 "", //cass
		 "", //blue
		 "", //brown
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kWc1mLhqctQ0", //west
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.kkpG4Gmig-9Q", //bluegold
		 "https://mapsengine.google.com/map/kml?mid=zMvDO9aLEkVU.k2zZgCenwp54"  //valleyview
	};
	
	private static void getKML(String id, String url) throws MalformedURLException, IOException{
		System.out.println("getting kml for " + id);
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		URL mUrl = new URL(url);
		String kml = IOUtils.toString(mUrl.openStream(), "UTF-8");
		Matcher m = Pattern.compile(polylinePattern, Pattern.DOTALL).matcher(kml);
		if(m.find()){
			Logger.debug("found polyline");
			String coordsString = m.group(1);
			Matcher coordsMatcher = Pattern.compile(latlonPattern).matcher(coordsString);
			String latlonString;
			String[] doubles;
			double lat;
			double lon;
			while(coordsMatcher.find()){
				latlonString = coordsMatcher.group(1);
				try{
					doubles = latlonString.split(",");
					lat = Double.parseDouble(doubles[1]);
					lon = Double.parseDouble(doubles[0]);
					coordinates.add(new Coordinate(lat, lon));
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		List<Bus> qlist = MongoFactory.getBusStore().find(Bus.class, "_id", id).asList();
		if(qlist.size() > 0){
			Logger.debug("setting polyline " + coordinates.size());
			Bus bus = qlist.get(0);
			bus.setPolyline(coordinates.toArray(new Coordinate[]{}));
			MongoFactory.getBusStore().save(bus);
		}
		m = Pattern.compile(placemarkPattern, Pattern.DOTALL).matcher(kml);
		String placemark, name, point, coords;
		String[] coordsArray;
		double lat, lon;
		TestLocation location;
		Matcher mPoint, mName, mTest;
		List<TestLocation> list = new ArrayList<TestLocation>();
		Config config = MongoFactory.getBusStore().find(Config.class).get();
		System.out.println("config from database = " + config);
		if(config == null){
			config = new Config(true);
		}
		while(m.find()){
			placemark = m.group(1);
			if(placemark.contains("<Point>")){
				location = config.new TestLocation();
				mPoint = Pattern.compile(placeNamePattern, Pattern.DOTALL).matcher(placemark);
				mTest = Pattern.compile(placeDescPattern, Pattern.DOTALL).matcher(placemark);
				if(mPoint.find()){
					location.setDesc(mPoint.group(1));
				}
				if(mTest.find()){
					location.setTest(mTest.group(1));
				}
				mName = Pattern.compile(placePointPattern, Pattern.DOTALL).matcher(placemark);
				if(mName.find()){
					try{
						coordsArray = mName.group(1).split(",");
						location.setLat(Double.parseDouble(coordsArray[0]));
						location.setLon(Double.parseDouble(coordsArray[1]));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				list.add(location);
			}
		}
		config.setLocationList(id, list);
		MongoFactory.getBusStore().save(config);
	}
	
	public static void getAllMaps() throws MalformedURLException, IOException{
		for(int i = 0; i < mapurls.length; i++){
			if(!mapurls[i].isEmpty()){
				try{
					getKML(ConfigArrays.busIds[i], mapurls[i]);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
}
