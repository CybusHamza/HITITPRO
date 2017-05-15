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


public class InsulationScreenFragment extends BaseFragment {

    Button next, back;
    View root;

    Button ATTIC_INSULATION,EXTERIORWALLINSULATION,BASEMENTWALLINSULATION,CRAWLSPACEINSULATION,
    VAPORRETARDERS,ROOFVENTILATION,CRAWLSPACEVENTILATION,EXHAUSTFANS_VENTS,Insulation_Ventilation_Observations,
    Attic_Roof,Basement,CrawlSpace;



    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;
    private static final String INSULATION_TABLE = "insulation";
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_insulation_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Insulation");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsulationSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlumbingScreenFragment()).addToBackStack("plumbing").commit();
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

        ATTIC_INSULATION = (Button) root.findViewById(R.id.attic_insulation);
        EXTERIORWALLINSULATION = (Button) root.findViewById(R.id.exterior_wall_insulation);
        BASEMENTWALLINSULATION = (Button) root.findViewById(R.id.basement_wall_insulation);
        CRAWLSPACEINSULATION = (Button) root.findViewById(R.id.crawl_space_insulation);
        VAPORRETARDERS = (Button) root.findViewById(R.id.vapor_retarders);
        ROOFVENTILATION = (Button) root.findViewById(R.id.roof_ventilation);
        CRAWLSPACEVENTILATION = (Button) root.findViewById(R.id.crawl_space_ventilation);
        EXHAUSTFANS_VENTS = (Button) root.findViewById(R.id.exhaust_fans_vents);
        Insulation_Ventilation_Observations = (Button) root.findViewById(R.id.insulation_ventilation_observations);
        Attic_Roof = (Button) root.findViewById(R.id.attic_and_roof);
        Basement = (Button) root.findViewById(R.id.basement);
        CrawlSpace = (Button) root.findViewById(R.id.crawl_space);



        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Insulation");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isInsulation_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getInsulation();

        }


        
        ATTIC_INSULATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.ATTIC_INSULATION_Values);
                intent.putExtra("heading",ATTIC_INSULATION.getText().toString());
                intent.putExtra("column","atticinsulation");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        EXTERIORWALLINSULATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.EXTERIORWALLINSULATION_Values);
                intent.putExtra("heading",EXTERIORWALLINSULATION.getText().toString());
                intent.putExtra("column","exteriorwallinsulation");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        BASEMENTWALLINSULATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.BASEMENTWALLINSULATION_Values);
                intent.putExtra("heading",BASEMENTWALLINSULATION.getText().toString());
                intent.putExtra("column","basementwallinsulation");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        CRAWLSPACEINSULATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.CRAWLSPACEINSULATION_Values);
                intent.putExtra("heading",CRAWLSPACEINSULATION.getText().toString());
                intent.putExtra("column","crawlspaceinsulation");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        VAPORRETARDERS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.VAPORRETARDERS_Values);
                intent.putExtra("heading",VAPORRETARDERS.getText().toString());
                intent.putExtra("column","vaporretarders");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        ROOFVENTILATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.ROOFVENTILATION_Values);
                intent.putExtra("heading",ROOFVENTILATION.getText().toString());
                intent.putExtra("column","roofventilation");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        CRAWLSPACEVENTILATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.CRAWLSPACEVENTILATION_Values);
                intent.putExtra("heading",CRAWLSPACEVENTILATION.getText().toString());
                intent.putExtra("column","servicedrop");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        EXHAUSTFANS_VENTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.EXHAUSTFANS_VENTS_Values);
                intent.putExtra("heading",EXHAUSTFANS_VENTS.getText().toString());
                intent.putExtra("column","servicedrop");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        Insulation_Ventilation_Observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.Insulation_Ventilation_Observations_Values);
                intent.putExtra("heading",Insulation_Ventilation_Observations.getText().toString());
                intent.putExtra("column","servicedrop");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        Attic_Roof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.Attic_Roof_Values);
                intent.putExtra("heading",Attic_Roof.getText().toString());
                intent.putExtra("column","atticandroof");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        Basement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.Basement_Values);
                intent.putExtra("heading",Basement.getText().toString());
                intent.putExtra("column","basement");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        CrawlSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.CrawlSpace_Values);
                intent.putExtra("heading",CrawlSpace.getText().toString());
                intent.putExtra("column","crawlspace");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INSULATION_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        return root;
    }

    private void getInsulation() {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(INSULATION_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("atticinsulation", object.getString("atticinsulation"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("exteriorwallinsulation", object.getString("exteriorwallinsulation"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("basementwallinsulation", object.getString("basementwallinsulation"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("csv", object.getString("csv"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("efv", object.getString("efv"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("crawlspaceinsulation", object.getString("crawlspaceinsulation"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("vaporretarders", object.getString("vaporretarders"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("roofventilation", object.getString("roofventilation"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("observations", object.getString("observations"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("atticandroof", object.getString("atticandroof"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("basement", object.getString("basement"), INSULATION_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("crawlspace", object.getString("crawlspace"), INSULATION_TABLE, StructureScreensActivity.inspectionID);

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


                Cursor cursor = database.getTable(INSULATION_TABLE, StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("tempid", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("temp_name", INSULATION_TABLE);


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


    public void InsulationSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Insulation ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_INSULATION,
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


                Cursor cursor = database.getTable(INSULATION_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("atticinsulation", cursor.getString(6));
                    params.put("exteriorwallinsulation", cursor.getString(7));
                    params.put("basementwallinsulation", cursor.getString(8));
                    params.put("csv", cursor.getString(9));
                    params.put("efv", cursor.getString(10));
                    params.put("crawlspaceinsulation", cursor.getString(11));
                    params.put("vaporretarders", cursor.getString(12));
                    params.put("roofventilation", cursor.getString(13));
                    params.put("observations", cursor.getString(14));
                    params.put("atticandroof", cursor.getString(15));
                    params.put("basement", cursor.getString(16));
                    params.put("crawlspace", cursor.getString(17));

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
