package com.example.mcqquizapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.mcqquizapp.util.Utils;
import com.example.mcqquizapp.model.QuestionDataModel;
import com.example.mcqquizapp.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentSummaryScreen extends Fragment {
    TextView timerTV, scoreTV;//Instance of TextView
    Button restartBtn, exitBtn;//Instance of button
    List<QuestionDataModel> dataList = new ArrayList<>();//ArrayList for Storing the Question Data list Locally


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int orientation = this.getResources().getConfiguration().orientation;
        View view;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            view = inflater.inflate(R.layout.fragment_summary_screen, container, false);
        } else {
            // code for landscape mode
            view = inflater.inflate(R.layout.fragment_summary_screen_land, container, false);
        }
        Observer<Integer> countDownTimerObserver;//observer for CountDownTimer
        Observer<List<QuestionDataModel>> listObserver;//Observer for QuestionData List
        timerTV = view.findViewById(R.id.rem_time);
        scoreTV = view.findViewById(R.id.scorevalue);
        restartBtn = view.findViewById(R.id.restart_btn);
        exitBtn = view.findViewById(R.id.exit_btn);
        Log.d("SummaryScreen", "Fragment is created");//Debug logs

        //observer of CountDownTimer
        countDownTimerObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer time) {
                timerTV.setText((new Utils()).format(time));
                //setting the data of Timer TextView to current timer formatted by the Custom Function to convert int to MM:SS format
            }
        };
        Utils.viewModelClass.getCountDownTimer().observe(getViewLifecycleOwner(), countDownTimerObserver);

        //Observer for the QuestionData
        listObserver = new Observer<List<QuestionDataModel>>() {
            @Override
            public void onChanged(List<QuestionDataModel> questionDataModels) {
                dataList = questionDataModels;//saving the response of data change
                float count = 0;//setting the score count =0;

                //logic for checking the number of correct Answers
                for (int index = 0; index < questionDataModels.size(); index++) {
                    QuestionDataModel itemData = questionDataModels.get(index);
                    if (!itemData.getUserAnswer().equals("")) {
                        if (itemData.getUserAnswer().equals(itemData.getCorrectOption()))
                            count++;//adding 1 on correct answer
                        else
                            count -= 0.5;//subtracting -1/2 if answer is wrong
                    }
                }
                scoreTV.setText(count + "/10");//printing the Score in textView
            }
        };
        Utils.viewModelClass.getFetchedData().observe(getViewLifecycleOwner(), listObserver);

        //when Restart Button is Pressed
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.viewModelClass.startSetupScreen();//calling method for starting the SetupScreen
                Log.d("SummaryScreen", "Restart button is clicked");//Debug logs
            }
        });

        //when Exit button is pressed
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finishAffinity();
                Log.d("SummaryScreen", "Exit button is clicked");//Debug logs
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SummaryScreen", "Fragment is destroyed");//Debug logs
    }
}