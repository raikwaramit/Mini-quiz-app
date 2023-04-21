package com.example.mcqquizapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mcqquizapp.util.Utils;
import com.example.mcqquizapp.R;

public class FragmentSetupScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int orientation = this.getResources().getConfiguration().orientation;
        View view;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            view = inflater.inflate(R.layout.fragment_setup_screen, container, false);
        } else {
            // code for landscape mode
            view = inflater.inflate(R.layout.fragment_setup_screen_land, container, false);
        }
        Log.d("SetupScreen", "Fragment is created");//debug logs
        //creating instance of Button for Start_button
        Button startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.viewModelClass.startQuestionListScreen();//calling method for starting the QuestionListScreen
                Utils.viewModelClass.startCountDown();//calling method for starting the CountDownTimer of 3 Minutes
                Log.d("SetupScreen", "Start Button is clicked");//Debug logs
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}