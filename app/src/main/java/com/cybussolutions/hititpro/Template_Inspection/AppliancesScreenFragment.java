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


public class AppliancesScreenFragment extends BaseFragment {

    View root;
    Button next, back;

    Button appliances_tested,laundry_facility,other_components_tested,appliance_observations,electric_range,
    gas_range,built_in_electric_oven,electric_cooktop,gas_cooktop,microwave_oven,dishwasher,waste_disposer,refrigerator,
    wine_cooler,trash_compactor,clothes_washer,clothes_dryer,cooktop_exhaust_fan,central_vacuum,door_bell;

    String[]  appliances_testedValues,laundry_facilityValues,other_components_testedValues,appliance_observationsValues,electric_rangeValues,
    gas_rangeValues,built_in_electric_ovenValues,electric_cooktopValues,gas_cooktopValues,microwave_ovenValues,dishwasherValues,waste_disposerValues,refrigeratorValues,
    wine_coolerValues,trash_compactorValues,clothes_washerValues,clothes_dryerValues,cooktop_exhaust_fanValues,central_vacuumValues,door_bellValues;

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
        sp=getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit=sp.edit();
        edit.putString("main_screen","Appliances Screen");
        edit.commit();

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppliancesSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new FirePlaceScreenFragment()).addToBackStack("fireplaces").commit();
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


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Appliances Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

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




        appliances_testedValues = new String[]{"Electric Range%0","Gas Range%0","Built-In Electric Oven %0","Electric Cooktop%0",
                "Gas Cooktop%0","Microwave Oven%0","Dishwasher%0","Waste Disposer%0","Refrigerator%0","Wine Cooler%0","Trash Compactor%0"
                ,"Clothes Washer%0","Clothes Dryer%0"};
        laundry_facilityValues = new String[]{"240V Circuit for Dryer%0","Gas Piping for Dryer%0","Vents to Building Exterior%0",
                "120V Circuit for Washer%0","Waste Standpipe for Washer%0"};
        other_components_testedValues = new String[]{"Kitchen Exhaust Hood%0","Cooktop Exhaust Fan%0","Central Vacuum%0"
                ,"Door Bell%0","Instant Hot Water Dispenser%0","Water Conditioning Equipment%0"};
        appliance_observationsValues = new String[]{"ALL TESTED APPLIANCES OK%0","MOST APPLIANCES NEWER%0","MOST TESTED APPLIANCES OK%0"
                , "AGING APPLIANCES%0","VERY OLD APPLIANCES%0"};
        electric_rangeValues = new String[] {"Need Anti-Tip Bracket%0","Burner Inoperative %0","Oven Light Inoperative%0"};
        gas_rangeValues = new String[]{"Need Anti-Tip Bracket%0","Burner Inoperative %0","Oven Light Inoperative%0"};
        built_in_electric_ovenValues = new String[]{"Old%0","Inoperative %0","Oven Light Inoperative%0"};
        electric_cooktopValues = new String[]{"Old%0","Burner Inoperative%0","Inoperative %0"};
        gas_cooktopValues = new String[]{"Old%0","Burner Inoperative%0","Inoperative %0"};
        microwave_ovenValues = new String[]{"Inoperative %0","Under Mount Light Inoperative%0"};
        dishwasherValues= new String[]{"Old%0","Inoperative%0","Leak%0","Loose %0"};
        waste_disposerValues = new String[]{"Inoperative%0","Needs Wire Clamp%0","Seized%0","Loose%0","Wiring Loose%0"};
        refrigeratorValues = new String[]{"Old %0","Inoperative%0"};
        wine_coolerValues = new String[]{"Old %0","Inoperative%0"};
        trash_compactorValues = new String[]{"Old %0","Inoperative%0"};
        clothes_washerValues = new String[]{"Old%0","Inoperative%0"};
        clothes_dryerValues = new String[]{"Old %0","Inoperative%0"};
        cooktop_exhaust_fanValues = new String[]{"Inoperative%0"};
        central_vacuumValues = new String[]{"Inoperative%0"};
        door_bellValues = new String[]{"Inoperative%0","None%0"};


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isAppliances_populated","");

        if(StructureScreensActivity.inspection_type.equals("old"))
        {
            getInsulation();

        }
        else
        {

            if(!(populate.equals("true")))
        {
            database.prePopulateData("appliancestested", appliances_testedValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("laundryfacility", laundry_facilityValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("othercomponentstested", other_components_testedValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observations", appliance_observationsValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("relectricrange", electric_rangeValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rgasrange", gas_rangeValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rbuiltinelectricoven", built_in_electric_ovenValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("relectriccooktop", electric_cooktopValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rgascooktop", gas_cooktopValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rmicrowaveoven", microwave_ovenValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdishwasher", dishwasherValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwastedisposer", waste_disposerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rrefrigerator", refrigeratorValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwinecooler", wine_coolerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rtrashcompactor", trash_compactorValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rclotheswasher", clothes_washerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rclothesdryer", clothes_dryerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rcooktopexhaustfan", cooktop_exhaust_fanValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rcentralvacuum", central_vacuumValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdoorbell", door_bellValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);

            // Saving string
            editor.putString("isAppliances_populated", "true");
            editor.apply();
        }

        }



        appliances_tested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",appliances_testedValues);
                intent.putExtra("heading",appliances_tested.getText().toString());
                intent.putExtra("column","appliancestested");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        laundry_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",laundry_facilityValues);
                intent.putExtra("heading",laundry_facility.getText().toString());
                intent.putExtra("column","laundryfacility");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        other_components_tested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",other_components_testedValues);
                intent.putExtra("heading",other_components_tested.getText().toString());
                intent.putExtra("column","othercomponentstested");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        appliance_observations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",false);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",appliance_observationsValues);
                intent.putExtra("heading",appliance_observations.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        electric_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electric_rangeValues);
                intent.putExtra("heading",electric_range.getText().toString());
                intent.putExtra("column","relectricrange");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        gas_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",gas_rangeValues);
                intent.putExtra("heading",gas_range.getText().toString());
                intent.putExtra("column","rgasrange");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        built_in_electric_oven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",built_in_electric_ovenValues);
                intent.putExtra("heading",built_in_electric_oven.getText().toString());
                intent.putExtra("column","rbuiltinelectricoven");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        electric_cooktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",electric_cooktopValues);
                intent.putExtra("heading",electric_cooktop.getText().toString());
                intent.putExtra("column","relectriccooktop");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        gas_cooktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",gas_cooktopValues);
                intent.putExtra("heading",gas_cooktop.getText().toString());
                intent.putExtra("column","rgascooktop");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });


        microwave_oven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",microwave_ovenValues);
                intent.putExtra("heading",microwave_oven.getText().toString());
                intent.putExtra("column","rmicrowaveoven");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        dishwasher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",dishwasherValues);
                intent.putExtra("heading",dishwasher.getText().toString());
                intent.putExtra("column","rdishwasher");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        waste_disposer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",waste_disposerValues);
                intent.putExtra("heading",waste_disposer.getText().toString());
                intent.putExtra("column","rwastedisposer");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        refrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",refrigeratorValues);
                intent.putExtra("heading",refrigerator.getText().toString());
                intent.putExtra("column","rrefrigerator");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        wine_cooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",wine_coolerValues);
                intent.putExtra("heading",wine_cooler.getText().toString());
                intent.putExtra("column","rwinecooler");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        trash_compactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",trash_compactorValues);
                intent.putExtra("heading",trash_compactor.getText().toString());
                intent.putExtra("column","rtrashcompactor");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        clothes_washer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",clothes_washerValues);
                intent.putExtra("heading",clothes_washer.getText().toString());
                intent.putExtra("column","rclotheswasher");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        clothes_dryer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",clothes_dryerValues);
                intent.putExtra("heading",clothes_dryer.getText().toString());
                intent.putExtra("column","rclothesdryer");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        cooktop_exhaust_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",cooktop_exhaust_fanValues);
                intent.putExtra("heading",cooktop_exhaust_fan.getText().toString());
                intent.putExtra("column","rcooktopexhaustfan");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        central_vacuum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",central_vacuumValues);
                intent.putExtra("heading",central_vacuum.getText().toString());
                intent.putExtra("column","rcentralvacuum");
                intent.putExtra("dbTable",APPLIANCE_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        door_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putBoolean("imageButton",true);
                edit.commit();
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",door_bellValues);
                intent.putExtra("heading",door_bell.getText().toString());
                intent.putExtra("column","rdoorbell");
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


                Cursor cursor = database.getTable(APPLIANCE_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();

                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
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
                    params.put("rcooktopexhaustfan", cursor.getString(24));
                    params.put("rcentralvacuum", cursor.getString(25));
                    params.put("rdoorbell", cursor.getString(26));

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


                            database.insertEntry("appliancestested", object.getString("appliancestested"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("laundryfacility", object.getString("laundryfacility"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("othercomponentstested", object.getString("othercomponentstested"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("observations", object.getString("observations"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("relectricrange", object.getString("relectricrange"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rgasrange", object.getString("rgasrange"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rbuiltinelectricoven", object.getString("rbuiltinelectricoven"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("relectriccooktop", object.getString("relectriccooktop"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rgascooktop", object.getString("rgascooktop"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rmicrowaveoven", object.getString("rmicrowaveoven"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rdishwasher", object.getString("rdishwasher"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rwastedisposer", object.getString("rwastedisposer"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rrefrigerator", object.getString("rrefrigerator"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rwinecooler", object.getString("rwinecooler"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rtrashcompactor", object.getString("rtrashcompactor"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rclotheswasher", object.getString("rclotheswasher"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rclothesdryer", object.getString("rclothesdryer"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rcooktopexhaustfan", object.getString("rcooktopexhaustfan"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rcentralvacuum", object.getString("rcentralvacuum"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
                            database.insertEntry("rdoorbell", object.getString("rdoorbell"), APPLIANCE_TABLE, StructureScreensActivity.inspectionID);

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

}
