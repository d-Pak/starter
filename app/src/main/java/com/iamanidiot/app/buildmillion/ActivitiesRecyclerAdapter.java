package com.iamanidiot.app.buildmillion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by iamanidiot on 30/1/17.
 */

public class ActivitiesRecyclerAdapter extends RecyclerView.Adapter<ActivitiesRecyclerAdapter.ActivityViewHolder>
{
    List<Activity> activities;
    Context context;
    public ActivitiesRecyclerAdapter(Context context, List<Activity> activities)
    {
        this.context = context;
        this.activities = activities;

    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,null);
        ActivityViewHolder activityViewHolder = new ActivityViewHolder(view);
        return activityViewHolder;
    }

    @Override
    public void onBindViewHolder(final ActivityViewHolder customViewHolder, final int i)
    {
        customViewHolder.ActivityName.setText(activities.get(i).activity_name);
//        customViewHolder.CreatedDate.setText(new Date(Long.valueOf(activities.get(i).Stamp)).toString());
        customViewHolder.cardView.setTag(activities.get(i).Activity_id);
        customViewHolder.Notes.setText(activities.get(i).getNotes());


        //
        //// Delete Button Tap
        //
        customViewHolder.delete.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                AlertDialog dialog;
                builder.setTitle("Are you Sure?");
                builder.setMessage("The more Activities you have, the more possible it is to co-relate.\nDo you want to delete the Activity?");
                //
                //// OK Button Handling
                //
                builder.setPositiveButton("Yes get rid of it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Mediator mediator = new Mediator(context);
                        if(mediator.deleteActivity((int) customViewHolder.cardView.getTag()))
                        {
                            Toast.makeText(context, " Activity Deleted!", Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(context,"Something Went wrong :)", Toast.LENGTH_LONG).show();
                    }
                });
                //
                //// Cancel Button Handling
                //
                builder.setNeutralButton("No :)", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        //
        //// Card Element Tap
        //
        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText (context,"Holder Tapped for position ",Toast.LENGTH_LONG).show();
                Log.d("CardView","Clicked on Card Element" + customViewHolder.getAdapterPosition());
                Intent activityIntent = new Intent(context,ActivityBase.class);
                activityIntent.putExtra("ID",String.valueOf(customViewHolder.cardView.getTag()));
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);

            }
        });

        customViewHolder.recordOccurence.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(final View view)
            {

                Intent intent = new Intent(context,RecordOccurrenceTransparent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID",String.valueOf(customViewHolder.cardView.getTag()));
                context.startActivity(intent);

/*                final Mediator mediator = new Mediator(context);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertBuilder.setTitle("Record Occurence");
                View dialog = LayoutInflater.from(context).inflate(R.layout.record_occurrence_dialog, null);
                alertBuilder.setView(dialog);

                final EditText notes = (EditText) dialog.findViewById(R.id.RecordOccurence_Dialog_EnterNotes);
                alertBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(mediator.addOccurence(String.valueOf(customViewHolder.cardView.getTag()),notes.getText().toString(),String.valueOf(System.currentTimeMillis())))
                        {
                            Log.d("ALERT FIALOG ------", notes.getText().toString());
                            Toast.makeText(context, "Recorded Occurence", Toast.LENGTH_LONG).show();
                            Vibrator vibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
                            vibrator.vibrate(pattern,-1);
                        }
                    }
                });
                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                        dialog.dismiss();
                    }
                });

                alertDialog = alertBuilder.create();
                alertDialog.show();
                //mediator.addOccurence()*/
            }
        });


    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount()
    {
        return activities.size();
    }

    @Override
    public long getItemId (int position)
    {
        return super.getItemId(position);
    }


    //Binding Static CardView Template
    public static class ActivityViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView ActivityName;
        TextView Notes;
        TextView CreatedDate;
        ImageButton delete;
        ImageButton recordOccurence;
        ActivityViewHolder(View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById (R.id.CardView_Template);
            ActivityName = (TextView) itemView.findViewById (R.id.CardView_Template_ActivityName);
            CreatedDate = (TextView) itemView.findViewById (R.id.CardView_Template_CreatedDate);
            delete = (ImageButton) itemView.findViewById (R.id.CardView_Template_Delete_Button);
            recordOccurence = (ImageButton) itemView.findViewById(R.id.CardView_Template_Add_Occurence_Button);
            Notes = (TextView) itemView.findViewById(R.id.CardView_Template_ActivityNotes);
        }
    }
}
