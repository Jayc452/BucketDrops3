package com.jayc6678.bucketdrops.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by sanjeeth on 8/1/2016.
 */

//this class monitors for touches
public class SimpleTouchCallback extends ItemTouchHelper.Callback {


    //Global variable for SwipeListener
    SwipeListener mListener;

    /*  I created this as an alternative to using SwipeListener


    //variable of type AdapterDrops
    //I created this to be used with Constructor for passing the adapter from MainActivity
    AdapterDrops mAdapterDrops;



    //constructor that will accept the Adapter from MainActivity class
    // this is constructor I created.
    //This is an alternative to accepting SwipeListener from MainActivity, as per Vivz example.
    //I figured why not pass the adapter itself.
    // I also created a method called deleteItem in AdapterDrops.java, which is a replica of onSwipe method there which we implemented in AdapterDrops, because we implemented SwipeListner interface.
    //then in this class's onSwiped method, use mAdapterDrops.deleteItem() method instead of using mListener.onSwipe();
    public SimpleTouchCallback(AdapterDrops adapterDrops) {

        mAdapterDrops = adapterDrops;

    }

    */

    //constructor that will accept the Swipelistener from MainActivity class
    //this is constructor vivz created.
    //note that in MainActivity we are passing mAdapter, an object of AdapterDrops, but here we are accepting a SwipeListener object.
    //that is because AdapterDrops class implements SwipeListener interface.
    //this is a behavior of interface
    public SimpleTouchCallback(SwipeListener listener) {

        mListener = listener;
    }

    //tracks movement of the touch
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        //the first param is for the direction in which the item can be dragged. We are not implementing dragging.
        // Hence we got the 1st param as 0
        //2nd param is for the direction in which the item can be swipped. END means right side.
        return makeMovementFlags(0,ItemTouchHelper.END);
    }


    //method to check if LongPress Drag is enabled.
    @Override
    public boolean isLongPressDragEnabled() {

        //we dont want it enabled, so we have return set to false.
        return false;
    }


    //method to check if items cann be swiped.
    @Override
    public boolean isItemViewSwipeEnabled() {

        //we want methods to be swiped, so we return true
        return true;
    }

    //this method relates to dragging, which we dont need right now. hence return is set to false
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    //we are override this method to detect AS A SWIPE IS HAPPENING.
    //and ensure that only DropHolder objects can get swiped
    //FYI. onSwiped method below detects AFTER the swipe has happened.
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if(viewHolder instanceof AdapterDrops.DropHolder){

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    }

    //we are override this method to detect AS A SWIPE IS HAPPENING.
    //and ensure that only DropHolder objects can get swiped
    //FYI. onSwiped method below detects AFTER the swipe has happened.
    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if(viewHolder instanceof AdapterDrops.DropHolder){

            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

    }

    //This method is called AFTER the viewholder is swiped by user. It is not called WHILE swipe is happening but AFTER it has happened.
    //so from this method we need to communicate with the adapter somehow to delete the item from the adapter.
    // We will do this by implementing an interface
    //inside this method, we will need an object that implements the swipe listener, which our AdapterDrops does
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        //we want the user to be able to swipe only if the item is drop
        // and not if it is a footer or "No Items To Show For This Sort"
        if(viewHolder instanceof AdapterDrops.DropHolder){

            //there are 2 types of positions we can get. viewHolder.getAdapterPosition and viewHolder.getLayoutPosition
            // There is a lag of 16ms for when the item AdapterPosition and LayoutPosition is out of sync when the data changes.
            //the adapter may have the latest data set, but the Layout(UI or view) may not be updated and still showing the old items/data set
            mListener.onSwipe(viewHolder.getLayoutPosition());

            Log.d("log ", "onSwiped: " + viewHolder.getLayoutPosition()  + " direction is " + direction);

            //I created this as an alternative to using SwipeListener
//        mAdapterDrops.deleteItem(viewHolder.getLayoutPosition() );



        }

    }
}
