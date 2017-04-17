package com.cybussolutions.hititpro.Template_Inspection;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
import com.cybussolutions.hititpro.Activities.Start_Inspection;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Fragments.BaseFragment;
import com.cybussolutions.hititpro.Fragments.ClientsFragment;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StructureScreenFragment extends BaseFragment {

    View root;
    Button next,back;
    private static final String PORTFOLIO_TABLE = "portfolio";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;
    ArrayList<String> datArray = new ArrayList<>();
    FragmentManager manager;

//    ImageView foundationImg, columnsImg, floorStructureImg, wallStructureImg, ceilingStructureImg, roofStructureImg,
//     structureObservationsImg, roFoundationImg, roCrawlSpacesImg, roFloorsImg, roExteriorWallsImg, roRoofImg;


    Button foundationButton, columnsButton, floorStructureButton,wallStructureSpinner, ceilingStructureButton, roofStructureButton,
            structureObservationsButton, roFoundationButton, roCrawlSpacesButton, roFloorsButton, roExteriorWallsButton, roRoofButton;


    String[] foundationSpinnerValues, columnsSpinnerValues, floorStructureSpinnerValues, wallStructureSpinnerValues, ceilingStructureSpinnerValues, roofStructureSpinnerValues,
            structureObservationsSpinnerValues, roFoundationSpinnerValues, roCrawlSpacesSpinnerValues, roFloorsValues, roExteriorWallsValues, roRoofValues;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        database= new Database(getActivity());

        root = inflater.inflate(R.layout.fragment_structure_screen, container, false);

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StructureSync();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RoofingScreenFragment()).addToBackStack("roofing").commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClientsFragment()).commit();
            }
        });

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        foundationButton = (Button) root.findViewById(R.id.foundation_button);
        columnsButton = (Button) root.findViewById(R.id.clolums_button);
        floorStructureButton = (Button) root.findViewById(R.id.floor_structure_button);
        wallStructureSpinner = (Button) root.findViewById(R.id.wall_structure_button);
        ceilingStructureButton = (Button) root.findViewById(R.id.celling_structure_button);
        roofStructureButton = (Button) root.findViewById(R.id.roof_structure_button);
        structureObservationsButton = (Button) root.findViewById(R.id.structure_observations_button);
        roFoundationButton = (Button) root.findViewById(R.id.foundation_structure_button);
        roCrawlSpacesButton = (Button) root.findViewById(R.id.crawl_spaces_button);
        roFloorsButton = (Button) root.findViewById(R.id.floor_button);
        roExteriorWallsButton = (Button) root.findViewById(R.id.exteroir_button);
        roRoofButton = (Button) root.findViewById(R.id.roof_button);

        // image view

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Structure Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        // setting up values


            foundationSpinnerValues = new String[]{"Poured Concrete%0", "Slab On Grade%0", "Concrete Block%0", "Masonry Block%0", "Piers%0", "Basement Configuration%0", "Crawl Space%0", "Basement/Crawl Space Configuration%0"};
            columnsSpinnerValues = new String[]{"Steel%0", "Wood%0", "Concrete Block%0", "Wood%0"};
            floorStructureSpinnerValues = new String[]{"Wood Joist%0", "Trusses%0", "Concrete%0"};
            wallStructureSpinnerValues = new String[]{"Wood Frame%0", "Wood Frame, Brick Veneer%0", "Masonry%0", "Other%0"};
            ceilingStructureSpinnerValues = new String[]{"Joist%0", "Truss%0", "Rafters%0", "Other%0"};
            roofStructureSpinnerValues = new String[]{"Rafters%0", "Truss%0", "Joists%0","Other%0"};
            structureObservationsSpinnerValues = new String[]{"GOOD%0", "AVERAGE%0", "MINOR SETTLEMENT%0", "SIGNIFICANT ISSUES%0"};
            roFoundationSpinnerValues = new String[]{"Cracks Minor%0", "Cracks Moderate%0", "Cracks Major%0", "Water Intrusion%0", "Floor Jacks%0", "Active Plumbing Leaks%0", "Termite Warning%0", "Basement Floor Cracks%0"};
            roCrawlSpacesSpinnerValues = new String[]{"Wood Debris/Trash%0", "Wood/Soil Contact%0", "Water Intrusion%0", "Efflorescence%0", "Floor Jacks%0", "Seal Openings%0", "Mildew/Mold on Joists%0", "Reroute Dryer Vent%0", "Vermin Activity%0", "Active Plumbing Leaks%0", "Standing Water%0"};
            roFloorsValues = new String[]{"Minor Framing Flaws%0", "Sills Near Grade%0", "Sills Below Grade%0",
                    "Joist Cracking%0", "Joist Notching%0", "Rot Visible%0"};
            roExteriorWallsValues = new String[]{"Foundation Cracks%0", "Chimney Movement%0", "Parge Exterior Walls%0"};
            roRoofValues = new String[]{"Ridge Sag%0", "Rafter Sag%0", "Collar Ties Insufficient%0", "Trusses Cut%0",
                    "Decking Delaminating%0", "Major Roof Leaks%0"};



        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isStructure_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getStructure();

        }

        else {
            if(!(populate.equals("true")))
            {
                database.prePopulateData("foundation", foundationSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("columns", columnsSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("floor_structure", floorStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("wall_structure", wallStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("celling_struture", ceilingStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("roof_structure", roofStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("observation", structureObservationsSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recomnd_foundation", roFoundationSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("crawl_space", roCrawlSpacesSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recomnd_floor", roFloorsValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("exterior_wall", roExteriorWallsValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("roof", roRoofValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);

                // Saving string
                editor.putString("isStructure_populated", "true");
                editor.apply();
            }

        }

        // inserting into local database for pre population of data

        foundationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",foundationSpinnerValues);
                intent.putExtra("heading",foundationButton.getText().toString());
                intent.putExtra("column","foundation");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);

            }
        });

        columnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",columnsSpinnerValues);
                intent.putExtra("heading",columnsButton.getText().toString());
                intent.putExtra("column","columns");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        floorStructureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",floorStructureSpinnerValues);
                intent.putExtra("heading",columnsButton.getText().toString());
                intent.putExtra("column","floor_structure");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        wallStructureSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",wallStructureSpinnerValues);
                intent.putExtra("heading",wallStructureSpinner.getText().toString());
                intent.putExtra("column","wall_structure");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        ceilingStructureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",ceilingStructureSpinnerValues);
                intent.putExtra("heading",ceilingStructureButton.getText().toString());
                intent.putExtra("column","celling_struture");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roofStructureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roofStructureSpinnerValues);
                intent.putExtra("heading",columnsButton.getText().toString());
                intent.putExtra("column","roof_structure");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        structureObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",structureObservationsSpinnerValues);
                intent.putExtra("heading",roofStructureButton.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFoundationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFoundationSpinnerValues);
                intent.putExtra("heading",roFoundationButton.getText().toString());
                intent.putExtra("column","recomnd_foundation");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roCrawlSpacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roCrawlSpacesSpinnerValues);
                intent.putExtra("heading",roCrawlSpacesButton.getText().toString());
                intent.putExtra("column","crawl_space");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roFloorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFloorsValues);
                intent.putExtra("heading",roFloorsButton.getText().toString());
                intent.putExtra("column","recomnd_floor");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roExteriorWallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roExteriorWallsValues);
                intent.putExtra("heading",roExteriorWallsButton.getText().toString());
                intent.putExtra("column","exterior_wall");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roRoofButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roRoofValues);
                intent.putExtra("heading",roRoofButton.getText().toString());
                intent.putExtra("column","roof");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });




        return root;
    }



    public void StructureSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Structure ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_STRUCTURE,
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


                Cursor cursor = database.getTable(PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                    Map<String, String> params = new HashMap<>();
                    params.put("template_id", StructureScreensActivity.template_id);
                    params.put("inspection_id", StructureScreensActivity.inspectionID);
                    params.put("client_id", StructureScreensActivity.client_id);
                    params.put("is_applicable", "1");
                    params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("foundation", cursor.getString(6));
                    params.put("columns", cursor.getString(7));
                    params.put("floor_structure", cursor.getString(8));
                    params.put("wall_structure", cursor.getString(9));
                    params.put("celling_struture", cursor.getString(10));
                    params.put("roof_structure", cursor.getString(11));
                    params.put("observation", cursor.getString(12));
                    params.put("recomnd_foundation", cursor.getString(13));
                    params.put("crawl_space", cursor.getString(14));
                    params.put("recomnd_floor", cursor.getString(15));
                    params.put("exterior_wall", cursor.getString(16));
                    params.put("roof", cursor.getString(17));
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

    public void getStructure() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(PORTFOLIO_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                                JSONObject object = jsonArray.getJSONObject(0);


                    database.insertEntry("foundation",object.getString("foundation"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("columns",object.getString("columns"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("floor_structure",object.getString("floor_structure"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("wall_structure",object.getString("wall_structure"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("celling_struture",object.getString("celling_struture"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("roof_structure",object.getString("roof_structure"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("observation",object.getString("observation"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("recomnd_foundation",object.getString("recomnd_foundation"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("crawl_space",object.getString("crawl_space"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("recomnd_floor",object.getString("recomnd_floor"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("exterior_wall",object.getString("exterior_wall"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                    database.insertEntry("roof",object.getString("roof"),PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);


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


                Cursor cursor = database.getTable(PORTFOLIO_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", PORTFOLIO_TABLE);


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
