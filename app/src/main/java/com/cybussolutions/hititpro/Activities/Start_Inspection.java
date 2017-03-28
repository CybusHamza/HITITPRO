package com.cybussolutions.hititpro.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
    EditText tempName;
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

        client_name = (TextView) findViewById(R.id.client_name_ins);
        tempName = (EditText) findViewById(R.id.tem_name);
        tem_spinner = (Spinner) findViewById(R.id.tem_spinner);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = pref.getString("user_id","");

        client_name.setText(client);

        getTemplates(clientId);


        Start_Ispection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrtmpName = tempName.getText().toString();

                startInspection();

                ringProgressDialog = ProgressDialog.show(Start_Inspection.this, "", "Please wait ...", true);
                ringProgressDialog.setCancelable(false);
                ringProgressDialog.show();

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

                       if (response.equals("Success!%"+temId))
                        {
                            Intent intent= new Intent(Start_Inspection.this,StructureScreensActivity.class);
                            intent.putExtra("inspectionId",response);
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

                        if (!(response.equals("0")))
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
                            tem_spinner.setVisibility(View.GONE);
                            tempName.setVisibility(View.VISIBLE);

                            new SweetAlertDialog(Start_Inspection.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("There Are No Templates Against this Client")
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



}
