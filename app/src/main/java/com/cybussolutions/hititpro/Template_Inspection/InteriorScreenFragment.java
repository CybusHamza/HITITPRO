package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InteriorScreenFragment extends BaseFragment {

    View root;
    Button next, back;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;


    Button wall_ceiling,floors_interoir,windows,doors,interior_observations,
            walls_ceilings,floors_ro,windows_ro,doors_ro,counters_cabinets,skylights_ro,
            stairways_ro,basement_ro,environmental_issues,walls_ceilings_ro,floors_observer,windows_observe,
    doors_observation,Counters_Cabinets_observation,skylights_obs,basement_observation,stairways_observation,environmental_issues_ro;


    String[] wall_ceilingValues,floors_interoirValues,windowsValues,doorsValues,interior_observationsValues,
            walls_ceilingsValues,floors_roValues,windows_roValues,doors_roValues,counters_cabinetsValues,skylights_roValues,
            stairways_roValues,basement_roValues,environmental_issuesValues,walls_ceilings_roValues,floors_observerValues,windows_observeValues,
            doors_observationValues,Counters_Cabinets_observationValues,skylights_obsValues,stairways_observationValues,basement_observationValues,environmental_issues_roValues;

    private static final String INTERIOR_TABLE = "interior";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_interior_screen, container, false);

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InteriorSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HeatingScreenFragment()).addToBackStack("heating").commit();
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

        wall_ceiling = (Button) root.findViewById(R.id.wall_ceiling);
        floors_interoir = (Button) root.findViewById(R.id.floors_interoir);
        windows = (Button) root.findViewById(R.id.windows);
        doors = (Button) root.findViewById(R.id.doors);
        interior_observations = (Button) root.findViewById(R.id.interior_observations);
        walls_ceilings = (Button) root.findViewById(R.id.walls_ceilings);
        floors_ro = (Button) root.findViewById(R.id.floors_ro);
        windows_ro = (Button) root.findViewById(R.id.windows_ro);
        doors_ro = (Button) root.findViewById(R.id.doors_ro);
        skylights_ro = (Button) root.findViewById(R.id.skylights_ro);
        stairways_ro = (Button) root.findViewById(R.id.stairways_ro);
        basement_ro = (Button) root.findViewById(R.id.basement_ro);
        counters_cabinets = (Button) root.findViewById(R.id.counters_cabinets);
        environmental_issues = (Button) root.findViewById(R.id.environmental_issues);
        walls_ceilings_ro = (Button) root.findViewById(R.id.walls_ceilings_ro);
        floors_observer = (Button) root.findViewById(R.id.floors_observer);
        windows_observe = (Button) root.findViewById(R.id.windows_observe);
        doors_observation = (Button) root.findViewById(R.id.doors_observation);
        Counters_Cabinets_observation = (Button) root.findViewById(R.id.Counters_Cabinets_observation);
        skylights_obs = (Button) root.findViewById(R.id.skylights_obs);
        basement_observation = (Button) root.findViewById(R.id.basement_observation);
        stairways_observation = (Button) root.findViewById(R.id.stairways);
        environmental_issues_ro = (Button) root.findViewById(R.id.environmental_issues_ro);


        wall_ceilingValues = new String[]{"Drywall%0","Wood%0","Plaster%0","Paneling%0","Masonry%0","Suspended Tile%0","Tile%0"};
        floors_interoirValues = new String[]{"Carpet%0","Tile%0","Wood%0","Vinyl Resilient%0","Concrete%0","Stone%0","Slate%0"};
        windowsValues = new String[]{"Double/Single Hung%0","Sliders%0","Casement%0","Fixed Pane%0","Jalousie%0","Awning%0","Single Pane%0","Double Pane%0"};
        doorsValues = new  String []{"Wood Solid Core%0","Wood Hollow Core%0","Metal%0","Sliding Glass%0","French%0","Other%0"};
        interior_observationsValues = new  String []{"GOOD%0","AVERAGE%0","BELOW AVERAGE%0"};
        walls_ceilingsValues = new  String []{"Ceiling Water Stain (Infra-Red%0)","Ceiling Water Stain (Other)%0","Active Ceiling Leak (Infra-Red)%0","Active Ceiling Leak (Moisture Meter)%0"
                ,"Drywall Blemishes%0","Patching%0","Damage Noted%0","Minor Ceiling /Wall Cracks%0","Drywall Tape Visible%0","Seal Trim/Miter Joints%0","Mold Like Substance%0"};
        floors_roValues = new  String []{"Loose Subflooring%0","Floor Creaking%0","Floor Popping%0","H’wood Floor Gaps%0"
                ,"Re-Stain Floor%0","H’wood Warped%0","Tile Cracked%0","Poor Tile Install%0","Vinyl Seams Poor%0","Vinyl Floor Damage%0",
                "Carpet Stains%0","Carpet Bunching%0","Base Molding Gap%0","Trim Incomplete%0","Trim Loose%0"};
        windows_roValues = new  String []{"Old Windows%0","Water Damage Below Sill%0","Windows Stuck%0","Sash Stuck Open%0",
                "Window Drafting%0","Window Locked Shut%0","Window Pane Cracked%0","Window Sprung%0","Window H’ware Missing%0",
                "Window H’ware Damaged%0","Some Seals Broken%0","Spacer Seal Showing%0","Speckled Windows%0","Window Screens Damaged%0","Window Screens Missing%0"};
        doors_roValues = new  String []{"Trim/Adjust%0","Door Stops%0","Top Latch Missing%0","Need Weather Stripping%0","Bi-Fold Door Off Track%0","Door H’ware Damaged%0"
                ,"Door Damaged%0","Double Keyed Deadbolt%0","Daylight at Door%0","Sliding Glass Door Old%0","Sliding Door Leak%0","Garage Man Door Rating%0"};
        counters_cabinetsValues = new  String []{"Damaged Countertops%0","Missing Grout%0","Damaged Tile%0","Damaged Cabinets%0"
                ,"Damaged Door Hinges%0","Missing/Damaged Cabinet Handles%0","Broken Drawers%0","Paint Cabinets%0"};
        skylights_roValues = new  String []{"Water Damage%0"};
        stairways_roValues = new  String []{"Loose Handrail%0","Need Handrail%0","Openings in Handrail%0","Railing Height%0",
                "Stairway Clearance%0","Stairway Treads%0","Door Opens Into Stairway%0"};
        basement_roValues = new  String []{"No Leaks Visible%0","Leaking Repair%0","Leaking Monitor%0","Leaking With Sump Pump Present%0"};
        environmental_issuesValues = new  String []{"Possible Asbestos%0","3 Hour Radon Screening%0"};
        walls_ceilings_roValues = new  String []{"Ceiling Water Stain (Infra-Red)%0","Ceiling Water Stain (Other)%0"
                ,"Active Ceiling Leak (Infra-Red)%0","Active Ceiling Leak (Moisture Meter)%0","Drywall Blemishes%0",
                "Patching%0","Damage Noted%0","Minor Ceiling / Wall Cracks%0","Drywall Tape%0","Seal Trim Miter Joints%0","Mold-Like Substance%0"};
        floors_observerValues = new  String []{"Loose Subflooring%0","Floor Creaking%0","Floor Popping%0","H’wood Floor Gaps%0"
                ,"Re-Stain Floor%0","H’wood Warped%0","Tile Cracked%0","Poor Tile Install%0","Vinyl Seams Poor%0","Vinyl Floor Damage%0",
                "Carpet Stains%0","Carpet Bunching%0","Base Molding Gap%0","Trim Incomplete%0","Trim Loose%0"};
        windows_observeValues = new  String []{"Old Windows%0","Water Damage Below Sill%0","Windows Stuck%0","Sash Stuck Open%0",
                "Window Drafting%0","Window Locked Shut%0","Window Pane Cracked%0","Window Sprung%0","Window H’ware Missing%0",
                "Window H’ware Damaged%0","Some Seals Broken%0","Spacer Seal Showing%0","Speckled Windows%0","Window Screens Damaged%0","Window Screens Missing%0"};
        doors_observationValues = new  String []{"Trim/Adjust%0","Door Stops%0","Top Latch Missing%0","Need Weather Stripping%0","Bi-Fold Door Off Track%0","Door H’ware Damaged%0"
                ,"Door Damaged%0","Double Keyed Deadbolt%0","Daylight at Door%0","Sliding Glass Door Old%0","Sliding Door Leak%0","Garage Man Door Rating%0"};
        Counters_Cabinets_observationValues = new  String []{"Damaged Countertops%0","Missing Grout%0","Damaged Tile%0","Damaged Cabinets%0"
                ,"Damaged Door Hinges%0","Missing/Damaged Cabinet Handles%0","Broken Drawers%0","Paint Cabinets%0"};
        skylights_obsValues = new  String []{"Water Damage%0"};
        stairways_observationValues = new  String []{"Loose Handrail%0","Need Handrail%0","Openings in Handrail%0","Railing Height%0",
                "Stairway Clearance%0","Stairway Treads%0","Door Opens Into Stairway%0"};
        basement_observationValues = new String[]{"No Leaks Visible%0","Leaking Repair%0","Leaking Monitor%0","Leaking With Sump Pump Present%0"};
        environmental_issues_roValues = new  String []{"Possible Asbestos%0","3 Hour Radon Screening%0"};


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Interior Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isInterior_populated","");


        if(!(populate.equals("true")))
        {
            database.prePopulateData("wall_cieling", wall_ceilingValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("floors", floors_interoirValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("windows", windowsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("doors", doorsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", interior_observationsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwalls_ceiling", walls_ceilingsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rfloors", floors_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwindows", windows_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdoors", doors_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rcounters_cabinets", counters_cabinetsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rskylights", skylights_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rstairways", stairways_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rbasement", basement_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("renvironmentalissues", environmental_issuesValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("walls_ceilings_ro", walls_ceilings_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("floors_observer", floors_observerValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("windows_observe", windows_observeValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("doors_observation", doors_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("Counters_Cabinets_observation", Counters_Cabinets_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("skylights_obs", skylights_obsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("stairways_observation", stairways_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("basement_observation", basement_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("environmental_issues_ro", environmental_issues_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);

            // Saving string
            editor.putString("isInterior_populated", "true");
            editor.apply();
        }


        wall_ceiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",wall_ceilingValues);
                intent.putExtra("heading",wall_ceiling.getText().toString());
                intent.putExtra("column","wall_cieling");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        floors_interoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",floors_interoirValues);
                intent.putExtra("heading",floors_interoir.getText().toString());
                intent.putExtra("column","floors");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        windows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",windowsValues);
                intent.putExtra("heading",windows.getText().toString());
                intent.putExtra("column","windows");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        doors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",doorsValues);
                intent.putExtra("heading",doors.getText().toString());
                intent.putExtra("column","doors");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        interior_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",interior_observationsValues);
                intent.putExtra("heading",interior_observations.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        walls_ceilings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",walls_ceilingsValues);
                intent.putExtra("heading",walls_ceilings.getText().toString());
                intent.putExtra("column","rwalls_ceiling");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        floors_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",floors_roValues);
                intent.putExtra("heading",floors_ro.getText().toString());
                intent.putExtra("column","rfloors");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        windows_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",windows_roValues);
                intent.putExtra("heading",windows_ro.getText().toString());
                intent.putExtra("column","rwindows");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        doors_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",doors_roValues);
                intent.putExtra("heading",doors_ro.getText().toString());
                intent.putExtra("column","rdoors");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        counters_cabinets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",counters_cabinetsValues);
                intent.putExtra("heading",counters_cabinets.getText().toString());
                intent.putExtra("column","rcounters_cabinets");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        skylights_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",skylights_roValues);
                intent.putExtra("heading",skylights_ro.getText().toString());
                intent.putExtra("column","rskylights");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        stairways_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",stairways_roValues);
                intent.putExtra("heading",stairways_ro.getText().toString());
                intent.putExtra("column","rstairways");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        basement_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",basement_roValues);
                intent.putExtra("heading",basement_ro.getText().toString());
                intent.putExtra("column","rbasement");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        environmental_issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",environmental_issuesValues);
                intent.putExtra("heading",environmental_issues.getText().toString());
                intent.putExtra("column","renvironmentalissues");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        walls_ceilings_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",walls_ceilings_roValues);
                intent.putExtra("heading",walls_ceilings_ro.getText().toString());
                intent.putExtra("column","floors_observer");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        floors_observer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",floors_observerValues);
                intent.putExtra("heading",floors_observer.getText().toString());
                intent.putExtra("column","floors_observer");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        windows_observe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",windows_observeValues);
                intent.putExtra("heading",windows_observe.getText().toString());
                intent.putExtra("column","windows_observe");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        doors_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",doors_observationValues);
                intent.putExtra("heading",doors_observation.getText().toString());
                intent.putExtra("column","doors_observation");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        Counters_Cabinets_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",Counters_Cabinets_observationValues);
                intent.putExtra("heading",Counters_Cabinets_observation.getText().toString());
                intent.putExtra("column","Counters_Cabinets_observation");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        skylights_obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",skylights_obsValues);
                intent.putExtra("heading",skylights_obs.getText().toString());
                intent.putExtra("column","skylights_obs");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        basement_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",basement_observationValues);
                intent.putExtra("heading",basement_observation.getText().toString());
                intent.putExtra("column","basement_observation");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        stairways_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",stairways_observationValues);
                intent.putExtra("heading",stairways_observation.getText().toString());
                intent.putExtra("column","stairways_observation");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        environmental_issues_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",environmental_issues_roValues);
                intent.putExtra("heading",environmental_issues_ro.getText().toString());
                intent.putExtra("column","environmental_issues_ro");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });



        return root;
    }


    public void InteriorSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Interior ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_INTERIOR,
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


                Cursor cursor = database.getTable(INTERIOR_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("wall_cieling", cursor.getString(6));
                    params.put("floors", cursor.getString(7));
                    params.put("windows", cursor.getString(8));
                    params.put("doors", cursor.getString(9));
                    params.put("observation", cursor.getString(10));
                    params.put("rwalls_ceiling", cursor.getString(11));
                    params.put("rfloors", cursor.getString(12));
                    params.put("rwindows", cursor.getString(13));
                    params.put("rdoors", cursor.getString(14));
                    params.put("rcounters_cabinets", cursor.getString(15));
                    params.put("rskylights", cursor.getString(16));
                    params.put("rstairways", cursor.getString(17));
                    params.put("rbasement", cursor.getString(18));
                    params.put("renvironmentalissues", cursor.getString(19));

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
