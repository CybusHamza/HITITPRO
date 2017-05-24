package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
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

public class PlumbingScreenFragment extends BaseFragment {

    View root;
    Button next, back;

    Button water_supply_source,service_pipe_to_house,main_water_valve_location,interior_supply_piping,
            waste_system,dwv_piping,water_heater,fuel_storage_distribution,fuel_shut_off_valves,other_components_plumbing,
            plumbing_observations,water_heater_plimbing,gas_piping,supply_piping,dwv_piping_observation,fixtures,sump_pump
            ,waste_ejector_pump;


    private static final String PLUMBING_TABLE = "plumbing";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_plumbing_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Plumbing");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlumbingSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new InteriorScreenFragment()).addToBackStack("interior").commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        database= new Database(getActivity());

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Plumbing");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        water_supply_source= (Button) root.findViewById(R.id.water_supply_source);
        service_pipe_to_house= (Button) root.findViewById(R.id.service_pipe_to_house);
        main_water_valve_location= (Button) root.findViewById(R.id.main_water_valve_location);
        interior_supply_piping= (Button) root.findViewById(R.id.interior_supply_piping);
        waste_system= (Button) root.findViewById(R.id.waste_system);
        dwv_piping= (Button) root.findViewById(R.id.dwv_piping);
        water_heater_plimbing= (Button) root.findViewById(R.id.water_heater_plimbing);
        water_heater= (Button) root.findViewById(R.id.water_heater);
        fuel_storage_distribution= (Button) root.findViewById(R.id.fuel_storage_distribution);
        fuel_shut_off_valves= (Button) root.findViewById(R.id.fuel_shut_off_valves);
        other_components_plumbing= (Button) root.findViewById(R.id.other_components_plumbing);
        plumbing_observations= (Button) root.findViewById(R.id.plumbing_observations);
        gas_piping= (Button) root.findViewById(R.id.gas_piping);
        supply_piping= (Button) root.findViewById(R.id.supply_piping);
        dwv_piping_observation= (Button) root.findViewById(R.id.dwv_piping_observation);
        fixtures= (Button) root.findViewById(R.id.fixtures);
        sump_pump= (Button) root.findViewById(R.id.sump_pump);
        waste_ejector_pump= (Button) root.findViewById(R.id.waste_ejector_pump);







        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getPlumbing();

        }



        water_supply_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.water_supply_sourceValues);
                intent.putExtra("heading",water_supply_source.getText().toString());
                intent.putExtra("column","watersupplysource");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        service_pipe_to_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.service_pipe_to_houseValues);
                intent.putExtra("heading",service_pipe_to_house.getText().toString());
                intent.putExtra("column","servicepipe");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        main_water_valve_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.main_water_valve_locationValues);
                intent.putExtra("heading",main_water_valve_location.getText().toString());
                intent.putExtra("column","mainwatervalvelocation");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        interior_supply_piping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.interior_supply_pipingValues);
                intent.putExtra("heading",interior_supply_piping.getText().toString());
                intent.putExtra("column","interiorsupply");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        waste_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.waste_systemValues);
                intent.putExtra("heading",waste_system.getText().toString());
                intent.putExtra("column","wastesystem");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        dwv_piping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.dwv_pipingValues);
                intent.putExtra("heading",dwv_piping.getText().toString());
                intent.putExtra("column","strudwvpiping");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        water_heater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.water_heaterValues);
                intent.putExtra("heading",water_heater.getText().toString());
                intent.putExtra("column","waterheater");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        fuel_storage_distribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.fuel_storage_distributionValues);
                intent.putExtra("heading",fuel_storage_distribution.getText().toString());
                intent.putExtra("column","fuelshortage_distribution");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        fuel_shut_off_valves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.fuel_shut_off_valvesValues);
                intent.putExtra("heading",fuel_shut_off_valves.getText().toString());
                intent.putExtra("column","fuelshutoffvalves");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        other_components_plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.other_components_plumbingValues);
                intent.putExtra("heading",other_components_plumbing.getText().toString());
                intent.putExtra("column","othercomponents");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        plumbing_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items", StructureScreensActivity.plumbing_observationsValues);
                intent.putExtra("heading",plumbing_observations.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        water_heater_plimbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.water_heater_plimbingValues);
                intent.putExtra("heading",water_heater_plimbing.getText().toString());
                intent.putExtra("column","rwaterheater");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        gas_piping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.gas_pipingValues);
                intent.putExtra("heading",gas_piping.getText().toString());
                intent.putExtra("column","gaspiping");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        supply_piping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.supply_pipingValues);
                intent.putExtra("heading",supply_piping.getText().toString());
                intent.putExtra("column","rsupplypiping");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        dwv_piping_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.dwv_piping_observationValues);
                intent.putExtra("heading",dwv_piping_observation.getText().toString());
                intent.putExtra("column","dwvpiping");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        fixtures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.fixturesValues);
                intent.putExtra("heading",fixtures.getText().toString());
                intent.putExtra("column","fixtures");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        sump_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.sump_pumpValues);
                intent.putExtra("heading",sump_pump.getText().toString());
                intent.putExtra("column","sumppump");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        waste_ejector_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items", StructureScreensActivity.waste_ejector_pumpValues);
                intent.putExtra("heading",waste_ejector_pump.getText().toString());
                intent.putExtra("column","wasteejectorpump");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });



        return root;
    }

    private void getPlumbing() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(PLUMBING_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("watersupplysource", object.getString("watersupplysource"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("servicepipe", object.getString("servicepipe"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("mainwatervalvelocation", object.getString("mainwatervalvelocation"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("interiorsupply", object.getString("interiorsupply"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("wastesystem", object.getString("wastesystem"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("strudwvpiping", object.getString("strudwvpiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("waterheater", object.getString("waterheater"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fuelshortage_distribution", object.getString("fuelshortage_distribution"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fuelshutoffvalves", object.getString("fuelshutoffvalves"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("othercomponents", object.getString("othercomponents"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("observation", object.getString("observation"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rwaterheater", object.getString("rwaterheater"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("gaspiping", object.getString("gaspiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rsupplypiping", object.getString("rsupplypiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("dwvpiping", object.getString("dwvpiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fixtures", object.getString("fixtures"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("wasteejectorpump", object.getString("wasteejectorpump"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("sumppump", object.getString("sumppump"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);

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


                Cursor cursor = database.getTable(PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("tempid", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("temp_name", PLUMBING_TABLE);


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

    public void PlumbingSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Plumbing ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_PLUMBING,
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


                Cursor cursor = database.getTable(PLUMBING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");

                if(cursor != null) {
                    params.put("watersupplysource", cursor.getString(6));
                    params.put("servicepipe", cursor.getString(7));
                    params.put("mainwatervalvelocation", cursor.getString(8));
                    params.put("interiorsupply", cursor.getString(9));
                    params.put("wastesystem", cursor.getString(10));
                    params.put("strudwvpiping", cursor.getString(11));
                    params.put("waterheater", cursor.getString(12));
                    params.put("fuelshortage_distribution", cursor.getString(13));
                    params.put("fuelshutoffvalves", cursor.getString(14));
                    params.put("othercomponents", cursor.getString(15));
                    params.put("observation", cursor.getString(16));
                    params.put("rwaterheater", cursor.getString(17));
                    params.put("gaspiping", cursor.getString(18));
                    params.put("rsupplypiping", cursor.getString(19));
                    params.put("dwvpiping", cursor.getString(20));
                    params.put("fixtures", cursor.getString(21));
                    params.put("sumppump", cursor.getString(22));
                    params.put("wasteejectorpump", cursor.getString(23));

                }
                int isAnyChecked = 0;
                for(int count=6;count<=23;count++)
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

                int total = 18 - isAnyChecked;
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
