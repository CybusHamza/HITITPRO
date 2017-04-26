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


public class CoolingScreenFragment extends BaseFragment {

    View root;
    Button next, back;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;

    Button coolingEnergyButton, coolingSystemButton, coolingEquipmentButton, coolingComponentsButton, coolingObservationsButton,
            roCentralButton, roPumpButton, roEvaporatorButton, roFansButton;

    String[] coolingEnergyButtonValues, coolingSystemButtonValues, coolingEquipmentButtonValues, coolingComponentsButtonValues,
            coolingObservationsButtonValues, roCentralButtonValues,
            roPumpButtonValues, roEvaporatorButtonValues, roFansButtonValues;

    private static final String COOLING_TABLE = "cooling";


    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cooling_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Cooling Screen");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoolingSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ElectricalScreenFragment()).addToBackStack("electrical").commit();
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

        coolingEnergyButton = (Button) root.findViewById(R.id.cooling_energy_Button);
        coolingSystemButton = (Button) root.findViewById(R.id.cooling_system_Button);
        coolingEquipmentButton = (Button) root.findViewById(R.id.cooling_equipment_Button);
        coolingComponentsButton = (Button) root.findViewById(R.id.cooling_components_Button);
        coolingObservationsButton = (Button) root.findViewById(R.id.cooling_observation_Button);

        roCentralButton = (Button) root.findViewById(R.id.ro_central_Button);
        roPumpButton = (Button) root.findViewById(R.id.ro_pump_Button);
        roEvaporatorButton = (Button) root.findViewById(R.id.ro_evaporator_Button);
        roFansButton = (Button) root.findViewById(R.id.ro_fans_Button);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cooling Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        coolingEnergyButtonValues = new String[]{"Gas%0", "Electricity%0", "Other%0"};
        coolingSystemButtonValues = new String[]{"Air Cooled Central Air%0", "Air Source Heat Pump System%0", "Water Source Heat Pump System%0",
                "Ground Source Heat Pump System%0", "Evaporator Cooler%0", "Capacity in Tons ??? (1 Ton Serves Approx 500-600 sf)%0",
                "Manufacturer / Age ???%0", "Serial Number ???%0"};
        coolingEquipmentButtonValues = new String[]{"Present At ???%0"};
        coolingComponentsButtonValues = new String[]{"Whole House Fan%0"};
        coolingObservationsButtonValues = new String[]{"SINGLE A/C UNIT GENERALLY OK DESCRIPTION%0", "MULTIPLE A/C UNITS GENERALLY OK DESCRIPTION%0",
                "NOT COOLING PROPERLY SINGLE UNIT%0", "NOT COOLING PROPERLY MULTI A/C CONFIGURATION%0"};

        roCentralButtonValues = new String[]{"Inoperative System%0", "Old System%0", "Very Old System%0", "Undersized%0", "Not Cooling Adequately%0",
                "Temperature Drop Excessive%0", "Clean Outdoor Unit%0", "Insulation Damage to Refrigerant Lines%0",
                "Units Out of Level%0", "Outdoor Unit Fin Damage%0", "Outdoor Unit Noisy", "Cut Back Vegetation%0",
                "Re-route Condensate Line%0", "Negative Slope on Condensate Line%0"};
        roPumpButtonValues = new String[]{"Inoperative System%0", "Old System%0", "Very Old System%0", "Undersized%0",
                "Not Cooling Adequately%0", "Temperature Drop Excessive%0", "Clean Outdoor Unit%0",
                "Insulation Damage to Refrigerant Lines%0", "Units Out of Level%0", "Outdoor Unit Fin Damage%0",
                "Outdoor Unit Noisy%0", "Cut Back Vegetation%0", "Re-route Condensate Line%0",
                "Negative Slope on Condensate Line%0"};
        roEvaporatorButtonValues = new String[]{"Inoperative%0", "Older System%0", "Lacking Maintenance%0",
                "Noisy%0", "Spray Nozzle Restricted%0", "Float Valve Suspect%0", "Pump Suspect%0",
                "Excess Humidity in House%0"};
        roFansButtonValues = new String[]{"Inoperative%0", "Removal Recommended%0"};

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cooling Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isColling_populated","");



        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getCooling();

        }
        else
        {

            if(!(populate.equals("true")))
            {
                database.prePopulateData("energysource", coolingEnergyButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("centralsystemtype", coolingSystemButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("throughwallequipment", coolingEquipmentButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("othercomponents", coolingComponentsButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("heatobservation", coolingObservationsButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recomndcentralaircondition", roCentralButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("heatpumps", roPumpButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("evaporator", roEvaporatorButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("housefans", roFansButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);

                // Saving string
                editor.putString("isColling_populated", "true");
                editor.apply();
            }
        }
        
        coolingEnergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",coolingEnergyButtonValues);
                intent.putExtra("heading",coolingEnergyButton.getText().toString());
                intent.putExtra("column","energysource");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        coolingSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",coolingSystemButtonValues);
                intent.putExtra("heading",coolingSystemButton.getText().toString());
                intent.putExtra("column","centralsystemtype");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        coolingEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",coolingEquipmentButtonValues);
                intent.putExtra("heading",coolingEquipmentButton.getText().toString());
                intent.putExtra("column","throughwallequipment");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        coolingComponentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",coolingComponentsButtonValues);
                intent.putExtra("heading",coolingComponentsButton.getText().toString());
                intent.putExtra("column","othercomponents");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        coolingObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",coolingObservationsButtonValues);
                intent.putExtra("heading",coolingObservationsButton.getText().toString());
                intent.putExtra("column","heatobservation");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roCentralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roCentralButtonValues);
                intent.putExtra("heading",roCentralButton.getText().toString());
                intent.putExtra("column","recomndcentralaircondition");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roPumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roPumpButtonValues);
                intent.putExtra("heading",roPumpButton.getText().toString());
                intent.putExtra("column","heatpumps");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roEvaporatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roEvaporatorButtonValues);
                intent.putExtra("heading",roEvaporatorButton.getText().toString());
                intent.putExtra("column","evaporator");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roFansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFansButtonValues);
                intent.putExtra("heading",roFansButton.getText().toString());
                intent.putExtra("column","housefans");
                intent.putExtra("dbTable",COOLING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        return root;
    }

    private void getCooling() {


        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(COOLING_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("energysource",object.getString("energysource"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("centralsystemtype",object.getString("centralsystemtype"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("throughwallequipment",object.getString("throughwallequipment"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("othercomponents",object.getString("othercomponents"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("heatobservation",object.getString("heatobservation"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("recomndcentralaircondition",object.getString("recomndcentralaircondition"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("heatpumps",object.getString("heatpumps"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("evaporator",object.getString("evaporator"),COOLING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("housefans",object.getString("housefans"),COOLING_TABLE,StructureScreensActivity.inspectionID);


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


                Cursor cursor = database.getTable(COOLING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", COOLING_TABLE);


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


    public void CoolingSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Cooling ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_COOLING,
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


                Cursor cursor = database.getTable(COOLING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("energysource", cursor.getString(6));
                    params.put("centralsystemtype", cursor.getString(7));
                    params.put("throughwallequipment", cursor.getString(8));
                    params.put("heatdistributionmethods", cursor.getString(9));
                    params.put("othercomponents", cursor.getString(10));
                    params.put("heatobservation", cursor.getString(11));
                    params.put("recomndcentralaircondition", cursor.getString(12));
                    params.put("heatpumps", cursor.getString(13));
                    params.put("evaporator", cursor.getString(14));
                    params.put("housefans", cursor.getString(15));

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

 

  

}
