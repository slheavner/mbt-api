package controllers;

import com.google.gson.Gson;
import helpers.MBTMail;
import helpers.MongoFactory;
import models.Bus;
import models.Config;
import org.mongodb.morphia.Datastore;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import twitter.MapParser;
import views.html.index;
import views.html.unknownlocations;
import views.html.unknownselect;

import java.util.List;

public class Application extends Controller {

	private static Gson gson;
	
	private static Gson gson(){
		if(gson == null){
			gson = new Gson();
		}
		return gson;
	}
	
    public static Result index() {
        return ok(index.render(""));
    }
    
    public static Result initialize(){
    	response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
    	List<Bus> buses = MongoFactory.getBusStore().find(Bus.class).asList();
    	return ok(gson().toJson(buses));
    }
    
    public static Result bus(String id){
    	List<Bus> buses = MongoFactory.getBusStore().find(Bus.class, "_id", id).asList();
    	return ok(gson().toJson(buses));
    }

    
    public static Result locations(String id){
    	List<Bus> buses = MongoFactory.getBusStore().find(Bus.class, "_id", id).asList();
    	if(buses.size() > 0){
    		Bus bus = buses.get(0);
    		return ok(gson().toJson(bus.getLocations()));
    	}
    	return ok(gson().toJson(buses));
    }
    
    public static Result updateMaps(){
    	try {
			MapParser.getAllMaps();
		}catch (Exception e) {
			e.printStackTrace();
		}
    	return ok();
    }
    
    
    public static Result email(){
    	System.out.println(request().body());
    	DynamicForm form = Form.form().bindFromRequest();
    	if(form.data().size() != 0){
    		System.out.println("got json for support");

    		MBTMail.send(form.get("username"), form.get("useremail"), form.get("message"));
    	}else{
    		System.out.println("No json for support");
    	}
    	return redirect("/");
    }

	public static Result unknown(){
        Datastore busStore = MongoFactory.getBusStore();
        List<Bus> buses = MongoFactory.getBusStore().find(Bus.class).asList();
        Config config = busStore.find(Config.class).get();
        return ok(unknownselect.render(buses, config));
    }

    public static Result updateUnknown(String id){
        Datastore busStore = MongoFactory.getBusStore();
        Bus bus = MongoFactory.getBusStore().find(Bus.class, "_id", id).get();
        Config config = busStore.find(Config.class).get();
        return ok(unknownlocations.render(bus, config));
    }

    public static Result submitUnknown(String id){
//        Datastore busStore = MongoFactory.getBusStore();
//        Config config = busStore.find(Config.class).get();
        DynamicForm form = Form.form().bindFromRequest();
//        List<Config.TestLocation> tests = new ArrayList<Config.TestLocation>();
        return ok(gson().toJson(form.data()).toString());
    }
    
    public static Result redirectPath(){
    	return redirect("/");
    }
    
    class EmailJson{
    	String message;
    	String username;
    	String useremail;
    	
    	EmailJson(){
    		
    	}
    }

}
