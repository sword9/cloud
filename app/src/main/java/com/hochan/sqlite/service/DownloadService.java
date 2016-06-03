package com.hochan.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Future;

public class DownloadService extends Service {

    public static final String URL = "url";
    public static final String PATH = "path";

    private HashMap<String, Future<File>> mDownloadList;

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadList = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra(URL);
        String path = intent.getStringExtra(PATH);
        File file = new File(path);

        //Future<File> downloading = Ion
          //      .with(this)
            //    .load(url)
            //    .write()
        System.out.println(url+" "+path);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
