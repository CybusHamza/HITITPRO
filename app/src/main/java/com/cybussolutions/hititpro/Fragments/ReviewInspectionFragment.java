package com.cybussolutions.hititpro.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cybussolutions.hititpro.R;

import java.util.List;



public class ReviewInspectionFragment extends BaseFragment {

    View root;
    Spinner clientsSpinner,templateSpinner;
    String[]  clientsSpinnerValues,templateSpinnerValues,remainingValues;
    ListView remainingInspectionsListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_review_inspection,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Review Inspection");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

       /* clientsSpinner= (Spinner) root.findViewById(R.id.review_clients_spinner);
        templateSpinner=(Spinner) root.findViewById(R.id.review_templates_spinner);
        remainingInspectionsListview=(ListView) root.findViewById(R.id.remaining_listview);

        setSpinnerValues();
        setSpinnerAdapter();
        setSpinnerListener();
        ArrayAdapter remainingAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,remainingValues);
        remainingInspectionsListview.setAdapter(remainingAdapter);
*/
        return root;
    }
    private void setSpinnerValues()
    {
        clientsSpinnerValues= new String[]{"Zaeem sattar","Hamza bin Tariq","Noor shidiqqui"};
        templateSpinnerValues= new String []{"Housing template 1","Housing template 2","Housing template 3"};
        remainingValues= new String []{"1","2","3","4","5","6","7","8","9","10"};


    }
    private void setSpinnerListener()
    {
        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        templateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setSpinnerAdapter()
    {
        ArrayAdapter<String> dataAdapter;

        // 1st spinner
        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clientsSpinnerValues);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clientsSpinner.setAdapter(dataAdapter);

        // 2nd spinner

        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, templateSpinnerValues);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        templateSpinner.setAdapter(dataAdapter);
    }
}
