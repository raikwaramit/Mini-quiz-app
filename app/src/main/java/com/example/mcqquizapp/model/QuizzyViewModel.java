package com.example.mcqquizapp.model;

import android.app.Application;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Viewmodel class for storing and doing the the network operation.
 */
public class QuizzyViewModel extends AndroidViewModel {

    //a static variable for doing the countDown Operation
    private static CountDownTimer count;
    Application application;

    //Mutable Live data for Changing and holding the Fragment States.
    private MutableLiveData<String> fragmentState = new MutableLiveData<>();

    //Mutable Live data for the countdown timer.
    private MutableLiveData<Integer> countDownTimer = new MutableLiveData<>();

    //Mutable Live data for Fetching the data for Internet and holding the same data.
    private MutableLiveData<List<QuestionDataModel>> questionData = new MutableLiveData<>();

    //Mutable Live data for holding the state of ItemClicked in RecyclerView List.
    private MutableLiveData<Integer> currentClicked = new MutableLiveData<>();
    private RequestQueue requestQueue;//For Making the Volley Requests

    // initial operation when app starts.
    public QuizzyViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        fragmentState.postValue("SPLASH_SCREEN");

        //calling the runSplash() method running the Splash Screen for 1.5 second
        runSplash();
        getQuestionData();//calling this function for fetching the Question data From internet using Volley Request
    }

    //method for showing the Splash Screen for 1.5 sec when app starts
    private void runSplash() {
        //initialising the countdownTimer for 1.5 second
        count = new CountDownTimer(1500, 500) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                //changing the fragmentState to SETUP_SCREEN
                fragmentState.postValue("SETUP_SCREEN");
                Log.d("splash", "Successful");
                Log.d("ViewModelClass", "SplashScreen time up");
            }
        }.start();
    }

    //method for changing fragment State
    public void startQuestionListScreen() {
        fragmentState.postValue("QUESTIONLIST_SCREEN");
    }

    public void startQuestionDetailsScreen() {
        fragmentState.postValue("QUESTIONDETAIL_SCREEN");
    }

    public void startSummaryScreen() {
        fragmentState.postValue("SUMMARY_SCREEN");
    }

    //method for Providing the Live data of the fragment State to the Observer
    public MutableLiveData<String> getFragmentState() {
        return fragmentState;
    }

    //method for starting the CountDown timer when user Starts the game
    public void startCountDown() {
        //count initialised with new object of CountDownTimer of 180 second i.e., 3 minuntes
        count = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long l) {
                //changing the livedata value with each tick of 1 sec
                countDownTimer.postValue((int) l / 1000);
            }

            @Override
            public void onFinish() {
                //when time get Over.
                finalScore();//calling method final score for stopping the clock and showing the Time Taken by user
                startSummaryScreen();//calling method for starting the Summary Screen
                Log.d("ViewModelClass", "Quiz time Over");//Debug logs
            }
        }.start();
    }

    //getter method for providing the live data to observer of countdowntimer
    public MutableLiveData<Integer> getCountDownTimer() {
        return countDownTimer;
    }

    //method for making the Volley request for fetching the data from the given URL.
    private void getQuestionData() {
        requestQueue = Volley.newRequestQueue(getApplication());//Object of newRequestQueue for making a JSONObjectRequest
        List<QuestionDataModel> dataFetched = new ArrayList<>();//List for Storing the Fetched Data
        String url = "https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json";//String URL

        //JSONIbjectRequest for data Fetching
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //function called when recieves response
                try {
                    //parsing hte JSON object to List of QuestionDataModel
                    JSONArray array = response.getJSONArray("questions");
                    for (int index1 = 0; index1 < array.length(); index1++) {
                        JSONObject single = array.getJSONObject(index1);
                        int id = single.getInt("id");
                        String question = single.getString("question");
                        List<String> option = new ArrayList<>();
                        JSONArray optionArray = single.getJSONArray("options");
                        for (int index2 = 0; index2 < optionArray.length(); index2++) {
                            String optString = optionArray.getString(index2);
                            option.add(optString);
                        }
                        String correctOption = option.get(single.getInt("correct_option"));//saving the Correct Option String so that Shuffling can be supported
                        Collections.shuffle(option);//for Shuffling the Options
                        dataFetched.add(new QuestionDataModel(id, question, option, correctOption));//adding the each parsed item in List
                        Log.d("ViewModelClass", "Data Successfully received");//Debug logs
                    }
                    Log.d("Data", "successfull");
                    Collections.shuffle(dataFetched);//for shuffling the List Item in List
                    questionData.postValue(dataFetched);//changing the live data of Questiondata into Datafetched
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ViewModelClass", "Error occured in data Fetching");//Debug logs
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //toast if internet is not Connected or any error occured while fetching
                Toast.makeText(getApplication(), "Questions data not found!", Toast.LENGTH_SHORT).show();
                Log.d("ViewModelClass", "Error occured in data Fetching");//Debug logs
            }
        });
        request.setTag("fetchData");
        requestQueue.add(request);//adding the request to requestQueue
    }

    //setter for the updation of QuestionData LiveData List
    public void setListDataUpdated(List<QuestionDataModel> List) {
        questionData.postValue(List);
    }

    //getter for observer of this Mutable LiveData
    public MutableLiveData<List<QuestionDataModel>> getFetchedData() {
        return questionData;
    }

    //method for storing the item Click Index
    public void itemClicked(int pressed) {
        currentClicked.postValue(pressed);
    }

    //getter for the observer of the Current item in the questionDetailsScreen
    public MutableLiveData<Integer> getCurrentClicked() {
        return currentClicked;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        requestQueue.cancelAll("fetchData");//Cancelling the request when App closes
        fragmentState.setValue(null);//setting the fragment state to null
        countDownTimer.setValue(null);//setting the countDowntimer to null
    }

    //method for the final score in summary Screen
    public void finalScore() {
        count.cancel();//stopping the timer
        count = null;//setting the count to null, in case for restart Operation
        countDownTimer.postValue(180 - countDownTimer.getValue());//Posting the Used time By user.
    }

    //method for starting the setupScreen after the restart Operation
    public void startSetupScreen() {
        fragmentState.postValue("SETUP_SCREEN");//setting the fragmentState to SETUP_SCREEN
        getQuestionData();//calling method for again Performing the VolleyRequest for data Fetch
    }

}

