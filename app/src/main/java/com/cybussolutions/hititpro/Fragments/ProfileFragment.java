package com.cybussolutions.hititpro.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cybussolutions.hititpro.Activities.Edit_Profile;
import com.cybussolutions.hititpro.R;



public class ProfileFragment extends BaseFragment {

    View root;
    TextView name,adress,contact,email;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_profile,container,false);

        // set toolbar with main activity
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);




        name = (TextView) root.findViewById(R.id.nameTV);
        email = (TextView) root.findViewById(R.id.emailtv);
        contact = (TextView) root.findViewById(R.id.numbertv);
        adress = (TextView) root.findViewById(R.id.adresstv);


        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        final String struserName = pref.getString("user_name", null);
        final String struserEmail = pref.getString("email", null);
        final String strcontact = pref.getString("phone", null);
        final String stradress = pref.getString("adress", null);

        name.setText(struserName);
        email.setText(struserEmail);
        contact.setText(strcontact);
        adress.setText(stradress);


        FloatingActionButton fab = (FloatingActionButton) root. findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Edit_Profile.class);
                intent.putExtra("user_name",struserName);
                intent.putExtra("email",struserEmail);
                intent.putExtra("phone",strcontact);
                intent.putExtra("adress",stradress);
                startActivity(intent);
//              Snackbar.make(view, "Hellooooo !!!!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        return root;
    }
}
