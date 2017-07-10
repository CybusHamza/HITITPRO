package com.cybussolutions.hititpro.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setp13_review extends BaseFragment {

    View root;
    TextView clientsSpinner,templateSpinner;
    ProgressDialog ringProgressDialog;
    String id, client_id;
    TextView tvportofolio,tvroofing,remainig,tvplumbing,tvinterior,tvinsulation,tvheating,tvfireplaces,tvexterior,tvelectrical,tvcooling,tvappliance;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root= inflater.inflate(R.layout.fragment_setp13_review, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Review Inspection");
        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        id = pref.getString("user_id", null);

        clientsSpinner= (TextView) root.findViewById(R.id.review_clients_spinner);
        templateSpinner=(TextView) root.findViewById(R.id.review_templates_spinner);
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

        getReviewInspection();

        clientsSpinner.setText(StructureScreensActivity.c_name);
        templateSpinner.setText(StructureScreensActivity.temp_name);


        return root;
    }


    private void getReviewInspection() {
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

                params.put("client_id", StructureScreensActivity.client_id);
                params.put("temp_id", StructureScreensActivity.template_id);

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



}
