package com.cybussolutions.hititpro.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class SignupActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS=0;
    Intent intent;
    private static int IMG_RESULT = 2;
    String ImageDecode;
    ImageView logo;
    int keyDel;

    String mCurrentPhotoPath,ba1,mSavedPhotoName;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText f_name,l_name,emial,phone,website,adress,password,comfirm_password,company_info,fax;
    String strf_name,strl_name,stremial,strphone,strwebsite,stradress,strcompanyinfo,strpassword,strconfirmpassword,strfax;
    Button signup,attachLogo;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        f_name = (EditText) findViewById(R.id.fist_name_et);
        l_name = (EditText) findViewById(R.id.last_name_et);
        emial = (EditText) findViewById(R.id.email_et);
        phone = (EditText) findViewById(R.id.phone_et);
        fax= (EditText) findViewById(R.id.fax_et);
        website = (EditText) findViewById(R.id.website_et);
        adress = (EditText) findViewById(R.id.address_et);
       // country = (EditText) findViewById(R.id.country_et);
        password = (EditText) findViewById(R.id.password_et);
        comfirm_password=(EditText)findViewById(R.id.confirm_password_et);
        company_info=(EditText)findViewById(R.id.company_info_et);

        signup = (Button) findViewById(R.id.signUp_button);
        attachLogo = (Button) findViewById(R.id.logo_button);

        logo= (ImageView) findViewById(R.id.logo);

        attachLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT > 22) {
                        requestPermissions(new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);

                    }
                } else {
                    attachLogo();

                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10 && (!phone.getText().toString().contains("(")||!phone.getText().toString().contains(")")||!phone.getText().toString().contains(" ")|| !phone.getText().toString().contains("-"))){
                    String strPhone=phone.getText().toString();
                    String subPhone1=strPhone.substring(0,3);
                    String subPhone2=strPhone.substring(3,6);
                    String subPhone3=strPhone.substring(6,10);
                    phone.setText("("+subPhone1+") "+subPhone2+"-"+subPhone3);
                }

            }
        });
        fax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10 && (!fax.getText().toString().contains("(")||!fax.getText().toString().contains(")")||!fax.getText().toString().contains(" ")|| !fax.getText().toString().contains("-"))){
                    String strPhone=fax.getText().toString();
                    String subPhone1=strPhone.substring(0,3);
                    String subPhone2=strPhone.substring(3,6);
                    String subPhone3=strPhone.substring(6,10);
                    fax.setText("("+subPhone1+") "+subPhone2+"-"+subPhone3);
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strf_name = f_name.getText().toString();
                strl_name = l_name.getText().toString();
                stremial = emial.getText().toString();
                strphone = phone.getText().toString();
                strwebsite = website.getText().toString();
                stradress = adress.getText().toString();
                strcompanyinfo = company_info.getText().toString();
                strpassword = password.getText().toString();
                strconfirmpassword=comfirm_password.getText().toString();
                strfax=fax.getText().toString();

                // calling for sign up

                if(strf_name.equals("") || strl_name.equals("")|| stremial.equals("")|| strphone.equals("")
                   || strwebsite.equals("")|| stradress.equals("")|| strcompanyinfo.equals("")|| strpassword.equals(""))
                {
                    Toast.makeText(SignupActivity.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(stremial.matches(emailPattern) && strpassword.length()>=8 && strpassword.equals(strconfirmpassword))
                    {
                        uploadlogo();
                        //Signup();
                    }else if(strpassword.length()<=7){
                        Toast.makeText(getApplicationContext(),"Password length can not be less than 8 characters!",Toast.LENGTH_LONG).show();
                    }
                    else if(!strpassword.equals(strconfirmpassword)){
                        Toast.makeText(getApplicationContext(),"Password Mismatches",Toast.LENGTH_LONG).show();
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


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SIGN_UP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
                params.put("website", strwebsite);
                params.put("adress", stradress);
                params.put("company_info", strcompanyinfo);
                params.put("profile_image", mSavedPhotoName);
                params.put("fax",strfax);

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
    private void attachLogo() {
        intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_RESULT);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            //  if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (requestCode == IMG_RESULT  && resultCode == RESULT_OK && data != null) {
                // textView.setText(stringBuffer.toString());
                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                mCurrentPhotoPath=ImageDecode;
                int w=logo.getWidth();
                int h=logo.getHeight();
                Bitmap unscaled=BitmapFactory.decodeFile(ImageDecode);
                Bitmap scaled=unscaled.createScaledBitmap(unscaled,w,h,true);

                cursor.close();
                logo.setImageBitmap(scaled);


            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again"+e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void uploadlogo() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

        ringProgressDialog = ProgressDialog.show(this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ringProgressDialog.dismiss();
                mSavedPhotoName = response;
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Signup();
                //    uploadToServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", ba1);
                params.put("name", formattedDate);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        requestQueue.add(stringRequest);
    }
}
