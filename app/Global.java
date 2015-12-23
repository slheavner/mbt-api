import helpers.ConfigHelper;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.Mode;
import play.api.Play;
import twitter.TwitterThread;

import java.io.File;

public class Global extends GlobalSettings {



	@Override
	public void onStart(Application arg0) {
		if(Play.current().mode() == Mode.Dev()){
            checkForPrivateConfig();
            checkConfigValues();
			Boolean enabled = play.Play.application().configuration().getBoolean("twitter.enable", Boolean.FALSE);
			if(enabled){
				TwitterThread.startThread();
			}else{
				Logger.debug("TwitterThread not started, twitter.enable=false");
			}
		}
		super.onStart(arg0);
	}

    @Override
    public void onStop(Application application) {
        super.onStop(application);
        TwitterThread.stopThread();
    }

    private void checkForPrivateConfig(){
        File file = play.Play.application().getFile("/conf/private.conf");
        boolean debug = play.Play.application().isDev();
        if(!file.exists() && debug){
            Logger.warn("You should create /conf/private.conf and set private values there.");
        }
    }

    private void checkConfigValues(){
        checkConfigForChangeme(ConfigHelper.getEmailConfig());
        checkConfigForChangeme(ConfigHelper.getMongoConfig());
        if(ConfigHelper.getGoogleMapsKey().equals("changeme")){
            Logger.warn("google.maps.key should be set in /conf/private.conf");
        }
    }

    private void checkConfigForChangeme(play.Configuration config){
        for(String key : config.keys()){
            if(config.getString(key).equals("changeme")){
                Logger.warn(key + " should be set in /conf/private.conf");
            }
        }
    }
}
