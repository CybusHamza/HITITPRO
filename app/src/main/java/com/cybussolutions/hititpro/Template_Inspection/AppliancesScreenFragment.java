package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
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


public class AppliancesScreenFragment extends BaseFragment {

    View root;
    Button next, back,save;

    TextView title;
    AlertDialog b;
    Button appliances_tested,laundry_facility,other_components_tested,appliance_observations,electric_range,
    gas_range,built_in_electric_oven,electric_cooktop,gas_cooktop,microwave_oven,dishwasher,waste_disposer,refrigerator,
    wine_cooler,trash_compactor,clothes_washer,clothes_dryer,cooktop_exhaust_fan,central_vacuum,door_bell;


    private static final String APPLIANCE_TABLE = "appliance";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;
    SharedPreferences sp;
    SharedPreferences.Editor edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_appliances_screen, container, false);
        ///////////set title of main screens/////////////////
        sp=getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Appliances");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);
        title = (TextView) root.findViewById(R.id.title);
        save = (Button) root.findViewById(R.id.save);
        if(!(StructureScreensActivity.is_notemplate.equals("true")))
        {
            save.setVisibility(View.GONE);
            title.setText("    Page 10 of Page 11");
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
               AppliancesSync();

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


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Appliances");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);

        appliances_tested = (Button) root.findViewById(R.id.appliances_tested);
        laundry_facility = (Button) root.findViewById(R.id.laundry_facility);
        other_components_tested = (Button) root.findViewById(R.id.other_components_tested);
        appliance_observations = (Button) root.findViewById(R.id.appliance_observations);
        electric_range = (Button) root.findViewById(R.id.electric_range);
        gas_range = (Button) root.findViewById(R.id.gas_range);
        built_in_electric_oven = (Button) root.findViewById(R.id.built_in_electric_oven);
        electric_cooktop = (Button) root.findViewById(R.id.electric_cooktop);
        gas_cooktop = (Button) root.findViewById(R.id.gas_cooktop);
        microwave_oven = (Button) root.findViewById(R.id.microwave_oven);
        dishwasher = (Button) root.findViewById(R.id.dishwasher);
        waste_disposer = (Button) root.findViewById(R.id.waste_disposer);
        refrigerator = (Button) root.findViewById(R.id.refrigerator);
        wine_cooler = (Button) root.findViewById(R.id.wine_cooler);
        trash_compactor = (Button) root.findViewById(R.id.trash_compactor);
        clothes_washer = (Button) root.findViewById(R.id.clothes_washer);
        clothes_dryer = (Button) root.findViewById(R.id.clothes_dryer);
        cooktop_exhaust_fan = (Button) root.findViewById(R.id.cooktop_exhaust_fan);
        central_vacuum = (Button) root.findViewById(R.id.central_vacuum);
        door_bell = (Button) root.findViewById(R.id.door_bell);






        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isAppliances_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getInsulation();

        }



        appliances_tested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.appliances_testedValues);
                intent.putExtra("heading",appliances_tested.getText().toString());
                intent.putExtra("column","appliancestested");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        laundry_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.laundry_facilityValues);
                intent.putExtra("heading",laundry_facility.getText().toString());
                intent.putExtra("column","laundryfacility");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        other_components_tested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.other_components_testedValues);
                intent.putExtra("heading",other_components_tested.getText().toString());
                intent.putExtra("column","othercomponentstested");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("fromAddapter","false");
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        appliance_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.putBoolean("addObservationButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_Structure_Screens.class);
                intent.putExtra("items",StructureScreensActivity.appliance_observationsValues);
                intent.putExtra("heading",appliance_observations.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        electric_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electric_rangeValues);
                intent.putExtra("heading",electric_range.getText().toString());
                intent.putExtra("column","relectricrange");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        gas_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.gas_rangeValues);
                intent.putExtra("heading",gas_range.getText().toString());
                intent.putExtra("column","rgasrange");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        built_in_electric_oven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.built_in_electric_ovenValues);
                intent.putExtra("heading",built_in_electric_oven.getText().toString());
                intent.putExtra("column","rbuiltinelectricoven");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        electric_cooktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.electric_cooktopValues);
                intent.putExtra("heading",electric_cooktop.getText().toString());
                intent.putExtra("column","relectriccooktop");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });


        gas_cooktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.gas_cooktopValues);
                intent.putExtra("heading",gas_cooktop.getText().toString());
                intent.putExtra("column","rgascooktop");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });


        microwave_oven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.microwave_ovenValues);
                intent.putExtra("heading",microwave_oven.getText().toString());
                intent.putExtra("column","rmicrowaveoven");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        dishwasher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.dishwasherValues);
                intent.putExtra("heading",dishwasher.getText().toString());
                intent.putExtra("column","rdishwasher");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        waste_disposer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.waste_disposerValues);
                intent.putExtra("heading",waste_disposer.getText().toString());
                intent.putExtra("column","rwastedisposer");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        refrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.refrigeratorValues);
                intent.putExtra("heading",refrigerator.getText().toString());
                intent.putExtra("column","rrefrigerator");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        wine_cooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.wine_coolerValues);
                intent.putExtra("heading",wine_cooler.getText().toString());
                intent.putExtra("column","rwinecooler");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        trash_compactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.trash_compactorValues);
                intent.putExtra("heading",trash_compactor.getText().toString());
                intent.putExtra("column","rtrashcompactor");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        clothes_washer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.clothes_washerValues);
                intent.putExtra("heading",clothes_washer.getText().toString());
                intent.putExtra("column","rclotheswasher");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        clothes_dryer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.clothes_dryerValues);
                intent.putExtra("heading",clothes_dryer.getText().toString());
                intent.putExtra("column","rclothesdryer");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        cooktop_exhaust_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.cooktop_exhaust_fanValues);
                intent.putExtra("heading",cooktop_exhaust_fan.getText().toString());
                intent.putExtra("column","rcooktopexhaustfan");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });
        central_vacuum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.central_vacuumValues);
                intent.putExtra("heading",central_vacuum.getText().toString());
                intent.putExtra("column","rcentralvacuum");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.template_id);
                startActivity(intent);
            }
        });

        door_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.putBoolean("addObservationButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",StructureScreensActivity.door_bellValues);
                intent.putExtra("heading",door_bell.getText().toString());
                intent.putExtra("column","rdoorbell");
                intent.putExtra("fromAddapter","false");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });




        return root;
    }

    public void AppliancesSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Appliances ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_APPLIANCES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new FirePlaceScreenFragment()).addToBackStack("fireplaces").commit();

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


                Cursor cursor = database.getTable(APPLIANCE_TABLE,StructureScreensActivity.template_id);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("appliancestested", cursor.getString(6));
                    params.put("laundryfacility", cursor.getString(7));
                    params.put("othercomponentstested", cursor.getString(8));
                    params.put("observations", cursor.getString(9));
                    params.put("relectricrange", cursor.getString(10));
                    params.put("rgasrange", cursor.getString(11));
                    params.put("rbuiltinelectricoven", cursor.getString(12));
                    params.put("relectriccooktop", cursor.getString(13));
                    params.put("rgascooktop", cursor.getString(14));
                    params.put("rmicrowaveoven", cursor.getString(15));
                    params.put("rdishwasher", cursor.getString(16));
                    params.put("rwastedisposer", cursor.getString(17));
                    params.put("rrefrigerator", cursor.getString(18));
                    params.put("rwinecooler", cursor.getString(19));
                    params.put("rtrashcompactor", cursor.getString(20));
                    params.put("rclotheswasher", cursor.getString(21));
                    params.put("rclothesdryer", cursor.getString(22));
                    params.put("rcooktopexhaustfan", cursor.getString(23));
                    params.put("rcentralvacuum", cursor.getString(24));
                    params.put("rdoorbell", cursor.getString(25));

                }

                int isAnyChecked = 0;
                for(int count=6;count<=25;count++)
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



    private void getInsulation() {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        database.clearTable(APPLIANCE_TABLE);


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject object = jsonArray.getJSONObject(0);


                            database.insertEntry("appliancestested", object.getString("appliancestested"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("laundryfacility", object.getString("laundryfacility"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("othercomponentstested", object.getString("othercomponentstested"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("observations", object.getString("observations"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("relectricrange", object.getString("relectricrange"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rgasrange", object.getString("rgasrange"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rbuiltinelectricoven", object.getString("rbuiltinelectricoven"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("relectriccooktop", object.getString("relectriccooktop"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rgascooktop", object.getString("rgascooktop"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rmicrowaveoven", object.getString("rmicrowaveoven"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rdishwasher", object.getString("rdishwasher"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rwastedisposer", object.getString("rwastedisposer"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rrefrigerator", object.getString("rrefrigerator"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rwinecooler", object.getString("rwinecooler"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rtrashcompactor", object.getString("rtrashcompactor"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rclotheswasher", object.getString("rclotheswasher"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rclothesdryer", object.getString("rclothesdryer"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rcooktopexhaustfan", object.getString("rcooktopexhaustfan"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rcentralvacuum", object.getString("rcentralvacuum"), APPLIANCE_TABLE, StructureScreensActivity.template_id);
                            database.insertEntry("rdoorbell", object.getString("rdoorbell"), APPLIANCE_TABLE, StructureScreensActivity.template_id);

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


                Cursor cursor = database.getTable(APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("client_id", StructureScreensActivity.client_id);
                params.put("tempid", StructureScreensActivity.template_id);
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("temp_name", APPLIANCE_TABLE);


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
                    b.dismiss();
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


}
