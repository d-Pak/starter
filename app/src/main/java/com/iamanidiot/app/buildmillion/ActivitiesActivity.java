package com.iamanidiot.app.buildmillion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitiesActivity extends AppCompatActivity
{
    public static int Add_Activity_Request_Code = 7;
    RecyclerView recyclerView;
    ActivitiesRecyclerAdapter activitiesRecyclerAdapter;
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_activities);
        //getSupportActionBar().setTitle(R.string.Activities_Title);

        FloatingActionButton FAB = (FloatingActionButton) findViewById (R.id.Activities_FAB);
        TextView Activities_No_Activity_Text = (TextView) findViewById(R.id.Activities_No_Activity_Text);

        /*
         *      FAB Actions
         */
        FAB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent AddActivityIntent = new Intent (ActivitiesActivity.this, Activity_Add_New.class);
                startActivityForResult (AddActivityIntent, Add_Activity_Request_Code);
            }
        });
        /*
         *      Recycler View Setting
         */
 /*       //CardView Activities_Card_View = (CardView) findViewById(R.id.Activities_Card_View); */
        //Recycler View Instanciation
        recyclerView = (RecyclerView) findViewById (R.id.ActivityActivities_RecyclerView);
        recyclerView.hasFixedSize();
        //LayoutManager Instanciation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //Setting Layout Manager to RecyclerView
        recyclerView.setLayoutManager(layoutManager);

        //Fetching Data from Mediatior
        Mediator mediator = new Mediator(getApplicationContext());
        List<com.iamanidiot.app.buildmillion.Activity> activitiesArrayList = mediator.getAllActivities();
        if(activitiesArrayList!=null)
        {
            activitiesRecyclerAdapter = new ActivitiesRecyclerAdapter(this.getApplicationContext(), activitiesArrayList);
            recyclerView.setAdapter(activitiesRecyclerAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this.getApplicationContext(), "No Entries for Activities", Toast.LENGTH_LONG).show();
            Activities_No_Activity_Text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    //
    //// This Function is to recieve Resulting Intents (Mainly from Activity Add)
    //
    protected void onActivityResult (int requestCode,int resultCode, Intent result)
    {
        //Cheking for the REQUESTCODE = 7 (Add Activity)
        if(requestCode == Add_Activity_Request_Code)
        {
            //
            //// If ResultCode is Success
            //
            if ( resultCode == Activity.RESULT_OK && result != null )
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitiesActivity.this);
                AlertDialog dialog;
                Mediator mediator = new Mediator(this.getApplicationContext());
                if(mediator.addActivity(result.getStringExtra("ActivityName"),result.getStringExtra("Notes")))
                {
                    builder.setTitle ("What we received: ");
                    builder.setIcon (R.drawable.ic_menu_send);
                    builder.setMessage ("'" + result.getStringExtra ("ActivityName") + " ("+ result.getStringExtra ("Notes") + ") "+ "' \nWill be created as an activity for you.");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog = builder.create();
                    dialog.show ();
                    UpdateData();
                    return;
                }
                else
                {
                    builder.setTitle("Actung!");
                    builder.setMessage ("Activity was not created sue to some reason. Please try again");
                    dialog = builder.create();
                    dialog.show();
                }
            }
            //
            //// If ResultCode is Cancelled
            //
            if(resultCode == Activity.RESULT_CANCELED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitiesActivity.this);
                AlertDialog dialog;
                builder.setTitle ("You Cancelled?");
                builder.setMessage ("The more activities you create, the more you discover about yourself.\n\nNow go and add some activity");
                builder.setCancelable(true);

                builder.setNeutralButton("Will Try", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void UpdateData()
    {
        Mediator mediator = new Mediator(getApplicationContext());
        List<com.iamanidiot.app.buildmillion.Activity> activitiesList = new ArrayList<>();
        activitiesList = mediator.getAllActivities();
        activitiesRecyclerAdapter.notifyDataSetChanged();
    }
}
