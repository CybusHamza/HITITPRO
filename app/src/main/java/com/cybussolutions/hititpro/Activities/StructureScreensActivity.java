package com.cybussolutions.hititpro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cybussolutions.hititpro.Template_Inspection.StructureScreenFragment;
import com.cybussolutions.hititpro.R;



public class StructureScreensActivity extends AppCompatActivity {

    Toolbar toolbar;
   public static String inspectionID;

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
            getSupportFragmentManager().popBackStack();

            finish();

        }
        else {
            finish();

            super.onBackPressed();
        }

    }
}
