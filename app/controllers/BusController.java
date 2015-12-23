package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.Bus;
import helpers.MongoFactory;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sam on 12/22/2015.
 */
public class BusController extends Controller {

    private static Gson gson;

    private static Gson gson(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }

    public static Result single(String id){
        Result result;
        Bus bus = MongoFactory.getBusStore().find(Bus.class, "_id", id).get();
        if(bus != null){
            result = ok(gson().toJson(bus));
        }else{
            result = notFound();
        }
        return result;
    }

    public static Result collection(){
        Result result;
        JsonNode body = request().body().asJson();
        String[] ids;
        if(body != null){
            ids = gson().fromJson(body.toString(), String[].class);
            Logger.debug("Body ids = " + Arrays.toString(ids));
            result = ok(getBusJson(ids));
        }else{
            String query = request().getQueryString("ids");
            if(query != null){
                Logger.debug("Query = " + query);
                ids = query.split(",");
                result = ok(getBusJson(ids));
            }else{
                result = badRequest();
            }
        }
        return result;
    }

    private static String getBusJson(String...ids){
        String json = "";
        List<Bus> busList = MongoFactory.getBusStore().createQuery(Bus.class).field("_id").hasAnyOf(Arrays.asList(ids)).asList();
        if(busList != null){
            json = gson().toJson(busList);
            Logger.debug("getBusJson = " + json);
        }else{
            Logger.debug("Bus retrieval was null");
        }
        return json;
    }
}
