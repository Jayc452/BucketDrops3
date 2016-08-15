package com.jayc6678.bucketdrops;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jayc6678.bucketdrops.beans.Drop;
import com.jayc6678.bucketdrops.widgets.CustomDatePickerView;

import java.text.DateFormat;

import io.realm.Realm;

/**
 * Created by sanjeeth on 7/27/2016.
 */

//java class file to inflate the dialog_add fragment
public class DialogAdd extends DialogFragment {


    private static String TAG = "CustomDatePickerView";

    //define the UI elements

    //close button x
    ImageButton btn_close;

    //Drop - what is it?
    EditText whatIsIt;

    //date
//    DatePicker datePicker;

    //define a variable of our CustomDatePickerView we created
    CustomDatePickerView datePicker;


    private Context mContext;

    //Add a Drop button
    Button btn_addDrop;

    // create an instance of an anonymous inner class for on click listener
    View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //get the id of the button that was clicked
            int id= view.getId();

            Log.d("log ", " id clicked was clicked " + id);

            //switch based on the id that was clicked
            switch (id){

                //if id that was clicked was btn_add_it - the 'add a drop' button
                case R.id.btn_add_it:

                    //call addAction to add the drop to our real DB
                    addAction();

                    Toast.makeText(getActivity(), " Add drop button clicked", Toast.LENGTH_LONG);
                    Log.d("log ", " add drop button clicked");
                    break;

            }
            //close the dialog box
            dismiss();
        }
    };

    //method to add a goal or a drop
    private void addAction() {

        Log.d("log ", " inside addAction");
        //get and store the value of the goal or drop
        String what = whatIsIt.getText().toString();

        //get the current time
        long now = System.currentTimeMillis();

        //get the date when the goal is due


/*
//        This was how we were getting the Goal Date when we were using the default DatePicker from android
          Commenting this out now, because we are now using our CustomDatePickerView class for date picker
          which does not contain datePicker.getMonth(), getYear, getDate methods

        //create an object of Calendar
        Calendar calendar = Calendar.getInstance();

        //get the day of the month from our datePicker UI element and store it in Calendar.DAY_OF_MONTH
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        //get themonth from our datePicker UI element and store it in Calendar.MONTH
        calendar.set(Calendar.MONTH, datePicker.getMonth());

        //get the year from our datePicker UI element and store it in Calendar.YEAR
        calendar.set(Calendar.YEAR, datePicker.getYear());

        //we dont need the hour, so we will store it as 0
        calendar.set(Calendar.HOUR, 0);

        //we dont need minutes, so we will store is as 0
        calendar.set(Calendar.MINUTE, 0);

        //we dont need seconds, so we will store is as 0
        calendar.set(Calendar.SECOND,0);



        //calculate the calendar's time in Milliseconds
        //we will pass this as the 3rd parameter when we instantiate our Drop object below and it in our DB
        Long goalDate = calendar.getTimeInMillis();

*/

        //datePicker is of type CustomDatePickerView
        //in our CustomDatePickerView class we have a method called getTime
        Long goalDate = datePicker.getTime();

        Log.d(" log TAG ", "addAction: goalDate is " +   DateFormat.getDateInstance().format(goalDate));


        //create an object of Drop class which will contain all the properties of the drop/goal
        //goalDate is when
        Drop drop = new Drop(what, now, goalDate, false);

        // we set the Realm configuration in AppBucketDrops.java file
        //get an instance of realm
        Realm realm = Realm.getDefaultInstance();

        //begin realm transaction
        realm.beginTransaction();

        //copy the drop to realm db
        realm.copyToRealm(drop);

        //commit the realm transaction
        realm.commitTransaction();

        //close the realm transaction
        realm.close();

    }




    //an empty constructor for this class
    public DialogAdd() {



    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the style for the dialog
        //the 1st param is the dialog style
        //2nd param is the theme we created in styles.xml
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);


    }

    //inflate the dialog_add fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);

    }

    //access all the UI elements in the view after it has been created
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //access the close button x
        btn_close = (ImageButton) view.findViewById(R.id.btn_close);

        //access the add a drop button
        btn_addDrop = (Button) view.findViewById(R.id.btn_add_it);

        //what is the drop
        whatIsIt = (EditText) view.findViewById(R.id.et_drop);




        //apply the custom font to "what is it" TextViews
        AppBucketDrops.setRalewayRegular(getContext(), whatIsIt);




        Log.d("log ", "onViewCreated: ");

        //date
//        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        //use our CustomDatePickerView
        datePicker = (CustomDatePickerView) view.findViewById(R.id.datePicker);


        //set a onClickListener on close button
        btn_close.setOnClickListener(mBtnClickListener);

        //set onclickListener on Add A Drop Button
        btn_addDrop.setOnClickListener(mBtnClickListener);
    }
}
