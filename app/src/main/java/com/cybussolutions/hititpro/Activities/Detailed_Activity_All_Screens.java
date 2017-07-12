package com.cybussolutions.hititpro.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.hititpro.Adapters.CustomArrayAdapter;
import com.cybussolutions.hititpro.Model.Checkbox_model;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Detailed_Activity_All_Screens extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    public static boolean isSame;
    ListView detailedListView;
    String[] items;
    String heading, fromadapter, dbColumn, userid, dbTable, enteredStructure = "", inspectionID, fromDataBase;
    CustomArrayAdapter Detailed_Adapter;
    Database database = new Database(this);
  //  Button addCategory;
    ImageView addCategory;
    AlertDialog b;
    ArrayAdapter<Checkbox_model> adapter;
    ArrayList<String> checkedValue;
    String toPass[];
    static  boolean isAnyChecked = false;
    String dbEnterArray[],tag;
    ProgressDialog ringProgressDialog;
    private ArrayList<Checkbox_model> list = new ArrayList<>();
    private ArrayList<Checkbox_model> list_temp;
    private static final Map<String, String> myMap;
    static
    {
        myMap = new HashMap<String, String>();
        myMap.put("portfolio", "16");
        myMap.put("roofing", "12");
    }

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
        fromadapter = intent.getStringExtra("fromAddapter");
        dbColumn = intent.getStringExtra("column");
        dbTable = intent.getStringExtra("dbTable");
        inspectionID = intent.getStringExtra("inspectionID");
        tag = intent.getStringExtra("tag");

        toPass = new String[]{heading, dbColumn, dbTable,tag};

        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userid = pref.getString("user_id", "");

        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("heading", heading);
        editor.commit();

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);

        detailedListView = (ListView) findViewById(R.id.details_listview);
        addCategory = (ImageView) findViewById(R.id.add_category);

        if (fromadapter.equals("false")) {
            Cursor cursor = database.getData(dbColumn, dbTable, inspectionID);
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    fromDataBase = cursor.getString(0);
                } while (cursor.moveToNext());

            }


        }
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDetail();
            }
        });


        if ((fromadapter.equals("false"))) {

            String splitter = "\\^";
            String[] row = fromDataBase.split(splitter);


            int position = 0;
            for (String item : row) {
                Checkbox_model model = new Checkbox_model();
                model.setTitle(item);

                String splitter1 = "%";
                String rows[] = item.split(splitter1);

                list.add(model);

                if (rows[1] != null && rows[1].equals("1")) {
                    list.get(position).setChecked(true);
                } else {
                    list.get(position).setChecked(false);
                }


                position++;
            }

        }
        else {
            int position = 0;

            if (items.length != 0) {

                int length = 0;
                if (fromadapter.equals("edit")) {
                    length = items.length;
                } else if (fromadapter.equals("true")) {
                    length = items.length - 1;
                }

                for (int i = 0; i < length; i++) {
                    Checkbox_model model = new Checkbox_model();
                    model.setTitle(items[i]);
                    String splitter1 = "%";
                    String rows[] = items[i].split(splitter1);

                    list.add(model);

                    if (rows[1] != null && rows[1].equals("1")) {
                        list.get(position).setChecked(true);
                    } else {
                        list.get(position).setChecked(false);
                    }
                    position++;
                }
            }

        }

        Detailed_Adapter = new CustomArrayAdapter(Detailed_Activity_All_Screens.this, list, toPass);
        detailedListView.setAdapter(Detailed_Adapter);

        detailedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSweet = detailedListView.getItemAtPosition(position).toString();


                TextView textView = (TextView) view.findViewById(R.id.textViewTitle);
                String text = textView.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(Detailed_Activity_All_Screens.this);
                builder.setTitle("").setMessage(text);


                builder.show();
            }
        });

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
        update();

        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        String[] insertArray = Detailed_Adapter.getDbInsertArray();


        for (int i = 0; i < insertArray.length - 1; i++) {

            enteredStructure += insertArray[i] + "^";
        }

        update();

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
        dialogBuilder.setCancelable(false);

        // initializing variables
        final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
        final Button to = (Button) dialogView.findViewById(R.id.add_BT);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);


        b = dialogBuilder.create();


        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        b.show();

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Add.getText().toString().equals("")) {

                    Toast.makeText(Detailed_Activity_All_Screens.this, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
                } else {


                    String[] insertArray = Detailed_Adapter.getDbInsertArray();

                    list_temp = new ArrayList<>(list);
                    // clearing list view
                    list.clear();


                    for (int item = 0; item <= insertArray.length - 1; item++) {

                        if (item != 0) {
                            if (insertArray[item - 1].equals(Add.getText().toString() + "%0") || insertArray[item - 1].equals(Add.getText().toString() + "%1")) {
                                Toast.makeText(Detailed_Activity_All_Screens.this, "Item Already Exists", Toast.LENGTH_SHORT).show();
                                //list = new ArrayList<>(list_temp);
                                break;
                            } else {

                                Checkbox_model model = new Checkbox_model();
                                model.setTitle(insertArray[item]);

                                if (item == insertArray.length - 1) {


                                    model.setTitle(Add.getText().toString() + "%0");

                                }

                                list.add(model);
                                if (item > list.size()) {
                                    list_temp.add(model);
                                }
                            }
                        } else {

                            Checkbox_model model = new Checkbox_model();
                            model.setTitle(insertArray[item]);

                            if (item == insertArray.length - 1) {

                                model.setTitle(Add.getText().toString() + "%0");

                            }

                            list.add(model);
                        }


                    }

                    dbEnterArray = new String[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        dbEnterArray[i] = list.get(i).getTitle();
                    }


                    Intent intent = new Intent(Detailed_Activity_All_Screens.this, Detailed_Activity_All_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("items", dbEnterArray);
                    intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                    intent.putExtra("heading", toPass[0]);
                    intent.putExtra("fromAddapter", "edit");
                    intent.putExtra("column", toPass[1]);
                    intent.putExtra("dbTable", toPass[2]);
                    intent.putExtra("tag", toPass[3]);
                    finish();
                    startActivity(intent);


                    b.dismiss();
                }

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }

    public void update() {



        StringRequest request = new StringRequest(Request.Method.POST, End_Points.UPDATELIVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                     //   Toast.makeText(Detailed_Activity_All_Screens.this, response, Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {

                    Toast.makeText(Detailed_Activity_All_Screens.this, " Data not Synced due to no internet connection", Toast.LENGTH_SHORT).show();


                } else if (error instanceof TimeoutError) {


                    Toast.makeText(Detailed_Activity_All_Screens.this, " Data not Synced due to Connection time out please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("dbTable", dbTable);
                params.put("dbColumn", dbColumn);
                params.put("enteredStructure", enteredStructure);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("added_by", userid);
              /*  params.put("is_checked", isAnyChecked+"");
                params.put("total_fields", myMap.get(dbTable));*/
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }
}
