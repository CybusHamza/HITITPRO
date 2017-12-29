package com.cybussolutions.hititpro.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Edit_Profile extends AppCompatActivity {
    String mCurrentPhotoPath,ba1,mSavedPhotoName;

    private static final int REQUEST_PERMISSIONS=0;
    Intent intent;
    private static int IMG_RESULT = 2;
    String ImageDecode;

    EditText FirstName,LastName,UserEmail,UserContact,UserCity,UserAddress,UserCountry,Website,CompanyInfo,Fax,UserState,Zip;
    String username,useremail,usercontact,useraddress,companyinfo,pic,userid,website,fax,userCity,userState,zip;
    String[] flname;
    String[] fulladdress;
    Button updateProfile,changelogo;

    ImageView logo;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog,ringProgressDialog1;
    SharedPreferences pref;
    int keyDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userid = pref.getString("user_id", null);
        pic = pref.getString("img",null);
       // SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        Intent intent = getIntent();
        username = pref.getString("user_name","");
        flname = username.split(" ");
        useremail = pref.getString("email","");
        usercontact = pref.getString("phone","");
        useraddress = pref.getString("adress","");
        companyinfo = pref.getString("company_info","");
        website = pref.getString("website","");
        fax = pref.getString("fax","");
        userCity = pref.getString("city","");
        userState = pref.getString("state","");
        zip = pref.getString("zip","");
        //fulladdress=useraddress.split(",");

        FirstName = (EditText) findViewById(R.id.et_first_name);
        LastName = (EditText) findViewById(R.id.et_last_name);
        UserEmail = (EditText) findViewById(R.id.et_user_email);
        UserContact = (EditText) findViewById(R.id.et_user_contact);
        Website = (EditText) findViewById(R.id.et_user_website);
        UserCity = (EditText) findViewById(R.id.client_city);
        UserState = (EditText) findViewById(R.id.client_state);
        Zip = (EditText) findViewById(R.id.zip);
      //  Fax = (EditText) findViewById(R.id.et_user_fax);
        CompanyInfo = (EditText) findViewById(R.id.et_user_company_info);
        UserAddress = (EditText) findViewById(R.id.et_user_address);
        logo = (ImageView) findViewById(R.id.logo);
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
                    attachLogo();

                }
            }
        });


        String url = End_Points.IMAGE_BASE_URL + pic.trim();
        Picasso.with(getApplicationContext())
                .load(url)
                .resize(300, 300)
                .into(logo);

        updateProfile = (Button) findViewById(R.id.update_profile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=FirstName.getText().toString();
                String lastName=LastName.getText().toString();
                String userEmail=UserEmail.getText().toString();
                String userContact=UserContact.getText().toString();
                String companyInfo=CompanyInfo.getText().toString();
                String userAddress=UserAddress.getText().toString();
                String website=Website.getText().toString();
              //  String fax=Fax.getText().toString();
                pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                pic=pref.getString("img",null);
                if(firstName.equals("")||lastName.equals("")||userEmail.equals("")||userContact.equals("")||companyInfo.equals("")||userAddress.equals("")/*||website.equals("")||fax.equals("")*/){
                    Toast.makeText(Edit_Profile.this,"Plz fill the empty fields",Toast.LENGTH_LONG).show();
                }else {

                UpdateClient();
                }
            }
        });

        FirstName.setText(flname[0]);
        LastName.setText(flname[1]);
        UserEmail.setText(useremail);
        UserContact.setText(usercontact);
        CompanyInfo.setText(companyinfo);
        UserAddress.setText(useraddress);
        Website.setText(website);
        if(userCity.equals("null")){
            UserCity.setText("");
        }else
            UserCity.setText(userCity);
        if(userState.equals("null")){
            UserState.setText("");
        }else
            UserState.setText(userState);
        if(zip.equals("null")){
            Zip.setText("");
        }else
            Zip.setText(zip);

      /*  if(fax.equals("null"))
        Fax.setText("");
        else
        Fax.setText(fax);*/
        UserContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10 && (!UserContact.getText().toString().contains("(")||!UserContact.getText().toString().contains(")")||!UserContact.getText().toString().contains(" ")|| !UserContact.getText().toString().contains("-"))){
                    String strPhone=UserContact.getText().toString();
                    String subPhone1=strPhone.substring(0,3);
                    String subPhone2=strPhone.substring(3,6);
                    String subPhone3=strPhone.substring(6,10);
                    UserContact.setText("("+subPhone1+") "+subPhone2+"-"+subPhone3);
                }

            }
        });
        /*Fax.addTextChangedListener(new TextWatcher() {
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
        });*/

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
                uploadlogo();


            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again"+e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void uploadlogo() {
        ringProgressDialog1 = ProgressDialog.show(this, "", "Please wait ...", true);
        ringProgressDialog1.setCancelable(false);
        ringProgressDialog1.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ringProgressDialog1.dismiss();
                mSavedPhotoName = response;
               SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor edit=pref.edit();
                edit.putString("img",mSavedPhotoName);
                edit.commit();
              //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                //Signup();
                //    uploadToServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog1.dismiss();
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

        RequestQueue requestQueue = Volley.newRequestQueue(Edit_Profile.this);
        requestQueue.add(stringRequest);
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
                                    .setConfirmText("OK").setContentText("Profile Updated Successfully")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("user_name", FirstName.getText().toString()+" "+LastName.getText().toString());
                                            editor.putString("email", UserEmail.getText().toString());
                                           // editor.putString("img", pic);
                                            editor.putString("phone",UserContact.getText().toString());
                                           // editor.putString("city",UserCity.getText().toString());
                                            editor.putString("adress", UserAddress.getText().toString());
                                            editor.putString("company_info", CompanyInfo.getText().toString());
                                            editor.putString("website",Website.getText().toString());
                                            editor.putString("city",UserCity.getText().toString());
                                            editor.putString("state",UserState.getText().toString());
                                            editor.putString("zip",Zip.getText().toString());
                                           // editor.putString("fax",Fax.getText().toString());
                                            editor.commit();
                                            Intent intent=new Intent(Edit_Profile.this,LandingScreen.class);
                                            intent.putExtra("activityName","");
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
                params.put("city",UserCity.getText().toString());
                params.put("state",UserState.getText().toString());
                params.put("zip",Zip.getText().toString());
              //  params.put("fax",Fax.getText().toString());
                params.put("profile_image",pic);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
               attachLogo();
            } else {


                Toast.makeText(Edit_Profile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                View view = this.getCurrentFocus();
                if(view!=null) {
                    InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                // I do not want this...
                // Home as up button is to navigate to Home-Activity not previous acitivity
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
