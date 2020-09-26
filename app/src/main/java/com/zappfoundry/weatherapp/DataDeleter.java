package com.zappfoundry.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DataDeleter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper dbhelper=new DatabaseHelper( context );
        dbhelper.deleteAllData();

        MainActivity.getInstance().refreshCachedCityData();
        MainActivity.getInstance().clearCityList();
        Toast toast=Toast.makeText(context,"time elapsed",Toast.LENGTH_SHORT);
        toast.show();
    }
}
