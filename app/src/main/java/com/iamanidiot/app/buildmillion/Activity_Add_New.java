package com.iamanidiot.app.buildmillion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Add_New extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_new);
        final Intent result = new Intent();
        final EditText Add_Activity_EnterText_Activity_Name = (EditText) findViewById (R.id.Add_Activity_EnterText_Activity_Name);
        Add_Activity_EnterText_Activity_Name.requestFocus();
        final EditText Add_Activity_EnterText_Activity_Notes = (EditText) findViewById (R.id.Add_Activity_EnterText_Activity_Notes);
        Button activity_add_new_Button_Done = (Button) findViewById (R.id.activity_add_new_Button_Done);
        Button activity_add_new_Button_Back = (Button) findViewById (R.id.activity_add_new_Button_Back);

        activity_add_new_Button_Done.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                String activityName = Add_Activity_EnterText_Activity_Name.getText().toString().trim();
                String notes = Add_Activity_EnterText_Activity_Notes.getText().toString().trim();
                if(activityName.length ()<=0)
                    Snackbar.make (view, "You need to have a name for the name's sake.\nCannot have an activity without ony name.",Snackbar.LENGTH_LONG).show();
                else if(activityName.length ()>28)
                    Snackbar.make (view, "That is a real long name indeed.\nTry giving it a pet name (less than 27 characters).",Snackbar.LENGTH_LONG).show();
                else
                {
                    InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    result.putExtra("ActivityName",activityName);
                    result.putExtra ("Notes",notes);
                    setResult (Activity.RESULT_OK,result);
                    finish();
                }
            }
        });

        activity_add_new_Button_Back.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                result.putExtra ("Cancelled","Cancelled");
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
