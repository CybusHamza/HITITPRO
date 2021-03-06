package com.cybussolutions.hititpro.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Add_Comments extends AppCompatActivity {
    ProgressDialog ringProgressDialog;

    private static final int MY_SOCKET_TIMEOUT_MS = 15000;

    EditText etComments;
    RadioGroup radioGroup;
    String recomendation;

    int pos;
    private String ba1,mCurrentPhotoPath;
    private String mSavedPhotoName,data,defaultText,attachmentName,tablename;

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    String userId,checkedBox;
    private RadioButton radioButton;
    Button btnSaveImage;
    String clientId,templateId,inspectionId;
   // String[] imageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_screen);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Add Comments");
        setSupportActionBar(toolbar);

        sp=getSharedPreferences("prefs", MODE_PRIVATE);

        Intent i=getIntent();
        mCurrentPhotoPath=i.getStringExtra("mCurrentPhotoPath");
        attachmentName=i.getStringExtra("attachmentName");
        data=i.getStringExtra("data");
        tablename=i.getStringExtra("dbTable");
        clientId=i.getStringExtra("clientId");
        templateId=i.getStringExtra("templateId");
        inspectionId=i.getStringExtra("inspectionId");
       // imageNames=i.getStringArrayExtra("imageNames");
        etComments= (EditText) findViewById(R.id.comments);
        btnSaveImage=(Button)findViewById(R.id.saveImageButton);
        if(mCurrentPhotoPath == null || mCurrentPhotoPath==""){
            btnSaveImage.setText("Save Comments");
        }else {
            btnSaveImage.setText("Save Image");
        }
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check=etComments.getText().toString();
                String messageToast="You must select some recommendation";
                /*if(check.equals("") || check==""){
                    messageToast="Please enter some comment";
                }*/
               // Boolean b=(check!="") && (radioGroup.getCheckedRadioButtonId()==-1);
                if(!etComments.getText().toString().equals("") && (radioGroup.getCheckedRadioButtonId()==-1)) {
                    Toast.makeText(getApplicationContext(),messageToast,Toast.LENGTH_LONG).show();

                }else{
                    if (mCurrentPhotoPath == null || mCurrentPhotoPath=="") {
                        if(radioGroup.getCheckedRadioButtonId()==-1){
                            checkedBox="";
                        }
                        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                        pos=radioGroup.getCheckedRadioButtonId();
                        if(pos!=-1) {
                            radioButton = (RadioButton) findViewById(pos);
                            checkedBox = radioButton.getText().toString();
                        }
                        mSavedPhotoName = "";
                        mCurrentPhotoPath = "";
                        attachmentName="";
                        uploadToServer();
                    } else {
                        if(radioGroup.getCheckedRadioButtonId()==-1){
                            checkedBox="";
                        }else {
                            radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                            pos=radioGroup.getCheckedRadioButtonId();
                            if(pos!=-1) {
                                radioButton = (RadioButton) findViewById(pos);
                                checkedBox = radioButton.getText().toString();
                            }
                        }
                        up();
                       // startDialog();
                    }
                }
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        pos=radioGroup.getCheckedRadioButtonId();
        if(pos!=-1) {
            radioButton = (RadioButton) findViewById(pos);
            checkedBox = radioButton.getText().toString();
        }
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                if(pos!=-1) {
                    pos = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(pos);
                    checkedBox = radioButton.getText().toString();
                }
                //up();

            }
        });

        getDefaultComments();
    }

    private void up() {
        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Uploading data ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
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

                mSavedPhotoName = response;
                MainActivity.mainActivityCount++;
              //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                uploadToServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {
                    new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    //finish();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                   // finish();

                                }
                            })
                            .show();
                }
             //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Comments.this);
        requestQueue.add(stringRequest);
    }

    private void uploadToServer() {
        if (mCurrentPhotoPath == null || mCurrentPhotoPath=="") {
            ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Uploading data ...", true);
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.show();
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = pref.getString("user_id", "");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String attachment_added_date = df.format(c.getTime());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ringProgressDialog.dismiss();
                new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setConfirmText("OK").setContentText("Successful")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                if (mCurrentPhotoPath != null && mCurrentPhotoPath!="" && MainActivity.mainActivityCount<3) {
                                    startDialog();
                                }else {
                                   /* Intent intent=new Intent(Add_Comments.this,ShowImages.class);
                                    intent.putExtra("dbTable",tablename);
                                    intent.putExtra("data",data);
                                    intent.putExtra("clientId",StructureScreensActivity.client_id);
                                    intent.putExtra("inspectionId",StructureScreensActivity.inspectionID);
                                    intent.putExtra("templateId",StructureScreensActivity.template_id);
                                    intent.putExtra("imageNames", imageNames);*/
                                    finish();
                                  //  startActivity(intent);
                                }
                            }
                        })
                        .show();


                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.ERROR_TYPE)
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
               // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("main_form_name", sp.getString("main_screen", ""));
                params.put("column_name", tablename);/*sp.getString("heading", "")*/
                params.put("element_id",data);
                params.put("attachment_name", " ");
                params.put("attachment_original_name", mSavedPhotoName);
                params.put("attachment_saved_name", mSavedPhotoName);
                params.put("image_comments", etComments.getText().toString());
                params.put("selrecomd", checkedBox);
                params.put("userid", userId);
                params.put("attchment_added_date", attachment_added_date);
                params.put("dbTable",tablename+"_comments");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Add_Comments.this);
        requestQueue.add(stringRequest);
    }
    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                Add_Comments.this);
        myAlertDialog.setTitle("Alert");
        myAlertDialog.setMessage("Do you want to add more pictures");

        myAlertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        edit = sp.edit();
                        edit.putString("imagecomments", etComments.getText().toString());
                        edit.putString("selectrecomend", checkedBox);
                        // edit.putString("back","add_comments");
                        edit.putBoolean("flag", true);
                        edit.commit();
                        finish();
                        Intent intent=new Intent(Add_Comments.this,MainActivity.class);
                        intent.putExtra("dbTable",tablename);
                        intent.putExtra("data",data);
                        intent.putExtra("showImages","true");
                        intent.putExtra("clientId",clientId);
                        intent.putExtra("inspectionId",inspectionId);
                        intent.putExtra("templateId",templateId);
                        intent.putExtra("attachmentName",attachmentName);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        myAlertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        edit = sp.edit();
                        edit.putString("comments",null);
                        edit.putString("selectrecomend",null);
//                      edit.putString("back",null);
                        edit.putBoolean("flag", true);
                        edit.commit();
                        finish();
                    }
                });
        myAlertDialog.show();
    }

    public void getDefaultComments() {

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_DEFAULT_COMMENTS_IMAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject object =jsonArray.getJSONObject(i);
                                defaultText=object.getString("defaulttext");
                                etComments.setText(defaultText);
                                recomendation=object.getString("selrecomd");
                                if(recomendation.equals("Monitor")){
                                    radioButton= (RadioButton) findViewById(R.id.radio0);
                                    radioButton.setChecked(true);
                                }
                                else if(recomendation.equals("Repair")){
                                    radioButton= (RadioButton) findViewById(R.id.radio1);
                                    radioButton.setChecked(true);
                                }
                                else if(recomendation.equals("Safety Issue")){
                                    radioButton= (RadioButton) findViewById(R.id.radio3);
                                    radioButton.setChecked(true);
                                }
                                else if(recomendation.equals("Improve")){
                                    radioButton= (RadioButton) findViewById(R.id.radio4);
                                    radioButton.setChecked(true);
                                }
                                else if(recomendation.equals("Concern")){
                                    radioButton= (RadioButton) findViewById(R.id.radio5);
                                    radioButton.setChecked(true);
                                }else if(recomendation.equals("observation_notCheck")){
                                    radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                                    radioGroup.setVisibility(View.GONE);
                                    TextView textView= (TextView) findViewById(R.id.selrecTV);
                                    textView.setVisibility(View.INVISIBLE);

                                }else {
                                    RadioButton radioButton= (RadioButton) findViewById(R.id.radio0);
                                    radioButton.setChecked(false);
                                    RadioButton radioButton1= (RadioButton) findViewById(R.id.radio1);
                                    radioButton1.setChecked(false);
                                    RadioButton radioButton2= (RadioButton) findViewById(R.id.radio3);
                                    radioButton2.setChecked(false);
                                    RadioButton radioButton3= (RadioButton) findViewById(R.id.radio4);
                                    radioButton3.setChecked(false);
                                    RadioButton radioButton4= (RadioButton) findViewById(R.id.radio5);
                                    radioButton4.setChecked(false);
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Add_Comments.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("fieldid",data);
                params.put("template_id",templateId);
                params.put("client_id",clientId);
                params.put("inspection_id",inspectionId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Comments.this);
        requestQueue.add(request);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}