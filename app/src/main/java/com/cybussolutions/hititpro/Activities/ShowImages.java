package com.cybussolutions.hititpro.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowImages extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    String ImageName,data,table_name;
    String[] imageNames;
    ImageView imageView,addNew;
    ImageButton image1,image2,image3,image4;
    EditText imageComments;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("View Images");
        setSupportActionBar(toolbar);

        getDefaultComments();
        imageView= (ImageView) findViewById(R.id.selectedImage);

        image1= (ImageButton) findViewById(R.id.image1);
        image2= (ImageButton) findViewById(R.id.image2);
        image3= (ImageButton) findViewById(R.id.image3);
        imageComments= (EditText) findViewById(R.id.imageComments);
//        image4= (ImageButton) findViewById(R.id.image4);

        addNew= (ImageView) findViewById(R.id.add_pic);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<3) {
                    Intent intent=new Intent(ShowImages.this,MainActivity.class);
                    intent.putExtra("dbTable",table_name);
                    intent.putExtra("data",data);
                    intent.putExtra("showImages","true");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(ShowImages.this,Add_Comments.class);
                    intent.putExtra("dbTable",table_name);
                    intent.putExtra("data",data);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "you cannot upload more photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent intent=getIntent();

        imageNames=intent.getStringArrayExtra("imageNames");
        data=intent.getStringExtra("data");
        table_name=intent.getStringExtra("dbTable");
        for (int i=0;i<imageNames.length;i++){
            if(imageNames[i].equals("")){
                count--;
            }else {
                count++;
            }
        }

        if(imageNames.length>0) {
            String url = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[0].trim();

                Picasso.with(ShowImages.this)
                        .load(url)
                        .into(image1);
        }

        if(imageNames.length>1) {
            String url1 = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[1].trim();
            Picasso.with(ShowImages.this)
                    .load(url1)
                    .into(image2);
        }

        if(imageNames.length>2) {
            String url2 = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[2].trim();
            Picasso.with(ShowImages.this)
                    .load(url2)
                    .into(image3);
        }
        imageView.setImageDrawable(image1.getDrawable());
        /*if(imageNames.length>3) {
            String url3 = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[3];
            Picasso.with(getApplicationContext())
                    .load(url3)
                    .into(image4);
        }*/

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image1.getDrawable());
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image2.getDrawable());
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image3.getDrawable());
            }
        });
       /* image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image4.getDrawable());
            }
        });*/
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

                               String imageText=object.getString("defaulttext");
                                imageComments.setText(imageText);
                                //etComments.setText(defaultText);
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

                    new SweetAlertDialog(ShowImages.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(ShowImages.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("fieldid",data);
                params.put("template_id",StructureScreensActivity.template_id);
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ShowImages.this);
        requestQueue.add(request);

    }
}
