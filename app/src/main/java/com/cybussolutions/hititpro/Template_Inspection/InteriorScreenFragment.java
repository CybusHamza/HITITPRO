package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cybussolutions.hititpro.Fragments.TemplatesFragment;
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

import static android.content.Context.MODE_PRIVATE;

public class InteriorScreenFragment extends BaseFragment {

    View root;
    Button next, back,save;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    TextView title;
    Database database;
    AlertDialog b;


    Button wall_ceiling,floors_interoir,windows,doors,interior_observations,
            walls_ceilings,floors_ro,windows_ro,doors_ro,counters_cabinets,skylights_ro,
            stairways_ro,basement_ro,environmental_issues;



    private static final String INTERIOR_TABLE = "interior";

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    String isApplicable;
    MenuItem item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_interior_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Interior");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        title = (TextView) root.findViewById(R.id.title);
        save = (Button) root.findViewById(R.id.save);
        if(!(StructureScreensActivity.is_notemplate.equals("true")))
        {
            save.setVisibility(View.GONE);
            title.setText("    Page 9 of Page 11");
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StructureScreensActivity.is_saved)
                {
                    addDetail();
                }
                else
                {
                    Toast.makeText(getContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InteriorSync();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlumbingScreenFragment()).commit();

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




        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Interior");
     /*   ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);
     */   setHasOptionsMenu(true);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isInterior_populated","");


        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getInterior();

        }




        wall_ceiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.wall_ceilingValues);
                intent.putExtra("heading",wall_ceiling.getText().toString());
                intent.putExtra("column","wall_cieling");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        floors_interoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.floors_interoirValues);
                intent.putExtra("heading",floors_interoir.getText().toString());
                intent.putExtra("column","floors");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        windows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.windowsValues);
                intent.putExtra("heading",windows.getText().toString());
                intent.putExtra("column","windows");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        doors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.doorsValues);
                intent.putExtra("heading",doors.getText().toString());
                intent.putExtra("column","doors");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        interior_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.interior_observationsValues);
                intent.putExtra("heading",interior_observations.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("tag","Interior_Observations");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        walls_ceilings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.walls_ceilingsValues);
                intent.putExtra("heading",walls_ceilings.getText().toString());
                intent.putExtra("column","rwalls_ceiling");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","recmd_WALLS_CEILINGS");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        floors_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.floors_roValues);
                intent.putExtra("heading",floors_ro.getText().toString());
                intent.putExtra("column","rfloors");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","recmd_FLOORS");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        windows_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.windows_roValues);
                intent.putExtra("heading",windows_ro.getText().toString());
                intent.putExtra("column","rwindows");
                intent.putExtra("tag","recmd_WINDOWS");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        doors_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.doors_roValues);
                intent.putExtra("heading",doors_ro.getText().toString());
                intent.putExtra("column","rdoors");
                intent.putExtra("tag","recmd_DOORS");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        counters_cabinets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.counters_cabinetsValues);
                intent.putExtra("heading",counters_cabinets.getText().toString());
                intent.putExtra("column","rcounters_cabinets");
                intent.putExtra("tag","recmd_COUNTERS_CABINETS");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        skylights_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.skylights_roValues);
                intent.putExtra("heading",skylights_ro.getText().toString());
                intent.putExtra("column","rskylights");
                intent.putExtra("tag","SKYLIGHTS");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        stairways_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.stairways_roValues);
                intent.putExtra("heading",stairways_ro.getText().toString());
                intent.putExtra("column","rstairways");
                intent.putExtra("tag","recmd_STAIRWAYS");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        basement_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.basement_roValues);
                intent.putExtra("heading",basement_ro.getText().toString());
                intent.putExtra("column","rbasement");
                intent.putExtra("tag","BASEMENT");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        environmental_issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.environmental_issuesValues);
                intent.putExtra("heading",environmental_issues.getText().toString());
                intent.putExtra("column","renvironmentalissues");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","ENVIRONMENTAL_ISSUES");
                intent.putExtra("dbTable",INTERIOR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });




        return root;
    }

    private void BookMarkForm() {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.BOOKMARK_FORM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        if(!response.equals("0")){
                            getActivity().finish();
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
                final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs",MODE_PRIVATE);

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id);
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("user_id", pref.getString("user_id", null));
                params.put("form_step", "9");



                return params;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
    public void InteriorSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Interior ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_INTERIOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AppliancesScreenFragment()).addToBackStack("appliances").commit();

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


                Cursor cursor = database.getTable(INTERIOR_TABLE,StructureScreensActivity.template_id);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
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

                int isAnyChecked = 0;
                for(int count=6;count<=19;count++)
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

                int total = 14 - isAnyChecked;
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

    public void getInterior() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(INTERIOR_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);
                            isApplicable=object.getString("is_applicable");
                            //  item= (MenuItem)(R.id.applicable);

                            item=StructureScreensActivity.menu.findItem(R.id.applicable);
                            if(isApplicable.equals("0")){

                                item.setChecked(true);
                            }else {
                                item.setChecked(false);
                            }

                            database.insertEntry("wall_cieling",object.getString("wall_cieling"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("floors",object.getString("floors"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("windows",object.getString("windows"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("doors",object.getString("doors"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("observation",object.getString("observation"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rwalls_ceiling",object.getString("rwalls_ceiling"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rfloors",object.getString("rfloors"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rwindows",object.getString("rwindows"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rdoors",object.getString("rdoors"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rcounters_cabinets",object.getString("rcounters_cabinets"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rskylights",object.getString("rskylights"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rstairways",object.getString("rstairways"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rbasement",object.getString("rbasement"),INTERIOR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("renvironmentalissues",object.getString("renvironmentalissues"),INTERIOR_TABLE,StructureScreensActivity.template_id);


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


                Cursor cursor = database.getTable(INTERIOR_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", INTERIOR_TABLE);


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
    void addDetail() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.savetemp, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        // intializing variables
        final EditText tmpName = (EditText) dialogView.findViewById(R.id.tmpName);
        final Button save = (Button) dialogView.findViewById(R.id.Savenotemp);
        final CheckBox isdefault = (CheckBox) dialogView.findViewById(R.id.checkDefault);


      //  isdefault.setVisibility(View.GONE);
        b = dialogBuilder.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tmpName.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Template name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    b.dismiss();
                    if(isdefault.isChecked())
                    {
                        saveNoTemp(tmpName.getText().toString(),"1");
                    }
                    else
                    {
                        saveNoTemp(tmpName.getText().toString(),"0");
                    }

                }

            }
        });

        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        b.show();



    }
    private void SetChecked(final String s) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.NOT_APPLICABLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        if(!response.equals("0")){

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
                final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs",MODE_PRIVATE);

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id);
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("table_name",INTERIOR_TABLE);
                params.put("isApplicable", s);



                return params;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
    public void saveNoTemp(final String txt, final String check) {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Saving Template ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SAVE_NOTEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        StructureScreensActivity.is_saved=true;

                        String resp[];

                        resp = response.split("%");


                        StructureScreensActivity.inspectionID = resp[1];
                        StructureScreensActivity.template_id = resp[0];



                        database.updateIds();





                        sp=getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                        edit=sp.edit();
                        edit.putBoolean("StructureScreenFragment",true);
                        edit.apply();
                        Toast.makeText(getContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
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



                Map<String, String> params = new HashMap<>();

                SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String  user = pref.getString("user_id","");

                params.put("name", txt);
                params.put("isDefault", String.valueOf(check));
                params.put("parah", TemplatesFragment.myparah_no_temp);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("added_by", user);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.cancel_btn){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Close Form")
                    .setMessage("Are you sure you want to Close Form !!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent=new Intent(getActivity(),LandingScreen.class);
                            intent.putExtra("activityName", "templateList");
                            startActivity(intent);*/
                            getActivity().finish();



                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Do whatever you want to do
            return true;
        }
        if(id == R.id.book_mark_btn){
            new AlertDialog.Builder(getActivity())
                    .setTitle("BookMark")
                    .setMessage("BookMark And Exit!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           /* Intent intent=new Intent(getActivity(),LandingScreen.class);
                            intent.putExtra("activityName", "templateList");
                            startActivity(intent);*/
                            BookMarkForm();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Do whatever you want to do
            return true;
        }
        if(id == R.id.applicable){
            if(item.isChecked()){
                item.setChecked(false);
                SetChecked("1");
            }else {
                item.setChecked(true);
                SetChecked("0");
            }

            //Do whatever you want to do
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
