package com.example.mcqquizapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.example.mcqquizapp.model.QuestionDataModel;
import com.example.mcqquizapp.model.QuizzyViewModel;

import java.util.List;

/**
 * Custom class for some important objects and methods.
 */
public class Utils {

    // Static object of essential used for all the Operation
    public static QuizzyViewModel viewModelClass;

    // To format time in second into MM:SS format.
    public String format(int count) {
        int minutes = count / 60;
        String finalTime = "0" + minutes + ":";
        int seconds = count % 60;
        if (seconds / 10 == 0)
            finalTime += "0";
        finalTime += seconds;
        return finalTime;
    }

    // To create a dialog box with some text, primary and secondary button.
    public void createDialogSubmitBtn(Context context, List<QuestionDataModel> dataList) {

        // Creating new alert dialog for taking user consent for submission.
        new AlertDialog.Builder(context).setMessage("Are you sure you want to Submit the test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Utils", "Dialog Box Yes Clicked");

                        // Calling method for starting the SummaryScreen Fragment.
                        viewModelClass.startSummaryScreen();

                        // Calling method for updating the data List Correctly.
                        viewModelClass.setListDataUpdated(dataList);

                        // Invokes the final score screen.
                        viewModelClass.finalScore();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Utils", "Dialog Box No clicked");

                        dialogInterface.dismiss();
                    }
                }).show();
        Log.d("Utils", "Dialog Box Created");
    }

}
