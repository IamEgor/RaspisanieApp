package com.example.raspviewproj;

import android.app.Application;
import db.MyPrefs;

/**
 * Created by Егор on 02.05.2015.
 */
public class RaspisanieApplication extends Application {
    @Override
    public void onTerminate() {
        MyPrefs.setPrefs(getApplicationContext(), MenuActivity.STATUS_NOT_UPDATING, false);
        super.onTerminate();
    }

}
