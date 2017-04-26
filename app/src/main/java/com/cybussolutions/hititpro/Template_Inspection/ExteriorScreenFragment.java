package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class ExteriorScreenFragment extends BaseFragment {

    View root;
    Button next, back;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;

    private static final String EXTERIROR_TABLE = "exterior";

    Button  exteriorWallCoveringButton, exteriorEavesButton, exteriorDoorsButton, exteriorWindowButton, exteriorDrivewaysButton, exteriorWalksButton,
            exteriorPorchButton, exteriorOverheadButton, exteriorSurfaceButton, exteriorTrainingButton, exteriorFencingButton,
            exteriorObservationButton, roWallsButton, roEavesButton, roDoorsButton, roGarageButton, roPorchesButton,
            roDrivewaysButton, roStepsButton, roDeckButton, roDrainangeButton, roLandscapButton, roRetainingButton,
            roFencingButton;



    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_exterior_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Exterior Screen");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExteriorSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new InteriorScreenFragment()).addToBackStack("interior").commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RoofingScreenFragment()).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        database= new Database(getActivity());

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        exteriorWallCoveringButton = (Button) root.findViewById(R.id.exterior_wall_covering_Button);
        exteriorEavesButton = (Button) root.findViewById(R.id.exterior_eaves_Button);
        exteriorDoorsButton = (Button) root.findViewById(R.id.exterior_doors_Button);
        exteriorWindowButton = (Button) root.findViewById(R.id.exterior_windows_Button);
        exteriorDrivewaysButton = (Button) root.findViewById(R.id.exterior_driveways_Button);
        exteriorWalksButton = (Button) root.findViewById(R.id.exterior_walks_Button);
        exteriorPorchButton = (Button) root.findViewById(R.id.exterior_porch_Button);
        exteriorOverheadButton = (Button) root.findViewById(R.id.exterior_overhead_Button);
        exteriorSurfaceButton = (Button) root.findViewById(R.id.exterior_surface_Button);
        exteriorTrainingButton = (Button) root.findViewById(R.id.exterior_training_Button);
        exteriorFencingButton = (Button) root.findViewById(R.id.exterior_fencing_Button);
        exteriorObservationButton = (Button) root.findViewById(R.id.exterior_observations_Button);
        roWallsButton = (Button) root.findViewById(R.id.ro_exterior_walls_Button);
        roEavesButton = (Button) root.findViewById(R.id.ro_eaves_Button);
        roDoorsButton = (Button) root.findViewById(R.id.ro_doors_Button);
        roGarageButton = (Button) root.findViewById(R.id.ro_garage_Button);
        roPorchesButton = (Button) root.findViewById(R.id.ro_porches_Button);
        roDrivewaysButton = (Button) root.findViewById(R.id.ro_driveways_Button);
        roStepsButton = (Button) root.findViewById(R.id.ro_steps_Button);
        roDeckButton = (Button) root.findViewById(R.id.ro_deck_Button);
        roDrainangeButton = (Button) root.findViewById(R.id.ro_drainage_Button);
        roLandscapButton = (Button) root.findViewById(R.id.ro_landscaping_Button);
        roRetainingButton = (Button) root.findViewById(R.id.ro_retaining_Button);
        roFencingButton = (Button) root.findViewById(R.id.ro_fencing_Button);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exterior Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);




        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exterior Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);



        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getExterior();

        }


        exteriorWallCoveringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorWallCoveringButtonValues);
                intent.putExtra("heading",exteriorWallCoveringButton.getText().toString());
                intent.putExtra("column","wallcovering");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorEavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorEavesButtonValues);
                intent.putExtra("heading",exteriorEavesButton.getText().toString());
                intent.putExtra("column","eaves_soffits_fascia");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorDoorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorDoorsButtonValues);
                intent.putExtra("heading",exteriorDoorsButton.getText().toString());
                intent.putExtra("column","exteriordoors");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorWindowButtonValues);
                intent.putExtra("heading",exteriorWindowButton.getText().toString());
                intent.putExtra("column","windows_doorframes_trim");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorDrivewaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorDrivewaysButtonValues);
                intent.putExtra("heading",exteriorDrivewaysButton.getText().toString());
                intent.putExtra("column","entry_driveways");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorWalksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorWalksButtonValues);
                intent.putExtra("heading",exteriorWalksButton.getText().toString());
                intent.putExtra("column","entrywalk_patios");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorPorchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorPorchButtonValues);
                intent.putExtra("heading",exteriorPorchButton.getText().toString());
                intent.putExtra("column","porch_decks_steps_railings");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorOverheadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorOverheadButtonValues);
                intent.putExtra("heading",exteriorOverheadButton.getText().toString());
                intent.putExtra("column","overheadgaragedoors");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorSurfaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorSurfaceButtonValues);
                intent.putExtra("heading",exteriorSurfaceButton.getText().toString());
                intent.putExtra("column","surfacedrainage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorTrainingButtonValues);
                intent.putExtra("heading",exteriorTrainingButton.getText().toString());
                intent.putExtra("column","retainingwalls");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorFencingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorFencingButtonvalues);
                intent.putExtra("heading",exteriorFencingButton.getText().toString());
                intent.putExtra("column","fencing");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorObservationButtonValues);
                intent.putExtra("heading",exteriorObservationButton.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roWallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roWallsButtonValues);
                intent.putExtra("heading",roWallsButton.getText().toString());
                intent.putExtra("column","rexteriorwalls");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roEavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roEavesButtonValues);
                intent.putExtra("heading",roEavesButton.getText().toString());
                intent.putExtra("column","reaves");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDoorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDoorsButtonValues);
                intent.putExtra("heading",roDoorsButton.getText().toString());
                intent.putExtra("column","rexteriordoors_windows");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roGarageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roGarageButtonValues);
                intent.putExtra("heading",roGarageButton.getText().toString());
                intent.putExtra("column","rgrage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roPorchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roPorchesButtonValues);
                intent.putExtra("heading",roPorchesButton.getText().toString());
                intent.putExtra("column","rporches");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDrivewaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDrivewaysButtonValues);
                intent.putExtra("heading",roDrivewaysButton.getText().toString());
                intent.putExtra("column","rdriveway");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roStepsButtonValues);
                intent.putExtra("heading",roStepsButton.getText().toString());
                intent.putExtra("column","rexteriorsteps_walkways");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDeckButtonValues);
                intent.putExtra("heading",roDeckButton.getText().toString());
                intent.putExtra("column","rdeck");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDrainangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDrainangeButtonValues);
                intent.putExtra("heading",roDrainangeButton.getText().toString());
                intent.putExtra("column","rlotdrainage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roLandscapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roLandscapButtonValues);
                intent.putExtra("heading",roLandscapButton.getText().toString());
                intent.putExtra("column","rlandscaping");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roRetainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roRetainingButtonValues);
                intent.putExtra("heading",roRetainingButton.getText().toString());
                intent.putExtra("column","retainwall");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFencingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFencingButtonValues);
                intent.putExtra("heading",roFencingButton.getText().toString());
                intent.putExtra("column","rfencing");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });



        return root;



    }


    public void ExteriorSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Exterior ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_EXTERIOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

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


                Cursor cursor = database.getTable(EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();



                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("wallcovering", cursor.getString(6));
                    params.put("eaves_soffits_fascia", cursor.getString(7));
                    params.put("exteriordoors", cursor.getString(8));
                    params.put("windows_doorframes_trim", cursor.getString(9));
                    params.put("entry_driveways", cursor.getString(10));
                    params.put("entrywalk_patios", cursor.getString(11));
                    params.put("porch_decks_steps_railings", cursor.getString(12));
                    params.put("overheadgaragedoors", cursor.getString(13));
                    params.put("surfacedrainage", cursor.getString(14));
                    params.put("retainingwalls", cursor.getString(15));
                    params.put("fencing", cursor.getString(16));
                    params.put("observations", cursor.getString(17));
                    params.put("rexteriorwalls", cursor.getString(18));
                    params.put("reaves", cursor.getString(19));
                    params.put("rexteriordoors_windows", cursor.getString(20));
                    params.put("rgrage", cursor.getString(21));
                    params.put("rporches", cursor.getString(22));
                    params.put("rdriveway", cursor.getString(23));
                    params.put("rexteriorsteps_walkways", cursor.getString(24));
                    params.put("rdeck", cursor.getString(25));
                    params.put("rlotdrainage", cursor.getString(26));
                    params.put("rlandscaping", cursor.getString(27));
                    params.put("retainwall", cursor.getString(28));
                    params.put("rfencing", cursor.getString(29));
                }

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



    public void getExterior() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(EXTERIROR_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("wallcovering",object.getString("wallcovering"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("eaves_soffits_fascia",object.getString("eaves_soffits_fascia"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("exteriordoors",object.getString("exteriordoors"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("windows_doorframes_trim",object.getString("windows_doorframes_trim"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("entry_driveways",object.getString("entry_driveways"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("entrywalk_patios",object.getString("entrywalk_patios"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("porch_decks_steps_railings",object.getString("porch_decks_steps_railings"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("overheadgaragedoors",object.getString("overheadgaragedoors"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("surfacedrainage",object.getString("surfacedrainage"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("retainingwalls",object.getString("retainingwalls"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("fencing",object.getString("fencing"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("observations",object.getString("observations"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rexteriorwalls",object.getString("rexteriorwalls"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("reaves",object.getString("reaves"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rexteriordoors_windows",object.getString("rexteriordoors_windows"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rgrage",object.getString("rgrage"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rporches",object.getString("rporches"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rdriveway",object.getString("rdriveway"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rexteriorsteps_walkways",object.getString("rexteriorsteps_walkways"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rdeck",object.getString("rdeck"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rlotdrainage",object.getString("rlotdrainage"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rlandscaping",object.getString("rlandscaping"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("retainwall",object.getString("retainwall"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rfencing",object.getString("rfencing"),EXTERIROR_TABLE,StructureScreensActivity.inspectionID);


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


                Cursor cursor = database.getTable(EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", EXTERIROR_TABLE);


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
