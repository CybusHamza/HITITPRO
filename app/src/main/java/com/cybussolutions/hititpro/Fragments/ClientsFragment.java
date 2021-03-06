package com.cybussolutions.hititpro.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.cybussolutions.hititpro.Activities.Add_Client;
import com.cybussolutions.hititpro.Adapters.Client_Adapter;
import com.cybussolutions.hititpro.Model.Clients_model;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ClientsFragment extends BaseFragment {

    View root;
    private ArrayList<Clients_model> list = new ArrayList<>();
    ListView client_list;
    ImageView addClient;
    String id;
    EditText search;
    TextView textnoclient,textAddclient;
    ImageView pictureadd,clientimg;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Button archive;
    Fragment fragment = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_clients, container, false);
        archive = (Button) root.findViewById(R.id.archive);
        client_list = (ListView) root.findViewById(R.id.client);
        addClient=(ImageView) root.findViewById(R.id.add_client);
        textnoclient=(TextView) root.findViewById(R.id.textclient);
        textAddclient=(TextView) root.findViewById(R.id.noclientText);
        pictureadd=(ImageView) root.findViewById(R.id.img_id);
        clientimg=(ImageView) root.findViewById(R.id.clientimg);
        search=(EditText) root.findViewById(R.id.search);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Clients");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_black);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

  /*      String[] client_name = new String[]{"Hamza Bin Tariq", "Zaeem Sattar", "Noor Siddiqui", "Maria Talib"};
        for (String aClient_name : client_name) {
            Clients_model model = new Clients_model();
            model.setClient_name(aClient_name);

            list.add(model);
        }*/

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ArchiveClient();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String nameToSearch = search.getText().toString();
                ArrayList<Clients_model> filteredLeaves = new ArrayList<Clients_model>();


                for (Clients_model data : list) {
                    if (data.getClient_name().toLowerCase().contains(nameToSearch.toLowerCase()) || data.getClient_phone().toLowerCase().equalsIgnoreCase(nameToSearch.toLowerCase())) {
                        filteredLeaves.add(data);
                    }


                }
                /*leaveDatas.clear();
                leaveDatas.addAll(filteredLeaves);
                leaves_adapter.notifyDataSetChanged();*/
                Client_Adapter client_adapter = new Client_Adapter(filteredLeaves, getActivity(),"active",root);
                client_list.setAdapter(client_adapter);

            }                //     listView.setAdapter(leaves_adapter);


            @Override
            public void afterTextChanged(Editable s) {


            }
        });





        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
         id = pref.getString("user_id", null);

        AllClients();

        client_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              /*  Intent intent = new Intent(getActivity(), Start_Inspection.class);
                intent.putExtra("client_name", list.get(i).getClient_name());
                intent.putExtra("client_id", list.get(i).getClient_id());
                startActivity(intent);*/
            }
        });

        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Add_Client.class);
                startActivity(intent);

            }
        });

        return root;
    }
    public void updateUi(View root){
        textnoclient=(TextView) root.findViewById(R.id.textclient);
        textAddclient=(TextView) root.findViewById(R.id.noclientText);
        pictureadd=(ImageView) root.findViewById(R.id.img_id);
        textAddclient.setVisibility(View.VISIBLE);
        textnoclient.setVisibility(View.VISIBLE);
        pictureadd.setVisibility(View.VISIBLE);
    }

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clients, menu);  // Use filter.xml from step 1
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

                            textAddclient.setVisibility(View.VISIBLE);
                            textnoclient.setVisibility(View.VISIBLE);
                            pictureadd.setVisibility(View.VISIBLE);
                           // clientimg.setVisibility(View.VISIBLE);
                        }
                        else {

                            parseJson(response);

                            Client_Adapter client_adapter = new Client_Adapter(list, getActivity(),"active",root);
                            client_list.setAdapter(client_adapter);


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


    public ArrayList<Clients_model>  parseJson(String responce)
    {

        try {

            JSONArray Array = new JSONArray(responce);

            for(int i=0;i<Array.length();i++) {

                JSONObject object = new JSONObject(Array.getJSONObject(i).toString());

                Clients_model model =  new Clients_model();

                model.setClient_name(object.getString("client_name"));
                model.setClient_id(object.getString("id"));
                model.setClient_adress(object.getString("address"));
                model.setClient_phone(object.getString("phone"));
                model.setContact_name(object.getString("contactname"));
                model.set_city(object.getString("city"));
                model.set_fax(object.getString("fax"));
                model.set_email(object.getString("email"));
                model.set_state(object.getString("state"));
                model.set_zip(object.getString("zip"));

                list.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  list;
    }

}

