package com.cybussolutions.hititpro.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Add_Client extends AppCompatActivity {

    Boolean firstTime=true;
    EditText Name,City,ContactName,Address,Phone,Fax,Email,State,Zip;
    String strName,strCity,strContactName,strAddress,strPhone,strFax,strEmail,strState,strZip,userid;
    Button Submit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //String numberPattern = "^[+]?[0-9]{10,13}$+";
    String numberPattern = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        Name = (EditText) findViewById(R.id.client_name);
        City = (EditText) findViewById(R.id.client_location);
        ContactName = (EditText) findViewById(R.id.client_contact_name);
        Address = (EditText) findViewById(R.id.client_adress);
        Phone = (EditText) findViewById(R.id.client_phone);
        Fax = (EditText) findViewById(R.id.client_fax);
        Email = (EditText) findViewById(R.id.client_email);
        State = (EditText) findViewById(R.id.client_state);
        Zip = (EditText) findViewById(R.id.client_zip);
        Submit = (Button) findViewById(R.id.register);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userid = pref.getString("user_id", null);
        Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10 && (!Phone.getText().toString().contains("(")||!Phone.getText().toString().contains(")")||!Phone.getText().toString().contains(" ")|| !Phone.getText().toString().contains("-"))){
                    String strPhone=Phone.getText().toString();
                    String subPhone1=strPhone.substring(0,3);
                    String subPhone2=strPhone.substring(3,6);
                    String subPhone3=strPhone.substring(6,10);
                    Phone.setText("("+subPhone1+") "+subPhone2+"-"+subPhone3);
                }

            }
        });
        Fax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10 && (!Fax.getText().toString().contains("(")||!Fax.getText().toString().contains(")")||!Fax.getText().toString().contains(" ")|| !Fax.getText().toString().contains("-"))){
                    String strPhone=Fax.getText().toString();
                    String subPhone1=strPhone.substring(0,3);
                    String subPhone2=strPhone.substring(3,6);
                    String subPhone3=strPhone.substring(6,10);
                    Fax.setText("("+subPhone1+") "+subPhone2+"-"+subPhone3);
                }

            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Add_Client.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(strEmail.matches(emailPattern))
                    {
                        if(strPhone.matches(numberPattern) && strFax.matches(numberPattern))
                        {
                            AddClient();
                        }
                        else {
                            new SweetAlertDialog(Add_Client.this, SweetAlertDialog.ERROR_TYPE)
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
                        new SweetAlertDialog(Add_Client.this, SweetAlertDialog.ERROR_TYPE)
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

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.ADD_CLIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        if (response.equals("")) {
                            new SweetAlertDialog(Add_Client.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Error Adding Client")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        }
                        else {
                            new SweetAlertDialog(Add_Client.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setConfirmText("OK").setContentText("Client Added Successful")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            finish();
                                            Intent intent = new Intent(Add_Client.this, LandingScreen.class);
                                            intent.putExtra("activityName", "addClientClass");
                                            startActivity(intent);

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

                    new SweetAlertDialog(Add_Client.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Add_Client.this, SweetAlertDialog.ERROR_TYPE)
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
