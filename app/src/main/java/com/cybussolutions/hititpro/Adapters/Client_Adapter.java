package com.cybussolutions.hititpro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.cybussolutions.hititpro.Model.Clients_model;
import com.cybussolutions.hititpro.R;

import org.w3c.dom.Text;

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
        view = inflter.inflate(R.layout.row_client, null);

        TextView name= (TextView) view.findViewById(R.id.client_name);
        TextView adress= (TextView) view.findViewById(R.id.client_adress);

        name.setText(arrayList.get(position).getClient_name());
        adress.setText(arrayList.get(position).getClient_adress());


        return view;
    }
}
