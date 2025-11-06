package com.example.settingsbindingarbitrarysercicespoc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyNotExportedService extends Service {
    public MyNotExportedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("evil", "MyNotExportedService.onBind()");
        return null;
    }
}