package com.cybussolutions.hititpro.Template_Inspection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.cybussolutions.hititpro.Activities.Detailed_Activity_All_Screens;
import com.cybussolutions.hititpro.Activities.Detailed_Activity_Structure_Screens;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Fragments.BaseFragment;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;


public class RoofingScreenFragment extends BaseFragment {

    View root;
    Button next, back,save;
    Database database;
    TextView title;
    private static final String ROOFING_TABLE = "roofing";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    AlertDialog b;
    Button roofCoveringButton, roofFlashingButton, chimneysButton, roofDrainageButton, skyLightsButton, methodInspectionButton,
            roofingObservationsButton, roSloppedButton, roFlatButton, roFlashingButton, roChimneyButton, roGutterDownspoutsButton;


    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_roofing_screen, container, false);

        database= new Database(getActivity());

        try {
            database = database.open();
            ///////////set title of main screens/////////////////
            sp=getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
            edit=sp.edit();
            edit.putString("main_screen","Roofing");
            edit.apply();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        save = (Button) root.findViewById(R.id.save);
        title = (TextView) root.findViewById(R.id.title);

        if(!(StructureScreensActivity.is_notemplate.equals("true")))
        {
            save.setVisibility(View.GONE);
            title.setText("    Page 2 of Page 11");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StructureScreensActivity.is_saved)
                {
                    addDetail();
                }
                else
                {

                    Toast.makeText(getContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StructureSync();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new StructureScreenFragment()).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        roofCoveringButton = (Button) root.findViewById(R.id.roof_covering_Button);
        roofFlashingButton = (Button) root.findViewById(R.id.roof_flashing_Button);
        chimneysButton = (Button) root.findViewById(R.id.chimneys_Button);
        roofDrainageButton = (Button) root.findViewById(R.id.roof_drainage_Button);
        skyLightsButton = (Button) root.findViewById(R.id.skylights_Button);
        methodInspectionButton = (Button) root.findViewById(R.id.method_inspection_Button);
        roofingObservationsButton = (Button) root.findViewById(R.id.roofing_observations_Button);
        roSloppedButton = (Button) root.findViewById(R.id.ro_slopped_Button);
        roFlatButton = (Button) root.findViewById(R.id.ro_flat_Button);
        roFlashingButton = (Button) root.findViewById(R.id.ro_flashing_Button);
        roChimneyButton = (Button) root.findViewById(R.id.ro_chimneys_Button);
        roGutterDownspoutsButton = (Button) root.findViewById(R.id.ro_gutters_downspouts_Button);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Roofing");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isRoofing_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getRoofing();

        }

        
      

        roofCoveringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roofCoveringButtonValues);
                intent.putExtra("heading",roofCoveringButton.getText().toString());
                intent.putExtra("column","roofcovering");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roofFlashingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roofFlashingButtonValues);
                intent.putExtra("heading",roofFlashingButton.getText().toString());
                intent.putExtra("column","roofflashing");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        chimneysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.chimneysButtonValues);
                intent.putExtra("heading",chimneysButton.getText().toString());
                intent.putExtra("column","chimneys");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roofDrainageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roofDrainageButtonValues);
                intent.putExtra("heading",roofDrainageButton.getText().toString());
                intent.putExtra("column","roofdrainage");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        skyLightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.skyLightsButtonValues);
                intent.putExtra("heading",skyLightsButton.getText().toString());
                intent.putExtra("column","skylights");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);

            }
        });
        methodInspectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.methodInspectionButtonValues);
                intent.putExtra("heading",methodInspectionButton.getText().toString());
                intent.putExtra("column","methodofinspection");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roofingObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",false);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roofingObservationsButtonValues);
                intent.putExtra("heading",roofingObservationsButton.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roSloppedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roSloppedButtonValues);
                intent.putExtra("heading",roSloppedButton.getText().toString());
                intent.putExtra("column","recommendslopedroofing");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFlatButtonValues);
                intent.putExtra("heading",roFlatButton.getText().toString());
                intent.putExtra("column","flatroofing");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roFlashingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFlashingButtonValues);
                intent.putExtra("heading",roFlashingButton.getText().toString());
                intent.putExtra("column","flashing");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roChimneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roChimneyButtonValues);
                intent.putExtra("heading",roChimneyButton.getText().toString());
                intent.putExtra("column","recommendchimneys");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roGutterDownspoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roGutterDownspoutsButtonValues);
                intent.putExtra("heading",roGutterDownspoutsButton.getText().toString());
                intent.putExtra("column","guttersdownspouts");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });



        return root;

    }





    public void StructureSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Roof ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_ROOf,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ExteriorScreenFragment()).addToBackStack("exterior").commit();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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


                Cursor cursor = database.getTable(ROOFING_TABLE,StructureScreensActivity.template_id);
                cursor.moveToFirst();



                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.template_id);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");
                if(cursor != null) {
                    params.put("roofcovering", cursor.getString(6));
                    params.put("roofflashing", cursor.getString(7));
                    params.put("chimneys", cursor.getString(8));
                    params.put("roofdrainage", cursor.getString(9));
                    params.put("skylights", cursor.getString(10));
                    params.put("methodofinspection", cursor.getString(11));
                    params.put("observations", cursor.getString(12));
                    params.put("recommendslopedroofing", cursor.getString(13));
                    params.put("flatroofing", cursor.getString(14));
                    params.put("flashing", cursor.getString(15));
                    params.put("recommendchimneys", cursor.getString(16));
                    params.put("guttersdownspouts", cursor.getString(17));
                }

                int isAnyChecked = 0;
                for(int count=6;count<=17;count++)
                {

                    String splitter = "\\^";
                    String[] insertArray = cursor.getString(count).split(splitter);

                    for (String anInsertArray : insertArray) {
                        String split = "%";

                        String[] row = anInsertArray.split(split);

                        if (row[1].equals("1")) {
                            isAnyChecked++;
                            break;
                        }
                    }

                }

                int total = 12 - isAnyChecked;
                params.put("empty_fields", total+"");

                return params;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    public void getRoofing() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(ROOFING_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("roofcovering",object.getString("roofcovering"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("roofflashing",object.getString("roofflashing"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("chimneys",object.getString("chimneys"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("roofdrainage",object.getString("roofdrainage"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("skylights",object.getString("skylights"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("methodofinspection",object.getString("methodofinspection"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("observations",object.getString("observations"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("recommendslopedroofing",object.getString("recommendslopedroofing"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("flatroofing",object.getString("flatroofing"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("flashing",object.getString("flashing"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("recommendchimneys",object.getString("recommendchimneys"),ROOFING_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("guttersdownspouts",object.getString("guttersdownspouts"),ROOFING_TABLE,StructureScreensActivity.template_id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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


                Cursor cursor = database.getTable(ROOFING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", ROOFING_TABLE);


                return params;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    void addDetail() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.savetemp, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        // intializing variables
        final EditText tmpName = (EditText) dialogView.findViewById(R.id.tmpName);
        final Button save = (Button) dialogView.findViewById(R.id.Savenotemp);
        final CheckBox isdefault = (CheckBox) dialogView.findViewById(R.id.checkDefault);


        isdefault.setVisibility(View.GONE);
        b = dialogBuilder.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tmpName.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Template name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(isdefault.isChecked())
                    {
                        saveNoTemp(tmpName.getText().toString(),true);
                    }
                    else
                    {
                        saveNoTemp(tmpName.getText().toString(),false);
                    }

                }

            }
        });

        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        b.show();



    }

    public void saveNoTemp(final String txt, final boolean check) {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Saving Template ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SAVE_NOTEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        StructureScreensActivity.is_saved=true;

                        String resp[];

                        resp = response.split("%");


                        StructureScreensActivity.inspectionID = resp[1];
                        StructureScreensActivity.template_id = resp[0];



                        database.updateIds();





                        sp=getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                        edit=sp.edit();
                        edit.putBoolean("StructureScreenFragment",true);
                        edit.apply();
                        Toast.makeText(getContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String  user = pref.getString("user_id","");

                params.put("name", txt);
                params.put("isDefault", String.valueOf(check));
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("added_by", user);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }


}
