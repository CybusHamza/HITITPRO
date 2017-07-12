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
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class TemplatesFragment extends BaseFragment {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    public static String myparah_no_temp = "";
    private static String responceStatic = null;
    View root;
    ProgressDialog ringProgressDialog;
    String id, client_id,tempnameSelection,clientid;
    Spinner tem_spinner, client_spinner, inspection_spinner;
    EditText paraEt;
    Button review;
    ImageView add_template;
    private List<String> client_list = new ArrayList<>();
    private List<String> client_id_list = new ArrayList<>();
    private List<String> para_list = new ArrayList<>();
    private List<String> template_list;
    private List<String> templateID_list;
    private List<String> templateID_date;
    private List<String> isStarted;
    private List<String> isdefault;
    private List<String> inspection_list;
    private List<String> inspection_id_list;
    private List<String> default_template;

    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_templates, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Inspection");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_black);

         final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
         editor = pref.edit();
        id = pref.getString("user_id", null);
        tempnameSelection=pref.getString("tempidselected",null);
        clientid=pref.getString("client_id",null);

        tem_spinner = (Spinner) root.findViewById(R.id.spinner);
        client_spinner = (Spinner) root.findViewById(R.id.client_spinner);
        paraEt = (EditText) root.findViewById(R.id.et_default_comments);
        // inspection_spinner = (Spinner) root.findViewById(R.id.inspection);
        review = (Button) root.findViewById(R.id.button);
       // add_template = (ImageView) root.findViewById(R.id.add_template);

        client_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener_client());

      /*  add_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int client_id = client_spinner.getSelectedItemPosition();

                if(!client_spinner.getSelectedItem().equals("Select")) {
                    Intent i = new Intent(getActivity(), AddTemplate.class);
                    i.putExtra("client_id", client_id_list.get(client_id));
                    startActivity(i);
                }else
                    Toast.makeText(getContext(),"Plz select client to add new template",Toast.LENGTH_LONG).show();
            }
        });*/
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    int client_id = client_spinner.getSelectedItemPosition();
                    // int inspection_id =  inspection_spinner.getSelectedItemPosition();
                    int template_id = tem_spinner.getSelectedItemPosition();
                    if (!client_spinner.getSelectedItem().toString().equals("Select")) {
                        if (!tem_spinner.getSelectedItem().toString().equals("Select")) {
                           myparah_no_temp= paraEt.getText().toString();
                            if (isStarted.get(tem_spinner.getSelectedItemPosition()).equals("0")) {
                                prePopulate(inspection_id_list.get(tem_spinner.getSelectedItemPosition()),client_spinner.getSelectedItem().toString(),tem_spinner.getSelectedItem().toString(),tem_spinner.getSelectedItem().toString(),isdefault.get(tem_spinner.getSelectedItemPosition()), client_id_list.get(client_spinner.getSelectedItemPosition()), default_template.get(tem_spinner.getSelectedItemPosition()));

                            } else {
                                //  if(!client_list.get(client_id).equals("Select")) {
                                Intent intent = new Intent(getActivity(), StructureScreensActivity.class);
                                intent.putExtra("inspectionId", templateID_list.get(0));
                                intent.putExtra("client_id", client_id_list.get(client_id));
                                intent.putExtra("is_notemplate","false");
                                intent.putExtra("client_name",client_spinner.getSelectedItem().toString());
                                intent.putExtra("template_name",tem_spinner.getSelectedItem().toString());
                                intent.putExtra("template_id", inspection_id_list.get(template_id));
                                intent.putExtra("inspection_type", "old");
                                startActivity(intent);
                                //}else {
                                //  Toast.makeText(getContext(),"plz select client to review",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Select template and start inspection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please select client and Template to continue", Toast.LENGTH_LONG).show();
                    }
                       /* Intent intent=new Intent(getActivity(), Start_Inspection.class);
                        intent.putExtra("client_name",client_spinner.getSelectedItem().toString());
                        intent.putExtra("client_id",client_id_list.get(client_id));
                        startActivity(intent);*/


            }
        });
        tem_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener_inspection());
        AllClients();

        return root;
    }

    public void getInspections(final String temp_id) {
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_INSPECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ringProgressDialog.dismiss();

                        if (response.equals("\"no record found\"")) {
                            template_list = new ArrayList<>();
                            template_list.add(0, "No Records Founds");
                           /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item,template_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            inspection_spinner.setAdapter(dataAdapter);*/
                            //  review.setText("Start Inspection");

                        } else if (!(response.equals("0"))) {
                            try {

                                template_list = new ArrayList<>();
                                templateID_list = new ArrayList<>();
                                templateID_date = new ArrayList<>();
                                JSONArray Array = new JSONArray(response);

                                for (int i = 0; i < Array.length(); i++) {

                                    JSONObject object = new JSONObject(Array.getJSONObject(i).toString());


                                    template_list.add(object.getString("id") + "  " + object.getString("name"));
                                    templateID_list.add(object.getString("id"));
                                    templateID_date.add(object.getString("added_on"));


                                }


                              /*  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_spinner_item,template_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                inspection_spinner.setAdapter(dataAdapter);*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // review.setText("Continue");
                        } else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("There are no registered Properties Against this client")
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

                params.put("tempid", temp_id);
                params.put("client_id", client_id);

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
                            inspection_id_list = new ArrayList<>();
                            isStarted = new ArrayList<>();
                            isdefault = new ArrayList<>();
                            default_template = new ArrayList<>();

                            inspection_list.add(0, "No Template Founds");
                            inspection_id_list.add(0, "No Template Founds");
                            para_list.add(0, "No Template Founds");
                            isStarted.add(0, "No Template Founds");
                            default_template.add(0, "No Template Founds");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item, inspection_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            tem_spinner.setAdapter(dataAdapter);
                           // review.setText("Start Inspection");

                        } else if (!(response.equals("0"))) {
                            try {

                                JSONObject obj = new JSONObject(response);



                                String objRes =  obj.getString("primary");


                                JSONArray Array = new JSONArray(objRes);

                                inspection_list = new ArrayList<>();
                                inspection_id_list = new ArrayList<>();
                                para_list = new ArrayList<>();
                                isStarted = new ArrayList<>();
                                isdefault = new ArrayList<>();
                                default_template = new ArrayList<>();
                                para_list.add(0,"");
                                isStarted.add(0,"");


                                inspection_list.add(0,"Select");
                                inspection_id_list.add(0,"0");
                                isdefault.add(0,"0");
                                default_template.add(0,"");


                                inspection_list.add(1,"No Template");
                                inspection_id_list.add(1,"0");
                                para_list.add(1,"");
                                isdefault.add(1,"");
                                isStarted.add(1,"0");
                                default_template.add(1,"");






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
                                    isdefault.add(object.getString("isDefault"));
                                    isStarted.add(object.getString("is_started"));
                                    default_template.add(object.getString("default_template"));


                                }

                                String objdef =  obj.getString("default");

                                if(!objdef.equals("No Data"))
                                {
                                    JSONArray Arraydef = new JSONArray(objdef);

                                    for (int i = 0; i < Arraydef.length(); i++) {

                                        JSONObject object = new JSONObject(Arraydef.getJSONObject(i).toString());

                                        if(!(inspection_list.contains(object.getString("name"))))
                                        {
                                            inspection_list.add(object.getString("name"));

                                            inspection_id_list.add(object.getString("ca_id"));
                                            para_list.add(object.getString("paragraph_text"));
                                            isStarted.add("0");
                                            isdefault.add(object.getString("isDefault"));
                                            default_template.add(object.getString("default_template"));

                                        }



                                    }
                                }

                                tem_spinner.setAdapter(null);

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_spinner_item, inspection_list);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                tem_spinner.setAdapter(dataAdapter);
                                if(tempnameSelection!=null){
                                  //  int pos1=client_list.indexOf(clientid);
                                    int pos=inspection_list.indexOf(tempnameSelection);
                                    //client_spinner.setSelection(pos1);
                                    tem_spinner.setSelection(pos);
                                    editor.putString("tempidselected",null);
                                    //editor.putString("client_id",null);
                                    editor.commit();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("There are no Properties against this client")
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

                            client_list.add(0, "No Client Founds");
                            client_id_list.add(0,"No Data");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getContext(), android.R.layout.simple_spinner_item, client_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            client_spinner.setAdapter(dataAdapter);

                        } else if (response.equals("false")) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("No Template Found ")
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
                                client_list.add(0,"Select");
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

                                if(clientid!=null){
                                    int pos1=client_id_list.indexOf(clientid);
                                    client_spinner.setSelection(pos1);
                                    editor.putString("client_id",null);
                                    editor.commit();
                                }

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

    public void prePopulate(final String temId, final String client_id, final String tempid , final String temname, final String isDefault, final String clientId , final String default_template ) {

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.PRE_POPULATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();



                        if (!(response.equals(""))) {


                            Intent intent = new Intent(getActivity(), StructureScreensActivity.class);

                            if(response.startsWith("defult"))
                            {
                                String temp[] ;
                                temp = response.split(" ");
                                intent.putExtra("inspectionId", temp[1]);
                            }
                            else
                            {

                                intent.putExtra("inspectionId",response);
                            }
                            intent.putExtra("client_id", clientId);
                            intent.putExtra("client_name",client_id);
                            intent.putExtra("template_name",tempid);

                            if(response.equals("0")){
                                intent.putExtra("is_notemplate", "true");
                            }
                            else
                            {
                                intent.putExtra("is_notemplate", "false");
                            }
                            if(response.startsWith("defult")){

                                String temp[] ;
                                temp = response.split(" ");
                                intent.putExtra("template_id", temp[2]);
                            }
                            else
                            {
                                intent.putExtra("template_id", temId);
                            }


                            intent.putExtra("inspection_type", "old");
                            startActivity(intent);
                        } else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("There was an error creating Template")
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

                params.put("client_id", clientId);
                params.put("template_id", temId);
                params.put("default_template", default_template);
                params.put("text_paragraph",paraEt.getText().toString());
                params.put("pagecover", "");
                params.put("temp_name",temname);
                params.put("isDeault",isDefault);
                params.put("userID", id);

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

    public class CustomOnItemSelectedListener_client implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            if (client_list.get(pos).equals("No Records Founds")) {
                client_id = "0";
            }
            else {
                client_id = client_id_list.get(pos);

            }
            if (!client_list.get(pos).equals("Select")) {
                getTemplates();
            } else {
                //Toast.makeText(getContext(),"Plz select client",Toast.LENGTH_LONG).show();
            }


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
            }
            else if (inspection_list.get(pos).equals("No Template") && pos ==1) {
                temp_id = "";
                paraEt.setText("");
            }
            else {
               // if(!inspection_list.get(pos).equals("Select"))

                temp_id = inspection_id_list.get(pos);
                para = para_list.get(pos);

                paraEt.setText(para);

            }
            if(pos>0)
            {
                if (!(isStarted.get(tem_spinner.getSelectedItemPosition()).equals("0"))) {

                    review.setText("Continue");
                }
                else
                {
                    review.setText(" Start Inspection");
                }
            }
            else{
                review.setText(" Start Inspection");
            }




            getInspections(temp_id);


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }


}
