package com.cybussolutions.hititpro.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Model.Templates_model;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Template_Adapter extends BaseAdapter {
    ArrayList<Templates_model> arrayList;
    Context context;
    LayoutInflater inflter;
    View view;
    String from;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;


    public Template_Adapter(ArrayList<Templates_model> arrayList, Context context,String from)
    {
        this.arrayList=arrayList;
        this.context=context;
        this.from = from;
        inflter = (LayoutInflater.from(context));


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
            v = li.inflate(R.layout.row_template, null);
            viewholder = new ViewHolder();
            Typeface face= Typeface.createFromAsset(context.getAssets(), "Calibri.ttf");
            viewholder.name = (TextView) v.findViewById(R.id.client_name);
            viewholder.template = (TextView) v.findViewById(R.id.template);
            viewholder.inspection=(TextView)v.findViewById(R.id.inspection);
            viewholder.inspection=(TextView)v.findViewById(R.id.inspection);
            viewholder.name.setTypeface(face, Typeface.BOLD);
            viewholder.name.setTextSize(17);
            viewholder.inspection.setTypeface(face);
            viewholder.template.setTypeface(face, Typeface.BOLD);
            viewholder.template.setTextSize(15);
            viewholder.editTemplate=(ImageView)v.findViewById(R.id.editTemplate);
            viewholder.deleteTemplate=(ImageView)v.findViewById(R.id.deleteTemplate);
            viewholder.archiveTemp=(ImageView)v.findViewById(R.id.archive);
            viewholder.donwloadTemplate=(ImageView)v.findViewById(R.id.download);
            v.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) v.getTag();
        }

        if(from.equals("archive"))
        {
            viewholder.archiveTemp.setVisibility(View.GONE);
        }

        viewholder.donwloadTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfView(position);
            }
        });
        viewholder.archiveTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Archive Template")
                        .setMessage("Are you sure you want to Archive this Template!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                archive(position);

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
        viewholder.inspection.setText(arrayList.get(position).get_inspection());
        viewholder.template.setText(arrayList.get(position).get_template());
        viewholder.editTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StructureScreensActivity.class);
                intent.putExtra("inspectionId", arrayList.get(position).get_inspection_id());
                intent.putExtra("client_id", arrayList.get(position).getClient_id());
                intent.putExtra("template_id", arrayList.get(position).get_template_id());
                intent.putExtra("template_name", arrayList.get(position).get_template());
                intent.putExtra("client_name", arrayList.get(position).getClient_name());
                intent.putExtra("inspection_type", "old");
                intent.putExtra("is_notemplate", "false");
                context.startActivity(intent);
            }
        });
        viewholder.deleteTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete Template")
                        .setMessage("Are you sure you want to Delete this Template ? This Will remove all the data in this Template!!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                delete(position);

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

        return v;
    }

    class ViewHolder{
        protected TextView name,template,inspection;
        ImageView editTemplate,deleteTemplate,archiveTemp,donwloadTemplate;


    }

    public void delete(final int position) {

        ringProgressDialog = ProgressDialog.show(context, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.DELETE_TEMPLATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        arrayList.remove(position);

                        notifyDataSetChanged();

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
                params.put("client_id",arrayList.get(position).getClient_id());
                params.put("ca_id",arrayList.get(position).get_template_id());
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

    public void archive(final int position) {

        ringProgressDialog = ProgressDialog.show(context, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.ARCHHIVE_TEMPLATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        arrayList.remove(position);

                        notifyDataSetChanged();

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
                params.put("client_id",arrayList.get(position).getClient_id());
                params.put("ca_id",arrayList.get(position).get_template_id());
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

    public void pdfView(final int position) {

        ringProgressDialog = ProgressDialog.show(context, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GENRATE_PDF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                      /*  Intent intent = new Intent(context, PdfView.class);
                        context.startActivity(intent);*/
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(End_Points.PDF_BASRURL+response));
                        context. startActivity(browserIntent);


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
                params.put("client",arrayList.get(position).getClient_id());
                params.put("temp",arrayList.get(position).get_template_id());
                params.put("insp",arrayList.get(position).get_inspection_id());
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
}
