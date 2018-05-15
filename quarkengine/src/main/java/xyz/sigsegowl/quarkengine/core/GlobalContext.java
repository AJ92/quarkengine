package xyz.sigsegowl.quarkengine.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by AJ on 27.10.2014.
 */
public class GlobalContext extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        GlobalContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GlobalContext.context;
    }
}
