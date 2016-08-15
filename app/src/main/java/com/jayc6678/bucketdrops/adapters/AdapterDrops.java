package com.jayc6678.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jayc6678.bucketdrops.AppBucketDrops;
import com.jayc6678.bucketdrops.R;
import com.jayc6678.bucketdrops.beans.Drop;
import com.jayc6678.bucketdrops.extras.Utils;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sanjeeth on 7/28/2016.
 */

//this adapter extends RecyclerView.Adapter which expects an argument of type RecyclerView.ViewHolder
// So we will create an inner class that will return object of type RecyclerView.Viewholder
// That inner class is DropHolder which extends RecyclerView.ViewHolder
 //we then pass AdapterDrops.DropHolder to the RecyclerView.Adapter as an argument
 //next implement the methods for the RecyclerView.Adapter class

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final String TAG = "AdapterDrops";

    //declare a constant that will represent if the item is a footer or not
    //this will be used to show ou sectionalized recycler view
    //the top part will contain the drops/item and lower part will be the footer
    public static final int ITEM = 0;

    //declare a constant that will represent if the item is a footer or not
    //this will be used to show ou sectionalized recycler view
    //the top part will contain the drops/item and lower part will be the footer
    //NO_ITEM means that while there are items to be shown, there no items to be shown as a result of complete or incomplete sort
    public static final int NO_ITEMS_FOR_THIS_SORT_OPTION = 1;

    //declare a constant that will represent if the item is a footer or not
    //this will be used to show ou sectionalized recycler view
    //the top part will contain the drops/item and lower part will be the footer
    public static final int FOOTER = 2;

    //defining mInflater as global variable
    private LayoutInflater mInflater;

    //data source
    RealmResults<Drop> mResults;

    //Context
     Context mContext;


    // Add it Button that is in the footer.xml
    Button addItButton;

    //instance variable of type AddListener
    private AddListener mAddListener;

    //Realm object
    private Realm mRealm;

    //instance variable for type DropClickListener
    private DropClickListener mDropClickListener;

    //the sort option stored for the user in the sharedpreferences
    int mSortOption;

    //this is the count for the footer that we have to show
    public static final int COUNT_FOOTER = 1;

    //this is count for the 1 item row we have to show saying "No Items to Show" when there are no items to show in a particular sort preference
    public static final int COUNT_NO_ITEMS = 1;

    ResetListener mResetListener;

    //constructor for this class
    //it accepts the context
    //2nd param is the data source for this list
    public AdapterDrops(Context context, DropClickListener dropClickListener, Realm realm, RealmResults<Drop> results, ResetListener resetListener){

        //create an object of LayoutInflater which we can use to inflate objects later
        //this object is later used in the ViewHolder method to inflate the viewholder
        mInflater = LayoutInflater.from(context);

        //accept the realm object from MainActivity when this adapter object is created
        mRealm = realm;

        //accept dropClickListener interface reference variable from MainActivity which handles opening of dialog to mark as complete
        mDropClickListener = dropClickListener;

        mContext = context;

         mResetListener = resetListener;

        update(results);
    }


    //update the adapter
    public void update(RealmResults<Drop> results){

        //store the results in mResults
        mResults = results;

        //get the sort option that is stored for the user in user preferences
        mSortOption = AppBucketDrops.getSortPreferences(mContext);

        //tell your adapter that data has changed and that it needs to update itself.
        notifyDataSetChanged();

    }


    //method that will attach AddListener interface we created to this object
    public void setAddListener(AddListener listener){

        mAddListener = listener;

    }





    //notice that the return type of this method is DropHolder/FootHolder etc. Which is the inner class we created below to the Views.
    //after the constructor, this method is automatically executed
    //value for viewType parameter is got from getItemView method above, which tells this method what view to inflate
    //The input to this is from getItemViewType method below
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        //inflate the appropriate view based on viewType


        //if view is an Drop/Goal then inflate the row_drop.xml
        //else inflate the footer which contains the ‘Add a Drop’ Button
        if(viewType == ITEM){


            //inflate the layout for the row
            View view = mInflater.inflate(R.layout.row_drop, parent, false);

            //create an object of DropHolder
            //this object will contain access to the all the individual UI elements in the view we created above
            DropHolder holder = new DropHolder(view, mDropClickListener);

            //return the DropHolder object
            //after this control is passed to onBindViewHolder method, where the individual UI elements from dropholder object will be upated with data
            return holder;

        }
        //if there are no items to be shown in "complete' Or 'inComplete' sort then use this
        else if(viewType == NO_ITEMS_FOR_THIS_SORT_OPTION){

            //inflate the layout for "No Items"
            View view = mInflater.inflate(R.layout.no_item_for_this_sort_option, parent, false);

            //create an object of NoItemsViewHolder with the layout
            NoItemsViewHolder holder = new NoItemsViewHolder(view);

            //pass the object
            return holder;

        }
        else {

            //inflate the layout for the footer
            View view = mInflater.inflate(R.layout.footer, parent, false);

            //create an object of FooterHolder
            //this object will contain access to the all the individual UI elements in the view we created above
            FooterHolder holder = new FooterHolder(view);

            //return the FooterHolder object
            //after this control is passed to onBindViewHolder method, where the individual UI elements from FooterHolder object will be updated with data
            return holder;

        }


//        Log.d(TAG, "onCreateViewHolder: ");

    }

    //after the onCreateViewHolder method executes, the holder object is passed to this onBindViewHolder method
    //here we update the UI fields of the holder object with the data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.d("log ", "onBindViewHolder: " + position);

        //the holder that is passed to this could be either of DropHolder type or FooterHolder
        //if holder is of type DropHolder we do the following to update the data in the DropHolder, else we do nothing
        if(holder instanceof DropHolder){


            //get the Drop object from the array at the particular position
            Drop drop = mResults.get(position);

            //get the 'what is it' property of the particular Drop object
            String whatIsIt = drop.getWhat();

//            boolean isCompleted = drop.isCompleted();

            //get the value of 'when' from our DB
            Long when = drop.getWhen();

            //create a new DropHolder object by type casting holder and use that to extract the UI elements in the next line
            DropHolder dropHolder = (DropHolder) holder;

            //set the value for the TextView UI element
            dropHolder.setWhat(whatIsIt);

//            if(isCompleted){
//
//                dropHolder.mTextWhen.setText("Completed");
//            }


            //set the background color of the item depending on its status
            dropHolder.setBackground(drop.isCompleted());
//            dropHolder.mTextWhen.setText(when);

            //set the value of when in the UI element
            dropHolder.setWhen(when);

        }
    }


    //get the count for the total number of items to be shown
    @Override
    public int getItemCount() {

        //if there are results to be shown then we show it as we normally would
        // if there are no results to be shown, then we should check if sort option (Complete or Incomplete) is selected
        //which would mean that, while there are results, there are no completed tasks, hence we need to show the user
        //NO_ITEM_FOR_THIS_SORT_OPTION.XML view and not EMPTY_DROPS.xml

        if(!mResults.isEmpty()){

            //return the size of the mResults array, which contains the data
            //add 1 to it, to accomodate the footer which we are using or our sectionalized recyclerView
            return (mResults.size() + COUNT_FOOTER) ;

        }
        else{

            //if the sort option selected is Complete or Incomplete, and the results is empty, it could be because
            // we wont have completed items.
            if (mSortOption == Sorter.COMPLETE
                    || mSortOption == Sorter.INCOMPLETE){

                //we have to return 2 rows as the size of mResults,
                //one for showing "No Items to show"
                //1 for showing the footer
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
            else{
                //if mSortOption is ascending, descending, none etc.
                //this value is used in BucketRecyclerView.handleViews method
                return 0;
            }
        }


    }




    //determine if the view at the current position is an drop/goal item or a footer (Add a Button)
    //the output of this method is sent to onCreateViewHolder method, which will inflate appropriate view (drop/goal or footer)
    // the input to this is from getItemCount method above
    public int getItemViewType(int position){


        //if there are results to be shown
        if(!mResults.isEmpty()){

            //if the position is lesser than the size of the results, that means this is an item/goal/drop
            //if position is larger than results, that means we have shown all items/goals/drops so this must be the footer

            if(position < mResults.size()){

                return ITEM;
            }
            else {
                return FOOTER;
            }

        }
        else{ //if there are no results from DB for sorting option complete or incomplete

            //if sort option selected is "Complete' or 'Incomplete'
            //we know it will return 2 as result from itemCount method
            //position 0 and position 1, will the 2 rows.
            //position 0 is our item where we display NO
            if(mSortOption == Sorter.COMPLETE || mSortOption == Sorter.INCOMPLETE){


                if(position == 0){
                    return  NO_ITEMS_FOR_THIS_SORT_OPTION;
                }
                else{

                    return FOOTER;
                }

            }
            else{

                return ITEM;
            }


        }

    }



    //identify a unique id for each item
    //we will use this for animation for our recycler view list

    @Override
    public long getItemId(int position) {

        //make sure that the item is a drop/goal and not the footer.
        //mResults contains all the drops (results)

        if(position < mResults.size()){

            //return a unique value which is the time the item/goal/drop was added to our DB
          return mResults.get(position).getAdded();

        }

        //if you ctrl+click on this, you will see it takes us to RecyclerView's getItemId method which returns constant No_ID
        return super.getItemId(position);
    }



    //this method is implemented when we implemented our custom SwipeListener inerface on AdapterDrops
    //we will delete our item from DB when swipe occurs
    @Override
    public void onSwipe(int position) {

        //Since we also have the footer in our recycler view, we need to make sure that only items from results array can be removed
        if(position < mResults.size()){

            //start the realm DB transaction
            mRealm.beginTransaction();

            //Remove the item at the particular position from our realm DB
            mResults.get(position).deleteFromRealm();

            //commit the transaction
            mRealm.commitTransaction();


            //update the adapter, notify of the item removed
            notifyItemRemoved(position);

        }


        //call this method if there are no items to show
        resetIfEmpty();

    }

    //reset view to empty_drops.xml if there are no more drops to show
    private void resetIfEmpty() {


        if(mResults.isEmpty() &&  (mSortOption == Sorter.COMPLETE || mSortOption == Sorter.INCOMPLETE)){

            //call the onReset method
            mResetListener.onReset();

        }

    }

    //i created this method as an alternative to using Listeners and interface and was calling it directly from SimpleTouchCallBack class
    //this is not being used right now. I only did it for learning and exploration
    //this method works, but using interfaces is a better option cause of loose couplig concept.
    public void deleteItem(int position) {

        //Since we also have the footer in our recycler view, we need to make sure that only items from results array can be removed
        if(position < mResults.size()){

            //start the realm DB transaction
            mRealm.beginTransaction();

            //Remove the item at the particular position from our realm DB
            mResults.get(position).deleteFromRealm();

            //commit the transaction
            mRealm.commitTransaction();

            //update the adapter, notify of the item removed
            notifyItemRemoved(position);

        }


    }

    //method to mark an item at the given position as complete in our DB
    public void markItemAsComplete(int position) {

        //Since we also have the footer in our recycler view, we need to make sure that only items from results array can be removed
        if(position < mResults.size()){

            //begin the transaction
            mRealm.beginTransaction();

            //get the item at the position and set it as true in our realm db
            mResults.get(position).setCompleted(true);

            //commit the transaction
            mRealm.commitTransaction();

            //will update the adapter and the view in our app
            notifyItemChanged(position);
        }

    }

    //inner class we created for ViewHolder
    //ViewHolder holds the View.
    //we use this class to access the UI elements in the View or the row
    //we pass this view to onBindViewHolder method, so the UI elements can be reused to populate with different data
    //we implement onclickListener in this class to handle clicks on the items

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //define UI variables
        TextView mTextWhat;

        TextView mTextWhen;

        TextView mCompleted;

        // we need to declare this in inner class DropHolder. Although we are getting its value from outclass AdapterDrops
        Context mContext;

        //View
        View mItemView;

        DropClickListener mDropClickListener;

        //this constructor is called from the onCreateViewHolder method, when the DropHolder object is being created
        //we get dropClickListener from the outer class AdapterDrops which gets it from MainActivity
        public DropHolder(View itemView, DropClickListener dropClickListener) {
            super(itemView);

            mItemView = itemView;

            //set an onclickListener on the entire item/row.
            //we use this to show user a dialog to mark item as complete
            itemView.setOnClickListener(this);

            //reference variable for DropClickListener from MainActivity
            mDropClickListener = dropClickListener;

            //get the context
            mContext= itemView.getContext();

            //access the UI element for What is it from the row/view
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_row_drop_what_is_it);

            //access the UI element for When from the row/view
            mTextWhen = (TextView) itemView.findViewById(R.id.tv_row_drop_when_is_it_due);

            //apply the custom font to "what" TextViews
            AppBucketDrops.setRalewayRegular(mContext, mTextWhat);

            //apply the custom font to "when" TextViews
            AppBucketDrops.setRalewayRegular(mContext, mTextWhen);

        }

        //the onClick method for the onClick listener
        @Override
        public void onClick(View view) {

            //if this was an activity, we could have just shown the dialog from here itself.
            //but since this isnt, we will use interface reference variable, mDropClickListener, we created
            // in MainActivity and pass it to here
            //this will show a dialog
            mDropClickListener.onDropItemClicked(getAdapterPosition());

        }

        //set the value of the UI element "What is it"
        public void setWhat(String what) {

            mTextWhat.setText(what);


        }

        //set the background color depending on the status
        public void setBackground(boolean completed) {

            //create an variable of type Drawable
            Drawable drawable;



            //if item is marked as completed
            if(completed){

                //set the drawable to be..
                //here note that we are getting the color from values > colors.xml file
                drawable = ContextCompat.getDrawable(mContext, R.color.bg_drop_complete);
            }
            else{
                //set the drawable to be...
                //here note that we are not using color but drawable folder where we have defined colors based on the selectors
                drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
            }

            //Utils is class we have in extras package
            //here we can set the background
            Utils.setBackground(mItemView, drawable);

        }

        public void setWhen(Long when) {

            //DateUtils class has a set of static methods like getRelativeTimeSpanString we can use to get approximate dates
            //1st param is when (future goal date),
            //2nd param is current time
            //3rd param is the resolution
            //4th is flag for abbreviation
            String dateApprox = (String) DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);

            mTextWhen.setText(dateApprox);
        }
    }


    //this is a ViewHolder to be used when there are no items to be shown in "Complete" or "Incomplete" sort.
    public class NoItemsViewHolder extends RecyclerView.ViewHolder{


        public NoItemsViewHolder(View itemView) {
            super(itemView);
        }
    }


    //inner class we created for ViewHolder for footer
    //ViewHolder holds the View.
    //we use this class to access the UI elements in the View or the row
    //we pass this view to onBindViewHolder method, so the UI elements can be reused to populate with different data
    public  class FooterHolder extends RecyclerView.ViewHolder{


        //this constructor is called from the onCreateViewHolder method, when the FooterHolder object is being created
        public FooterHolder(View itemView) {
            super(itemView);

            //access the UI element for What is it from the row/view
            Button addItButton = (Button) itemView.findViewById(R.id.btn_add_it);

            //set a click listener on the button to listen for when the button gets clicked
            addItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //we are creating this object in MainActivity and passing it to this Adapter via setAddListener method
                    //alternatively we could also pass it to this adapter directly via the constructor itself.
                    mAddListener.add();

                }
            });


        }
    }

}
