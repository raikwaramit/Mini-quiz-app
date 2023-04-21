package com.example.mcqquizapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.mcqquizapp.util.Utils;
import com.example.mcqquizapp.model.QuestionDataModel;
import com.example.mcqquizapp.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentQuestionDetailsScreen extends Fragment {

    private static int first = 1;//for the configuration change
    TextView question, optionA, optionB, optionC, optionD;//Instances of textview
    ImageView bookmarkImage;//Instance of Image for bookmark
    private static List<QuestionDataModel> dataList = new ArrayList<>();//List for storing the data Locally
    private int current = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Observer<Integer> countDownTimerObserver;//observer for Countdown timer
        Observer<Integer> currentItemClick;//observer for current item that is clicked in the recyclerView of QuestionListScreen fragment
        Observer<List<QuestionDataModel>> listObserver;//observer for storing the Fetched Data List Locally

        int orientation = this.getResources().getConfiguration().orientation;
        View view;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            view = inflater.inflate(R.layout.fragment_question_details_screen, container, false);
        } else {
            // code for landscape mode
            view = inflater.inflate(R.layout.fragment_question_details_screen_land, container, false);
        }
        TextView tv = view.findViewById(R.id.rem_time);
        Log.d("QuestionDetailsScreen", "Fragment created");//debug logs

        //initialising the objects
        bookmarkImage = view.findViewById(R.id.bookmark_image);
        question = view.findViewById(R.id.question_text);
        optionA = view.findViewById(R.id.option_a);
        optionB = view.findViewById(R.id.option_b);
        optionC = view.findViewById(R.id.option_c);
        optionD = view.findViewById(R.id.option_d);
        if (first == 0) {
            checkBookmarkstate();//checking bookmark for configuration change
            checkAlreadyAnswered();//checking options for configuration change
        }
        first = 0;

        //clickListener for Bookmark and storing the data directly to the QuestionDataList
        bookmarkImage.setOnClickListener(view1 -> {
            Log.d("QuestionDetailsScreen", "bookmark clicked-"+current);//debug logs
            if (dataList.get(current).isBookmarked) {
                bookmarkImage.setImageResource(R.drawable.bookmark_on);//setting the bookmark status on in background of ImageView
                dataList.get(current).isBookmarked = true;

            } else {
                bookmarkImage.setImageResource(R.drawable.bookmark_off);//setting the bookmark status off in background of ImageView
                dataList.get(current).isBookmarked = false;//changing the livedata status
            }
            Utils.viewModelClass.setListDataUpdated(dataList);
        });

        //listener when Option A is clicked
        optionA.setOnClickListener(view18 -> {
            Log.d("QuestionDetailsScreen", "option A clicked-"+current);//debug logs
            List<String> currentOption = dataList.get(current).getOptions();
            optionsBackground(0);
            dataList.get(current).setUserAnswer(currentOption.get(0));//setting the user answer in livedata List
            Utils.viewModelClass.setListDataUpdated(dataList);
        });
        //listener when Option B is clicked
        optionB.setOnClickListener(view17 -> {
            Log.d("QuestionDetailsScreen", "option B clicked-"+current);//debug logs
            List<String> currentOption = dataList.get(current).getOptions();
            optionsBackground(1);
            dataList.get(current).setUserAnswer(currentOption.get(1));//setting the user answer in livedata List
            Utils.viewModelClass.setListDataUpdated(dataList);

        });
        //listener when Option C is clicked
        optionC.setOnClickListener(view16 -> {
            Log.d("QuestionDetailsScreen", "option C clicked-"+current);//debug logs
            List<String> currentOption = dataList.get(current).getOptions();
            optionsBackground(2);
            dataList.get(current).setUserAnswer(currentOption.get(2));//setting the user answer in livedata List
            Utils.viewModelClass.setListDataUpdated(dataList);

        });
        //listener when Option D is clicked
        optionD.setOnClickListener(view15 -> {
            Log.d("QuestionDetailsScreen", "option D clicked-"+current);//debug logs
            List<String> currentOption = dataList.get(current).getOptions();
            optionsBackground(3);
            dataList.get(current).setUserAnswer(currentOption.get(3));//setting the user answer in livedata List
            Utils.viewModelClass.setListDataUpdated(dataList);
        });

        Button nextButton = view.findViewById(R.id.next_btn);
        Button prevButton = view.findViewById(R.id.previous_btn);
        Button submitButton = view.findViewById(R.id.submitbutton);

        //listener for previous button
        prevButton.setOnClickListener(view13 -> {

            //setting the enable disable functionality
            if (current > 0) {
                current--;
                if (nextButton.isEnabled() == false)
                    nextButton.setEnabled(true);
                clearBackground();
            }
            if(current==0)prevButton.setEnabled(false);
            printData(current);//print data of current question in question and options textView
            checkAlreadyAnswered();//calling  the method for checking if question is answered or not
            checkBookmarkstate();//calling method for Checking bookmarkStatus
            Log.d("QuestionDetailsScreen", "Prev Button clicked");//debug logs
        });
        //Listener for Next Button
        nextButton.setOnClickListener(view12 -> {
            //setting the enable disable functionality
            if (current < 9) {
                current++;
                if (prevButton.isEnabled() == false)
                    prevButton.setEnabled(true);
                clearBackground();
            }
            if(current==9)nextButton.setEnabled(false);
            printData(current);//print data of current question in question and options textView
            checkAlreadyAnswered();//calling  the method for checking if question is answered or not
            checkBookmarkstate();//calling method for Checking bookmarkStatus
            Log.d("QuestionDetailsScreen", "Next Button clicked");//debug logs
        });

        //listener for Submit Button
        submitButton.setOnClickListener(view14 -> {
            new Utils().createDialogSubmitBtn(getContext(), dataList);//creating the DialogBo
            Log.d("QuestionDetailsScreen", "Submit Button clicked");//debug logs
        });

        //Observer for CountDownTimer
        countDownTimerObserver = time -> {
            tv.setText((new Utils()).format(time));//setting the Formatted Live CountDownTimer in TimerTextView
        };
        Utils.viewModelClass.getCountDownTimer().observe(getViewLifecycleOwner(), countDownTimerObserver);

        //Observer For QuestionData List
        listObserver = questionDataModels -> dataList = questionDataModels;
        Utils.viewModelClass.getFetchedData().observe(getViewLifecycleOwner(), listObserver);


        //Observer for the ItemClick data
        currentItemClick = integer -> {
            current = integer;
            Log.d("Data", "Int-" + integer);//debug logs
            printData(current);
        };
        Utils.viewModelClass.getCurrentClicked().observe(getViewLifecycleOwner(), currentItemClick);
        return view;
    }

    //method for checking the Already answered question and shows the background different when option is already chosen
    public void checkAlreadyAnswered() {
        //logic for showing the answer option with different background if user Navigate to the same Question
        for (int i = 0; i < 4; i++)
            if (dataList.get(current).getOptions().get(i).equals(dataList.get(current).getUserAnswer()))
                optionsBackground(i);//set background of chosen option to be selected
    }

    public void checkBookmarkstate() {
        if (dataList.get(current).isBookmarked) {
            bookmarkImage.setImageResource(R.drawable.bookmark_on);//setting the bookmark status on in background of ImageView

        } else {
            bookmarkImage.setImageResource(R.drawable.bookmark_off);//setting the bookmark status off in background of ImageView
        }
    }

    //method for setting all option background to unselected
    private void clearBackground() {
        optionA.setBackgroundResource(R.drawable.layoutshapes);
        optionB.setBackgroundResource(R.drawable.layoutshapes);
        optionC.setBackgroundResource(R.drawable.layoutshapes);
        optionD.setBackgroundResource(R.drawable.layoutshapes);
    }

    //method for setting the background to selected if some item is already selected
    private void optionsBackground(int a) {
        switch (a) {
            case 0:
                optionA.setBackgroundResource(R.drawable.layoutshapesfill);
                break;
            case 1:
                optionB.setBackgroundResource(R.drawable.layoutshapesfill);
                break;
            case 2:
                optionC.setBackgroundResource(R.drawable.layoutshapesfill);
                break;
            case 3:
                optionD.setBackgroundResource(R.drawable.layoutshapesfill);
                break;
        }
        if (a != 0) {
            optionA.setBackgroundResource(R.drawable.layoutshapes);
        }
        if (a != 1) {
            optionB.setBackgroundResource(R.drawable.layoutshapes);
        }
        if (a != 2) {
            optionC.setBackgroundResource(R.drawable.layoutshapes);
        }
        if (a != 3) {
            optionD.setBackgroundResource(R.drawable.layoutshapes);
        }
    }


    //method for Printing the current list item data into TextViews
    private void printData(int currentData) {
        QuestionDataModel data = dataList.get(currentData);
        Log.d("Question", "" + data.getQuestion());//debug logs
        this.question.setText(data.getQuestion());
        this.optionA.setText(data.getOptions().get(0));
        this.optionB.setText(data.getOptions().get(1));
        this.optionC.setText(data.getOptions().get(2));
        this.optionD.setText(data.getOptions().get(3));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("QuestionDetailsScreen", "Fragment Destroyed");//debug logs
        Utils.viewModelClass.setListDataUpdated(dataList);//storing the updated Question Data
    }


}