package helpers;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import models.Bus;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import play.Logger;

import java.net.UnknownHostException;

public class MongoFactory {

	private static Morphia morphia;
	private static MongoClient client;
	private static MongoClientURI uri;
	private static Datastore busDatastore;
	
	public static Morphia getMorphia(){
		if(morphia == null){
			morphia = new Morphia();
			morphia.map(Bus.class);
		}
		return morphia;
	}
	
	public static MongoClient getClient() throws UnknownHostException{
		if(client == null){
			client = new MongoClient(getURI());
		}
		return client;
	}
	
	public static MongoClientURI getURI(){
		if(uri == null){
			String uriString = ConfigHelper.getMongoUrl();
            Logger.debug("mongouri = " + uriString);
            if(uriString == null){
                uri = new MongoClientURI("mongodb://localhost/dev");
            }else{
                uri = new MongoClientURI(uriString);
            }
		}
		return uri;
	}
	
	public static Datastore getBusStore(){
		if(busDatastore == null){
			try {
				busDatastore = getMorphia().createDatastore(getClient(), getURI().getDatabase());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return busDatastore;
	}
	
}
