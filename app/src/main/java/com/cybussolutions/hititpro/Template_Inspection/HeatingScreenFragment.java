package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
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

    String[] heatingEnergyButtonValues, heatingSystemButtonValues, heatingChimneysButtonValues, heatingDistributionValues, heatingComponentsButtonValues,
            heatingObservationsButtonValues, roFuranceButtonValues,
            roDuctWorkButtonValues, roBoilerButtonValues, roCombustionButtonValues, roChimneysButtonValues,
            roThermostatsButtonValues;

    private static final String HEATING_TABLE = "heating";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_heating_screen, container, false);

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


        heatingEnergyButtonValues = new String[]{"Gas%0", "Electricity%0", "Oil%0","Wood%0","Other%0"};
        heatingSystemButtonValues = new String[]{"Forced Air Furnace%0", "Air Handler for Heat Pump%0", "Hot Water Boiler%0","Steam Boiler%0","Manufacturer%0","Age%0"};
        heatingChimneysButtonValues = new String[]{"Metal Single Wal%0l", "Metal Multi-Wall%0", "Plastic%0","Masonry%0","Not Visible%0"};
        heatingDistributionValues = new String[]{"Ductwork%0", "Radiators%0", "Baseboard Heaters%0", "Other%0"};
        heatingComponentsButtonValues = new String[]{"Humidifier%0", "Condensate Pump%0", "Electronic Air Cleaner%0", "Other%0"};
        heatingObservationsButtonValues = new String[]{"SINGLE FURNACE GENERALLY OK DESCRIPTION%0", "MULTIPLE FURNACES GENERALLY OK DESCRIPTION%0", 
                "NOT OPERATIVE SINGLE FURNACE%0","NOT OPERATIVE IN MULTI FURNACE CONFIGURATION%0"};
        roFuranceButtonValues = new String[]{"Older Single Unit Needs Service%0","Older Multiple Units Need Service%0",
                "Replacement Rusted Burner%0 ","Near End of Life Cycle%0", "Replacement Imminent%0","Seal Openings%0",
                "Flexible Gas Line%0","Sediment Trap Missing/Improper%0","Air Filter Dirty%0","Humidifier Needs Maintenance%0",
                "Condensate Line Dirty%0","Condensate Pump Inoperative%0","Carbon Monoxide Detected%0","Natural Gas Leak Detected%0"};
        roDuctWorkButtonValues = new String[]{"No Ductwork in Basement%0", "No Heat/Cooling Supply%0", "Low Flow at Register%0"
                , "Duct Flow Restricted%0", "Supply Vent Disconnected%0", "Proximity of Furnace Poor%0", "Seal Ductwork%0",
                "Electronic Dampers Suspect%0","Asbestos Tape on Ductwork%0"};
        roBoilerButtonValues = new String[]{"Near End of Life %0", "Older Boiler Unit%0", "Older Boiler Unit%0", "Asbestos Covering%0", "Servicing Needed%0"};
        roCombustionButtonValues = new String[]{"Combustion Air Lacking%0", "Burners Dirty%0", "Burners Rusting%0","Flashback%0", "Back Drafting%0",
                "Poor Flue Connections%0","Flue Slope%0 ", "Flue Slope%0"};
        roChimneysButtonValues = new String[]{"Liner Needed%0", "Obstructed Chimney%0"};
        roThermostatsButtonValues = new String[]{"Inoperative%0", "Loose%0", "Old/Replace%0"};


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Heating Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isHeating_populated","");




        if(!(populate.equals("true")))
        {
            database.prePopulateData("energy_source", heatingEnergyButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("heatingsystemtype", heatingSystemButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("vent_flues_chimneys", heatingChimneysButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("heatdistributionmethods", heatingDistributionValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("othercomponents", heatingComponentsButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", heatingObservationsButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rfurnace", roFuranceButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rsupplyairductwork", roDuctWorkButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("boiler", roBoilerButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("combustion_exhaust", roCombustionButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("furnace_chimneys", roChimneysButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("thermostats", roThermostatsButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);

            // Saving string
            editor.putString("isHeating_populated", "true");
            editor.apply();
        }


        heatingEnergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",heatingEnergyButtonValues);
                intent.putExtra("heading",heatingEnergyButton.getText().toString());
                intent.putExtra("column","energy_source");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        heatingSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",heatingSystemButtonValues);
                intent.putExtra("heading",heatingSystemButton.getText().toString());
                intent.putExtra("column","heatingsystemtype");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingChimneysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",heatingChimneysButtonValues);
                intent.putExtra("heading",heatingChimneysButton.getText().toString());
                intent.putExtra("column","vent_flues_chimneys");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",heatingDistributionValues);
                intent.putExtra("heading",heatingDistribution.getText().toString());
                intent.putExtra("column","heatdistributionmethods");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingComponentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",heatingComponentsButtonValues);
                intent.putExtra("heading",heatingComponentsButton.getText().toString());
                intent.putExtra("column","othercomponents");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        heatingObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",heatingObservationsButtonValues);
                intent.putExtra("heading",heatingObservationsButton.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roFuranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFuranceButtonValues);
                intent.putExtra("heading",roFuranceButton.getText().toString());
                intent.putExtra("column","rfurnace");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roDuctWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDuctWorkButtonValues);
                intent.putExtra("heading",roDuctWorkButton.getText().toString());
                intent.putExtra("column","rsupplyairductwork");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        roBoilerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roBoilerButtonValues);
                intent.putExtra("heading",roBoilerButton.getText().toString());
                intent.putExtra("column","boiler");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roCombustionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roCombustionButtonValues);
                intent.putExtra("heading",roCombustionButton.getText().toString());
                intent.putExtra("column","combustion_exhaust");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roChimneysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roChimneysButtonValues);
                intent.putExtra("heading",roChimneysButton.getText().toString());
                intent.putExtra("column","furnace_chimneys");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roThermostatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roThermostatsButtonValues);
                intent.putExtra("heading",roThermostatsButton.getText().toString());
                intent.putExtra("column","thermostats");
                intent.putExtra("dbTable",HEATING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        return root;
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
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
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
