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


public class FirePlaceScreenFragment extends BaseFragment {

    View root;
    Button save, back;

    Button fireplaces_wood_stoves,wood_coal_stoves,vents_flues_chimney,fireplace_wood_stove_observations,
    fireplace,wood_stove,fireplace_ro,wood_stove_ro;


    String []  fireplaces_wood_stovesValues,wood_coal_stovesValues,vents_flues_chimneyValues,fireplaces_wood_stoves_observationValues,
            fireplaceValues,wood_stoveValues,fireplace_roValues,wood_stove_roValues;

    private static final String FIREPLACE_TABLE = "fireplaces";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_fire_place_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","FirePlace Screen");
        edit.commit();

        save = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirePlaceSync();
                Toast.makeText(getActivity(), "Finish", Toast.LENGTH_SHORT).show();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Fire Place Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        fireplaces_wood_stoves = (Button) root.findViewById(R.id.fireplaces_wood_stoves);
        wood_coal_stoves = (Button) root.findViewById(R.id.wood_coal_stoves);
        vents_flues_chimney = (Button) root.findViewById(R.id.vents_flues_chimney);
        fireplace_wood_stove_observations = (Button) root.findViewById(R.id.fireplace_wood_stove_observations);
        fireplace = (Button) root.findViewById(R.id.fireplace);
        wood_stove = (Button) root.findViewById(R.id.wood_stove);
        fireplace_ro = (Button) root.findViewById(R.id.fireplace_ro);
        wood_stove_ro = (Button) root.findViewById(R.id.wood_stove_ro);


        fireplaces_wood_stovesValues = new String[]{"Masonry%0","Factory Insert%0","Steel Firebox%0","Direct Vent%0",
                "Vent Free%0","Gas%0"};
        wood_coal_stovesValues = new String[]{"Wood Stove%0"};
        vents_flues_chimneyValues = new String[]{"Outside Combustion Air%0","Masonry%0","Metal%0"};
        fireplaces_wood_stoves_observationValues = new String[]{"ABOVE AVERAGE%0","BELOW AVERAGE%0"};
        fireplaceValues = new String[]{"Inspection Needed%0","Firebox Repair Needed%0","Firebox Repair Needed%0","Seal Gas Line Opening%0",
                "Fireplace Key Hole Jammed%0","Damper Repairs%0","Hearth Insufficient%0"};
        wood_stoveValues = new String[]{"Old%0","Clearance From Combustibles%0","Inadequate Hearth%0","Improper Flue Pipe Configuration%0"};
        fireplace_roValues = new String[]{"Inspection Needed%0","Firebox Repair Needed%0","Firebox Repair Needed%0"
                ,"Seal Gas Line Opening%0","Fireplace Key Hole Jammed%0","Damper Repairs Needed%0","Hearth Insufficient%0"};
        wood_stove_roValues = new String[]{"Old%0","Clearance From Combustibles%0","Inadequate Hearth%0","Improper Flue Configuration%0"};



        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isFirePlace_populated","");



        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getInsulation();

        }
        else
        {
            if(!(populate.equals("true")))
            {
                database.prePopulateData("fireplaceswoodstoves", fireplaces_wood_stovesValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("woodcoalstoves", wood_coal_stovesValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("ventsflueschimney", vents_flues_chimneyValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("observations", fireplaces_wood_stoves_observationValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recommendationsfireplace", fireplaceValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recommendationswood", wood_stoveValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("fireplace_ro", fireplace_roValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("wood_stove_ro", wood_stove_roValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);

                // Saving string
                editor.putString("isFirePlace_populated", "true");
                editor.apply();
            }
        }

        
        fireplaces_wood_stoves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",fireplaces_wood_stovesValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","fireplaceswoodstoves");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        wood_coal_stoves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",wood_coal_stovesValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","woodcoalstoves");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        vents_flues_chimney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",vents_flues_chimneyValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","ventsflueschimney");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        fireplace_wood_stove_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",fireplaces_wood_stoves_observationValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        fireplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",fireplaceValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","recommendationsfireplace");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        wood_stove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",wood_stoveValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","recommendationswood");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        fireplace_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",fireplace_roValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","fireplace_ro");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        wood_stove_ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",wood_stove_roValues);
                intent.putExtra("heading",wood_stove_ro.getText().toString());
                intent.putExtra("column","wood_stove_ro");
                intent.putExtra("dbTable",FIREPLACE_TABLE);
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

                        database.clearTable(FIREPLACE_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);


                            database.insertEntry("fireplaceswoodstoves",  object.getString("fireplaceswoodstoves"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("woodcoalstoves",  object.getString("woodcoalstoves"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("ventsflueschimney",  object.getString("ventsflueschimney"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("observations",  object.getString("observations"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("recommendationsfireplace",  object.getString("recommendationsfireplace"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("recommendationswood",  object.getString("recommendationswood"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("fireplace_ro",  object.getString("fireplace_ro"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("wood_stove_ro",  object.getString("wood_stove_ro"), FIREPLACE_TABLE, StructureScreensActivity.inspectionID);

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


                Cursor cursor = database.getTable(FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("tempid", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("temp_name", FIREPLACE_TABLE);


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


    public void FirePlaceSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync FirePlace ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_FIREPLACE,
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


                Cursor cursor = database.getTable(FIREPLACE_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("fireplaceswoodstoves", cursor.getString(6));
                    params.put("woodcoalstoves", cursor.getString(7));
                    params.put("ventsflueschimney", cursor.getString(8));
                    params.put("observations", cursor.getString(9));
                    params.put("recommendationsfireplace", cursor.getString(10));
                    params.put("recommendationswood", cursor.getString(11));

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
