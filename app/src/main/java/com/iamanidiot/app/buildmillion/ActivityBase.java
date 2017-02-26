package com.iamanidiot.app.buildmillion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class ActivityBase extends AppCompatActivity
{

    public ActivityBase()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Context context = getApplicationContext();
        Intent incoming = getIntent();
        Mediator mediator = new Mediator(context);
        Activity act = mediator.getActivityDetails(incoming.getStringExtra("ID"));

        TextView ActivityBase_Actitivy_Notes = (TextView) findViewById(R.id.ActivityBase_Actitivy_Notes);
        TextView ActivityBase_Actitivy_Title = (TextView) findViewById(R.id.ActivityBase_Actitivy_Title);
        TextView ActivityBase_Actitivy_CreatedDateActual = (TextView) findViewById(R.id.ActivityBase_Actitivy_CreatedDateActual);
        ActivityBase_Actitivy_Title.setText(act.activity_name);
        ActivityBase_Actitivy_CreatedDateActual.setText(new Date(Long.valueOf(act.getStamp())).toString());
        ActivityBase_Actitivy_Notes.setText(act.getNotes());
    }
}
