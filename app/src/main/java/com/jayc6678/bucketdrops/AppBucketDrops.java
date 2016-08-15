package com.jayc6678.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.jayc6678.bucketdrops.adapters.Sorter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sanjeeth on 7/29/2016.
 */

//This class is available to the entire application
//    unlike an activity, an application is never going to be destroyed
//in this class we will set the Realm configuration, so it is available to the entire application
// after this we also have to update the manifest file to let app know that we are using custom application class
public class AppBucketDrops extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //get the configuration
        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();

        //set the real configuration
        Realm.setDefaultConfiguration(configuration);


    }


    //save SharedPreferences
    //this method was initially in MainActivity, we moved it to application class

    public static void save(Context context, int sortOption){

        //because we are in application class and not in an Activity, we cannot use the old way to save shared preference
        //SharedPreferences pref = getPreferences(MODE_PRIVATE);

        //new way to save shared Preferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("sortOption", sortOption);

        //this saves it asynchronously
        editor.apply();


    }


    //Method to get the sort option of he user from shared preferences
    //this method was initially in MainActivity, we moved it to application class

    public static int getSortPreferences(Context context){


        //because we are in application class and not in an Activity, we cannot use the old way to save shared preference
        //SharedPreferences pref = getPreferences(MODE_PRIVATE);

        //new way to save shared Preferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        //1st param is the name under which you have stored the sort option value
        //2nd param is the default value, if sortOption returns null
        int sortOptions = pref.getInt("sortOption", Sorter.NONE);

        //returns the sort option of he user from shared preferences
        return sortOptions;
    }


    //method used to set the custom font
    //1st param is context
    //2nd param is the textView on which we are applying our custom font
    public static void setRalewayRegular(Context context, TextView textView){

        //create an object of TypeFace
        //1st param is the assets folder
        //2nd param is the location of the font inside the assets folder
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");

        //apply the custom font to textViews
        textView.setTypeface(typeface);
    }



    //method used to set the custom font
    //1st param is context
    //2nd param is an array of textViews on which we are applying our custom font

    public static void setRalewayRegular(Context context, TextView... textViews){

        //create an object of TypeFace
        //1st param is the assets folder
        //2nd param is the location of the font inside the assets folder
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");

        //traverse through each of the item in array and apply the typeface
        for(TextView textView : textViews){

            //apply the custom font to "what" TextViews
            textView.setTypeface(typeface);

        }


    }



}



