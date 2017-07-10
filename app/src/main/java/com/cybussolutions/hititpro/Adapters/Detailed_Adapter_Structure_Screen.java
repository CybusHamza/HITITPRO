package com.cybussolutions.hititpro.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.cybussolutions.hititpro.Activities.Detailed_Activity_Structure_Screens;
import com.cybussolutions.hititpro.Activities.MainActivity;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Model.Checkbox_model;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Detailed_Adapter_Structure_Screen extends ArrayAdapter<Checkbox_model>
{

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    String data,defaultText,observation_comments="";
    private LayoutInflater layoutInflater;
    String[] dbEnterArray;
    private final List<Checkbox_model> list;
    private List<Checkbox_model> list_temp;
    Context context;
    AlertDialog b;
    String topass[],enteredStructure = "";
    static  int count=0;
    Database database;
    EditText subEditText;


    public Detailed_Adapter_Structure_Screen(Activity context, ArrayList<Checkbox_model> list, int resource,String topass[]) {
        super(context,resource,list);
        layoutInflater = LayoutInflater.from(context);
        this.list= list;
        this.context = context;
        this.topass= topass;
        database= new Database(context);


        dbEnterArray = new String[list.size() + 1];


        for (int i = 0; i < list.size(); i++) {
            dbEnterArray[i] = list.get(i).getTitle();
        }

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();

            convertView = layoutInflater.inflate(R.layout.row_detailed, parent, false);
            holder.setCheckBox((CheckBox) convertView
                    .findViewById(R.id.check));

            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.edit = (ImageView) convertView.findViewById(R.id.edit);
            holder.imageEditor = (ImageView) convertView.findViewById(R.id.imageEditor);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        SharedPreferences sp=context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if(sp.getBoolean("imageButton",true)!=true){
            try {
                ImageView iv= (ImageView) convertView.findViewById(R.id.imageEditor);
                iv.setVisibility(View.INVISIBLE);

            }catch (Exception e){
            }
        }
        if(sp.getBoolean("addObservationButton",true)==false){
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.edit = (ImageView) convertView.findViewById(R.id.edit);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.edit.setVisibility(View.INVISIBLE);
        }



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int t) {

                        list.clear();
                        for (int i = 0; i < dbEnterArray.length -1; i++) {
                            Checkbox_model model = new Checkbox_model();
                            model.setTitle(dbEnterArray[i]);

                            list.add(model);
                        }

                        list.remove(position);


                        dbEnterArray = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            dbEnterArray[i] = list.get(i).getTitle();
                        }

                        Intent intent= new Intent(getContext(), Detailed_Activity_Structure_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("items",dbEnterArray);
                        intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                        intent.putExtra("heading", topass[0]);
                        intent.putExtra("fromAddapter","edit");
                        intent.putExtra("column", topass[1]);
                        intent.putExtra("dbTable",topass[2]);
                        ((Activity)context).finish();
                        context.startActivity(intent);





                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        }).show();

            }
        });


        holder.imageEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("dbTable",topass[1]);
                    intent.putExtra("showImages","false");
                    context.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        });


        final Holder finalHolder = holder;
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editItem(position, finalHolder.getCheckBox().getText().toString());

            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                int position = (Integer) view.getTag();
                String name = checkBox.getText().toString().replaceAll("\\s+","");
                String observation_name="";
                int pagePosition= 0;
                switch (topass[2]){
                    case "portfolio":
                        pagePosition= 1;
                        observation_name="observation";
                        observation_comments="observation_comments";
                        break;
                    case "roofing":
                        pagePosition= 2;
                        observation_name="roofingobservations";
                        observation_comments="observations_comments";
                        break;
                    case "exterior":
                        pagePosition= 3;
                        observation_name="observation";
                        observation_comments="observations_comments";
                        break;
                    case "interior":
                        pagePosition= 9;
                        observation_name="Interior_Observations";
                        observation_comments="Interior_Observations";
                        break;
                    case "heating":
                        pagePosition= 5;
                        observation_name="Heating_Observations";
                        observation_comments="observation_comments";
                        break;
                    case "cooling":
                        pagePosition= 6;
                        observation_name="Heating_Observations";
                        observation_comments="heatobservation_comments";
                        break;
                    case "electrical":
                        pagePosition= 4;
                        observation_name="Electrical_Observations";
                        observation_comments="observation_comments";
                        break;
                    case "insulation":
                        pagePosition= 7;
                        observation_name="Insulation_Ventilation_Observations";
                        observation_comments="observations_comments";
                        break;
                    case "plumbing":
                        pagePosition= 8;
                        observation_name="Plumbing_Observations";
                        observation_comments="observation_comments";
                        break;
                    case "appliance":
                        pagePosition= 10;
                        observation_name="Appliance_Observations";
                        observation_comments="observations_comments";
                        break;
                    case "fireplaces":
                        pagePosition= 11;
                        observation_name="observations";
                        observation_comments="observations_comments";
                        break;
                    default:
                        pagePosition= 0;

                }
                data = pagePosition+"_"+observation_name+"_"+name;

                if((topass[0].equals("Exterior Observations")||topass[0].equals("Electrical Observations")||topass[0].equals("Structure Observations")
                        || topass[0].equals("Heating Observations") || topass[0].equals("Cooling/Heat Pump Observations")
                        || topass[0].equals("Interior Observations")  || topass[0].equals("Insulation / Ventilation Observations")
                        || topass[0].equals("Plumbing Observations")||  topass[0].equals("Roofing Observations") || topass[0].equals("Appliance Observations:")
                        || topass[0].equals("Fireplace / Wood Stove Observations:"))&& checkBox.isChecked()){

                        getDefaultComments();
                        getItem(position).setChecked(checkBox.isChecked());

                        showDialog(position, view);
                } else {
                    getItem(position).setChecked(checkBox.isChecked());

                    if (checkBox.isChecked()) {

                        int size = list.size();
                        list_temp = new ArrayList<>(list);
                        list.clear();
                        for (int i = 0; i < size; i++) {
                            String splitter = "%";
                            String row[] = list_temp.get(i).getTitle().split(splitter);

                            Checkbox_model model = new Checkbox_model();
                            if (i == position) {

                                model.setTitle(row[0] + "%1");
                            } else {
                                model.setTitle(row[0] + "%0");
                            }

                            list.add(model);
                        }


                        for (int i = 0; i < list.size(); i++) {
                            dbEnterArray[i] = list.get(i).getTitle();
                        }


                        Intent intent = new Intent(getContext(), Detailed_Activity_Structure_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("items", dbEnterArray);
                        intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                        intent.putExtra("heading", topass[0]);
                        intent.putExtra("fromAddapter", "true");
                        intent.putExtra("column", topass[1]);
                        intent.putExtra("dbTable", topass[2]);
                        ((Activity) context).finish();
                        context.startActivity(intent);


                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            String row1[] = list.get(i).getTitle().split("%");
                            dbEnterArray[i] = row1[0] + "%0";
                        }
                    }
                }
            }
        });



        String splitter = "%";
        String rows[] = list.get(position).getTitle().split(splitter);

        final Holder finalHolder1 = holder;




        final Checkbox_model row = getItem(position);
        holder.getCheckBox().setText(rows[0]);
        holder.getCheckBox().setTag(position);
        holder.getCheckBox().setChecked(row.isChecked());






        return convertView;
    }

    private void showDialog(final int position,final View v) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View subView = inflater.inflate(R.layout.dilogue_layout, null);
       subEditText = (EditText)subView.findViewById(R.id.dialogEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Observation Comment");
        builder.setMessage("Comment:");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CheckBox checkBox = (CheckBox)v ;

                if (checkBox.isChecked()) {

                    int size = list.size();
                    list_temp = new ArrayList<>(list);
                    list.clear();
                    for (int i = 0; i < size; i++) {
                        String splitter = "%";
                        String row[] = list_temp.get(i).getTitle().split(splitter);

                        Checkbox_model model = new Checkbox_model();
                        if (i == position) {

                            model.setTitle(row[0] + "%1");
                        } else {
                            model.setTitle(row[0] + "%0");
                        }

                        list.add(model);
                    }


                    for (int i = 0; i < list.size(); i++) {
                        dbEnterArray[i] = list.get(i).getTitle();
                    }


                    Intent intent = new Intent(getContext(), Detailed_Activity_Structure_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("items", dbEnterArray);
                    intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                    intent.putExtra("heading", topass[0]);
                    intent.putExtra("fromAddapter", "true");
                    intent.putExtra("column", topass[1]);
                    intent.putExtra("dbTable", topass[2]);
                    ((Activity) context).finish();
                    context.startActivity(intent);


                } else {
                    for (int i = 0; i < list.size(); i++) {
                        String row1[] = list.get(i).getTitle().split("%");
                        dbEnterArray[i] = row1[0] + "%0";
                    }
                }
                saveObservationComments();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckBox checkBox = (CheckBox)v ;

                if (checkBox.isChecked()) {

                    int size = list.size();
                    list_temp = new ArrayList<>(list);
                    list.clear();
                    for (int i = 0; i < size; i++) {
                        String splitter = "%";
                        String row[] = list_temp.get(i).getTitle().split(splitter);

                        Checkbox_model model = new Checkbox_model();
                        if (i == position) {

                            model.setTitle(row[0] + "%1");
                        } else {
                            model.setTitle(row[0] + "%0");
                        }

                        list.add(model);
                    }


                    for (int i = 0; i < list.size(); i++) {
                        dbEnterArray[i] = list.get(i).getTitle();
                    }


                    Intent intent = new Intent(getContext(), Detailed_Activity_Structure_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("items", dbEnterArray);
                    intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                    intent.putExtra("heading", topass[0]);
                    intent.putExtra("fromAddapter", "true");
                    intent.putExtra("column", topass[1]);
                    intent.putExtra("dbTable", topass[2]);
                    ((Activity) context).finish();
                    context.startActivity(intent);


                } else {
                    for (int i = 0; i < list.size(); i++) {
                        String row1[] = list.get(i).getTitle().split("%");
                        dbEnterArray[i] = row1[0] + "%0";
                    }
                }
                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();



    }

    private void saveObservationComments() {
        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.SAVE_OBSERVATION_COMMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // ringProgressDialog.dismiss();
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
                params.put("observation_comments",subEditText.getText().toString());
                params.put("template_id",StructureScreensActivity.template_id);
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("table_name",topass[2]);
                params.put("observations_comments",observation_comments);
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


    public String[] getDbInsertArray() {

        return dbEnterArray;
    }



    static class Holder {
        TextView textViewTitle;
        TextView textViewSubtitle;
        CheckBox checkBox;
        ImageView edit, delete,imageEditor;



        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

    }



    void editItem(final int position, final String text) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = layoutInflater.inflate(R.layout.add_dalogbox, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        // intializing variables
        final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
        final Button to = (Button) dialogView.findViewById(R.id.add_BT);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);




        Add.setText(text);
        Add.setSelection(Add.getText().length());

        to.setText("Edit Item");

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        imm.showSoftInput(Add, InputMethodManager.SHOW_IMPLICIT);
        b = dialogBuilder.create();
        b.show();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clearing list view


			/*	for (int i = 0; i < dbEnterArray.length - 1; i++) {
					enteredStructure += dbEnterArray[i] + "^";
				}

				enteredStructure = enteredStructure.substring(0, enteredStructure.length() - 1);

				// Insert in local DataBase*/

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are You Sure!")
                        .setConfirmText("OK").setContentText("If you edit this label , associated images will be removed")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                list.clear();

                                for (int item = 0; item < dbEnterArray.length; item++) {
                                    Checkbox_model model = new Checkbox_model();
                                    model.setTitle(dbEnterArray[item]);

                                    list.add(model);

                                    if (item == (dbEnterArray.length - 1)) {
                                        list.remove(position);

                                        if (Add.getText().toString().equals("")) {
                                            Toast.makeText(context, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            model.setTitle(Add.getText().toString() + "%0");

                                            list.add(position, model);

                                            list.remove(list.size() - 1);

                                            for (int i = 0; i < list.size(); i++) {
                                                dbEnterArray[i] = list.get(i).getTitle();
                                            }

                                        }

                                    }

                                }

                                Intent intent= new Intent(getContext(), Detailed_Activity_Structure_Screens.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("items",dbEnterArray);
                                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                                intent.putExtra("heading", topass[0]);
                                intent.putExtra("fromAddapter","true");
                                intent.putExtra("column", topass[1]);
                                intent.putExtra("dbTable",topass[2]);
                                ((Activity)context).finish();
                                context.startActivity(intent);

                                b.dismiss();
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);
                            }
                        })
                        .show();



            }
        });


    }
    public void getDefaultComments() {

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_DEFAULT_COMMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject object =jsonArray.getJSONObject(i);
                                defaultText=object.getString("defaulttext");
                                subEditText.setText(defaultText);
                               // String test=defaultText;
                                //etComments.setText(defaultText);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // ringProgressDialog.dismiss();
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
                params.put("fieldid",data);
                params.put("template_id",StructureScreensActivity.template_id);
                params.put("inspection_id",StructureScreensActivity.inspectionID);
                params.put("client_id",StructureScreensActivity.client_id);
                params.put("table_name",topass[2]);
                params.put("observation_comments",observation_comments);
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