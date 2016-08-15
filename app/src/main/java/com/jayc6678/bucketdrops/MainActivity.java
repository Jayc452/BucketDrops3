package com.jayc6678.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jayc6678.bucketdrops.adapters.AdapterDrops;
import com.jayc6678.bucketdrops.adapters.AddListener;
import com.jayc6678.bucketdrops.adapters.Divider;
import com.jayc6678.bucketdrops.adapters.DropClickListener;
import com.jayc6678.bucketdrops.adapters.MarkItemAsCompletedListener;
import com.jayc6678.bucketdrops.adapters.ResetListener;
import com.jayc6678.bucketdrops.adapters.SimpleTouchCallback;
import com.jayc6678.bucketdrops.adapters.Sorter;
import com.jayc6678.bucketdrops.adapters.SwipeListener;
import com.jayc6678.bucketdrops.beans.Drop;
import com.jayc6678.bucketdrops.extras.Utils;
import com.jayc6678.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {


    //testing gitgit status - 2ssdd


    private static String TAG = "Log MainActivity";

    //define mToolbar
    Toolbar mToolbar;

    //holds the results from Realm query
    RealmResults<Drop> mResults;

    Button button;

    //define an varaible of type Realm. This is for our Database
    Realm mRealm;

    //variable of type RecyclerView
    //here we are using BucketRecyclerView which is our custom RecyclerView we created
    BucketRecyclerView mRecycler;

    //define a variable of type View
    //this will hold the UI element empty_drops from activity_main.xml
    View mEmptyView;

    //define a variable of type AdapterDrops
    AdapterDrops mAdapter;

    //create a variable for a clickListener
    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showDialog();
            Toast.makeText(MainActivity.this, "Button was clicked from an anonymous listener", Toast.LENGTH_LONG).show();
        }
    };

    // Create a reference variable that stores an anonymous implmentation of the AddListener interface
    //we could alternatively have implemented AddListener on the ENTIRE MainActivity class, but we preferred this way.
    //we will then pass this reference variable to AdapterDrops via the setAddListener method or by the AdapterDrops constructor
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {

            //show the dialog box to "add a drop"
            showDialog();

        }
    };

    // Create a reference variable that stores an anonymous implmentation of the DropClickListener interface
    //we could alternatively have implemented DropClickListener on the ENTIRE MainActivity class, but we preferred this way.
    //we will then pass this reference variable to AdapterDrops via the setMarkListener method or via the AdapterDrops constructor
    private DropClickListener mDropClickListener = new DropClickListener() {
        @Override

        //from DropHolder class we get the position of the item that was clicked
        public void onDropItemClicked(int position) {

            //show the dialog box to mark item as completed
            //pass the position of the item tht was clicked
            showDialogMark(position);
        }
    };


    //create reference variable that stores an anonymous implementation of the MarkItemAsCompletedListener interface
    private MarkItemAsCompletedListener markItemAsCompletedListener = new MarkItemAsCompletedListener() {
        @Override

        public void onCompleteClicked(int position) {
            //TODO use the position to mark this item as complete in DB via AdapterDrops class file
            Log.d(TAG, "onCompleteClicked: item to be clicked is at position " + position);


            //use the adapter to mark the item as complete in our DB and update view on phone
            mAdapter.markItemAsComplete(position);


        }
    };

    //RealmChangeListener will listen for changes in the Realm DB
    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            Log.d(TAG, "onChange: was called ");

            //mResults automatically contains the latest results, as per Realm documentation
            //call the update method in AdapterDrops with the latest Realm results
            mAdapter.update(mResults);

        }
    };



    private SwipeListener mSwipeListener = new SwipeListener() {
        @Override
        public void onSwipe(int position) {
            mAdapter.deleteItem(position);
        }
    };

    //create an interface reference variable that will hold an implementation of the interface
    private ResetListener mResetListener = new ResetListener() {


        @Override
        public void onReset() {

            //update the sharedPreference to hold sort option as None
            AppBucketDrops.save(MainActivity.this, Sorter.NONE);

            //load results based with sort preference set to None
            //
            loadBasedOnSortPreferences(Sorter.NONE);

        }
    } ;


    //when Add a Drop button is clicked, show the dialog
    private void showDialog() {

        //create an object of DialogAdd which is the java file for the dialog_add.xml fragmnt
        DialogAdd dialogAdd = new DialogAdd();

        //use the show method to show the dialog
        //1st param is fragment manager, 2nd param is tag
        //there is another way to use show method. Where in 1st method is fragment transaction and 2nd param is tag
        dialogAdd.show(getSupportFragmentManager(), "AddADropDialogFragment");

    }


    //when an item is clicked, we show them a dialog to mark the item as complete
    private void showDialogMark(int position){

        //create an object of DialogMark which is the java file for the dialog_mark.xml fragment
        //we cannot pass position in the constructor, cause we need a default constructor for fragment, so we will use bundle
        DialogMark dialogMark = new DialogMark();

        //a  bundle is like a bag into which we put stuff
        Bundle bundle = new Bundle();

        //put our position of the item that was clicked into the bundle
        bundle.putInt("POSITION", position);

        //use setArguments to pass the bundle, which can be retrieved in DialogMark class
        dialogMark.setArguments(bundle);

        //pass the interface refernce variable to DialogMark object
        dialogMark.setMarkItemAsCompleted(markItemAsCompletedListener);

        //use the show method to show the dialog
        //1st param is fragment manager, 2nd param is tag
        //there is another way to use show method. Where in 1st method is fragment transaction and 2nd param is tag
        dialogMark.show(getSupportFragmentManager(), "MarkCompletedDialogFragment");

    }


    //onStart is a Activity lifecycle method.
    // we will add a changeListener to listen for changes in the Realm results
    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }


    //onStop is a Activity lifecycle method.
    // here we will remove the changeListener we added in onStart method
    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access the toolbar from UI
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //access the recyclerView from UI - initialise RecyclerView
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);

        //access the UI element empty_drops from activity_main.xml
        mEmptyView = findViewById(R.id.empty_drops);

        //if there are no Drops to show, pass the toolbar so we can hide it
        mRecycler.hideThisIfNoDropsToShow(mToolbar);

        //if there are no Drops to show, pass the mEmptyView so we can show it
        mRecycler.showThisIfNoDropsToShow(mEmptyView );

        //instantiate a layout manager
        LinearLayoutManager manager = new LinearLayoutManager(this);

        //set the layout manager on the recycler view
        mRecycler.setLayoutManager(manager);

        //create a real instance
        //we using the configuration from AppBucketDrops class file for this
        mRealm = Realm.getDefaultInstance();

        //find all objects of Drop aysnchronously - on the background thread
        //store results in object of type RealmResults
//        mResults = mRealm.where(Drop.class).findAllAsync();

        //load data based on sort preferences of the user
        //we are getting the users preferences from the application class we created calleed AppBucketDrops
        loadBasedOnSortPreferences(AppBucketDrops.getSortPreferences(MainActivity.this));

        //create an object of AdapterDrops
        //mDropClickListener, mRealm, mResults are variables we are passing to the AdapterDrops constructor,
        // cause we need them in AdapterDrop class. They are not required to create a basic adapter
        mAdapter = new AdapterDrops(this, mDropClickListener, mRealm, mResults, mResetListener);

        //specifcy the Addlistner reference you created above.
        //instead of using this method to pass the AddListener, we could have also passed it via the constructor
        //when we created the mAdapter object, by overloading the constructor in the AdapterDrops class.
        //Basically that means, passing mAddListener in addition to 'this' & mResults above
        mAdapter.setAddListener(mAddListener);

        //set hasStableIds as true to enable animation
        mAdapter.setHasStableIds(true);

        //set the adapter
        mRecycler.setAdapter(mAdapter);

        //add the divider using RecyclerView.ItemDecoration class
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));

        //this is our default animator
        //we dont need this line because this is applied by default
        //if we want to change our default animator, then change the parameters in this command.
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        //create an object of SimpleTouchCallback
        //our adapter implements swipe listener interface so we pass it
        SimpleTouchCallback callback = new SimpleTouchCallback(mSwipeListener);

        //create a ItemTouchHelper Object
        ItemTouchHelper helper = new ItemTouchHelper(callback);

        //atach the helper to our RecyclerView
        helper.attachToRecyclerView(mRecycler);

        //set the toolbar
        setSupportActionBar(mToolbar);

        //load the background Image
        initBackgroundImage();

        //access the button from the UI
        button = (Button) findViewById(R.id.button);

        //set a click listener on the button
        button.setOnClickListener(mBtnAddListener);


        //schedule alarm to periodically trigger notificationService to check if notifications need to be sent for soon ending goals.
        Utils.scheduleAlarm(this);

        Log.d(TAG, "onCreate: hello ");



    }

    //method to load the background Image using Glide
    private void initBackgroundImage() {

        //access the background ImageView
        ImageView backgroundImageView = (ImageView) findViewById(R.id.background);

        //use glide to load image
        Glide.with(this)
                //the location of file in the drawable folder
                .load(R.drawable.background)

                .centerCrop()

                //put image into backgroundImageView we got above
                .into(backgroundImageView);

    }

    //Get all the data from Realm DB
    public void getRealmData(View view){

        Log.i( " log ", " inside getRealmData " );


        //get an instance of realm
        Realm realm = Realm.getDefaultInstance();

        //get data for Drop objects
        RealmResults<Drop> allDrops = realm.where(Drop.class).findAll();

        for(Drop drop: allDrops){

            Log.i( " log ", " what is it: "  + drop.getWhat().toString());
            Log.i( " log ", " added : "  + drop.getAdded());
            Log.i( " log ", " Completed : "  + drop.isCompleted());

        }

    }


    //save the sort preferences of the user in shared preferences
    public void  saveSortPreferences(int sortOption){

        AppBucketDrops.save(this, sortOption);

    }



    //load data based on sort preferences of the user
    public void loadBasedOnSortPreferences(int sortPreference){


        switch(sortPreference){

            case Sorter.NONE:

                mResults = mRealm.where(Drop.class).findAllAsync();

                break;

            case Sorter.LEAST_TIME_LEFT:

                //instead of using existing realm results we will get fresh set of realm results asynchronously
                // which are already sorted
                //get all results sorted by the when column

                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");

                break;

            case Sorter.MOST_TIME_LEFT:

                //sort results by descending order
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);

                break;

            case Sorter.COMPLETE:

                //find all realm object where completed is set to true
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();

                break;

            case Sorter.INCOMPLETE:

                //find all realm object where completed is set to false
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();

                break;

        }

        //register change listener for these new set of results
        mResults.addChangeListener(mChangeListener);

    }




    //create the menu in the action bar
    public boolean onCreateOptionsMenu(Menu menu){

        //inflate the menu_main.xml file
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //Handle clicks on the menu in the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //get the id of the menu item that was clicked
        int id = item.getItemId();

        boolean handled = true;

        //switch based on the id
        switch (id){

            case R.id.action_add:
                showDialog();
               break;
            case R.id.action_sort_by_ascending_date:

                //save this preferences in sharedPreferences so it can be used next time
                //Sorter is interfaces in adapters package that has the constants
                saveSortPreferences(Sorter.LEAST_TIME_LEFT);

                //use loadBasedOnSortPreferences method to load results
               loadBasedOnSortPreferences(Sorter.LEAST_TIME_LEFT);

                break;

            case R.id.action_sort_by_descending_date:


                //save this preferences in sharedPreferences so it can be used next time
                //Sorter is interfaces in adapters package that has the constants
                saveSortPreferences(Sorter.MOST_TIME_LEFT);


                //use loadBasedOnSortPreferences method to load results
                loadBasedOnSortPreferences(Sorter.MOST_TIME_LEFT);
                break;

            case R.id.action_show_completed:

                //save this preferences in sharedPreferences so it can be used next time
                //Sorter is interfaces in adapters package that has the constants
                saveSortPreferences(Sorter.COMPLETE);

                //use loadBasedOnSortPreferences method to load results
                loadBasedOnSortPreferences(Sorter.COMPLETE);

                break;

            case R.id.action_show_incomplete:

                //save this preferences in sharedPreferences so it can be used next time
                //Sorter is interfaces in adapters package that has the constants
                saveSortPreferences(Sorter.INCOMPLETE);

                //use loadBasedOnSortPreferences method to load results
                loadBasedOnSortPreferences(Sorter.INCOMPLETE);

                break;

            default:
                handled = false;
                break;

        }

        return handled;

    }
}
