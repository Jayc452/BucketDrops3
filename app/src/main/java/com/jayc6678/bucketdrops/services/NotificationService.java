package com.jayc6678.bucketdrops.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;

import com.jayc6678.bucketdrops.MainActivity;
import com.jayc6678.bucketdrops.R;
import com.jayc6678.bucketdrops.beans.Drop;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */


//this class is called by the AlarmManager in MainActivity
public class NotificationService extends IntentService {

    private static String TAG = "NotificationSerivce.java";


    public NotificationService() {
        super("NotificationService");
        Log.d(TAG, "NotificationService: ");
    }

    //method which is executed to handle intents
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent: ");

        //create a Realm object
        Realm realm = null;


        try {

            //get access to the realm DB
            realm = Realm.getDefaultInstance();

            //find all Drops/goals in RealmDB.
            //note we are using findall instead of findAsyncAll, because we are already in the background thread with this notification service
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("completed", false).findAll();

            //go through all Drops/goals
            for (Drop current : results) {
                if (isNotificationNeeded(current.getAdded(), current.getWhen())) {


                    Log.d(TAG, "onHandleIntent: notifcation needed");

                    //fire a notification
                    fireNotification();

                }
            }

        } finally {
            if (realm != null) {
                realm.close();
            }
        }

    }

    //create a notification
    public void fireNotification(){

        //we got this skeleton from pug notification's github page
        //we can get context using 'this' cause we are inside an intent service and service also extends from context indirectly
        PugNotification.with(this)
                .load()
                .title("Achievement")
                .message("You are nearing your goal")
                .bigTextStyle("Congratualtions you are on the verge of accomplishing your goal")
                .smallIcon(R.drawable.ic_drop)
                .largeIcon(R.drawable.ic_drop)
                .flags(Notification.DEFAULT_ALL)
                //when user clicks on the notification, the notification goes away
              //  .autoCancel(true)
                //when the notification is clicked, we take the user to this file in our app
                .click(MainActivity.class)
                .simple()
                .build();

    }

    //method to check if the Drop is nearing 10% of time left to accomplish
    private boolean isNotificationNeeded(long added, long when) {
        long now = System.currentTimeMillis();
        if (now > when) {
            return false;
        } else {
            long difference90 = (long) (0.9 * (when - added));
            return (now > (added + difference90)) ? true : false;
        }
    }

}
