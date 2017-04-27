package com.cybussolutions.hititpro.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditClient extends AppCompatActivity {


    EditText Name,City,ContactName,Address,Phone,Fax,Email,State,Zip;
    String strName,strCity,strContactName,strAddress,strPhone,strFax,strEmail,strState,strZip,userid;
    Button Update;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String numberPattern = "^[+]?[0-9]{10,13}$+";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    ////// Strings to get default values //////////
    String clientId,clientName,clientContact,clientAddress,clientCity,clientPhone,clientFax,clientEmail,clientState,clientZip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        Intent intent = getIntent();
        clientId =intent.getStringExtra("client_id");
        clientName =intent.getStringExtra("client_name");
        clientContact=intent.getStringExtra("client_contact");
        clientAddress =intent.getStringExtra("client_address");
        clientCity =intent.getStringExtra("client_city");
        clientPhone =intent.getStringExtra("client_phone");
        clientFax =intent.getStringExtra("client_fax");
        clientEmail =intent.getStringExtra("client_email");
        clientState =intent.getStringExtra("client_state");
        clientZip =intent.getStringExtra("client_zip");


        Name = (EditText) findViewById(R.id.client_name);
        City = (EditText) findViewById(R.id.client_location);
        ContactName = (EditText) findViewById(R.id.client_contact_name);
        Address = (EditText) findViewById(R.id.client_adress);
        Phone = (EditText) findViewById(R.id.client_phone);
        Fax = (EditText) findViewById(R.id.client_fax);
        Email = (EditText) findViewById(R.id.client_email);
        State = (EditText) findViewById(R.id.client_state);
        Zip = (EditText) findViewById(R.id.client_zip);
        Update = (Button) findViewById(R.id.update);

        Name.setText(clientName);
        ContactName.setText(clientContact);
        Address.setText(clientAddress);
        City.setText(clientCity);
        Phone.setText(clientPhone);
        Fax.setText(clientFax);
        Email.setText(clientEmail);
        State.setText(clientState);
        Zip.setText(clientZip);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userid = pref.getString("user_id", null);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strName = Name.getText().toString();
                strCity = City.getText().toString();
                strContactName = ContactName.getText().toString();
                strAddress = Address.getText().toString();
                strPhone = Phone.getText().toString();
                strFax = Fax.getText().toString();
                strEmail = Email.getText().toString();
                strState = State.getText().toString();
                strZip = Zip.getText().toString();

                if(strName.equals("") || strCity.equals("")|| strContactName.equals("")|| strAddress.equals("")
                        || strPhone.equals("")|| strFax.equals("")|| strEmail.equals(""))
                {
                    Toast.makeText(EditClient.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(strEmail.matches(emailPattern))
                    {
                        if(strPhone.matches(numberPattern))
                        {
                            AddClient();
                        }
                        else {
                            new SweetAlertDialog(EditClient.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Invalid format of Phone number")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        }
                    }
                    else {
                        new SweetAlertDialog(EditClient.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setConfirmText("OK").setContentText("Invalid format of email adress")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();

                                    }
                                })
                                .show();
                    }
                }
            }
        });

    }


    public void AddClient() {

        ringProgressDialog = ProgressDialog.show(this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.UPDATE_CLIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        if (response.equals("")) {
                            new SweetAlertDialog(EditClient.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Error Updating Client")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        }
                        else {
                            new SweetAlertDialog(EditClient.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setConfirmText("OK").setContentText("Client Updated Successful")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                            finish();

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

                    new SweetAlertDialog(EditClient.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(EditClient.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("id",clientId);
                params.put("client_name", strName);
                params.put("city", strCity);
                params.put("contactname", strContactName);
                params.put("address", strAddress);
                params.put("phone", strPhone);
                params.put("fax", strFax);
                params.put("email", strEmail);
                params.put("addedby",userid);
                params.put("state",strState);
                params.put("zip",strZip);

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
