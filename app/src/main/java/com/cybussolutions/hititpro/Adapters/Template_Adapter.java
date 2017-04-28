package com.cybussolutions.hititpro.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cybussolutions.hititpro.Activities.EditClient;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Model.Clients_model;
import com.cybussolutions.hititpro.Model.Templates_model;
import com.cybussolutions.hititpro.R;


import java.util.ArrayList;

public class Template_Adapter extends BaseAdapter {
    ArrayList<Templates_model> arrayList;
    Context context;
    LayoutInflater inflter;
    View view;


    public Template_Adapter(ArrayList<Templates_model> arrayList, Context context)
    {
        this.arrayList=arrayList;
        this.context=context;
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
            viewholder.name.setTypeface(face, Typeface.BOLD);
            viewholder.name.setTextSize(17);
            viewholder.inspection.setTypeface(face);
            viewholder.template.setTypeface(face, Typeface.BOLD);
            viewholder.template.setTextSize(15);
            viewholder.editTemplate=(ImageView)v.findViewById(R.id.editTemplate);
            viewholder.deleteTemplate=(ImageView)v.findViewById(R.id.deleteTemplate);
            v.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) v.getTag();
        }

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
                intent.putExtra("inspection_type", "old");
                context.startActivity(intent);
            }
        });
        viewholder.deleteTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"deleted",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    class ViewHolder{
        protected TextView name,template,inspection;
        ImageView editTemplate,deleteTemplate;


    }
}
