package com.jayc6678.bucketdrops.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jayc6678.bucketdrops.extras.Utils;


//this is class that restarts the AlarmManager and the notification service
//this class is linked to the broadcast <receiver> in the Manifest file
public class MyReceiver extends BroadcastReceiver {

    public static final String TAG = "MyReceiver log ";

    //hello

    public MyReceiver() {

        Log.d(TAG, "MyReceiver: ");
    }

    //method that handles when broadcast receiver receives a message
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d(TAG, "onReceive: ");

        //execute the schedule Alarm method that will trigger the notificationService to check if there are any soon ending goals.
        Utils.scheduleAlarm(context);
        Log.d(TAG, "onReceive: hello");

        //blah blah


    }
}
