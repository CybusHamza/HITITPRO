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


public class HeatingScreenFragment extends BaseFragment {

    View root;
    Button next, back;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;

    
    Button heatingEnergyButton, heatingSystemButton, heatingChimneysButton, heatingDistribution, heatingComponentsButton, heatingObservationsButton,
            roFuranceButton, roDuctWorkButton, roBoilerButton, roCombustionButton, roChimneysButton, roThermostatsButton;



    private static final String HEATING_TABLE = "heating";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_heating_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Heating");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HeatingSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new CoolingScreenFragment()).addToBackStack("cooling").commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ExteriorScreenFragment()).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        database= new Database(getActivity());

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        heatingEnergyButton = (Button) root.findViewById(R.id.heating_energy_Button);
        heatingSystemButton = (Button) root.findViewById(R.id.heating_system_Button);
        heatingChimneysButton = (Button) root.findViewById(R.id.heating_chimneys_Button);
        heatingDistribution = (Button) root.findViewById(R.id.heating_distribution_Button);
        heatingComponentsButton = (Button) root.findViewById(R.id.heating_components_Button);
        heatingObservationsButton = (Button) root.findViewById(R.id.heating_observations_Button);

        roFuranceButton = (Button) root.findViewById(R.id.ro_furanc_Button);
        roDuctWorkButton = (Button) root.findViewById(R.id.ro_ductwork_Button);
        roBoilerButton = (Button) root.findViewById(R.id.ro_boiler_Button);
        roCombustionButton = (Button) root.findViewById(R.id.ro_combustion_Button);
        roChimneysButton = (Button) root.findViewById(R.id.ro_chimneys_Button);
        roThermostatsButton = (Button) root.findViewById(R.id.ro_thermostats_Button);





        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Heating");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isHeating_populated","");


        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getHeating();

        }


      

        heatingEnergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.heatingEnergyButtonValues);
                intent.putExtra("heading",heatingEnergyButton.getText().toString());
                intent.putExtra("column","energy_source");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        heatingSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.heatingSystemButtonValues);
                intent.putExtra("heading",heatingSystemButton.getText().toString());
                intent.putExtra("column","heatingsystemtype");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingChimneysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.heatingChimneysButtonValues);
                intent.putExtra("heading",heatingChimneysButton.getText().toString());
                intent.putExtra("column","vent_flues_chimneys");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.heatingDistributionValues);
                intent.putExtra("heading",heatingDistribution.getText().toString());
                intent.putExtra("column","heatdistributionmethods");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingComponentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.heatingComponentsButtonValues);
                intent.putExtra("heading",heatingComponentsButton.getText().toString());
                intent.putExtra("column","othercomponents");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.heatingObservationsButtonValues);
                intent.putExtra("heading",heatingObservationsButton.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roFuranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFuranceButtonValues);
                intent.putExtra("heading",roFuranceButton.getText().toString());
                intent.putExtra("column","rfurnace");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roDuctWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDuctWorkButtonValues);
                intent.putExtra("heading",roDuctWorkButton.getText().toString());
                intent.putExtra("column","rsupplyairductwork");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        roBoilerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roBoilerButtonValues);
                intent.putExtra("heading",roBoilerButton.getText().toString());
                intent.putExtra("column","boiler");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roCombustionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roCombustionButtonValues);
                intent.putExtra("heading",roCombustionButton.getText().toString());
                intent.putExtra("column","combustion_exhaust");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roChimneysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roChimneysButtonValues);
                intent.putExtra("heading",roChimneysButton.getText().toString());
                intent.putExtra("column","furnace_chimneys");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roThermostatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roThermostatsButtonValues);
                intent.putExtra("heading",roThermostatsButton.getText().toString());
                intent.putExtra("column","thermostats");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        return root;
    }

    private void getHeating() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(HEATING_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("energy_source",object.getString("energy_source"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("heatingsystemtype",object.getString("heatingsystemtype"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("vent_flues_chimneys",object.getString("vent_flues_chimneys"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("heatdistributionmethods",object.getString("heatdistributionmethods"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("othercomponents",object.getString("othercomponents"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("observation",object.getString("observation"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rfurnace",object.getString("rfurnace"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("rsupplyairductwork",object.getString("rsupplyairductwork"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("boiler",object.getString("boiler"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("combustion_exhaust",object.getString("combustion_exhaust"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("furnace_chimneys",object.getString("furnace_chimneys"),HEATING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("thermostats",object.getString("thermostats"),HEATING_TABLE,StructureScreensActivity.inspectionID);


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


                Cursor cursor = database.getTable(HEATING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", HEATING_TABLE);


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


    public void HeatingSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Heating ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_HEATING,
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


                Cursor cursor = database.getTable(HEATING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("energy_source", cursor.getString(6));
                    params.put("heatingsystemtype", cursor.getString(7));
                    params.put("vent_flues_chimneys", cursor.getString(8));
                    params.put("heatdistributionmethods", cursor.getString(9));
                    params.put("othercomponents", cursor.getString(10));
                    params.put("observation", cursor.getString(11));
                    params.put("rfurnace", cursor.getString(12));
                    params.put("rsupplyairductwork", cursor.getString(13));
                    params.put("boiler", cursor.getString(14));
                    params.put("combustion_exhaust", cursor.getString(15));
                    params.put("furnace_chimneys", cursor.getString(16));
                    params.put("thermostats", cursor.getString(17));
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



   

}
