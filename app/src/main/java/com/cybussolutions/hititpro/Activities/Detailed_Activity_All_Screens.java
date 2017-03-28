package com.cybussolutions.hititpro.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cybussolutions.hititpro.Adapters.Detailed_Adapter;
import com.cybussolutions.hititpro.Model.Detailed_model;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import java.sql.SQLException;
import java.util.ArrayList;



public class Detailed_Activity_All_Screens extends AppCompatActivity {

    ListView detailedListView;
    String[] items;
    String heading, dbColumn, dbTable, enteredStructure = "", inspectionID, fromDataBase;
    Detailed_Adapter Detailed_Adapter;
    Database database = new Database(this);
    Button addCategory;
    AlertDialog b;
    private ArrayList<Detailed_model> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity_all_screens);

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // receiving values from previous activities

        final Intent intent = getIntent();
        items = intent.getStringArrayExtra("items");
        heading = intent.getStringExtra("heading");
        dbColumn = intent.getStringExtra("column");
        dbTable = intent.getStringExtra("dbTable");
        inspectionID = intent.getStringExtra("inspectionID");

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        detailedListView = (ListView) findViewById(R.id.details_listview);
        addCategory = (Button) findViewById(R.id.add_category);

        Cursor cursor = database.getData(dbColumn, dbTable, inspectionID);
        cursor.moveToFirst();

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDetail();
            }
        });

        if (cursor.moveToFirst()) {
            do {
                fromDataBase = cursor.getString(0);
            } while (cursor.moveToNext());

        }
        if (fromDataBase != null) {

            String splitter = "\\^";
            String[] row = fromDataBase.split(splitter);

            for (String item : row) {
                Detailed_model model = new Detailed_model();
                model.setItemName(item);
                list.add(model);
            }

        }
        else {
            if (items.length != 0) {
                for (String item : items) {
                    Detailed_model model = new Detailed_model();
                    model.setItemName(item);
                    list.add(model);
                }
            }

        }

        Detailed_Adapter = new Detailed_Adapter(list, Detailed_Activity_All_Screens.this);
        detailedListView.setAdapter(Detailed_Adapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String[] insertArray = Detailed_Adapter.getDbInsertArray();

        for (int i = 0; i < insertArray.length - 1; i++) {
            enteredStructure += insertArray[i] + "^";
        }

        enteredStructure = enteredStructure.substring(0, enteredStructure.length() - 1);

        // Insert in local DataBase
        database.insertEntry(dbColumn, enteredStructure, dbTable, inspectionID);

        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        String[] insertArray = Detailed_Adapter.getDbInsertArray();

        for (int i = 0; i < insertArray.length - 1; i++) {
            enteredStructure += insertArray[i] + "^";
        }

        enteredStructure = enteredStructure.substring(0, enteredStructure.length() - 1);

        // Insert in local DataBase
        database.insertEntry(dbColumn, enteredStructure, dbTable, inspectionID);


        super.onBackPressed();
    }


    void addDetail() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dalogbox, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        // intializing variables
        final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
        final Button to = (Button) dialogView.findViewById(R.id.add_BT);


        b = dialogBuilder.create();
        b.show();

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] insertArray = Detailed_Adapter.getDbInsertArray();

                // clearing list view

                list.clear();
                Detailed_Adapter.notifyDataSetChanged();

                for (int item = 0; item < insertArray.length; item++) {
                    Detailed_model model = new Detailed_model();
                    model.setItemName(insertArray[item]);

                    if (item == (insertArray.length - 1)) {
                        if (Add.getText().toString().equals(""))
                        {
                            Toast.makeText(Detailed_Activity_All_Screens.this, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            model.setItemName(Add.getText().toString() + "%0");
                        }
                    }

                    list.add(model);
                }


                Detailed_Adapter.notifyDataSetChanged();

                b.dismiss();

            }
        });


    }
}
