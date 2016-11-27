package ru.pvolan;

import android.app.Application;
import android.util.Log;

import ru.pvolan.strip1.n.API;

public class SApp extends Application {


    private static SApp app;


    private API api;

    @Override
    public void onCreate () {
        super.onCreate ();

        app = this;

        api = new API (this);


        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler () {
            @Override
            public void uncaughtException (Thread thread, Throwable ex) {
                Log.e ("Error", ex.toString ());
                ex.printStackTrace ();
            }
        });
    }

    public API getApi () {
        return api;
    }

    public static SApp getApp () {
        return app;
    }
}
