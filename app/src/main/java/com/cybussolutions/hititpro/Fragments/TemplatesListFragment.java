package com.cybussolutions.hititpro.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.cybussolutions.hititpro.Adapters.Template_Adapter;
import com.cybussolutions.hititpro.Model.Templates_model;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class TemplatesListFragment extends BaseFragment {

    View root;
    private ArrayList<Templates_model> list = new ArrayList<>();
    ListView templates_list;
    ImageView addClient;
    String id;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Button archive;
    Fragment fragment = null;
    String templatename,inspectionname;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_list_templates, container, false);

        templates_list = (ListView) root.findViewById(R.id.templates_list);

        archive = (Button) root.findViewById(R.id.archive);

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ArchiveList();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Inspection List");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_black);




        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        id = pref.getString("user_id", null);
        AllClients();
        templates_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Intent intent = new Intent(getActivity(), StructureScreensActivity.class);
                intent.putExtra("inspectionId",list.get(position).get_inspection_id());
                intent.putExtra("client_id", list.get(position).getClient_id());
                intent.putExtra("template_id",list.get(position).get_template_id());
                intent.putExtra("inspection_type", "old");
                startActivity(intent);*/
            }
        });
        return root;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    public void AllClients() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_ALL_TEMPLATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        if (response.equals("[]")) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("No Property Found ")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        }
                        else {

                            parseJson(response);

                            Template_Adapter client_adapter = new Template_Adapter(list, getActivity(),"template");
                            templates_list.setAdapter(client_adapter);


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
                params.put("id",id);
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


    public ArrayList<Templates_model>  parseJson(String responce)
    {

        try {

            JSONArray Array = new JSONArray(responce);

            for(int i=0;i<Array.length();i++) {

                JSONObject object = new JSONObject(Array.getJSONObject(i).toString());

                Templates_model model =  new Templates_model();
                templatename=object.getString("name");
                if(templatename==null||templatename.equals("null")){
                    templatename=object.getString("template_name");
                }
                else
                templatename=object.getString("name");

                inspectionname=object.getString("inspection_name");
                if(inspectionname==null || inspectionname.equals("")){
                    inspectionname="No inspection found";
                }
                else
                    inspectionname=object.getString("inspection_name");

                model.setClient_name("Client Name: "+object.getString("client_name"));
                model.setClient_id(object.getString("client_id"));
                model.set_template("Property: "+templatename);
                model.set_template_id(object.getString("template_id"));
                model.set_inspection("Inspection: "+inspectionname);
                model.set_inspection_id(object.getString("inspection_id"));
                list.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  list;
    }

}

