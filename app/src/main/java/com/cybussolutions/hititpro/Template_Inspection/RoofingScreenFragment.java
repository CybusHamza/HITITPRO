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


public class RoofingScreenFragment extends BaseFragment {

    View root;
    Button next, back;
    Database database;
    private static final String ROOFING_TABLE = "roofing";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    Button roofCoveringButton, roofFlashingButton, chimneysButton, roofDrainageButton, skyLightsButton, methodInspectionButton,
            roofingObservationsButton, roSloppedButton, roFlatButton, roFlashingButton, roChimneyButton, roGutterDownspoutsButton;

    String[] roofCoveringButtonValues, roofFlashingButtonValues, chimneysButtonValues, roofDrainageButtonValues, skyLightsButtonValues, methodInspectionButtonValues,
            roofingObservationsButtonValues, roSloppedButtonValues, roFlatButtonValues, roFlashingButtonValues, roChimneyButtonValues, roGutterDownspoutsButtonValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_roofing_screen, container, false);

        database= new Database(getActivity());

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StructureSync();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ExteriorScreenFragment()).addToBackStack("exterior").commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new StructureScreenFragment()).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        roofCoveringButtonValues = new String[]{"Asphalt Shingle%0", "Metal%0", "Roll Roofing%0"};
        roofFlashingButtonValues = new String[]{"Metal%0", "Asphalt%0", "Not Visible%0"};
        chimneysButtonValues = new String[]{"Masonry%0", "Metal Below Siding%0", "Metal%0"};
        roofDrainageButtonValues = new String[]{"Aluminum%0", "Galvanized Steel%0", "Plastic%0", "None%0", "Downspouts Discharge Above Grade%0", "Downspouts Discharge Below Grade%0", "Downspouts Discharge Above and Below Grade%0"};
        skyLightsButtonValues = new String[]{"Curb-Type%0", "Bubble-Type%0", "None%0"};
        methodInspectionButtonValues = new String[]{"Walked on Roof%0", "Viewed From Ladder at Eave%0", "Viewed with Binoculars%0", "Other%0"};
        roofingObservationsButtonValues = new String[]{"NEWER%0", "ABOVE AVERAGE%0", "AVERAGE%0", "BELOW AVERAGE%0"};
        roSloppedButtonValues = new String[]{"Minor Repairs Only%0", "Replacement%0", "Near End of Life Cycle%0", "Moss%0", "Algae%0", "Prior Repairs%0", "Nail Heads Exposed%0", "Roof Openings%0", "Remove Debris%0", "Shingles Tear Easily%0", "Granular Shortage%0", "Short Plumbing Vents%0", "Atlas Chalet Shingles%0", "Trim Branches From Roof%0"};
        roFlatButtonValues = new String[]{"Replacement Needed%0", "Near End of Life Cycle%0", "Remove Debris%0"};
        roFlashingButtonValues = new String[]{"Old-Replacement Needed%0", "Masonry/Roof Intersection Needs Flashing%0", "Rusting", "Damaged%0"};
        roChimneyButtonValues = new String[]{"Masonry Re-Pointing%0", "Masonry Spalling%0", "Masonry Out of Plumb%0", "Rain Cap/Vermin Screen Needed%0", "Rusted Metal Chimney%0", "Metal Chimney Damage%0"};
        roGutterDownspoutsButtonValues = new String[]{"Cleaning Needed%0", "Cover Gutters%0", "Gutter Damage%0", "Gutter Rusting%0", "Downspout Discharges Near House%0", "Downspout Damaged/Loose%0", "Gutters and Downspouts Needed%0"};


        roofCoveringButton = (Button) root.findViewById(R.id.roof_covering_Button);
        roofFlashingButton = (Button) root.findViewById(R.id.roof_flashing_Button);
        chimneysButton = (Button) root.findViewById(R.id.chimneys_Button);
        roofDrainageButton = (Button) root.findViewById(R.id.roof_drainage_Button);
        skyLightsButton = (Button) root.findViewById(R.id.skylights_Button);
        methodInspectionButton = (Button) root.findViewById(R.id.method_inspection_Button);
        roofingObservationsButton = (Button) root.findViewById(R.id.roofing_observations_Button);
        roSloppedButton = (Button) root.findViewById(R.id.ro_slopped_Button);
        roFlatButton = (Button) root.findViewById(R.id.ro_flat_Button);
        roFlashingButton = (Button) root.findViewById(R.id.ro_flashing_Button);
        roChimneyButton = (Button) root.findViewById(R.id.ro_chimneys_Button);
        roGutterDownspoutsButton = (Button) root.findViewById(R.id.ro_gutters_downspouts_Button);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Roofing Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isRoofing_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getRoofing();

        }
        else {
            if(!(populate.equals("true")))
            {
                database.prePopulateData("roofcovering", roofCoveringButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("roofflashing", roofFlashingButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("chimneys", chimneysButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("roofdrainage", roofDrainageButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("skylights", skyLightsButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("methodofinspection", methodInspectionButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("observations", roofingObservationsButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recommendslopedroofing", roSloppedButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("flatroofing", roFlatButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("flashing", roFlashingButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("recommendchimneys", roChimneyButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
                database.prePopulateData("guttersdownspouts", roGutterDownspoutsButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);

                // Saving string
                editor.putString("isRoofing_populated", "true");
                editor.apply();
            }
        }
        
      

        roofCoveringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roofCoveringButtonValues);
                intent.putExtra("heading",roofCoveringButton.getText().toString());
                intent.putExtra("column","roofcovering");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roofFlashingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roofFlashingButtonValues);
                intent.putExtra("heading",roofFlashingButton.getText().toString());
                intent.putExtra("column","roofflashing");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        chimneysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",chimneysButtonValues);
                intent.putExtra("heading",chimneysButton.getText().toString());
                intent.putExtra("column","chimneys");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roofDrainageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roofDrainageButtonValues);
                intent.putExtra("heading",roofDrainageButton.getText().toString());
                intent.putExtra("column","roofdrainage");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        skyLightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",skyLightsButtonValues);
                intent.putExtra("heading",skyLightsButton.getText().toString());
                intent.putExtra("column","skylights");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);

            }
        });
        methodInspectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",methodInspectionButtonValues);
                intent.putExtra("heading",methodInspectionButton.getText().toString());
                intent.putExtra("column","methodofinspection");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roofingObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roofingObservationsButtonValues);
                intent.putExtra("heading",roofingObservationsButton.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roSloppedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roSloppedButtonValues);
                intent.putExtra("heading",roSloppedButton.getText().toString());
                intent.putExtra("column","recommendslopedroofing");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFlatButtonValues);
                intent.putExtra("heading",roFlatButton.getText().toString());
                intent.putExtra("column","flatroofing");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFlashingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFlashingButtonValues);
                intent.putExtra("heading",roFlashingButton.getText().toString());
                intent.putExtra("column","flashing");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roChimneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roChimneyButtonValues);
                intent.putExtra("heading",roChimneyButton.getText().toString());
                intent.putExtra("column","recommendchimneys");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roGutterDownspoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roGutterDownspoutsButtonValues);
                intent.putExtra("heading",roGutterDownspoutsButton.getText().toString());
                intent.putExtra("column","guttersdownspouts");
                intent.putExtra("dbTable",ROOFING_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });



        return root;

    }





    public void StructureSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Roof ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_ROOf,
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


                Cursor cursor = database.getTable(ROOFING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();



                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("roofcovering", cursor.getString(6));
                    params.put("roofflashing", cursor.getString(7));
                    params.put("chimneys", cursor.getString(8));
                    params.put("roofdrainage", cursor.getString(9));
                    params.put("skylights", cursor.getString(10));
                    params.put("methodofinspection", cursor.getString(11));
                    params.put("observations", cursor.getString(12));
                    params.put("recommendslopedroofing", cursor.getString(13));
                    params.put("flatroofing", cursor.getString(14));
                    params.put("flashing", cursor.getString(15));
                    params.put("recommendchimneys", cursor.getString(16));
                    params.put("guttersdownspouts", cursor.getString(17));
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

    public void getRoofing() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(ROOFING_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);

                            database.insertEntry("roofcovering",object.getString("roofcovering"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("roofflashing",object.getString("roofflashing"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("chimneys",object.getString("chimneys"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("roofdrainage",object.getString("roofdrainage"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("skylights",object.getString("skylights"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("methodofinspection",object.getString("methodofinspection"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("observations",object.getString("observations"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("recommendslopedroofing",object.getString("recommendslopedroofing"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("flatroofing",object.getString("flatroofing"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("flashing",object.getString("flashing"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("recommendchimneys",object.getString("recommendchimneys"),ROOFING_TABLE,StructureScreensActivity.inspectionID);
                            database.insertEntry("guttersdownspouts",object.getString("guttersdownspouts"),ROOFING_TABLE,StructureScreensActivity.inspectionID);


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


                Cursor cursor = database.getTable(ROOFING_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", ROOFING_TABLE);


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
