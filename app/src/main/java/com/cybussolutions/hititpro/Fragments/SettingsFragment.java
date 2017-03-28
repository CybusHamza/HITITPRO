package com.cybussolutions.hititpro.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cybussolutions.hititpro.R;

/**
 * Created by Stack on 1/17/2017.
 */

public class SettingsFragment extends BaseFragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_settings,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        return root;
    }
}
