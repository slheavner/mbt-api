package helpers;

import java.util.List;

/**
 * Created by Sam on 12/22/2015.
 */
public class ConfigHelper {

    private static final play.Configuration config = play.Play.application().configuration();

    private static final String EMAIL = "email";
    private static final String MONGO = "mongo";
    private static final String BUSES = "buses";
    private static final String GOOGLE_MAPS_KEY = "google.maps.key";

    private static final String
            B_IDS = "ids",
            B_NAMES = "names",
            B_NUMBERS = "numbers",
            B_COLORS = "colors",
            B_SERVICE = "service",
            B_FIRSTRUN = "firstrun",
            B_LASTRUN = "lastrun",
            B_RUNTIME = "runtime";

    public static play.Configuration getEmailConfig(){
        return config.getConfig(EMAIL);
    }
    public static boolean getEmailEnabled(){
        return getEmailConfig().getBoolean("enabled");
    }
    public static boolean getEmailName(){
        return getEmailConfig().getBoolean("username");
    }
    public static boolean getEmailPassword(){
        return getEmailConfig().getBoolean("password");
    }


    public static play.Configuration getMongoConfig(){
        return config.getConfig(MONGO);
    }
    public static String getMongoUrl(){
        return getMongoConfig().getString("url");
    }

    public static String getGoogleMapsKey(){
        return config.getString(GOOGLE_MAPS_KEY);
    }

    public static play.Configuration getBusesConfig(){
        return config.getConfig(BUSES);
    }
    public static List<String> getBusIds(){
        return getBusesConfig().getStringList(B_IDS);
    }
    public static List<String> getBusNames(){
        return getBusesConfig().getStringList(B_NAMES);
    }
    public static List<String> getBusNumbers(){
        return getBusesConfig().getStringList(B_NUMBERS);
    }
    public static List<String> getBusServices(){
        return getBusesConfig().getStringList(B_SERVICE);
    }
    public static List<String> getBusFirstRuns(){
        return getBusesConfig().getStringList(B_FIRSTRUN);
    }
    public static List<String> getBusLastRuns(){
        return getBusesConfig().getStringList(B_LASTRUN);
    }
    public static List<String> getBusRuntimes(){
        return getBusesConfig().getStringList(B_RUNTIME);
    }
    public static List<String> getBusColors(){
        return getBusesConfig().getStringList(B_COLORS);
    }


}
