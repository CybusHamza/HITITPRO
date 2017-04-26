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
import android.widget.Button;
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
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Template_Inspection.StructureScreenFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class TemplatesFragment extends BaseFragment {

    View root;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    String id, client_id;
    Spinner tem_spinner,client_spinner,inspection_spinner;

    private List<String> client_list = new ArrayList<>();
    private List<String> client_id_list = new ArrayList<>();

    private List<String>template_list;
    private List<String> templateID_list ;
    private List<String> templateID_date ;

    private List<String> inspection_list;
    private List<String> inspection_id_list;

    Button review;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_templates,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Templates");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        id = pref.getString("user_id", null);

        tem_spinner = (Spinner) root.findViewById(R.id.spinner);
        client_spinner = (Spinner) root.findViewById(R.id.client_spinner);
        inspection_spinner = (Spinner) root.findViewById(R.id.inspection);
        review = (Button) root.findViewById(R.id.button);

        client_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener_client());

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int client_id =  client_spinner.getSelectedItemPosition();
                int inspection_id =  inspection_spinner.getSelectedItemPosition();
                int template_id =  tem_spinner.getSelectedItemPosition();

                if (client_spinner.getSelectedItem().equals("No Records Founds") && inspection_spinner.getSelectedItem().equals("No Records Founds") && tem_spinner.getSelectedItem().equals("No Records Founds")) {
                    Toast.makeText(getActivity(), "Please Select Templates", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(getActivity(), StructureScreensActivity.class);
                    intent.putExtra("inspectionId", templateID_list.get(inspection_id));
                    intent.putExtra("client_id", client_id_list.get(client_id));
                    intent.putExtra("template_id", inspection_id_list.get(template_id));
                    intent.putExtra("inspection_type", "old");
                    startActivity(intent);
                }

            }
        });
        tem_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener_inspection());

        AllClients();

        return root;
    }


    public class CustomOnItemSelectedListener_client implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            if(client_list.get(pos).equals("No Records Founds"))
            {
                client_id = "0";
            }
            else {
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

            String temp_id = null;

            if(inspection_list.get(pos).equals("No Records Founds"))
            {
                temp_id = "0";
            }
            else {

                temp_id = inspection_id_list.get(pos);

            }

            getInspections(temp_id);


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }
    public void getInspections(final String temp_id)
    {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_INSPECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ringProgressDialog.dismiss();

                        if(response.equals("\"no record found\""))
                        {
                            template_list  = new ArrayList<>();
                            template_list.add(0,"No Records Founds");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item,template_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            inspection_spinner.setAdapter(dataAdapter);

                        }

                        else if (!(response.equals("0")))
                        {
                            try {

                                template_list  = new ArrayList<>();
                                templateID_list  = new ArrayList<>();
                                templateID_date   = new ArrayList<>();
                                JSONArray Array = new JSONArray(response);

                                for(int i=0;i<Array.length();i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());


                                    template_list.add(object.getString("id")+"  "+object.getString("name"));
                                    templateID_list.add(object.getString("id"));
                                    templateID_date.add(object.getString("added_on"));

                                }


                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_spinner_item,template_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                inspection_spinner.setAdapter(dataAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
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

                params.put("tempid",temp_id);
                params.put("client_id",client_id);

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

    public void getTemplates()
    {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TEMPLATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ringProgressDialog.dismiss();

                        if(response.equals("\"no record found\""))
                        {


                            inspection_list= new ArrayList<>();

                            inspection_list.add(0,"No Records Founds");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item,inspection_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            tem_spinner.setAdapter(dataAdapter);

                        }


                        else if (!(response.equals("0")))
                        {
                            try {


                                JSONArray Array = new JSONArray(response);

                                inspection_list= new ArrayList<>();
                                inspection_id_list = new ArrayList<>();


                                for(int i=0;i<Array.length();i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());

                                    inspection_list.add(object.getString("name"));
                                    inspection_id_list.add(object.getString("ca_id"));

                                }

                                tem_spinner.setAdapter(null);

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_spinner_item,inspection_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                tem_spinner.setAdapter(dataAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        else
                        {
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

                params.put("client_id",client_id);
                params.put("user_id",id);

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

                        if(response.equals("\"no record found\""))
                        {

                            client_list.add(0,"No Records Founds");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item,client_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            client_spinner.setAdapter(dataAdapter);

                        }

                        else if (response.equals("false")) {
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
                        }

                        else {

                            try {

                                JSONArray Array = new JSONArray(response);

                                for(int i=0;i<Array.length();i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());

                                    client_list .add(object.getString("client_name"));
                                    client_id_list .add(object.getString("id"));

                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_spinner_item,client_list);

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
                params.put("userid",id);
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
