package com.cybussolutions.hititpro.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cybussolutions.hititpro.R;

/**
 * Created by Stack on 1/17/2017.
 */

public class LogoutFragment extends BaseFragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_logout,container,false);

        return root;
    }
}
