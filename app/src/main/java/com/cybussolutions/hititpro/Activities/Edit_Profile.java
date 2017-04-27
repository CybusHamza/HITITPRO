package com.cybussolutions.hititpro.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Edit_Profile extends AppCompatActivity {

    EditText FirstName,LastName,UserEmail,UserContact,UserCity,UserAddress,UserCountry;
    String username,useremail,usercontact,useraddress,userid;
    String[] flname;
    String[] fulladdress;
    Button updateProfile;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userid = pref.getString("user_id", null);

        Intent intent = getIntent();
        username =intent.getStringExtra("user_name");
        flname=username.split(" ");
        useremail =intent.getStringExtra("email");
        usercontact=intent.getStringExtra("phone");
        useraddress=intent.getStringExtra("adress");
        fulladdress=useraddress.split(",");

        FirstName = (EditText) findViewById(R.id.et_first_name);
        LastName = (EditText) findViewById(R.id.et_last_name);
        UserEmail = (EditText) findViewById(R.id.et_user_email);
        UserContact = (EditText) findViewById(R.id.et_user_contact);
        UserCity = (EditText) findViewById(R.id.et_user_city);
        UserCountry = (EditText) findViewById(R.id.et_user_country);
        UserAddress = (EditText) findViewById(R.id.et_user_address);

        updateProfile=(Button)findViewById(R.id.update_profile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateClient();
            }
        });

        FirstName.setText(flname[0]);
        LastName.setText(flname[1]);
        UserEmail.setText(useremail);
        UserContact.setText(usercontact);
        UserCity.setText(fulladdress[1]);
        UserAddress.setText(fulladdress[0]);
        UserCountry.setText(fulladdress[2]);

    }
    public void UpdateClient() {

        ringProgressDialog = ProgressDialog.show(this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        if (response.equals("")) {
                            new SweetAlertDialog(Edit_Profile.this, SweetAlertDialog.ERROR_TYPE)
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
                            new SweetAlertDialog(Edit_Profile.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setConfirmText("OK").setContentText("Profile Updated Successful")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("user_name", FirstName.getText().toString()+" "+LastName.getText().toString());
                                            editor.putString("email", UserEmail.getText().toString());
                                            editor.putString("city",UserCity.getText().toString());
                                            editor.putString("adress", UserAddress.getText().toString()+" ,"+UserCity.getText().toString()+" , "+UserCountry.getText().toString());
                                            editor.commit();
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

                    new SweetAlertDialog(Edit_Profile.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Edit_Profile.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("first_name",FirstName.getText().toString());
                params.put("last_name",LastName.getText().toString());
                params.put("email", UserEmail.getText().toString());
                params.put("phone_number", UserContact.getText().toString());
                params.put("city",UserCity.getText().toString());
                params.put("adress", UserAddress.getText().toString());
                params.put("country",UserCountry.getText().toString());
                params.put("id", userid);

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
