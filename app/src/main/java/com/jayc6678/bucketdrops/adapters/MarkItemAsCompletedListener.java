package com.jayc6678.bucketdrops.adapters;

/**
 * Created by sanjeeth on 8/6/2016.
 */

//this interface is used to pass data between MainActivity and DialogMark class
//    we create a reference variable of this interface in MainActivity and pass it to DialogMark class

public interface MarkItemAsCompletedListener {

    //    When the user clicks on "Mark as Complete" button, this method is executed via its reference variable is MainActivity
    public void onCompleteClicked(int position);
}
