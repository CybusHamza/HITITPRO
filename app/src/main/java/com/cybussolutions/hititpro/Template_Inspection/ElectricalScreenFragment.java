package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
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
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;


public class ElectricalScreenFragment extends BaseFragment {

    View root;
    Button next, back,save;
    TextView title;

    AlertDialog b;
    Button electricalSizeButton, electricalDropButton, electricalConductorsButton, electricalDisconnectButton, electricalGroundingButton,
            electricalPanelButton, electricalSubpanelButton, electricalWiringButton, electricalMethodButton,
            electricalSwitchesButton, electricalgfciButton, electricalSmokeButton, electricalObservationButton,
            roEntranceButton, roMainpanelButton, roSubpanelButton, roDistributionButton, roOutletButton, roSwitchesButton,
            roCeilingButton, roDetectorButton;


    private static final String ELECTRICAL_TABLE = "electrical";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_electrical_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Electrical");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        title = (TextView) root.findViewById(R.id.title);
        save = (Button) root.findViewById(R.id.save);
        if(!(StructureScreensActivity.is_notemplate.equals("true")))
        {
            save.setVisibility(View.GONE);
            title.setText("    Page 4 of Page 11");
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
                ElectricalSync();

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


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Electrical");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isElectrical_populated","");


        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getElectrical();

        }


       

        electricalSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalSizeButtonValues);
                intent.putExtra("heading",electricalSizeButton.getText().toString());
                intent.putExtra("column","sizeofservice");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalDropButtonValues);
                intent.putExtra("heading",electricalDropButton.getText().toString());
                intent.putExtra("column","servicedrop");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalConductorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalConductorsButtonValues);
                intent.putExtra("heading",electricalConductorsButton.getText().toString());
                intent.putExtra("column","entranceconductors");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalDisconnectButtonValues);
                intent.putExtra("heading",electricalDisconnectButton.getText().toString());
                intent.putExtra("column","maindisconnect");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalGroundingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalGroundingButtonValues);
                intent.putExtra("heading",electricalGroundingButton.getText().toString());
                intent.putExtra("column","grounding");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalPanelButtonValues);
                intent.putExtra("heading",electricalPanelButton.getText().toString());
                intent.putExtra("column","servicepanel");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalSubpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalSubpanelButtonValues);
                intent.putExtra("heading",electricalSubpanelButton.getText().toString());
                intent.putExtra("column","sub_panel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalWiringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalWiringButtonValues);
                intent.putExtra("heading",electricalWiringButton.getText().toString());
                intent.putExtra("column","wiring");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalMethodButtonValues);
                intent.putExtra("heading",electricalMethodButton.getText().toString());
                intent.putExtra("column","wiring_method");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalSwitchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalSwitchesButtonValues);
                intent.putExtra("heading",electricalSwitchesButton.getText().toString());
                intent.putExtra("column","switches_receptacles");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });


        electricalgfciButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalgfciButtonValues);
                intent.putExtra("heading",electricalgfciButton.getText().toString());
                intent.putExtra("column","gfci");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalSmokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalSmokeButtonValues);
                intent.putExtra("heading",electricalSmokeButton.getText().toString());
                intent.putExtra("column","smoke_codetector");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        electricalObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electricalObservationButtonValues);
                intent.putExtra("heading",electricalObservationButton.getText().toString());
                intent.putExtra("column","observation");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roEntranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roEntranceButtonValues);
                intent.putExtra("heading",roEntranceButton.getText().toString());
                intent.putExtra("column","serviceentrance");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });


        roMainpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roMainpanelButtonValues);
                intent.putExtra("heading",roMainpanelButton.getText().toString());
                intent.putExtra("column","mainpanel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roSubpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roSubpanelButtonValues);
                intent.putExtra("heading",roSubpanelButton.getText().toString());
                intent.putExtra("column","subpanel");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roDistributionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDistributionButtonValues);
                intent.putExtra("heading",roDistributionButton.getText().toString());
                intent.putExtra("column","distribution");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roOutletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roOutletButtonValues);
                intent.putExtra("heading",roOutletButton.getText().toString());
                intent.putExtra("column","outlets");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roSwitchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roSwitchesButtonValues);
                intent.putExtra("heading",roSwitchesButton.getText().toString());
                intent.putExtra("column","switches");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roCeilingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roCeilingButtonValues);
                intent.putExtra("heading",roCeilingButton.getText().toString());
                intent.putExtra("column","lights_ceiling_fans");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roDetectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDetectorButtonValues);
                intent.putExtra("heading",roDetectorButton.getText().toString());
                intent.putExtra("column","smoke_co_detectors");
                intent.putExtra("dbTable",ELECTRICAL_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
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

                            database.insertEntry("sizeofservice", object.getString("sizeofservice"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("servicedrop", object.getString("servicedrop"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("entranceconductors", object.getString("entranceconductors"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("maindisconnect", object.getString("maindisconnect"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("grounding", object.getString("grounding"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("servicepanel", object.getString("servicepanel"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("sub_panel", object.getString("sub_panel"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("wiring", object.getString("wiring") ,ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("wiring_method", object.getString("wiring_method"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("switches_receptacles", object.getString("switches_receptacles"),ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("gfci", object.getString("gfci"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("smoke_codetector", object.getString("smoke_codetector"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("observation", object.getString("observation"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("serviceentrance", object.getString("serviceentrance"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("mainpanel", object.getString("mainpanel"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("subpanel", object.getString("subpanel"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("distribution", object.getString("distribution"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("outlets", object.getString("outlets"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("switches", object.getString("switches"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("lights_ceiling_fans", object.getString("lights_ceiling_fans"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("smoke_co_detectors", object.getString("smoke_co_detectors"), ELECTRICAL_TABLE, StructureScreensActivity.template_id);


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
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HeatingScreenFragment()).addToBackStack("heating").commit();

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


                Cursor cursor = database.getTable(ELECTRICAL_TABLE,StructureScreensActivity.template_id);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
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

                int isAnyChecked = 0;
                for(int count=6;count<=26;count++)
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

                int total = 21 - isAnyChecked;
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
                        edit.commit();
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
