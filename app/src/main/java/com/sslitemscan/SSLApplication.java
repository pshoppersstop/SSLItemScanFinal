package com.sslitemscan;

import android.app.Application;
import android.content.Context;

public class SSLApplication extends Application {

    private static volatile SSLApplication mInstance;
    private Context mContext;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
    }

    public synchronized static SSLApplication getInstance() {
        if (mInstance == null) { //if there is no instance available... create new one
            synchronized (SSLApplication.class) {
                if (mInstance == null) mInstance = new SSLApplication();
            }
        }
        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }
}
