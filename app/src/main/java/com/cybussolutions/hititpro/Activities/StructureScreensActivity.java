package com.cybussolutions.hititpro.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cybussolutions.hititpro.Template_Inspection.StructureScreenFragment;
import com.cybussolutions.hititpro.R;



public class StructureScreensActivity extends AppCompatActivity {

    Toolbar toolbar;
   public static String inspectionID;
   public static String inspection_type;
   public static String client_id;
   public static String template_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_structure_screens);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Structure Screens");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();

        inspectionID = intent.getStringExtra("inspectionId");
        inspection_type = intent.getStringExtra("inspection_type");

        if(inspection_type.equals("old"))
        {
            client_id = intent.getStringExtra("client_id");
            template_id = intent.getStringExtra("template_id");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new StructureScreenFragment()).addToBackStack("structure").commit();
    }

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()==1)
        {

            new AlertDialog.Builder(StructureScreensActivity.this)
                    .setTitle("Close Inspection")
                    .setMessage("Are you sure you want to Close this Inspection? This Will remove all un synced data !!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            getSupportFragmentManager().popBackStack();

                            finish();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }
        else {
            finish();

            super.onBackPressed();
        }

    }
}
