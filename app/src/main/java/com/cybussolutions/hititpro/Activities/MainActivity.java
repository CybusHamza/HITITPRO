package com.cybussolutions.hititpro.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DrawingView imageView;
    Spinner spinnerDropDown;
    String[] recomend = {
            "Select Recommendation",
            "Monitor",
            "b",
            "c",
            "d"
    };

    //protected DrawingView mDrawingView;
    private static final int REQUEST_PERMISSIONS=0;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    Button b,saveImage,drawLine,drawCircle,drawSquare,drawArrow,drawPen;
   // private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    private static int IMG_RESULT = 2;
    String ImageDecode;
    View mview;
    ImageView imageViewLoad;
    Button LoadImage;
    LinearLayout linearLayout;
    public int mCurrentShape;
    float mx,my;
    Intent intent;
    String[] FILE;

    EditText et;
    private String mCurrentPhotoPath,ba1,mSavedPhotoName;

    Bitmap bm = null;
    String userId;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Image Editer");
        setSupportActionBar(toolbar);
       imageView = (DrawingView)findViewById(R.id.drawingview);
        //spinnerDropDown =(Spinner)findViewById(R.id.spinner2);
        //ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
          //      R.layout.simple_spinner_dropdown_item ,recomend);

        //spinnerDropDown.setAdapter(adapter);

       sp=getSharedPreferences("prefs",MODE_PRIVATE);

       // et=(EditText)findViewById(R.id.comments);

        b=(Button)findViewById(R.id.button);
        saveImage=(Button)findViewById(R.id.saveButton);
        drawLine=(Button)findViewById(R.id.button2);
        drawCircle=(Button)findViewById(R.id.button3);
        drawSquare=(Button)findViewById(R.id.button4);
        drawArrow=(Button)findViewById(R.id.button5);
        drawPen=(Button)findViewById(R.id.button6);

        drawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.mCurrentShape = DrawingView.LINE;
                imageView.reset();
            }
        });
        drawCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.mCurrentShape = DrawingView.CIRCLE;
                imageView.reset();
            }
        });
        drawSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.mCurrentShape = DrawingView.SQUARE;
                imageView.reset();
            }
        });
        drawArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.mCurrentShape = DrawingView.ARROW;
                imageView.reset();
            }
        });
        drawPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               imageView.mCurrentShape = DrawingView.SMOOTHLINE;
                imageView.reset();
               // uploadToServer();

                // Upload image to server
               // new uploadToServer().execute();
               // imageView.mCurrentShape = DrawingView.SMOOTHLINE;
                //imageView.reset();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    imageView.buildDrawingCache();
                     bm = imageView.getDrawingCache();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"no image selected",Toast.LENGTH_LONG).show();
                }
                if (bm != null) {
                   // up();
                    OutputStream fOut = null;
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    Uri outputFileUri;
                    try {
                        File root = new File(Environment.getExternalStorageDirectory()
                                + File.separator + "folder_name" + File.separator);
                        root.mkdirs();
                        File sdImageMainDirectory = new File(root, formattedDate + "myPicName.jpg");
                        outputFileUri = Uri.fromFile(sdImageMainDirectory);
                        fOut = new FileOutputStream(sdImageMainDirectory);
                        Intent intent=new Intent(MainActivity.this,Add_Comments.class);
                        intent.putExtra("mCurrentPhotoPath",mCurrentPhotoPath);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error occured. Please try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
                    try {
                        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"No image selected",Toast.LENGTH_LONG).show();
                }
            }
        });
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container1, new ShapeFragment())
                    .commit();
        }
    }

    private void up(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba,Base64.NO_WRAP);
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
                // params.put("user_name", );
                //params.put("password", );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                params.put("attachment_saved_name",mSavedPhotoName);
                params.put("image_comments",et.getText().toString());
                params.put("selrecomd",spinnerDropDown.getSelectedItem().toString());
                params.put("userid",userId);
                params.put("attchment_added_date",attachment_added_date);

               // params.put("user_name", );
                //params.put("password", );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void camera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           /* uri = getOutputMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);*/
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        //  imageView.mCurrentShape = DrawingView.SMOOTHLINE;
        //imageView.reset();
    }

    private void upload() {
        intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_RESULT);
    }


    public void text(){
        if(DrawingView.textInput.equals("text")){

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(ShapeFragment.inputtext.equals("text"))
        {
            mx = event.getX();
            my = event.getY();


            DrawingView drawingView = new DrawingView(MainActivity.this);

            drawingView.onTouchEventText(event,mx,my,"hello");
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView=(DrawingView)findViewById(R.id.drawingview);
                //imageView.setImageBitmap(imageBitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
               // Toast.makeText(MainActivity.this,"Here "+ getRealPathFromURI(tempUri),Toast.LENGTH_LONG).show();
                mCurrentPhotoPath=getPathFromURI(tempUri);
                imageView.setImageBitmap(BitmapFactory.decodeFile(getRealPathFromURI(tempUri)));
               // img.setImageBitmap(imageBitmap);

            }
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();
                imageView=(DrawingView)findViewById(R.id.drawingview);
                mCurrentPhotoPath=ImageDecode;
                imageView.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
               // imageView.setImageBitmap(BitmapFactory
                 //       .decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again"+e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {




        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                if(requestCode==IMG_RESULT) {
                    upload();
                }
                if(requestCode==REQUEST_IMAGE_CAPTURE){
                    camera();
                }
            } else {


                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                MainActivity.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                            if (Build.VERSION.SDK_INT > 22) {

                                requestPermissions(new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS);
                      /*  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);*/
                                // Toast.makeText(MainActivity.this.getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();

                            }

                        }

                        else
                        {
                            upload();

                        }

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                            if (Build.VERSION.SDK_INT > 22) {

                                requestPermissions(new String[]{Manifest.permission
                                                .CAMERA},
                                        REQUEST_IMAGE_CAPTURE);
                      /*  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);*/
                                // Toast.makeText(MainActivity.this.getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();

                            }

                        }

                        else
                        {
                            camera();

                        }

                    }
                });
        myAlertDialog.show();
    }

}