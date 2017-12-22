package com.cybussolutions.hititpro.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.cybussolutions.hititpro.Activities.EditClient;
import com.cybussolutions.hititpro.Activities.LandingScreen;
import com.cybussolutions.hititpro.Fragments.ArchiveClient;
import com.cybussolutions.hititpro.Fragments.ClientsFragment;
import com.cybussolutions.hititpro.Model.Clients_model;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Client_Adapter extends BaseAdapter {
    ArrayList<Clients_model> arrayList;
    Context context;
    LayoutInflater inflter;
    View view;
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    String active;
    View root;
    public Client_Adapter(ArrayList<Clients_model> arrayList, Context context,String active,View root)
    {
        this.arrayList=arrayList;
        this.context=context;
        inflter = (LayoutInflater.from(context));
        this.active=active;
        this.root=root;


    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder viewholder = null;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.row_client, null);
            viewholder = new ViewHolder();
            Typeface face= Typeface.createFromAsset(context.getAssets(), "Calibri.ttf");
            viewholder.name = (TextView) v.findViewById(R.id.client_name);
            viewholder.adress = (TextView) v.findViewById(R.id.client_adress);
            viewholder.phone_number=(TextView)v.findViewById(R.id.client_phone);
            viewholder.name.setTypeface(face, Typeface.BOLD);
            viewholder.name.setTextSize(17);
            viewholder.adress.setTypeface(face);
            viewholder.phone_number.setTypeface(face, Typeface.BOLD);
            viewholder.phone_number.setTextSize(15);
            viewholder.editClient=(ImageView)v.findViewById(R.id.editClient);
            viewholder.active=(ImageView)v.findViewById(R.id.activeClient);
            viewholder.deleteClient=(ImageView)v.findViewById(R.id.deleteClient);
            v.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) v.getTag();
        }
        if(active.equals("active")){
            viewholder.active.setVisibility(View.GONE);
            viewholder.deleteClient.setVisibility(View.VISIBLE);
            viewholder.editClient.setVisibility(View.VISIBLE);
        }else {
            viewholder.active.setVisibility(View.VISIBLE);
            viewholder.deleteClient.setVisibility(View.GONE);
            viewholder.editClient.setVisibility(View.GONE);
        }
        viewholder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to activate Client ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent=new Intent(getActivity(),LandingScreen.class);
                            intent.putExtra("activityName", "StructureScreen");
                            startActivity(intent);*/
                                activeClient(arrayList.get(position).getClient_id(),position);



                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        viewholder.name.setText(arrayList.get(position).getClient_name());
        viewholder.adress.setText(arrayList.get(position).getClient_adress());
        viewholder.phone_number.setText(arrayList.get(position).getClient_phone());
        viewholder.deleteClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Do you really want to delete Client")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent=new Intent(getActivity(),LandingScreen.class);
                            intent.putExtra("activityName", "StructureScreen");
                            startActivity(intent);*/
                            deleteClient(arrayList.get(position).getClient_id(),position);



                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            }
        });
        viewholder.editClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, EditClient.class);
                    intent.putExtra("client_id", arrayList.get(position).getClient_id());
                    intent.putExtra("client_name", arrayList.get(position).getClient_name());
                    intent.putExtra("client_contact", arrayList.get(position).getContact_name());
                    intent.putExtra("client_address", arrayList.get(position).getClient_adress());
                    intent.putExtra("client_city", arrayList.get(position).get_city());
                    intent.putExtra("client_phone", arrayList.get(position).getClient_phone());
                    intent.putExtra("client_fax", arrayList.get(position).get_fax());
                    intent.putExtra("client_email", arrayList.get(position).get_email());
                    intent.putExtra("client_state", arrayList.get(position).get_state());
                    intent.putExtra("client_zip", arrayList.get(position).get_zip());
                    context.startActivity(intent);
                }
            }

            );
            return v;
        }

    private void deleteClient(final String client_id, final int position) {
        ringProgressDialog = ProgressDialog.show(context, "", "Please wait ...Deleting client......", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.DELETE_CLIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success!")
                                .setConfirmText("OK").setContentText("Client deleted Successfully")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                        arrayList.remove(position);
                                        notifyDataSetChanged();
                                        if(arrayList.size()<1){
                                           ClientsFragment fragment = new ClientsFragment();
                                            fragment.updateUi(root);
                                        }
                                    }
                                })
                                .show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                params.put("cliet_id",client_id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    private void activeClient(final String client_id, final int position) {
        ringProgressDialog = ProgressDialog.show(context, "", "Please wait ...Activating client......", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.ACTIVE_CLIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success!")
                                .setConfirmText("OK").setContentText("Client Activated Successfully")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                        arrayList.remove(position);
                                        notifyDataSetChanged();
                                        if(arrayList.size()<1){
                                             ArchiveClient fragment = new ArchiveClient();
                                           fragment.updateUi(root);
                                        }
                                    }
                                })
                                .show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                params.put("cliet_id",client_id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    class ViewHolder{
          protected TextView name,adress,phone_number;
          ImageView editClient,deleteClient,active;


    }
}
