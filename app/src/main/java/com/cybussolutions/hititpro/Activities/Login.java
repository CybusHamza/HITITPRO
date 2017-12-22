package com.cybussolutions.hititpro.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cybussolutions.hititpro.R.id.login_button;

public class Login extends AppCompatActivity {

    Button login,signup;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    TextView username, password;
    String strUser,strPass;
    TextView forgotPassword;
    AlertDialog b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SharedPreferences preferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
        String islogged = preferences.getString("isloggedin","");

        if(islogged.equals("true"))
        {
            Intent intent = new Intent(Login.this,LandingScreen.class);
            intent.putExtra("activityName","addClientClass");
            finish();
            startActivity(intent);

        }
        login = (Button) findViewById(login_button);
        signup = (Button) findViewById(R.id.signUp_button);
        username = (TextView) findViewById(R.id.username_login);
        password = (TextView) findViewById(R.id.password_login);
        forgotPassword= (TextView) findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strUser = username.getText().toString();
                strPass = password.getText().toString();



                if(strUser.equals("") || strPass.equals(""))
                {
                    Toast.makeText(Login.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(strPass.length()<8){
                        Toast.makeText(Login.this, "enter the password  minimum 8 characters", Toast.LENGTH_SHORT).show();
                    }else {
                        login();
                    }
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent= new Intent(Login.this,SignupActivity.class);
               // startActivity(intent);
                Toast.makeText(Login.this, "To signup to HITIT Pro, please visit our website and complete your billing process!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login() {

        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Checking Credentials ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        if (response.equals("false")) {
                            Toast.makeText(Login.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                           /* new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Incorrect Username or Password")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();*/
                        }
                        else {

                            String userid;

                            try {

                                String name,email,adress,phone,img,company_info,website,fax,city,state,zip;

                                JSONObject object = new JSONObject(response);

                                userid = object.getString("id");
                                name = object.getString("first_name");
                                name += " " + object.getString("last_name");
                                email=object.getString("email");;
                                adress=object.getString("adress");
                               // adress += " ," +object.getString("city");
                                //adress += " , "+object.getString("country");
                                phone =object.getString("phone_number");
                                img =object.getString("profile_image");
                                company_info =object.getString("company_info");
                                website=object.getString("website");
                                fax=object.getString("fax");
                                city=object.getString("city");
                                state=object.getString("state");
                                zip=object.getString("zip");

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("user_id", userid);// Saving string
                                editor.putString("user_name", name);
                                editor.putString("email", email);
                                editor.putString("adress", adress);
                                editor.putString("phone", phone);
                                editor.putString("img", img);
                                editor.putString("isloggedin","true");
                                editor.putString("logo","true");
                                editor.putString("company_info",company_info);
                                editor.putString("website",website);
                                editor.putString("fax",fax);
                                editor.putString("city",city);
                                editor.putString("state",state);
                                editor.putString("zip",zip);
                                editor.apply();

                                Intent intent = new Intent(Login.this, LandingScreen.class);
                                intent.putExtra("activityName","addClientClass");
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("user_name",strUser);
                params.put("password",strPass );
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
    public void forgotPasswordDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dalogboxforgot, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        // initializing variables
        final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
        final Button to = (Button) dialogView.findViewById(R.id.add_BT);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();

            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword(Add.getText().toString());
                b.dismiss();
            }
        });

        b = dialogBuilder.create();


        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        b.show();

    }

    public void ForgotPassword(final String email) {

        ringProgressDialog = ProgressDialog.show(this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        if(response.equals("001"))
                        {
                            Toast.makeText(Login.this, "No user found with this Email-ID", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Done!")
                                    .setConfirmText("OK").setContentText("An E-Mail has been sent to your respective ID to reset your password")
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

                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("forgot_email",email);

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