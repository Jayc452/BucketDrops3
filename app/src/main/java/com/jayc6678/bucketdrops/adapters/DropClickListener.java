package com.jayc6678.bucketdrops.adapters;

/**
 * Created by sanjeeth on 8/5/2016.
 */

//interface we created to pass data between MainActivity and AdapterDrops to show the dialog box to show  mark the item as completed
//when the goal/item/drop is clicked

public interface DropClickListener {

    //position is the position where you performed the click.
    //in our recycler view item list, it is the position of the item on that list
     void onDropItemClicked(int position);
}
