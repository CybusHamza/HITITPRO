package com.cybussolutions.hititpro.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cybussolutions.hititpro.Model.PDFModel;
import com.cybussolutions.hititpro.R;

import java.util.ArrayList;

/**
 * Created by Hamza Android on 7/14/2017.
 */

public class PDFAddapter extends ArrayAdapter<PDFModel>
{
    LayoutInflater inflter;
    View view;
    TextView name,date,client;
    Context context;
    ArrayList<PDFModel> pdfModels;
    public PDFAddapter(@NonNull Context context, @LayoutRes int resource , ArrayList<PDFModel> list) {
        super(context, resource);

        this.context= context;
        this.pdfModels= list;
    }

    @Override
    public int getCount() {
        return pdfModels.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.pdf_row, null);

        name= (TextView) view.findViewById(R.id.pdf_name);
        date= (TextView) view.findViewById(R.id.date_created);
        client= (TextView) view.findViewById(R.id.client);

        PDFModel pdfModel = pdfModels.get(position);

        name.setText(pdfModel.getName());
        date.setText(pdfModel.getDate());
        client.setText(pdfModel.getClient());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m1.cybussolutions.com/hititpro/uploads/reports/"+pdfModels.get(position).getName()));
                context. startActivity(browserIntent);
            }
        });
        return view;

    }
}
