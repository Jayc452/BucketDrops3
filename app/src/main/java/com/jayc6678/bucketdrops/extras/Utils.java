package com.jayc6678.bucketdrops.extras;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.jayc6678.bucketdrops.services.NotificationService;

import java.util.List;

/**
 * Created by sanjeeth on 7/29/2016.
 */
public class Utils {

    //method to show views.
    public static void showViews(List<View> viewList){

        for(View view: viewList){

            //set visibility of the view as VISIBLE
            view.setVisibility(View.VISIBLE);
        }

    }


    //method to show views.
    public static void hideViews(List<View> viewList){

        for(View view: viewList){

            //set visibility of the view as VISIBLE
            view.setVisibility(View.GONE);
        }

    }

    //checks if the version is more than 15 or not and return a boolean value
    public static boolean moreThanJellyBean(){

        return Build.VERSION.SDK_INT > 15;
    }

    //method to set the background of the item based on the version of android
    public static void setBackground(View itemView, Drawable drawable ){

        if(moreThanJellyBean()){

            //if version is more than jellyBean use setBackground
                itemView.setBackground(drawable);
        }
        else{

            //if version is less than jellyBean use setBackgroundDrawable
            itemView.setBackgroundDrawable(drawable);
        }


    }


    //method to create an object of AlarmManager, which will periodically trigger our NotificationService to run.
    //we are passing context from MainActivity
    public static void scheduleAlarm(Context context){

        //create an instance of AlarmManager
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        //create an intent to execute the NotificationService
        Intent intent = new Intent(context, NotificationService.class);

        //create a PendingIntent
        //1st param context
        //2nd param is requestCode - an unique identifier to identify this request if we need to later
        //3rd param is the intent we created above or the action we are looking to perform
        //4th param is how we handle if there are new pendingIntent objects. we are just updating them.
        PendingIntent pendingIntent = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //1st param is type. our selection says, wake up device if it is asleep after a certain time has elapsed and run service
        //2nd param is time after which you want to launch the service the first time
        //3rd param is interval - how often the service should run
        //4th param is the pendingIntent we created above

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 5000, pendingIntent);
    }
}
