package com.example.mcqquizapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mcqquizapp.util.Utils;
import com.example.mcqquizapp.model.QuizzyViewModel;
import com.example.mcqquizapp.R;

/**
 * MainActivity hosting all the fragment for the app and targets app to be a one activity app.
 */
public class MainActivity extends AppCompatActivity {

    private final FragmentSetupScreen fragmentSetupScreen = new FragmentSetupScreen();
    private final FragmentSplashScreen fragmentSplashScreen = new FragmentSplashScreen();
    private final FragmentQuestionsListScreen fragmentQuestionsListScreen = new FragmentQuestionsListScreen();
    private final FragmentQuestionDetailsScreen fragmentQuestionDetailsScreen = new FragmentQuestionDetailsScreen();
    private final FragmentSummaryScreen fragmentSummaryScreen = new FragmentSummaryScreen();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//Hiding the ActionBar
        Log.d("MainActivity", "Activity created");

        //initialising the ViewModelProvider
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        //initialising the custom ViewModel Class
        Utils.viewModelClass = viewModelProvider.get(QuizzyViewModel.class);

        //setting the Observer for the Various Fragments Screen
        Observer<String> fragmentStateObserver = fragmentState -> {
            switch (fragmentState) {
                case "SPLASH_SCREEN":
                    startSplashScreen();//calling method to show SplashScreen fragment
                    break;
                case "SETUP_SCREEN":
                    startSetupScreen();//calling method to show SetupScreen fragment
                    break;
                case "QUESTIONLIST_SCREEN":
                    startQuestionListScreen();//calling method to show QuestionListScreen fragment
                    break;
                case "QUESTIONDETAIL_SCREEN":
                    startQuestionDetailsScreen();//calling method to show QuestionDetailsScreen fragment
                    break;
                case "SUMMARY_SCREEN":
                    startSummaryScreen();//calling method to show SummaryScreen fragment
                    break;
            }
            Log.d("MainActivity", "Fragment - " + fragmentState + "Active");
        };
        Utils.viewModelClass.getFragmentState().observe(this, fragmentStateObserver);
    }


    //For setting the splashscreen over main activity.
    private void startSplashScreen() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentSplashScreen).commit();
        Log.d("MainActivity", "SplashScreen Successful");
        getSupportActionBar().setTitle("");
    }

    //For replacing the splashscreen with setupScreen on main activity after 1.5 second.
    private void startSetupScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSetupScreen).commit();
        Log.d("MainActivity", "SetupScreen Successful");
    }


    //For setting the QuestionListScreen on clicking the start button of the setupScreen.
    private void startQuestionListScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentQuestionsListScreen).commit();
        Log.d("MainActivity", "QuestionListScreen Successful");
    }

    //For setting the QuestionDetailsScreen on clicking the item on the recyclerView List of QuestionListScreen
    private void startQuestionDetailsScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentQuestionDetailsScreen).addToBackStack(null).commit();
        Log.d("MainActivity", "QuestionDetailsScreen Successful");
    }

    //For setting the SummaryScreen on clicking the Submit button of the QuestionDetails and QuestionList Screen
    private void startSummaryScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSummaryScreen).commit();
        Log.d("QuestionListScreen", "Successful");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Utils.viewModelClass = null;
        Log.d("MainActivity", "MainActivity is Destroyed");
        super.onDestroy();
    }
}