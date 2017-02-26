package com.iamanidiot.app.buildmillion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iamanidiot on 29/1/17.
 */

public class Activity /*implements Parcelable*/
{
    int Activity_id;
    String activity_name;
    String notes;
    boolean isActive;
    String Stamp;

    public Activity (String stamp, int activity_id, String activity_name, String notes, boolean isActive)
    {
        Stamp = stamp;
        Activity_id = activity_id;
        this.activity_name = activity_name;
        this.notes = notes;
        this.isActive = isActive;
    }

    public Activity(Parcel in)
    {
        this.Activity_id = in.readInt();
        activity_name = in.readString();
        notes = in.readString();
        Stamp = in.readString();
    }

    public String getStamp ()
    {
        return Stamp;
    }

    public void setStamp (String stamp)
    {
        Stamp = stamp;
    }

    public boolean isActive ()
    {
        return isActive;
    }

    public void setActive (boolean active)
    {
        isActive = active;
    }

    public String getNotes ()
    {
        return notes;
    }

    public void setNotes (String notes)
    {
        this.notes = notes;
    }

    public String getActivity_name ()
    {
        return activity_name;
    }

    public void setActivity_name (String activity_name)
    {
        this.activity_name = activity_name;
    }

    public int getActivity_id ()
    {
        return Activity_id;
    }

    public void setActivity_id (int activity_id)
    {
        Activity_id = activity_id;
    }

/*    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(Activity_id);
        dest.writeString(activity_name);
        dest.writeString(notes);
        dest.writeString(Stamp);
    }

    public static final Parcelable.Creator<Activity> CREATOR = new Parcelable.Creator<Activity>()
    {
        public Activity createFromParcel(Parcel in)
        {
            return new Activity(in);
        }

        public Activity[] newArray(int size)
        {
            return new Activity[size];
        }
    };*/
}
