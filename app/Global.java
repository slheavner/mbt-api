import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.Mode;
import play.api.Play;
import twitter.TwitterThread;

public class Global extends GlobalSettings {



	@Override
	public void onStart(Application arg0) {
		if(Play.current().mode() == Mode.Dev()){

			Object enabled = Play.current().configuration().getBoolean("twitter.enable").get();
			if(enabled instanceof Boolean && (Boolean)enabled){
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
}
