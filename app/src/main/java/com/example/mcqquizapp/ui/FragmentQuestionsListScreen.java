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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcqquizapp.util.ContactAdapter;
import com.example.mcqquizapp.util.Utils;
import com.example.mcqquizapp.model.QuestionDataModel;
import com.example.mcqquizapp.R;

import java.util.ArrayList;
import java.util.List;

//FragmentQuestionListScreen Fragment
public class FragmentQuestionsListScreen extends Fragment {

    private static List<QuestionDataModel> dataList = new ArrayList<>();//List for storing the list data Locally
    private ContactAdapter contactAdapterClass;//Object of Custom AdapterClass

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int orientation = this.getResources().getConfiguration().orientation;
        View view;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            view = inflater.inflate(R.layout.fragment_questions_list_screen, container, false);
        } else {
            // code for landscape mode
            view = inflater.inflate(R.layout.fragment_questions_list_screen_land, container, false);
        }
        Observer<Integer> countDownTimerObserver;//Observer for CountDownTimer
        Observer<List<QuestionDataModel>> listObserver;//Observer For ListData
        TextView tv = view.findViewById(R.id.rem_time);
        RecyclerView recyclerView = view.findViewById(R.id.question_list);
        Button submitButton = view.findViewById(R.id.submitbutton);
        Log.d("QuestionListScreen", "Fragment is created");//debug logs

        //Listener for SubmitButton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utils().createDialogSubmitBtn(getContext(), dataList);//creating the DialogBox
                Log.d("QuestionListScreen", "Submit button is clicked");//debug logs
            }
        });


        //observer for Countdown timer
        countDownTimerObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer time) {
                tv.setText((new Utils()).format(time));//Setting the data of timer TextView to current remaining time
            }
        };
        Utils.viewModelClass.getCountDownTimer().observe(getViewLifecycleOwner(), countDownTimerObserver);

        //observer for QuestionData List
        listObserver = new Observer<List<QuestionDataModel>>() {
            @Override
            public void onChanged(List<QuestionDataModel> questionDataModels) {
                dataList = questionDataModels;
                //method for Setting the new adapterClass ,setting LayoutManager, ItemDecoration, etc to RecyclerView
                contactAdapterClass = new ContactAdapter(questionDataModels);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//setting the layout Manager
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));//setting the ItemDecoration
                recyclerView.setItemAnimator(new DefaultItemAnimator());//Setting the ItemAnimator
                recyclerView.setAdapter(contactAdapterClass);//Setting the custom AdapterClass object to RecyclerView
                Log.d("QuestionListScreen", "data is successfully shown in RecyclerView");//debug logs
            }
        };
        Utils.viewModelClass.getFetchedData().observe(getViewLifecycleOwner(), listObserver);
        // Inflate the layout for this fragment
        return view;

    }

}