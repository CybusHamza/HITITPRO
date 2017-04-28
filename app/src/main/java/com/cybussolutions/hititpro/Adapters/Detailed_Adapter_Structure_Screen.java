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

import com.cybussolutions.hititpro.Activities.MainActivity;
import com.cybussolutions.hititpro.Model.Checkbox_model;
import com.cybussolutions.hititpro.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Detailed_Adapter_Structure_Screen extends ArrayAdapter<Checkbox_model> {

    private final ArrayList<Checkbox_model> list;
    private ArrayList<Checkbox_model> list_temp;
    private final Activity context;
    LayoutInflater inflter;
    String[] dbEnterArray;
    AlertDialog b;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    private int selectedPosition = -1;

    public Detailed_Adapter_Structure_Screen(Activity context, ArrayList<Checkbox_model> list, int resource) {
        super(context,resource,list);
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;

            inflter = context.getLayoutInflater();
            convertView = inflter.inflate(R.layout.row_detailed, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.check);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
            viewHolder.edit = (ImageView) convertView.findViewById(R.id.edit);
            viewHolder.imageEditor = (ImageView) convertView.findViewById(R.id.imageEditor);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.label, viewHolder.text);
            convertView.setTag(R.id.check, viewHolder.checkbox);
       


        SharedPreferences sp=context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if(sp.getBoolean("imageButton",true)!=true){
            try {
                ImageView iv= (ImageView) convertView.findViewById(R.id.imageEditor);
                iv.setVisibility(View.INVISIBLE);

            }catch (Exception e){
            }

        }



        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Here we get the position that we have set for the checkbox using setTag.


                if (viewHolder.checkbox.isChecked()) {

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


                    notifyDataSetChanged();


                }

                else {
                    for(int i=0;i<list.size();i++){
                        String row1[]=list.get(i).getTitle().split("%");
                        dbEnterArray[i] = row1[0] + "%0";
                    }
                }

            }

        });



        viewHolder.checkbox.setTag(position); // This line is important.
//
//        viewHolder.checkbox.setChecked(list.get(position).isSelected());

        dbEnterArray = new String[list.size() + 1];

        String splitter = "%";
        String row[] = list.get(position).getTitle().split(splitter);

        if (row[1] != null && row[1].equals("1")) {
            viewHolder.checkbox.setChecked(true);
        } else {
            viewHolder.checkbox.setChecked(false);
        }

        viewHolder.text.setText(row[0]);
        for (int i = 0; i < list.size(); i++) {
            dbEnterArray[i] = list.get(i).getTitle();
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(position);
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
        viewHolder.imageEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent(context,MainActivity.class);

                    context.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        });

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editItem(position, finalViewHolder.text.getText().toString());

            }
        });


        return convertView;
    }



    public String[] getDbInsertArray() {

        return dbEnterArray;
    }





    void editItem(final int position, final String text) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = inflter.inflate(R.layout.add_dalogbox, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

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

                list.clear();
                notifyDataSetChanged();

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
                        }

                    }

                }

                notifyDataSetChanged();

                b.dismiss();

            }
        });


    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected ImageView edit, delete,imageEditor;
    }


}