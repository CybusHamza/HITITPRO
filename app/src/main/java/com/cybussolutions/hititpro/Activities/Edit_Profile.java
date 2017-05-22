package com.cybussolutions.hititpro.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.cybussolutions.hititpro.Helper.CircleTransform;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Edit_Profile extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS=0;
    Intent intent;
    private static int IMG_RESULT = 2;
    String ImageDecode;

    EditText FirstName,LastName,UserEmail,UserContact,UserCity,UserAddress,UserCountry,Website,CompanyInfo,Fax;
    String username,useremail,usercontact,useraddress,companyinfo,pic,userid,website,fax;
    String[] flname;
    String[] fulladdress;
    Button updateProfile,changelogo;

    ImageView logo;

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
        companyinfo=intent.getStringExtra("company_info");
        pic=intent.getStringExtra("img");
        website=intent.getStringExtra("website");
        fax=intent.getStringExtra("fax");
        //fulladdress=useraddress.split(",");

        FirstName = (EditText) findViewById(R.id.et_first_name);
        LastName = (EditText) findViewById(R.id.et_last_name);
        UserEmail = (EditText) findViewById(R.id.et_user_email);
        UserContact = (EditText) findViewById(R.id.et_user_contact);
        Website = (EditText) findViewById(R.id.et_user_website);
        Fax = (EditText) findViewById(R.id.et_user_fax);
        CompanyInfo = (EditText) findViewById(R.id.et_user_company_info);
        UserAddress = (EditText) findViewById(R.id.et_user_address);
        logo= (ImageView) findViewById(R.id.logo);
        changelogo = (Button) findViewById(R.id.change_logo_button);
        changelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Edit_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                    if (Build.VERSION.SDK_INT > 22) {

                        requestPermissions(new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);

                    }

                } else {
                    //attachLogo();

                }
            }
        });



        String url = End_Points.IMAGE_BASE_URL + pic;
        Picasso.with(getApplicationContext())
                .load(url)
                .resize(300, 300)
                .into(logo);

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
        CompanyInfo.setText(companyinfo);
        Website.setText(website);
        Fax.setText(fax);

//        UserCity.setText(fulladdress[1]);
        UserAddress.setText(useraddress);
  //      UserCountry.setText(fulladdress[2]);
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
                                            editor.putString("img", pic);
                                            editor.putString("phone",UserContact.getText().toString());
                                           // editor.putString("city",UserCity.getText().toString());
                                            editor.putString("adress", UserAddress.getText().toString());
                                            editor.putString("company_info", CompanyInfo.getText().toString());
                                            editor.putString("website",Website.getText().toString());
                                            editor.putString("fax",Fax.getText().toString());
                                            editor.commit();
                                            Intent intent=new Intent(Edit_Profile.this,LandingScreen.class);
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
                params.put("company_info",CompanyInfo.getText().toString());
                params.put("adress", UserAddress.getText().toString());
                params.put("website",Website.getText().toString());
                params.put("fax",Fax.getText().toString());
               // params.put("country",UserCountry.getText().toString());
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
