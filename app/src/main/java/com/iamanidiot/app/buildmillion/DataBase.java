package com.iamanidiot.app.buildmillion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by iamanidiot on 29/1/17.
 */

class DataBase extends SQLiteOpenHelper
{
    SQLiteDatabase DB;
    /*
    **** General DB Info
     */
    public static String DATABASE_NAME = "BuildMillion.db";
    public static int DATABASE_VERSION = 4;
    /*
    **** Activity Table Info
    */
    public static String DATABASE_TABLE_Activity = "Activities";
    public static String COLUMN_ACTIVITY_TABLE_id = "Activity_id";
    public static String COLUMN_ACTIVITY_TABLE_ActivityName = "activity_name";
    //Currently not using:
    public static String COLUMN_ACTIVITY_TABLE_PseudoName = "pseudo_name";
    public static String COLUMN_ACTIVITY_TABLE_Notes = "notes";
    public static String COLUMN_ACTIVITY_TABLE_isActive = "isActive";
    public static String COLUMN_ACTIVITY_TABLE_Stamp = "Stamp";
    /*
    **** Occurrence Table Info
    */
    public static String DATABASE_TABLE_Occurence = "Occurences";
    public static String COLUMN_OCCURENCE_TABLE_id = "Occurence_Activity_id";
    public static String COLUMN_OCCURENCE_TABLE_OccuredAt = "Occured_at";
    public static String COLUMN_OCCURENCE_TABLE_isCurrent = "isCurrent";
    public static String COLUMN_OCCURENCE_TABLE_Description = "Description";
    public static String COLUMN_OCCURENCE_TABLE_Stamp = "Stamp";
    /*
    **** LOG Tags Info
    */
    public static String LOG_TAG_DB_OnCreate = "DB_OnCreate";
    public static String LOG_TAG_DB_OCC = "OccurenceDataConnect";
    public static String LOG_TAG_DB_Add_Act = "ActivityDataConnect";
    public static String LOG_TAG_DB_All_Activities = "AllActivities";
    public static String LOG_TAG_DB_HOMESCREEN_RECENTACT = "HOMESCREEN_RECENTACT";
    public static String LOG_TAG_Mediator_All_Activities = "-Mediator-AllActivities";

    public DataBase(Context context)
    {
        this (context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public DataBase (Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super (context, name, null, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db)
    {
        Log.e(LOG_TAG_DB_OnCreate, "Inside OnCreate Method");
        //
        //// Activity Table Create Statement
        //
        String CREATE_ACTIVITY_TABLE = "CREATE TABLE " +
                DATABASE_TABLE_Activity + " ( " +
                COLUMN_ACTIVITY_TABLE_id + " INTEGER PRIMARY KEY ASC AUTOINCREMENT, " +
                COLUMN_ACTIVITY_TABLE_ActivityName + " STRING (27), " +
                COLUMN_ACTIVITY_TABLE_Notes + " STRING(50)," +
                COLUMN_ACTIVITY_TABLE_isActive + " BOOLEAN DEFAULT \"TRUE\", " +
                COLUMN_ACTIVITY_TABLE_Stamp + " STRING (10));";
        //
        //// Occurrence Table Create Statement
        //
        String CREATE_OCCURENCE_TABLE = "CREATE TABLE " +
                DATABASE_TABLE_Occurence + " ( " +
                COLUMN_OCCURENCE_TABLE_id + " INTEGER " +
                " REFERENCES " + DATABASE_TABLE_Activity + "(" + COLUMN_ACTIVITY_TABLE_id +") ON DELETE CASCADE ON UPDATE CASCADE MATCH FULL NOT DEFERRABLE INITIALLY IMMEDIATE, " +
                COLUMN_OCCURENCE_TABLE_OccuredAt + " STRING(10), " +
                COLUMN_OCCURENCE_TABLE_isCurrent + " BOOLEAN DEFAULT TRUE, " +
                COLUMN_OCCURENCE_TABLE_Description + " STRING (50), " +
                COLUMN_OCCURENCE_TABLE_Stamp + " STRING(10));";

        //// Execute the Create Statements
        try
        {
            if (db.isOpen())
            {
                db.execSQL(CREATE_ACTIVITY_TABLE);
                Log.e(LOG_TAG_DB_OnCreate, "Inside OnCreate Method\n-- Created Activity Table");
                db.execSQL(CREATE_OCCURENCE_TABLE);
                Log.e(LOG_TAG_DB_OnCreate, "Inside OnCreate Method\n-- Created Occurence Table");
                //db.execSQL(allActivitiesGroupedView);
            }
        }
        catch (SQLException e)
        {
            Log.d(LOG_TAG_DB_OnCreate,"_*_*_*_*_*_*_*_ DB OnCreate Completed_*_*_*_*_*_*_*_\n" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int i, int i1)
    {
        String delete = "DELETE FROM " + DATABASE_TABLE_Activity + ";";
        if(db.isOpen())
            db.execSQL(delete);
    }


    /*
       *
       *
       * Custom Functions for Application Functionality
       *
       *
     */

    public boolean addActivity(String activity, String notes, String stamp)
    {
        try
        {
            DB = this.getWritableDatabase();
            if (DB.isOpen())
            {
                Log.e(LOG_TAG_DB_Add_Act, "DB Open, gonna insert Activity");
                ContentValues insertActivity = new ContentValues ();
                insertActivity.put(COLUMN_ACTIVITY_TABLE_ActivityName, activity);
                insertActivity.put(COLUMN_ACTIVITY_TABLE_Notes, notes);
                insertActivity.put (COLUMN_ACTIVITY_TABLE_Stamp,stamp);
                long rownum = DB.insert(DATABASE_TABLE_Activity, null, insertActivity);
                if (rownum >= 0)
                {
                    Log.e(LOG_TAG_DB_Add_Act, "Value Inserted -> " + activity + ", " + notes);
                    DB.close();
                    return true;
                }
                else
                {
                    DB.close();
                    return false;
                }
            }
            else
                return false;
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG_DB_Add_Act,e.getMessage());
            DB.close();
            return false;
        }
    }

    public Cursor getAllActivities()
    {
        Log.i(LOG_TAG_DB_All_Activities,"In DB All Activities");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DATABASE_TABLE_Activity + " WHERE " + COLUMN_ACTIVITY_TABLE_isActive + " IS NOT \"FALSE\";";
        Cursor allActivitiesCursor;
        try
        {
            Log.i (LOG_TAG_DB_All_Activities, "In TRY Block for Executing Query");
            allActivitiesCursor = db.rawQuery (query, null);
        }
        catch(Exception e)
        {
            Log.i (LOG_TAG_DB_All_Activities,e.getMessage());
            return null;
        }
        Log.i (LOG_TAG_DB_All_Activities,"Returning Activities List without Exception");
        return allActivitiesCursor;
    }

    public boolean deleteActivity(int ID, String timeStamp)
    {
        ContentValues updateTable = new ContentValues();
        updateTable.put(COLUMN_ACTIVITY_TABLE_isActive,"FALSE");
        updateTable.put(COLUMN_ACTIVITY_TABLE_Stamp,timeStamp);
        DB = this.getWritableDatabase();
        if(DB.isOpen())
        {
            int returnRows = DB.update(DATABASE_TABLE_Activity, updateTable, COLUMN_ACTIVITY_TABLE_id + " = " + ID, null);
            DB.close();
            Log.d(LOG_TAG_DB_Add_Act,"Row updated: " + returnRows);
            if ( returnRows == 1)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public Activity getActivityDetails(String ID)
    {
        DB = this.getWritableDatabase();
        Cursor selection = DB.query(DATABASE_TABLE_Activity,null,COLUMN_ACTIVITY_TABLE_id + " = ?", new String[]{ID},null,null,null);
        if(selection.getCount() > 0 && selection.getCount() == 1)
        {
            selection.moveToFirst();
            Activity returnActivity = new Activity(selection.getString(selection.getColumnIndex(COLUMN_ACTIVITY_TABLE_Stamp)),
                    Integer.valueOf(selection.getString(selection.getColumnIndex(COLUMN_ACTIVITY_TABLE_id))),
                    selection.getString(selection.getColumnIndex(COLUMN_ACTIVITY_TABLE_ActivityName)),
                    selection.getString(selection.getColumnIndex(COLUMN_ACTIVITY_TABLE_Notes)),
                    Boolean.getBoolean(selection.getString(selection.getColumnIndex(COLUMN_ACTIVITY_TABLE_isActive))));
            return returnActivity;
        }
        return null;
    }

    public boolean recordOccurrence(String ID, String time, String notes, String stamp)
    {
        DB = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(COLUMN_OCCURENCE_TABLE_id,ID);
        value.put(COLUMN_OCCURENCE_TABLE_Stamp,stamp);
        value.put(COLUMN_OCCURENCE_TABLE_OccuredAt,time);
        value.put(COLUMN_OCCURENCE_TABLE_Description, notes);
        value.put(COLUMN_OCCURENCE_TABLE_isCurrent,"TRUE");
        if(DB.insert(DATABASE_TABLE_Occurence,null,value) > 0)
            return true;
        else
            return false;
    }
}
