package com.cybussolutions.hititpro.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cybussolutions.hititpro.Activities.Detailed_Activity_All_Screens;
import com.cybussolutions.hititpro.Model.Detailed_model;
import com.cybussolutions.hititpro.R;

import java.util.ArrayList;


public class Detailed_Adapter extends BaseAdapter {
    ArrayList<Detailed_model> arrayList;
    Context context;
    LayoutInflater inflter;
    View view;
    String[] dbEnterArray;
    AlertDialog b;

    public Detailed_Adapter(ArrayList<Detailed_model> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        view = inflter.inflate(R.layout.row_detailed, null);

        final CheckBox item = (CheckBox) view.findViewById(R.id.detailed_checkbox);
        ImageView edit = (ImageView) view.findViewById(R.id.edit);
        ImageView delete = (ImageView) view.findViewById(R.id.delete);

        dbEnterArray = new String[arrayList.size() + 1];

        String splitter = "%";
        String row[] = arrayList.get(position).getItemName().split(splitter);

            if (row[1] !=null && row[1].equals("1")) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }

            item.setText(row[0]);


            for (int i = 0; i < arrayList.size(); i++) {
                dbEnterArray[i] = arrayList.get(i).getItemName();
            }



        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String splitter = "%";
                String row[] = arrayList.get(position).getItemName().split(splitter);

                    if (b) {
                        dbEnterArray[position] = row[0] + "%1";
                    }
                    else {
                        dbEnterArray[position] = row[0] + "%0";
                    }
            }

        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.remove(position);
                        notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        }).show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editItem(position, item.getText().toString());

            }
        });


        return view;
    }

    public String[] getDbInsertArray() {

        return dbEnterArray;
    }

    void editItem(final int position, final String text) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = inflter.inflate(R.layout.add_dalogbox, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        // intializing variables
        final EditText Add = (EditText) dialogView.findViewById(R.id.add_ET);
        final Button to = (Button) dialogView.findViewById(R.id.add_BT);

        Add.setText(text);

        to.setText("Edit Item");

        b = dialogBuilder.create();
        b.show();

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clearing list view

                arrayList.clear();
                notifyDataSetChanged();

                for (int item = 0; item < dbEnterArray.length; item++) {
                    Detailed_model model = new Detailed_model();
                    model.setItemName(dbEnterArray[item]);

                    arrayList.add(model);

                    if (item == (dbEnterArray.length - 1)) {
                        arrayList.remove(position);

                        if (Add.getText().toString().equals(""))
                        {
                            Toast.makeText(context, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            model.setItemName(Add.getText().toString() + "%0");

                            arrayList.add(position, model);

                            arrayList.remove(arrayList.size() - 1);
                        }

                    }

                }

                notifyDataSetChanged();

                b.dismiss();

            }
        });


    }


}