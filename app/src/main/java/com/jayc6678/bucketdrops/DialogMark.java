package com.jayc6678.bucketdrops;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jayc6678.bucketdrops.adapters.MarkItemAsCompletedListener;

/**
 * Created by sanjeeth on 8/5/2016.
 */


public class DialogMark extends DialogFragment {

    //define variables that will hold the UI elements - the close button
    private ImageButton mBtnClose;

    //define variables that will hold the UI elements - completed button
    private Button mBtnCompleted;

    public static final String TAG = " log DialogMark";

    private MarkItemAsCompletedListener mMarkItemAsCompleted;

    private int position;

    //create a Click listener
    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id){

                case R.id.btn_completed:

                    //Todo handle the action here to mark the item as complete
                    Toast.makeText(getActivity(), "Dialog clicked ", Toast.LENGTH_LONG);
                    Log.d(TAG, "onClick: Marked as Complete");

                    mMarkItemAsCompleted.onCompleteClicked(position);
                    break;

                case R.id.btn_close:

                    break;
            }

            //dismisses the dialog box
            dismiss();

        }
    };



    //inflate the dialog_mark fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.dialog_mark, container, false);
    }

    //access the UI elements in dialog_mark.xml
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //Access the UI elements - the close button
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);

        //Access the UI elements  - Completed button
        mBtnCompleted = (Button) view.findViewById(R.id.btn_completed);




        //set onClickListener on the close button
        mBtnClose.setOnClickListener(mBtnClickListener);

        //set onClickListener on the completed button
        mBtnCompleted.setOnClickListener(mBtnClickListener);

        //in showDialogMark method, the arguments were passed.
        //we retrieve them here.
        Bundle arguments = getArguments();


        if(arguments != null){

             position = arguments.getInt("POSITION");
            Toast.makeText(getActivity(), "position clicked was " + position , Toast.LENGTH_LONG).show();
        }


    }

    //get the MarkItemAsCompletedListener reference variable from ShowDialogMark in MainActivity
    public void setMarkItemAsCompleted(MarkItemAsCompletedListener markItemAsCompleted) {

        this.mMarkItemAsCompleted = markItemAsCompleted;

    }
}
