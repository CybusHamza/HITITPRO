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

import java.util.List;


public class Detailed_Adapter_Structure_Screen extends ArrayAdapter<Checkbox_model> {

    int mSelectedIndex = -1;
    private final List<Checkbox_model> list;
    private final Activity context;
    LayoutInflater inflter;
    String[] dbEnterArray;
    AlertDialog b;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    private int selectedPosition = -1;
    boolean test=false;

    public Detailed_Adapter_Structure_Screen(Activity context, List<Checkbox_model> list, int resource) {
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
        if (convertView == null) {
            inflter = context.getLayoutInflater();
            convertView = inflter.inflate(R.layout.structure_observations, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);
            viewHolder.checkbox = (RadioButton) convertView.findViewById(R.id.check);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
            viewHolder.edit = (ImageView) convertView.findViewById(R.id.edit);
            viewHolder.imageEditor = (ImageView) convertView.findViewById(R.id.imageEditor);
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
                public void onClick(View v) {
                    String splitter = "%";
                    String row[] = list.get(position).getName().split(splitter);
                    for(int i=0;i<list.size();i++){
                        String row1[]=list.get(i).getName().split("%");
                        dbEnterArray[i] = row1[0] + "%0";
                        viewHolder.checkbox.setChecked(false);
                        if (i == position) {
                            dbEnterArray[i]=row[0]+"%1";
                            viewHolder.checkbox.setChecked(true);

                        }
                    }

                }
            });
//            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    String splitter = "%";
//                    //setSelectedIndex((Integer) buttonView.getTag());
//                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
//                    String row[] = list.get(getPosition).getName().split(splitter);
//                    list.get(getPosition).setSelected((buttonView.isChecked())); // Set the value of checkbox to maintain its state.
//                    //Boolean test=buttonView.isChecked();
//                    //Toast.makeText(context,String.valueOf(buttonView.isChecked()),Toast.LENGTH_LONG).show();
//
//
//
//               }
//
//            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.label, viewHolder.text);
            convertView.setTag(R.id.check, viewHolder.checkbox);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setTag(position); // This line is important.

        viewHolder.checkbox.setChecked(list.get(position).isSelected());

        dbEnterArray = new String[list.size() + 1];
        String splitter = "%";

        String row[] = list.get(position).getName().split(splitter);
        if (row[1] != null && row[1].equals("1")) {
            viewHolder.checkbox.setChecked(true);
            dbEnterArray[position]=row[0]+"%1";
        } else {
            viewHolder.checkbox.setChecked(false);
            dbEnterArray[position]=row[0]+"%0";
        }


        viewHolder.text.setText(row[0]);
        for (int i = 0; i < list.size(); i++) {
            dbEnterArray[i] = list.get(i).getName();
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
        String splitter = "%";
        String row[] = list.get(mSelectedIndex).getName().split(splitter);
        dbEnterArray[mSelectedIndex]=row[0]+"%1";
        return dbEnterArray;
    }
    public void setSelectedIndex(int index)
    {
        mSelectedIndex = index;
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
                    model.setName(dbEnterArray[item]);

                    list.add(model);

                    if (item == (dbEnterArray.length - 1)) {
                        list.remove(position);

                        if (Add.getText().toString().equals("")) {
                            Toast.makeText(context, "Please Enter Some Data !!", Toast.LENGTH_SHORT).show();
                        } else {
                            model.setName(Add.getText().toString() + "%0");

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
        protected RadioButton checkbox;
        protected ImageView edit, delete,imageEditor;
    }


}