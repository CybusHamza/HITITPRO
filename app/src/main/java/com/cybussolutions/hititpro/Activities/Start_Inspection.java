package com.cybussolutions.hititpro.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Spinner;
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
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Start_Inspection extends AppCompatActivity {

    private Toolbar toolbar;
    String client,clientId,StrtmpName,userId;
    TextView client_name;
    Button Start_Ispection;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    AlertDialog b;
    ImageView add_template;
    Spinner tem_spinner;
    private List<String> inspection_list = new ArrayList<>();
    private List<String> inspection_id_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_inspection);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        Start_Ispection = (Button) findViewById(R.id.button);
        toolbar.setTitle("Start inspection");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        Intent intent = getIntent();
        client =intent.getStringExtra("client_name");
        clientId =intent.getStringExtra("client_id");
        //int pos=inspection_list.indexOf()

        client_name = (TextView) findViewById(R.id.client_name_ins);
        tem_spinner = (Spinner) findViewById(R.id.tem_spinner);
        add_template = (ImageView) findViewById(R.id.add_template);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = pref.getString("user_id","");

        client_name.setText(client);

        getTemplates(clientId);


        add_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDetail();
            }
        });


        Start_Ispection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tem_spinner.getSelectedItem().equals("Select a Template"))
                {
                    Toast.makeText(Start_Inspection.this, "Please Select A Template ", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    StrtmpName  = tem_spinner.getSelectedItem().toString();

                    startInspection();

                    ringProgressDialog = ProgressDialog.show(Start_Inspection.this, "", "Please wait ...", true);
                    ringProgressDialog.setCancelable(false);
                    ringProgressDialog.show();

                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();

                break;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startInspection() {

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.START_INSPECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                           prePopulate(response,clientId);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String time = DateFormat.getDateTimeInstance().format(new Date());

                SharedPreferences pref = getApplicationContext().getSharedPreferences("HititPro", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();


                Map<String, String> params = new HashMap<>();
                params.put("name",StrtmpName);
                params.put("client_id",clientId);
                params.put("is_started","1");
                params.put("added_by",userId);
                params.put("modified_by",userId);
                params.put("date",time);
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

    public void prePopulate(final String temId, final String clientId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, End_Points.PRE_POPULATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                       if (!(response.equals("")))
                        {
                            Intent intent= new Intent(Start_Inspection.this,StructureScreensActivity.class);
                            intent.putExtra("inspectionId",response);
                            intent.putExtra("client_id",clientId);
                            intent.putExtra("template_id",temId);
                            intent.putExtra("inspection_type","new");
                            startActivity(intent);
                        }

                        else
                       {
                           new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                                   .setTitleText("Error!")
                                   .setConfirmText("OK").setContentText("There was an Error creating template")
                                   .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sDialog) {
                                           sDialog.dismiss();
                                       }
                                   })
                                   .show();
                       }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();

                params.put("client_id",clientId);
                params.put("template_id",temId);
                params.put("pagecover","");
                params.put("userID",userId);

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

    public void getTemplates(final String clientId)
    {

        ringProgressDialog = ProgressDialog.show(Start_Inspection.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        if (!(response.equals("\"no record found\"")))
                        {
                            try {

                                JSONArray Array = new JSONArray(response);

                                for(int i=0;i<Array.length();i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());


                                    inspection_id_list.add(object.getString("ca_id"));
                                    inspection_list.add(object.getString("name"));


                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (Start_Inspection.this, android.R.layout.simple_spinner_item,inspection_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                tem_spinner.setAdapter(dataAdapter);

                                tem_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener_Product());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            inspection_list.add(0,"Select a Template");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (Start_Inspection.this, android.R.layout.simple_spinner_item,inspection_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            tem_spinner.setAdapter(dataAdapter);

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();

                params.put("client_id",clientId);
                params.put("user_id",userId);

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

    public class CustomOnItemSelectedListener_Product implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {



        }



        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    void addDetail() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dalogbox, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        // intializing variables
        final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
        final Button to = (Button) dialogView.findViewById(R.id.add_BT);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);

        to.setText("Create and Start");


        Add.setHint("Template Name");
        b = dialogBuilder.create();

        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        b.show();

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Add.getText().toString().equals(""))
                {
                    Toast.makeText(Start_Inspection.this, "Enter Template Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StrtmpName  = Add.getText().toString();

                    startInspection();

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



}
