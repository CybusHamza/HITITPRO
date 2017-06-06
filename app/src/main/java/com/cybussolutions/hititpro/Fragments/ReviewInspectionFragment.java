package com.cybussolutions.hititpro.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ReviewInspectionFragment extends BaseFragment {

    View root;
    Spinner clientsSpinner,templateSpinner;
    String[]  clientsSpinnerValues,templateSpinnerValues,remainingValues;
    ListView remainingInspectionsListview;
    ProgressDialog ringProgressDialog;
    String id, client_id;
    TextView tvportofolio,tvroofing,remainig,tvplumbing,tvinterior,tvinsulation,tvheating,tvfireplaces,tvexterior,tvelectrical,tvcooling,tvappliance;


    private List<String> client_list = new ArrayList<>();
    private List<String> client_id_list = new ArrayList<>();
    private List<String> para_list = new ArrayList<>();
    private List<String> template_list;
    private List<String> templateID_list;
    private List<String> templateID_date;
    private List<String> isStarted;
    private List<String> inspection_list;
    private List<String> inspection_id_list;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_review_inspection,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Review Inspection");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        id = pref.getString("user_id", null);
        Intent intent= getActivity().getIntent();

        AllClients();
        //getTemplates();

        clientsSpinner= (Spinner) root.findViewById(R.id.review_clients_spinner);
       templateSpinner=(Spinner) root.findViewById(R.id.review_templates_spinner);
        tvportofolio=(TextView)root.findViewById(R.id.structure);
        tvroofing=(TextView)root.findViewById(R.id.roofing);
        tvplumbing=(TextView)root.findViewById(R.id.plumbing);
        tvinterior=(TextView)root.findViewById(R.id.interior);
        tvinsulation=(TextView)root.findViewById(R.id.insulation);
        tvheating=(TextView)root.findViewById(R.id.heating);
        tvfireplaces=(TextView)root.findViewById(R.id.fireplace);
        tvexterior=(TextView)root.findViewById(R.id.exterior);
        remainig=(TextView)root.findViewById(R.id.remainig);
        tvelectrical=(TextView)root.findViewById(R.id.electrical);
        tvcooling=(TextView)root.findViewById(R.id.cooling);
        tvappliance=(TextView)root.findViewById(R.id.appliance);
        clientsSpinner.setOnItemSelectedListener(new ReviewInspectionFragment.CustomOnItemSelectedListener_client());
        templateSpinner.setOnItemSelectedListener(new ReviewInspectionFragment.CustomOnItemSelectedListener_inspection());
       /* remainingInspectionsListview=(ListView) root.findViewById(R.id.remaining_listview);

        setSpinnerValues();
        setSpinnerAdapter();
        setSpinnerListener();
        ArrayAdapter remainingAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,remainingValues);
        remainingInspectionsListview.setAdapter(remainingAdapter);
*/
        return root;
    }
    private void setSpinnerValues()
    {
        clientsSpinnerValues= new String[]{"Zaeem sattar","Hamza bin Tariq","Noor shidiqqui"};
        templateSpinnerValues= new String []{"Housing template 1","Housing template 2","Housing template 3"};
        remainingValues= new String []{"1","2","3","4","5","6","7","8","9","10"};


    }
    private void setSpinnerListener()
    {
        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        templateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setSpinnerAdapter()
    {
        ArrayAdapter<String> dataAdapter;

        // 1st spinner
        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clientsSpinnerValues);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clientsSpinner.setAdapter(dataAdapter);

        // 2nd spinner

        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, templateSpinnerValues);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        templateSpinner.setAdapter(dataAdapter);
    }
    public class CustomOnItemSelectedListener_client implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            if (client_list.get(pos).equals("No Records Founds")) {
                client_id = "0";
            } else {
                client_id = client_id_list.get(pos);

            }

            getTemplates();


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }
    public class CustomOnItemSelectedListener_inspection implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            String temp_id = null, para;

            if (inspection_list.get(pos).equals("No Records Founds")) {
                temp_id = "0";
            } else {

                temp_id = inspection_id_list.get(pos);
                para = para_list.get(pos);

//                    paraEt.setText(para);

            }

            getReviewInspection(temp_id);
//                getInspections(temp_id);


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    private void getReviewInspection(final String temp_id) {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_REVIEW_INSPECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        try {
                            JSONObject main=new JSONObject(response);
                            JSONArray portfolio = new JSONArray(main.getJSONArray("portfolio").toString());
                            JSONArray roofing = new JSONArray(main.getJSONArray("roofing").toString());
                            JSONArray plumbing = new JSONArray(main.getJSONArray("plumbing").toString());
                            JSONArray interior = new JSONArray(main.getJSONArray("interior").toString());
                            JSONArray insulation = new JSONArray(main.getJSONArray("insulation").toString());
                            JSONArray heating = new JSONArray(main.getJSONArray("heating").toString());
                            JSONArray fireplaces = new JSONArray(main.getJSONArray("fireplaces").toString());
                            JSONArray exterior = new JSONArray(main.getJSONArray("exterior").toString());
                            JSONArray electrical = new JSONArray(main.getJSONArray("electrical").toString());
                            JSONArray cooling = new JSONArray(main.getJSONArray("cooling").toString());
                            JSONArray appliance = new JSONArray(main.getJSONArray("appliance").toString());


                            int total = 0;
                            for (int i=0;i<portfolio.length();i++) {
                                JSONObject objectportfolio = portfolio.getJSONObject(i);
                                JSONObject objectroofing = roofing.getJSONObject(i);
                                JSONObject objectplumbing = plumbing.getJSONObject(i);
                                JSONObject objectinterior = interior.getJSONObject(i);
                                JSONObject objectinsulation = insulation.getJSONObject(i);
                                JSONObject objectheating = heating.getJSONObject(i);
                                JSONObject objectfireplaces = fireplaces.getJSONObject(i);
                                JSONObject objectexterior = exterior.getJSONObject(i);
                                JSONObject objectelectrical = electrical.getJSONObject(i);
                                JSONObject objectcooling = cooling.getJSONObject(i);
                                JSONObject objectappliance = appliance.getJSONObject(i);
                                //String port=objectportfolio.getString("empty_fields");

                                tvportofolio.setText(objectportfolio.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectportfolio.get("empty_fields").toString());
                                tvroofing.setText(objectroofing.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectroofing.get("empty_fields").toString());
                                tvplumbing.setText(objectplumbing.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectplumbing.get("empty_fields").toString());
                                tvinterior.setText(objectinterior.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectinterior.get("empty_fields").toString());
                                tvinsulation.setText(objectinsulation.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectinsulation.get("empty_fields").toString());
                                tvheating.setText(objectheating.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectheating.get("empty_fields").toString());
                                tvfireplaces.setText(objectfireplaces.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectfireplaces.get("empty_fields").toString());
                                tvexterior.setText(objectexterior.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectexterior.get("empty_fields").toString());
                                tvelectrical.setText(objectelectrical.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectelectrical.get("empty_fields").toString());
                                tvcooling.setText(objectcooling.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectcooling.get("empty_fields").toString());
                                tvappliance.setText(objectappliance.get("empty_fields").toString());
                                total = total+ Integer.parseInt(objectappliance.get("empty_fields").toString());

                                remainig.setText(total+"");
                            }

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

                Map<String, String> params = new HashMap<>();

                params.put("client_id", client_id_list.get((int) clientsSpinner.getSelectedItemId()));
                params.put("temp_id", temp_id);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }


    public void getTemplates() {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        if (response.equals("\"no record found\"")) {
                            inspection_list = new ArrayList<>();

                            inspection_list.add(0, "No Records Founds");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item, inspection_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            templateSpinner.setAdapter(dataAdapter);

                        } else if (!(response.equals("0"))) {
                            try {

                                JSONArray Array = new JSONArray(response);

                                inspection_list = new ArrayList<>();
                                inspection_id_list = new ArrayList<>();
                                para_list = new ArrayList<>();
                                isStarted = new ArrayList<>();


                                for (int i = 0; i < Array.length(); i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());
                                    if(object.getString("name").equals("null"))
                                    {
                                        inspection_list.add(object.getString("template_name"));
                                    }
                                    else
                                    {
                                        inspection_list.add(object.getString("name"));
                                    }

                                    inspection_id_list.add(object.getString("ca_id"));
                                    para_list.add(object.getString("paragraph_text"));
                                    isStarted.add(object.getString("is_started"));
                                }

                                templateSpinner.setAdapter(null);

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_spinner_item, inspection_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                templateSpinner.setAdapter(dataAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("There Are No Templates Against this Client")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();

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

                Map<String, String> params = new HashMap<>();

                params.put("client_id", client_id);
                params.put("user_id", id);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }
    public void AllClients() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_CLIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        if (response.equals("\"no record found\"")) {

                            client_list.add(0, "No Records Founds");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item, client_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            clientsSpinner.setAdapter(dataAdapter);

                        } else if (response.equals("false")) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("No Clients Found ")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        } else {

                            try {

                                JSONArray Array = new JSONArray(response);

                                for (int i = 0; i < Array.length(); i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());

                                    client_list.add(object.getString("client_name"));
                                    client_id_list.add(object.getString("id"));

                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_spinner_item, client_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                clientsSpinner.setAdapter(dataAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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

                Map<String, String> params = new HashMap<>();
                params.put("userid", id);
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
