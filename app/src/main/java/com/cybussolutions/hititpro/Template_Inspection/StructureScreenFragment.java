package com.cybussolutions.hititpro.Template_Inspection;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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

import static android.content.Context.MODE_PRIVATE;


public class StructureScreenFragment extends BaseFragment {

    View root;
    Button next,back,save;
    private static final String PORTFOLIO_TABLE = "portfolio";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;
    TextView title;
    ArrayList<String> datArray = new ArrayList<>();
    FragmentManager manager;
    AlertDialog b;
    int isAnyChecked = 0;


    Button foundationButton, columnsButton, floorStructureButton,wallStructureSpinner, ceilingStructureButton, roofStructureButton,
            structureObservationsButton, roFoundationButton, roCrawlSpacesButton, roFloorsButton, roExteriorWallsButton, roRoofButton;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        database= new Database(getActivity());

        root = inflater.inflate(R.layout.fragment_structure_screen, container, false);



        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        save = (Button) root.findViewById(R.id.save);
        title = (TextView) root.findViewById(R.id.title);

        if(!(StructureScreensActivity.is_notemplate.equals("true")))
        {
            save.setVisibility(View.GONE);
            title.setText("    Page 1 of Page 11");
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

                    StructureSync();


            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Close Inspection")
                            .setMessage("Are you sure you want to Close this Inspection? This Will remove all un synced data !!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    getActivity().getSupportFragmentManager().popBackStack();

                                    getActivity().finish();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                } else {
                    getActivity().finish();

               }
            }
        });

        try {
            database = database.open();
            ///////////set title of main screens/////////////////
            sp=getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
            edit=sp.edit();
            edit.putString("main_screen","Structure");
            edit.commit();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Structure");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);




        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isStructure_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getStructure();

        }


        // inserting into local database for pre population of data

        foundationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.apply();

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.foundationSpinnerValues);
                intent.putExtra("heading",foundationButton.getText().toString());
                intent.putExtra("column","foundation");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);

            }
        });

        columnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.columnsSpinnerValues);
                intent.putExtra("heading",columnsButton.getText().toString());
                intent.putExtra("column","columns");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        floorStructureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.floorStructureSpinnerValues);
                intent.putExtra("heading",floorStructureButton.getText().toString());
                intent.putExtra("column","floor_structure");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        wallStructureSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.wallStructureSpinnerValues);
                intent.putExtra("heading",wallStructureSpinner.getText().toString());
                intent.putExtra("column","wall_structure");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        ceilingStructureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.ceilingStructureSpinnerValues);
                intent.putExtra("heading",ceilingStructureButton.getText().toString());
                intent.putExtra("column","celling_struture");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roofStructureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roofStructureSpinnerValues);
                intent.putExtra("heading",columnsButton.getText().toString());
                intent.putExtra("column","roof_structure");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        structureObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.structureObservationsSpinnerValues);
                intent.putExtra("heading",structureObservationsButton.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFoundationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFoundationSpinnerValues);
                intent.putExtra("heading",roFoundationButton.getText().toString());
                intent.putExtra("column","recomnd_foundation");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roCrawlSpacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roCrawlSpacesSpinnerValues);
                intent.putExtra("heading",roCrawlSpacesButton.getText().toString());
                intent.putExtra("column","crawl_space");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roFloorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFloorsValues);
                intent.putExtra("heading",roFloorsButton.getText().toString());
                intent.putExtra("column","recomnd_floor");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roExteriorWallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roExteriorWallsValues);
                intent.putExtra("heading",roExteriorWallsButton.getText().toString());
                intent.putExtra("column","exterior_wall");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roRoofButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roRoofValues);
                intent.putExtra("heading",roRoofButton.getText().toString());
                intent.putExtra("column","roof");
                intent.putExtra("dbTable",PORTFOLIO_TABLE);
                intent.putExtra("fromAddapter","false");
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

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RoofingScreenFragment()).addToBackStack("roofing").commit();


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

        isdefault.setVisibility(View.GONE);

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
                    if(isdefault.isChecked())
                    {
                        saveNoTemp(tmpName.getText().toString(),true);
                    }
                    else
                    {
                        saveNoTemp(tmpName.getText().toString(),false);
                    }

                }

            }
        });

        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        b.show();



    }

    public void saveNoTemp(final String txt, final boolean check) {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Saving Template ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SAVE_NOTEMP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        StructureScreensActivity.is_saved=true;
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


}
