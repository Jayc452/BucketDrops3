package com.jayc6678.bucketdrops.beans;


import io.realm.RealmObject;

/**
 * Created by sanjeeth on 7/27/2016.
 */

//this class holds the data for each drop
//to convert the properties of this objects into tables in realm DB, we extend this class with RealObject
public class Drop extends RealmObject {

    //this property holds the goal. What it is.
    private String what;

    //when the drop was added
    private long added;

    //when the goal is due
    private long when;

    //status of the goal or drop
    private boolean completed;

    //constructor with no parameters
    public Drop() {
    }

    //constructor with parameters for creating the object
    public Drop(String what, long added, long when, boolean completed) {
        this.what = what;
        this.added = added;
        this.when = when;
        this.completed = completed;
    }

    //getter and setter methods are below

    public String getWhat() {
        return what;
    }

    public long getAdded() {
        return added;
    }

    public long getWhen() {
        return when;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
