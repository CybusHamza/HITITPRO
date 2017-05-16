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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cybussolutions.hititpro.Activities.Detailed_Activity_All_Screens;
import com.cybussolutions.hititpro.Activities.Detailed_Activity_Structure_Screens;
import com.cybussolutions.hititpro.Activities.MainActivity;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Model.Checkbox_model;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Detailed_Adapter_Structure_Screen extends ArrayAdapter<Checkbox_model>
{


    private LayoutInflater layoutInflater;
    String[] dbEnterArray;
    private final List<Checkbox_model> list;
    private List<Checkbox_model> list_temp;
    Context context;
    AlertDialog b;
    String topass[],enteredStructure = "";
    static  int count=0;
    Database database;


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
                getItem(position).setChecked(checkBox.isChecked());



                if (checkBox.isChecked()) {

                    int size = list.size();
                    list_temp = new ArrayList<>(list);
                    list.clear();
                    for(int i=0;i<size;i++){
                        String splitter = "%";
                        String row[] = list_temp.get(i).getTitle().split(splitter);

                        Checkbox_model model = new Checkbox_model();
                        if (i == position) {

                            model.setTitle(row[0]+"%1");
                        }
                        else
                        {
                            model.setTitle(row[0]+"%0");
                        }

                        list.add(model);
                    }


                    for (int i = 0; i < list.size(); i++) {
                        dbEnterArray[i] = list.get(i).getTitle();
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


                }

                else {
                    for(int i=0;i<list.size();i++){
                        String row1[]=list.get(i).getTitle().split("%");
                        dbEnterArray[i] = row1[0] + "%0";
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
        Add.setSelection(Add.getText().length());

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Add, InputMethodManager.SHOW_IMPLICIT);

        Add.setText(text);

        to.setText("Edit Item");

        b = dialogBuilder.create();
        b.show();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
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

            }
        });


    }




}