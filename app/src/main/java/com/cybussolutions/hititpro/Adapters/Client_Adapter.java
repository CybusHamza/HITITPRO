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


import com.cybussolutions.hititpro.Activities.EditClient;
import com.cybussolutions.hititpro.Model.Clients_model;
import com.cybussolutions.hititpro.R;


import java.util.ArrayList;

public class Client_Adapter extends BaseAdapter {
    ArrayList<Clients_model> arrayList;
    Context context;
    LayoutInflater inflter;
    View view;


    public Client_Adapter(ArrayList<Clients_model> arrayList, Context context)
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
            v.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) v.getTag();
        }

        viewholder.name.setText(arrayList.get(position).getClient_name());
        viewholder.adress.setText(arrayList.get(position).getClient_adress());
        viewholder.phone_number.setText(arrayList.get(position).getClient_phone());
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

        class ViewHolder{
          protected TextView name,adress,phone_number;
          ImageView editClient;


    }
}
