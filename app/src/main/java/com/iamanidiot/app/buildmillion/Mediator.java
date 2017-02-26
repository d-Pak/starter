package com.iamanidiot.app.buildmillion;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.iamanidiot.app.buildmillion.DataBase.COLUMN_ACTIVITY_TABLE_ActivityName;
import static com.iamanidiot.app.buildmillion.DataBase.COLUMN_ACTIVITY_TABLE_Notes;
import static com.iamanidiot.app.buildmillion.DataBase.COLUMN_ACTIVITY_TABLE_Stamp;
import static com.iamanidiot.app.buildmillion.DataBase.COLUMN_ACTIVITY_TABLE_id;
import static com.iamanidiot.app.buildmillion.DataBase.COLUMN_ACTIVITY_TABLE_isActive;
import static com.iamanidiot.app.buildmillion.DataBase.LOG_TAG_Mediator_All_Activities;

/**
 * Created by iamanidiot on 29/1/17.
 */

class Mediator
{
    Context context;
    public Mediator (Context context)
    {
        this.context = context;
    }

    /* This class behaves as a mediator between the main UI Class and DB Class*/
    public boolean addActivity(String activityName, String notes)
    {
        DataBase DB = new DataBase (context);
        return DB.addActivity(activityName, notes, String.valueOf(System.currentTimeMillis()));
    }
    /*
    *
    * Function to return all the activities (ActivitiesActivity)
    *
     */
    public List<Activity> getAllActivities() {
        Log.i(LOG_TAG_Mediator_All_Activities, "Within Mediator GetAllActivities");
        DataBase DB = new DataBase(context);
        Cursor allActivitiesCursor = DB.getAllActivities();
        List<Activity> activities = new ArrayList<>();
        if (allActivitiesCursor.getCount() > 0)
        {
            allActivitiesCursor.moveToFirst();
            do {
                activities.add(new Activity(allActivitiesCursor.getString(allActivitiesCursor.getColumnIndex(COLUMN_ACTIVITY_TABLE_Stamp)),
                        Integer.valueOf(allActivitiesCursor.getString(allActivitiesCursor.getColumnIndex(COLUMN_ACTIVITY_TABLE_id))),
                        allActivitiesCursor.getString(allActivitiesCursor.getColumnIndex(COLUMN_ACTIVITY_TABLE_ActivityName)),
                        allActivitiesCursor.getString(allActivitiesCursor.getColumnIndex(COLUMN_ACTIVITY_TABLE_Notes)),
                        Boolean.getBoolean(allActivitiesCursor.getString(allActivitiesCursor.getColumnIndex(COLUMN_ACTIVITY_TABLE_isActive)))));
            } while (allActivitiesCursor.moveToNext());
            return activities;
        }
        else
            return null;
    }

    public boolean deleteActivity(int ID)
    {
        DataBase db = new DataBase(context);
        return (db.deleteActivity(ID,String.valueOf(System.currentTimeMillis())));
    }

    public Activity getActivityDetails(String ID)
    {
        DataBase db = new DataBase(context);
        return db.getActivityDetails(ID);
    }

    public boolean addOccurence(String ID, String notes, String time)
    {
        DataBase db = new DataBase(context);
        if(db.recordOccurrence(ID,time,notes,String.valueOf(System.currentTimeMillis())))
            return true;
        return false;
    }
}
