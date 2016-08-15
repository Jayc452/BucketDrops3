package com.jayc6678.bucketdrops.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jayc6678.bucketdrops.R;

/**
 * Created by sanjeeth on 7/30/2016.
 */

//This class draws the dividers between the items in the list
public class Divider extends RecyclerView.ItemDecoration {

    //define divider as a variable of type drawable
    private Drawable mDivider;

    //variable that will hold the orientation
    private int mOrientation;



    //constructor for this class
    //2nd param is orientation of the recycler view, cause w need to ensure that we draw horizontal divider for a vertical layout
    public  Divider(Context context, int orientation){

        //access the divider.xml we created in drawable folder
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);

        //check if the orientation is vertical or not
        //our divider is horizontal, so it only makes sense to use it on vertical orientation
        if(orientation != LinearLayoutManager.VERTICAL){

            throw new IllegalArgumentException("This Item Decoration can only be used with a RecyclerView that uses a Linear Layout with a vertical orientation");

        }

        //set the orientation
        mOrientation = orientation;

    }



    @Override
    //method to draw the divider
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        //check if the orientation of the layout is vertical
        if(mOrientation == LinearLayoutManager.VERTICAL){


            drawHorizontalDivider(c,parent, state);

        }

    }


    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {


        //there are going to be 4 values to draw our divider
        int left, top, right, bottom;

        //get the padding for the recyclerView
        //you will find its value in the activity_main.xml > RecyclerView widget, if it exists
        left = parent.getPaddingLeft();

        //get the amount of space to be left on the right hand side
        //this will give the length of the line to draw starting from the left to right
        right = parent.getWidth() - parent.getPaddingRight();

        //no. of children the recycle view has
        int count = parent.getChildCount();

        //loop through each child by using the for loop
        for (int i = 0; i < count; i++) {


            //check if the current view's type is footer.
            //draw the divider only if it isnt the footer
            if(AdapterDrops.FOOTER != parent.getAdapter().getItemViewType(1)){

                //get the current child
                //this is the individual row  that contain the Drop/goal, from the list/RecyclerView
                View current = parent.getChildAt(i);

                //get layout margin applied to each child at the given position
                //layout margin is defined in row_drop.xml
                //margin value of child is stored in layoutParams. Each view or each child has its own LayoutParams object
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) current.getLayoutParams();


           /*################################
           we used this to draw margin at the top. But commenting it out now, because we are only going to draw dividers at

            //the bottom of the rows.

            //gurrent.getTop will give us the value of top of the view
            //imagine it as the distance on y axis from the very edge at top starting at 0 to the point where row starts on the Y axis
            //params.topMargin will give us the topMargin value assigned in row_drop.xml
            top = current.getTop() - params.topMargin;


            */

                //gurrent.getBottom will give us the value of bottom of the view
                //imagine it as distance on y axis from the very edge at top starting at 0 to the point where row ends on the Y axis
                //params.bottomMargin will give us the bottomMargin value assigned in row_drop.xml
                top = current.getBottom() + params.bottomMargin;

                //bottom of the divider is the sum of top of the divider and height of the divider
                bottom = top + mDivider.getIntrinsicHeight();

                //set the bounds of the divider
                mDivider.setBounds(left, top, right, bottom);

                //draw the divider
                mDivider.draw(c);


            }


        }

    }


    @Override
    //give offset space or not around the item
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);


        if(mOrientation == LinearLayoutManager.VERTICAL){

            //this will draw the divider
            outRect.set(0,0,0, mDivider.getIntrinsicHeight());

        }
    }
}
