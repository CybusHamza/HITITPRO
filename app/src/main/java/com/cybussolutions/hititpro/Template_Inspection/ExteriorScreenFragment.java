package com.cybussolutions.hititpro.Template_Inspection;


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


public class ExteriorScreenFragment extends BaseFragment {

    View root;
    Button next, back,save;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;
    TextView title;

    AlertDialog b;
    String isApplicable;
    MenuItem item;
    private static final String EXTERIROR_TABLE = "exterior";

    Button  exteriorWallCoveringButton, exteriorEavesButton, exteriorDoorsButton, exteriorWindowButton, exteriorDrivewaysButton, exteriorWalksButton,
            exteriorPorchButton, exteriorOverheadButton, exteriorSurfaceButton, exteriorTrainingButton, exteriorFencingButton,
            exteriorObservationButton, roWallsButton, roEavesButton, roDoorsButton, roGarageButton, roPorchesButton,
            roDrivewaysButton, roStepsButton, roDeckButton, roDrainangeButton, roLandscapButton, roRetainingButton,
            roFencingButton;



    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_exterior_screen, container, false);

        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Exterior");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        save = (Button) root.findViewById(R.id.save);
        title = (TextView) root.findViewById(R.id.title);
        if(!(StructureScreensActivity.is_notemplate.equals("true")))
        {
            save.setVisibility(View.GONE);
            title.setText("    Page 3 of Page 11");
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
                ExteriorSync();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RoofingScreenFragment()).commit();
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RoofingScreenFragment()).commit();

            }
        });


        database= new Database(getActivity());

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        exteriorWallCoveringButton = (Button) root.findViewById(R.id.exterior_wall_covering_Button);
        exteriorEavesButton = (Button) root.findViewById(R.id.exterior_eaves_Button);
        exteriorDoorsButton = (Button) root.findViewById(R.id.exterior_doors_Button);
        exteriorWindowButton = (Button) root.findViewById(R.id.exterior_windows_Button);
        exteriorDrivewaysButton = (Button) root.findViewById(R.id.exterior_driveways_Button);
        exteriorWalksButton = (Button) root.findViewById(R.id.exterior_walks_Button);
        exteriorPorchButton = (Button) root.findViewById(R.id.exterior_porch_Button);
        exteriorOverheadButton = (Button) root.findViewById(R.id.exterior_overhead_Button);
        exteriorSurfaceButton = (Button) root.findViewById(R.id.exterior_surface_Button);
        exteriorTrainingButton = (Button) root.findViewById(R.id.exterior_training_Button);
        exteriorFencingButton = (Button) root.findViewById(R.id.exterior_fencing_Button);
        exteriorObservationButton = (Button) root.findViewById(R.id.exterior_observations_Button);
        roWallsButton = (Button) root.findViewById(R.id.ro_exterior_walls_Button);
        roEavesButton = (Button) root.findViewById(R.id.ro_eaves_Button);
        roDoorsButton = (Button) root.findViewById(R.id.ro_doors_Button);
        roGarageButton = (Button) root.findViewById(R.id.ro_garage_Button);
        roPorchesButton = (Button) root.findViewById(R.id.ro_porches_Button);
        roDrivewaysButton = (Button) root.findViewById(R.id.ro_driveways_Button);
        roStepsButton = (Button) root.findViewById(R.id.ro_steps_Button);
        roDeckButton = (Button) root.findViewById(R.id.ro_deck_Button);
        roDrainangeButton = (Button) root.findViewById(R.id.ro_drainage_Button);
        roLandscapButton = (Button) root.findViewById(R.id.ro_landscaping_Button);
        roRetainingButton = (Button) root.findViewById(R.id.ro_retaining_Button);
        roFencingButton = (Button) root.findViewById(R.id.ro_fencing_Button);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exterior");
        /*((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);*/
        setHasOptionsMenu(true);



        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getExterior();

        }


        exteriorWallCoveringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorWallCoveringButtonValues);
                intent.putExtra("heading",exteriorWallCoveringButton.getText().toString());
                intent.putExtra("column","wallcovering");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorEavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorEavesButtonValues);
                intent.putExtra("heading",exteriorEavesButton.getText().toString());
                intent.putExtra("column","eaves_soffits_fascia");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorDoorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorDoorsButtonValues);
                intent.putExtra("heading",exteriorDoorsButton.getText().toString());
                intent.putExtra("column","exteriordoors");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorWindowButtonValues);
                intent.putExtra("heading",exteriorWindowButton.getText().toString());
                intent.putExtra("column","windows_doorframes_trim");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorDrivewaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorDrivewaysButtonValues);
                intent.putExtra("heading",exteriorDrivewaysButton.getText().toString());
                intent.putExtra("column","entry_driveways");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorWalksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorWalksButtonValues);
                intent.putExtra("heading",exteriorWalksButton.getText().toString());
                intent.putExtra("column","entrywalk_patios");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorPorchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorPorchButtonValues);
                intent.putExtra("heading",exteriorPorchButton.getText().toString());
                intent.putExtra("column","porch_decks_steps_railings");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorOverheadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorOverheadButtonValues);
                intent.putExtra("heading",exteriorOverheadButton.getText().toString());
                intent.putExtra("column","overheadgaragedoors");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorSurfaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorSurfaceButtonValues);
                intent.putExtra("heading",exteriorSurfaceButton.getText().toString());
                intent.putExtra("column","surfacedrainage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorTrainingButtonValues);
                intent.putExtra("heading",exteriorTrainingButton.getText().toString());
                intent.putExtra("column","retainingwalls");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorFencingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorFencingButtonvalues);
                intent.putExtra("heading",exteriorFencingButton.getText().toString());
                intent.putExtra("column","fencing");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        exteriorObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.exteriorObservationButtonValues);
                intent.putExtra("heading",exteriorObservationButton.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","observation");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        roWallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roWallsButtonValues);
                intent.putExtra("heading",roWallsButton.getText().toString());
                intent.putExtra("column","rexteriorwalls");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","EXTERIOR_WALLS");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roEavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roEavesButtonValues);
                intent.putExtra("heading",roEavesButton.getText().toString());
                intent.putExtra("column","reaves");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","EAVES");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roDoorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDoorsButtonValues);
                intent.putExtra("heading",roDoorsButton.getText().toString());
                intent.putExtra("column","rexteriordoors_windows");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","EXTERIOR_DOORS_WINDOW");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roGarageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roGarageButtonValues);
                intent.putExtra("heading",roGarageButton.getText().toString());
                intent.putExtra("column","rgrage");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","GARAGE");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roPorchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roPorchesButtonValues);
                intent.putExtra("heading",roPorchesButton.getText().toString());
                intent.putExtra("column","rporches");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","PORCHES");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roDrivewaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDrivewaysButtonValues);
                intent.putExtra("heading",roDrivewaysButton.getText().toString());
                intent.putExtra("column","rdriveway");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","DRIVEWAY");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roStepsButtonValues);
                intent.putExtra("heading",roStepsButton.getText().toString());
                intent.putExtra("column","rexteriorsteps_walkways");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","EXTERIOR_STEPS_WALKWAYS");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDeckButtonValues);
                intent.putExtra("heading",roDeckButton.getText().toString());
                intent.putExtra("column","rdeck");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","DECK");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roDrainangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roDrainangeButtonValues);
                intent.putExtra("heading",roDrainangeButton.getText().toString());
                intent.putExtra("column","rlotdrainage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","LOT_DRAINAGE");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roLandscapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roLandscapButtonValues);
                intent.putExtra("heading",roLandscapButton.getText().toString());
                intent.putExtra("column","rlandscaping");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","LANDSCAPING");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        roRetainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roRetainingButtonValues);
                intent.putExtra("heading",roRetainingButton.getText().toString());
                intent.putExtra("column","retainwall");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","RETAINING_WALL");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFencingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.roFencingButtonValues);
                intent.putExtra("heading",roFencingButton.getText().toString());
                intent.putExtra("column","rfencing");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("tag","FENCING1");
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
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
                params.put("form_step", "3");



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
    public void ExteriorSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Exterior ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_EXTERIOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ElectricalScreenFragment()).addToBackStack("electrical").commit();

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


                Cursor cursor = database.getTable(EXTERIROR_TABLE,StructureScreensActivity.template_id);
                cursor.moveToFirst();



                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("wallcovering", cursor.getString(6));
                    params.put("eaves_soffits_fascia", cursor.getString(7));
                    params.put("exteriordoors", cursor.getString(8));
                    params.put("windows_doorframes_trim", cursor.getString(9));
                    params.put("entry_driveways", cursor.getString(10));
                    params.put("entrywalk_patios", cursor.getString(11));
                    params.put("porch_decks_steps_railings", cursor.getString(12));
                    params.put("overheadgaragedoors", cursor.getString(13));
                    params.put("surfacedrainage", cursor.getString(14));
                    params.put("retainingwalls", cursor.getString(15));
                    params.put("fencing", cursor.getString(16));
                    params.put("observations", cursor.getString(17));
                    params.put("rexteriorwalls", cursor.getString(18));
                    params.put("reaves", cursor.getString(19));
                    params.put("rexteriordoors_windows", cursor.getString(20));
                    params.put("rgrage", cursor.getString(21));
                    params.put("rporches", cursor.getString(22));
                    params.put("rdriveway", cursor.getString(23));
                    params.put("rexteriorsteps_walkways", cursor.getString(24));
                    params.put("rdeck", cursor.getString(25));
                    params.put("rlotdrainage", cursor.getString(26));
                    params.put("rlandscaping", cursor.getString(27));
                    params.put("retainwall", cursor.getString(28));
                    params.put("rfencing", cursor.getString(29));
                }

                int isAnyChecked = 0;
                for(int count=6;count<=29;count++)
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

                int total = 24 - isAnyChecked;
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



    public void getExterior() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(EXTERIROR_TABLE);


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

                            database.insertEntry("wallcovering",object.getString("wallcovering"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("eaves_soffits_fascia",object.getString("eaves_soffits_fascia"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("exteriordoors",object.getString("exteriordoors"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("windows_doorframes_trim",object.getString("windows_doorframes_trim"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("entry_driveways",object.getString("entry_driveways"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("entrywalk_patios",object.getString("entrywalk_patios"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("porch_decks_steps_railings",object.getString("porch_decks_steps_railings"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("overheadgaragedoors",object.getString("overheadgaragedoors"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("surfacedrainage",object.getString("surfacedrainage"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("retainingwalls",object.getString("retainingwalls"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("fencing",object.getString("fencing"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("observations",object.getString("observations"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rexteriorwalls",object.getString("rexteriorwalls"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("reaves",object.getString("reaves"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rexteriordoors_windows",object.getString("rexteriordoors_windows"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rgrage",object.getString("rgrage"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rporches",object.getString("rporches"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rdriveway",object.getString("rdriveway"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rexteriorsteps_walkways",object.getString("rexteriorsteps_walkways"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rdeck",object.getString("rdeck"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rlotdrainage",object.getString("rlotdrainage"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rlandscaping",object.getString("rlandscaping"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("retainwall",object.getString("retainwall"),EXTERIROR_TABLE,StructureScreensActivity.template_id);
                            database.insertEntry("rfencing",object.getString("rfencing"),EXTERIROR_TABLE,StructureScreensActivity.template_id);


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


                Cursor cursor = database.getTable(EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("tempid",StructureScreensActivity.template_id );
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("temp_name", EXTERIROR_TABLE);


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
                params.put("table_name",EXTERIROR_TABLE);
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
