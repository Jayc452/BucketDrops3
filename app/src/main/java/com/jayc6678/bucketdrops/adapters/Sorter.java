package com.jayc6678.bucketdrops.adapters;

/**
 * Created by sanjeeth on 8/6/2016.
 */


//interface that holds the values based on the sorting preferences of the user
public interface Sorter {

    //sort list based on when the item was added into our db, irrespective of the date
    int NONE = 0;

    //Deadline is the farthest
    int MOST_TIME_LEFT = 1;

    int LEAST_TIME_LEFT = 2;

    //item is marked as complete
    int COMPLETE = 3;

    int INCOMPLETE = 4;
}
