package com.cybussolutions.hititpro.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

/**
 * Created by Rizwan Butt on 26-Apr-17.
 */
public class Add_Comments extends AppCompatActivity {
    EditText etComments;
    RadioGroup radioGroup;
    int pos;
    private String ba1,mCurrentPhotoPath;
    private String mSavedPhotoName;

    SharedPreferences sp;
    String userId,checkedBox;
    private RadioButton radioButton;
    Button btnSaveImage;

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
        etComments= (EditText) findViewById(R.id.comments);
        btnSaveImage=(Button)findViewById(R.id.saveImageButton);
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up();
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        pos=radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(pos);
        checkedBox=radioButton.getText().toString();
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos=radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(pos);
                checkedBox=radioButton.getText().toString();
                    //up();

            }
        });
    }
    private void up(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);
        // Toast.makeText(getApplicationContext(),ba1.toString(),Toast.LENGTH_LONG).show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, End_Points.UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSavedPhotoName=response;
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                uploadToServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image",ba1);
                params.put("name",formattedDate);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Add_Comments.this);
        requestQueue.add(stringRequest);
    }
    private void uploadToServer() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = pref.getString("user_id","");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String attachment_added_date = df.format(c.getTime());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, End_Points.UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("template_id",StructureScreensActivity.template_id);
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("main_form_name",sp.getString("main_screen",""));
                params.put("column_name",sp.getString("heading",""));
                params.put("attachment_name","test");
                params.put("attachment_original_name",mCurrentPhotoPath);
                params.put("attachment_saved_name", mSavedPhotoName);
                params.put("image_comments",etComments.getText().toString());
                params.put("selrecomd",checkedBox);
                params.put("userid",userId);
                params.put("attchment_added_date",attachment_added_date);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Add_Comments.this);
        requestQueue.add(stringRequest);
    }
}
