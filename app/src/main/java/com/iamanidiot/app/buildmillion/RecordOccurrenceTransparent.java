package com.iamanidiot.app.buildmillion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class RecordOccurrenceTransparent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_occurrence_transparent);
        Intent intent = new Intent();
        intent = getIntent();
        final String ID = intent.getStringExtra("ID");
        PopupWindow popup = new PopupWindow(this);
        //Alert Dialogue Building
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Record Occurence");
        builder.setCancelable(false);
        LayoutInflater inflator = this.getLayoutInflater();
        final View occurenceDialog = inflator.inflate(R.layout.record_occurrence_dialog,null);
        builder.setView(occurenceDialog);
        //
        //// OK Button Handling
        //
        builder.setPositiveButton("Record this", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Call to Mediator to add the Occurence
                Mediator mediator = new Mediator(getApplicationContext());
                EditText notes = (EditText) occurenceDialog.findViewById(R.id.RecordOccurence_Dialog_EnterNotes);
                if(mediator.addOccurence(ID,notes.getText().toString().trim(),String.valueOf(System.currentTimeMillis())))
                {

                    Toast.makeText(getApplicationContext(), "Recorded Occurence", Toast.LENGTH_SHORT).show();
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(150);
                    //TODO Create the Date/Time picker
                }
                finish();
            }
        });
        //
        //// Cancel Button Handling
        //
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

