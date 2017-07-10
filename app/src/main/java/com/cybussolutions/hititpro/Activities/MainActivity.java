package com.cybussolutions.hititpro.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.graphics.CanvasView;
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
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.graphics.Color.BLACK;

public class MainActivity extends AppCompatActivity {

    static int mainActivityCount= 0;

    ProgressDialog ringProgressDialog;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
//    DrawingView imageView;

    private static final int REQUEST_PERMISSIONS=0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button selectImage,saveImage,drawLine,drawCircle,drawSquare,drawArrow,drawPen,undo,redo;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    private static int IMG_RESULT = 2;
    String ImageDecode;

    public int mCurrentShape;
    float mx,my;
    Intent intent;
    String[] FILE;
    String data;
    String table_name;

    EditText etAttachmentName;
    private String mCurrentPhotoPath,ba1,mSavedPhotoName;
    String mainFormName;

    Bitmap bm = null;
    Bitmap scaled=null;
    String userId;

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    private CanvasView canvas = null;
     String showImage,clientId,templateId,inspectionId;

    String attachment_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("prefs",MODE_PRIVATE);
        mainFormName=sp.getString("main_screen","");
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Image Editor");
        setSupportActionBar(toolbar);
       // imageView = (DrawingView)findViewById(R.id.drawingview);
        this.canvas = (CanvasView)this.findViewById(R.id.canvas);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        table_name=intent.getStringExtra("dbTable");
        showImage=intent.getStringExtra("showImages");
        clientId=intent.getStringExtra("clientId");
        templateId=intent.getStringExtra("templateId");
        inspectionId=intent.getStringExtra("inspectionId");
        attachment_name=intent.getStringExtra("attachmentName");

        if(showImage.equals("false"))
        getImages();

        if(scaled!=null){
            scaled.recycle();
        }



        selectImage=(Button)findViewById(R.id.selectButton);
        saveImage=(Button)findViewById(R.id.saveButton);
        drawLine=(Button)findViewById(R.id.line);
        drawCircle=(Button)findViewById(R.id.circle);
        drawSquare=(Button)findViewById(R.id.square);
        drawArrow=(Button)findViewById(R.id.arrow);
        drawPen=(Button)findViewById(R.id.pen);
        undo= (Button) findViewById(R.id.undo);
        redo= (Button) findViewById(R.id.redo);
        etAttachmentName= (EditText) findViewById(R.id.et_attachment_name);
        etAttachmentName.setText(attachment_name);

        if(scaled==null){
            drawLine.setVisibility(View.INVISIBLE);
            drawCircle.setVisibility(View.INVISIBLE);
            drawSquare.setVisibility(View.INVISIBLE);
            drawArrow.setVisibility(View.INVISIBLE);
            drawPen.setVisibility(View.INVISIBLE);
            undo.setVisibility(View.INVISIBLE);
            redo.setVisibility(View.INVISIBLE);
            canvas.setVisibility(View.INVISIBLE);
        }else {
            drawLine.setVisibility(View.VISIBLE);
            drawCircle.setVisibility(View.VISIBLE);
            drawSquare.setVisibility(View.VISIBLE);
            drawArrow.setVisibility(View.VISIBLE);
            drawPen.setVisibility(View.VISIBLE);
            undo.setVisibility(View.VISIBLE);
            redo.setVisibility(View.VISIBLE);
            canvas.setVisibility(View.VISIBLE);

        }

        drawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*imageView.mCurrentShape = DrawingView.LINE;
                imageView.reset();*/
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.LINE);
            }
        });
        drawCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.mCurrentShape = DrawingView.CIRCLE;
//                imageView.reset();
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.CIRCLE);
            }
        });
        drawSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* imageView.mCurrentShape = DrawingView.SQUARE;
                imageView.reset();*/
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.RECTANGLE);
            }
        });
        drawArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* imageView.mCurrentShape = DrawingView.ARROW;
                imageView.reset();*/
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.ARROW);
            }
        });
        drawPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.PEN);
                /*imageView.mCurrentShape = DrawingView.SMOOTHLINE;
                imageView.reset();*/
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canvas.canUndo()) {
                    canvas.undo();
                }else {
                    Toast.makeText(MainActivity.this,"Nothing to undo",Toast.LENGTH_LONG).show();
                }
            }
        });
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canvas.canRedo()) {
                    canvas.redo();
                }else {
                    Toast.makeText(MainActivity.this,"Nothing to redo",Toast.LENGTH_LONG).show();
                }
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (scaled != null) {
                    try {
                        canvas.buildDrawingCache();
                        scaled=canvas.getDrawingCache();
                        // scaled = canvas.getDrawingCache();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "no image selected", Toast.LENGTH_LONG).show();
                    }
                    if (sp.getBoolean("flag", false) == true) {
                        String check=etAttachmentName.getText().toString();
                        if(check.equals("")){
                            Toast.makeText(getApplicationContext(),"Plz enter some attachment name",Toast.LENGTH_LONG).show();
                        }
                        else {
                            up();
                        }

                    } else {
                        OutputStream fOut = null;
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        Uri outputFileUri;
                        try {

                            String check=etAttachmentName.getText().toString();
                            if(check.equals("")){
                               // Toast ToastMessage =
                                 Toast.makeText(getApplicationContext(),"Plz enter some attachment name",Toast.LENGTH_SHORT).show();
//                                View toastView = ToastMessage.getView();
//                                toastView.setBackgroundResource(R.color.colorPrimary);
//                                ToastMessage.show();
                            }
                            else {
                                File root = new File(Environment.getExternalStorageDirectory()
                                        + File.separator + "folder_name" + File.separator);
                                root.mkdirs();
                                File sdImageMainDirectory = new File(root, formattedDate + "myPicName.jpg");
                                outputFileUri = Uri.fromFile(sdImageMainDirectory);
                                fOut = new FileOutputStream(sdImageMainDirectory);
                                Intent intent = new Intent(MainActivity.this, Add_Comments.class);
                                mCurrentPhotoPath=outputFileUri.getPath();
                                intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
                                intent.putExtra("attachmentName", etAttachmentName.getText().toString());
                                intent.putExtra("dbTable",table_name);
                                intent.putExtra("data", data);
                                intent.putExtra("clientId",clientId);
                                intent.putExtra("inspectionId",inspectionId);
                                intent.putExtra("templateId",templateId);
                                finish();
                                startActivity(intent);
                                try {
                                    scaled.compress(Bitmap.CompressFormat.JPEG,100,fOut);
                                    // bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error occured. Please try again later.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    mCurrentPhotoPath=null;
                    Intent intent = new Intent(MainActivity.this, Add_Comments.class);
                    intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
                    intent.putExtra("attachmentName","");
                    intent.putExtra("dbTable",table_name);
                    intent.putExtra("data", data);
                    intent.putExtra("clientId",clientId);
                    intent.putExtra("inspectionId",inspectionId);
                    intent.putExtra("templateId",templateId);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_LONG).show();
                }
            }
        });
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container1, new ShapeFragment())
//                    .commit();
//        }
    }

    private void getImages() {
        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Getting data ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, End_Points.GET_IMAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ringProgressDialog.dismiss();
                mainActivityCount=0;
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                if(!response.equals("0") && response!=null){

                    int count=0;
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        String[] imageName = new String[jsonArray.length()];
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            if(!jsonObject.getString("attachment_saved_name").equals("")) {
                                imageName[i] = jsonObject.getString("attachment_saved_name");
                                attachment_name=jsonObject.getString("attachment_name");
                                count++;
                                mainActivityCount++;
                            }
                            else imageName[i]="";
                        }
                        if(count>0) {
                            finish();
                            Intent i = new Intent(MainActivity.this, ShowImages.class);
                            i.putExtra("imageNames", imageName);
                            i.putExtra("dbTable",table_name);
                            i.putExtra("data",data);
                            i.putExtra("attachmentName",attachment_name);
                            startActivity(i);
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        ringProgressDialog.dismiss();

                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    finish();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("template_id",templateId);
                params.put("inspection_id",inspectionId);
                params.put("client_id",clientId);
                params.put("main_form_name",mainFormName);
                params.put("column_name",table_name);//sp.getString("heading","")
                params.put("element_id",data);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void up(){
        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Uploading data ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        OutputStream fOut = null;
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        Uri outputFileUri;
        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + "folder_name" + File.separator);
        root.mkdirs();
        File sdImageMainDirectory = new File(root, formattedDate + "myPicName.jpg");
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        try {
            fOut = new FileOutputStream(sdImageMainDirectory);
            scaled.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            // bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath=outputFileUri.getPath();
       // Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //final String formattedDate = df.format(c.getTime());
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
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                uploadToServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                ringProgressDialog.dismiss();
                mainActivityCount++;
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setConfirmText("OK").setContentText("Successful")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                Intent intent = getIntent();
                                intent.putExtra("clientId",clientId);
                                intent.putExtra("inspectionId",inspectionId);
                                intent.putExtra("templateId",templateId);
                                intent.putExtra("showImages","false");
                                finish();
                                startActivity(intent);

                                //finish();

                            }
                        })
                        .show();
               // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
              //  Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("template_id",templateId);
                params.put("inspection_id",inspectionId);
                params.put("client_id",clientId);
                params.put("main_form_name",mainFormName);
                params.put("column_name",table_name);
                params.put("element_id",data);
                params.put("attachment_name",etAttachmentName.getText().toString());
                params.put("attachment_original_name",mSavedPhotoName);/*mCurrentPhotoPath*/
                params.put("attachment_saved_name",mSavedPhotoName);
                params.put("image_comments",sp.getString("imagecomments",null));
                params.put("selrecomd",sp.getString("selectrecomend",null));
                params.put("userid",userId);
                params.put("attchment_added_date",attachment_added_date);
                params.put("dbTable",table_name+"_comments");

                // params.put("user_name", );
                //params.put("password", );
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(ShapeFragment.inputtext.equals("text"))
        {
            mx = event.getX();
            my = event.getY();
            DrawingView drawingView = new DrawingView(MainActivity.this);
            drawingView.onTouchEventText(event,mx,my,"hello");
        }
        return false;
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                int w=canvas.getWidth();
                int h=canvas.getHeight();
                scaled=imageBitmap.createScaledBitmap(imageBitmap,w,h,true);
                //imageView=(DrawingView)findViewById(R.id.drawingview);
                //imageView.setImageBitmap(imageBitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                canvas.drawBitmap(scaled);
                // Toast.makeText(MainActivity.this,"Here "+ getRealPathFromURI(tempUri),Toast.LENGTH_LONG).show();
                mCurrentPhotoPath=getPathFromURI(tempUri);
               // imageView.setImageBitmap(BitmapFactory.decodeFile(getRealPathFromURI(tempUri)));
                // img.setImageBitmap(imageBitmap);
                drawLine.setVisibility(View.VISIBLE);
                drawCircle.setVisibility(View.VISIBLE);
                drawSquare.setVisibility(View.VISIBLE);
                drawArrow.setVisibility(View.VISIBLE);
                drawPen.setVisibility(View.VISIBLE);
                undo.setVisibility(View.VISIBLE);
                redo.setVisibility(View.VISIBLE);
                canvas.setVisibility(View.VISIBLE);
                while (canvas.canUndo()){
                    canvas.historyPointer=canvas.historyPointer-1;
                }
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.PEN);

            }
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
                cursor.close();
                int w=canvas.getWidth();
                int h=canvas.getHeight();
                /* Get the size of the image */
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(ImageDecode, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
                int scaleFactor = 1;
                if ((w > 0) || (h > 0)) {
                    scaleFactor = Math.min(photoW/w, photoH/h);
                }

		/* Set bitmap options to scale the image decode target */
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap unscaled=BitmapFactory.decodeFile(ImageDecode);
                scaled=unscaled.createScaledBitmap(unscaled,w,h,true);

               Uri tempUri = getImageUri(getApplicationContext(), scaled);
                String Data_Path = tempUri.getPath();
                try
                {
                    ExifInterface exif = new ExifInterface(ImageDecode);
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    Log.v("MainController", "Orient: " + exifOrientation);
                    int rotate = 0;
                    switch (exifOrientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                    }
                    Log.v("MainController", "Rotation: " + rotate);
                    if (rotate != 0)
                    {
                        // Getting width & height of the given image.
                        int w1 = canvas.getWidth();
                        int h1 = canvas.getHeight();
                        // Setting pre rotate
                        Matrix mtx = new Matrix();
                        mtx.preRotate(rotate);
                        // Rotating Bitmap
                        scaled = Bitmap.createBitmap(scaled, 0, 0, w1, h1, mtx, false);
                        canvas.drawBitmap(scaled);
                    }else {
                        canvas.drawBitmap(scaled);
                    }

                }
                catch (IOException e)
                {
                    Log.e("MainController", "Couldn't correct orientation: " + e.toString());
                }
//                imageView=(DrawingView)findViewById(R.id.drawingview);
                mCurrentPhotoPath=ImageDecode;
//                imageView.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
                drawLine.setVisibility(View.VISIBLE);
                drawCircle.setVisibility(View.VISIBLE);
                drawSquare.setVisibility(View.VISIBLE);
                drawArrow.setVisibility(View.VISIBLE);
                drawPen.setVisibility(View.VISIBLE);
                undo.setVisibility(View.VISIBLE);
                redo.setVisibility(View.VISIBLE);
                canvas.setVisibility(View.VISIBLE);
                while (canvas.canUndo()){
                    canvas.historyPointer=canvas.historyPointer-1;
                }
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.PEN);
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
        //getMenuInflater().inflate(R.menu.menu, menu);
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

        if (requestCode == REQUEST_PERMISSIONS || requestCode==REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                if(requestCode==0) {
                    upload();
                }
                else if(requestCode==1){
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
                        /*Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
                        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 4);
                        startActivityForResult(intent, Constants.REQUEST_CODE);*/

                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                            if (Build.VERSION.SDK_INT > 22) {

                                requestPermissions(new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS);

                            }

                        } else {
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

                        } else {
                            camera();

                        }

                    }
                });
        myAlertDialog.show();
    }
    public void onBackPressed(){
        super.onBackPressed();
        edit=sp.edit();
        edit.putString("comments", null);
        edit.putString("selectrecomend", null);
        edit.putString("back", null);
        edit.putBoolean("flag", false);
        edit.commit();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}