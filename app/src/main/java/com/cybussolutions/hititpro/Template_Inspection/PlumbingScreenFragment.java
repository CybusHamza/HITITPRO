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
            ,waste_ejector_pump,water_heater_plumbing,gas_piping_plumbing,supply_plumbing,dwv_plumbing,fixtures_observation,sump_pump_plumbing,waste_ejector_pump_ro;

    String[] water_supply_sourceValues,service_pipe_to_houseValues,main_water_valve_locationValues,interior_supply_pipingValues,
            waste_systemValues,water_heaterValues,dwv_pipingValues,fuel_storage_distributionValues,fuel_shut_off_valvesValues,other_components_plumbingValues,
            plumbing_observationsValues,water_heater_plimbingValues,gas_pipingValues,supply_pipingValues,dwv_piping_observationValues,fixturesValues,sump_pumpValues
            ,waste_ejector_pumpValues,water_heater_plumbingValues,gas_piping_plumbingValues,supply_plumbingValues,dwv_plumbingValues,fixtures_observationValues,sump_pump_plumbingValues,waste_ejector_pump_roValues;

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
        edit.putString("main_screen","Plumbing Screen");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlumbingSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AppliancesScreenFragment()).addToBackStack("appliances").commit();
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


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Plumbing Screen");
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
        water_heater_plumbing= (Button) root.findViewById(R.id.water_heater_plumbing);
        gas_piping_plumbing= (Button) root.findViewById(R.id.gas_piping_plumbing);
        supply_plumbing= (Button) root.findViewById(R.id.supply_plumbing);
        dwv_plumbing= (Button) root.findViewById(R.id.dwv_plumbing);
        fixtures_observation= (Button) root.findViewById(R.id.fixtures_observation);
        sump_pump_plumbing= (Button) root.findViewById(R.id.sump_pump_plumbing);
        waste_ejector_pump_ro= (Button) root.findViewById(R.id.waste_ejector_pump_ro);


        water_supply_sourceValues =  new String[]{"Public%0","Private%0","Unknown%0"};
        service_pipe_to_houseValues =  new String[]{"Copper%0","Plastic%0","Steel%0","Lead%0","Not Visible%0"};
        main_water_valve_locationValues =  new String[]{"Front Wall Basement%0","Rear Wall of Basement%0","Crawl Space%0","Beside Water Heater%0","Location ???%0","Not Found%0"};
        interior_supply_pipingValues =  new String[]{"Copper%0","Plastic%0","Steel%0","Lead%0"};
        waste_systemValues =  new String[]{"Public Sewer%0","Private Sewer%0","Unknown%0"};
        dwv_pipingValues =  new String[]{"Plastic%0","Cast Iron%0","Steel%0","Copper%0","Lead%0"};
        water_heaterValues =  new String[]{"Gas%0","Electric%0","Tank Capacity%0","Age%0","Manufacturer%0","Tankless Gas%0",
                "Tankless Electric%0"};
        fuel_storage_distributionValues =  new String[]{"Natural Gas%0","Propane%0","Liquid Petrolium%0", "None; All Electric%0"};
        fuel_shut_off_valvesValues =  new String[]{"Natural Gas Main Side of Home%0","Natural Gas Main %0",
                "Propane Shut-Off %0","LP Shut-Off%0","Heating Oil Tank %0"};
        other_components_plumbingValues =  new String[]{"Pressure Regulator on Main Line%0","Sump Pump%0",
                "Sewer Ejector Pump%0","Hot Water Circulator%0","Backflow Devices on Hose Bibs%0","Sprinkler  System%0"};
        plumbing_observationsValues =  new String[]{"OVERALL GOOD CONDITION%0","OVERALL GOOD WITH OLDER PLUMBING FIXTURES%0","NUMEROUS REPAIRS NEEDED%0"};
        water_heater_plimbingValues =  new String[]{"Very Old Unit%0","Old Unit%0","Leakage Evident%0", "Thermal Expansion Device Needed%0",
                "Sediment Trap%0","Capacity Questionable%0","Burner Dirty/Rust%0","No Cold Water Shut-Off%0", "No TPR Valve%0"
                ,"Leaking TPR Valve%0","Wiring Connection Poor%0","Exhaust Spillage %0","Missing Burner Cover%0",
                "Lack of Combustion Air%0","Improve Draft Diverter%0"};
        gas_pipingValues =  new String[]{"Gas Leak%0","Cap Gas Lines %0","Main Shut-Off  Not Found%0","Secure Gas Line%0","Gas Line Corrosion%0",
                "Drip Leg Needed%0","Shut-Off  Valve Needed%0","Dryer Gas Line Cap Needed%0","Support Lacking%0"};
        supply_pipingValues =  new String[]{"Pressure Regulator Needed%0","Label Water Shut-Offs%0","Leak%0"
                ,"Polybutelyne%0","Freezing Potential%0","Support Lacking%0","Supply Handle Missing%0","Pipe Hammer%0"};
        dwv_piping_observationValues =  new String[]{"Leaks%0","Flexible Piping%0","Overflow Pan for Washer%0",
                "Overflow Pan for WH%0","Trap Leak%0","S Trap%0","Insufficient Slope%0","Older Piping %0","Support Lacking%0",
                "No Main Clean-Out Found%0","Septic System Disclosure%0","Septic System Disclaimer%0","Odor%0","Vent Stack Height Inadequate%0"};
        fixturesValues =  new String[]{"Older%0","Insufficient Water Flow%0","Hot/Cold Reversed%0","Faucet Leaky%0",
                "Previous Leaks Under Sink%0","Under Sink Openings%0","Sink Drains Slow%0","Sink Drain Damaged%0",
                "Sink Drain Missing%0","Loose Toilet Only%0","Loose Toilet Wax Ring%0","Toilet Lid Cracked%0","Toilet Slow Flush%0",
                "Toilet Slow Flush%0","Toilet Old%0","Shower Head Leak%0","Hot Shower/Cold Shower%0", "Seal Shower Fixtures%0",
                "Backsplash Caulking Top%0","Backslash Caulking Bottom%0","Shower Stall Tile/Grout%0","Shower Stall Tile Damage%0",
                "Shower Stall Drains Slow%0","Bathtub Tile/Grout%0","Bathtub Enclosure Caulk%0","Bathtub Floor Damage%0",
                "Bathtub No Drain Plug%0","Bathtub Drains Slow%0","Jetted Tub Inoperative%0","Jetted Tub Noisy%0","Jetted Tub No Motor Access%0"
                ,"Jetted Tub Lacks GFCI%0","Laundry Tub Loose%0","Exhaust Fan Needed%0","Exhaust Fan Inoperative%0","Hose Bib Anti-Siphon Needed%0"};
        sump_pumpValues =  new String[]{"Inoperative%0","Old%0 ","Discharge Line Suspect%0","Cover Needed%0"};
        waste_ejector_pumpValues =  new String[]{"Inoperative%0","Seal Openings%0","No Vent Visible%0"};
        water_heater_plumbingValues =  new String[]{"Very Old Unit%0","Older Unit%0","Leakage Evident%0","Thermal Expansion Device Needed%0",
                "Sediment Trap%0","Capacity Questionable%0","Burner Dirty/Rust%0","No Cold Water Shut-Off%0","No TPR Valve%0",
                "Leaking TPR Valve%0","TPR Discharge Improvement%0","Wiring Connection Poor%0","Missing Burner Cover%0"
                ,"Exhaust Spillage%0","Lack of Combustion Air%0","Improve Draft Diverter%0","Vent Pipe Clearance%0",
                "Protect WH from Damage%0","Older WH Garage Floor Clearance Needed%0"};
        gas_piping_plumbingValues =  new String[]{"Gas Leak%0","Cap Gas Lines%0","Main Shut-Off Not Found%0","Secure Gas Line%0",
                "Gas Line Corrosion%0", "Drip Leg Needed%0","Shut-Off Valve Needed%0","Dryer Gas Line Cap Needed%0","Support Lacking%0",
                "Gas Meter Locked%0"};
        supply_plumbingValues =  new String[]{"Pressure Regulator Needed%0","Label Water Shut-Offs%0","Leak%0",
                "Polybutylene%0","Freezing Potential%0","Support Lacking%0","Supply Handle Missing%0","Pipe Hammer%0"};
        dwv_plumbingValues =  new String[]{"Leaks%0","Flexible Piping%0","Overflow Pan for Washing Machine%0",
                "Overflow Pan for WH%0","Trap Leak%0","S-Trap%0", "Insufficient Slope%0","Support Lacking%0","Older Piping%0",
                "No Main Clean-Out Found%0","Septic System Warning/Disclosure%0", "Septic System Disclaimer%0",
                "Odor%0","Vent Stack Height Insufficient%0"};
        fixtures_observationValues =  new String[]{"Older%0","Insufficient Water Flow%0","Hot/Cold Reversed%0","Faucet Leaking%0","Previous Leaks Under Sinks%0"
                ,"Under Sink Openings%0","Sink Drains Slow%0", "Sink Drain Damaged%0","Sink Drain Missing%0","Loose Toilet Only%0"
                ,"Loose Toilet Wax Ring%0","Toilet Lid Cracked%0","Toilet Slow Flush%0","Toilet Runs%0",
                "Toilet Old%0", "Shower Head Leak%0","Hot Shower/Cold Shower%0","Seal Shower Fixtures%0","Backsplash Top Caulking%0"
                ,"Backsplash Bottom Caulking%0","Shower Stall Tile Grout/Caulk%0","Shower Stall Tile Damage%0","Shower Stall Drains Slow%0",
                "Bathtub Tile Grout/Caulk%0","Bathtub Floor Damage %0","Bathtub Enclosure Caulk%0","Bathtub Drains Slow%0",
                "Bathtub No Drain Plug%0","Jetted Tub Inoperative%0","Jetted Tub Noisy%0","Jetted Tub No Motor Access%0",
                "Jetted Tub Lacks GFCI%0","Waste Ejector Pump%0","Laundry Tub Loose%0","Exhaust Fan Needed%0","Exhaust Fan Inoperative%0","Hose Bib Anti-Siphon Needed%0 "
        };
        sump_pump_plumbingValues =  new String[]{"Inoperative%0","Old%0","Discharge Line Suspect%0","Cover Needed%0"};
        waste_ejector_pump_roValues =  new String[]{"Inoperative%0","Unsealed Openings%0","No Vent%0"};


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isInsulation_populated","");



        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getPlumbing();

        }
        else
        {
            if(!(populate.equals("true")))
            {
                database.prePopulateData("watersupplysource", water_supply_sourceValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("servicepipe", service_pipe_to_houseValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("mainwatervalvelocation", main_water_valve_locationValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("interiorsupply", interior_supply_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("wastesystem", waste_systemValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("waterheater", water_heaterValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("rfurnace", dwv_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("fuelshortage_distribution", fuel_storage_distributionValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("fuelshutoffvalves", fuel_shut_off_valvesValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("othercomponents", other_components_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("observation", plumbing_observationsValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("rwaterheater", water_heater_plimbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("gaspiping", gas_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("rsupplypiping", supply_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("dwvpiping", dwv_piping_observationValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("fixtures", fixturesValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("sumppump", sump_pumpValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("wasteejectorpump", waste_ejector_pumpValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("gas_piping_plumbing", gas_piping_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("supply_plumbing", supply_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("dwv_plumbing", dwv_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("fixtures_observation", fixtures_observationValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("sump_pump_plumbing", sump_pump_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("waste_ejector_pump_ro", waste_ejector_pump_roValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);

                // Saving string
                editor.putString("isInsulation_populated", "true");
                editor.apply();
            }

        }


        water_supply_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",water_supply_sourceValues);
                intent.putExtra("heading",water_supply_source.getText().toString());
                intent.putExtra("column","watersupplysource");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",service_pipe_to_houseValues);
                intent.putExtra("heading",service_pipe_to_house.getText().toString());
                intent.putExtra("column","servicepipe");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",main_water_valve_locationValues);
                intent.putExtra("heading",main_water_valve_location.getText().toString());
                intent.putExtra("column","mainwatervalvelocation");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",interior_supply_pipingValues);
                intent.putExtra("heading",interior_supply_piping.getText().toString());
                intent.putExtra("column","interiorsupply");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",waste_systemValues);
                intent.putExtra("heading",waste_system.getText().toString());
                intent.putExtra("column","wastesystem");
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
                intent.putExtra("items",dwv_pipingValues);
                intent.putExtra("heading",dwv_piping.getText().toString());
                intent.putExtra("column","strudwvpiping");
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
                intent.putExtra("items",water_heaterValues);
                intent.putExtra("heading",water_heater.getText().toString());
                intent.putExtra("column","waterheater");
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
                intent.putExtra("items",fuel_storage_distributionValues);
                intent.putExtra("heading",fuel_storage_distribution.getText().toString());
                intent.putExtra("column","fuelshortage_distribution");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",fuel_shut_off_valvesValues);
                intent.putExtra("heading",fuel_shut_off_valves.getText().toString());
                intent.putExtra("column","fuelshutoffvalves");
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
                intent.putExtra("items",other_components_plumbingValues);
                intent.putExtra("heading",other_components_plumbing.getText().toString());
                intent.putExtra("column","othercomponents");
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
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",plumbing_observationsValues);
                intent.putExtra("heading",plumbing_observations.getText().toString());
                intent.putExtra("column","observation");
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
                intent.putExtra("items",water_heater_plimbingValues);
                intent.putExtra("heading",water_heater_plimbing.getText().toString());
                intent.putExtra("column","rwaterheater");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",gas_pipingValues);
                intent.putExtra("heading",gas_piping.getText().toString());
                intent.putExtra("column","gaspiping");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",supply_pipingValues);
                intent.putExtra("heading",supply_piping.getText().toString());
                intent.putExtra("column","rsupplypiping");
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
                intent.putExtra("items",dwv_piping_observationValues);
                intent.putExtra("heading",dwv_piping_observation.getText().toString());
                intent.putExtra("column","dwvpiping");
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
                intent.putExtra("items",fixturesValues);
                intent.putExtra("heading",fixtures.getText().toString());
                intent.putExtra("column","fixtures");
                intent.putExtra("dbTable",PLUMBING_TABLE);
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
                intent.putExtra("items",sump_pumpValues);
                intent.putExtra("heading",sump_pump.getText().toString());
                intent.putExtra("column","sumppump");
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
                intent.putExtra("items",waste_ejector_pumpValues);
                intent.putExtra("heading",waste_ejector_pump.getText().toString());
                intent.putExtra("column","wasteejectorpump");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        water_heater_plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",water_heater_plumbingValues);
                intent.putExtra("heading",water_heater_plumbing.getText().toString());
                intent.putExtra("column","water_heater_plumbing");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        gas_piping_plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",gas_piping_plumbingValues);
                intent.putExtra("heading",gas_piping_plumbing.getText().toString());
                intent.putExtra("column","gas_piping_plumbing");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        supply_plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",supply_plumbingValues);
                intent.putExtra("heading",supply_plumbing.getText().toString());
                intent.putExtra("column","supply_plumbing");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        dwv_plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",dwv_plumbingValues);
                intent.putExtra("heading",dwv_plumbing.getText().toString());
                intent.putExtra("column","dwv_plumbing");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        fixtures_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",fixtures_observationValues);
                intent.putExtra("heading",fixtures_observation.getText().toString());
                intent.putExtra("column","fixtures_observation");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        sump_pump_plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",sump_pump_plumbingValues);
                intent.putExtra("heading",sump_pump_plumbing.getText().toString());
                intent.putExtra("column","sump_pump_plumbing");
                intent.putExtra("dbTable",PLUMBING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        waste_ejector_pump_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",waste_ejector_pump_roValues);
                intent.putExtra("heading",waste_ejector_pump_ro.getText().toString());
                intent.putExtra("column","waste_ejector_pump_ro");
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
                            database.insertEntry("waterheater", object.getString("waterheater"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rfurnace", object.getString("rfurnace"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fuelshortage_distribution", object.getString("fuelshortage_distribution"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fuelshutoffvalves", object.getString("fuelshutoffvalves"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("othercomponents", object.getString("othercomponents"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("observation", object.getString("observation"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rwaterheater", object.getString("rwaterheater"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("gaspiping", object.getString("gaspiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rsupplypiping", object.getString("rsupplypiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("dwvpiping", object.getString("dwvpiping"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fixtures", object.getString("fixtures"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("c", object.getString("fixtures"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("wasteejectorpump", object.getString("wasteejectorpump"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("gas_piping_plumbing", object.getString("gas_piping_plumbing"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("supply_plumbing", object.getString("supply_plumbing"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("dwv_plumbing", object.getString("dwv_plumbing"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fixtures_observation", object.getString("fixtures_observation"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("sump_pump_plumbing", object.getString("sump_pump_plumbing"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("waste_ejector_pump_ro", object.getString("waste_ejector_pump_ro"), PLUMBING_TABLE, StructureScreensActivity.inspectionID);

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
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
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
