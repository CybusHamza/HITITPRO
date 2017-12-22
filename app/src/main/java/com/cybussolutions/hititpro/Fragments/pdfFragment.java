package com.cybussolutions.hititpro.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.cybussolutions.hititpro.Adapters.PDFAddapter;
import com.cybussolutions.hititpro.Model.PDFModel;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class pdfFragment extends Fragment {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    ArrayList<PDFModel> pdfModels = new ArrayList<>();
    Spinner client_spinner;
    String client_id,clientid,id;
    private List<String> client_list = new ArrayList<>();
    private List<String> client_id_list = new ArrayList<>();
    ListView  listView;

    public pdfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.pdf_fragment, container, false);

        listView = (ListView) v.findViewById(R.id.myList);
        client_spinner = (Spinner) v.findViewById(R.id.client_spinner);

        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);

        id = pref.getString("user_id", null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Report History");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_black);

        client_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener_client());

        AllClients();


        return v;
    }

    public class CustomOnItemSelectedListener_client implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            if (client_list.get(pos).equals("No Client Founds")) {
                client_id = "0";
            }
            else {
                client_id = client_id_list.get(pos);

            }
            if (!client_list.get(pos).equals("All Clients")) {
              getPdf(client_id,"0");
            } else {
                getPdf(client_id,"1");
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

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

                            client_list.add(0, "No Client Found");
                            client_id_list.add(0,"No Data");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item, client_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            client_spinner.setAdapter(dataAdapter);

                        } else {

                            try {

                                JSONArray Array = new JSONArray(response);
                                client_list.add(0,"All Clients");
                                client_id_list.add(0,"0");

                                for (int i = 0; i < Array.length(); i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());

                                    client_list.add(object.getString("client_name"));
                                    client_id_list.add(object.getString("id"));

                                }


                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_spinner_item, client_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                client_spinner.setAdapter(dataAdapter);

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
                params.put("is_active","0");
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


    private void getPdf(final String client , final String isAll) {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETPDF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        pdfModels = new ArrayList<>();

                        if (response.equals("0"))
                        {
                            Toast.makeText(getActivity(), "No PDF found against this client", Toast.LENGTH_SHORT).show();

                            PDFAddapter pdfAddapter = new PDFAddapter(getActivity(),R.layout.pdf_row,pdfModels);

                            listView.setAdapter(pdfAddapter);

                        }
                        else
                        {


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                PDFModel pdfModel = new PDFModel();

                                JSONObject object = new JSONObject(jsonArray.getJSONObject(i).toString());

                                pdfModel.setClient(object.getString("client_name"));
                                pdfModel.setDate(object.getString("created_date"));
                                pdfModel.setName(object.getString("report_name"));


                                pdfModels.add(pdfModel);



                            }



                            PDFAddapter pdfAddapter = new PDFAddapter(getActivity(),R.layout.pdf_row,pdfModels);

                            listView.setAdapter(pdfAddapter);


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

                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                params.put("user_id", pref.getString("user_id",""));
                params.put("client_id",client);
                params.put("isAll", isAll);

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
