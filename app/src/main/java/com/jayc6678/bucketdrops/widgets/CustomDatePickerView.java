package com.jayc6678.bucketdrops.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayc6678.bucketdrops.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sanjeeth on 8/9/2016.
 */

// this class is for our custom_date_picker.xml
//    we are extending LinearLayout because custom_date_picker.xml uses LinearLayout and we need to access the UI elements there
public class CustomDatePickerView extends LinearLayout implements View.OnTouchListener {


    //define the UI elements we need to access from custom_date_picker.xml
    private TextView mTextDate;
    private TextView mTextMonth;
    private TextView mTextYear;

    //used to format date - to show as JAN, FEB etc in our custom date widget
    private SimpleDateFormat mFormatter;

    //define a variable of type Calendar
    private Calendar mCalendar;

    //define TAG which can be used in our log statements
    private static String TAG = "CustomDatePickerView";


    //getCompoundDrawables returns Drawable array, with 1st position for drawableLeft, 2nd position for DrawableTop
    //we are creating these constants to be used in hasDrawableTop and hasDrawableBottom methods
    private static int LEFT = 0;
    private static int TOP = 1;
    private static int RIGHT = 2;
    private static int BOTTOM = 3;

    //this is unique id we use when use mHandler.sendEmptyMessageAtTime
    private int MESSAGE_WHAT= 123;



    //boolean variables that control if things should be incremented or decremented, when the views are touched
    private boolean mIncrement;
    private boolean mDecrement;


    //this is the id of the textView whose up or down arrows are being pressed.
    //this is used in Handler loop below
    private int mActiveId;

    //delay set for sending messages to the handler
    private static int DELAY = 250;

    //this is from android.os package
    //create a handler
    //everytime this handler receives a message, it runs.
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            //if the mIncrement is set to true...
            if(mIncrement){

                increment(mActiveId);

            }//if the mDecrement is set to true...
            else if(mDecrement){

                decrement(mActiveId);

            }
            if(mIncrement || mDecrement){

                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            }
            Log.d(TAG, "handleMessage: " + " message received");
            return true;
        }
    });

    //this is used if you want to create you Custom Date Picker view only in code
    //in built method which we implemented
    public CustomDatePickerView(Context context) {
        super(context);

        //call init method
        init(context);
    }


    //  this method is used if yu want custom date picke view to be initialized from xml
    //in built method which we implemented
    public CustomDatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //call init method
        init(context);
    }

    //  this method is used if yu want custom date picke view to be initialized from xml
    //in built method which we implemented
    public CustomDatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //call init method
        init(context);
    }

    /*
    //this needs a minimum API version of 21. so Viviz deleted it. we will comment it out
    public CustomDatePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    */


    //this is lifecyle method
    //it is called before this class is destroyed when the user rotates the screen
    //we will use this method to save the current state of the app
    @Override
    protected Parcelable onSaveInstanceState() {

        //bundle is just like a bag into which you can store or retrieve data
        //bundle implements the parcelable interface, so we can return Bundle from this method
        Bundle bundle = new Bundle();

        //save the state of our LinearLayout of this screen inside our Bundle
        //this will store all the data related to the view
        bundle.putParcelable("stateOftheView", super.onSaveInstanceState());


        //save our data in the bundle
        //get it from mCalendar
        bundle.putInt("date", mCalendar.get(Calendar.DATE));
        bundle.putInt("month", mCalendar.get(Calendar.MONTH));
        bundle.putInt("year", mCalendar.get(Calendar.YEAR));

        //Bundle implement parcelable interface, so Bundle can be returned, even though return type of this method is parcelable
        return bundle;

    }


    //this is lifecyle method
    //it is called when this CustomDatePicker is created
    //we will use this method to retrieve the state of the app that we saved in onSaveInstanceState and reuse it
    //to restore the state
    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        //in onSavedInstance method we returned a Bundle
        //here we are accepting that as input and checking if it is of type Parcelable
        //Bundle class implement Parcelable interface, so they are compatible and be considered to be of type Parcelable also.

        if(state instanceof Parcelable){


            //store the state as a bundle and typecast it
            Bundle bundle  = (Bundle) state;

            //retrieve the view data from the Bundle
            //this is passed to super.onRestoreInstanceState(state) below
            state = bundle.getParcelable("stateOftheView");

            //get our data
            int date = bundle.getInt("date");
            int month = bundle.getInt("month");
            int year = bundle.getInt("year");

            //send this data to the update method to update the
            update(date, month, year, 0,0,0);


        }

        //return its view data
        super.onRestoreInstanceState(state);


    }




    // method we created to initialize everything
    private void init(Context context){

        //inflate custom_date_picker_view.xml
        View view = LayoutInflater.from(context).inflate(R.layout.custom_date_picker_view, this );

        //get an instance of Calendar object
        mCalendar = Calendar.getInstance();


        //create an object of SimpleDateFormat
        //pass MMM as param to indicate formatting for month
        //MMM will give us month as Jan, MMMMM will give us month as January, MM will give month as 01
        //similar we can also get date, time etc.
        mFormatter = new SimpleDateFormat("MMM");

    }


    //use this method to access the UI elements in our layout
    //we could have used view in the init method to access UI elements too, but this method is called after inflating is completed.
    //so we will get access to ALL the child views
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //'this' refers to the LinearLayout, cause we are currently in LinearLayout

        //access the day UI element
        mTextDate = (TextView) this.findViewById(R.id.tv_day);

        //access the month UI element
        mTextMonth = (TextView) this.findViewById(R.id.tv_month);

        //access the year  UI element
        mTextYear = (TextView) this.findViewById(R.id.tv_year);

        //setOnTouchListener on the UI elements

        mTextDate.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);

        //get the current date from the Calendar
        int date = mCalendar.get(Calendar.DATE);

        //get current month
        int month = mCalendar.get(Calendar.MONTH);

        //get current year
        int year = mCalendar.get(Calendar.YEAR);

        //pass the date, month and year to update method which will update the UI
        update(date, month, year, 0, 0, 0);


    }




    //update the UI elements with the current date
    private void update(int date, int month, int year, int hour, int minute, int second){

        mCalendar.set(Calendar.DATE, date);

        mCalendar.set(Calendar.MONTH, month);

        mCalendar.set(Calendar.YEAR, year);


        //+"" is to convert from int to string
        mTextDate.setText(date+"");

        //+"" is to convert from int to string
        mTextMonth.setText(month+"");

        //use the formatter we defined above in init method to extract the month from
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));

        //+"" is to convert from int to string
        mTextYear.setText(year+"");


    }

    //get time
    //this is called from DialogAdd class to get the goalDate
    //whatever is the date and time that is set on mCalendar is returned
    public long getTime(){

        formatDate(mCalendar.getTimeInMillis());

        return mCalendar.getTimeInMillis();

    }

    static void formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        Log.i(" log ", " date is " + DateFormat.getDateInstance().format(date)) ;
    }

    //this method is implemented when we implemented the View.OnTouchListener on this class
    //used to detect and handle touch events
    //1st param is the view that is touched
    //2nd param is the event (down, up, cancel, move)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        //get the id of the view that was clicked and switch based on it
        //we have 3 text views for date, month and year
        switch(view.getId()){

            case R.id.tv_day:

                 //pass the textView UI element and  motionEvent to processEventsFor method which will handle how to deal with it
                 processEventsFor(mTextDate, motionEvent);

                break;

            case R.id.tv_month:

                //pass the textView UI element and  motionEvent to processEventsFor method which will handle how to deal with it
                processEventsFor(mTextMonth, motionEvent);

                break;

            case R.id.tv_year:

                //pass the textView UI element and  motionEvent to processEventsFor method which will handle how to deal with it
                processEventsFor(mTextYear, motionEvent);

                break;

        }


        //needs to return true if we need touch listener to work
        return true;
    }



    //this method will handle how the click on the particular text view should be dealt with
    //the 1st param is the textView that was touched
    //the 2nd param is the event (up, down, move, cancel) etc
    //Based on textView and on which position (up or down) clicked , we will increment or decrement the values in the textViews
    private void processEventsFor(TextView textView, MotionEvent event){

        //get the bounds or the area that each compoundDrawable takes
        //store it inside Drawable array
        //compoundDrawables is how we drew the up & down  arrows on textView and attached it to textView using drawableTop/Bottom
        //getCompoundDrawables() returns values for drawableLeft, drawableTop, drawableRight and drawableBottom
        //drawables[0] is drawableLeft, drawable[1] is drawableTop,drawables[2] is drawableRight, drawables[3] is drawableBottom
        //drawableTop, left, right, bottom all correspond to the drawable we used in the TextView when we drew the up and down arrows
        Drawable[] drawables = textView.getCompoundDrawables();

        //set the mActive variable
        mActiveId = textView.getId();

        //Check if we have top and bottom drawable set for the textView
        if(hasDrawableTop(drawables) && hasDrawableBottom(drawables)){

            //get bounds of the top drawable (drawableTop in our xml file)
            Rect topBounds = drawables[TOP].getBounds();

            //get bounds for the bottom drawable (drawableBottom in our xml file)
            Rect bottomBounds = drawables[BOTTOM].getBounds();

            //get co ordinates of our motion event.
            // This is the location where our touch has occured
            float x = event.getX();
            float y = event.getY();

            //decide which drawable was clicked (top or bottom) and increment or decrement values in the textview accordingly

           // if top drawable = Up arrows was touched, then increment the values
            if(topDrawableHit(textView, topBounds.height(), x, y)){

                Log.d(TAG, "processEventsFor: topDrawable was hit");

                //motionEvent.action_up is when we STOP incrementing values continuously,
                // cause it is triggered when touch has stopped occurring

                //if action_down is triggered = textView is pressed
                 if(isActionDown(event)){

                     //increase the value in this particular textView
                     increment(textView.getId());

                     //set mIncrement to be true to indicate that we need to increment the value
                     mIncrement = true;

                     //remove any messages
                     mHandler.removeMessages(MESSAGE_WHAT);


                     //1st param is identifier for this message,that we can use to distinguish between different messages later
                     //2nd param is the delay time in milli seconds
                     //Based on the delay, it will call the handleMessage method in mHandler periodically
                     //so as long as touch is occurring we will call the handleMessage method in mHandler and increment value
                     //of the textView periodically, based on the delay we have set here (2nd param)
                     mHandler.sendEmptyMessageAtTime(MESSAGE_WHAT, 250);


                     //change the drawableTop to show up_pressed.xml when the up arrow is clicked on the textView
                     toggleDrawable(textView, true);

                 }  //if action_up or cancel is triggered ...
                if(isActionUpOrCancel(event)){

                    //set mIncrement to false to stop incrementing values
                    mIncrement = false;


                    //change the drawableTop to show up_normal.xml when the up arrow is not clicked anymore
                    toggleDrawable(textView, false);
                }

            }
            // if bottom drawable = down arrows are touched, then decrement the values
            else if(bottomDrawableHit(textView, bottomBounds.height(), x,y)){


                //motionEvent.action_up is when we STOP incrementing values continuously,
                // cause it is triggered when touch has stopped occurring

                //if action_down is triggered = textView is pressed
                if(isActionDown(event)){

                    //decrease the value in this particular textView
                    decrement(textView.getId());

                    //set mDecrement to true
                    mDecrement = true;

                    //remove any messages
                    mHandler.removeMessages(MESSAGE_WHAT);


                    //1st param is identifier for this message, that we can use to distinguish between different messages later
                    //2nd param is the delay time in milli seconds
                    //Based on the delay, it will call the handleMessage method in mHandler periodically
                    //so as long as touch is occurring we will call the handleMessage method in mHandler and decrement value
                    //of the textView periodically, based on the delay we have set here (2nd param)
                    mHandler.sendEmptyMessageAtTime(MESSAGE_WHAT, 100);


                    //change the drawableTop to show down_pressed.xml when the down arrow is clicked on the textView
                    toggleDrawable(textView, true);

                }
                else if (isActionUpOrCancel(event)){

                    //set mDecrement to false to stop decrementing values
                    mDecrement = false;


                    //change the drawableTop to show down_normal.xml when the down arrow is not clicked anymore
                    toggleDrawable(textView, false);
                }

//                Log.d(TAG, "processEventsFor: bottom drawable was hit ");

            } //if neither top nor down arrows are touched, then set both mIncrement and mDecrement values as false
            else{


                mIncrement = false;
                mDecrement = false;


                //change the drawableTop to show down_normal.xml when the down arrow is not clicked anymore
                toggleDrawable(textView, false);

            }
        }

    }


    //method to increase the value in the textView
    private void increment(int id){

        switch (id){

            case R.id.tv_day:

                //add 1 to the date
                mCalendar.add(Calendar.DATE, 1);
                break;

            case R.id.tv_month:

                //add 1 to the month
                mCalendar.add(Calendar.MONTH, 1);
                break;

            case R.id.tv_year:

                //add 1 to the year
                mCalendar.add(Calendar.YEAR, 1);
                break;

        }

        //update the textView with the new values after increment them
        //mCalendar has the latest values, so lets us it to update the textView
        set(mCalendar);

    }

    //method to update the textView
    private void set(Calendar mCalendar) {

        //get the values from the calendar

        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        //update the textViews
        mTextDate.setText(date+"");
        mTextYear.setText(year+"");

        //use the formatter to display the month as Jan
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));


    }


    //method to decrease the value in the textView
    private void decrement(int id){

        switch (id){

            case R.id.tv_day:

                //minus 1 frm the date
                mCalendar.add(Calendar.DATE, -1);
                break;

            case R.id.tv_month:
                //minus 1 frm the month
                mCalendar.add(Calendar.MONTH, -1);
                break;

            case R.id.tv_year:
                //minus 1 frm the year
                mCalendar.add(Calendar.YEAR, -1);
                break;

        }

        //update the textView with the new values after increment them
        //mCalendar has the latest values, so lets us it to update the textView
        set(mCalendar);

    }


    //method to toggle the drawable based on the status - if it is pressed or not
    //1st param is the textView on which it should change the drawable
    //2nd param is boolean to indicate weather the textView is pressed or not
    private void toggleDrawable(TextView textView, boolean pressed){

        if(pressed){  // a textView Drawable is pressed

            if(mIncrement){

                //change the drawable on the textView
                //1st param is drawable we are to use for drawableLeft
                //2nd param is drawable to use for drawableTop
                //3rd param is drawable to use for drawableRight
                //4th param is drawable to use for drawableBottom
                //note that we are changing the drawable to be up_pressed for the drawableTop.
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_pressed, 0, R.drawable.down_normal);


            }
            if(mDecrement){

                //1st param is drawable we are to use for drawableLeft
                //2nd param is drawable to use for drawableTop
                //3rd param is drawable to use for drawableRight
                //4th param is drawable to use for drawableBottom
                //note that we are changing the drawable to be up_pressed for the drawableTop.
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_pressed);


            }

        }
        else{  // a textView Drawable is not pressed

            //1st param is drawable we are to use for drawableLeft
            //2nd param is drawable to use for drawableTop
            //3rd param is drawable to use for drawableRight
            //4th param is drawable to use for drawableBottom
            //note that we are changing the drawable to be up_pressed for the drawableTop.
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_normal);


        }
    }



    //method to check if the event is ACTION_DOWN - or touch has occured
    private  boolean isActionDown(MotionEvent event){

        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    //method to check if event is ACTION_UP or ACTION_CANCEL
    private boolean isActionUpOrCancel(MotionEvent event){

        //return true if event.getAction is up or cancel
        //meaning the user has removed their finger from screen
        // or if the scrolling has reached end of scroll and android has triggered a cancel
        return event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    //method to check if drawableTop was touched
    //1st param is the textView
    //2nd param is drawableHeight
    //3 & 4th params are x & y co ordindates where the touch occured
    private boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y){



        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + drawableHeight;

        //x should be between xmin and xmax, y should be between ymin and ymax
        return x > xmin && x <xmax && y > ymin && y < ymax ;
    }

    //method to check if drawableBottom was touched
    //1st param is the textView
    //2nd param is drawableHeight
    //3 & 4th params are x & y co ordindates where the touch occured
    private boolean bottomDrawableHit(TextView textView, int drawableHeight, float x, float y){


        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymax = textView.getHeight() - textView.getPaddingBottom();
        int ymin = ymax - drawableHeight;


        //x should be between xmin and xmax, y should be between ymin and ymax
        return x > xmin && x <xmax && y > ymin && y < ymax ;

    }
    //this method will return true if drawableTop is not null
    //this relates to our textViews in CustomDatePickerView
    //
    private boolean hasDrawableTop(Drawable[] drawables){

        //returns true if drawables[TOP]  is not null
        //TOP = 1, which is the position of drawableTop iin drawables array
        return drawables[TOP] != null;

    }

    //this method will return true is drawableBottom is not null
    //this relates to our textViews in CustomDatePickerView
    private boolean hasDrawableBottom(Drawable[] drawables){

        //returns true if drawables[BOTTOM]  is not null
        //BOTTOM = 3, which is the position of drawableTop iin drawables array
        return drawables[BOTTOM] != null;

    }
}
