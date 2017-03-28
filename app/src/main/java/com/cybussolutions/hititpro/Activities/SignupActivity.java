package com.cybussolutions.hititpro.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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



public class SignupActivity extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText f_name,l_name,emial,phone,city,adress,country,password;
    String strf_name,strl_name,stremial,strphone,strcity,stradress,strcountry,strpassword;
    Button signup;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        f_name = (EditText) findViewById(R.id.fist_name_et);
        l_name = (EditText) findViewById(R.id.last_name_et);
        emial = (EditText) findViewById(R.id.email_et);
        phone = (EditText) findViewById(R.id.phone_et);
        city = (EditText) findViewById(R.id.city_et);
        adress = (EditText) findViewById(R.id.address_et);
        country = (EditText) findViewById(R.id.country_et);
        password = (EditText) findViewById(R.id.password_et);

        signup = (Button) findViewById(R.id.signUp_button);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strf_name = f_name.getText().toString();
                strl_name = l_name.getText().toString();
                stremial = emial.getText().toString();
                strphone = phone.getText().toString();
                strcity = city.getText().toString();
                stradress = adress.getText().toString();
                strcountry = country.getText().toString();
                strpassword = password.getText().toString();


                // calling for sign up

                if(strf_name.equals("") || strl_name.equals("")|| stremial.equals("")|| strphone.equals("")
                   || strcity.equals("")|| stradress.equals("")|| strcountry.equals("")|| strpassword.equals(""))
                {
                    Toast.makeText(SignupActivity.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(stremial.matches(emailPattern))
                    {
                        Signup();
                    }
                    else {
                        new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
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




    public void Signup() {


        ringProgressDialog = ProgressDialog.show(this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SIGN_UP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ringProgressDialog.dismiss();
                        if (response.equals("User already Registered with an this Email Adress")) {
                            new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText(response)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        }
                        else {

                            new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setConfirmText("OK").setContentText("Sign up Successful")
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

                    new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("email", stremial);
                params.put("password", strpassword);
                params.put("first_name", strf_name);
                params.put("last_name", strl_name);
                params.put("phone_number", strphone);
                params.put("city", strcity);
                params.put("adress", stradress);
                params.put("country", strcountry);

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
