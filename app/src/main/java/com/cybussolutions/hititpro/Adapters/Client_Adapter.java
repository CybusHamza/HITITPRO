package com.cybussolutions.hititpro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder viewholder = null;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.row_client, null);
            viewholder = new ViewHolder();
            viewholder.name = (TextView) v.findViewById(R.id.client_name);
            viewholder.adress = (TextView) v.findViewById(R.id.client_adress);
            viewholder.phone_number=(TextView)v.findViewById(R.id.client_phone);
            v.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) v.getTag();
        }

        viewholder.name.setText(arrayList.get(position).getClient_name());
        viewholder.adress.setText(arrayList.get(position).getClient_adress());
        viewholder.phone_number.setText(arrayList.get(position).getClient_phone());

        return v;
    }

      class ViewHolder{
          protected TextView name,adress,phone_number;


    }
}
