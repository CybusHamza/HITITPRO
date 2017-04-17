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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ElectricalScreenFragment extends BaseFragment {

    View root;
    Button next, back;

    Button electricalSizeButton, electricalDropButton, electricalConductorsButton, electricalDisconnectButton, electricalGroundingButton,
            electricalPanelButton, electricalSubpanelButton, electricalWiringButton, electricalMethodButton,
            electricalSwitchesButton, electricalgfciButton, electricalSmokeButton, electricalObservationButton,
            roEntranceButton, roMainpanelButton, roSubpanelButton, roDistributionButton, roOutletButton, roSwitchesButton,
            roCeilingButton, roDetectorButton;

    String[] electricalSizeButtonValues, electricalDropButtonValues, electricalConductorsButtonValues, electricalDisconnectButtonValues,
            electricalGroundingButtonValues, electricalPanelButtonValues,
            electricalSubpanelButtonValues, electricalWiringButtonValues, electricalMethodButtonValues, electricalSwitchesButtonValues,
            electricalgfciButtonValues, electricalSmokeButtonValues, electricalObservationButtonValues, roEntranceButtonValues,
            roMainpanelButtonValues, roSubpanelButtonValues, roDistributionButtonValues, roOutletButtonValues,
            roSwitchesButtonValues, roCeilingButtonValues, roDetectorButtonValues;

    private static final String ELECTRICAL_TABLE = "electrical";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_electrical_screen, container, false);

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElectricalSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new InsulationScreenFragment()).addToBackStack("insulation").commit();
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

        electricalSizeButton = (Button) root.findViewById(R.id.electrical_size_Button);
        electricalDropButton = (Button) root.findViewById(R.id.electrical_drop_Button);
        electricalConductorsButton = (Button) root.findViewById(R.id.electrical_conductors_Button);
        electricalDisconnectButton = (Button) root.findViewById(R.id.electrical_disconnect_Button);
        electricalGroundingButton = (Button) root.findViewById(R.id.electrical_grounding_Button);
        electricalPanelButton = (Button) root.findViewById(R.id.electrical_panel_Button);
        electricalSubpanelButton = (Button) root.findViewById(R.id.electrical_subpanel_Button);
        electricalWiringButton = (Button) root.findViewById(R.id.electrical_wiring_Button);
        electricalMethodButton = (Button) root.findViewById(R.id.electrical_method_Button);
        electricalSwitchesButton = (Button) root.findViewById(R.id.electrical_switches_Button);
        electricalgfciButton = (Button) root.findViewById(R.id.electrical_gfci_Button);
        electricalSmokeButton = (Button) root.findViewById(R.id.electrical_smoke_Button);
        electricalObservationButton = (Button) root.findViewById(R.id.electrical_observation_Button);

        roEntranceButton = (Button) root.findViewById(R.id.ro_entrance_Button);
        roMainpanelButton = (Button) root.findViewById(R.id.ro_mainpanel_Button);
        roSubpanelButton = (Button) root.findViewById(R.id.ro_subpanel_Button);
        roDistributionButton = (Button) root.findViewById(R.id.ro_distribution_Button);
        roOutletButton = (Button) root.findViewById(R.id.ro_outlets_Button);
        roSwitchesButton = (Button) root.findViewById(R.id.ro_switches_Button);
        roCeilingButton = (Button) root.findViewById(R.id.ro_ceiling_Button);
        roDetectorButton = (Button) root.findViewById(R.id.ro_dectectors_Button);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Electrical Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        electricalSizeButtonValues = new String[]{"100 Amps  120/240v Main Service%0", "150 Amps  120/240v Main Service%0", "200 Amps  120/240v Main Service%0", "(2) 150 Amps  120/240v Main Service%0"};
        electricalDropButtonValues = new String[]{"Overhead%0", "Underground%0", "Not Visible%0"};
        electricalConductorsButtonValues = new String[]{"Aluminum%0", "Copper%0", "Not Visible%0"};
        electricalDisconnectButtonValues = new String[]{"Main Service Rating 100 Amps%0", "Main Service Rating 150 Amps%0", "Main Service Rating 200 Amps%0", "Main Service Rating (2) 150 Amps%0"};
        electricalGroundingButtonValues = new String[]{"Copper%0", "Aluminum%0", "Ground Rod Connection%0", "UFER Ground%0", "Connection Not Visible%0"};
        electricalPanelButtonValues = new String[]{"Panel Rating 100 Amps%0", "Panel Rating 150 Amps%0", "Panel Rating 200 Amps%0", "Fuses%0", "Breakers%0", "Location ???%0"};
        electricalSubpanelButtonValues = new String[]{"Panel Rating  60 Amps%0", "Panel Rating 100 Amps%0", "Panel Rating 150 Amps%0", "Breakers%0", "Fuses%0", "Location???%0"};
        electricalWiringButtonValues = new String[]{"Copper%0", "Aluminum Solid Conductor%0", "Aluminum Multi-Strand%0"};
        electricalMethodButtonValues = new String[]{"Non- Metalic  Cable Romex%0", "Tinned Copper%0", "Armored Cable BX%0", "Fabric Covered%0", "Knob and Tube Copper%0"};
        electricalSwitchesButtonValues = new String[]{"Grounded%0", "Ungrounded%0", "Grounded and Ungrounded%0"};
        electricalgfciButtonValues = new String[]{"Bathroom(s)%0", "Kitchen%0", "Exterior%0", "Garage%0", "Jetted Tub%0"};
        electricalSmokeButtonValues = new String[]{"Present%0", "Smoke Detectors Present%0", "None Observed%0"};
        electricalObservationButtonValues = new String[]{"MINOR IMPROVEMENTS NEEDED%0", "NO IMPROVEMENTS%0", "NUMEROUS REPAIRS NEEDED%0", "OBSOLETE SYSTEM%0"};

        roEntranceButtonValues = new String[]{"Undersized%0", "Tap on Main Service%0", "Drip Loop Insufficient%0", "Clearance Inadequate%0", "Poorly Attached%0", "Service Conductors Need Conduit%0", "Rusted Service Box%0", "Damaged Service Box%0", "Ground Rod Ineffective%0"};
        roMainpanelButtonValues = new String[]{"Old%0", "Federal Pacific Panel%0", "Zinsko Panel%0", "Panel Location Poor%0", "Panel Crowded%0", "Close Panel Openings%0",
                "Panel Front Missing%0", "Panel Poorly Labeled%0", "Wiring Clamps Missing%0", "Blunt Panel Screws Needed%0", "Overheated Wiring%0", "Double Taps Present%0",
                "Oversized AC Breaker%0"};
        roSubpanelButtonValues = new String[]{"Old%0", "Panel Location Poor%0", "Panel Crowded%0", "Close Panel Openings%0", "Panel Poorly Labeled%0",
                "Wiring Clamps Missing%0", "Blunt Panel Screws Needed%0", "Overheated Wiring%0", "Double Taps Present%0"};
        roDistributionButtonValues = new String[]{"Abandoned Wiring%0", "Loose Wiring%0", "Overheated%0", "Exposed Wiring%0", "Extension Cords%0", "Open Junction Box%0",
                "Loose Junction Box%0", "Poor Wiring Installation%0", "Knob and Tube%0"};
        roOutletButtonValues = new String[]{"Inoperative%0", "Damaged%0", "Missing Covers%0", "Loose Outlet%0", "Missing Cover Plate%0", "Ungrounded 3-Prong Outlet%0",
                "Grounding Pins Broken%0", "Outlet Layout Inadequate%0", "Reversed Polarity%0", "Most Outlets Ungrounded%0", "Overheated Outlet%0",
                "GFCI Inoperative%0", "GFCI Did Not Trip%0", "GFCI Recommended%0", "3-Prong Dryer Outlet%0", "Dryer Outlet Inoperative%0", "Add ARC Fault Breakers%0", "Add Surge Protector%0"};
        roSwitchesButtonValues = new String[]{"Inoperative%0", "Damaged%0", "Loose%0", "Non-Functioning 3-Way%0", "Overheated%0", "Inoperative Dimmer Switch%0"};
        roCeilingButtonValues = new String[]{"Non-Working Lights%0", "Damaged Fixture%0", "Loose Fixture%0", "Missing Globe%0", "Recessed Light Rating%0", "Ceiling Fan Inoperative%0",
                "Ceiling Fan Wobbles%0", "Ceiling Fan Loose%0", "Fan Not Operable at Low Speeds%0"};
        roDetectorButtonValues = new String[]{"No Detectors Present%0", "Need CO Detector%0", "Smoke Detector Non-Responsive%0", "Update Smoke Detectors%0"};


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isElectrical_populated","");


        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getElectrical();

        }
        else
        {
            if(!(populate.equals("true")))
            {
                database.prePopulateData("sizeofservice", electricalSizeButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("servicedrop", electricalDropButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("entranceconductors", electricalConductorsButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("maindisconnect", electricalDisconnectButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("grounding", electricalGroundingButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("servicepanel", electricalPanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("sub_panel", electricalSubpanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("wiring", electricalWiringButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("wiring_method", electricalMethodButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("switches_receptacles", electricalSwitchesButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("gfci", electricalgfciButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("smoke_codetector", electricalSmokeButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("observation", electricalObservationButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("serviceentrance", roEntranceButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("mainpanel", roMainpanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("subpanel", roSubpanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("distribution", roDistributionButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("outlets", roOutletButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("switches", roSwitchesButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("lights_ceiling_fans", roCeilingButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("smoke_co_detectors", roDetectorButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);

                // Saving string
                editor.putString("isElectrical_populated", "true");
                editor.apply();
            } 
        }

       

        electricalSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalSizeButtonValues);
                intent.putExtra("heading",electricalSizeButton.getText().toString());
                intent.putExtra("column","sizeofservice");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalDropButtonValues);
                intent.putExtra("heading",electricalDropButton.getText().toString());
                intent.putExtra("column","servicedrop");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalConductorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalConductorsButtonValues);
                intent.putExtra("heading",electricalConductorsButton.getText().toString());
                intent.putExtra("column","entranceconductors");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalDisconnectButtonValues);
                intent.putExtra("heading",electricalDisconnectButton.getText().toString());
                intent.putExtra("column","maindisconnect");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalGroundingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalGroundingButtonValues);
                intent.putExtra("heading",electricalGroundingButton.getText().toString());
                intent.putExtra("column","grounding");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalPanelButtonValues);
                intent.putExtra("heading",electricalPanelButton.getText().toString());
                intent.putExtra("column","servicepanel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalSubpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalSubpanelButtonValues);
                intent.putExtra("heading",electricalSubpanelButton.getText().toString());
                intent.putExtra("column","sub_panel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalWiringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalWiringButtonValues);
                intent.putExtra("heading",electricalWiringButton.getText().toString());
                intent.putExtra("column","wiring");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalMethodButtonValues);
                intent.putExtra("heading",electricalMethodButton.getText().toString());
                intent.putExtra("column","wiring_method");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalSwitchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalSwitchesButtonValues);
                intent.putExtra("heading",electricalSwitchesButton.getText().toString());
                intent.putExtra("column","switches_receptacles");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        electricalgfciButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalgfciButtonValues);
                intent.putExtra("heading",electricalgfciButton.getText().toString());
                intent.putExtra("column","gfci");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalSmokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalSmokeButtonValues);
                intent.putExtra("heading",electricalSmokeButton.getText().toString());
                intent.putExtra("column","smoke_codetector");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        electricalObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electricalObservationButtonValues);
                intent.putExtra("heading",electricalObservationButton.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roEntranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roEntranceButtonValues);
                intent.putExtra("heading",roEntranceButton.getText().toString());
                intent.putExtra("column","serviceentrance");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        roMainpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roMainpanelButtonValues);
                intent.putExtra("heading",roMainpanelButton.getText().toString());
                intent.putExtra("column","mainpanel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roSubpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roSubpanelButtonValues);
                intent.putExtra("heading",roSubpanelButton.getText().toString());
                intent.putExtra("column","subpanel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roDistributionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDistributionButtonValues);
                intent.putExtra("heading",roDistributionButton.getText().toString());
                intent.putExtra("column","distribution");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roOutletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roOutletButtonValues);
                intent.putExtra("heading",roOutletButton.getText().toString());
                intent.putExtra("column","outlets");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roSwitchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roSwitchesButtonValues);
                intent.putExtra("heading",roSwitchesButton.getText().toString());
                intent.putExtra("column","switches");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roCeilingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roCeilingButtonValues);
                intent.putExtra("heading",roCeilingButton.getText().toString());
                intent.putExtra("column","lights_ceiling_fans");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roDetectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDetectorButtonValues);
                intent.putExtra("heading",roDetectorButton.getText().toString());
                intent.putExtra("column","smoke_co_detectors");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        return root;
    }

    private void getElectrical() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(ELECTRICAL_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("sizeofservice", object.getString("sizeofservice"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("servicedrop", object.getString("servicedrop"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("entranceconductors", object.getString("entranceconductors"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("maindisconnect", object.getString("maindisconnect"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("grounding", object.getString("grounding"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("servicepanel", object.getString("servicepanel"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("sub_panel", object.getString("sub_panel"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("wiring", object.getString("wiring") ,ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("wiring_method", object.getString("wiring_method"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("switches_receptacles", object.getString("switches_receptacles"),ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("gfci", object.getString("gfci"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("smoke_codetector", object.getString("smoke_codetector"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("observation", object.getString("observation"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("serviceentrance", object.getString("serviceentrance"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("mainpanel", object.getString("mainpanel"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("subpanel", object.getString("subpanel"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("distribution", object.getString(""), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("outlets", object.getString("distribution"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("switches", object.getString("switches"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("lights_ceiling_fans", object.getString("lights_ceiling_fans"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("smoke_co_detectors", object.getString("smoke_co_detectors"), ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);


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


                Cursor cursor = database.getTable(ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("tempid", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("temp_name", ELECTRICAL_TABLE);


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


    public void ElectricalSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Electrical ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_ELECTRICAL,
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


                Cursor cursor = database.getTable(ELECTRICAL_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("sizeofservice", cursor.getString(6));
                    params.put("servicedrop", cursor.getString(7));
                    params.put("entranceconductors", cursor.getString(8));
                    params.put("maindisconnect", cursor.getString(9));
                    params.put("grounding", cursor.getString(10));
                    params.put("servicepanel", cursor.getString(11));
                    params.put("sub_panel", cursor.getString(12));
                    params.put("wiring", cursor.getString(13));
                    params.put("wiring_method", cursor.getString(14));
                    params.put("switches_receptacles", cursor.getString(15));
                    params.put("gfci", cursor.getString(16));
                    params.put("smoke_codetector", cursor.getString(17));
                    params.put("observation", cursor.getString(18));
                    params.put("serviceentrance", cursor.getString(19));
                    params.put("mainpanel", cursor.getString(20));
                    params.put("subpanel", cursor.getString(21));
                    params.put("distribution", cursor.getString(22));
                    params.put("outlets", cursor.getString(23));
                    params.put("switches", cursor.getString(24));
                    params.put("lights_ceiling_fans", cursor.getString(25));
                    params.put("smoke_co_detectors", cursor.getString(26));

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
