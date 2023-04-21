package com.example.mcqquizapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mcqquizapp.R;

public class FragmentSplashScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int orientation = this.getResources().getConfiguration().orientation;
        View view;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        } else {
            // code for landscape mode
            view = inflater.inflate(R.layout.fragment_splash_screen_land, container, false);
        }

        Log.d("SplashScreen", "Fragment Button is created");//Debug logs
        // Inflate the layout for this fragment
        return view;
    }
}