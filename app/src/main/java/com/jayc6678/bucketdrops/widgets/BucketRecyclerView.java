package com.jayc6678.bucketdrops.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.jayc6678.bucketdrops.extras.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sanjeeth on 7/29/2016.
 */

//we will replace the default RecyclerView with our custom BucketRecyclerView
//create a class called BucketRecyclerView which extends RecyclerView
//this class is used to show different views/screens based on the availability of the drops.

public class BucketRecyclerView extends RecyclerView {


    //this will contains a list of views that need to be hidden if there are no drops/goals to show
    // Collections.emptyList() is given to avoid null point exception error
    private List<View> hideThisifNoDropsToShow = Collections.emptyList();

    private List<View> showThisifNoDropsToShow = Collections.emptyList();

    //Create an instance of AdapterDataObserver and implement all its methods
    //this monitors for data and triggers a method based on changes
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
         @Override
         public void onChanged() {

             handleViews();

         }

         @Override
         public void onItemRangeChanged(int positionStart, int itemCount) {

             handleViews();
         }

         @Override
         public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
             super.onItemRangeChanged(positionStart, itemCount, payload);
             handleViews();
         }

         @Override
         public void onItemRangeInserted(int positionStart, int itemCount) {
             handleViews();

         }

         @Override
         public void onItemRangeRemoved(int positionStart, int itemCount) {
             handleViews();

         }

         @Override
         public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
             handleViews();

         }
     };



    //method to handle what views should be shown and what should be hidden
    //when there are no Drops/goals to be shown, then we hide the toolbar and show the empty_drops.xml layout
    //these views are stored in hideThisifNoDropsToShow and showThisifNoDropsToShow
    private void handleViews() {

//        if(getAdapter()!=null && )

        //if there are no items in the adapter
        if(getAdapter().getItemCount() == 0){

            // go through the list hideThisifNoDropsToShow, and hide all views in there
            //hides the toolbar
            Utils.hideViews(hideThisifNoDropsToShow);

            //hide the recycler view
            //here View is the recyclerView
            setVisibility(View.GONE);

            // go through the list showThisifNoDropsToShow, and show all views in there

            Utils.showViews(showThisifNoDropsToShow);

        }
        else{

            //if there are items/drops to show

            // go through the list hideThisifNoDropsToShow, and show all views in there
            Utils.showViews(hideThisifNoDropsToShow);

            //show the recycler view
            //here View is the recyclerView
            setVisibility(View.VISIBLE);


            // go through the list showThisifNoDropsToShow, and hide all views in there
            Utils.hideViews(showThisifNoDropsToShow);

        }

    }



    //initialize recyclerView from code
    public BucketRecyclerView(Context context) {
        super(context);
    }

    //constructor to initialise recyclerView from XML
    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //constructor to initialise recyclerView from XML
    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    //we are overriding the default behavior of setAdapter method
    //we will register the data observer variable we created above, here
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter!=null){

            //register the data observer
            adapter.registerAdapterDataObserver(mDataObserver);
        }

        //everytime setAdapter method is called, our adapter actually changes
        //so we will trigger onChanged method for the data observer
        mDataObserver.onChanged();
    }

    //These are the views we need to hide if there are no Drops to be shown
    //in our case it is only toolbar that we need to hide
    //but we did it this way, incase we want to add more items besides toolbar
    // the ... is array type argument, which can store multiple elements
    //this method is called from MainActivity
    public void hideThisIfNoDropsToShow(View... viewsToHide) {

        //convert viewsToHide to List
         hideThisifNoDropsToShow = Arrays.asList(viewsToHide) ;
    }

    // //These are the views we need to show if there are no Drops to be shown
    //This contains the empty_drops from activity.xml
    //this method is called from MainActivity
    public void showThisIfNoDropsToShow(View... viewsToShow) {

        showThisifNoDropsToShow = Arrays.asList(viewsToShow) ;

    }




}
